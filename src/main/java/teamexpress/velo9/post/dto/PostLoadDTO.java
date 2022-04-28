package teamexpress.velo9.post.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostAccess;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostThumbnail;
import teamexpress.velo9.post.domain.Series;
import teamexpress.velo9.post.domain.TemporaryPost;

@Data
public class PostLoadDTO {

	private Long postId;
	private String title;
	private String introduce;
	private String content;
	private String access;

	private SeriesReadDTO series;
	private List<String> tags;

	private ThumbnailResponseDTO thumbnail;
	private TemporaryPostReadDTO temporary;

	public PostLoadDTO(Post post, List<PostTag> postTags) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.introduce = post.getIntroduce();
		this.content = post.getContent();
		this.access = makeAccess(post.getAccess());
		this.series = makeSeries(post.getSeries());
		this.tags = makeTags(postTags);
		this.thumbnail = makeThumbnail(post.getPostThumbnail());
		this.temporary = makeTemporary(post.getTemporaryPost());
	}

	private String makeAccess(PostAccess access) {
		return access != null ? access.name() : null;
	}

	private SeriesReadDTO makeSeries(Series series) {
		return series != null ? new SeriesReadDTO(series) : null;
	}

	private List<String> makeTags(List<PostTag> postTags) {
		return postTags.stream()
			.map(postTag -> postTag.getTag().getName())
			.collect(Collectors.toList());
	}

	private ThumbnailResponseDTO makeThumbnail(PostThumbnail postThumbnail) {
		return postThumbnail != null ?
			new ThumbnailResponseDTO(new PostThumbnailDTO(postThumbnail).getSFileNameWithPath()) : null;
	}

	private TemporaryPostReadDTO makeTemporary(TemporaryPost temporaryPost) {
		return temporary != null ? new TemporaryPostReadDTO(temporaryPost) : null;
	}
}
