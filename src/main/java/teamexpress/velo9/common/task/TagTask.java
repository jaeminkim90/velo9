package teamexpress.velo9.common.task;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.post.domain.PostTagRepository;
import teamexpress.velo9.post.domain.TagRepository;

@Component
@RequiredArgsConstructor
public class TagTask {

	private final TagRepository tagRepository;
	private final PostTagRepository postTagRepository;

	@Scheduled(cron = "0 12 18 * * *")
	@Transactional
	public void removeUselessTags() {
		List<Long> usedTagIds = postTagRepository.findTagIds();
		List<Long> allTagIds = tagRepository.findIds();
		List<Long> idsToRemove = allTagIds.stream().filter(id -> !usedTagIds.contains(id)).collect(Collectors.toList());
		tagRepository.deleteByIds(idsToRemove);
	}
}
