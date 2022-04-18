package teamexpress.velo9.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LookRepository extends JpaRepository<Look, Long>, LookRepositoryCustom{
	@Query("delete from Look l where l.post.id = :id")
	@Modifying
	void deleteByPostId(@Param("id") Long id);
}
