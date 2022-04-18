package teamexpress.velo9.post.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series, Long>, SeriesRepositoryCustom {
	@Query("select s from Series s where s.member.id = :memberId")
	List<Series> findByMember(@Param("memberId") Long memberId);

	@Query("select distinct s from Series s join s.member m on s.member.id = m.id"
		+ " where m.nickname = :nickname")
	List<Series> findUsedSeries(@Param("nickname") String nickname);
}
