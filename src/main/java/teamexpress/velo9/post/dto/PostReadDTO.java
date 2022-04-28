package teamexpress.velo9.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostThumbnail;

@Data
public class PostReadDTO {

	private Long id;
	private String title;
	private String introduce;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
	private LocalDateTime createdDate;
	private ThumbnailResponseDTO thumbnail;
	private List<String> tags;

	public PostReadDTO(Post post, List<PostTag> postTagList) {
		id = post.getId();
		title = post.getTitle();
		introduce = post.getIntroduce();
		createdDate = post.getCreatedDate();
		thumbnail = makeThumbnail(post.getPostThumbnail());
		tags = postTagList.stream()
			.filter(postTag -> post.getId().equals(postTag.getPost().getId()))
			.map(postTag -> postTag.getTag().getName())
			.collect(Collectors.toList());
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
