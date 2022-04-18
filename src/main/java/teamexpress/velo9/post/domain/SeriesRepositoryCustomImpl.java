package teamexpress.velo9.post.domain;

import static teamexpress.velo9.post.domain.QSeries.series;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SeriesRepositoryCustomImpl implements SeriesRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public SeriesRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Slice<Series> findPostBySeriesName(String nickname, Pageable pageable) {
		List<Series> content = queryFactory
			.selectFrom(series)
			.where(series.posts.isNotEmpty()
				.and(checkNickname(nickname)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = isHasNext(content, pageable);

		return new SliceImpl<>(content, pageable, hasNext);
	}

	private boolean isHasNext(List<Series> result, Pageable pageable) {
		boolean hasNext = false;
		if (result.size() > pageable.getPageSize()) {
			result.remove(pageable.getPageSize());
			hasNext = true;
		}
		return hasNext;
	}

	private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
		try {
			return new BooleanBuilder(f.get());
		} catch (NullPointerException e) {
			return new BooleanBuilder();
		}
	}

	private BooleanBuilder checkNickname(String nickname) {
		return nullSafeBuilder(() -> series.member.nickname.eq(nickname));
	}
}
