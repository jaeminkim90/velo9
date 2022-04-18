package teamexpress.velo9.post.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamexpress.velo9.common.domain.BaseEntity;

@Entity(name = "post_thumbnail")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostThumbnail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_thumbnail_id")
	private Long id;
	private String uuid;
	private String name;
	private String path;
}
