package teamexpress.velo9.post.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

	@Query("select distinct t from Tag t left join post_tag pt on t.id = pt.tag.id left join Member m on pt.post.member.id = m.id "
		+ "where m.nickname = :nickname")
	List<Tag> findUsedTags(@Param("nickname") String nickname);

	@Query("select t.id from Tag t")
	List<Long> findIds();
}
