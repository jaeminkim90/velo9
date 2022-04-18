package teamexpress.velo9.member.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberEditDTO {

	@NotNull
	private String nickname;
	private String introduce;
	@NotNull
	private String blogTitle;
	private String socialEmail;
	private String socialGithub;
}
