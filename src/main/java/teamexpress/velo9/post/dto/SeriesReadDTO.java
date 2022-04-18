package teamexpress.velo9.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import teamexpress.velo9.post.domain.Series;

@Data
@NoArgsConstructor
public class SeriesReadDTO {

	private Long seriesId;
	private String seriesName;

	public SeriesReadDTO(Series series) {
		this.seriesId = series.getId();
		this.seriesName = series.getName();
	}
}
