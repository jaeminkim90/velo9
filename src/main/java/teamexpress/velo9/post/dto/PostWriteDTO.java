package teamexpress.velo9.post.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostThumbnail;

@Data
public class PostWriteDTO {

	private Long postId;
	private String title;
	private String introduce;
	private String content;
	private String access;

	private SeriesReadDTO series;
	private List<String> tags;

	private ThumbnailResponseDTO thumbnail;
	private TemporaryPostReadDTO temporary;

	public PostWriteDTO(Post post, List<PostTag> postTags) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.introduce = post.getIntroduce();
		this.content = post.getContent();

		if (post.getAccess() != null) {
			this.access = post.getAccess().name();
		}

		if (post.getSeries() != null) {
			this.series = new SeriesReadDTO(post.getSeries());
		}

		this.tags = postTags.stream()
			.map(postTag -> postTag.getTag().getName())
			.collect(Collectors.toList());

		this.thumbnail = makeThumbnail(post.getPostThumbnail());

		if (post.getTemporaryPost() != null) {
			this.temporary = new TemporaryPostReadDTO(post.getTemporaryPost());
		}
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
