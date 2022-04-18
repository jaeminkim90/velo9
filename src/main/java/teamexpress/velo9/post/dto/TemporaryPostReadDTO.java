package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.post.domain.TemporaryPost;

@Data
public class TemporaryPostReadDTO {

	private String title;
	private String content;

	public TemporaryPostReadDTO(TemporaryPost temporaryPost) {
		this.title = temporaryPost.getTitle();
		this.content = temporaryPost.getContent();
	}
}
