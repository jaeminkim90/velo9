package teamexpress.velo9.post.dto;

import java.time.LocalDateTime;
import lombok.Data;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostStatus;
import teamexpress.velo9.post.domain.TemporaryPost;

@Data
public class TemporaryPostWriteDTO {

	private Long postId;
	private String title;
	private String content;

	private Long alternativeId;

	public Post toPost(Member member, LocalDateTime createdDate) {
		return Post.builder()
			.id(this.postId)
			.title(this.title)
			.content(this.content)
			.status(PostStatus.TEMPORARY)
			.member(member)
			.createdDate(createdDate)
			.build();
	}

	public TemporaryPost toTemporaryPost() {
		return TemporaryPost.builder()
			.id(alternativeId)
			.title(this.title)
			.content(this.content)
			.build();
	}
}
