package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberThumbnail;
import teamexpress.velo9.member.dto.MemberThumbnailDTO;

@Data
public class PostMemberDTO {

	private String name;
	private String introduce;
	private String socialGithub;
	private String socialEmail;
	private ThumbnailResponseDTO thumbnail;

	public PostMemberDTO(Member member) {
		this.name = member.getNickname();
		this.introduce = member.getIntroduce();
		this.socialGithub = member.getSocialGithub();
		this.socialEmail = member.getSocialEmail();
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
