// Generated from OParser.g4 by ANTLR 4.7.1
package prompto.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OParser}.
 */
public interface OParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_category_declaration(OParser.Enum_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_category_declaration(OParser.Enum_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_native_declaration(OParser.Enum_native_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_native_declaration(OParser.Enum_native_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol(OParser.Category_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol(OParser.Category_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol(OParser.Native_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol(OParser.Native_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_declaration(OParser.Attribute_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_declaration(OParser.Attribute_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_widget_declaration(OParser.Concrete_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_widget_declaration(OParser.Concrete_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_widget_declaration(OParser.Native_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_widget_declaration(OParser.Native_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_category_declaration(OParser.Concrete_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_category_declaration(OParser.Concrete_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleton_category_declaration(OParser.Singleton_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleton_category_declaration(OParser.Singleton_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DerivedListItem}
	 * labeled alternative in {@link OParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void enterDerivedListItem(OParser.DerivedListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DerivedListItem}
	 * labeled alternative in {@link OParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void exitDerivedListItem(OParser.DerivedListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DerivedList}
	 * labeled alternative in {@link OParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void enterDerivedList(OParser.DerivedListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DerivedList}
	 * labeled alternative in {@link OParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void exitDerivedList(OParser.DerivedListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyCategoryMethodList}
	 * labeled alternative in {@link OParser#category_method_list}.
	 * @param ctx the parse tree
	 */
	void enterEmptyCategoryMethodList(OParser.EmptyCategoryMethodListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyCategoryMethodList}
	 * labeled alternative in {@link OParser#category_method_list}.
	 * @param ctx the parse tree
	 */
	void exitEmptyCategoryMethodList(OParser.EmptyCategoryMethodListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurlyCategoryMethodList}
	 * labeled alternative in {@link OParser#category_method_list}.
	 * @param ctx the parse tree
	 */
	void enterCurlyCategoryMethodList(OParser.CurlyCategoryMethodListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurlyCategoryMethodList}
	 * labeled alternative in {@link OParser#category_method_list}.
	 * @param ctx the parse tree
	 */
	void exitCurlyCategoryMethodList(OParser.CurlyCategoryMethodListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterOperator_method_declaration(OParser.Operator_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitOperator_method_declaration(OParser.Operator_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSetter_method_declaration(OParser.Setter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSetter_method_declaration(OParser.Setter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_setter_declaration(OParser.Native_setter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_setter_declaration(OParser.Native_setter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterGetter_method_declaration(OParser.Getter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitGetter_method_declaration(OParser.Getter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_getter_declaration(OParser.Native_getter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_getter_declaration(OParser.Native_getter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_resource_declaration(OParser.Native_resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_resource_declaration(OParser.Native_resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_declaration(OParser.Native_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_declaration(OParser.Native_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_bindings(OParser.Native_category_bindingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_bindings(OParser.Native_category_bindingsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link OParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingListItem(OParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link OParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingListItem(OParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link OParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingList(OParser.NativeCategoryBindingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link OParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingList(OParser.NativeCategoryBindingListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAbstract_method_declaration(OParser.Abstract_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAbstract_method_declaration(OParser.Abstract_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_method_declaration(OParser.Concrete_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_method_declaration(OParser.Concrete_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_method_declaration(OParser.Native_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_method_declaration(OParser.Native_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTest_method_declaration(OParser.Test_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTest_method_declaration(OParser.Test_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assertion}.
	 * @param ctx the parse tree
	 */
	void enterAssertion(OParser.AssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assertion}.
	 * @param ctx the parse tree
	 */
	void exitAssertion(OParser.AssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void enterTyped_argument(OParser.Typed_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void exitTyped_argument(OParser.Typed_argumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingleStatement}
	 * labeled alternative in {@link OParser#statement_or_list}.
	 * @param ctx the parse tree
	 */
	void enterSingleStatement(OParser.SingleStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleStatement}
	 * labeled alternative in {@link OParser#statement_or_list}.
	 * @param ctx the parse tree
	 */
	void exitSingleStatement(OParser.SingleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CurlyStatementList}
	 * labeled alternative in {@link OParser#statement_or_list}.
	 * @param ctx the parse tree
	 */
	void enterCurlyStatementList(OParser.CurlyStatementListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CurlyStatementList}
	 * labeled alternative in {@link OParser#statement_or_list}.
	 * @param ctx the parse tree
	 */
	void exitCurlyStatementList(OParser.CurlyStatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallStatement(OParser.MethodCallStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallStatement(OParser.MethodCallStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignInstanceStatement(OParser.AssignInstanceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignInstanceStatement(OParser.AssignInstanceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignTupleStatement(OParser.AssignTupleStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignTupleStatement(OParser.AssignTupleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStoreStatement(OParser.StoreStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStoreStatement(OParser.StoreStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(OParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(OParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(OParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(OParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(OParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(OParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(OParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(OParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(OParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(OParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForEachStatement(OParser.ForEachStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForEachStatement(OParser.ForEachStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(OParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(OParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(OParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(OParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(OParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(OParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRaiseStatement(OParser.RaiseStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRaiseStatement(OParser.RaiseStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWriteStatement(OParser.WriteStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWriteStatement(OParser.WriteStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithResourceStatement(OParser.WithResourceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithResourceStatement(OParser.WithResourceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithSingletonStatement(OParser.WithSingletonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithSingletonStatement(OParser.WithSingletonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterClosureStatement(OParser.ClosureStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitClosureStatement(OParser.ClosureStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentStatement(OParser.CommentStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link OParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentStatement(OParser.CommentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void enterFlush_statement(OParser.Flush_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void exitFlush_statement(OParser.Flush_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void enterStore_statement(OParser.Store_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void exitStore_statement(OParser.Store_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_resource_statement(OParser.With_resource_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_resource_statement(OParser.With_resource_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_singleton_statement(OParser.With_singleton_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_singleton_statement(OParser.With_singleton_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_statement(OParser.Switch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_statement(OParser.Switch_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link OParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterAtomicSwitchCase(OParser.AtomicSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link OParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitAtomicSwitchCase(OParser.AtomicSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link OParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCollectionSwitchCase(OParser.CollectionSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link OParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCollectionSwitchCase(OParser.CollectionSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_each_statement(OParser.For_each_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_each_statement(OParser.For_each_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void enterDo_while_statement(OParser.Do_while_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void exitDo_while_statement(OParser.Do_while_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_statement(OParser.While_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_statement(OParser.While_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(OParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(OParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link OParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementList(OParser.ElseIfStatementListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link OParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementList(OParser.ElseIfStatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link OParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementListItem(OParser.ElseIfStatementListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link OParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementListItem(OParser.ElseIfStatementListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void enterRaise_statement(OParser.Raise_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void exitRaise_statement(OParser.Raise_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void enterTry_statement(OParser.Try_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void exitTry_statement(OParser.Try_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link OParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchAtomicStatement(OParser.CatchAtomicStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link OParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchAtomicStatement(OParser.CatchAtomicStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link OParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchCollectionStatement(OParser.CatchCollectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link OParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchCollectionStatement(OParser.CatchCollectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_statement(OParser.Break_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_statement(OParser.Break_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(OParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(OParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#method_call}.
	 * @param ctx the parse tree
	 */
	void enterMethod_call(OParser.Method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#method_call}.
	 * @param ctx the parse tree
	 */
	void exitMethod_call(OParser.Method_callContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodName}
	 * labeled alternative in {@link OParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(OParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodName}
	 * labeled alternative in {@link OParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(OParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodParent}
	 * labeled alternative in {@link OParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void enterMethodParent(OParser.MethodParentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodParent}
	 * labeled alternative in {@link OParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void exitMethodParent(OParser.MethodParentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableSelector}
	 * labeled alternative in {@link OParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void enterCallableSelector(OParser.CallableSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableSelector}
	 * labeled alternative in {@link OParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void exitCallableSelector(OParser.CallableSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableRoot}
	 * labeled alternative in {@link OParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void enterCallableRoot(OParser.CallableRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableRoot}
	 * labeled alternative in {@link OParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void exitCallableRoot(OParser.CallableRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableMemberSelector}
	 * labeled alternative in {@link OParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void enterCallableMemberSelector(OParser.CallableMemberSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableMemberSelector}
	 * labeled alternative in {@link OParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void exitCallableMemberSelector(OParser.CallableMemberSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableItemSelector}
	 * labeled alternative in {@link OParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void enterCallableItemSelector(OParser.CallableItemSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableItemSelector}
	 * labeled alternative in {@link OParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void exitCallableItemSelector(OParser.CallableItemSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#x_expression}.
	 * @param ctx the parse tree
	 */
	void enterX_expression(OParser.X_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#x_expression}.
	 * @param ctx the parse tree
	 */
	void exitX_expression(OParser.X_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntDivideExpression(OParser.IntDivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntDivideExpression(OParser.IntDivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAnyExpression(OParser.HasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAnyExpression(OParser.HasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasExpression(OParser.HasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasExpression(OParser.HasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpression(OParser.TernaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpression(OParser.TernaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualsExpression(OParser.NotEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualsExpression(OParser.NotEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInExpression(OParser.InExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInExpression(OParser.InExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsAnExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsAnExpression(OParser.IsAnExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsAnExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsAnExpression(OParser.IsAnExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterJsxExpression(OParser.JsxExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitJsxExpression(OParser.JsxExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(OParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(OParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanExpression(OParser.GreaterThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanExpression(OParser.GreaterThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(OParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(OParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCodeExpression(OParser.CodeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCodeExpression(OParser.CodeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualExpression(OParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualExpression(OParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAnyExpression(OParser.NotHasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAnyExpression(OParser.NotHasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(OParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(OParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasExpression(OParser.NotHasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasExpression(OParser.NotHasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterClosureExpression(OParser.ClosureExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitClosureExpression(OParser.ClosureExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAllExpression(OParser.NotHasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAllExpression(OParser.NotHasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterContainsExpression(OParser.ContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitContainsExpression(OParser.ContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotContainsExpression(OParser.NotContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotContainsExpression(OParser.NotContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyExpression(OParser.MultiplyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyExpression(OParser.MultiplyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRoughlyEqualsExpression(OParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRoughlyEqualsExpression(OParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNotAnExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsNotAnExpression(OParser.IsNotAnExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNotAnExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsNotAnExpression(OParser.IsNotAnExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExpression(OParser.ExecuteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExpression(OParser.ExecuteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodExpression(OParser.MethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodExpression(OParser.MethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualExpression(OParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualExpression(OParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotInExpression(OParser.NotInExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotInExpression(OParser.NotInExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIteratorExpression(OParser.IteratorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIteratorExpression(OParser.IteratorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsNotExpression(OParser.IsNotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsNotExpression(OParser.IsNotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivideExpression(OParser.DivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivideExpression(OParser.DivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(OParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(OParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(OParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(OParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpression(OParser.AddExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpression(OParser.AddExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAllExpression(OParser.HasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAllExpression(OParser.HasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceExpression(OParser.InstanceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceExpression(OParser.InstanceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCssExpression(OParser.CssExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCssExpression(OParser.CssExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(OParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(OParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterModuloExpression(OParser.ModuloExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitModuloExpression(OParser.ModuloExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanExpression(OParser.LessThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanExpression(OParser.LessThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualsExpression(OParser.EqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link OParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualsExpression(OParser.EqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#an_expression}.
	 * @param ctx the parse tree
	 */
	void enterAn_expression(OParser.An_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#an_expression}.
	 * @param ctx the parse tree
	 */
	void exitAn_expression(OParser.An_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#closure_expression}.
	 * @param ctx the parse tree
	 */
	void enterClosure_expression(OParser.Closure_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#closure_expression}.
	 * @param ctx the parse tree
	 */
	void exitClosure_expression(OParser.Closure_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link OParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectorExpression(OParser.SelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link OParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectorExpression(OParser.SelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link OParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectableExpression(OParser.SelectableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link OParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectableExpression(OParser.SelectableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#method_expression}.
	 * @param ctx the parse tree
	 */
	void enterMethod_expression(OParser.Method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#method_expression}.
	 * @param ctx the parse tree
	 */
	void exitMethod_expression(OParser.Method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void enterBlob_expression(OParser.Blob_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void exitBlob_expression(OParser.Blob_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void enterDocument_expression(OParser.Document_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void exitDocument_expression(OParser.Document_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void enterWrite_statement(OParser.Write_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void exitWrite_statement(OParser.Write_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#filtered_list_expression}.
	 * @param ctx the parse tree
	 */
	void enterFiltered_list_expression(OParser.Filtered_list_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#filtered_list_expression}.
	 * @param ctx the parse tree
	 */
	void exitFiltered_list_expression(OParser.Filtered_list_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link OParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchOne(OParser.FetchOneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link OParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchOne(OParser.FetchOneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link OParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchMany(OParser.FetchManyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link OParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchMany(OParser.FetchManyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void enterSorted_expression(OParser.Sorted_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void exitSorted_expression(OParser.Sorted_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberSelector(OParser.MemberSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberSelector(OParser.MemberSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterItemSelector(OParser.ItemSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitItemSelector(OParser.ItemSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterSliceSelector(OParser.SliceSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link OParser#selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitSliceSelector(OParser.SliceSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link OParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorFrom(OParser.ConstructorFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link OParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorFrom(OParser.ConstructorFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link OParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorNoFrom(OParser.ConstructorNoFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link OParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorNoFrom(OParser.ConstructorNoFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void enterCopy_from(OParser.Copy_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void exitCopy_from(OParser.Copy_fromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpressionAssignmentList}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentList(OParser.ExpressionAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpressionAssignmentList}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentList(OParser.ExpressionAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentList(OParser.ArgumentAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentList(OParser.ArgumentAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentListItem(OParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link OParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentListItem(OParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void enterArgument_assignment(OParser.Argument_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void exitArgument_assignment(OParser.Argument_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_instance_statement(OParser.Assign_instance_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_instance_statement(OParser.Assign_instance_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link OParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterMemberInstance(OParser.MemberInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link OParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitMemberInstance(OParser.MemberInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link OParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterItemInstance(OParser.ItemInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link OParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitItemInstance(OParser.ItemInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_tuple_statement(OParser.Assign_tuple_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_tuple_statement(OParser.Assign_tuple_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void enterNull_literal(OParser.Null_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void exitNull_literal(OParser.Null_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void enterJsx_ws(OParser.Jsx_wsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void exitJsx_ws(OParser.Jsx_wsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link OParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterFullDeclarationList(OParser.FullDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link OParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitFullDeclarationList(OParser.FullDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(OParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(OParser.DeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(OParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(OParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_constructor(OParser.Annotation_constructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_constructor(OParser.Annotation_constructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_identifier(OParser.Annotation_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_identifier(OParser.Annotation_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterResource_declaration(OParser.Resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitResource_declaration(OParser.Resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_declaration(OParser.Enum_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_declaration(OParser.Enum_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol_list(OParser.Native_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol_list(OParser.Native_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol_list(OParser.Category_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol_list(OParser.Category_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_list(OParser.Symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_list(OParser.Symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingList(OParser.MatchingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingList(OParser.MatchingListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingSet(OParser.MatchingSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingSet(OParser.MatchingSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingRange(OParser.MatchingRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingRange(OParser.MatchingRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingPattern(OParser.MatchingPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingPattern(OParser.MatchingPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingExpression(OParser.MatchingExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link OParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingExpression(OParser.MatchingExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void enterList_literal(OParser.List_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void exitList_literal(OParser.List_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void enterSet_literal(OParser.Set_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void exitSet_literal(OParser.Set_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(OParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(OParser.Expression_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void enterRange_literal(OParser.Range_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void exitRange_literal(OParser.Range_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterIteratorType(OParser.IteratorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitIteratorType(OParser.IteratorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterSetType(OParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitSetType(OParser.SetTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterListType(OParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitListType(OParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterDictType(OParser.DictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitDictType(OParser.DictTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterCursorType(OParser.CursorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitCursorType(OParser.CursorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryType(OParser.PrimaryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link OParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryType(OParser.PrimaryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link OParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterNativeType(OParser.NativeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link OParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitNativeType(OParser.NativeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link OParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterCategoryType(OParser.CategoryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link OParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitCategoryType(OParser.CategoryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(OParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(OParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCharacterType(OParser.CharacterTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCharacterType(OParser.CharacterTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTextType(OParser.TextTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTextType(OParser.TextTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterImageType(OParser.ImageTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitImageType(OParser.ImageTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterIntegerType(OParser.IntegerTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitIntegerType(OParser.IntegerTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDecimalType(OParser.DecimalTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDecimalType(OParser.DecimalTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDocumentType(OParser.DocumentTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDocumentType(OParser.DocumentTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateType(OParser.DateTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateType(OParser.DateTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeType(OParser.DateTimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeType(OParser.DateTimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTimeType(OParser.TimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTimeType(OParser.TimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterPeriodType(OParser.PeriodTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitPeriodType(OParser.PeriodTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterVersionType(OParser.VersionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitVersionType(OParser.VersionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCodeType(OParser.CodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCodeType(OParser.CodeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBlobType(OParser.BlobTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBlobType(OParser.BlobTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterUUIDType(OParser.UUIDTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitUUIDType(OParser.UUIDTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterHtmlType(OParser.HtmlTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link OParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitHtmlType(OParser.HtmlTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#category_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_type(OParser.Category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#category_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_type(OParser.Category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void enterMutable_category_type(OParser.Mutable_category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void exitMutable_category_type(OParser.Mutable_category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#code_type}.
	 * @param ctx the parse tree
	 */
	void enterCode_type(OParser.Code_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#code_type}.
	 * @param ctx the parse tree
	 */
	void exitCode_type(OParser.Code_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteCategoryDeclaration(OParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteCategoryDeclaration(OParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryDeclaration(OParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryDeclaration(OParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingletonCategoryDeclaration(OParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link OParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingletonCategoryDeclaration(OParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link OParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteWidgetDeclaration(OParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link OParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteWidgetDeclaration(OParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link OParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeWidgetDeclaration(OParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link OParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeWidgetDeclaration(OParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier_list(OParser.Type_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier_list(OParser.Type_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void enterMethod_identifier(OParser.Method_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void exitMethod_identifier(OParser.Method_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_or_keyword(OParser.Identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_or_keyword(OParser.Identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_hyphen_identifier_or_keyword(OParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_hyphen_identifier_or_keyword(OParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_identifier_or_keyword(OParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_identifier_or_keyword(OParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableIdentifier(OParser.VariableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableIdentifier(OParser.VariableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(OParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(OParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbolIdentifier(OParser.SymbolIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link OParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbolIdentifier(OParser.SymbolIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier(OParser.Variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier(OParser.Variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier(OParser.Attribute_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier(OParser.Attribute_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier(OParser.Type_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier(OParser.Type_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_identifier(OParser.Symbol_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_identifier(OParser.Symbol_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAny_identifier(OParser.Any_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAny_identifier(OParser.Any_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(OParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(OParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link OParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterCodeArgument(OParser.CodeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link OParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitCodeArgument(OParser.CodeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link OParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterOperatorArgument(OParser.OperatorArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link OParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitOperatorArgument(OParser.OperatorArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void enterOperator_argument(OParser.Operator_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void exitOperator_argument(OParser.Operator_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void enterNamed_argument(OParser.Named_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void exitNamed_argument(OParser.Named_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void enterCode_argument(OParser.Code_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void exitCode_argument(OParser.Code_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_or_any_type(OParser.Category_or_any_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_or_any_type(OParser.Category_or_any_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyListType(OParser.AnyListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyListType(OParser.AnyListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(OParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyType(OParser.AnyTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyDictType(OParser.AnyDictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link OParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyDictType(OParser.AnyDictTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration_list(OParser.Member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration_list(OParser.Member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration(OParser.Member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration(OParser.Member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration_list(OParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration_list(OParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration(OParser.Native_member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration(OParser.Native_member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaCategoryBinding(OParser.JavaCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaCategoryBinding(OParser.JavaCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCategoryBinding(OParser.CSharpCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCategoryBinding(OParser.CSharpCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython2CategoryBinding(OParser.Python2CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython2CategoryBinding(OParser.Python2CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython3CategoryBinding(OParser.Python3CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython3CategoryBinding(OParser.Python3CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptCategoryBinding(OParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link OParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptCategoryBinding(OParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython_category_binding(OParser.Python_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython_category_binding(OParser.Python_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_module}.
	 * @param ctx the parse tree
	 */
	void enterPython_module(OParser.Python_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_module}.
	 * @param ctx the parse tree
	 */
	void exitPython_module(OParser.Python_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_category_binding(OParser.Javascript_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_category_binding(OParser.Javascript_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_module(OParser.Javascript_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_module(OParser.Javascript_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier_list(OParser.Variable_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier_list(OParser.Variable_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier_list(OParser.Attribute_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier_list(OParser.Attribute_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMethod_declaration(OParser.Method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMethod_declaration(OParser.Method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void enterComment_statement(OParser.Comment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void exitComment_statement(OParser.Comment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_statement_list(OParser.Native_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_statement_list(OParser.Native_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaNativeStatement(OParser.JavaNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaNativeStatement(OParser.JavaNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpNativeStatement(OParser.CSharpNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpNativeStatement(OParser.CSharpNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython2NativeStatement(OParser.Python2NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython2NativeStatement(OParser.Python2NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython3NativeStatement(OParser.Python3NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython3NativeStatement(OParser.Python3NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptNativeStatement(OParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link OParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptNativeStatement(OParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython_native_statement(OParser.Python_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython_native_statement(OParser.Python_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_native_statement(OParser.Javascript_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_native_statement(OParser.Javascript_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(OParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(OParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void enterAssertion_list(OParser.Assertion_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void exitAssertion_list(OParser.Assertion_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_case_statement_list(OParser.Switch_case_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_case_statement_list(OParser.Switch_case_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterCatch_statement_list(OParser.Catch_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitCatch_statement_list(OParser.Catch_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralRangeLiteral(OParser.LiteralRangeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralRangeLiteral(OParser.LiteralRangeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralListLiteral(OParser.LiteralListLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralListLiteral(OParser.LiteralListLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralSetLiteral(OParser.LiteralSetLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link OParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralSetLiteral(OParser.LiteralSetLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMinIntegerLiteral(OParser.MinIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMinIntegerLiteral(OParser.MinIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMaxIntegerLiteral(OParser.MaxIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMaxIntegerLiteral(OParser.MaxIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(OParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(OParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(OParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(OParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterCharacterLiteral(OParser.CharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitCharacterLiteral(OParser.CharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateLiteral(OParser.DateLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateLiteral(OParser.DateLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTimeLiteral(OParser.TimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTimeLiteral(OParser.TimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTextLiteral(OParser.TextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTextLiteral(OParser.TextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(OParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(OParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeLiteral(OParser.DateTimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeLiteral(OParser.DateTimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(OParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(OParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterPeriodLiteral(OParser.PeriodLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitPeriodLiteral(OParser.PeriodLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterVersionLiteral(OParser.VersionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitVersionLiteral(OParser.VersionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterUUIDLiteral(OParser.UUIDLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitUUIDLiteral(OParser.UUIDLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(OParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link OParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(OParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_list_literal(OParser.Literal_list_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_list_literal(OParser.Literal_list_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisExpression(OParser.ParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisExpression(OParser.ParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(OParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(OParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(OParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(OParser.IdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(OParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link OParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(OParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void enterThis_expression(OParser.This_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void exitThis_expression(OParser.This_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis_expression(OParser.Parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis_expression(OParser.Parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_expression(OParser.Literal_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_expression(OParser.Literal_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void enterCollection_literal(OParser.Collection_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void exitCollection_literal(OParser.Collection_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void enterTuple_literal(OParser.Tuple_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void exitTuple_literal(OParser.Tuple_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void enterDict_literal(OParser.Dict_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void exitDict_literal(OParser.Dict_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void enterDocument_literal(OParser.Document_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void exitDocument_literal(OParser.Document_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void enterExpression_tuple(OParser.Expression_tupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void exitExpression_tuple(OParser.Expression_tupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry_list(OParser.Dict_entry_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry_list(OParser.Dict_entry_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry(OParser.Dict_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry(OParser.Dict_entryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link OParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyIdentifier(OParser.DictKeyIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link OParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyIdentifier(OParser.DictKeyIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link OParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyText(OParser.DictKeyTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link OParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyText(OParser.DictKeyTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstAndLast(OParser.SliceFirstAndLastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstAndLast(OParser.SliceFirstAndLastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstOnly(OParser.SliceFirstOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstOnly(OParser.SliceFirstOnlyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceLastOnly(OParser.SliceLastOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link OParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceLastOnly(OParser.SliceLastOnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_variable_statement(OParser.Assign_variable_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_variable_statement(OParser.Assign_variable_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link OParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterChildInstance(OParser.ChildInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link OParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitChildInstance(OParser.ChildInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link OParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterRootInstance(OParser.RootInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link OParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitRootInstance(OParser.RootInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link OParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsATypeExpression(OParser.IsATypeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link OParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsATypeExpression(OParser.IsATypeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link OParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsOtherExpression(OParser.IsOtherExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link OParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsOtherExpression(OParser.IsOtherExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_all_expression(OParser.Read_all_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_all_expression(OParser.Read_all_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_one_expression(OParser.Read_one_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_one_expression(OParser.Read_one_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_list(OParser.Order_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_list(OParser.Order_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#order_by}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by(OParser.Order_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#order_by}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by(OParser.Order_byContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorPlus(OParser.OperatorPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorPlus(OParser.OperatorPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMinus(OParser.OperatorMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMinus(OParser.OperatorMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMultiply(OParser.OperatorMultiplyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMultiply(OParser.OperatorMultiplyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorDivide(OParser.OperatorDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorDivide(OParser.OperatorDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorIDivide(OParser.OperatorIDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorIDivide(OParser.OperatorIDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorModulo(OParser.OperatorModuloContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link OParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorModulo(OParser.OperatorModuloContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(OParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(OParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#new_token}.
	 * @param ctx the parse tree
	 */
	void enterNew_token(OParser.New_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#new_token}.
	 * @param ctx the parse tree
	 */
	void exitNew_token(OParser.New_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#key_token}.
	 * @param ctx the parse tree
	 */
	void enterKey_token(OParser.Key_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#key_token}.
	 * @param ctx the parse tree
	 */
	void exitKey_token(OParser.Key_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#module_token}.
	 * @param ctx the parse tree
	 */
	void enterModule_token(OParser.Module_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#module_token}.
	 * @param ctx the parse tree
	 */
	void exitModule_token(OParser.Module_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#value_token}.
	 * @param ctx the parse tree
	 */
	void enterValue_token(OParser.Value_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#value_token}.
	 * @param ctx the parse tree
	 */
	void exitValue_token(OParser.Value_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void enterSymbols_token(OParser.Symbols_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void exitSymbols_token(OParser.Symbols_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#assign}.
	 * @param ctx the parse tree
	 */
	void enterAssign(OParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#assign}.
	 * @param ctx the parse tree
	 */
	void exitAssign(OParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#multiply}.
	 * @param ctx the parse tree
	 */
	void enterMultiply(OParser.MultiplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#multiply}.
	 * @param ctx the parse tree
	 */
	void exitMultiply(OParser.MultiplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#divide}.
	 * @param ctx the parse tree
	 */
	void enterDivide(OParser.DivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#divide}.
	 * @param ctx the parse tree
	 */
	void exitDivide(OParser.DivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#idivide}.
	 * @param ctx the parse tree
	 */
	void enterIdivide(OParser.IdivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#idivide}.
	 * @param ctx the parse tree
	 */
	void exitIdivide(OParser.IdivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#modulo}.
	 * @param ctx the parse tree
	 */
	void enterModulo(OParser.ModuloContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#modulo}.
	 * @param ctx the parse tree
	 */
	void exitModulo(OParser.ModuloContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#lfs}.
	 * @param ctx the parse tree
	 */
	void enterLfs(OParser.LfsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#lfs}.
	 * @param ctx the parse tree
	 */
	void exitLfs(OParser.LfsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#lfp}.
	 * @param ctx the parse tree
	 */
	void enterLfp(OParser.LfpContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#lfp}.
	 * @param ctx the parse tree
	 */
	void exitLfp(OParser.LfpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link OParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptReturnStatement(OParser.JavascriptReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link OParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptReturnStatement(OParser.JavascriptReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link OParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptStatement(OParser.JavascriptStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link OParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptStatement(OParser.JavascriptStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link OParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptSelectorExpression(OParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link OParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptSelectorExpression(OParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link OParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptPrimaryExpression(OParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link OParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptPrimaryExpression(OParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_primary_expression(OParser.Javascript_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_primary_expression(OParser.Javascript_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_this_expression(OParser.Javascript_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_this_expression(OParser.Javascript_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_new_expression(OParser.Javascript_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_new_expression(OParser.Javascript_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMethodExpression(OParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMethodExpression(OParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMemberExpression(OParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMemberExpression(OParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptItemExpression(OParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link OParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptItemExpression(OParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_method_expression(OParser.Javascript_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_method_expression(OParser.Javascript_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link OParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentList(OParser.JavascriptArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link OParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentList(OParser.JavascriptArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link OParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentListItem(OParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link OParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentListItem(OParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_item_expression(OParser.Javascript_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_item_expression(OParser.Javascript_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_parenthesis_expression(OParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_parenthesis_expression(OParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier_expression(OParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier_expression(OParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptIntegerLiteral(OParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptIntegerLiteral(OParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptDecimalLiteral(OParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptDecimalLiteral(OParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptTextLiteral(OParser.JavascriptTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptTextLiteral(OParser.JavascriptTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptBooleanLiteral(OParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptBooleanLiteral(OParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptCharacterLiteral(OParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link OParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptCharacterLiteral(OParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier(OParser.Javascript_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier(OParser.Javascript_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link OParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonReturnStatement(OParser.PythonReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link OParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonReturnStatement(OParser.PythonReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link OParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonStatement(OParser.PythonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link OParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonStatement(OParser.PythonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link OParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelectorExpression(OParser.PythonSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link OParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelectorExpression(OParser.PythonSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link OParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPrimaryExpression(OParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link OParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPrimaryExpression(OParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelfExpression(OParser.PythonSelfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelfExpression(OParser.PythonSelfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonParenthesisExpression(OParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonParenthesisExpression(OParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifierExpression(OParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifierExpression(OParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonLiteralExpression(OParser.PythonLiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonLiteralExpression(OParser.PythonLiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonGlobalMethodExpression(OParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link OParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonGlobalMethodExpression(OParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_self_expression(OParser.Python_self_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_self_expression(OParser.Python_self_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link OParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonMethodExpression(OParser.PythonMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link OParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonMethodExpression(OParser.PythonMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link OParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonItemExpression(OParser.PythonItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link OParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonItemExpression(OParser.PythonItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_method_expression(OParser.Python_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_method_expression(OParser.Python_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalOnlyArgumentList(OParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalOnlyArgumentList(OParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedOnlyArgumentList(OParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedOnlyArgumentList(OParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonArgumentList(OParser.PythonArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link OParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonArgumentList(OParser.PythonArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link OParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentList(OParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link OParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentList(OParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link OParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentListItem(OParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link OParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentListItem(OParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link OParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentList(OParser.PythonNamedArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link OParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentList(OParser.PythonNamedArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link OParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentListItem(OParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link OParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentListItem(OParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_parenthesis_expression(OParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_parenthesis_expression(OParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonChildIdentifier(OParser.PythonChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonChildIdentifier(OParser.PythonChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPromptoIdentifier(OParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPromptoIdentifier(OParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifier(OParser.PythonIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link OParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifier(OParser.PythonIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIntegerLiteral(OParser.PythonIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIntegerLiteral(OParser.PythonIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonDecimalLiteral(OParser.PythonDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonDecimalLiteral(OParser.PythonDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonTextLiteral(OParser.PythonTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonTextLiteral(OParser.PythonTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonBooleanLiteral(OParser.PythonBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonBooleanLiteral(OParser.PythonBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonCharacterLiteral(OParser.PythonCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link OParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonCharacterLiteral(OParser.PythonCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void enterPython_identifier(OParser.Python_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void exitPython_identifier(OParser.Python_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link OParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaReturnStatement(OParser.JavaReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link OParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaReturnStatement(OParser.JavaReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link OParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaStatement(OParser.JavaStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link OParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaStatement(OParser.JavaStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link OParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaSelectorExpression(OParser.JavaSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link OParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaSelectorExpression(OParser.JavaSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link OParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaPrimaryExpression(OParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link OParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaPrimaryExpression(OParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_primary_expression(OParser.Java_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_primary_expression(OParser.Java_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_this_expression(OParser.Java_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_this_expression(OParser.Java_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_new_expression(OParser.Java_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_new_expression(OParser.Java_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link OParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaMethodExpression(OParser.JavaMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link OParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaMethodExpression(OParser.JavaMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link OParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaItemExpression(OParser.JavaItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link OParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaItemExpression(OParser.JavaItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_method_expression(OParser.Java_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_method_expression(OParser.Java_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link OParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentListItem(OParser.JavaArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link OParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentListItem(OParser.JavaArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link OParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentList(OParser.JavaArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link OParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentList(OParser.JavaArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_item_expression(OParser.Java_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_item_expression(OParser.Java_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_parenthesis_expression(OParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_parenthesis_expression(OParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link OParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIdentifier(OParser.JavaIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link OParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIdentifier(OParser.JavaIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link OParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildIdentifier(OParser.JavaChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link OParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildIdentifier(OParser.JavaChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link OParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaClassIdentifier(OParser.JavaClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link OParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaClassIdentifier(OParser.JavaClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link OParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildClassIdentifier(OParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link OParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildClassIdentifier(OParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIntegerLiteral(OParser.JavaIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIntegerLiteral(OParser.JavaIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaDecimalLiteral(OParser.JavaDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaDecimalLiteral(OParser.JavaDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaTextLiteral(OParser.JavaTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaTextLiteral(OParser.JavaTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaBooleanLiteral(OParser.JavaBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaBooleanLiteral(OParser.JavaBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaCharacterLiteral(OParser.JavaCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link OParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaCharacterLiteral(OParser.JavaCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJava_identifier(OParser.Java_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJava_identifier(OParser.Java_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link OParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpReturnStatement(OParser.CSharpReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link OParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpReturnStatement(OParser.CSharpReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link OParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpStatement(OParser.CSharpStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link OParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpStatement(OParser.CSharpStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link OParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpSelectorExpression(OParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link OParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpSelectorExpression(OParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link OParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPrimaryExpression(OParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link OParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPrimaryExpression(OParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_primary_expression(OParser.Csharp_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_primary_expression(OParser.Csharp_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_this_expression(OParser.Csharp_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_this_expression(OParser.Csharp_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_new_expression(OParser.Csharp_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_new_expression(OParser.Csharp_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link OParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpMethodExpression(OParser.CSharpMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link OParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpMethodExpression(OParser.CSharpMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link OParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpItemExpression(OParser.CSharpItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link OParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpItemExpression(OParser.CSharpItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_method_expression(OParser.Csharp_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_method_expression(OParser.Csharp_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link OParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentList(OParser.CSharpArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link OParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentList(OParser.CSharpArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link OParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentListItem(OParser.CSharpArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link OParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentListItem(OParser.CSharpArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_item_expression(OParser.Csharp_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_item_expression(OParser.Csharp_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_parenthesis_expression(OParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_parenthesis_expression(OParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIdentifier(OParser.CSharpIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIdentifier(OParser.CSharpIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpChildIdentifier(OParser.CSharpChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpChildIdentifier(OParser.CSharpChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPromptoIdentifier(OParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link OParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPromptoIdentifier(OParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIntegerLiteral(OParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIntegerLiteral(OParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpDecimalLiteral(OParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpDecimalLiteral(OParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpTextLiteral(OParser.CSharpTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpTextLiteral(OParser.CSharpTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpBooleanLiteral(OParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpBooleanLiteral(OParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCharacterLiteral(OParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link OParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCharacterLiteral(OParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_identifier(OParser.Csharp_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_identifier(OParser.Csharp_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void enterJsx_expression(OParser.Jsx_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void exitJsx_expression(OParser.Jsx_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link OParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxSelfClosing(OParser.JsxSelfClosingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link OParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxSelfClosing(OParser.JsxSelfClosingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link OParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxElement(OParser.JsxElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link OParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxElement(OParser.JsxElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment(OParser.Jsx_fragmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment(OParser.Jsx_fragmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_start(OParser.Jsx_fragment_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_start(OParser.Jsx_fragment_startContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_end(OParser.Jsx_fragment_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_end(OParser.Jsx_fragment_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_self_closing(OParser.Jsx_self_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_self_closing(OParser.Jsx_self_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void enterJsx_opening(OParser.Jsx_openingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void exitJsx_opening(OParser.Jsx_openingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_closing(OParser.Jsx_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_closing(OParser.Jsx_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void enterJsx_element_name(OParser.Jsx_element_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void exitJsx_element_name(OParser.Jsx_element_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJsx_identifier(OParser.Jsx_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJsx_identifier(OParser.Jsx_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void enterJsx_attribute(OParser.Jsx_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void exitJsx_attribute(OParser.Jsx_attributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link OParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxLiteral(OParser.JsxLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link OParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxLiteral(OParser.JsxLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link OParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxValue(OParser.JsxValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link OParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxValue(OParser.JsxValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void enterJsx_children(OParser.Jsx_childrenContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void exitJsx_children(OParser.Jsx_childrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxText(OParser.JsxTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxText(OParser.JsxTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxChild(OParser.JsxChildContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxChild(OParser.JsxChildContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxCode(OParser.JsxCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link OParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxCode(OParser.JsxCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void enterJsx_text(OParser.Jsx_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void exitJsx_text(OParser.Jsx_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void enterCss_expression(OParser.Css_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void exitCss_expression(OParser.Css_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#css_field}.
	 * @param ctx the parse tree
	 */
	void enterCss_field(OParser.Css_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#css_field}.
	 * @param ctx the parse tree
	 */
	void exitCss_field(OParser.Css_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCss_identifier(OParser.Css_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCss_identifier(OParser.Css_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link OParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssValue(OParser.CssValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link OParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssValue(OParser.CssValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link OParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssText(OParser.CssTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link OParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssText(OParser.CssTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link OParser#css_text}.
	 * @param ctx the parse tree
	 */
	void enterCss_text(OParser.Css_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link OParser#css_text}.
	 * @param ctx the parse tree
	 */
	void exitCss_text(OParser.Css_textContext ctx);
}