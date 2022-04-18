package teamexpress.velo9.member.dto;

import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberThumbnail;

@Data
public class MemberMainDTO {

	private String nickname;
	private ThumbnailResponseDTO memberThumbnail;

	public MemberMainDTO(Member member) {
		nickname = member.getNickname();
		memberThumbnail = makeThumbnail(member.getMemberThumbnail());
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
