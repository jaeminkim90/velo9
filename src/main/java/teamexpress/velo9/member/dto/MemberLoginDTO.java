package teamexpress.velo9.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberLoginDTO {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
