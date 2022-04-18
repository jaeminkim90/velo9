package teamexpress.velo9.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberThumbnail;

@Getter
@NoArgsConstructor
public class MemberHeaderDTO {

	private Long id;
	private String nickname;
	private String blogTitle;
	private ThumbnailResponseDTO thumbnail;

	public MemberHeaderDTO(Member member) {
		this.id = member.getId();
		this.nickname = member.getNickname();
		this.blogTitle = member.getBlogTitle();
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
