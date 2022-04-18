package teamexpress.velo9.post.controller.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.config.RestDocsConfig;
import teamexpress.velo9.member.security.oauth.SessionConst;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import(RestDocsConfig.class)
public class SeriesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getList() throws Exception {

		mockMvc.perform(get("/getSeriesList")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l))
			.andExpect(status().isOk())
			.andDo(document("seriesList",
				relaxedResponseFields(
					fieldWithPath("data").description("시리즈의 id와 이름이 담긴 정보들이 있습니다.")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void addSeries() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/addSeries")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 1l)
				.content("{"
					+ "\n\"name\":\"loveyou\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("addSeries",
				requestFields(
					fieldWithPath("name").description("원하는 새 시리즈 이름 입력, 단 내부적으로 중복검사 합니다.")
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void deleteSeries() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/deleteSeries")
				.param("id", "10"))
			.andExpect(status().isOk())
			.andDo(document("deleteSeries",
				requestParameters(
					parameterWithName("id").description("삭제할 시리즈의 id")
				)
			));
	}
}
