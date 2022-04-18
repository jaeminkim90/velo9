package teamexpress.velo9.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.Role;

@Data
@NoArgsConstructor
public class MemberSignupDTO {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String nickname;
	private String blogTitle;
	@NotBlank
	@Email
	private String email;
	private Role role;

	public MemberSignupDTO(Member member) {
		this.username = member.getUsername();
		this.password = member.getPassword();
		this.nickname = member.getNickname();
		this.blogTitle = member.getNickname();
		this.email = member.getEmail();
		this.role = member.getRole();
	}

	public Member toMember() {
		return Member.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.blogTitle(nickname)
			.email(email)
			.role(Role.ROLE_USER)
			.build();
	}
}
