package teamexpress.velo9.post.service;

import com.mchange.util.DuplicateElementException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.post.domain.PostRepository;
import teamexpress.velo9.post.domain.Series;
import teamexpress.velo9.post.domain.SeriesRepository;
import teamexpress.velo9.post.dto.SeriesAddDTO;
import teamexpress.velo9.post.dto.SeriesReadDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeriesService {

	private final SeriesRepository seriesRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;

	public List<SeriesReadDTO> getAll(Long memberId) {
		return seriesRepository.findByMember(memberId)
			.stream().map(SeriesReadDTO::new).collect(Collectors.toList());
	}

	@Transactional
	public void add(SeriesAddDTO seriesAddDTO, Long memberId) {
		checkName(memberId, seriesAddDTO.getName());

		Member member = memberRepository.findById(memberId).orElseThrow();
		Series series = seriesAddDTO.toSeries(member);

		seriesRepository.save(series);
	}

	@Transactional
	public void delete(Long id) {
		postRepository.updateSeries(id);
		seriesRepository.deleteById(id);
	}

	private void checkName(Long memberId, String name) {
		if (this.getAll(memberId).stream().map(SeriesReadDTO::getSeriesName).collect(Collectors.toList()).contains(name)) {
			throw new DuplicateElementException("이미 있는 이름의 시리즈 입니다.");
		}
	}
}
