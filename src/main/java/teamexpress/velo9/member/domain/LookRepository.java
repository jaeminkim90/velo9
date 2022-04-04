package teamexpress.velo9.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamexpress.velo9.post.domain.Post;

public interface LookRepository extends JpaRepository<Look, Long> {
	Optional<Look> findByPostAndMember(Post post, Member member);

	int countByPost(Post post);

	@Query("delete from Look l where l.post.id = :id")
	@Modifying
	void deleteByPostId(@Param("id") Long id);
}
