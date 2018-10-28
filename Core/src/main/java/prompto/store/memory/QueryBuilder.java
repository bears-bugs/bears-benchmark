package prompto.store.memory;

import prompto.store.AttributeInfo;
import prompto.store.IQuery;
import prompto.store.IQueryBuilder;

public class QueryBuilder implements IQueryBuilder {

	Query query = new Query();

	@Override
	public <T> QueryBuilder verify(AttributeInfo info, MatchOp match, T fieldValue) {
		query.verify(info, match, fieldValue);
		return this;
	}

	@Override
	public QueryBuilder and() {
		query.and();
		return this;
	}

	@Override
	public QueryBuilder or() {
		query.or();
		return this;
	}

	@Override
	public QueryBuilder not() {
		query.not();
		return this;
	}

	@Override
	public QueryBuilder first(Long first) {
		query.setFirst(first);
		return this;
	}

	@Override
	public QueryBuilder last(Long last) {
		query.setLast(last);
		return this;
	}

	@Override
	public QueryBuilder orderBy(AttributeInfo attribute, boolean descending) {
		query.addOrderByClause(attribute, descending);
		return this;
	}

	@Override
	public IQuery build() {
		return query;
	}
	
	
}
