package teamexpress.velo9.post.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import teamexpress.velo9.post.dto.SearchCondition;

public interface PostRepositoryCustom {
	Slice<Post> findPost(String nickname, Pageable pageable);
	Page<Post> search(SearchCondition condition, Pageable pageable);
	Slice<Post> findByJoinLove(Long memberId, Pageable pageable);
	Slice<Post> findByJoinLook(Long memberId, Pageable pageable);
	Page<Post> findReadPost(Long postId);
	Slice<Post> findByJoinSeries(Long memberId, String seriesName, Pageable pageable);

	Post findPrevPost(Post findPost);
	Post findNextPost(Post findPost);

	List<Post> findPrevNextPost(Post findPost);
}
