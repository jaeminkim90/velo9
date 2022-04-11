package teamexpress.velo9.post.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamexpress.velo9.common.controller.BaseController;
import teamexpress.velo9.common.domain.PostResult;
import teamexpress.velo9.common.domain.Result;
import teamexpress.velo9.member.security.oauth.SessionConst;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.dto.LookPostDTO;
import teamexpress.velo9.post.dto.LoveDTO;
import teamexpress.velo9.post.dto.LovePostDTO;
import teamexpress.velo9.post.dto.PostReadDTO;
import teamexpress.velo9.post.dto.PostSaveDTO;
import teamexpress.velo9.post.dto.PostWriteDTO;
import teamexpress.velo9.post.dto.ReadDTO;
import teamexpress.velo9.post.dto.SeriesDTO;
import teamexpress.velo9.post.dto.SeriesPostSummaryDTO;
import teamexpress.velo9.post.dto.SeriesReadDTO;
import teamexpress.velo9.post.dto.TagDTO;
import teamexpress.velo9.post.dto.TemporaryPostWriteDTO;
import teamexpress.velo9.post.service.PostService;
import teamexpress.velo9.post.service.TagService;

@RestController
@RequiredArgsConstructor
public class PostController extends BaseController {

	private static final int SERIES_SIZE = 5;
	private static final int SIZE = 10;
	private static final int ARCHIVE_SIZE = 20;

	private final PostService postService;
	private final TagService tagService;

	@GetMapping("/write")
	public PostWriteDTO write(@RequestParam(value = "id", required = false) Long id) {
		return postService.getPostById(id);
	}

	@PostMapping("/write")
	public Result write(@Valid @RequestBody PostSaveDTO postSaveDTO, HttpSession session) {
		Post post = postService.write(postSaveDTO, getMemberId(session));
		tagService.addTags(post, postSaveDTO.getTags());
		tagService.removeUselessTags();
		return new Result<>(post.getId());
	}

	@PostMapping("/writeTemporary")
	public Result writeTemporary(@RequestBody TemporaryPostWriteDTO temporaryPostWriteDTO, HttpSession session) {
		return new Result<>(postService.writeTemporary(temporaryPostWriteDTO, getMemberId(session)));
	}

	@PostMapping("/delete")
	public void delete(@RequestParam Long id) {
		postService.delete(id);
		tagService.removeUselessTags();
	}

	@GetMapping("/{nickname}/series")
	public PostResult series(
		@PathVariable String nickname,
		@RequestParam(defaultValue = "0") int page) {

		Slice<SeriesDTO> series = postService.findSeries(nickname, PageRequest.of(page, SERIES_SIZE));
		List<SeriesReadDTO> seriesDTOList = postService.getUsedSeries(nickname);
		return new PostResult(series, seriesDTOList);
	}

	@GetMapping("/{nickname}/series/{seriesName}")
	public Slice<SeriesPostSummaryDTO> seriesPost(
		@PathVariable String nickname,
		@PathVariable String seriesName,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "createdDate") String sortCondition) {

		PageRequest pageRequest = getPageRequest(page, sortCondition);

		return postService.findSeriesPost(nickname, seriesName, pageRequest);
	}

	@GetMapping("/{nickname}/main")
	public PostResult postsRead(@PathVariable String nickname,
		@RequestParam(required = false) String tagName,
		@RequestParam(defaultValue = "0") int page) {

		PageRequest pageRequest = PageRequest.of(page, SIZE, Sort.by(Direction.DESC, "createdDate"));
		Slice<PostReadDTO> posts = postService.findPost(nickname, tagName, pageRequest);
		List<TagDTO> usedTags = tagService.getUsedTags(nickname);
		return new PostResult(posts, usedTags);
	}

	@GetMapping("/temp")
	public Result tempPostsRead(HttpSession session) {
		return new Result(postService.getTempSavedPost(getMemberId(session)));
	}

	@PostMapping("/love")
	public void love(@RequestBody LoveDTO loveDTO, HttpSession session) {
		postService.loveOrNot(loveDTO, getMemberId(session));
	}

	@GetMapping("/archive/like")
	public Slice<LovePostDTO> lovePostRead(@RequestParam(defaultValue = "0") int page, HttpSession session) {
		PageRequest pageRequest = PageRequest.of(page, ARCHIVE_SIZE, Sort.by(Direction.DESC, "createdDate"));
		return postService.getLovePosts(getMemberId(session), pageRequest);
	}

	@GetMapping("/archive/recent")
	public Slice<LookPostDTO> lookPostRead(@RequestParam(defaultValue = "0") int page, HttpSession session) {
		PageRequest pageRequest = PageRequest.of(page, ARCHIVE_SIZE, Sort.by(Direction.DESC, "createdDate"));
		return postService.getLookPosts(getMemberId(session), pageRequest);
	}

	@GetMapping("/{nickname}/read/{postId}")
	public ReadDTO readPost(@PathVariable String nickname, @PathVariable Long postId, HttpSession session) {
		if (session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
			postService.look(postId, getMemberId(session));
		}

		return postService.findReadPost(postId, nickname);
	}

	private PageRequest getPageRequest(int page, String sortValue) {
		Sort sort = sortValue.equals(("old")) ?
			Sort.by(Direction.ASC, "createdDate") : Sort.by(Direction.DESC, sortValue);

		return PageRequest.of(page, SIZE, sort);
	}
}
