package teamexpress.velo9.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialSignupDTO {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String nickname;
}
