package teamexpress.velo9.post.controller;

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
import teamexpress.velo9.common.domain.Result;
import teamexpress.velo9.post.dto.LookPostDTO;
import teamexpress.velo9.post.dto.LoveDTO;
import teamexpress.velo9.post.dto.LovePostDTO;
import teamexpress.velo9.post.dto.PostReadDTO;
import teamexpress.velo9.post.dto.PostSaveDTO;
import teamexpress.velo9.post.dto.ReadDTO;
import teamexpress.velo9.post.dto.SeriesDTO;
import teamexpress.velo9.post.dto.SeriesPostSummaryDTO;
import teamexpress.velo9.post.dto.TemporaryPostWriteDTO;
import teamexpress.velo9.post.service.PostService;
import teamexpress.velo9.post.service.TagService;

@RestController
@RequiredArgsConstructor
public class PostController {

	private static final int SERIES_SIZE = 5;
	private static final int SIZE = 10;
	private static final int ARCHIVE_SIZE = 20;

	private final PostService postService;
	private final TagService tagService;

	@GetMapping("/write")
	public PostSaveDTO write(@RequestParam Long id) {
		return postService.getPostById(id);
	}

	@PostMapping("/write")
	public Result write(@RequestBody PostSaveDTO postSaveDTO) {
		Long postId = postService.write(postSaveDTO); // 받아온 JSON data를 DTO에 받아 저장(업데이트)
		tagService.addTags(postId, postSaveDTO.getTagNames()); // JSON 데이터에서 tag를 따로 저장
		tagService.removeUselessTags();
		return new Result<>(postId);
	}

	@PostMapping("/writeTemporary")
	public void writeTemporary(@RequestBody TemporaryPostWriteDTO temporaryPostWriteDTO) {
		postService.writeTemporary(temporaryPostWriteDTO);
	}

	@PostMapping("/delete")
	public void delete(@RequestParam Long id) {
		postService.delete(id);
		tagService.removeUselessTags();
	}

	@GetMapping("/{nickname}/series")
	public Slice<SeriesDTO> series(
		@PathVariable String nickname,
		@RequestParam(defaultValue = "0") int page) {

		return postService.findSeries(nickname, PageRequest.of(page, SERIES_SIZE));
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
	public Slice<PostReadDTO> postsRead(@PathVariable String nickname,
		@RequestParam(required = false) String tagName,
		@RequestParam(defaultValue = "0") int page) {

		PageRequest pageRequest = PageRequest.of(page, SIZE, Sort.by(Direction.DESC, "createdDate"));
		return postService.findPost(nickname, tagName, pageRequest);
	}

	@GetMapping("/temp")
	public Result tempPostsRead(@RequestParam Long id) {
		return new Result(postService.getTempSavedPost(id));
	}

	@PostMapping("/love")
	public void love(@RequestBody LoveDTO loveDTO) {
		postService.loveOrNot(loveDTO);
	}

	@GetMapping("/archive/like")
	public Slice<LovePostDTO> lovePostRead(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam Long memberId) {

		PageRequest pageRequest = PageRequest.of(page, ARCHIVE_SIZE, Sort.by(Direction.DESC, "createdDate"));

		return postService.getLovePosts(memberId, pageRequest);
	}

	@GetMapping("/archive/recent")
	public Slice<LookPostDTO> lookPostRead(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam Long memberId) {

		PageRequest pageRequest = PageRequest.of(page, ARCHIVE_SIZE, Sort.by(Direction.DESC, "createdDate"));

		return postService.getLookPosts(memberId, pageRequest);
	}

	@GetMapping("/{nickname}/read/{postId}")
	public ReadDTO readPost(@PathVariable String nickname, @PathVariable Long postId, @RequestParam Long memberId) {
		postService.look(postId, memberId);
		return postService.findReadPost(postId, nickname);
	}

	private PageRequest getPageRequest(int page, String sortValue) {
		Sort sort = sortValue.equals(("old")) ?
			Sort.by(Direction.ASC, "createdDate") : Sort.by(Direction.DESC, sortValue);

		return PageRequest.of(page, SIZE, sort);
	}
}
