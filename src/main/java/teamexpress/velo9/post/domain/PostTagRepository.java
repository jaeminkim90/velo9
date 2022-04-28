package teamexpress.velo9.post.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	void deleteAllByPost(Post post);

	@Query("select pt from post_tag pt join fetch pt.tag where pt.post = :post")
	List<PostTag> findByPost(@Param("post") Post post);

	@Query("select pt from post_tag pt left join fetch pt.tag where pt.post.id in (:postIds)")
	List<PostTag> findByPostIds(@Param("postIds") List<Long> postIds);

	@Query("select distinct pt.tag.id from post_tag pt")
	List<Long> findTagIds();
}
