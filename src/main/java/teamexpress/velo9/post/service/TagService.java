package teamexpress.velo9.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.post.domain.TagRepository;
import teamexpress.velo9.post.dto.TagDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

	private final TagRepository tagRepository;

	public List<TagDTO> findAllTags(String nickname) {
		return tagRepository.findUsedTags(nickname).stream().map(TagDTO::new).collect(Collectors.toList());
	}
}
