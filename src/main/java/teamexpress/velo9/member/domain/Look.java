package teamexpress.velo9.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamexpress.velo9.post.domain.Post;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Look {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "look_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public Look(Post post, Member member) {
		this.post = post;
		this.member = member;
	}
}
