package teamexpress.velo9.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;
import teamexpress.velo9.member.domain.Member;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
@Builder
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;
	private String title;
	private String introduce;
	private String content;
	private int loveCount;
	private int viewCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "series_id")
	private Series series;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "post_thumbnail_id")
	private PostThumbnail postThumbnail;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "temporary_post_id")
	private TemporaryPost temporaryPost;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<PostTag> postTags = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private PostStatus status;

	@Enumerated(EnumType.STRING)
	private PostAccess access;

	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	public void edit(String title, String introduce, String content, String access, Series series, PostThumbnail postThumbnail) {
		this.title = title;
		this.introduce = introduce;
		this.content = content;
		this.access = makeAccess(access);
		this.createdDate = makeDate();
		this.status = PostStatus.GENERAL;
		this.series = series;
		this.postThumbnail = postThumbnail;
		this.temporaryPost = null;
	}

	private PostAccess makeAccess(String access) {
		return StringUtils.hasText(access) ? PostAccess.valueOf(access) : this.access;
	}

	private LocalDateTime makeDate() {
		return this.status == PostStatus.TEMPORARY ? LocalDateTime.now() : this.createdDate;
	}

	public void addViewCount() {
		this.viewCount++;
	}

	public void updateTempPost(TemporaryPost temporaryPost) {
		this.temporaryPost = temporaryPost;
	}

	public void updateLoveCount(int loveCount) {
		this.loveCount = loveCount;
	}
}
