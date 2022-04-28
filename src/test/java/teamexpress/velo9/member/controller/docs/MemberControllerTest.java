package teamexpress.velo9.member.controller.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.config.RestDocsConfig;
import teamexpress.velo9.member.security.oauth.SessionConst;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import(RestDocsConfig.class)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void login() throws Exception {
		this.mockMvc.perform(post("/login")
				.content("{\"username\": \"test\","
					+ " \n\"password\": \"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("login",
				requestFields(
					fieldWithPath("username").description("username"),
					fieldWithPath("password").description("password")
				)
			));
	}

	@Test
	void getHeaderInfo() throws Exception {
		this.mockMvc.perform(get("/getHeaderInfo")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
			)
			.andExpect(status().isOk())
			.andDo(document("header",
				relaxedResponseFields(
					fieldWithPath("id").description("memberId").optional(),
					fieldWithPath("nickname").description("nickname").optional(),
					fieldWithPath("blogTitle").description("blogTitle").optional(),
					fieldWithPath("thumbnail").description("thumbnail").optional()
				)
			));
	}

	@Test
	void logout() throws Exception {
		this.mockMvc.perform(get("/memberLogout"))
			.andExpect(status().isOk())
			.andDo(document("logout"));
	}

	@Test
	@Transactional
	@Rollback
	void addMember() throws Exception {
		this.mockMvc.perform(post("/signup")
				.content("{\"username\": \"username33\","
					+ " \n\"password\": \"password\","
					+ "\n\"nickname\": \"nickname33\","
					+ "\n\"email\": \"email3332@test.com\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("addMember",
				requestFields(
					fieldWithPath("username").description("username"),
					fieldWithPath("password").description("password"),
					fieldWithPath("nickname").description("nickname"),
					fieldWithPath("email").description("email")
				)
			));
	}

	@Test
	void GetEditMember() throws Exception {
		this.mockMvc.perform(get("/setting")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l))
			.andExpect(status().isOk())
			.andDo(document("GetEditMember",
				relaxedResponseFields(
					fieldWithPath("username").description("username"),
					fieldWithPath("nickname").description("nickname"),
					fieldWithPath("email").description("email 내용"),
					fieldWithPath("introduce").description("introduce 내용").optional(),
					fieldWithPath("blogTitle").description("blogTitle 내용"),
					fieldWithPath("socialEmail").description("socialEmail 내용").optional(),
					fieldWithPath("socialGithub").description("socialGithub 내용").optional(),
					fieldWithPath("thumbnail").description("섬네일 파일 이름").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void PostEditMember() throws Exception {
		this.mockMvc.perform(post("/setting")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 3l)
				.content("{"
					+ "\"nickname\": \"nickname\","
					+ "\"introduce\": \"introduce\","
					+ "\"blogTitle\": \"blogTitle\","
					+ "\"socialEmail\": \"socialEmail\","
					+ "\"socialGithub\": \"socialGithub\""
					+ "}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostEditMember",
				requestFields(
					fieldWithPath("nickname").description("바꿀 nickname(바꾸지 않더라도 기존 내용을 주긴 해야합니다.)"),
					fieldWithPath("introduce").description("바꿀 introduce(위 사항은 아래 모두 마찬가지입니다.)").optional(),
					fieldWithPath("blogTitle").description("바꿀 blogTitle"),
					fieldWithPath("socialEmail").description("바꿀 socialEmail").optional(),
					fieldWithPath("socialGithub").description("socialGithub 내용").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void changePassword() throws Exception {
		this.mockMvc.perform(post("/changePassword")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 3l)
				.content("{\"oldPassword\": \"1234\", \n\"newPassword\": \"0000\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostChangePw",
				requestFields(
					fieldWithPath("oldPassword").description("현재 비밀번호"),
					fieldWithPath("newPassword").description("새로운 비밀번호")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void withdraw() throws Exception {
		this.mockMvc.perform(post("/withdraw")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 3l)
				.content("{\"oldPassword\": \"1234\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostWithdraw",
				requestFields(
					fieldWithPath("oldPassword").description("이전 비밀번호")
				)
			));
	}

	@Test
	void sendMail() throws Exception {
		this.mockMvc.perform(post("/sendMail")
				.content("{\"email\": \"email2@test.com\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostSendMail",
				requestFields(
					fieldWithPath("email").description("회원가입시 입력했던 이메일로 인증번호가 전송됩니다.")
				)
			));
	}

	@Test
	void sendMailByFindPw() throws Exception {
		this.mockMvc.perform(post("/sendMailPw")
				.content("{\"email\": \"test1@nate.com\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostSendMailByFindPw",
				requestFields(
					fieldWithPath("email").description("회원가입시 입력했던 이메일로 인증번호가 전송됩니다. DB에 이메일이 존재하지 않으면 예외를 던집니다.")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void findId() throws Exception {
		this.mockMvc.perform(post("/findId")
				.content("{\"email\": \"test1@nate.com\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostFindId",
				requestFields(
					fieldWithPath("email").description("회원가입시 입력했던 이메일로 id(username)이 전송됩니다.")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void findPw() throws Exception {
		this.mockMvc.perform(post("/findPw")
				.content("{\"username\":\"test\",\"email\":\"test3@nate.com\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostFindPw",
				requestFields(
					fieldWithPath("username").description("회원가입시 입력했던 아이디"),
					fieldWithPath("email").description("회원가입시 입력했던 이메일")
				),
				responseFields(
					fieldWithPath("data").description("방금 찾은 회원의 id값입니다.")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void changePw() throws Exception {
		this.mockMvc.perform(post("/changePasswordAfterFindPW")
				.content("{\"memberId\":\"2\", \"password\": \"0000\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostChangePw2",
				requestFields(
					fieldWithPath("memberId").description("아까 /findPw로 찾아온 회원 id"),
					fieldWithPath("password").description("새로운 비밀번호")
				)
			));
	}

	@Test
	void checkNumber() throws Exception {

		this.mockMvc.perform(post("/certifyNumber")
				.sessionAttr(SessionConst.RANDOM_NUMBER, "000000")
				.content("{\"inputNumber\": \"000000\"}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("PostCheckNumber",
				requestFields(
					fieldWithPath("inputNumber").description("이 인증번호가 알맞아 비동기 호출이 성공해야만 "
						+ "changePasswordAfterFindPW를 진행하면 됩니다.")
				)
			));
	}


	@Test
	void checkUsername() throws Exception {
		this.mockMvc.perform(get("/validateUsername")
				.param("username", "testUser"))
			.andExpect(status().isOk())
			.andDo(document("UsernameCheck",
				requestParameters(
					parameterWithName("username").description("중복 확인이 필요한 username을 입력하세요")
				)
			));
	}

	@Test
	void checkNickname() throws Exception {
		this.mockMvc.perform(get("/validateNickname")
				.param("nickname", "testNickname"))
			.andExpect(status().isOk())
			.andDo(document("NicknameCheck",
				requestParameters(
					parameterWithName("nickname").description("중복 확인이 필요한 nickname을 입력하세요")
				)
			));
	}
}
