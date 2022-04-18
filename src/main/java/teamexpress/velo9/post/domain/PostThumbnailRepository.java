package teamexpress.velo9.post.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostThumbnailRepository extends JpaRepository<PostThumbnail, Long> {
	@Query(value = "select * from post_thumbnail where path = date_format(sysdate() - interval 1 day,'%Y\\%m\\%d')", nativeQuery = true)
	List<PostThumbnail> getOldFiles();
}
