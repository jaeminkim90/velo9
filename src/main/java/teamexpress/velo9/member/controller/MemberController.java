package teamexpress.velo9.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamexpress.velo9.common.controller.BaseController;
import teamexpress.velo9.common.domain.Result;
import teamexpress.velo9.member.dto.FindInfoDTO;
import teamexpress.velo9.member.dto.MailDTO;
import teamexpress.velo9.member.dto.MemberDTO;
import teamexpress.velo9.member.dto.MemberEditDTO;
import teamexpress.velo9.member.dto.MemberHeaderDTO;
import teamexpress.velo9.member.dto.MemberNewPwDTO;
import teamexpress.velo9.member.dto.MemberSignupDTO;
import teamexpress.velo9.member.dto.NumberDTO;
import teamexpress.velo9.member.dto.PasswordDTO;
import teamexpress.velo9.member.dto.RandomNumber;
import teamexpress.velo9.member.dto.SocialSignupDTO;
import teamexpress.velo9.member.security.oauth.SessionConst;
import teamexpress.velo9.member.service.MailService;
import teamexpress.velo9.member.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController extends BaseController {

	public static final int INTERVAL = 180;
	private final MemberService memberService;
	private final MailService mailService;

	@GetMapping("/getHeaderInfo")
	public MemberHeaderDTO getHeaderInfo(HttpSession session) {
		return memberService.getHeaderInfo(getMemberId(session));
	}

	@PostMapping("/signup")
	public void addMember(@Valid @RequestBody MemberSignupDTO memberSignupDTO) {
		memberService.join(memberSignupDTO);
	}

	@PostMapping("/sendMail")
	public void sendMail(@Valid @RequestBody MailDTO mailDTO, HttpSession session) {
		memberService.findEmail(mailDTO);
		sendRandomNumber(mailDTO, session);
	}

	@PostMapping("/sendMailPw")
	public void sendMailByFindPw(@Valid @RequestBody MailDTO mailDTO, HttpSession session) {
		memberService.findEmailByFinPW(mailDTO);
		sendRandomNumber(mailDTO, session);
	}

	@PostMapping("/certifyNumber")
	public void checkNumber(@RequestBody NumberDTO numberDTO, HttpSession session) {
		checkInputNumber(numberDTO, session);
	}

	@GetMapping("/setting")
	public MemberDTO editMember(HttpSession session) {
		return memberService.getLoginMember(getMemberId(session));
	}

	@PostMapping("/setting")
	public void editMember(@RequestBody MemberEditDTO memberEditDTO, HttpSession session) {
		memberService.editMember(getMemberId(session), memberEditDTO);
	}

	@PostMapping("/changePassword")
	public void changePassword(@RequestBody PasswordDTO passwordDTO, HttpSession session) {
		memberService.changePassword(getMemberId(session), passwordDTO);
	}

	@PostMapping("/withdraw")
	public void withdrawMember(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
		memberService.withdraw(getMemberId(request.getSession()), passwordDTO);
		new SecurityContextLogoutHandler().logout(request, null, null);
	}

	@PostMapping("/socialSignup")
	public void socialSignup(@Valid @RequestBody SocialSignupDTO socialSignupDTO, HttpServletRequest request) {
		memberService.joinSocial(socialSignupDTO);
		new SecurityContextLogoutHandler().logout(request, null, null);
	}

	@PostMapping("/findId")
	public void findId(@Valid @RequestBody FindInfoDTO findInfoDTO) {
		String findUsername = memberService.findIdByEmail(findInfoDTO);
		mailService.sendMailFindId(findInfoDTO, findUsername);
	}

	@PostMapping("/findPw")
	public Result findPw(@Valid @RequestBody FindInfoDTO findInfoDTO, HttpSession session) {
		String randomNumber = getNumber();
		Long memberId = memberService.findPw(findInfoDTO);
		mailService.sendNumberMail(findInfoDTO.getEmail(), randomNumber);
		session.setAttribute(SessionConst.RANDOM_NUMBER, randomNumber);
		session.setMaxInactiveInterval(INTERVAL);
		return new Result(memberId);
	}

	@PostMapping("/changePasswordAfterFindPW")
	public void changePw(@RequestBody MemberNewPwDTO memberNewPwDTO) {
		memberService.changeNewPw(memberNewPwDTO);
	}

	@GetMapping("/memberLogout")
	public void logout(HttpServletRequest request) {
		new SecurityContextLogoutHandler().logout(request, null, null);
	}

	@GetMapping("/validateUsername")
	public void checkUsername(@RequestParam String username) {
		memberService.validateUsername(username);
	}

	@GetMapping("/validateNickname")
	public void checkNickname(@RequestParam String nickname) {
		memberService.validateNickname(nickname);
	}

	@GetMapping("/denied")
	public void denied() {
	}

	private String getNumber() {
		RandomNumber randomNumber = RandomNumber.get();
		return randomNumber.getNumber();
	}

	private void checkInputNumber(NumberDTO numberDTO, HttpSession session) {
		String certificationNumber = (String) session.getAttribute(SessionConst.RANDOM_NUMBER);
		if (!isEquals(certificationNumber, numberDTO)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
	}

	private boolean isEquals(String certificationNumber, NumberDTO numberDTO) {
		return certificationNumber.equals(numberDTO.getInputNumber());
	}

	private void sendRandomNumber(MailDTO mailDTO, HttpSession session) {
		String randomNumber = getNumber();
		mailService.sendNumberMail(mailDTO.getEmail(), randomNumber);
		session.setAttribute(SessionConst.RANDOM_NUMBER, randomNumber);
		session.setMaxInactiveInterval(INTERVAL);
	}
}
