package teamexpress.velo9.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostRepository;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostTagRepository;
import teamexpress.velo9.post.domain.Tag;
import teamexpress.velo9.post.domain.TagRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

	private final PostRepository postRepository;
	private final TagRepository tagRepository;
	private final PostTagRepository postTagRepository;

	@Transactional
	public void addTags(Long postId, List<String> tagNames) {

		// 태그가 없으면 리턴!
		if (tagNames == null) {
			return;
		}

		// 중복 태그가 모두 제거된 tag List
		List<String> tags = removeDuplication(tagNames);

		// post 정보를 참조
		Post post = postRepository.findById(postId).orElse(null);

		// 전체 태그 이름 조회
		List<String> realTagNames = tagRepository.getTagNames();

		tags.stream().filter(name -> !realTagNames.contains(name)) // 새로운 태그 이름이 기존 태그 이름에 없을 경우
			.forEach((name) -> tagRepository.save(Tag.builder().name(name).build())); // 새로운 태그로 저장 처리

		// postTag 전체 삭제
		postTagRepository.deleteAllByPost(post);

		//  중복 제거된 태그를 PostTag로 만들어 저장
		tags.forEach(name -> postTagRepository.save( // 포스트 태그 객체 저장
			PostTag.builder() // 포스트 태그 객체 생성
				.tag(tagRepository.findByName(name)) // 각가의 태그 이름으로 태그 조회
				.post(post) // 포스트 조회
				.build()
		));
	}

	@Transactional
	public void removeUselessTags() {
		// post와 연결되어 있지 않은 tag는 삭제
		tagRepository.findAll().stream().filter(tag -> postTagRepository.countByTag(tag) == 0)
			.forEach(tagRepository::delete);
	}

	// 중복되는 아이템들을 모두 제거해주고 새로운 List을 반환
	private List removeDuplication(List<String> tagNames) {
		return tagNames.stream().distinct().collect(Collectors.toList());

	}
}
