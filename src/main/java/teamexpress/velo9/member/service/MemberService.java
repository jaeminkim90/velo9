package teamexpress.velo9.member.service;

import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.api.MemberThumbnailFileUploader;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.member.domain.MemberThumbnail;
import teamexpress.velo9.member.domain.MemberThumbnailRepository;
import teamexpress.velo9.member.domain.Role;
import teamexpress.velo9.member.dto.FindInfoDTO;
import teamexpress.velo9.member.dto.MailDTO;
import teamexpress.velo9.member.dto.MemberDTO;
import teamexpress.velo9.member.dto.MemberEditDTO;
import teamexpress.velo9.member.dto.MemberHeaderDTO;
import teamexpress.velo9.member.dto.MemberNewPwDTO;
import teamexpress.velo9.member.dto.MemberSignupDTO;
import teamexpress.velo9.member.dto.MemberThumbnailDTO;
import teamexpress.velo9.member.dto.PasswordDTO;
import teamexpress.velo9.member.dto.SocialSignupDTO;
import teamexpress.velo9.member.security.oauth.SessionConst;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberThumbnailRepository memberThumbnailRepository;
	private final MemberThumbnailFileUploader uploader;

	@Transactional
	public void join(MemberSignupDTO signupDTO) {
		checkDuplicateMember(signupDTO.getUsername(), signupDTO.getNickname());
		encodePassword(signupDTO);
		Member member = signupDTO.toMember();
		memberRepository.save(member);
	}

	@Transactional
	public void editMember(Long memberId, MemberEditDTO memberEditDTO) {
		Member findMember = getMember(memberId);
		validateEditNickname(findMember, memberEditDTO.getNickname());
		changeMemberInfo(memberEditDTO, findMember);
	}

	@Transactional
	public void changePassword(Long memberId, PasswordDTO passwordDTO) {
		Member findMember = checkPasswordMember(passwordDTO, memberId);
		String encodedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
		findMember.changePassword(encodedPassword);
	}

	@Transactional
	public void withdraw(Long memberId, PasswordDTO passwordDTO) {
		Member findMember = checkPasswordMember(passwordDTO, memberId);
		memberRepository.delete(findMember);
	}

	@Transactional
	public void uploadThumbnail(MemberThumbnailDTO memberThumbnailDTO, Long memberId) {
		Member member = getMember(memberId);
		member.uploadThumbnail(memberThumbnailDTO.toMemberThumbnail());
	}

	@Transactional
	public void deleteThumbnail(Long memberId) {
		Member member = getMember(memberId);

		if (member.getMemberThumbnail() == null) {
			return;
		}

		member.uploadThumbnail(null);

		memberRepository.save(member);
	}

	public String findIdByEmail(FindInfoDTO findInfoDTO) {
		Member findMember = memberRepository.findByEmail(findInfoDTO.getEmail())
			.orElseThrow(() -> {
				throw new IllegalArgumentException("존재하지 않는 회원입니다.");
			});
		return findMember.getUsername();
	}

	public void findEmail(MailDTO mailDTO) {
		memberRepository.findByEmail(mailDTO.getEmail())
			.ifPresent(m -> {
				throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
			});
	}

	@Transactional
	public void joinSocial(SocialSignupDTO socialSignupDTO) {
		checkDuplicateMember(socialSignupDTO.getUsername(), socialSignupDTO.getNickname());

		Map<String, Object> attributes = getAttributes();

		String email = (String) attributes.get("email");
		String picture = getPicture(attributes);
		MemberThumbnail memberThumbnail = saveMemberThumbnail(picture);

		saveSocialMember(socialSignupDTO, email, memberThumbnail);
	}

	public Long findPw(FindInfoDTO findInfoDTO) {
		Member findMember = memberRepository.findByUsernameAndEmail(findInfoDTO.getUsername(), findInfoDTO.getEmail()).orElseThrow(() -> {
			throw new IllegalArgumentException("아이디 또는 이메일이 일치하지 않습니다.");
		});
		return findMember.getId();
	}

	@Transactional
	public void changeNewPw(MemberNewPwDTO memberNewPwDTO) {
		Member member = getMember(memberNewPwDTO.getMemberId());
		String encodedPassword = passwordEncoder.encode(memberNewPwDTO.getPassword());
		member.changePassword(encodedPassword);
	}

	private Member getMember(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		});
	}

	private void encodePassword(MemberSignupDTO signupDTO) {
		signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
	}

	private void checkDuplicateMember(String username, String nickname) {
		validateUsername(username);
		validateNickname(nickname);
	}

	public void validateUsername(String username) {
		memberRepository.findByUsername(username)
			.ifPresent(m -> {
				throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
			});
	}

	public void validateNickname(String nickname) {
		memberRepository.findByNickname(nickname)
			.ifPresent(m -> {
				throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
			});
	}

	public void validateEditNickname(Member findMember, String nickname) {
		if (!nickname.equals(findMember.getNickname())) {
			validateNickname(nickname);
		}
	}

	public boolean getMemberByEmail(HttpSession session) {
		Map<String, Object> attributes = getAttributes();
		String email = (String) attributes.get("email");
		Member member = memberRepository.findByEmail(email).orElse(null);
		saveSession(member, session);
		return isExistMember(member);
	}

	public MemberDTO getLoginMember(Long memberId) {
		Member member = getMember(memberId);
		return new MemberDTO(member);
	}

	private void changeMemberInfo(MemberEditDTO memberEditDTO, Member findMember) {
		findMember.edit(
			memberEditDTO.getNickname(), memberEditDTO.getIntroduce(),
			memberEditDTO.getBlogTitle(), memberEditDTO.getSocialEmail(),
			memberEditDTO.getSocialGithub());
	}

	private Member checkPasswordMember(PasswordDTO passwordDTO, Long memberId) {
		Member findMember = getMember(memberId);
		checkPassword(passwordDTO.getOldPassword(), findMember.getPassword());
		return findMember;
	}

	private void checkPassword(String oldPassword, String savedPassword) {
		if (!passwordEncoder.matches(oldPassword, savedPassword)) {
			throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
		}
	}

	private MemberThumbnail saveMemberThumbnail(String picture) {
		MemberThumbnailDTO memberThumbnailDTO = uploader.upload(picture);
		MemberThumbnail memberThumbnail = memberThumbnailDTO.toMemberThumbnail();

		memberThumbnailRepository.save(memberThumbnail);
		return memberThumbnail;
	}

	public MemberHeaderDTO getHeaderInfo(Long memberId) {
		return new MemberHeaderDTO(getMember(memberId));
	}

	private Map<String, Object> getAttributes() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2User principal = (OAuth2User) authentication.getPrincipal();
		return principal.getAttributes();
	}

	private String getPicture(Map<String, Object> attributes) {
		return attributes.get("picture") == null
			? (String) attributes.get("avatar_url") : (String) attributes.get("picture");
	}

	private void saveSocialMember(SocialSignupDTO socialSignupDTO, String email, MemberThumbnail memberThumbnail) {
		Member member = Member.builder()
			.username(socialSignupDTO.getUsername())
			.password(passwordEncoder.encode(socialSignupDTO.getPassword()))
			.nickname(socialSignupDTO.getNickname())
			.email(email)
			.role(Role.ROLE_USER)
			.memberThumbnail(memberThumbnail)
			.build();

		memberRepository.save(member);
	}

	private void saveSession(Member member, HttpSession session) {
		if (isExistMember(member)) {
			session.setAttribute(SessionConst.LOGIN_MEMBER, member.getId());
		}
	}

	private boolean isExistMember(Member member) {
		return member != null;
	}

	public void findEmailByFinPW(MailDTO mailDTO) {
		if (memberRepository.findByEmail(mailDTO.getEmail()).isEmpty()) {
			throw new IllegalArgumentException("이메일과 일치하는 회원이 존재하지 않습니다.");
		}
	}
}
