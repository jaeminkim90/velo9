package teamexpress.velo9.member.dto;

import lombok.Data;

@Data
public class PasswordDTO {

	private String oldPassword;
	private String newPassword;
}
