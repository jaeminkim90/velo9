package teamexpress.velo9.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostAccess;
import teamexpress.velo9.post.domain.PostStatus;
import teamexpress.velo9.post.domain.PostThumbnail;
import teamexpress.velo9.post.domain.Series;

@Data
@NoArgsConstructor
public class PostSaveDTO {

	private static final int MAX_INTRODUCE_LENGTH = 150;
	private static final int FIRST_INDEX = 0;

	private Long postId;
	@NotBlank
	private String title;
	private String introduce;
	@NotBlank
	private String content;
	@NotBlank
	private String access;

	private Long seriesId;
	private List<String> tags;

	private String thumbnailFileName;

	public Post toPost(Member member, Series series, PostThumbnail postThumbnail) {
		setIntroduce();

		return Post.builder()
			.title(this.title)
			.introduce(this.introduce)
			.content(this.content)
			.status(PostStatus.GENERAL)
			.access(PostAccess.valueOf(this.access))
			.member(member)
			.series(series)
			.postThumbnail(postThumbnail)
			.build();
	}

	public void setIntroduce() {
		if (checkIntroduce()) {
			return;
		}
		if (smallerThanMax(this.content)) {
			this.introduce = this.content;
			return;
		}
		this.introduce = this.content.substring(FIRST_INDEX, MAX_INTRODUCE_LENGTH);
	}

	private boolean smallerThanMax(String content) {
		return content.length() < MAX_INTRODUCE_LENGTH;
	}

	private boolean checkIntroduce() {
		return StringUtils.hasText(this.introduce);
	}
}
