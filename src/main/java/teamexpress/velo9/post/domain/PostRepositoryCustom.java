package teamexpress.velo9.post.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import teamexpress.velo9.post.dto.SearchCondition;

public interface PostRepositoryCustom {
	Slice<Post> findPost(String nickname, String tagName, Pageable pageable);
	Page<Post> search(SearchCondition condition, Pageable pageable);
	Slice<Post> findByJoinLove(Long memberId, Pageable pageable);
	Slice<Post> findByJoinLook(Long memberId, Pageable pageable);
	Slice<Post> findByJoinSeries(String nickname, String seriesName, Pageable pageable);
	List<Post> findPrevNextPost(Post findPost);
	Optional<Post> findWritePost(Long id);
	List<Post> findPostByIds(List<Long> seriesIds);
}
