package teamexpress.velo9.post.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.Series;

@Data
public class SeriesDTO {

	public static final int SIZE = 3;

	private Long seriesId;
	private String seriesName;
	private List<PostSummaryDTO> posts;

	public SeriesDTO(Series series, List<Post> postList) {
		seriesId = series.getId();
		seriesName = series.getName();
		posts = postList.stream()
			.map(PostSummaryDTO::new)
			.collect(Collectors.toList());
	}
}
