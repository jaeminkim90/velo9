package teamexpress.velo9.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import teamexpress.velo9.post.domain.Post;

import java.time.LocalDateTime;

@Data
public class TempSavedPostDTO {

	private Long id;
	private String title;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
	private LocalDateTime createdDate;

	public TempSavedPostDTO(Post post) {
		id = post.getId();
		title = post.getTitle();
		content = post.getContent();
		createdDate = post.getCreatedDate();
	}
}
