package teamexpress.velo9.post.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
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
import org.springframework.test.web.servlet.MockMvc;
import teamexpress.velo9.config.RestDocsConfig;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import(RestDocsConfig.class)
public class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void mainPage() throws Exception {

		mockMvc.perform(get("/")
				.param("tagSelect", "false")
				.param("content", "test")
				.param("page", "0")
				.param("sortValue", "old")
			)
			.andExpect(status().isOk())
			.andDo(document("main",
				requestParameters(
					parameterWithName("tagSelect").description("태그 검색 모드인지를 의미합니다.").optional(),
					parameterWithName("content").description("검색 키워드 입니다.").optional(),
					parameterWithName("page").description("원하는 페이지 수보다 1작은 값입니다.").optional(),
					parameterWithName("sortValue").description("어떤 순서로 볼지 결정 하는 부분입니다.").optional()
				),
				relaxedResponseFields(
					fieldWithPath("content").description("-").optional(),
					fieldWithPath("last").description("-").optional(),
					fieldWithPath("totalElements").description("numberOfElements와 같습니다.").optional(),
					fieldWithPath("totalPages").description("총 페이지 수").optional(),
					fieldWithPath("number").description("-").optional(),
					fieldWithPath("first").description("-").optional(),
					fieldWithPath("numberOfElements").description("-").optional(),
					fieldWithPath("empty").description("-").optional()
				)
			));
	}
}
