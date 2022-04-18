package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.post.domain.Series;

@Data
public class SeriesAddDTO {

	private String name;

	public Series toSeries(Member member) {
		return Series.builder()
			.name(this.name)
			.member(member)
			.build();
	}
}
