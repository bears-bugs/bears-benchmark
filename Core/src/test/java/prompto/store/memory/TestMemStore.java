package prompto.store.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import prompto.declaration.AttributeDeclaration;
import prompto.expression.EqualsExpression;
import prompto.expression.IPredicateExpression;
import prompto.expression.UnresolvedIdentifier;
import prompto.grammar.EqOp;
import prompto.grammar.Identifier;
import prompto.grammar.OrderByClause;
import prompto.grammar.OrderByClauseList;
import prompto.intrinsic.PromptoList;
import prompto.literal.TextLiteral;
import prompto.runtime.Context;
import prompto.store.AttributeInfo;
import prompto.store.DataStore;
import prompto.store.IQueryBuilder;
import prompto.store.IStorable;
import prompto.store.IStored;
import prompto.store.IStoredIterable;
import prompto.store.IQueryBuilder.MatchOp;
import prompto.store.memory.MemStore;
import prompto.store.memory.QueryBuilder;
import prompto.type.TextType;
import prompto.utils.IdentifierList;

public class TestMemStore {

	Context context;
	
	@Before
	public void before() throws Exception {
		DataStore.setInstance(new MemStore());
		context = Context.newGlobalContext();
		AttributeDeclaration attr = new AttributeDeclaration(new Identifier("__id__"), TextType.instance());
		attr.register(context);
	}
	
	private IPredicateExpression createPredicate(String name, EqOp op, String value) {
		return new EqualsExpression(
				new UnresolvedIdentifier(new Identifier(name)), 
				op, 
				new TextLiteral('"' + value + '"'));
	}

	private IStorable store(String name, String value) throws Exception {
		IStorable doc = DataStore.getInstance().newStorable(new String[0], null);
		doc.setData(name, value);
		DataStore.getInstance().store(doc);
		return doc;
	}


	@Test
	public void testFetchOneEmpty() throws Exception {
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test__");
		filter.interpretQuery(context, builder);
		IStored d = DataStore.getInstance().fetchOne(builder.build());
		assertNull(d);
	}


	@Test
	public void testFetchOneExists() throws Exception {
		IStorable d1 = store("__id__", "__test__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test__");
		filter.interpretQuery(context, builder);
		IStored d2 = DataStore.getInstance().fetchOne(builder.build());
		assertEquals(d1,  d2);
	}

	@Test
	public void testFetchOneMissing() throws Exception {
		store("__id__", "__test__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test1__");
		filter.interpretQuery(context, builder);
		IStored d2 = DataStore.getInstance().fetchOne(builder.build());
		assertNull(d2);
	}

	@Test
	public void testFetchOneMulti() throws Exception {
		store("__id__", "__test1__");
		IStorable d2 = store("__id__", "__test2__");
		store("__id__", "__test3__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test2__");
		filter.interpretQuery(context, builder);
		IStored d4 = DataStore.getInstance().fetchOne(builder.build());
		assertEquals(d2,  d4);
	}

	@Test
	public void testFetchManyEmpty() throws Exception {
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test__");
		filter.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertFalse(docs.hasNext());
	}

	@Test
	public void testFetchManyOneExists() throws Exception {
		IStorable d1 = store("__id__", "__test1__");
		store("__id__", "__test2__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test1__");
		filter.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		assertEquals(d1,  docs.next());
		assertFalse(docs.hasNext());
	}

	@Test
	public void testFetchManyOneMissing() throws Exception {
		store("__id__", "__test1__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test2__");
		filter.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertFalse(docs.hasNext());
	}

	@Test
	public void testFetchManyManyExist() throws Exception {
		store("__id__", "__test1__");
		store("__id__", "__test1__");
		store("__id__", "__test1__");
		store("__id__", "__test2__");
		IQueryBuilder builder = new QueryBuilder();
		IPredicateExpression filter = createPredicate("__id__", EqOp.EQUALS, "__test1__");
		filter.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertFalse(docs.hasNext());
	}

	@Test
	public void testOrderByAsc() throws Exception {
		IStorable d3 = store("__id__", "__test3__");
		IStorable d1 = store("__id__", "__test1__");
		IStorable d4 = store("__id__", "__test4__");
		IStorable d2 = store("__id__", "__test2__");
		IQueryBuilder builder = new QueryBuilder();
		OrderByClauseList obc = new OrderByClauseList(
				new OrderByClause(
						new IdentifierList(new Identifier("__id__")), 
						false));
		obc.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		assertEquals(d1,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d2,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d3,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d4,  docs.next());
		assertFalse(docs.hasNext());
	}

	@Test
	public void testOrderByDesc() throws Exception {
		IStorable d3 = store("__id__", "__test3__");
		IStorable d1 = store("__id__", "__test1__");
		IStorable d4 = store("__id__", "__test4__");
		IStorable d2 = store("__id__", "__test2__");
		IQueryBuilder builder = new QueryBuilder();
		OrderByClauseList obc = new OrderByClauseList(
				new OrderByClause(
						new IdentifierList(new Identifier("__id__")), 
						true));
		obc.interpretQuery(context, builder);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		assertEquals(d4,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d3,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d2,  docs.next());
		assertTrue(docs.hasNext());
		assertEquals(d1,  docs.next());
		assertFalse(docs.hasNext());
	}
	
	@Test
	public void testSliceEmpty() throws Exception {
		IQueryBuilder builder = new QueryBuilder()
			.first(1L)
			.last(10L);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertFalse(docs.hasNext());
	}

	@Test
	public void testSliceBeyond() throws Exception {
		IStorable d1 = store("__id__", "__test1__");
		IQueryBuilder builder = new QueryBuilder()
			.first(1L)
			.last(10L);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		assertEquals(d1,  docs.next());
		assertFalse(docs.hasNext());
	}

	@Test
	public void testFullSlice() throws Exception {
		store("__id__", "__test1__");
		store("__id__", "__test2__");
		store("__id__", "__test3__");
		store("__id__", "__test4__");
		IQueryBuilder builder = new QueryBuilder()
			.first(1L)
			.last(4L);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertFalse(docs.hasNext());
	}
	
	@Test
	public void testSubSlice() throws Exception {
		store("__id__", "__test1__");
		store("__id__", "__test2__");
		store("__id__", "__test3__");
		store("__id__", "__test4__");
		IQueryBuilder builder = new QueryBuilder()
			.first(2L)
			.last(3L);
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		docs.next();
		assertTrue(docs.hasNext());
		docs.next();
		assertFalse(docs.hasNext());
	}
	
	@Test
	public void testFetchOneFinalCategory() throws Exception {
		store("__id__", "__test0__");
		IStorable d1 = store("__id__", "__test1__");
		PromptoList<String> categories = new PromptoList<>(false);
		categories.add("Project");
		categories.add("Application");
		d1.setData("category", categories);
		IQueryBuilder builder = new QueryBuilder();
		AttributeInfo info = AttributeInfo.CATEGORY;
		builder.verify(info, MatchOp.CONTAINS, "Application");
		IStored doc = DataStore.getInstance().fetchOne(builder.build());
		assertEquals(d1, doc);
	}

	@Test
	public void testFetchOneRootCategory() throws Exception {
		store("__id__", "__test0__");
		IStorable d1 = store("__id__", "__test1__");
		PromptoList<String> categories = new PromptoList<>(false);
		categories.add("Project");
		categories.add("Application");
		d1.setData("category", categories);
		IQueryBuilder builder = new QueryBuilder();
		AttributeInfo info = AttributeInfo.CATEGORY;
		builder.verify(info, MatchOp.CONTAINS, "Project");
		IStored doc = DataStore.getInstance().fetchOne(builder.build());
		assertEquals(d1, doc);
	}

	@Test
	public void testFetchManyCategory() throws Exception {
		store("__id__", "__test0__");
		IStorable d1 = store("__id__", "__test1__");
		PromptoList<String> categories = new PromptoList<>(false);
		categories.add("Project");
		categories.add("Application");
		d1.setData("category", categories);
		IQueryBuilder builder = new QueryBuilder();
		AttributeInfo info = AttributeInfo.CATEGORY;
		builder.verify(info, MatchOp.CONTAINS, "Application");
		IStoredIterable iterable = DataStore.getInstance().fetchMany(builder.build());
		Iterator<IStored> docs = iterable.iterator();
		assertTrue(docs.hasNext());
		docs.next();
		assertFalse(docs.hasNext());
	}

}
