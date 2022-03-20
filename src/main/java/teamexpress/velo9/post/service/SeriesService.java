package teamexpress.velo9.post.service;

import com.mchange.util.DuplicateElementException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.post.domain.Series;
import teamexpress.velo9.post.domain.SeriesRepository;
import teamexpress.velo9.post.dto.SeriesAddDTO;
import teamexpress.velo9.post.dto.SeriesReadDTO;

@Service
@RequiredArgsConstructor
public class SeriesService {

	private final SeriesRepository seriesRepository;
	private final MemberRepository memberRepository;

	private void checkName(String name) {
		List<String> names = this.getAll().stream().map(series -> series.getName())
			.collect(Collectors.toList());

		if (names.contains(name)) {
			throw new DuplicateElementException("이미 있는 이름의 시리즈 입니다.");
		}
	}

	public List<SeriesReadDTO> getAll() {
		return seriesRepository.findAll().stream().map(series -> new SeriesReadDTO(series)).collect(
			Collectors.toList());
	}

	public void add(SeriesAddDTO seriesAddDTO) {
		checkName(seriesAddDTO.getName());

		Member member = memberRepository.findById(seriesAddDTO.getMemberId()).orElse(null);
		Series series = seriesAddDTO.toSeries(member);

		seriesRepository.save(series);
	}
}
