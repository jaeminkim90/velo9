package teamexpress.velo9.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailDTO {

	@Email(message = "이메일 형식에 맞지 않습니다.")
	@NotBlank(message = "이메일은 필수 값입니다.")
	private String email;
}
