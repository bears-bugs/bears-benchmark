// Generated from MParser.g4 by ANTLR 4.7.1
package prompto.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MParser}.
 */
public interface MParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_category_declaration(MParser.Enum_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#enum_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_category_declaration(MParser.Enum_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_native_declaration(MParser.Enum_native_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#enum_native_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_native_declaration(MParser.Enum_native_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol(MParser.Native_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_symbol}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol(MParser.Native_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol(MParser.Category_symbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#category_symbol}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol(MParser.Category_symbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_declaration(MParser.Attribute_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#attribute_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_declaration(MParser.Attribute_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#index_clause}.
	 * @param ctx the parse tree
	 */
	void enterIndex_clause(MParser.Index_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#index_clause}.
	 * @param ctx the parse tree
	 */
	void exitIndex_clause(MParser.Index_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_widget_declaration(MParser.Concrete_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#concrete_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_widget_declaration(MParser.Concrete_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_widget_declaration(MParser.Native_widget_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_widget_declaration(MParser.Native_widget_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_category_declaration(MParser.Concrete_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#concrete_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_category_declaration(MParser.Concrete_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleton_category_declaration(MParser.Singleton_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#singleton_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleton_category_declaration(MParser.Singleton_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void enterDerived_list(MParser.Derived_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#derived_list}.
	 * @param ctx the parse tree
	 */
	void exitDerived_list(MParser.Derived_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterOperator_method_declaration(MParser.Operator_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#operator_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitOperator_method_declaration(MParser.Operator_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSetter_method_declaration(MParser.Setter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#setter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSetter_method_declaration(MParser.Setter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_setter_declaration(MParser.Native_setter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_setter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_setter_declaration(MParser.Native_setter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterGetter_method_declaration(MParser.Getter_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#getter_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitGetter_method_declaration(MParser.Getter_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_getter_declaration(MParser.Native_getter_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_getter_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_getter_declaration(MParser.Native_getter_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_declaration(MParser.Native_category_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_declaration(MParser.Native_category_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_resource_declaration(MParser.Native_resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_resource_declaration(MParser.Native_resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void enterNative_category_bindings(MParser.Native_category_bindingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_category_bindings}.
	 * @param ctx the parse tree
	 */
	void exitNative_category_bindings(MParser.Native_category_bindingsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link MParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingListItem(MParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingListItem}
	 * labeled alternative in {@link MParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingListItem(MParser.NativeCategoryBindingListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link MParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryBindingList(MParser.NativeCategoryBindingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryBindingList}
	 * labeled alternative in {@link MParser#native_category_binding_list}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryBindingList(MParser.NativeCategoryBindingListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAbstract_method_declaration(MParser.Abstract_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#abstract_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAbstract_method_declaration(MParser.Abstract_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcrete_method_declaration(MParser.Concrete_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#concrete_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcrete_method_declaration(MParser.Concrete_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_method_declaration(MParser.Native_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_method_declaration(MParser.Native_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTest_method_declaration(MParser.Test_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#test_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTest_method_declaration(MParser.Test_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assertion}.
	 * @param ctx the parse tree
	 */
	void enterAssertion(MParser.AssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assertion}.
	 * @param ctx the parse tree
	 */
	void exitAssertion(MParser.AssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void enterTyped_argument(MParser.Typed_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#typed_argument}.
	 * @param ctx the parse tree
	 */
	void exitTyped_argument(MParser.Typed_argumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallStatement(MParser.MethodCallStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCallStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallStatement(MParser.MethodCallStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignInstanceStatement(MParser.AssignInstanceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignInstanceStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignInstanceStatement(MParser.AssignInstanceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignTupleStatement(MParser.AssignTupleStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignTupleStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignTupleStatement(MParser.AssignTupleStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStoreStatement(MParser.StoreStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StoreStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStoreStatement(MParser.StoreStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterFlushStatement(MParser.FlushStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FlushStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitFlushStatement(MParser.FlushStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(MParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BreakStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(MParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(MParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(MParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(MParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SwitchStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(MParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForEachStatement(MParser.ForEachStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForEachStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForEachStatement(MParser.ForEachStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(MParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(MParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(MParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DoWhileStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(MParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterRaiseStatement(MParser.RaiseStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RaiseStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitRaiseStatement(MParser.RaiseStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(MParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(MParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWriteStatement(MParser.WriteStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WriteStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWriteStatement(MParser.WriteStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithResourceStatement(MParser.WithResourceStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithResourceStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithResourceStatement(MParser.WithResourceStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWithSingletonStatement(MParser.WithSingletonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WithSingletonStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWithSingletonStatement(MParser.WithSingletonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterClosureStatement(MParser.ClosureStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitClosureStatement(MParser.ClosureStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommentStatement(MParser.CommentStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommentStatement}
	 * labeled alternative in {@link MParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommentStatement(MParser.CommentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void enterFlush_statement(MParser.Flush_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#flush_statement}.
	 * @param ctx the parse tree
	 */
	void exitFlush_statement(MParser.Flush_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void enterStore_statement(MParser.Store_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#store_statement}.
	 * @param ctx the parse tree
	 */
	void exitStore_statement(MParser.Store_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#method_call}.
	 * @param ctx the parse tree
	 */
	void enterMethod_call(MParser.Method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#method_call}.
	 * @param ctx the parse tree
	 */
	void exitMethod_call(MParser.Method_callContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodName}
	 * labeled alternative in {@link MParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void enterMethodName(MParser.MethodNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodName}
	 * labeled alternative in {@link MParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void exitMethodName(MParser.MethodNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodParent}
	 * labeled alternative in {@link MParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void enterMethodParent(MParser.MethodParentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodParent}
	 * labeled alternative in {@link MParser#method_selector}.
	 * @param ctx the parse tree
	 */
	void exitMethodParent(MParser.MethodParentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableSelector}
	 * labeled alternative in {@link MParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void enterCallableSelector(MParser.CallableSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableSelector}
	 * labeled alternative in {@link MParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void exitCallableSelector(MParser.CallableSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableRoot}
	 * labeled alternative in {@link MParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void enterCallableRoot(MParser.CallableRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableRoot}
	 * labeled alternative in {@link MParser#callable_parent}.
	 * @param ctx the parse tree
	 */
	void exitCallableRoot(MParser.CallableRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableMemberSelector}
	 * labeled alternative in {@link MParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void enterCallableMemberSelector(MParser.CallableMemberSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableMemberSelector}
	 * labeled alternative in {@link MParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void exitCallableMemberSelector(MParser.CallableMemberSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallableItemSelector}
	 * labeled alternative in {@link MParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void enterCallableItemSelector(MParser.CallableItemSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallableItemSelector}
	 * labeled alternative in {@link MParser#callable_selector}.
	 * @param ctx the parse tree
	 */
	void exitCallableItemSelector(MParser.CallableItemSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_resource_statement(MParser.With_resource_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#with_resource_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_resource_statement(MParser.With_resource_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void enterWith_singleton_statement(MParser.With_singleton_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#with_singleton_statement}.
	 * @param ctx the parse tree
	 */
	void exitWith_singleton_statement(MParser.With_singleton_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_statement(MParser.Switch_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#switch_statement}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_statement(MParser.Switch_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link MParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterAtomicSwitchCase(MParser.AtomicSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomicSwitchCase}
	 * labeled alternative in {@link MParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitAtomicSwitchCase(MParser.AtomicSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link MParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void enterCollectionSwitchCase(MParser.CollectionSwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CollectionSwitchCase}
	 * labeled alternative in {@link MParser#switch_case_statement}.
	 * @param ctx the parse tree
	 */
	void exitCollectionSwitchCase(MParser.CollectionSwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void enterFor_each_statement(MParser.For_each_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#for_each_statement}.
	 * @param ctx the parse tree
	 */
	void exitFor_each_statement(MParser.For_each_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void enterDo_while_statement(MParser.Do_while_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#do_while_statement}.
	 * @param ctx the parse tree
	 */
	void exitDo_while_statement(MParser.Do_while_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void enterWhile_statement(MParser.While_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#while_statement}.
	 * @param ctx the parse tree
	 */
	void exitWhile_statement(MParser.While_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(MParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(MParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link MParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementList(MParser.ElseIfStatementListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementList}
	 * labeled alternative in {@link MParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementList(MParser.ElseIfStatementListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link MParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatementListItem(MParser.ElseIfStatementListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ElseIfStatementListItem}
	 * labeled alternative in {@link MParser#else_if_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatementListItem(MParser.ElseIfStatementListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void enterRaise_statement(MParser.Raise_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#raise_statement}.
	 * @param ctx the parse tree
	 */
	void exitRaise_statement(MParser.Raise_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void enterTry_statement(MParser.Try_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#try_statement}.
	 * @param ctx the parse tree
	 */
	void exitTry_statement(MParser.Try_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link MParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchAtomicStatement(MParser.CatchAtomicStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchAtomicStatement}
	 * labeled alternative in {@link MParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchAtomicStatement(MParser.CatchAtomicStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link MParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void enterCatchCollectionStatement(MParser.CatchCollectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CatchCollectionStatement}
	 * labeled alternative in {@link MParser#catch_statement}.
	 * @param ctx the parse tree
	 */
	void exitCatchCollectionStatement(MParser.CatchCollectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_statement(MParser.Break_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_statement(MParser.Break_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(MParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(MParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntDivideExpression(MParser.IntDivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntDivideExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntDivideExpression(MParser.IntDivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAnyExpression(MParser.HasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAnyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAnyExpression(MParser.HasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasExpression(MParser.HasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasExpression(MParser.HasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpression(MParser.TernaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpression(MParser.TernaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualsExpression(MParser.NotEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualsExpression(MParser.NotEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInExpression(MParser.InExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInExpression(MParser.InExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterJsxExpression(MParser.JsxExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitJsxExpression(MParser.JsxExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(MParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(MParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanExpression(MParser.GreaterThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanExpression(MParser.GreaterThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(MParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(MParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCodeExpression(MParser.CodeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCodeExpression(MParser.CodeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualExpression(MParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOrEqualExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualExpression(MParser.LessThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAnyExpression(MParser.NotHasAnyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAnyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAnyExpression(MParser.NotHasAnyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(MParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(MParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasExpression(MParser.NotHasExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasExpression(MParser.NotHasExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterClosureExpression(MParser.ClosureExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClosureExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitClosureExpression(MParser.ClosureExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotHasAllExpression(MParser.NotHasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotHasAllExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotHasAllExpression(MParser.NotHasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterContainsExpression(MParser.ContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ContainsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitContainsExpression(MParser.ContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FilteredListExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFilteredListExpression(MParser.FilteredListExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FilteredListExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFilteredListExpression(MParser.FilteredListExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotContainsExpression(MParser.NotContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotContainsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotContainsExpression(MParser.NotContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyExpression(MParser.MultiplyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplyExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyExpression(MParser.MultiplyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRoughlyEqualsExpression(MParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RoughlyEqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRoughlyEqualsExpression(MParser.RoughlyEqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExecuteExpression(MParser.ExecuteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExecuteExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExecuteExpression(MParser.ExecuteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodExpression(MParser.MethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodExpression(MParser.MethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualExpression(MParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanOrEqualExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualExpression(MParser.GreaterThanOrEqualExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotInExpression(MParser.NotInExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotInExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotInExpression(MParser.NotInExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIteratorExpression(MParser.IteratorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIteratorExpression(MParser.IteratorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsNotExpression(MParser.IsNotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsNotExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsNotExpression(MParser.IsNotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivideExpression(MParser.DivideExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DivideExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivideExpression(MParser.DivideExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIsExpression(MParser.IsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIsExpression(MParser.IsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(MParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinusExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(MParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpression(MParser.AddExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpression(MParser.AddExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterHasAllExpression(MParser.HasAllExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HasAllExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitHasAllExpression(MParser.HasAllExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceExpression(MParser.InstanceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InstanceExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceExpression(MParser.InstanceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCssExpression(MParser.CssExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCssExpression(MParser.CssExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(MParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CastExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(MParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterModuloExpression(MParser.ModuloExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModuloExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitModuloExpression(MParser.ModuloExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanExpression(MParser.LessThanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanExpression(MParser.LessThanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualsExpression(MParser.EqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link MParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualsExpression(MParser.EqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#closure_expression}.
	 * @param ctx the parse tree
	 */
	void enterClosure_expression(MParser.Closure_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#closure_expression}.
	 * @param ctx the parse tree
	 */
	void exitClosure_expression(MParser.Closure_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link MParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectorExpression(MParser.SelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectorExpression}
	 * labeled alternative in {@link MParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectorExpression(MParser.SelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link MParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void enterSelectableExpression(MParser.SelectableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelectableExpression}
	 * labeled alternative in {@link MParser#instance_expression}.
	 * @param ctx the parse tree
	 */
	void exitSelectableExpression(MParser.SelectableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#method_expression}.
	 * @param ctx the parse tree
	 */
	void enterMethod_expression(MParser.Method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#method_expression}.
	 * @param ctx the parse tree
	 */
	void exitMethod_expression(MParser.Method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterMemberSelector(MParser.MemberSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitMemberSelector(MParser.MemberSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterSliceSelector(MParser.SliceSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitSliceSelector(MParser.SliceSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void enterItemSelector(MParser.ItemSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemSelector}
	 * labeled alternative in {@link MParser#instance_selector}.
	 * @param ctx the parse tree
	 */
	void exitItemSelector(MParser.ItemSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void enterBlob_expression(MParser.Blob_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#blob_expression}.
	 * @param ctx the parse tree
	 */
	void exitBlob_expression(MParser.Blob_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void enterDocument_expression(MParser.Document_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#document_expression}.
	 * @param ctx the parse tree
	 */
	void exitDocument_expression(MParser.Document_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link MParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorFrom(MParser.ConstructorFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorFrom}
	 * labeled alternative in {@link MParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorFrom(MParser.ConstructorFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link MParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorNoFrom(MParser.ConstructorNoFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstructorNoFrom}
	 * labeled alternative in {@link MParser#constructor_expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorNoFrom(MParser.ConstructorNoFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void enterCopy_from(MParser.Copy_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#copy_from}.
	 * @param ctx the parse tree
	 */
	void exitCopy_from(MParser.Copy_fromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpressionAssignmentList}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterExpressionAssignmentList(MParser.ExpressionAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpressionAssignmentList}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitExpressionAssignmentList(MParser.ExpressionAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentList(MParser.ArgumentAssignmentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentList}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentList(MParser.ArgumentAssignmentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void enterArgumentAssignmentListItem(MParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentAssignmentListItem}
	 * labeled alternative in {@link MParser#argument_assignment_list}.
	 * @param ctx the parse tree
	 */
	void exitArgumentAssignmentListItem(MParser.ArgumentAssignmentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void enterArgument_assignment(MParser.Argument_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#argument_assignment}.
	 * @param ctx the parse tree
	 */
	void exitArgument_assignment(MParser.Argument_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void enterWrite_statement(MParser.Write_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#write_statement}.
	 * @param ctx the parse tree
	 */
	void exitWrite_statement(MParser.Write_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#filtered_list_suffix}.
	 * @param ctx the parse tree
	 */
	void enterFiltered_list_suffix(MParser.Filtered_list_suffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#filtered_list_suffix}.
	 * @param ctx the parse tree
	 */
	void exitFiltered_list_suffix(MParser.Filtered_list_suffixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link MParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchOne(MParser.FetchOneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchOne}
	 * labeled alternative in {@link MParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchOne(MParser.FetchOneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link MParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void enterFetchMany(MParser.FetchManyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FetchMany}
	 * labeled alternative in {@link MParser#fetch_store_expression}.
	 * @param ctx the parse tree
	 */
	void exitFetchMany(MParser.FetchManyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void enterSorted_expression(MParser.Sorted_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#sorted_expression}.
	 * @param ctx the parse tree
	 */
	void exitSorted_expression(MParser.Sorted_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_instance_statement(MParser.Assign_instance_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assign_instance_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_instance_statement(MParser.Assign_instance_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link MParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterMemberInstance(MParser.MemberInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberInstance}
	 * labeled alternative in {@link MParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitMemberInstance(MParser.MemberInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link MParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void enterItemInstance(MParser.ItemInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ItemInstance}
	 * labeled alternative in {@link MParser#child_instance}.
	 * @param ctx the parse tree
	 */
	void exitItemInstance(MParser.ItemInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_tuple_statement(MParser.Assign_tuple_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assign_tuple_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_tuple_statement(MParser.Assign_tuple_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#lfs}.
	 * @param ctx the parse tree
	 */
	void enterLfs(MParser.LfsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#lfs}.
	 * @param ctx the parse tree
	 */
	void exitLfs(MParser.LfsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#lfp}.
	 * @param ctx the parse tree
	 */
	void enterLfp(MParser.LfpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#lfp}.
	 * @param ctx the parse tree
	 */
	void exitLfp(MParser.LfpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void enterJsx_ws(MParser.Jsx_wsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_ws}.
	 * @param ctx the parse tree
	 */
	void exitJsx_ws(MParser.Jsx_wsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#indent}.
	 * @param ctx the parse tree
	 */
	void enterIndent(MParser.IndentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#indent}.
	 * @param ctx the parse tree
	 */
	void exitIndent(MParser.IndentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#dedent}.
	 * @param ctx the parse tree
	 */
	void enterDedent(MParser.DedentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#dedent}.
	 * @param ctx the parse tree
	 */
	void exitDedent(MParser.DedentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void enterNull_literal(MParser.Null_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#null_literal}.
	 * @param ctx the parse tree
	 */
	void exitNull_literal(MParser.Null_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link MParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterFullDeclarationList(MParser.FullDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FullDeclarationList}
	 * labeled alternative in {@link MParser#declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitFullDeclarationList(MParser.FullDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(MParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(MParser.DeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(MParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(MParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_constructor(MParser.Annotation_constructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#annotation_constructor}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_constructor(MParser.Annotation_constructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation_identifier(MParser.Annotation_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#annotation_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation_identifier(MParser.Annotation_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void enterResource_declaration(MParser.Resource_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#resource_declaration}.
	 * @param ctx the parse tree
	 */
	void exitResource_declaration(MParser.Resource_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void enterEnum_declaration(MParser.Enum_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#enum_declaration}.
	 * @param ctx the parse tree
	 */
	void exitEnum_declaration(MParser.Enum_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_symbol_list(MParser.Native_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_symbol_list(MParser.Native_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterCategory_symbol_list(MParser.Category_symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#category_symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitCategory_symbol_list(MParser.Category_symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_list(MParser.Symbol_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#symbol_list}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_list(MParser.Symbol_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingList(MParser.MatchingListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingList}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingList(MParser.MatchingListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingSet(MParser.MatchingSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingSet}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingSet(MParser.MatchingSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingRange(MParser.MatchingRangeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingRange}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingRange(MParser.MatchingRangeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingPattern(MParser.MatchingPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingPattern}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingPattern(MParser.MatchingPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void enterMatchingExpression(MParser.MatchingExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MatchingExpression}
	 * labeled alternative in {@link MParser#attribute_constraint}.
	 * @param ctx the parse tree
	 */
	void exitMatchingExpression(MParser.MatchingExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void enterList_literal(MParser.List_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#list_literal}.
	 * @param ctx the parse tree
	 */
	void exitList_literal(MParser.List_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void enterSet_literal(MParser.Set_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#set_literal}.
	 * @param ctx the parse tree
	 */
	void exitSet_literal(MParser.Set_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(MParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(MParser.Expression_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void enterRange_literal(MParser.Range_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#range_literal}.
	 * @param ctx the parse tree
	 */
	void exitRange_literal(MParser.Range_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterIteratorType(MParser.IteratorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IteratorType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitIteratorType(MParser.IteratorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterSetType(MParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitSetType(MParser.SetTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterListType(MParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitListType(MParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterDictType(MParser.DictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitDictType(MParser.DictTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterCursorType(MParser.CursorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CursorType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitCursorType(MParser.CursorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryType(MParser.PrimaryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryType}
	 * labeled alternative in {@link MParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryType(MParser.PrimaryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link MParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterNativeType(MParser.NativeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeType}
	 * labeled alternative in {@link MParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitNativeType(MParser.NativeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link MParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void enterCategoryType(MParser.CategoryTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CategoryType}
	 * labeled alternative in {@link MParser#primary_type}.
	 * @param ctx the parse tree
	 */
	void exitCategoryType(MParser.CategoryTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(MParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(MParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCharacterType(MParser.CharacterTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCharacterType(MParser.CharacterTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTextType(MParser.TextTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTextType(MParser.TextTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterImageType(MParser.ImageTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImageType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitImageType(MParser.ImageTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterIntegerType(MParser.IntegerTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitIntegerType(MParser.IntegerTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDecimalType(MParser.DecimalTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDecimalType(MParser.DecimalTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDocumentType(MParser.DocumentTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DocumentType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDocumentType(MParser.DocumentTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateType(MParser.DateTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateType(MParser.DateTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeType(MParser.DateTimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeType(MParser.DateTimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterTimeType(MParser.TimeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitTimeType(MParser.TimeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterPeriodType(MParser.PeriodTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitPeriodType(MParser.PeriodTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterVersionType(MParser.VersionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitVersionType(MParser.VersionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterCodeType(MParser.CodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitCodeType(MParser.CodeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterBlobType(MParser.BlobTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlobType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitBlobType(MParser.BlobTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterUUIDType(MParser.UUIDTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitUUIDType(MParser.UUIDTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void enterHtmlType(MParser.HtmlTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HtmlType}
	 * labeled alternative in {@link MParser#native_type}.
	 * @param ctx the parse tree
	 */
	void exitHtmlType(MParser.HtmlTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#category_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_type(MParser.Category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#category_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_type(MParser.Category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void enterMutable_category_type(MParser.Mutable_category_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#mutable_category_type}.
	 * @param ctx the parse tree
	 */
	void exitMutable_category_type(MParser.Mutable_category_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#code_type}.
	 * @param ctx the parse tree
	 */
	void enterCode_type(MParser.Code_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#code_type}.
	 * @param ctx the parse tree
	 */
	void exitCode_type(MParser.Code_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteCategoryDeclaration(MParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteCategoryDeclaration(MParser.ConcreteCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeCategoryDeclaration(MParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeCategoryDeclaration(MParser.NativeCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSingletonCategoryDeclaration(MParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingletonCategoryDeclaration}
	 * labeled alternative in {@link MParser#category_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSingletonCategoryDeclaration(MParser.SingletonCategoryDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link MParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterConcreteWidgetDeclaration(MParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConcreteWidgetDeclaration}
	 * labeled alternative in {@link MParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitConcreteWidgetDeclaration(MParser.ConcreteWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link MParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNativeWidgetDeclaration(MParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NativeWidgetDeclaration}
	 * labeled alternative in {@link MParser#widget_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNativeWidgetDeclaration(MParser.NativeWidgetDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier_list(MParser.Type_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#type_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier_list(MParser.Type_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void enterMethod_identifier(MParser.Method_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#method_identifier}.
	 * @param ctx the parse tree
	 */
	void exitMethod_identifier(MParser.Method_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_or_keyword(MParser.Identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_or_keyword(MParser.Identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_hyphen_identifier_or_keyword(MParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#nospace_hyphen_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_hyphen_identifier_or_keyword(MParser.Nospace_hyphen_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void enterNospace_identifier_or_keyword(MParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#nospace_identifier_or_keyword}.
	 * @param ctx the parse tree
	 */
	void exitNospace_identifier_or_keyword(MParser.Nospace_identifier_or_keywordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableIdentifier(MParser.VariableIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableIdentifier(MParser.VariableIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(MParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(MParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbolIdentifier(MParser.SymbolIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SymbolIdentifier}
	 * labeled alternative in {@link MParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbolIdentifier(MParser.SymbolIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier(MParser.Variable_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#variable_identifier}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier(MParser.Variable_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier(MParser.Attribute_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#attribute_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier(MParser.Attribute_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void enterType_identifier(MParser.Type_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#type_identifier}.
	 * @param ctx the parse tree
	 */
	void exitType_identifier(MParser.Type_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_identifier(MParser.Symbol_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#symbol_identifier}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_identifier(MParser.Symbol_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void enterAny_identifier(MParser.Any_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#any_identifier}.
	 * @param ctx the parse tree
	 */
	void exitAny_identifier(MParser.Any_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(MParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(MParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link MParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterCodeArgument(MParser.CodeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CodeArgument}
	 * labeled alternative in {@link MParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitCodeArgument(MParser.CodeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link MParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterOperatorArgument(MParser.OperatorArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorArgument}
	 * labeled alternative in {@link MParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitOperatorArgument(MParser.OperatorArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void enterOperator_argument(MParser.Operator_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#operator_argument}.
	 * @param ctx the parse tree
	 */
	void exitOperator_argument(MParser.Operator_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void enterNamed_argument(MParser.Named_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#named_argument}.
	 * @param ctx the parse tree
	 */
	void exitNamed_argument(MParser.Named_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void enterCode_argument(MParser.Code_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#code_argument}.
	 * @param ctx the parse tree
	 */
	void exitCode_argument(MParser.Code_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void enterCategory_or_any_type(MParser.Category_or_any_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#category_or_any_type}.
	 * @param ctx the parse tree
	 */
	void exitCategory_or_any_type(MParser.Category_or_any_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyListType(MParser.AnyListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyListType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyListType(MParser.AnyListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(MParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyType(MParser.AnyTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAnyDictType(MParser.AnyDictTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyDictType}
	 * labeled alternative in {@link MParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAnyDictType(MParser.AnyDictTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration_list(MParser.Member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration_list(MParser.Member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMember_method_declaration(MParser.Member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMember_method_declaration(MParser.Member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration_list(MParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_member_method_declaration_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration_list(MParser.Native_member_method_declaration_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterNative_member_method_declaration(MParser.Native_member_method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_member_method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitNative_member_method_declaration(MParser.Native_member_method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaCategoryBinding(MParser.JavaCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaCategoryBinding(MParser.JavaCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCategoryBinding(MParser.CSharpCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCategoryBinding(MParser.CSharpCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython2CategoryBinding(MParser.Python2CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2CategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython2CategoryBinding(MParser.Python2CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython3CategoryBinding(MParser.Python3CategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3CategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython3CategoryBinding(MParser.Python3CategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptCategoryBinding(MParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptCategoryBinding}
	 * labeled alternative in {@link MParser#native_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptCategoryBinding(MParser.JavaScriptCategoryBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterPython_category_binding(MParser.Python_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitPython_category_binding(MParser.Python_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_module}.
	 * @param ctx the parse tree
	 */
	void enterPython_module(MParser.Python_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_module}.
	 * @param ctx the parse tree
	 */
	void exitPython_module(MParser.Python_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_category_binding(MParser.Javascript_category_bindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_category_binding}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_category_binding(MParser.Javascript_category_bindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_module(MParser.Javascript_moduleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_module}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_module(MParser.Javascript_moduleContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterVariable_identifier_list(MParser.Variable_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#variable_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitVariable_identifier_list(MParser.Variable_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_identifier_list(MParser.Attribute_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#attribute_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_identifier_list(MParser.Attribute_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void enterMethod_declaration(MParser.Method_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#method_declaration}.
	 * @param ctx the parse tree
	 */
	void exitMethod_declaration(MParser.Method_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void enterComment_statement(MParser.Comment_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#comment_statement}.
	 * @param ctx the parse tree
	 */
	void exitComment_statement(MParser.Comment_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterNative_statement_list(MParser.Native_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#native_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitNative_statement_list(MParser.Native_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaNativeStatement(MParser.JavaNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaNativeStatement(MParser.JavaNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpNativeStatement(MParser.CSharpNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpNativeStatement(MParser.CSharpNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython2NativeStatement(MParser.Python2NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python2NativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython2NativeStatement(MParser.Python2NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython3NativeStatement(MParser.Python3NativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Python3NativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython3NativeStatement(MParser.Python3NativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptNativeStatement(MParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptNativeStatement}
	 * labeled alternative in {@link MParser#native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptNativeStatement(MParser.JavaScriptNativeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterPython_native_statement(MParser.Python_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitPython_native_statement(MParser.Python_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_native_statement(MParser.Javascript_native_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_native_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_native_statement(MParser.Javascript_native_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(MParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(MParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void enterAssertion_list(MParser.Assertion_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assertion_list}.
	 * @param ctx the parse tree
	 */
	void exitAssertion_list(MParser.Assertion_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterSwitch_case_statement_list(MParser.Switch_case_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#switch_case_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitSwitch_case_statement_list(MParser.Switch_case_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void enterCatch_statement_list(MParser.Catch_statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#catch_statement_list}.
	 * @param ctx the parse tree
	 */
	void exitCatch_statement_list(MParser.Catch_statement_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralRangeLiteral(MParser.LiteralRangeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralRangeLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralRangeLiteral(MParser.LiteralRangeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralListLiteral(MParser.LiteralListLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralListLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralListLiteral(MParser.LiteralListLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void enterLiteralSetLiteral(MParser.LiteralSetLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralSetLiteral}
	 * labeled alternative in {@link MParser#literal_collection}.
	 * @param ctx the parse tree
	 */
	void exitLiteralSetLiteral(MParser.LiteralSetLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMinIntegerLiteral(MParser.MinIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MinIntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMinIntegerLiteral(MParser.MinIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterMaxIntegerLiteral(MParser.MaxIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MaxIntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitMaxIntegerLiteral(MParser.MaxIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(MParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(MParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiteral(MParser.HexadecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code HexadecimalLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiteral(MParser.HexadecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterCharacterLiteral(MParser.CharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharacterLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitCharacterLiteral(MParser.CharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateLiteral(MParser.DateLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateLiteral(MParser.DateLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTimeLiteral(MParser.TimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TimeLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTimeLiteral(MParser.TimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterTextLiteral(MParser.TextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TextLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitTextLiteral(MParser.TextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(MParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DecimalLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(MParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeLiteral(MParser.DateTimeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DateTimeLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeLiteral(MParser.DateTimeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(MParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(MParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterPeriodLiteral(MParser.PeriodLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PeriodLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitPeriodLiteral(MParser.PeriodLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterVersionLiteral(MParser.VersionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VersionLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitVersionLiteral(MParser.VersionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterUUIDLiteral(MParser.UUIDLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UUIDLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitUUIDLiteral(MParser.UUIDLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(MParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link MParser#atomic_literal}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(MParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_list_literal(MParser.Literal_list_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#literal_list_literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_list_literal(MParser.Literal_list_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisExpression(MParser.ParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisExpression(MParser.ParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(MParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(MParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(MParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(MParser.IdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(MParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link MParser#selectable_expression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(MParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void enterThis_expression(MParser.This_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#this_expression}.
	 * @param ctx the parse tree
	 */
	void exitThis_expression(MParser.This_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis_expression(MParser.Parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis_expression(MParser.Parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_expression(MParser.Literal_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_expression(MParser.Literal_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void enterCollection_literal(MParser.Collection_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#collection_literal}.
	 * @param ctx the parse tree
	 */
	void exitCollection_literal(MParser.Collection_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void enterTuple_literal(MParser.Tuple_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#tuple_literal}.
	 * @param ctx the parse tree
	 */
	void exitTuple_literal(MParser.Tuple_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void enterDict_literal(MParser.Dict_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#dict_literal}.
	 * @param ctx the parse tree
	 */
	void exitDict_literal(MParser.Dict_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void enterDocument_literal(MParser.Document_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#document_literal}.
	 * @param ctx the parse tree
	 */
	void exitDocument_literal(MParser.Document_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void enterExpression_tuple(MParser.Expression_tupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#expression_tuple}.
	 * @param ctx the parse tree
	 */
	void exitExpression_tuple(MParser.Expression_tupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry_list(MParser.Dict_entry_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#dict_entry_list}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry_list(MParser.Dict_entry_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void enterDict_entry(MParser.Dict_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#dict_entry}.
	 * @param ctx the parse tree
	 */
	void exitDict_entry(MParser.Dict_entryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link MParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyIdentifier(MParser.DictKeyIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyIdentifier}
	 * labeled alternative in {@link MParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyIdentifier(MParser.DictKeyIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link MParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void enterDictKeyText(MParser.DictKeyTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictKeyText}
	 * labeled alternative in {@link MParser#dict_key}.
	 * @param ctx the parse tree
	 */
	void exitDictKeyText(MParser.DictKeyTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstAndLast(MParser.SliceFirstAndLastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstAndLast}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstAndLast(MParser.SliceFirstAndLastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceFirstOnly(MParser.SliceFirstOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceFirstOnly}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceFirstOnly(MParser.SliceFirstOnlyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void enterSliceLastOnly(MParser.SliceLastOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SliceLastOnly}
	 * labeled alternative in {@link MParser#slice_arguments}.
	 * @param ctx the parse tree
	 */
	void exitSliceLastOnly(MParser.SliceLastOnlyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void enterAssign_variable_statement(MParser.Assign_variable_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assign_variable_statement}.
	 * @param ctx the parse tree
	 */
	void exitAssign_variable_statement(MParser.Assign_variable_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link MParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterChildInstance(MParser.ChildInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ChildInstance}
	 * labeled alternative in {@link MParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitChildInstance(MParser.ChildInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link MParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void enterRootInstance(MParser.RootInstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RootInstance}
	 * labeled alternative in {@link MParser#assignable_instance}.
	 * @param ctx the parse tree
	 */
	void exitRootInstance(MParser.RootInstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link MParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsATypeExpression(MParser.IsATypeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsATypeExpression}
	 * labeled alternative in {@link MParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsATypeExpression(MParser.IsATypeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link MParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void enterIsOtherExpression(MParser.IsOtherExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsOtherExpression}
	 * labeled alternative in {@link MParser#is_expression}.
	 * @param ctx the parse tree
	 */
	void exitIsOtherExpression(MParser.IsOtherExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_all_expression(MParser.Read_all_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#read_all_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_all_expression(MParser.Read_all_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void enterRead_one_expression(MParser.Read_one_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#read_one_expression}.
	 * @param ctx the parse tree
	 */
	void exitRead_one_expression(MParser.Read_one_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by_list(MParser.Order_by_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#order_by_list}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by_list(MParser.Order_by_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#order_by}.
	 * @param ctx the parse tree
	 */
	void enterOrder_by(MParser.Order_byContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#order_by}.
	 * @param ctx the parse tree
	 */
	void exitOrder_by(MParser.Order_byContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorPlus(MParser.OperatorPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorPlus}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorPlus(MParser.OperatorPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMinus(MParser.OperatorMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMinus}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMinus(MParser.OperatorMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorMultiply(MParser.OperatorMultiplyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorMultiply}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorMultiply(MParser.OperatorMultiplyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorDivide(MParser.OperatorDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorDivide}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorDivide(MParser.OperatorDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorIDivide(MParser.OperatorIDivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorIDivide}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorIDivide(MParser.OperatorIDivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperatorModulo(MParser.OperatorModuloContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OperatorModulo}
	 * labeled alternative in {@link MParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperatorModulo(MParser.OperatorModuloContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(MParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(MParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#new_token}.
	 * @param ctx the parse tree
	 */
	void enterNew_token(MParser.New_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#new_token}.
	 * @param ctx the parse tree
	 */
	void exitNew_token(MParser.New_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#key_token}.
	 * @param ctx the parse tree
	 */
	void enterKey_token(MParser.Key_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#key_token}.
	 * @param ctx the parse tree
	 */
	void exitKey_token(MParser.Key_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#module_token}.
	 * @param ctx the parse tree
	 */
	void enterModule_token(MParser.Module_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#module_token}.
	 * @param ctx the parse tree
	 */
	void exitModule_token(MParser.Module_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#value_token}.
	 * @param ctx the parse tree
	 */
	void enterValue_token(MParser.Value_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#value_token}.
	 * @param ctx the parse tree
	 */
	void exitValue_token(MParser.Value_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void enterSymbols_token(MParser.Symbols_tokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#symbols_token}.
	 * @param ctx the parse tree
	 */
	void exitSymbols_token(MParser.Symbols_tokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#assign}.
	 * @param ctx the parse tree
	 */
	void enterAssign(MParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#assign}.
	 * @param ctx the parse tree
	 */
	void exitAssign(MParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#multiply}.
	 * @param ctx the parse tree
	 */
	void enterMultiply(MParser.MultiplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#multiply}.
	 * @param ctx the parse tree
	 */
	void exitMultiply(MParser.MultiplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#divide}.
	 * @param ctx the parse tree
	 */
	void enterDivide(MParser.DivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#divide}.
	 * @param ctx the parse tree
	 */
	void exitDivide(MParser.DivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#idivide}.
	 * @param ctx the parse tree
	 */
	void enterIdivide(MParser.IdivideContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#idivide}.
	 * @param ctx the parse tree
	 */
	void exitIdivide(MParser.IdivideContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#modulo}.
	 * @param ctx the parse tree
	 */
	void enterModulo(MParser.ModuloContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#modulo}.
	 * @param ctx the parse tree
	 */
	void exitModulo(MParser.ModuloContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link MParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptReturnStatement(MParser.JavascriptReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptReturnStatement}
	 * labeled alternative in {@link MParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptReturnStatement(MParser.JavascriptReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link MParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptStatement(MParser.JavascriptStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptStatement}
	 * labeled alternative in {@link MParser#javascript_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptStatement(MParser.JavascriptStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link MParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptSelectorExpression(MParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptSelectorExpression}
	 * labeled alternative in {@link MParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptSelectorExpression(MParser.JavascriptSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link MParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptPrimaryExpression(MParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptPrimaryExpression}
	 * labeled alternative in {@link MParser#javascript_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptPrimaryExpression(MParser.JavascriptPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_primary_expression(MParser.Javascript_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_primary_expression(MParser.Javascript_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_this_expression(MParser.Javascript_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_this_expression(MParser.Javascript_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_new_expression(MParser.Javascript_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_new_expression(MParser.Javascript_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMethodExpression(MParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMethodExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMethodExpression(MParser.JavaScriptMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptMemberExpression(MParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptMemberExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptMemberExpression(MParser.JavaScriptMemberExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaScriptItemExpression(MParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaScriptItemExpression}
	 * labeled alternative in {@link MParser#javascript_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaScriptItemExpression(MParser.JavaScriptItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_method_expression(MParser.Javascript_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_method_expression(MParser.Javascript_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link MParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentList(MParser.JavascriptArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentList}
	 * labeled alternative in {@link MParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentList(MParser.JavascriptArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link MParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptArgumentListItem(MParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptArgumentListItem}
	 * labeled alternative in {@link MParser#javascript_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptArgumentListItem(MParser.JavascriptArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_item_expression(MParser.Javascript_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_item_expression(MParser.Javascript_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_parenthesis_expression(MParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_parenthesis_expression(MParser.Javascript_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier_expression(MParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier_expression(MParser.Javascript_identifier_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptIntegerLiteral(MParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptIntegerLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptIntegerLiteral(MParser.JavascriptIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptDecimalLiteral(MParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptDecimalLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptDecimalLiteral(MParser.JavascriptDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptTextLiteral(MParser.JavascriptTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptTextLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptTextLiteral(MParser.JavascriptTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptBooleanLiteral(MParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptBooleanLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptBooleanLiteral(MParser.JavascriptBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavascriptCharacterLiteral(MParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavascriptCharacterLiteral}
	 * labeled alternative in {@link MParser#javascript_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavascriptCharacterLiteral(MParser.JavascriptCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJavascript_identifier(MParser.Javascript_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#javascript_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJavascript_identifier(MParser.Javascript_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link MParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonReturnStatement(MParser.PythonReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonReturnStatement}
	 * labeled alternative in {@link MParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonReturnStatement(MParser.PythonReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link MParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void enterPythonStatement(MParser.PythonStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonStatement}
	 * labeled alternative in {@link MParser#python_statement}.
	 * @param ctx the parse tree
	 */
	void exitPythonStatement(MParser.PythonStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link MParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelectorExpression(MParser.PythonSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelectorExpression}
	 * labeled alternative in {@link MParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelectorExpression(MParser.PythonSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link MParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPrimaryExpression(MParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPrimaryExpression}
	 * labeled alternative in {@link MParser#python_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPrimaryExpression(MParser.PythonPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonSelfExpression(MParser.PythonSelfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonSelfExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonSelfExpression(MParser.PythonSelfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonParenthesisExpression(MParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonParenthesisExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonParenthesisExpression(MParser.PythonParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifierExpression(MParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifierExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifierExpression(MParser.PythonIdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonLiteralExpression(MParser.PythonLiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonLiteralExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonLiteralExpression(MParser.PythonLiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonGlobalMethodExpression(MParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonGlobalMethodExpression}
	 * labeled alternative in {@link MParser#python_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonGlobalMethodExpression(MParser.PythonGlobalMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_self_expression(MParser.Python_self_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_self_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_self_expression(MParser.Python_self_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link MParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonMethodExpression(MParser.PythonMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonMethodExpression}
	 * labeled alternative in {@link MParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonMethodExpression(MParser.PythonMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link MParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonItemExpression(MParser.PythonItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonItemExpression}
	 * labeled alternative in {@link MParser#python_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonItemExpression(MParser.PythonItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_method_expression(MParser.Python_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_method_expression(MParser.Python_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalOnlyArgumentList(MParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalOnlyArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalOnlyArgumentList(MParser.PythonOrdinalOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedOnlyArgumentList(MParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedOnlyArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedOnlyArgumentList(MParser.PythonNamedOnlyArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonArgumentList(MParser.PythonArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonArgumentList}
	 * labeled alternative in {@link MParser#python_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonArgumentList(MParser.PythonArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link MParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentList(MParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentList}
	 * labeled alternative in {@link MParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentList(MParser.PythonOrdinalArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link MParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonOrdinalArgumentListItem(MParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonOrdinalArgumentListItem}
	 * labeled alternative in {@link MParser#python_ordinal_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonOrdinalArgumentListItem(MParser.PythonOrdinalArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link MParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentList(MParser.PythonNamedArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentList}
	 * labeled alternative in {@link MParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentList(MParser.PythonNamedArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link MParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void enterPythonNamedArgumentListItem(MParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonNamedArgumentListItem}
	 * labeled alternative in {@link MParser#python_named_argument_list}.
	 * @param ctx the parse tree
	 */
	void exitPythonNamedArgumentListItem(MParser.PythonNamedArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterPython_parenthesis_expression(MParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitPython_parenthesis_expression(MParser.Python_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonChildIdentifier(MParser.PythonChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonChildIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonChildIdentifier(MParser.PythonChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonPromptoIdentifier(MParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonPromptoIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonPromptoIdentifier(MParser.PythonPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIdentifier(MParser.PythonIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIdentifier}
	 * labeled alternative in {@link MParser#python_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIdentifier(MParser.PythonIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonIntegerLiteral(MParser.PythonIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonIntegerLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonIntegerLiteral(MParser.PythonIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonDecimalLiteral(MParser.PythonDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonDecimalLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonDecimalLiteral(MParser.PythonDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonTextLiteral(MParser.PythonTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonTextLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonTextLiteral(MParser.PythonTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonBooleanLiteral(MParser.PythonBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonBooleanLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonBooleanLiteral(MParser.PythonBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterPythonCharacterLiteral(MParser.PythonCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PythonCharacterLiteral}
	 * labeled alternative in {@link MParser#python_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitPythonCharacterLiteral(MParser.PythonCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void enterPython_identifier(MParser.Python_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#python_identifier}.
	 * @param ctx the parse tree
	 */
	void exitPython_identifier(MParser.Python_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link MParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaReturnStatement(MParser.JavaReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaReturnStatement}
	 * labeled alternative in {@link MParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaReturnStatement(MParser.JavaReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link MParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void enterJavaStatement(MParser.JavaStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaStatement}
	 * labeled alternative in {@link MParser#java_statement}.
	 * @param ctx the parse tree
	 */
	void exitJavaStatement(MParser.JavaStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link MParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaSelectorExpression(MParser.JavaSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaSelectorExpression}
	 * labeled alternative in {@link MParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaSelectorExpression(MParser.JavaSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link MParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaPrimaryExpression(MParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaPrimaryExpression}
	 * labeled alternative in {@link MParser#java_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaPrimaryExpression(MParser.JavaPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_primary_expression(MParser.Java_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_primary_expression(MParser.Java_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_this_expression(MParser.Java_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_this_expression(MParser.Java_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_new_expression(MParser.Java_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_new_expression(MParser.Java_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link MParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaMethodExpression(MParser.JavaMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaMethodExpression}
	 * labeled alternative in {@link MParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaMethodExpression(MParser.JavaMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link MParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaItemExpression(MParser.JavaItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaItemExpression}
	 * labeled alternative in {@link MParser#java_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaItemExpression(MParser.JavaItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_method_expression(MParser.Java_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_method_expression(MParser.Java_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link MParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentListItem(MParser.JavaArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentListItem}
	 * labeled alternative in {@link MParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentListItem(MParser.JavaArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link MParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void enterJavaArgumentList(MParser.JavaArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaArgumentList}
	 * labeled alternative in {@link MParser#java_arguments}.
	 * @param ctx the parse tree
	 */
	void exitJavaArgumentList(MParser.JavaArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_item_expression(MParser.Java_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_item_expression(MParser.Java_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterJava_parenthesis_expression(MParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitJava_parenthesis_expression(MParser.Java_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link MParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIdentifier(MParser.JavaIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIdentifier}
	 * labeled alternative in {@link MParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIdentifier(MParser.JavaIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link MParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildIdentifier(MParser.JavaChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildIdentifier}
	 * labeled alternative in {@link MParser#java_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildIdentifier(MParser.JavaChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link MParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaClassIdentifier(MParser.JavaClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaClassIdentifier}
	 * labeled alternative in {@link MParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaClassIdentifier(MParser.JavaClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link MParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaChildClassIdentifier(MParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaChildClassIdentifier}
	 * labeled alternative in {@link MParser#java_class_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaChildClassIdentifier(MParser.JavaChildClassIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaIntegerLiteral(MParser.JavaIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaIntegerLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaIntegerLiteral(MParser.JavaIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaDecimalLiteral(MParser.JavaDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaDecimalLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaDecimalLiteral(MParser.JavaDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaTextLiteral(MParser.JavaTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaTextLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaTextLiteral(MParser.JavaTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaBooleanLiteral(MParser.JavaBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaBooleanLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaBooleanLiteral(MParser.JavaBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterJavaCharacterLiteral(MParser.JavaCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JavaCharacterLiteral}
	 * labeled alternative in {@link MParser#java_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitJavaCharacterLiteral(MParser.JavaCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJava_identifier(MParser.Java_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#java_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJava_identifier(MParser.Java_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link MParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpReturnStatement(MParser.CSharpReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpReturnStatement}
	 * labeled alternative in {@link MParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpReturnStatement(MParser.CSharpReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link MParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void enterCSharpStatement(MParser.CSharpStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpStatement}
	 * labeled alternative in {@link MParser#csharp_statement}.
	 * @param ctx the parse tree
	 */
	void exitCSharpStatement(MParser.CSharpStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link MParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpSelectorExpression(MParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpSelectorExpression}
	 * labeled alternative in {@link MParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpSelectorExpression(MParser.CSharpSelectorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link MParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPrimaryExpression(MParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPrimaryExpression}
	 * labeled alternative in {@link MParser#csharp_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPrimaryExpression(MParser.CSharpPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_primary_expression(MParser.Csharp_primary_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_primary_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_primary_expression(MParser.Csharp_primary_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_this_expression(MParser.Csharp_this_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_this_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_this_expression(MParser.Csharp_this_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_new_expression(MParser.Csharp_new_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_new_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_new_expression(MParser.Csharp_new_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link MParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpMethodExpression(MParser.CSharpMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpMethodExpression}
	 * labeled alternative in {@link MParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpMethodExpression(MParser.CSharpMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link MParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpItemExpression(MParser.CSharpItemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpItemExpression}
	 * labeled alternative in {@link MParser#csharp_selector_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpItemExpression(MParser.CSharpItemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_method_expression(MParser.Csharp_method_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_method_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_method_expression(MParser.Csharp_method_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link MParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentList(MParser.CSharpArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentList}
	 * labeled alternative in {@link MParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentList(MParser.CSharpArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link MParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void enterCSharpArgumentListItem(MParser.CSharpArgumentListItemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpArgumentListItem}
	 * labeled alternative in {@link MParser#csharp_arguments}.
	 * @param ctx the parse tree
	 */
	void exitCSharpArgumentListItem(MParser.CSharpArgumentListItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_item_expression(MParser.Csharp_item_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_item_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_item_expression(MParser.Csharp_item_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_parenthesis_expression(MParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_parenthesis_expression}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_parenthesis_expression(MParser.Csharp_parenthesis_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIdentifier(MParser.CSharpIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIdentifier(MParser.CSharpIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpChildIdentifier(MParser.CSharpChildIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpChildIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpChildIdentifier(MParser.CSharpChildIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpPromptoIdentifier(MParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpPromptoIdentifier}
	 * labeled alternative in {@link MParser#csharp_identifier_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpPromptoIdentifier(MParser.CSharpPromptoIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpIntegerLiteral(MParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpIntegerLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpIntegerLiteral(MParser.CSharpIntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpDecimalLiteral(MParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpDecimalLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpDecimalLiteral(MParser.CSharpDecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpTextLiteral(MParser.CSharpTextLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpTextLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpTextLiteral(MParser.CSharpTextLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpBooleanLiteral(MParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpBooleanLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpBooleanLiteral(MParser.CSharpBooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void enterCSharpCharacterLiteral(MParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CSharpCharacterLiteral}
	 * labeled alternative in {@link MParser#csharp_literal_expression}.
	 * @param ctx the parse tree
	 */
	void exitCSharpCharacterLiteral(MParser.CSharpCharacterLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCsharp_identifier(MParser.Csharp_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#csharp_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCsharp_identifier(MParser.Csharp_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void enterJsx_expression(MParser.Jsx_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_expression}.
	 * @param ctx the parse tree
	 */
	void exitJsx_expression(MParser.Jsx_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link MParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxSelfClosing(MParser.JsxSelfClosingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxSelfClosing}
	 * labeled alternative in {@link MParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxSelfClosing(MParser.JsxSelfClosingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link MParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void enterJsxElement(MParser.JsxElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxElement}
	 * labeled alternative in {@link MParser#jsx_element}.
	 * @param ctx the parse tree
	 */
	void exitJsxElement(MParser.JsxElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment(MParser.Jsx_fragmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_fragment}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment(MParser.Jsx_fragmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_start(MParser.Jsx_fragment_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_fragment_start}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_start(MParser.Jsx_fragment_startContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void enterJsx_fragment_end(MParser.Jsx_fragment_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_fragment_end}.
	 * @param ctx the parse tree
	 */
	void exitJsx_fragment_end(MParser.Jsx_fragment_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_self_closing(MParser.Jsx_self_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_self_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_self_closing(MParser.Jsx_self_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void enterJsx_opening(MParser.Jsx_openingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_opening}.
	 * @param ctx the parse tree
	 */
	void exitJsx_opening(MParser.Jsx_openingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void enterJsx_closing(MParser.Jsx_closingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_closing}.
	 * @param ctx the parse tree
	 */
	void exitJsx_closing(MParser.Jsx_closingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void enterJsx_element_name(MParser.Jsx_element_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_element_name}.
	 * @param ctx the parse tree
	 */
	void exitJsx_element_name(MParser.Jsx_element_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void enterJsx_identifier(MParser.Jsx_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_identifier}.
	 * @param ctx the parse tree
	 */
	void exitJsx_identifier(MParser.Jsx_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void enterJsx_attribute(MParser.Jsx_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_attribute}.
	 * @param ctx the parse tree
	 */
	void exitJsx_attribute(MParser.Jsx_attributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link MParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxLiteral(MParser.JsxLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxLiteral}
	 * labeled alternative in {@link MParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxLiteral(MParser.JsxLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link MParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterJsxValue(MParser.JsxValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxValue}
	 * labeled alternative in {@link MParser#jsx_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitJsxValue(MParser.JsxValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void enterJsx_children(MParser.Jsx_childrenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_children}.
	 * @param ctx the parse tree
	 */
	void exitJsx_children(MParser.Jsx_childrenContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxText(MParser.JsxTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxText}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxText(MParser.JsxTextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxChild(MParser.JsxChildContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxChild}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxChild(MParser.JsxChildContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void enterJsxCode(MParser.JsxCodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JsxCode}
	 * labeled alternative in {@link MParser#jsx_child}.
	 * @param ctx the parse tree
	 */
	void exitJsxCode(MParser.JsxCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void enterJsx_text(MParser.Jsx_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#jsx_text}.
	 * @param ctx the parse tree
	 */
	void exitJsx_text(MParser.Jsx_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void enterCss_expression(MParser.Css_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#css_expression}.
	 * @param ctx the parse tree
	 */
	void exitCss_expression(MParser.Css_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#css_field}.
	 * @param ctx the parse tree
	 */
	void enterCss_field(MParser.Css_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#css_field}.
	 * @param ctx the parse tree
	 */
	void exitCss_field(MParser.Css_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void enterCss_identifier(MParser.Css_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#css_identifier}.
	 * @param ctx the parse tree
	 */
	void exitCss_identifier(MParser.Css_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link MParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssValue(MParser.CssValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssValue}
	 * labeled alternative in {@link MParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssValue(MParser.CssValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link MParser#css_value}.
	 * @param ctx the parse tree
	 */
	void enterCssText(MParser.CssTextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CssText}
	 * labeled alternative in {@link MParser#css_value}.
	 * @param ctx the parse tree
	 */
	void exitCssText(MParser.CssTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link MParser#css_text}.
	 * @param ctx the parse tree
	 */
	void enterCss_text(MParser.Css_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link MParser#css_text}.
	 * @param ctx the parse tree
	 */
	void exitCss_text(MParser.Css_textContext ctx);
}