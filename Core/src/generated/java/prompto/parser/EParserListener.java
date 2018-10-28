// Generated from EParser.g4 by ANTLR 4.7.1
package prompto.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EParser}.
 */
public interface EParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_category_declaration(EParser.Enum_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_category_declaration(EParser.Enum_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_native_declaration(EParser.Enum_native_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_native_declaration(EParser.Enum_native_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol(EParser.Native_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol(EParser.Native_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol(EParser.Category_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol(EParser.Category_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_declaration(EParser.Attribute_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_declaration(EParser.Attribute_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_widget_declaration(EParser.Concrete_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_widget_declaration(EParser.Concrete_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_widget_declaration(EParser.Native_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_widget_declaration(EParser.Native_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_category_declaration(EParser.Concrete_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_category_declaration(EParser.Concrete_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleton_category_declaration(EParser.Singleton_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleton_category_declaration(EParser.Singleton_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DerivedList}
	 * labeled alternative in {@link EParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void enterDerivedList(EParser.DerivedListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DerivedList}
	 * labeled alternative in {@link EParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void exitDerivedList(EParser.DerivedListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DerivedListItem}
	 * labeled alternative in {@link EParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void enterDerivedListItem(EParser.DerivedListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DerivedListItem}
	 * labeled alternative in {@link EParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void exitDerivedListItem(EParser.DerivedListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterOperator_method_declaration(EParser.Operator_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitOperator_method_declaration(EParser.Operator_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSetter_method_declaration(EParser.Setter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSetter_method_declaration(EParser.Setter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_setter_declaration(EParser.Native_setter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_setter_declaration(EParser.Native_setter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterGetter_method_declaration(EParser.Getter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitGetter_method_declaration(EParser.Getter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_getter_declaration(EParser.Native_getter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_getter_declaration(EParser.Native_getter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_declaration(EParser.Native_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_declaration(EParser.Native_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_resource_declaration(EParser.Native_resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_resource_declaration(EParser.Native_resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_bindings(EParser.Native_category_bindingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_bindings(EParser.Native_category_bindingsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link EParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingListItem(EParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link EParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingListItem(EParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link EParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingList(EParser.NativeCategoryBindingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link EParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingList(EParser.NativeCategoryBindingListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AttributeList}
	 * labeled alternative in {@link EParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void enterAttributeList(EParser.AttributeListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AttributeList}
	 * labeled alternative in {@link EParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void exitAttributeList(EParser.AttributeListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AttributeListItem}
	 * labeled alternative in {@link EParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void enterAttributeListItem(EParser.AttributeListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AttributeListItem}
	 * labeled alternative in {@link EParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void exitAttributeListItem(EParser.AttributeListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAbstract_method_declaration(EParser.Abstract_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAbstract_method_declaration(EParser.Abstract_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_method_declaration(EParser.Concrete_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_method_declaration(EParser.Concrete_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_method_declaration(EParser.Native_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_method_declaration(EParser.Native_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTest_method_declaration(EParser.Test_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTest_method_declaration(EParser.Test_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assertion}.
	 * @param ctx the parse tree
	 */
	void enterAssertion(EParser.AssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assertion}.
	 * @param ctx the parse tree
	 */
	void exitAssertion(EParser.AssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#full_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterFull_argument_list(EParser.Full_argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#full_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitFull_argument_list(EParser.Full_argument_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void enterTyped_argument(EParser.Typed_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void exitTyped_argument(EParser.Typed_argumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignInstanceStatement(EParser.AssignInstanceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignInstanceStatement(EParser.AssignInstanceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallStatement(EParser.MethodCallStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallStatement(EParser.MethodCallStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignTupleStatement(EParser.AssignTupleStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignTupleStatement(EParser.AssignTupleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStoreStatement(EParser.StoreStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStoreStatement(EParser.StoreStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(EParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(EParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(EParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(EParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(EParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(EParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(EParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(EParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(EParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(EParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForEachStatement(EParser.ForEachStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForEachStatement(EParser.ForEachStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(EParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(EParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(EParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(EParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRaiseStatement(EParser.RaiseStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRaiseStatement(EParser.RaiseStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(EParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(EParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWriteStatement(EParser.WriteStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWriteStatement(EParser.WriteStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithResourceStatement(EParser.WithResourceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithResourceStatement(EParser.WithResourceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithSingletonStatement(EParser.WithSingletonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithSingletonStatement(EParser.WithSingletonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterClosureStatement(EParser.ClosureStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitClosureStatement(EParser.ClosureStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentStatement(EParser.CommentStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link EParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentStatement(EParser.CommentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void enterFlush_statement(EParser.Flush_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void exitFlush_statement(EParser.Flush_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void enterStore_statement(EParser.Store_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void exitStore_statement(EParser.Store_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnresolvedWithArgsStatement}
	 * labeled alternative in {@link EParser#method_call_statement}.
	 * @param ctx the parse tree
	 */
	void enterUnresolvedWithArgsStatement(EParser.UnresolvedWithArgsStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnresolvedWithArgsStatement}
	 * labeled alternative in {@link EParser#method_call_statement}.
	 * @param ctx the parse tree
	 */
	void exitUnresolvedWithArgsStatement(EParser.UnresolvedWithArgsStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InvokeStatement}
	 * labeled alternative in {@link EParser#method_call_statement}.
	 * @param ctx the parse tree
	 */
	void enterInvokeStatement(EParser.InvokeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InvokeStatement}
	 * labeled alternative in {@link EParser#method_call_statement}.
	 * @param ctx the parse tree
	 */
	void exitInvokeStatement(EParser.InvokeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_resource_statement(EParser.With_resource_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_resource_statement(EParser.With_resource_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_singleton_statement(EParser.With_singleton_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_singleton_statement(EParser.With_singleton_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_statement(EParser.Switch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_statement(EParser.Switch_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link EParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterAtomicSwitchCase(EParser.AtomicSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link EParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitAtomicSwitchCase(EParser.AtomicSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link EParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCollectionSwitchCase(EParser.CollectionSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link EParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCollectionSwitchCase(EParser.CollectionSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_each_statement(EParser.For_each_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_each_statement(EParser.For_each_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void enterDo_while_statement(EParser.Do_while_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void exitDo_while_statement(EParser.Do_while_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_statement(EParser.While_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_statement(EParser.While_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(EParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(EParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link EParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementList(EParser.ElseIfStatementListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link EParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementList(EParser.ElseIfStatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link EParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementListItem(EParser.ElseIfStatementListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link EParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementListItem(EParser.ElseIfStatementListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void enterRaise_statement(EParser.Raise_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void exitRaise_statement(EParser.Raise_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void enterTry_statement(EParser.Try_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void exitTry_statement(EParser.Try_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link EParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchAtomicStatement(EParser.CatchAtomicStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link EParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchAtomicStatement(EParser.CatchAtomicStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link EParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchCollectionStatement(EParser.CatchCollectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link EParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchCollectionStatement(EParser.CatchCollectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_statement(EParser.Break_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_statement(EParser.Break_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(EParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(EParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntDivideExpression(EParser.IntDivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntDivideExpression(EParser.IntDivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAnyExpression(EParser.HasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAnyExpression(EParser.HasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasExpression(EParser.HasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasExpression(EParser.HasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInExpression(EParser.InExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInExpression(EParser.InExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterJsxExpression(EParser.JsxExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitJsxExpression(EParser.JsxExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanExpression(EParser.GreaterThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanExpression(EParser.GreaterThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(EParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(EParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReadOneExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterReadOneExpression(EParser.ReadOneExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReadOneExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitReadOneExpression(EParser.ReadOneExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAnyExpression(EParser.NotHasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAnyExpression(EParser.NotHasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(EParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(EParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCallExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallExpression(EParser.MethodCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCallExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallExpression(EParser.MethodCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasExpression(EParser.NotHasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasExpression(EParser.NotHasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SortedExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSortedExpression(EParser.SortedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SortedExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSortedExpression(EParser.SortedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAllExpression(EParser.NotHasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAllExpression(EParser.NotHasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterContainsExpression(EParser.ContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitContainsExpression(EParser.ContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotContainsExpression(EParser.NotContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotContainsExpression(EParser.NotContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRoughlyEqualsExpression(EParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRoughlyEqualsExpression(EParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExpression(EParser.ExecuteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExpression(EParser.ExecuteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualExpression(EParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualExpression(EParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIteratorExpression(EParser.IteratorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIteratorExpression(EParser.IteratorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsNotExpression(EParser.IsNotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsNotExpression(EParser.IsNotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivideExpression(EParser.DivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivideExpression(EParser.DivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(EParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(EParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpression(EParser.AddExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpression(EParser.AddExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceExpression(EParser.InstanceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceExpression(EParser.InstanceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReadAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterReadAllExpression(EParser.ReadAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReadAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitReadAllExpression(EParser.ReadAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(EParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(EParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterModuloExpression(EParser.ModuloExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitModuloExpression(EParser.ModuloExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpression(EParser.TernaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpression(EParser.TernaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchStoreExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchStoreExpression(EParser.FetchStoreExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchStoreExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchStoreExpression(EParser.FetchStoreExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualsExpression(EParser.NotEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualsExpression(EParser.NotEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DocumentExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDocumentExpression(EParser.DocumentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DocumentExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDocumentExpression(EParser.DocumentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(EParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(EParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InvocationExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInvocationExpression(EParser.InvocationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InvocationExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInvocationExpression(EParser.InvocationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCodeExpression(EParser.CodeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCodeExpression(EParser.CodeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AmbiguousExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAmbiguousExpression(EParser.AmbiguousExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AmbiguousExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAmbiguousExpression(EParser.AmbiguousExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualExpression(EParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualExpression(EParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterClosureExpression(EParser.ClosureExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitClosureExpression(EParser.ClosureExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlobExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBlobExpression(EParser.BlobExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlobExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBlobExpression(EParser.BlobExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilteredListExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFilteredListExpression(EParser.FilteredListExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilteredListExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFilteredListExpression(EParser.FilteredListExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorExpression(EParser.ConstructorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorExpression(EParser.ConstructorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyExpression(EParser.MultiplyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyExpression(EParser.MultiplyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotInExpression(EParser.NotInExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotInExpression(EParser.NotInExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnresolvedExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnresolvedExpression(EParser.UnresolvedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnresolvedExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnresolvedExpression(EParser.UnresolvedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(EParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(EParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAllExpression(EParser.HasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAllExpression(EParser.HasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCssExpression(EParser.CssExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCssExpression(EParser.CssExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanExpression(EParser.LessThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanExpression(EParser.LessThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualsExpression(EParser.EqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link EParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualsExpression(EParser.EqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnresolvedSelector}
	 * labeled alternative in {@link EParser#unresolved_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnresolvedSelector(EParser.UnresolvedSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnresolvedSelector}
	 * labeled alternative in {@link EParser#unresolved_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnresolvedSelector(EParser.UnresolvedSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnresolvedIdentifier}
	 * labeled alternative in {@link EParser#unresolved_expression}.
	 * @param ctx the parse tree
	 */
	void enterUnresolvedIdentifier(EParser.UnresolvedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnresolvedIdentifier}
	 * labeled alternative in {@link EParser#unresolved_expression}.
	 * @param ctx the parse tree
	 */
	void exitUnresolvedIdentifier(EParser.UnresolvedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#unresolved_selector}.
	 * @param ctx the parse tree
	 */
	void enterUnresolved_selector(EParser.Unresolved_selectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#unresolved_selector}.
	 * @param ctx the parse tree
	 */
	void exitUnresolved_selector(EParser.Unresolved_selectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#invocation_expression}.
	 * @param ctx the parse tree
	 */
	void enterInvocation_expression(EParser.Invocation_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#invocation_expression}.
	 * @param ctx the parse tree
	 */
	void exitInvocation_expression(EParser.Invocation_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#invocation_trailer}.
	 * @param ctx the parse tree
	 */
	void enterInvocation_trailer(EParser.Invocation_trailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#invocation_trailer}.
	 * @param ctx the parse tree
	 */
	void exitInvocation_trailer(EParser.Invocation_trailerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link EParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectorExpression(EParser.SelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link EParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectorExpression(EParser.SelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link EParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectableExpression(EParser.SelectableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link EParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectableExpression(EParser.SelectableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterMemberSelector(EParser.MemberSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitMemberSelector(EParser.MemberSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterSliceSelector(EParser.SliceSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitSliceSelector(EParser.SliceSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterItemSelector(EParser.ItemSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link EParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitItemSelector(EParser.ItemSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void enterDocument_expression(EParser.Document_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void exitDocument_expression(EParser.Document_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void enterBlob_expression(EParser.Blob_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void exitBlob_expression(EParser.Blob_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link EParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorFrom(EParser.ConstructorFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link EParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorFrom(EParser.ConstructorFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link EParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorNoFrom(EParser.ConstructorNoFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link EParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorNoFrom(EParser.ConstructorNoFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void enterWrite_statement(EParser.Write_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void exitWrite_statement(EParser.Write_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#ambiguous_expression}.
	 * @param ctx the parse tree
	 */
	void enterAmbiguous_expression(EParser.Ambiguous_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#ambiguous_expression}.
	 * @param ctx the parse tree
	 */
	void exitAmbiguous_expression(EParser.Ambiguous_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#filtered_list_suffix}.
	 * @param ctx the parse tree
	 */
	void enterFiltered_list_suffix(EParser.Filtered_list_suffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#filtered_list_suffix}.
	 * @param ctx the parse tree
	 */
	void exitFiltered_list_suffix(EParser.Filtered_list_suffixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link EParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchOne(EParser.FetchOneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link EParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchOne(EParser.FetchOneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link EParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchMany(EParser.FetchManyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link EParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchMany(EParser.FetchManyContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void enterSorted_expression(EParser.Sorted_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void exitSorted_expression(EParser.Sorted_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentListExpression}
	 * labeled alternative in {@link EParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentListExpression(EParser.ArgumentAssignmentListExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentListExpression}
	 * labeled alternative in {@link EParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentListExpression(EParser.ArgumentAssignmentListExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentListNoExpression}
	 * labeled alternative in {@link EParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentListNoExpression(EParser.ArgumentAssignmentListNoExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentListNoExpression}
	 * labeled alternative in {@link EParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentListNoExpression(EParser.ArgumentAssignmentListNoExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link EParser#with_argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentList(EParser.ArgumentAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link EParser#with_argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentList(EParser.ArgumentAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link EParser#with_argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentListItem(EParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link EParser#with_argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentListItem(EParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void enterArgument_assignment(EParser.Argument_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void exitArgument_assignment(EParser.Argument_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_instance_statement(EParser.Assign_instance_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_instance_statement(EParser.Assign_instance_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link EParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterMemberInstance(EParser.MemberInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link EParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitMemberInstance(EParser.MemberInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link EParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterItemInstance(EParser.ItemInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link EParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitItemInstance(EParser.ItemInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_tuple_statement(EParser.Assign_tuple_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_tuple_statement(EParser.Assign_tuple_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#lfs}.
	 * @param ctx the parse tree
	 */
	void enterLfs(EParser.LfsContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#lfs}.
	 * @param ctx the parse tree
	 */
	void exitLfs(EParser.LfsContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#lfp}.
	 * @param ctx the parse tree
	 */
	void enterLfp(EParser.LfpContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#lfp}.
	 * @param ctx the parse tree
	 */
	void exitLfp(EParser.LfpContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void enterJsx_ws(EParser.Jsx_wsContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void exitJsx_ws(EParser.Jsx_wsContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#indent}.
	 * @param ctx the parse tree
	 */
	void enterIndent(EParser.IndentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#indent}.
	 * @param ctx the parse tree
	 */
	void exitIndent(EParser.IndentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#dedent}.
	 * @param ctx the parse tree
	 */
	void enterDedent(EParser.DedentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#dedent}.
	 * @param ctx the parse tree
	 */
	void exitDedent(EParser.DedentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void enterNull_literal(EParser.Null_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void exitNull_literal(EParser.Null_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link EParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterFullDeclarationList(EParser.FullDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link EParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitFullDeclarationList(EParser.FullDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(EParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(EParser.DeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(EParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(EParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_constructor(EParser.Annotation_constructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_constructor(EParser.Annotation_constructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_identifier(EParser.Annotation_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_identifier(EParser.Annotation_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterResource_declaration(EParser.Resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitResource_declaration(EParser.Resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_declaration(EParser.Enum_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_declaration(EParser.Enum_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol_list(EParser.Native_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol_list(EParser.Native_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol_list(EParser.Category_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol_list(EParser.Category_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_list(EParser.Symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_list(EParser.Symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingList(EParser.MatchingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingList(EParser.MatchingListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingSet(EParser.MatchingSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingSet(EParser.MatchingSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingRange(EParser.MatchingRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingRange(EParser.MatchingRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingPattern(EParser.MatchingPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingPattern(EParser.MatchingPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingExpression(EParser.MatchingExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link EParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingExpression(EParser.MatchingExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void enterList_literal(EParser.List_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void exitList_literal(EParser.List_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void enterSet_literal(EParser.Set_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void exitSet_literal(EParser.Set_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(EParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(EParser.Expression_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void enterRange_literal(EParser.Range_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void exitRange_literal(EParser.Range_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterIteratorType(EParser.IteratorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitIteratorType(EParser.IteratorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterSetType(EParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitSetType(EParser.SetTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterListType(EParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitListType(EParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterDictType(EParser.DictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitDictType(EParser.DictTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterCursorType(EParser.CursorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitCursorType(EParser.CursorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryType(EParser.PrimaryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link EParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryType(EParser.PrimaryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link EParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterNativeType(EParser.NativeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link EParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitNativeType(EParser.NativeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link EParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterCategoryType(EParser.CategoryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link EParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitCategoryType(EParser.CategoryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(EParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(EParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCharacterType(EParser.CharacterTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCharacterType(EParser.CharacterTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTextType(EParser.TextTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTextType(EParser.TextTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterImageType(EParser.ImageTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitImageType(EParser.ImageTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterIntegerType(EParser.IntegerTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitIntegerType(EParser.IntegerTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDecimalType(EParser.DecimalTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDecimalType(EParser.DecimalTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDocumentType(EParser.DocumentTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDocumentType(EParser.DocumentTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateType(EParser.DateTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateType(EParser.DateTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeType(EParser.DateTimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeType(EParser.DateTimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTimeType(EParser.TimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTimeType(EParser.TimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterPeriodType(EParser.PeriodTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitPeriodType(EParser.PeriodTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterVersionType(EParser.VersionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitVersionType(EParser.VersionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCodeType(EParser.CodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCodeType(EParser.CodeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBlobType(EParser.BlobTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBlobType(EParser.BlobTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterUUIDType(EParser.UUIDTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitUUIDType(EParser.UUIDTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterHtmlType(EParser.HtmlTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link EParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitHtmlType(EParser.HtmlTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#category_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_type(EParser.Category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#category_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_type(EParser.Category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void enterMutable_category_type(EParser.Mutable_category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void exitMutable_category_type(EParser.Mutable_category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#code_type}.
	 * @param ctx the parse tree
	 */
	void enterCode_type(EParser.Code_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#code_type}.
	 * @param ctx the parse tree
	 */
	void exitCode_type(EParser.Code_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteCategoryDeclaration(EParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteCategoryDeclaration(EParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryDeclaration(EParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryDeclaration(EParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingletonCategoryDeclaration(EParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link EParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingletonCategoryDeclaration(EParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link EParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteWidgetDeclaration(EParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link EParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteWidgetDeclaration(EParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link EParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeWidgetDeclaration(EParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link EParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeWidgetDeclaration(EParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier_list(EParser.Type_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier_list(EParser.Type_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void enterMethod_identifier(EParser.Method_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void exitMethod_identifier(EParser.Method_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_or_keyword(EParser.Identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_or_keyword(EParser.Identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_hyphen_identifier_or_keyword(EParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_hyphen_identifier_or_keyword(EParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_identifier_or_keyword(EParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_identifier_or_keyword(EParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableIdentifier(EParser.VariableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableIdentifier(EParser.VariableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(EParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(EParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbolIdentifier(EParser.SymbolIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link EParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbolIdentifier(EParser.SymbolIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier(EParser.Variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier(EParser.Variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier(EParser.Attribute_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier(EParser.Attribute_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier(EParser.Type_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier(EParser.Type_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_identifier(EParser.Symbol_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_identifier(EParser.Symbol_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAny_identifier(EParser.Any_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAny_identifier(EParser.Any_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(EParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(EParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link EParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterCodeArgument(EParser.CodeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link EParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitCodeArgument(EParser.CodeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link EParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterOperatorArgument(EParser.OperatorArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link EParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitOperatorArgument(EParser.OperatorArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void enterOperator_argument(EParser.Operator_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void exitOperator_argument(EParser.Operator_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void enterNamed_argument(EParser.Named_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void exitNamed_argument(EParser.Named_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void enterCode_argument(EParser.Code_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void exitCode_argument(EParser.Code_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_or_any_type(EParser.Category_or_any_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_or_any_type(EParser.Category_or_any_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyListType(EParser.AnyListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyListType(EParser.AnyListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(EParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyType(EParser.AnyTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyDictType(EParser.AnyDictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link EParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyDictType(EParser.AnyDictTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration_list(EParser.Member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration_list(EParser.Member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration(EParser.Member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration(EParser.Member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration_list(EParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration_list(EParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration(EParser.Native_member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration(EParser.Native_member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaCategoryBinding(EParser.JavaCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaCategoryBinding(EParser.JavaCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCategoryBinding(EParser.CSharpCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCategoryBinding(EParser.CSharpCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython2CategoryBinding(EParser.Python2CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython2CategoryBinding(EParser.Python2CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython3CategoryBinding(EParser.Python3CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython3CategoryBinding(EParser.Python3CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptCategoryBinding(EParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link EParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptCategoryBinding(EParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython_category_binding(EParser.Python_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython_category_binding(EParser.Python_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_module}.
	 * @param ctx the parse tree
	 */
	void enterPython_module(EParser.Python_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_module}.
	 * @param ctx the parse tree
	 */
	void exitPython_module(EParser.Python_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_category_binding(EParser.Javascript_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_category_binding(EParser.Javascript_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_module(EParser.Javascript_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_module(EParser.Javascript_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier_list(EParser.Variable_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier_list(EParser.Variable_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier_list(EParser.Attribute_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier_list(EParser.Attribute_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMethod_declaration(EParser.Method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMethod_declaration(EParser.Method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void enterComment_statement(EParser.Comment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void exitComment_statement(EParser.Comment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_statement_list(EParser.Native_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_statement_list(EParser.Native_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaNativeStatement(EParser.JavaNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaNativeStatement(EParser.JavaNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpNativeStatement(EParser.CSharpNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpNativeStatement(EParser.CSharpNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython2NativeStatement(EParser.Python2NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython2NativeStatement(EParser.Python2NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython3NativeStatement(EParser.Python3NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython3NativeStatement(EParser.Python3NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptNativeStatement(EParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link EParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptNativeStatement(EParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython_native_statement(EParser.Python_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython_native_statement(EParser.Python_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_native_statement(EParser.Javascript_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_native_statement(EParser.Javascript_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(EParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(EParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void enterAssertion_list(EParser.Assertion_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void exitAssertion_list(EParser.Assertion_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_case_statement_list(EParser.Switch_case_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_case_statement_list(EParser.Switch_case_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterCatch_statement_list(EParser.Catch_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitCatch_statement_list(EParser.Catch_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralRangeLiteral(EParser.LiteralRangeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralRangeLiteral(EParser.LiteralRangeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralListLiteral(EParser.LiteralListLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralListLiteral(EParser.LiteralListLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralSetLiteral(EParser.LiteralSetLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link EParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralSetLiteral(EParser.LiteralSetLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMinIntegerLiteral(EParser.MinIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMinIntegerLiteral(EParser.MinIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMaxIntegerLiteral(EParser.MaxIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMaxIntegerLiteral(EParser.MaxIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(EParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(EParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(EParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(EParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterCharacterLiteral(EParser.CharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitCharacterLiteral(EParser.CharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateLiteral(EParser.DateLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateLiteral(EParser.DateLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTimeLiteral(EParser.TimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTimeLiteral(EParser.TimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTextLiteral(EParser.TextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTextLiteral(EParser.TextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(EParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(EParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeLiteral(EParser.DateTimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeLiteral(EParser.DateTimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(EParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(EParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterPeriodLiteral(EParser.PeriodLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitPeriodLiteral(EParser.PeriodLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterVersionLiteral(EParser.VersionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitVersionLiteral(EParser.VersionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterUUIDLiteral(EParser.UUIDLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitUUIDLiteral(EParser.UUIDLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(EParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link EParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(EParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_list_literal(EParser.Literal_list_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_list_literal(EParser.Literal_list_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisExpression(EParser.ParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisExpression(EParser.ParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(EParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(EParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(EParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(EParser.IdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(EParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link EParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(EParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void enterThis_expression(EParser.This_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void exitThis_expression(EParser.This_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis_expression(EParser.Parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis_expression(EParser.Parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_expression(EParser.Literal_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_expression(EParser.Literal_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void enterCollection_literal(EParser.Collection_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void exitCollection_literal(EParser.Collection_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void enterTuple_literal(EParser.Tuple_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void exitTuple_literal(EParser.Tuple_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void enterDict_literal(EParser.Dict_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void exitDict_literal(EParser.Dict_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void enterDocument_literal(EParser.Document_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void exitDocument_literal(EParser.Document_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void enterExpression_tuple(EParser.Expression_tupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void exitExpression_tuple(EParser.Expression_tupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry_list(EParser.Dict_entry_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry_list(EParser.Dict_entry_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry(EParser.Dict_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry(EParser.Dict_entryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link EParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyIdentifier(EParser.DictKeyIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link EParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyIdentifier(EParser.DictKeyIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link EParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyText(EParser.DictKeyTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link EParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyText(EParser.DictKeyTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstAndLast(EParser.SliceFirstAndLastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstAndLast(EParser.SliceFirstAndLastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstOnly(EParser.SliceFirstOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstOnly(EParser.SliceFirstOnlyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceLastOnly(EParser.SliceLastOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link EParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceLastOnly(EParser.SliceLastOnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_variable_statement(EParser.Assign_variable_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_variable_statement(EParser.Assign_variable_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link EParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterChildInstance(EParser.ChildInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link EParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitChildInstance(EParser.ChildInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link EParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterRootInstance(EParser.RootInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link EParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitRootInstance(EParser.RootInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link EParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsATypeExpression(EParser.IsATypeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link EParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsATypeExpression(EParser.IsATypeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link EParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsOtherExpression(EParser.IsOtherExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link EParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsOtherExpression(EParser.IsOtherExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_all_expression(EParser.Read_all_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_all_expression(EParser.Read_all_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_one_expression(EParser.Read_one_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_one_expression(EParser.Read_one_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_list(EParser.Order_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_list(EParser.Order_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#order_by}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by(EParser.Order_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#order_by}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by(EParser.Order_byContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorPlus(EParser.OperatorPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorPlus(EParser.OperatorPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMinus(EParser.OperatorMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMinus(EParser.OperatorMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMultiply(EParser.OperatorMultiplyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMultiply(EParser.OperatorMultiplyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorDivide(EParser.OperatorDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorDivide(EParser.OperatorDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorIDivide(EParser.OperatorIDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorIDivide(EParser.OperatorIDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorModulo(EParser.OperatorModuloContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link EParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorModulo(EParser.OperatorModuloContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(EParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(EParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#new_token}.
	 * @param ctx the parse tree
	 */
	void enterNew_token(EParser.New_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#new_token}.
	 * @param ctx the parse tree
	 */
	void exitNew_token(EParser.New_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#key_token}.
	 * @param ctx the parse tree
	 */
	void enterKey_token(EParser.Key_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#key_token}.
	 * @param ctx the parse tree
	 */
	void exitKey_token(EParser.Key_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#module_token}.
	 * @param ctx the parse tree
	 */
	void enterModule_token(EParser.Module_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#module_token}.
	 * @param ctx the parse tree
	 */
	void exitModule_token(EParser.Module_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#value_token}.
	 * @param ctx the parse tree
	 */
	void enterValue_token(EParser.Value_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#value_token}.
	 * @param ctx the parse tree
	 */
	void exitValue_token(EParser.Value_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void enterSymbols_token(EParser.Symbols_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void exitSymbols_token(EParser.Symbols_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#assign}.
	 * @param ctx the parse tree
	 */
	void enterAssign(EParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#assign}.
	 * @param ctx the parse tree
	 */
	void exitAssign(EParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#multiply}.
	 * @param ctx the parse tree
	 */
	void enterMultiply(EParser.MultiplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#multiply}.
	 * @param ctx the parse tree
	 */
	void exitMultiply(EParser.MultiplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#divide}.
	 * @param ctx the parse tree
	 */
	void enterDivide(EParser.DivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#divide}.
	 * @param ctx the parse tree
	 */
	void exitDivide(EParser.DivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#idivide}.
	 * @param ctx the parse tree
	 */
	void enterIdivide(EParser.IdivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#idivide}.
	 * @param ctx the parse tree
	 */
	void exitIdivide(EParser.IdivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#modulo}.
	 * @param ctx the parse tree
	 */
	void enterModulo(EParser.ModuloContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#modulo}.
	 * @param ctx the parse tree
	 */
	void exitModulo(EParser.ModuloContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link EParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptReturnStatement(EParser.JavascriptReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link EParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptReturnStatement(EParser.JavascriptReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link EParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptStatement(EParser.JavascriptStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link EParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptStatement(EParser.JavascriptStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link EParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptSelectorExpression(EParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link EParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptSelectorExpression(EParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link EParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptPrimaryExpression(EParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link EParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptPrimaryExpression(EParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_primary_expression(EParser.Javascript_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_primary_expression(EParser.Javascript_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_this_expression(EParser.Javascript_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_this_expression(EParser.Javascript_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_new_expression(EParser.Javascript_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_new_expression(EParser.Javascript_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMethodExpression(EParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMethodExpression(EParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMemberExpression(EParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMemberExpression(EParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptItemExpression(EParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link EParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptItemExpression(EParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_method_expression(EParser.Javascript_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_method_expression(EParser.Javascript_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link EParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentList(EParser.JavascriptArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link EParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentList(EParser.JavascriptArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link EParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentListItem(EParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link EParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentListItem(EParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_item_expression(EParser.Javascript_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_item_expression(EParser.Javascript_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_parenthesis_expression(EParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_parenthesis_expression(EParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier_expression(EParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier_expression(EParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptIntegerLiteral(EParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptIntegerLiteral(EParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptDecimalLiteral(EParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptDecimalLiteral(EParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptTextLiteral(EParser.JavascriptTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptTextLiteral(EParser.JavascriptTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptBooleanLiteral(EParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptBooleanLiteral(EParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptCharacterLiteral(EParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link EParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptCharacterLiteral(EParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier(EParser.Javascript_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier(EParser.Javascript_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link EParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonReturnStatement(EParser.PythonReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link EParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonReturnStatement(EParser.PythonReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link EParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonStatement(EParser.PythonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link EParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonStatement(EParser.PythonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link EParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelectorExpression(EParser.PythonSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link EParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelectorExpression(EParser.PythonSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link EParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPrimaryExpression(EParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link EParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPrimaryExpression(EParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelfExpression(EParser.PythonSelfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelfExpression(EParser.PythonSelfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonParenthesisExpression(EParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonParenthesisExpression(EParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifierExpression(EParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifierExpression(EParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonLiteralExpression(EParser.PythonLiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonLiteralExpression(EParser.PythonLiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonGlobalMethodExpression(EParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link EParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonGlobalMethodExpression(EParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_self_expression(EParser.Python_self_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_self_expression(EParser.Python_self_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link EParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonMethodExpression(EParser.PythonMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link EParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonMethodExpression(EParser.PythonMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link EParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonItemExpression(EParser.PythonItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link EParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonItemExpression(EParser.PythonItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_method_expression(EParser.Python_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_method_expression(EParser.Python_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalOnlyArgumentList(EParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalOnlyArgumentList(EParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedOnlyArgumentList(EParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedOnlyArgumentList(EParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonArgumentList(EParser.PythonArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link EParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonArgumentList(EParser.PythonArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link EParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentList(EParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link EParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentList(EParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link EParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentListItem(EParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link EParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentListItem(EParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link EParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentList(EParser.PythonNamedArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link EParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentList(EParser.PythonNamedArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link EParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentListItem(EParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link EParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentListItem(EParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_parenthesis_expression(EParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_parenthesis_expression(EParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonChildIdentifier(EParser.PythonChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonChildIdentifier(EParser.PythonChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPromptoIdentifier(EParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPromptoIdentifier(EParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifier(EParser.PythonIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link EParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifier(EParser.PythonIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIntegerLiteral(EParser.PythonIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIntegerLiteral(EParser.PythonIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonDecimalLiteral(EParser.PythonDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonDecimalLiteral(EParser.PythonDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonTextLiteral(EParser.PythonTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonTextLiteral(EParser.PythonTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonBooleanLiteral(EParser.PythonBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonBooleanLiteral(EParser.PythonBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonCharacterLiteral(EParser.PythonCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link EParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonCharacterLiteral(EParser.PythonCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void enterPython_identifier(EParser.Python_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void exitPython_identifier(EParser.Python_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link EParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaReturnStatement(EParser.JavaReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link EParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaReturnStatement(EParser.JavaReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link EParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaStatement(EParser.JavaStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link EParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaStatement(EParser.JavaStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link EParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaSelectorExpression(EParser.JavaSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link EParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaSelectorExpression(EParser.JavaSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link EParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaPrimaryExpression(EParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link EParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaPrimaryExpression(EParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_primary_expression(EParser.Java_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_primary_expression(EParser.Java_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_this_expression(EParser.Java_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_this_expression(EParser.Java_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_new_expression(EParser.Java_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_new_expression(EParser.Java_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link EParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaMethodExpression(EParser.JavaMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link EParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaMethodExpression(EParser.JavaMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link EParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaItemExpression(EParser.JavaItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link EParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaItemExpression(EParser.JavaItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_method_expression(EParser.Java_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_method_expression(EParser.Java_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link EParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentListItem(EParser.JavaArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link EParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentListItem(EParser.JavaArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link EParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentList(EParser.JavaArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link EParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentList(EParser.JavaArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_item_expression(EParser.Java_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_item_expression(EParser.Java_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_parenthesis_expression(EParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_parenthesis_expression(EParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link EParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIdentifier(EParser.JavaIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link EParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIdentifier(EParser.JavaIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link EParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildIdentifier(EParser.JavaChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link EParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildIdentifier(EParser.JavaChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link EParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaClassIdentifier(EParser.JavaClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link EParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaClassIdentifier(EParser.JavaClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link EParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildClassIdentifier(EParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link EParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildClassIdentifier(EParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIntegerLiteral(EParser.JavaIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIntegerLiteral(EParser.JavaIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaDecimalLiteral(EParser.JavaDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaDecimalLiteral(EParser.JavaDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaTextLiteral(EParser.JavaTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaTextLiteral(EParser.JavaTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaBooleanLiteral(EParser.JavaBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaBooleanLiteral(EParser.JavaBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaCharacterLiteral(EParser.JavaCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link EParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaCharacterLiteral(EParser.JavaCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJava_identifier(EParser.Java_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJava_identifier(EParser.Java_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link EParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpReturnStatement(EParser.CSharpReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link EParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpReturnStatement(EParser.CSharpReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link EParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpStatement(EParser.CSharpStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link EParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpStatement(EParser.CSharpStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link EParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpSelectorExpression(EParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link EParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpSelectorExpression(EParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link EParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPrimaryExpression(EParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link EParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPrimaryExpression(EParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_primary_expression(EParser.Csharp_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_primary_expression(EParser.Csharp_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_this_expression(EParser.Csharp_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_this_expression(EParser.Csharp_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_new_expression(EParser.Csharp_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_new_expression(EParser.Csharp_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link EParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpMethodExpression(EParser.CSharpMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link EParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpMethodExpression(EParser.CSharpMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link EParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpItemExpression(EParser.CSharpItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link EParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpItemExpression(EParser.CSharpItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_method_expression(EParser.Csharp_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_method_expression(EParser.Csharp_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link EParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentList(EParser.CSharpArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link EParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentList(EParser.CSharpArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link EParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentListItem(EParser.CSharpArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link EParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentListItem(EParser.CSharpArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_item_expression(EParser.Csharp_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_item_expression(EParser.Csharp_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_parenthesis_expression(EParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_parenthesis_expression(EParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIdentifier(EParser.CSharpIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIdentifier(EParser.CSharpIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpChildIdentifier(EParser.CSharpChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpChildIdentifier(EParser.CSharpChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPromptoIdentifier(EParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link EParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPromptoIdentifier(EParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIntegerLiteral(EParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIntegerLiteral(EParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpDecimalLiteral(EParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpDecimalLiteral(EParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpTextLiteral(EParser.CSharpTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpTextLiteral(EParser.CSharpTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpBooleanLiteral(EParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpBooleanLiteral(EParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCharacterLiteral(EParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link EParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCharacterLiteral(EParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_identifier(EParser.Csharp_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_identifier(EParser.Csharp_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void enterJsx_expression(EParser.Jsx_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void exitJsx_expression(EParser.Jsx_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link EParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxSelfClosing(EParser.JsxSelfClosingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link EParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxSelfClosing(EParser.JsxSelfClosingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link EParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxElement(EParser.JsxElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link EParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxElement(EParser.JsxElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment(EParser.Jsx_fragmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment(EParser.Jsx_fragmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_start(EParser.Jsx_fragment_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_start(EParser.Jsx_fragment_startContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_end(EParser.Jsx_fragment_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_end(EParser.Jsx_fragment_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_self_closing(EParser.Jsx_self_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_self_closing(EParser.Jsx_self_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void enterJsx_opening(EParser.Jsx_openingContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void exitJsx_opening(EParser.Jsx_openingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_closing(EParser.Jsx_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_closing(EParser.Jsx_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void enterJsx_element_name(EParser.Jsx_element_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void exitJsx_element_name(EParser.Jsx_element_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJsx_identifier(EParser.Jsx_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJsx_identifier(EParser.Jsx_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void enterJsx_attribute(EParser.Jsx_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void exitJsx_attribute(EParser.Jsx_attributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link EParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxLiteral(EParser.JsxLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link EParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxLiteral(EParser.JsxLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link EParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxValue(EParser.JsxValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link EParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxValue(EParser.JsxValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void enterJsx_children(EParser.Jsx_childrenContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void exitJsx_children(EParser.Jsx_childrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxText(EParser.JsxTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxText(EParser.JsxTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxChild(EParser.JsxChildContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxChild(EParser.JsxChildContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxCode(EParser.JsxCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link EParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxCode(EParser.JsxCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void enterJsx_text(EParser.Jsx_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void exitJsx_text(EParser.Jsx_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void enterCss_expression(EParser.Css_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void exitCss_expression(EParser.Css_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#css_field}.
	 * @param ctx the parse tree
	 */
	void enterCss_field(EParser.Css_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#css_field}.
	 * @param ctx the parse tree
	 */
	void exitCss_field(EParser.Css_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCss_identifier(EParser.Css_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCss_identifier(EParser.Css_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link EParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssValue(EParser.CssValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link EParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssValue(EParser.CssValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link EParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssText(EParser.CssTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link EParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssText(EParser.CssTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link EParser#css_text}.
	 * @param ctx the parse tree
	 */
	void enterCss_text(EParser.Css_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link EParser#css_text}.
	 * @param ctx the parse tree
	 */
	void exitCss_text(EParser.Css_textContext ctx);
}