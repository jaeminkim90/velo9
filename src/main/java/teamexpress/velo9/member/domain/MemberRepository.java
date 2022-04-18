package teamexpress.velo9.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByEmail(String email);

	Optional<Member> findByUsernameAndEmail(String username, String email);
}
