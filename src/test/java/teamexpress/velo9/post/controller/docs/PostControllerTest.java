package teamexpress.velo9.post.controller.docs;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void writeGet() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/write")
				.param("id", "1"))
			.andExpect(status().isOk())
			.andDo(document("writeGet",
				requestParameters(
					parameterWithName("id").description("새 글 작성 시 id 값 없이 url을 호출하면 됩니다."
						+ "글 수정 시 존재하는 postId를 담아 호출하시길 바랍니다.").optional()
				),
				relaxedResponseFields(
					fieldWithPath("postId").description("작성중인 게시글의 id 입니다. post 방식으로 넘어갈 때 활용할 수 있습니다.").optional(),
					fieldWithPath("title").description("제목"),
					fieldWithPath("introduce").description("소개글"),
					fieldWithPath("content").description("본문"),
					fieldWithPath("access").description("공개설정"),
					fieldWithPath("series").description("속해있는 시리즈의 정보입니다.").optional(),
					fieldWithPath("tags").description("달려있는 태그의 이름들 입니다.").optional(),
					fieldWithPath("thumbnail").description("썸네일 파일 이름 관련 정보가 들어 있을 수도 있습니다.").optional(),
					fieldWithPath("temporary").description("임시저장된 제목과 내용이 들어 있을 수도 있습니다.").optional()
				)
			));
	}

	@Test
	void series() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/series", "admin"))
			.andExpect(status().isOk())
			.andDo(document("GetSeries", pathParameters(
					parameterWithName("nickname").description("유효한 회원의 닉네임을 입력해주세요")
				),
				relaxedResponseFields(
					fieldWithPath("data.content").description("시리즈네임, id와 최대 3개의 게시글들로 이루어져 있습니다.").optional(),
					fieldWithPath("data.size").description("한 페이지당 시리즈 개수입니다.").optional(),
					fieldWithPath("data.number").description("현재 페이지입니다.").optional(),
					fieldWithPath("data.first").description("첫 페이지인지 여부입니다.").optional(),
					fieldWithPath("data.last").description("끝 페이지인지 여부입니다.").optional(),
					fieldWithPath("data.numberOfElements").description("총 시리즈 개수입니다.").optional(),
					fieldWithPath("data.empty").description("내용이 없는지 여부입니다.").optional(),
					fieldWithPath("subData").description("시리즈 네임 리스트가 들어가있습니다.(좌측 nav활용)").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void writePost() throws Exception {

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":1,"
					+ "\n\"title\":\"testtest\","
					+ "\n\"introduce\":\"1\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"seriesId\":1,"
					+ "\n\"tags\":[\"A\",\"B\"],"
					+ "\n\"thumbnailFileName\":\"2020/03/01/s_uuid_name.png\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("writePost",
				relaxedRequestFields(
					fieldWithPath("postId").description("해당 필드가 없으면(null) 새 글(또는 임시글)이 작성됩니다.\n"
						+ "해당 필드가 있으면 기존에 존재하는 글(또는 임시글)의 수정(덮어쓰기)가 됩니다.").optional(),
					fieldWithPath("title").description("제목"),
					fieldWithPath("introduce").description("새 글작성일 때 introduce가 비어있을 경우 content의 150자 이내가 소개글이 됩니다.\n").optional(),
					fieldWithPath("content").description("본문내용"),
					fieldWithPath("access").description("PUBLIC(전체공개), PRIVATE(비공개) 중에서 적습니다.\n"
						+ "적지 않으면 새 글의 경우 public이 들어가고, 수정의 경우 유지가 됩니다."),
					fieldWithPath("seriesId").description("상세조건에서 시리즈를 선택하면 선택된 시리즈Object내부의 id값을 의미합니다.").optional(),
					fieldWithPath("tags").description("태그를 입력했다면, array로([]) 주십시오").optional(),
					fieldWithPath("thumbnailFileName").description("업로드나, 게시글 볼 때 반환되는 썸네일 오브젝트를 변수로 가지고 있다가\n"
						+ "글이 작성될 때 Object.fileName을 주세요").optional()
				),
				responseFields(
					fieldWithPath("data").description("방금 작성(수정)된 글의 id 입니다. 작성(수정)후에는 작성된 게시글 상세보기로 가야합니다.")
				)
			));
	}

	@Test
	void seriesPost() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/series/{seriesName}", "admin", "series1")
				.param("page", "0")
				.param("sortCondition", "createdDate"))
			.andExpect(status().isOk())
			.andDo(document("GetSeriesPost",
				requestParameters(
					parameterWithName("page").description("페이지 번호에서 1뺀 값입니다.").optional(),
					parameterWithName("sortCondition").description("최신순(createDate), 오래된순(old) 중에 입력하세요. "
						+ "없으면 최신순으로 동작합니다.").optional()
				),
				pathParameters(
					parameterWithName("nickname").description("유효한 회원 닉네임").optional(),
					parameterWithName("seriesName").description("유효한 시리즈 이름").optional()
				),
				relaxedResponseFields(
					fieldWithPath("content").description("해당 시리즈에 포함된 게시글들의 정보가 있습니다.").optional(),
					fieldWithPath("number").description("-").optional(),
					fieldWithPath("first").description("-").optional(),
					fieldWithPath("last").description("-").optional(),
					fieldWithPath("numberOfElements").description("-").optional(),
					fieldWithPath("empty").description("-").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void writeTemporary() throws Exception {

		mockMvc.perform(post("/writeTemporary")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":2,"
					+ "\n\"title\":\"testTMP\","
					+ "\n\"content\":\"TMPTMP\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("writeTemporaryPost",
				requestFields(
					fieldWithPath("postId").description("id가 없으면 새 글을 임시저장하는 경우(임시글목록에 보임)이며,"
						+ " id가 있으면 기존 임시글의 덮어쓰기(임시글목록에 보임) 또는"
						+ "기존 게시글의 대안 임시글을 생성(임시글목록에는 안보이고 수정화면에서 임시데이터가 있으면 불러오는 창나옴)"
						+ "하는 경우입니다.").optional(),
					fieldWithPath("title").description("title"),
					fieldWithPath("content").description("content")
				),
				responseFields(
					fieldWithPath("data").description("임시저장이 되었을 때 해당 글의 id를 반환합니다."
						+ "\n최초에 타이머에 의해 임시저장을 하고나면 그 다음 타이머에의한 임시저장은 \n"
						+ "id가 없는 새 글이 아니라 id가 있는 기존 글의 수정이어야 하기 때문에\n"
						+ "두 번째 타이머 이벤트 부터는 이 id 값을 가지고 url호출을 하면 좋을 것 같습니다.")
				)
			));
	}

	@Test
	void postsRead() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/main", "admin")
				.param("page", "0"))
			.andExpect(status().isOk())
			.andDo(document("GetPostsRead",
				pathParameters(
					parameterWithName("nickname").description("유효한 닉네임")
				),
				requestParameters(
					parameterWithName("page").description("원하는 페이지 보다 1작은 값, 없으면 첫페이지").optional()
				),
				relaxedResponseFields(
					fieldWithPath("data.content").description("게시글 관련 정보들이 여러 개 있습니다.(제목, 댓글수, 썸네일, 태그 등)").optional(),
					fieldWithPath("data.size").description("-").optional(),
					fieldWithPath("data.number").description("-").optional(),
					fieldWithPath("data.first").description("-").optional(),
					fieldWithPath("data.last").description("-").optional(),
					fieldWithPath("data.numberOfElements").description("-").optional(),
					fieldWithPath("data.empty").description("-").optional(),
					fieldWithPath("subData").description("시리즈 네임 리스트가 들어가있습니다.").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void delete() throws Exception {

		mockMvc.perform(post("/delete")
				.param("id", "1"))
			.andExpect(status().isOk())
			.andDo(document("delete",
				requestParameters(
					parameterWithName("id").description("삭제할 게시글의 id 입니다.")
				)
			));
	}

	@Test
	void tempPostsRead() throws Exception {
		this.mockMvc.perform(get("/temp")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l))
			.andExpect(status().isOk())
			.andDo(document("GetTemp",
				relaxedResponseFields(
					fieldWithPath("data").description("임시글 목록에 나올 게시글들의 요약정보들이 들어있습니다.").optional()
				)
			));
	}

	@Test
	void lovePostRead() throws Exception {
		this.mockMvc.perform(get("/archive/like")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l))
			.andExpect(status().isOk())
			.andDo(document("GetLovePostRead",
				relaxedResponseFields(
					fieldWithPath("content").description("좋아요를 누른 게시글의 요약 정보들이 들어있습니다.").optional(),
					fieldWithPath("number").description("-").optional(),
					fieldWithPath("first").description("-").optional(),
					fieldWithPath("last").description("-").optional(),
					fieldWithPath("numberOfElements").description("-").optional(),
					fieldWithPath("empty").description("-").optional()
				)
			));
	}

	@Test
	void lookPostRead() throws Exception {
		this.mockMvc.perform(get("/archive/recent")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l))
			.andExpect(status().isOk())
			.andDo(document("GetLookPostRead",
				relaxedResponseFields(
					fieldWithPath("content").description("최근 읽은 게시글의 요약 정보들이 들어있습니다.").optional(),
					fieldWithPath("number").description("-").optional(),
					fieldWithPath("first").description("-").optional(),
					fieldWithPath("last").description("-").optional(),
					fieldWithPath("numberOfElements").description("-").optional(),
					fieldWithPath("empty").description("-").optional()
				)
			));
	}

	@Test
	@Transactional
	@Rollback
	void love() throws Exception {
		mockMvc.perform(post("/love")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":1"
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("love",
				requestFields(
					fieldWithPath("postId").description("좋아요 누른 게시글 id")
				)
			));
	}

	@Test
	void readPost() throws Exception {

		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/read/{postId}", "admin", "2")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 3l))
			.andExpect(status().isOk())
			.andDo(document("GetReadPost",
				pathParameters(
					parameterWithName("nickname").description("유효한 닉네임"),
					parameterWithName("postId").description("상세보기할 게시글")
				),
				relaxedResponseFields(
					fieldWithPath("title").description("제목"),
					fieldWithPath("seriesName").description("소개글").optional(),
					fieldWithPath("content").description("본문"),
					fieldWithPath("loveCount").description("소개글"),
					fieldWithPath("createdDate").description("작성날짜"),
					fieldWithPath("memberInfo").description("게시글 하단 프로필 정보에 들어갈 데이터들 입니다."),
					fieldWithPath("tags").description("달려있는 태그의 이름들 입니다.").optional(),
					fieldWithPath("prevPost").description("이전 게시글 정보입니다.").optional(),
					fieldWithPath("nextPost").description("다음 게시글 정보입니다.").optional()
				)
			));
	}
}

