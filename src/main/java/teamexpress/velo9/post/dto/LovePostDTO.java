package teamexpress.velo9.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import teamexpress.velo9.post.domain.Post;

@Data
public class LovePostDTO {
	private String title;
	private String introduce;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
	private LocalDateTime createdDate;

	public LovePostDTO(Post post) {
		title = post.getTitle();
		introduce = post.getIntroduce();
		createdDate = post.getCreatedDate();
	}
}
