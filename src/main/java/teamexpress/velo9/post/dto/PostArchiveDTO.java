package teamexpress.velo9.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import teamexpress.velo9.post.domain.Post;

@Data
public class PostArchiveDTO {

	private Long id;
	private String title;
	private String introduce;
	private String writerNickname;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH-mm-ss", timezone = "Asia/Seoul")
	private LocalDateTime createdDate;

	public PostArchiveDTO(Post post) {
		id = post.getId();
		title = post.getTitle();
		introduce = post.getIntroduce();
		writerNickname = post.getMember().getNickname();
		createdDate = post.getCreatedDate();
	}
}
