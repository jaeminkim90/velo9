package teamexpress.velo9.post.domain;

import static teamexpress.velo9.post.domain.QTag.tag;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import teamexpress.velo9.post.dto.SearchCondition;

public class TagRepositoryCustomImpl extends QuerydslRepositorySupport implements TagRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public TagRepositoryCustomImpl(EntityManager em) {
		super(Tag.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Tag> searchTag(SearchCondition searchCondition, Pageable pageable) {
		JPAQuery<Tag> query = queryFactory.
			selectFrom(tag)
			.where(tagContains(searchCondition.getContent()));

		List<Tag> content = getQuerydsl().applyPagination(pageable, query).fetch();

		return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
	}

	private BooleanBuilder tagContains(String content) {
		return nullSafeBuilder(() -> tag.name.contains(content));
	}

	private static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
		try {
			return new BooleanBuilder(f.get());
		} catch (NullPointerException e) {
			return new BooleanBuilder();
		}
	}
}
