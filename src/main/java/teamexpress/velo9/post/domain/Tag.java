package teamexpress.velo9.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamexpress.velo9.common.domain.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id;
	private String name;

	@OneToMany(mappedBy = "tag")
	@JsonIgnore
	private List<PostTag> postTags = new ArrayList<>();
}
