package teamexpress.velo9.post.dto;

import lombok.Getter;
import teamexpress.velo9.post.domain.Post;

@Getter
public class TransferDTO {

	private Long id;
	private String title;

	public TransferDTO(Post post) {
		id = post.getId();
		title = post.getTitle();
	}
}
