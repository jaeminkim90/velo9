package teamexpress.velo9.post.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.security.oauth.SessionConst;
import teamexpress.velo9.post.domain.PostStatus;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private EntityManager em;

	//writeGET
	@Test
	void id에_null이_들어왔을_경우_새글작성이므로_null을_반환한다() throws Exception {
		mockMvc.perform(get("/write"))
			.andExpect(status().isOk())
			.andExpect(content().string(""));
	}

	@Test
	void 없는_게시글의_id가_들어왔을_경우_예외를_던진다() {
		Query query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();

		assertThatThrownBy(() ->
			mockMvc.perform(get("/write")
					.param("id", String.valueOf(postId + 1)))
				.andExpect(status().isOk())).isInstanceOf(Exception.class);

	}

	//writePOST - 새 글 작성
	@Test
	@Transactional
	@Rollback
	void 새글작성시_총글개수가_늘어난다() throws Exception {
		Query query = em.createNativeQuery("select COUNT(*) from post");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"title\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();

		assertThat(cntAfter == cntBefore + 1).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_제목_내용_공개설정이_비어있으면_에러가_발생한다() {

		//제목이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"content\":\"333333\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);

		//내용이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"title\":\"testtest\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);

		//공개설정이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"title\":\"testtest\","
						+ "\n\"content\":\"333333\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_소개글을_주지_않거나_공백일_경우_본문에서_일부분을_추출한다() throws Exception {

		//소개글 주지 않은 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		Query query = em.createNativeQuery("select MAX(post_id) from post");//방금 작성한 글의
		Long postId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);//소개글
		assertThat(query.getSingleResult().equals("333333")).isTrue();

		//빈 걸 준 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"introduce\":\"\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select MAX(post_id) from post");
		postId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals("333333")).isTrue();

		//공백 준 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"introduce\":\"         \","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select MAX(post_id) from post");
		postId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals("333333")).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_status는_무조건_GENERAL_tempPost_없다() throws Exception {
		Query query = em.createNativeQuery("select COUNT(*) from temporary_post");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select status from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals(PostStatus.GENERAL.name())).isTrue();

		query = em.createNativeQuery("select temporary_post_id from post where post_id = " + postId);
		assertThat(((BigInteger) query.getSingleResult())).isNull();

		query = em.createNativeQuery("select COUNT(*) from temporary_post");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_기존에_없는_태그만_태그테이블에_새로_추가된다() throws Exception {
		//전 태그개수
		Query query = em.createNativeQuery("select count(*) from tag");
		Long tagCnt = ((BigInteger) query.getSingleResult()).longValue();

		//있는 태그
		String existTag = "tag1";
		query = em.createNativeQuery("select count(*) from tag where name = ?").setParameter(1, existTag);
		long cnt = ((BigInteger) query.getSingleResult()).longValue();
		if (cnt != 1) {
			throw new IllegalStateException("있어야 하는데 없거나, 중복되는 태그이름이 있습니다.");
		}

		//없는 태그
		String notExistTag = "sirius";
		query = em.createNativeQuery("select count(*) from tag where name = ?").setParameter(1, notExistTag);
		cnt = ((BigInteger) query.getSingleResult()).longValue();
		if (cnt != 0) {
			throw new IllegalStateException("없어야 하는데 있는 태그이름이 있습니다.");
		}

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"tags\":[\"" + existTag + "\",\"" + notExistTag + "\"]"
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		//후 태그개수 - 전 태그개수
		query = em.createNativeQuery("select count(*) from tag");
		tagCnt = ((BigInteger) query.getSingleResult()).longValue() - tagCnt;

		assertThat(tagCnt == 1).isTrue();

		query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();
		query = em.createNativeQuery("select distinct name from post_tag inner join tag on post_tag.tag_id = tag.tag_id where post_tag.post_id = " + postId);
		assertThat(query.getResultList()).containsExactly("tag1", "sirius");
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_썸네일을_주지않으면_썸네일총개수는유지되며_데이터도들어가지않는다() throws Exception {
		Query query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore).isTrue();

		query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();
		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + postId);
		assertThat(query.getSingleResult()).isNull();
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_썸네일을주면_썸네일총개수가증가하며_데이터도_들어간다() throws Exception {
		Query query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"thumbnailFileName\":\"2020/03/01/s_uuid_name.png\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore + 1).isTrue();

		query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();
		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + postId);
		assertThat(query.getSingleResult()).isNotNull();
	}

	@Test
	@Transactional
	@Rollback
	void 새글작성시_멤버는_무조건_있어야한다() throws Exception {
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		Query query = em.createNativeQuery("select MAX(post_id) from post");
		Long postId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select member_id from post where post_id = " + postId);
		assertThat(query.getSingleResult()).isNotNull();

		assertThatThrownBy(() ->//로그인을 안한 경우에 예외가 발생한다.
			mockMvc.perform(post("/write")
					.content("{"
						+ "\n\"title\":\"testtest\","
						+ "\n\"content\":\"333333\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Exception.class);
	}

	//writePOST - 기존글 수정
	@Test
	@Transactional
	@Rollback
	void 글수정시_총글개수가_유지된다() throws Exception {
		Query query = em.createNativeQuery("select COUNT(*) from post");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","
					+ "\n\"title\":\"title\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();

		assertThat(cntAfter == cntBefore).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_제목_내용_공개설정이_비어있으면_에러가_발생한다() {

		//제목이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"postId\":\"1\","
						+ "\n\"content\":\"333333\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);

		//내용이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"postId\":\"1\","
						+ "\n\"title\":\"testtest\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);

		//공개설정이 없는 경우
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
					.content("{"
						+ "\n\"postId\":\"1\","
						+ "\n\"title\":\"testtest\","
						+ "\n\"content\":\"333333\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Error.class);
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_소개글을_주지_않거나_공백일_경우_기존것이_유지된다() throws Exception {

		Long postId = 1l;//수정할 글
		Query query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		String introBefore = (String) query.getSingleResult();

		//소개글 주지 않은 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\": " + postId + ","
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals("333333")).isTrue();

		//빈 걸 준 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"introduce\":\"\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals("333333")).isTrue();

		//공백 준 경우
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"introduce\":\"         \","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select introduce from post where post_id = " + postId);
		assertThat(query.getSingleResult().equals("333333")).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_status는_무조건_GENERAL_tempPost_없다() throws Exception {

		//temp가 있는 post는 temp테이블의 테이터 개수가 적어질 것이다.
		Query query = em.createNativeQuery("select COUNT(*) from temporary_post");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","//temp있는 경우 요구할 것
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select status from post where post_id = " + 1);
		assertThat(query.getSingleResult().equals(PostStatus.GENERAL.name())).isTrue();
		query = em.createNativeQuery("select temporary_post_id from post where post_id = " + 1);
		assertThat(query.getSingleResult()).isNull();

		query = em.createNativeQuery("select COUNT(*) from temporary_post");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore - 1).isTrue();

		//temp가 없는 post는 temp테이블의 테이터 개수가 유지될 것이다.
		query = em.createNativeQuery("select COUNT(*) from temporary_post");
		cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"2\","//temp없는 경우 요구할 것
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select status from post where post_id = " + 2);
		assertThat(query.getSingleResult().equals(PostStatus.GENERAL.name())).isTrue();
		query = em.createNativeQuery("select temporary_post_id from post where post_id = " + 2);
		assertThat(query.getSingleResult()).isNull();

		query = em.createNativeQuery("select COUNT(*) from temporary_post");
		cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_멤버는_유지되며_소유권자불일치시_예외가_발생한다() {
		assertThatThrownBy(() ->
			mockMvc.perform(post("/write")
					.sessionAttr(SessionConst.LOGIN_MEMBER, 3l)
					.content("{"
						+ "\n\"postId\":\"1\","
						+ "\n\"title\":\"testtest\","
						+ "\n\"content\":\"333333\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).isInstanceOf(Exception.class);

		assertThatThrownBy(() ->//로그인을 안한 경우도 예외가 발생한다.
			mockMvc.perform(post("/write")
					.content("{"
						+ "\n\"postId\":\"1\","
						+ "\n\"title\":\"testtest\","
						+ "\n\"content\":\"333333\","
						+ "\n\"access\":\"PRIVATE\""
						+ "\n}")
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		).isInstanceOf(Exception.class);
	}

	@Test
	@Transactional
	@Rollback
	void 임시글을_일반글로_전환하는경우_소개글을_본문에서_채운다() throws Exception {
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"5\","//임시글인 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		Query query = em.createNativeQuery("select introduce from post where post_id = " + 5);
		assertThat(query.getSingleResult().equals("333333")).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 임시글을_일반글로_전환하는경우_작성일이_갱신된다() throws Exception {

		Query query = em.createNativeQuery("select created_date from post where post_id = " + 5);
		String dateBefore = query.getSingleResult().toString();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"5\","//임시글인 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select created_date from post where post_id = " + 5);
		assertThat(query.getSingleResult().equals(dateBefore)).isFalse();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_loveCnt_viewCnt_createdDate_유지된다() throws Exception {
		Query query = em.createNativeQuery("select created_date from post where post_id = " + 1);
		String createDateBefore = query.getSingleResult().toString();
		query = em.createNativeQuery("select love_count from post where post_id = " + 1);
		int loveCountBefore = ((Integer) query.getSingleResult()).intValue();
		query = em.createNativeQuery("select view_count from post where post_id = " + 1);
		int viewCountBefore = ((Integer) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select created_date from post where post_id = " + 1);
		String createDateAfter = query.getSingleResult().toString();
		query = em.createNativeQuery("select love_count from post where post_id = " + 1);
		int loveCountAfter = ((Integer) query.getSingleResult()).intValue();
		query = em.createNativeQuery("select view_count from post where post_id = " + 1);
		int viewCountAfter = ((Integer) query.getSingleResult()).intValue();

		assertThat(createDateAfter.equals(createDateBefore) && loveCountAfter == loveCountBefore &&
			viewCountAfter == viewCountBefore).isTrue();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_updateDate_바뀐다() throws Exception {
		Query query = em.createNativeQuery("select updated_date from post where post_id = " + 1);
		String before = query.getSingleResult().toString();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select updated_date from post where post_id = " + 1);
		String after = query.getSingleResult().toString();

		assertThat(after.equals(before)).isFalse();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_썸네일을_주지않으면_일어나야할_일들() throws Exception {
		//원래없는데 없으면 개수유지 값널이다
		Query query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"2\","//원래 썸네일이 없는 글의 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore).isTrue();

		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + 2);
		assertThat(query.getSingleResult()).isNull();

		//원래있는데 없으면 개수줄고 값널이다
		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","//원래 썸네일이 있는 글의 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					//+ "\n\"thumbnailFileName\":\"2020/03/01/s_uuid_name.png\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore - 1).isTrue();

		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + 2);
		assertThat(query.getSingleResult()).isNull();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_썸네일을주면_일어나야하는일들() throws Exception {
		//원래없는데 있으면 개수늘고 값이있다
		Query query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"2\","//원래 썸네일이 없는 글의 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"thumbnailFileName\":\"2020/03/01/s_uuid_name.png\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		int cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cntAfter == cntBefore + 1).isTrue();

		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + 2);
		assertThat(query.getSingleResult()).isNotNull();

		//원래있는데 있으면 개수유지 값이 바뀌어야한다.
		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		cntBefore = ((BigInteger) query.getSingleResult()).intValue();

		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = 1");
		long postThumbnailId = ((BigInteger) query.getSingleResult()).longValue();

		query = em.createNativeQuery("select uuid from post_thumbnail where post_thumbnail_id = " + postThumbnailId);
		String before = query.getSingleResult().toString();

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"1\","//원래 썸네일이 있는 글의 id
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"thumbnailFileName\":\"2020/03/01/s_uuid2_name.png\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		query = em.createNativeQuery("select COUNT(*) from post_thumbnail");
		cntAfter = ((BigInteger) query.getSingleResult()).intValue();
		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = 1");
		postThumbnailId = ((BigInteger) query.getSingleResult()).longValue();
		query = em.createNativeQuery("select uuid from post_thumbnail where post_thumbnail_id = " + postThumbnailId);
		String after = query.getSingleResult().toString();

		assertThat(cntAfter == cntBefore).isTrue();
		assertThat(before.equals(after)).isFalse();

		query = em.createNativeQuery("select post_thumbnail_id from post where post_id = " + 2);
		assertThat(query.getSingleResult()).isNotNull();
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_태그관련하여_일어날일들() throws Exception {
		//태그 원래있던거 + 원래있던거 삭제 + 새로추가된 것 / null

		//전 태그개수
		Query query = em.createNativeQuery("select count(*) from tag");
		Long tagCnt = ((BigInteger) query.getSingleResult()).longValue();

		//있는 태그
		String existTag = "tag1";
		query = em.createNativeQuery("select count(*) from tag where name = ?").setParameter(1, existTag);
		long cnt = ((BigInteger) query.getSingleResult()).longValue();
		if (cnt != 1) {
			throw new IllegalStateException("있어야 하는데 없거나, 중복되는 태그이름이 있습니다.");
		}

		//있다없어진 태그
		String goneTag = "tag2";
		query = em.createNativeQuery("select count(*) from tag where name = ?").setParameter(1, goneTag);
		cnt = ((BigInteger) query.getSingleResult()).longValue();
		if (cnt != 1) {
			throw new IllegalStateException("원래는 있었다가 없어질 태그가 들어갈 자리 입니다.");
		}

		//없는 태그
		String notExistTag = "sirius";
		query = em.createNativeQuery("select count(*) from tag where name = ?").setParameter(1, notExistTag);
		cnt = ((BigInteger) query.getSingleResult()).longValue();
		if (cnt != 0) {
			throw new IllegalStateException("없어야 하는데 있는 태그이름이 있습니다.");
		}

		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"2\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\","
					+ "\n\"tags\":[\"" + existTag + "\",\"" + notExistTag + "\"]"
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		//후 태그개수 - 전 태그개수
		query = em.createNativeQuery("select count(*) from tag");
		tagCnt = ((BigInteger) query.getSingleResult()).longValue() - tagCnt;

		assertThat(tagCnt == 1).isTrue();

		query = em.createNativeQuery("select distinct name from post_tag inner join tag on post_tag.tag_id = tag.tag_id where post_tag.post_id = " + 2);
		assertThat(query.getResultList()).containsExactly("tag1", "sirius");
	}

	@Test
	@Transactional
	@Rollback
	void 글수정시_태그를_모두_없앨때() throws Exception {
		mockMvc.perform(post("/write")
				.sessionAttr(SessionConst.LOGIN_MEMBER, 2l)
				.content("{"
					+ "\n\"postId\":\"2\","
					+ "\n\"title\":\"testtest\","
					+ "\n\"content\":\"333333\","
					+ "\n\"access\":\"PRIVATE\""
					+ "\n}")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		Query query = em.createNativeQuery("select COUNT(*) from post_tag where post_id = " + 2);
		int cnt = ((BigInteger) query.getSingleResult()).intValue();
		assertThat(cnt == 0).isTrue();
	}

	//writeTemporary
	//새 임시저장
	//임시저장의 수정
	//기존글의 새 임시저장
	//기존글 임시저장의 수정
}
