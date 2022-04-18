package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.dto.MemberMainDTO;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostThumbnail;

@Data
public class PostMainDTO {

	private Long postId;
	private String title;
	private String introduce;
	private int loveCount;
	private MemberMainDTO member;
	private ThumbnailResponseDTO postThumbnail;

	public PostMainDTO(Post post) {
		postId = post.getId();
		title = post.getTitle();
		introduce = post.getIntroduce();
		loveCount = post.getLoveCount();
		member = new MemberMainDTO(post.getMember());
		postThumbnail = makeThumbnail(post.getPostThumbnail());
	}

	private ThumbnailResponseDTO makeThumbnail(PostThumbnail postThumbnail) {
		ThumbnailResponseDTO result = null;

		if (postThumbnail != null) {
			result = new ThumbnailResponseDTO(
				new PostThumbnailDTO(postThumbnail)
					.getSFileNameWithPath());
		}

		return result;
	}
}
