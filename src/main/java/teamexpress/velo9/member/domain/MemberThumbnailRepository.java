package teamexpress.velo9.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberThumbnailRepository extends JpaRepository<MemberThumbnail, Long> {
	@Query(value = "select * from member_thumbnail where path = date_format(sysdate() - interval 1 day,'%Y\\%m\\%d')", nativeQuery = true)
	List<MemberThumbnail> getOldFiles();
}
