package teamexpress.velo9.post.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SeriesRepositoryCustom {
	Slice<Series> findPostBySeriesName(String nickname, Pageable pageable);
}
