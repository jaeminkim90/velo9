package teamexpress.velo9.member.domain;

import static teamexpress.velo9.member.domain.QLook.*;
import static teamexpress.velo9.member.domain.QMember.*;
import static teamexpress.velo9.post.domain.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;

public class LookRepositoryCustomImpl implements LookRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public LookRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Optional<Look> findByPostAndMember(Long postId, Long memberId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(look)
			.leftJoin(member)
			.on(look.member.id.eq(member.id))
			.leftJoin(post)
			.on(look.post.id.eq(post.id))
			.where(post.member.id.eq(postId))
			.where(look.member.id.eq(memberId))
			.fetchOne());
	}
}
