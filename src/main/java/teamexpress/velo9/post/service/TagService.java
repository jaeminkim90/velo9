package teamexpress.velo9.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostTagRepository;
import teamexpress.velo9.post.domain.Tag;
import teamexpress.velo9.post.domain.TagRepository;
import teamexpress.velo9.post.dto.TagDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

	private final TagRepository tagRepository;
	private final PostTagRepository postTagRepository;

	@Transactional
	public void addTags(Post post, List<String> tagNames) {
		if (tagNames == null) {
			postTagRepository.deleteAllByPost(post);
			return;
		}

		List<String> tags = removeDuplication(tagNames);

		List<String> realTagNames = tagRepository.getTagNames();

		tags.stream().filter(name -> !realTagNames.contains(name))
			.forEach((name) -> tagRepository.save(Tag.builder().name(name).build()));

		postTagRepository.deleteAllByPost(post);

		tags.forEach(name -> postTagRepository.save(
			PostTag.builder()
				.tag(tagRepository.findByName(name))
				.post(post)
				.build()
		));
	}

	@Transactional
	public void removeUselessTags() {
		tagRepository.findAll().stream().filter(tag -> postTagRepository.countByTag(tag) == 0)
			.forEach(tagRepository::delete);
	}

	private List<String> removeDuplication(List<String> tagNames) {
		return tagNames.stream().distinct().collect(Collectors.toList());
	}


	public List<TagDTO> findAllTags(String nickname) {
		return tagRepository.findUsedTags(nickname).stream().map(TagDTO::new).collect(Collectors.toList());
	}
}
