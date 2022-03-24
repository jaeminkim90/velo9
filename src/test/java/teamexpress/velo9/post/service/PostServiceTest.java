package teamexpress.velo9.post.service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.post.dto.LovePostDTO;
import teamexpress.velo9.post.dto.TempSavedPostDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

	@Autowired
	private PostService postService;


	@Test
	void findTempSavedPosts() {
		// given
		List<TempSavedPostDTO> validTempSavedPosts = postService.getTempSavedPost(2L);
		List<TempSavedPostDTO> invalidTempSavedPosts = postService.getTempSavedPost(1L);

		// when
		int validPostsSize = validTempSavedPosts.size();
		int invalidPostsSize = invalidTempSavedPosts.size();

		// then
		assertThat(validPostsSize).isEqualTo(100);
		assertThat(invalidPostsSize).isEqualTo(0);
	}

	@Test
	void findLovePosts() {
		// given
		List<LovePostDTO> lovePosts = postService.getLovePosts(2L);

		// when
		int lovePostsSize = lovePosts.size();
		LovePostDTO findLovePostDTO = lovePosts.stream()
			.filter(p -> p.getTitle().equals("1"))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException());

		// then
		assertThat(lovePostsSize).isEqualTo(10);
		assertThat(findLovePostDTO.getIntroduce()).isEqualTo("1");


	}
}
