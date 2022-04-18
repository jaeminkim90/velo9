package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostThumbnail;

@Data
public class PostSummaryDTO {

	private Long postId;
	private String title;
	private String introduce;
	private ThumbnailResponseDTO thumbnail;

	public PostSummaryDTO(Post post) {
		postId = post.getId();
		title = post.getTitle();
		introduce = post.getIntroduce();
		thumbnail = makeThumbnail(post.getPostThumbnail());
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
