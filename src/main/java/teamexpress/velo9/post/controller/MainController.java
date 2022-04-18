package teamexpress.velo9.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamexpress.velo9.post.dto.PostMainDTO;
import teamexpress.velo9.post.dto.SearchCondition;
import teamexpress.velo9.post.service.PostService;

@RestController
@RequiredArgsConstructor
public class MainController {

	private static final int SIZE = 20;
	private final PostService postService;

	@GetMapping("/")
	public Page<PostMainDTO> mainPage(
		@RequestParam(required = false) boolean tagSelect,
		@RequestParam(required = false) String content,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "createdDate") String sortValue) {

		PageRequest pageRequest = getPageRequest(page, sortValue);
		SearchCondition searchCondition = new SearchCondition(tagSelect, content);
		Page<PostMainDTO> mainSearchPage = postService.searchMain(searchCondition, pageRequest);

		return mainSearchPage;
	}

	private PageRequest getPageRequest(int page, String sortValue) {
		Sort sort = sortValue.equals(("old")) ?
			Sort.by(Direction.ASC, "createdDate") : Sort.by(Direction.DESC, sortValue);

		return PageRequest.of(page, SIZE, sort);
	}
}
