package prompto.store;



public interface IQueryBuilder {

	public static enum MatchOp {
		EQUALS,	// strict equality
		ROUGHLY, // caseless or rounded equality 
		CONTAINS, // pattern matching
		HAS, // collection contains value
		IN, // value in collection 
		GREATER,
		LESSER
	}
	
	// create atomic predicates
	<T> IQueryBuilder verify(AttributeInfo info, MatchOp match, T fieldValue);
	// the below make the assumption that the atomic predicates are available from a stack
	IQueryBuilder and();
	IQueryBuilder or();
	IQueryBuilder not();
	// 1 based range limits
	IQueryBuilder first(Long first); 
	IQueryBuilder last(Long last);
	// ordering
	IQueryBuilder orderBy(AttributeInfo attribute, boolean descending);
	// return the built IQuery object
	IQuery build();


}
