package teamexpress.velo9.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberThumbnail;

@Data
public class MemberDTO {

	private String username;
	private String nickname;
	@JsonIgnore
	private String password;
	private String email;
	private String introduce;
	private String blogTitle;
	private String socialEmail;
	private String socialGithub;
	private ThumbnailResponseDTO thumbnail;

	public MemberDTO(Member member) {
		this.username = member.getUsername();
		this.nickname = member.getNickname();
		this.password = member.getPassword();
		this.email = member.getEmail();
		this.introduce = member.getIntroduce();
		this.blogTitle = member.getBlogTitle();
		this.socialEmail = member.getSocialEmail();
		this.socialGithub = member.getSocialGithub();
		this.thumbnail = makeThumbnail(member.getMemberThumbnail());
	}

	private ThumbnailResponseDTO makeThumbnail(MemberThumbnail memberThumbnail) {
		ThumbnailResponseDTO result = null;

		if (memberThumbnail != null) {
			result = new ThumbnailResponseDTO(
				new MemberThumbnailDTO(memberThumbnail)
					.getSFileNameWithPath());
		}

		return result;
	}
}
