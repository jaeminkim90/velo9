package teamexpress.velo9.member.domain;

import java.util.Optional;

public interface LookRepositoryCustom {
	Optional<Look> findByPostAndMember(Long postId, Long memberId);
}
