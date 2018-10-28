package prompto.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import prompto.argument.CategoryArgument;
import prompto.argument.CodeArgument;
import prompto.argument.ExtendedArgument;
import prompto.argument.IArgument;
import prompto.argument.UnresolvedArgument;
import prompto.constraint.IAttributeConstraint;
import prompto.constraint.MatchingCollectionConstraint;
import prompto.constraint.MatchingExpressionConstraint;
import prompto.constraint.MatchingPatternConstraint;
import prompto.csharp.CSharpBooleanLiteral;
import prompto.csharp.CSharpCharacterLiteral;
import prompto.csharp.CSharpDecimalLiteral;
import prompto.csharp.CSharpExpression;
import prompto.csharp.CSharpExpressionList;
import prompto.csharp.CSharpIdentifierExpression;
import prompto.csharp.CSharpIntegerLiteral;
import prompto.csharp.CSharpMethodExpression;
import prompto.csharp.CSharpNativeCall;
import prompto.csharp.CSharpNativeCategoryBinding;
import prompto.csharp.CSharpSelectorExpression;
import prompto.csharp.CSharpStatement;
import prompto.csharp.CSharpTextLiteral;
import prompto.csharp.CSharpThisExpression;
import prompto.css.CssCode;
import prompto.css.CssExpression;
import prompto.css.CssField;
import prompto.css.CssText;
import prompto.css.ICssValue;
import prompto.declaration.AbstractMethodDeclaration;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.ConcreteCategoryDeclaration;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.ConcreteWidgetDeclaration;
import prompto.declaration.DeclarationList;
import prompto.declaration.EnumeratedCategoryDeclaration;
import prompto.declaration.EnumeratedNativeDeclaration;
import prompto.declaration.GetterMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.IMethodDeclaration;
import prompto.declaration.NativeCategoryDeclaration;
import prompto.declaration.NativeGetterMethodDeclaration;
import prompto.declaration.NativeMethodDeclaration;
import prompto.declaration.NativeResourceDeclaration;
import prompto.declaration.NativeSetterMethodDeclaration;
import prompto.declaration.NativeWidgetDeclaration;
import prompto.declaration.OperatorMethodDeclaration;
import prompto.declaration.SetterMethodDeclaration;
import prompto.declaration.SingletonCategoryDeclaration;
import prompto.declaration.TestMethodDeclaration;
import prompto.expression.BlobExpression;
import prompto.expression.CategorySymbol;
import prompto.expression.NativeSymbol;
import prompto.expression.PlusExpression;
import prompto.expression.AndExpression;
import prompto.expression.CastExpression;
import prompto.expression.CodeExpression;
import prompto.expression.CompareExpression;
import prompto.expression.ConstructorExpression;
import prompto.expression.ContainsExpression;
import prompto.expression.DivideExpression;
import prompto.expression.DocumentExpression;
import prompto.expression.EqualsExpression;
import prompto.expression.ExecuteExpression;
import prompto.expression.FetchManyExpression;
import prompto.expression.FilteredExpression;
import prompto.expression.FetchOneExpression;
import prompto.expression.IExpression;
import prompto.expression.IntDivideExpression;
import prompto.expression.ItemSelector;
import prompto.expression.IteratorExpression;
import prompto.expression.MemberSelector;
import prompto.expression.MethodExpression;
import prompto.expression.MethodSelector;
import prompto.expression.MinusExpression;
import prompto.expression.ModuloExpression;
import prompto.expression.MultiplyExpression;
import prompto.expression.NotExpression;
import prompto.expression.OrExpression;
import prompto.expression.ParenthesisExpression;
import prompto.expression.ReadAllExpression;
import prompto.expression.ReadOneExpression;
import prompto.expression.SelectorExpression;
import prompto.expression.SliceSelector;
import prompto.expression.SortedExpression;
import prompto.expression.SubtractExpression;
import prompto.expression.SymbolExpression;
import prompto.expression.TernaryExpression;
import prompto.expression.ThisExpression;
import prompto.expression.TypeExpression;
import prompto.expression.UnresolvedIdentifier;
import prompto.grammar.Annotation;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.CategorySymbolList;
import prompto.grammar.CmpOp;
import prompto.grammar.ContOp;
import prompto.grammar.EqOp;
import prompto.grammar.Identifier;
import prompto.grammar.MethodDeclarationList;
import prompto.grammar.NativeCategoryBinding;
import prompto.grammar.NativeCategoryBindingList;
import prompto.grammar.NativeSymbolList;
import prompto.grammar.Operator;
import prompto.grammar.OrderByClause;
import prompto.grammar.OrderByClauseList;
import prompto.instance.IAssignableInstance;
import prompto.instance.IAssignableSelector;
import prompto.instance.ItemInstance;
import prompto.instance.MemberInstance;
import prompto.instance.VariableInstance;
import prompto.java.JavaBooleanLiteral;
import prompto.java.JavaCharacterLiteral;
import prompto.java.JavaDecimalLiteral;
import prompto.java.JavaExpression;
import prompto.java.JavaExpressionList;
import prompto.java.JavaIdentifierExpression;
import prompto.java.JavaIntegerLiteral;
import prompto.java.JavaItemExpression;
import prompto.java.JavaMethodExpression;
import prompto.java.JavaNativeCall;
import prompto.java.JavaNativeCategoryBinding;
import prompto.java.JavaSelectorExpression;
import prompto.java.JavaStatement;
import prompto.java.JavaTextLiteral;
import prompto.java.JavaThisExpression;
import prompto.javascript.JavaScriptBooleanLiteral;
import prompto.javascript.JavaScriptCharacterLiteral;
import prompto.javascript.JavaScriptDecimalLiteral;
import prompto.javascript.JavaScriptExpression;
import prompto.javascript.JavaScriptExpressionList;
import prompto.javascript.JavaScriptIdentifierExpression;
import prompto.javascript.JavaScriptIntegerLiteral;
import prompto.javascript.JavaScriptMemberExpression;
import prompto.javascript.JavaScriptMethodExpression;
import prompto.javascript.JavaScriptModule;
import prompto.javascript.JavaScriptNativeCall;
import prompto.javascript.JavaScriptNativeCategoryBinding;
import prompto.javascript.JavaScriptNewExpression;
import prompto.javascript.JavaScriptSelectorExpression;
import prompto.javascript.JavaScriptStatement;
import prompto.javascript.JavaScriptTextLiteral;
import prompto.javascript.JavaScriptThisExpression;
import prompto.jsx.IJsxExpression;
import prompto.jsx.IJsxValue;
import prompto.jsx.JsxAttribute;
import prompto.jsx.JsxClosing;
import prompto.jsx.JsxElement;
import prompto.jsx.JsxText;
import prompto.jsx.JsxExpression;
import prompto.jsx.JsxLiteral;
import prompto.jsx.JsxSelfClosing;
import prompto.literal.BooleanLiteral;
import prompto.literal.CharacterLiteral;
import prompto.literal.ContainerLiteral;
import prompto.literal.DateLiteral;
import prompto.literal.DateTimeLiteral;
import prompto.literal.DecimalLiteral;
import prompto.literal.DictEntry;
import prompto.literal.DictEntryList;
import prompto.literal.DictIdentifierKey;
import prompto.literal.DictKey;
import prompto.literal.DictLiteral;
import prompto.literal.DictTextKey;
import prompto.literal.DocEntryList;
import prompto.literal.DocumentLiteral;
import prompto.literal.HexaLiteral;
import prompto.literal.IntegerLiteral;
import prompto.literal.ListLiteral;
import prompto.literal.MaxIntegerLiteral;
import prompto.literal.MinIntegerLiteral;
import prompto.literal.NullLiteral;
import prompto.literal.PeriodLiteral;
import prompto.literal.RangeLiteral;
import prompto.literal.SetLiteral;
import prompto.literal.TextLiteral;
import prompto.literal.TimeLiteral;
import prompto.literal.TupleLiteral;
import prompto.literal.UUIDLiteral;
import prompto.literal.VersionLiteral;
import static prompto.parser.MParser.*;
import prompto.python.Python2NativeCall;
import prompto.python.Python2NativeCategoryBinding;
import prompto.python.Python3NativeCall;
import prompto.python.Python3NativeCategoryBinding;
import prompto.python.PythonArgumentList;
import prompto.python.PythonBooleanLiteral;
import prompto.python.PythonCharacterLiteral;
import prompto.python.PythonDecimalLiteral;
import prompto.python.PythonExpression;
import prompto.python.PythonIdentifierExpression;
import prompto.python.PythonIntegerLiteral;
import prompto.python.PythonMethodExpression;
import prompto.python.PythonModule;
import prompto.python.PythonNamedArgument;
import prompto.python.PythonNativeCategoryBinding;
import prompto.python.PythonOrdinalArgument;
import prompto.python.PythonSelectorExpression;
import prompto.python.PythonSelfExpression;
import prompto.python.PythonStatement;
import prompto.python.PythonTextLiteral;
import prompto.statement.AssignInstanceStatement;
import prompto.statement.AssignTupleStatement;
import prompto.statement.AssignVariableStatement;
import prompto.statement.AtomicSwitchCase;
import prompto.statement.BreakStatement;
import prompto.statement.CollectionSwitchCase;
import prompto.statement.CommentStatement;
import prompto.statement.DeclarationStatement;
import prompto.statement.DoWhileStatement;
import prompto.statement.FlushStatement;
import prompto.statement.ForEachStatement;
import prompto.statement.IStatement;
import prompto.statement.IfStatement;
import prompto.statement.RaiseStatement;
import prompto.statement.ReturnStatement;
import prompto.statement.StatementList;
import prompto.statement.StoreStatement;
import prompto.statement.SwitchCase;
import prompto.statement.SwitchErrorStatement;
import prompto.statement.SwitchStatement;
import prompto.statement.UnresolvedCall;
import prompto.statement.WhileStatement;
import prompto.statement.WithResourceStatement;
import prompto.statement.WithSingletonStatement;
import prompto.statement.WriteStatement;
import prompto.statement.BaseSwitchStatement.SwitchCaseList;
import prompto.statement.IfStatement.IfElement;
import prompto.statement.IfStatement.IfElementList;
import prompto.type.AnyType;
import prompto.type.BlobType;
import prompto.type.BooleanType;
import prompto.type.CategoryType;
import prompto.type.CharacterType;
import prompto.type.CodeType;
import prompto.type.DateTimeType;
import prompto.type.DateType;
import prompto.type.DecimalType;
import prompto.type.DictType;
import prompto.type.DocumentType;
import prompto.type.HtmlType;
import prompto.type.IType;
import prompto.type.ImageType;
import prompto.type.IntegerType;
import prompto.type.IteratorType;
import prompto.type.ListType;
import prompto.type.NativeType;
import prompto.type.PeriodType;
import prompto.type.SetType;
import prompto.type.TextType;
import prompto.type.TimeType;
import prompto.type.UUIDType;
import prompto.type.VersionType;
import prompto.utils.AssertionList;
import prompto.utils.ExpressionList;
import prompto.utils.IdentifierList;
import prompto.value.SetValue;

public class MPromptoBuilder extends MParserBaseListener {

	ParseTreeProperty<Object> nodeValues = new ParseTreeProperty<Object>();
	BufferedTokenStream input;
	String path = "";

	public MPromptoBuilder(MCleverParser parser) {
		this.input = (BufferedTokenStream)parser.getTokenStream();
		this.path = parser.getPath();
	}
	
	protected String getHiddenTokensBefore(Token token) {
		List<Token> hidden = input.getHiddenTokensToLeft(token.getTokenIndex());
		return getHiddenTokensText(hidden);
	}
	
	protected String getHiddenTokensAfter(Token token) {
		List<Token> hidden = input.getHiddenTokensToRight(token.getTokenIndex());
		return getHiddenTokensText(hidden);
	}
	
	
	private String getHiddenTokensText(List<Token> hidden) {
		if(hidden==null || hidden.isEmpty())
			return null;
		else
			return hidden.stream()
					.map(Token::getText)
					.collect(Collectors.joining());
	}

	private String getJsxWhiteSpace(ParserRuleContext ctx) {
		String within = ctx.children==null ? null : ctx.children.stream()
				.filter(child->isNotIndent(child))
				.map(child->child.getText())
				.collect(Collectors.joining());
		if(within==null)
			return null;
		String before = getHiddenTokensBefore(ctx.getStart());
		if(before!=null)
			within = before + within;
		String after = getHiddenTokensAfter(ctx.getStop());
		if(after!=null)
			within = within + after;
		return within; 
	}
	
	private static boolean isNotIndent(ParseTree tree) {
		return !(tree instanceof TerminalNode) || ((TerminalNode)tree).getSymbol().getType()!=INDENT;
	}

	public void buildSection(ParserRuleContext node, Section section) {
		Token first = findFirstValidToken(node.start.getTokenIndex());
		Token last = findLastValidToken(node.stop.getTokenIndex());
		section.setFrom(path, first, last, Dialect.M);
	}
	
	private List<Annotation> readAnnotations(List<? extends ParseTree> contexts) {
		List<Annotation> annotations = contexts.stream()
			.map(cx->(Annotation)this.<Annotation>getNodeValue(cx))
			.collect(Collectors.toList());
		return annotations.isEmpty() ?  null : annotations;
	}

	private List<CommentStatement> readComments(List<? extends ParseTree> contexts) {
		List<CommentStatement> comments = contexts.stream()
			.map(cx->(CommentStatement)this.<CommentStatement>getNodeValue(cx))
			.collect(Collectors.toList());
		return comments.isEmpty() ? null : comments;
	}

	@Override
	public void exitAbstract_method_declaration(Abstract_method_declarationContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.typ);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		ArgumentList args = this.<ArgumentList>getNodeValue(ctx.args);
		setNodeValue(ctx, new AbstractMethodDeclaration(name, args, type));
	}
	
	@Override
	public void exitAddExpression(AddExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		IExpression exp = ctx.op.getType()==MParser.PLUS ? new PlusExpression(left, right) : new SubtractExpression(left, right);
		setNodeValue(ctx, exp);
	}

	@Override
	public void exitAndExpression(AndExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new AndExpression(left, right));
	}
	
	@Override
	public void exitAnnotation_constructor(Annotation_constructorContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new Annotation(name, exp));
	}

	
	@Override
	public void exitAnnotation_identifier(Annotation_identifierContext ctx) {
		String name = ctx.getText();
		setNodeValue(ctx, new Identifier(name));
	}
	
	@Override
	public void exitAnyDictType(AnyDictTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.any_type());
		setNodeValue(ctx, new DictType(type));
	}
	
	@Override
	public void exitAnyListType(AnyListTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.any_type());
		setNodeValue(ctx, new ListType(type));
	}
	
	@Override
	public void exitAnyType(AnyTypeContext ctx) {
		setNodeValue(ctx, AnyType.instance());
	}
	
	@Override
	public void exitArgument_assignment(Argument_assignmentContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		IArgument arg = new UnresolvedArgument(name);
		ArgumentAssignment item = new ArgumentAssignment(arg, exp);
		setNodeValue(ctx, item);
	}

	
	@Override
	public void exitArgument_list(Argument_listContext ctx) {
		ArgumentList items = new ArgumentList();
		ctx.argument().forEach((a)->{
			IArgument item = this.<IArgument>getNodeValue(a); 
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	

	@Override
	public void exitArgumentAssignmentList(ArgumentAssignmentListContext ctx) {
		ArgumentAssignment item = this.<ArgumentAssignment>getNodeValue(ctx.item);
		ArgumentAssignmentList items = new ArgumentAssignmentList(Collections.singletonList(item));
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitArgumentAssignmentListItem(ArgumentAssignmentListItemContext ctx) {
		ArgumentAssignment item = this.<ArgumentAssignment>getNodeValue(ctx.item);
		ArgumentAssignmentList items = this.<ArgumentAssignmentList>getNodeValue(ctx.items);
		items.add(item);
		setNodeValue(ctx, items);
	}
	
	


	@Override
	public void exitAssertion(AssertionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new Assertion(exp));
	}
	
	
	@Override
	public void exitAssertion_list(Assertion_listContext ctx) {
		AssertionList items = new AssertionList();
		ctx.assertion().forEach((a)->{
			Assertion item = this.<Assertion>getNodeValue(a);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}

	@Override
	public void exitAssign_instance_statement(Assign_instance_statementContext ctx) {
		IAssignableInstance inst = this.<IAssignableInstance>getNodeValue(ctx.inst);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new AssignInstanceStatement(inst, exp));
	}
	
	@Override
	public void exitAssign_tuple_statement(Assign_tuple_statementContext ctx) {
		IdentifierList items = this.<IdentifierList>getNodeValue(ctx.items);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new AssignTupleStatement(items, exp));
	}
	
	@Override
	public void exitAssign_variable_statement(Assign_variable_statementContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.variable_identifier());
		IExpression exp = this.<IExpression>getNodeValue(ctx.expression());
		setNodeValue(ctx, new AssignVariableStatement(name, exp));
	}
	
	@Override
	public void exitAssignInstanceStatement(AssignInstanceStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitAssignTupleStatement(AssignTupleStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitAtomicSwitchCase(AtomicSwitchCaseContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new AtomicSwitchCase(exp, stmts));
	}
	
	@Override
	public void exitAttribute_declaration(Attribute_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IType type = this.<IType>getNodeValue(ctx.typ);
		IAttributeConstraint match = this.<IAttributeConstraint>getNodeValue(ctx.match);
		IdentifierList indices = ctx.index_clause()!=null ? this.<IdentifierList>getNodeValue(ctx.index_clause()) : null;
		AttributeDeclaration decl = new AttributeDeclaration(name, type, match, indices);
		decl.setStorable(ctx.STORABLE()!=null);
		setNodeValue(ctx, decl);
	}
	
	
	@Override
	public void exitIndex_clause(Index_clauseContext ctx) {
		IdentifierList indices = ctx.indices!=null ? 
				this.<IdentifierList>getNodeValue(ctx.indices) :
				new IdentifierList();
		setNodeValue(ctx, indices);
	}

	@Override
	public void exitAttribute_identifier(Attribute_identifierContext ctx) {
		setNodeValue(ctx, new Identifier(ctx.getText()));
	}
	
	@Override
	public void exitAttribute_identifier_list(Attribute_identifier_listContext ctx) {
		IdentifierList list = new IdentifierList();
		for(Attribute_identifierContext v : ctx.attribute_identifier()){
			Identifier item = this.<Identifier>getNodeValue(v);
			list.add(item);
		}
		setNodeValue(ctx, list);
	}
	
	@Override
	public void exitBlob_expression(Blob_expressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.expression());
		setNodeValue(ctx, new BlobExpression(exp));
	}
	
	@Override
	public void exitBlobType(BlobTypeContext ctx) {
		setNodeValue(ctx, BlobType.instance());
	}
	
	@Override
	public void exitBooleanLiteral(BooleanLiteralContext ctx) {
		setNodeValue(ctx, new BooleanLiteral(ctx.t.getText()));
	}

	@Override
	public void exitBooleanType(BooleanTypeContext ctx) {
		setNodeValue(ctx, BooleanType.instance());
	}

	
	@Override
	public void exitBreakStatement(BreakStatementContext ctx) {
		setNodeValue(ctx, new BreakStatement());
	}

	
	@Override
	public void exitCallableItemSelector(CallableItemSelectorContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new ItemSelector(exp));
	}
	
	
	@Override
	public void exitCallableMemberSelector(CallableMemberSelectorContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new MemberSelector(name));
	}
	
	
	@Override
	public void exitCallableRoot(CallableRootContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new UnresolvedIdentifier(name));
	}
	
	
	@Override
	public void exitCallableSelector(CallableSelectorContext ctx) {
		IExpression parent = this.<IExpression>getNodeValue(ctx.parent);
		SelectorExpression select = this.<SelectorExpression>getNodeValue(ctx.select);
		select.setParent(parent);
		setNodeValue(ctx, select);
	}


	@Override
	public void exitCastExpression(CastExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IType type = this.<IType>getNodeValue(ctx.right);
		setNodeValue(ctx, new CastExpression(left, type));
	}
	
	
	@Override
	public void exitCatch_statement_list(Catch_statement_listContext ctx) {
		SwitchCaseList items = new SwitchCaseList();
		ctx.catch_statement().forEach((s)->{
			SwitchCase item = this.<SwitchCase>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	

	@Override
	public void exitCatchAtomicStatement(CatchAtomicStatementContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new AtomicSwitchCase(new SymbolExpression(name), stmts));
	}

	@Override
	public void exitCatchCollectionStatement(CatchCollectionStatementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new CollectionSwitchCase(exp, stmts));
	}
	
	@Override
	public void exitCategory_or_any_type(Category_or_any_typeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, type);
	}
	
	@Override
	public void exitCategory_symbol(Category_symbolContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		ArgumentAssignmentList args = this.<ArgumentAssignmentList>getNodeValue(ctx.args);
		setNodeValue(ctx, new CategorySymbol(name, args));
	}

	@Override
	public void exitCategory_symbol_list(Category_symbol_listContext ctx) {
		CategorySymbolList items = new CategorySymbolList();
		ctx.category_symbol().forEach((s)->{
			CategorySymbol item = this.<CategorySymbol>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}

	@Override
	public void exitCategory_type(Category_typeContext ctx) {
		Identifier name = new Identifier(ctx.getText());
		setNodeValue(ctx, new CategoryType(name));
	}
	
	
	@Override
	public void exitCategoryType(CategoryTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.c);
		setNodeValue(ctx, type);
	}

	@Override
	public void exitCharacterLiteral(CharacterLiteralContext ctx) {
		setNodeValue(ctx, new CharacterLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitCharacterType(CharacterTypeContext ctx) {
		setNodeValue(ctx, CharacterType.instance());
	}

	@Override
	public void exitChildInstance(ChildInstanceContext ctx) {
		IAssignableInstance parent = this.<IAssignableInstance>getNodeValue(ctx.assignable_instance());
		IAssignableSelector child = this.<IAssignableSelector>getNodeValue(ctx.child_instance());
		child.setParent(parent);
		setNodeValue(ctx, child);
	}

	@Override
	public void exitClosure_expression(Closure_expressionContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new MethodExpression(name));
	}

	@Override
	public void exitClosureExpression(ClosureExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}

	@Override
	public void exitClosureStatement(ClosureStatementContext ctx) {
		ConcreteMethodDeclaration decl = this.<ConcreteMethodDeclaration>getNodeValue(ctx.decl);
		setNodeValue(ctx, new DeclarationStatement<ConcreteMethodDeclaration>(decl));
	}
	
	@Override
	public void exitCode_argument(Code_argumentContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new CodeArgument(name));
	}

	@Override
	public void exitCode_type(Code_typeContext ctx) {
		setNodeValue(ctx, CodeType.instance());
	}

	@Override
	public void exitCodeArgument(CodeArgumentContext ctx) {
		IArgument arg = this.<IArgument>getNodeValue(ctx.arg);
		setNodeValue(ctx, arg);
	}
	
	@Override
	public void exitCodeExpression(CodeExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new CodeExpression(exp));
	}

	@Override
	public void exitCodeType(CodeTypeContext ctx) {
		setNodeValue(ctx, CodeType.instance());
	}

	@Override
	public void exitCollection_literal(Collection_literalContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitCollectionSwitchCase(CollectionSwitchCaseContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new CollectionSwitchCase(exp, stmts));
	}

	@Override
	public void exitCommentStatement(CommentStatementContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.comment_statement()));
	}
	
	@Override
	public void exitComment_statement(Comment_statementContext ctx) {
		setNodeValue(ctx, new CommentStatement(ctx.getText()));
	}

	@Override
	public void exitConcrete_category_declaration(Concrete_category_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		IdentifierList derived = this.<IdentifierList>getNodeValue(ctx.derived);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		ConcreteCategoryDeclaration decl = new ConcreteCategoryDeclaration(name, attrs, derived, methods);
		decl.setStorable(ctx.STORABLE()!=null);
		setNodeValue(ctx, decl);
	}

	@Override
	public void exitConcrete_method_declaration(Concrete_method_declarationContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.typ);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		ArgumentList args = this.<ArgumentList>getNodeValue(ctx.args);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new ConcreteMethodDeclaration(name, args, type, stmts));
	}
	
	
	@Override
	public void exitConcrete_widget_declaration(Concrete_widget_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		Identifier derived = this.<Identifier>getNodeValue(ctx.derived);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		ConcreteWidgetDeclaration decl = new ConcreteWidgetDeclaration(name, derived, methods);
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitConcreteCategoryDeclaration(ConcreteCategoryDeclarationContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.decl));
	}
	
	@Override
	public void exitConcreteWidgetDeclaration(ConcreteWidgetDeclarationContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.decl));
	}
	
	@Override
	public void exitConstructorFrom(ConstructorFromContext ctx) {
		CategoryType type = this.<CategoryType>getNodeValue(ctx.typ);
		IExpression copyFrom =  this.<IExpression>getNodeValue(ctx.copyExp);
		ArgumentAssignmentList args = this.<ArgumentAssignmentList>getNodeValue(ctx.args);
		setNodeValue(ctx, new ConstructorExpression(type, copyFrom, args, true));
	}

	
	@Override
	public void exitConstructorNoFrom(ConstructorNoFromContext ctx) {
		CategoryType type = this.<CategoryType>getNodeValue(ctx.typ);
		ArgumentAssignmentList args = this.<ArgumentAssignmentList>getNodeValue(ctx.args);
		setNodeValue(ctx, new ConstructorExpression(type, null, args, true));
	}
	
	@Override
	public void exitCopy_from(Copy_fromContext ctx) {
		setNodeValue(ctx, this.getNodeValue(ctx.exp));
	}
	
	@Override
	public void exitHasExpression(HasExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.HAS, right));
	}

	@Override
	public void exitHasAllExpression(HasAllExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.HAS_ALL, right));
	}

	@Override
	public void exitHasAnyExpression(HasAnyExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.HAS_ANY, right));
	}

	@Override
	public void exitContainsExpression(ContainsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new EqualsExpression(left, EqOp.CONTAINS, right));
	}

	@Override
	public void exitCsharp_identifier(Csharp_identifierContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}

	@Override
	public void exitCsharp_method_expression(Csharp_method_expressionContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		CSharpExpressionList args = this.<CSharpExpressionList>getNodeValue(ctx.args);
		setNodeValue(ctx, new CSharpMethodExpression(name, args));
	}

	@Override
	public void exitCsharp_primary_expression(Csharp_primary_expressionContext ctx) {
		CSharpExpression exp = this.<CSharpExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, exp);
	}
		
	public void exitCsharp_this_expression(Csharp_this_expressionContext ctx) {
		setNodeValue(ctx, new CSharpThisExpression());
	}
	
	@Override
	public void exitCSharpArgumentList(CSharpArgumentListContext ctx) {
		CSharpExpression item = this.<CSharpExpression>getNodeValue(ctx.item);
		setNodeValue(ctx, new CSharpExpressionList(item));
	}
	
	@Override
	public void exitCSharpArgumentListItem(CSharpArgumentListItemContext ctx) {
		CSharpExpression item = this.<CSharpExpression>getNodeValue(ctx.item);
		CSharpExpressionList items = this.<CSharpExpressionList>getNodeValue(ctx.items);
		items.add(item);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitCSharpBooleanLiteral(CSharpBooleanLiteralContext ctx) {
		setNodeValue(ctx, new CSharpBooleanLiteral(ctx.getText()));
	}
	
	@Override
	public void exitCSharpCategoryBinding(CSharpCategoryBindingContext ctx) {
		CSharpIdentifierExpression map = this.<CSharpIdentifierExpression>getNodeValue(ctx.binding);
		setNodeValue(ctx, new CSharpNativeCategoryBinding(map));
	}
	
	@Override
	public void exitCSharpCharacterLiteral(CSharpCharacterLiteralContext ctx) {
		setNodeValue(ctx, new CSharpCharacterLiteral(ctx.getText()));
	}
	
	@Override
	public void exitCSharpChildIdentifier(CSharpChildIdentifierContext ctx) {
		CSharpIdentifierExpression parent = this.<CSharpIdentifierExpression>getNodeValue(ctx.parent);
		String name = this.<String>getNodeValue(ctx.name);
		CSharpIdentifierExpression child = new CSharpIdentifierExpression(parent, name);
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitCSharpDecimalLiteral(CSharpDecimalLiteralContext ctx) {
		setNodeValue(ctx, new CSharpDecimalLiteral(ctx.getText()));
	}
	
	@Override
	public void exitCSharpIdentifier(CSharpIdentifierContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		setNodeValue(ctx, new CSharpIdentifierExpression(name));
	}
	
	@Override
	public void exitCSharpIntegerLiteral(CSharpIntegerLiteralContext ctx) {
		setNodeValue(ctx, new CSharpIntegerLiteral(ctx.getText()));
	}
	
	@Override
	public void exitCSharpMethodExpression(CSharpMethodExpressionContext ctx) {
		CSharpExpression exp = this.<CSharpExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitCSharpNativeStatement(CSharpNativeStatementContext ctx) {
		CSharpStatement stmt = this.<CSharpStatement>getNodeValue(ctx.csharp_statement());
		setNodeValue(ctx, new CSharpNativeCall(stmt));
	}
	
	@Override
	public void exitCSharpPromptoIdentifier(CSharpPromptoIdentifierContext ctx) {
		String name = ctx.DOLLAR_IDENTIFIER().getText();
		setNodeValue(ctx, new CSharpIdentifierExpression(name));
	}
	
	@Override
	public void exitCSharpPrimaryExpression(CSharpPrimaryExpressionContext ctx) {
		CSharpExpression exp = this.<CSharpExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitCSharpReturnStatement(CSharpReturnStatementContext ctx) {
		CSharpExpression exp = this.<CSharpExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new CSharpStatement(exp,true));
	}
	
	@Override
	public void exitCSharpSelectorExpression(CSharpSelectorExpressionContext ctx) {
		CSharpExpression parent = this.<CSharpExpression>getNodeValue(ctx.parent);
		CSharpSelectorExpression child = this.<CSharpSelectorExpression>getNodeValue(ctx.child);
		child.setParent(parent);
		setNodeValue(ctx, child);
	};
	
	@Override
	public void exitCSharpStatement(CSharpStatementContext ctx) {
		CSharpExpression exp = this.<CSharpExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new CSharpStatement(exp,false));
	}
	
	@Override
	public void exitCSharpTextLiteral(CSharpTextLiteralContext ctx) {
		setNodeValue(ctx, new CSharpTextLiteral(ctx.getText()));
	}
	
	@Override
	public void exitCssExpression(CssExpressionContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.exp));
	}
	
	@Override
	public void exitCss_expression(Css_expressionContext ctx) {
		CssExpression exp = new CssExpression();
		ctx.css_field().forEach(cx->{
			CssField field = this.<CssField>getNodeValue(cx);
			exp.addField(field);
		});
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitCss_field(Css_fieldContext ctx) {
		String name = ctx.name.getText();
		ICssValue value = this.<ICssValue>getNodeValue(ctx.value);
		setNodeValue(ctx, new CssField(name, value));
	}
	
	@Override
	public void exitCssText(CssTextContext ctx) {
		String text = ctx.text.getText();
		setNodeValue(ctx, new CssText(text));
	}
	
	@Override
	public void exitCssValue(CssValueContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new CssCode(exp));
	}

	@Override
	public void exitDateLiteral(DateLiteralContext ctx) {
		setNodeValue(ctx, new DateLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitDateTimeLiteral(DateTimeLiteralContext ctx) {
		setNodeValue(ctx, new DateTimeLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitDateTimeType(DateTimeTypeContext ctx) {
		setNodeValue(ctx, DateTimeType.instance());
	}
	
	@Override
	public void exitDateType(DateTypeContext ctx) {
		setNodeValue(ctx, DateType.instance());
	}
	
	@Override
	public void exitDecimalLiteral(DecimalLiteralContext ctx) {
		setNodeValue(ctx, new DecimalLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitDecimalType(DecimalTypeContext ctx) {
		setNodeValue(ctx, DecimalType.instance());
	}
	
	@Override
	public void exitDeclaration(DeclarationContext ctx) {
		List<CommentStatement> comments = readComments(ctx.comment_statement());
		List<Annotation> annotations = readAnnotations(ctx.annotation_constructor());
		ParseTree ctx_ = ctx.getChild(ctx.getChildCount()-1);
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx_);
		if(decl!=null) {
			decl.setComments(comments);
			decl.setAnnotations(annotations);
			setNodeValue(ctx, decl);
		}
	}
	

	@Override
	public void exitDeclarations(DeclarationsContext ctx) {
		DeclarationList items = new DeclarationList();
		ctx.declaration().forEach((d)->{
			IDeclaration item = this.<IDeclaration>getNodeValue(d);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitDerived_list(Derived_listContext ctx) {
		IdentifierList items = this.<IdentifierList>getNodeValue(ctx.items);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitDict_entry(Dict_entryContext ctx) {
		DictKey key = this.<DictKey>getNodeValue(ctx.key);
		IExpression value = this.<IExpression>getNodeValue(ctx.value);
		DictEntry entry = new DictEntry(key, value);
		setNodeValue(ctx, entry);
	}
	
	@Override
	public void exitDict_literal(Dict_literalContext ctx) {
		boolean mutable = ctx.MUTABLE()!=null;
		DictEntryList items = this.<DictEntryList>getNodeValue(ctx.dict_entry_list());
		IExpression value = items==null ? new DictLiteral(mutable) : new DictLiteral(items, mutable);
		setNodeValue(ctx, value);
	}
	
	
	@Override
	public void exitDict_entry_list(Dict_entry_listContext ctx) {
		DictEntryList items = new DictEntryList();
		ctx.dict_entry().forEach((e)->{
			DictEntry item = this.<DictEntry>getNodeValue(e);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitDictKeyIdentifier(DictKeyIdentifierContext ctx) {
		String text = ctx.name.getText();
		setNodeValue(ctx, new DictIdentifierKey(new Identifier(text)));
	}
	
	@Override
	public void exitDictKeyText(DictKeyTextContext ctx) {
		String text = ctx.name.getText();
		setNodeValue(ctx, new DictTextKey(text));
	}
	
	@Override
	public void exitDictType(DictTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.d);
		setNodeValue(ctx, new DictType(type));
	}
	
	@Override
	public void exitDivideExpression(DivideExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new DivideExpression(left, right));
	}
	
	@Override
	public void exitDo_while_statement(Do_while_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new DoWhileStatement(exp, stmts));
	}
	
	@Override
	public void exitDocument_expression(Document_expressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.expression());
		setNodeValue(ctx, new DocumentExpression(exp));
	}
	
	
	@Override
	public void exitDocumentType(DocumentTypeContext ctx) {
		setNodeValue(ctx, DocumentType.instance());
	}
	
	
	@Override
	public void exitDocument_literal(Document_literalContext ctx) {
		DictEntryList entries = this.<DictEntryList>getNodeValue(ctx.dict_entry_list());
		DocEntryList items = entries!=null ? new DocEntryList(entries) : new DocEntryList();
		setNodeValue(ctx, new DocumentLiteral(items));
	}

	
	@Override
	public void exitDoWhileStatement(DoWhileStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitElseIfStatementList(ElseIfStatementListContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		IfElement elem = new IfElement(exp, stmts);
		setNodeValue(ctx, new IfElementList(elem));
	}
	
	@Override
	public void exitElseIfStatementListItem(ElseIfStatementListItemContext ctx) {
		IfElementList items = this.<IfElementList>getNodeValue(ctx.items);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		IfElement elem = new IfElement(exp, stmts);
		items.add(elem);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitEnum_category_declaration(Enum_category_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		Identifier parent = this.<Identifier>getNodeValue(ctx.derived);
		IdentifierList derived = parent==null ? null : new IdentifierList(parent);
		CategorySymbolList symbols = this.<CategorySymbolList>getNodeValue(ctx.symbols);
		setNodeValue(ctx, new EnumeratedCategoryDeclaration(name, attrs, derived, symbols));
	}
	
	@Override
	public void exitEnum_native_declaration(Enum_native_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		NativeType type = this.<NativeType>getNodeValue(ctx.typ);
		NativeSymbolList symbols = this.<NativeSymbolList>getNodeValue(ctx.symbols);
		setNodeValue(ctx, new EnumeratedNativeDeclaration(name, type, symbols));
	}
	
	
	@Override
	public void exitEnum_declaration(Enum_declarationContext ctx) {
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, decl);
	}
	
		
	@Override
	public void exitEqualsExpression(EqualsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new EqualsExpression(left, EqOp.EQUALS, right));
	}
	
	
	@Override
	public void exitExpression_list(Expression_listContext ctx) {
		ExpressionList items = new ExpressionList();
		ctx.expression().forEach((e)->{
			IExpression item = this.<IExpression>getNodeValue(e);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitExpression_tuple(Expression_tupleContext ctx) {
		ExpressionList items = new ExpressionList();
		ctx.expression().forEach((e)->{
			IExpression item = this.<IExpression>getNodeValue(e);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitExecuteExpression(ExecuteExpressionContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new ExecuteExpression(name));
	}
	
	@Override
	public void exitExpressionAssignmentList(ExpressionAssignmentListContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		ArgumentAssignmentList items = new ArgumentAssignmentList();
		items.add(new ArgumentAssignment(null, exp));
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitFilteredListExpression(FilteredListExpressionContext ctx) {
		FilteredExpression fetch = this.<FilteredExpression>getNodeValue(ctx.filtered_list_suffix());
		IExpression source = this.<IExpression>getNodeValue(ctx.src);
		fetch.setSource(source);
		setNodeValue(ctx, fetch);
	}
	
	
	@Override
	public void exitFiltered_list_suffix(Filtered_list_suffixContext ctx) {
		Identifier itemName = this.<Identifier>getNodeValue(ctx.name);
		IExpression predicate = this.<IExpression>getNodeValue(ctx.predicate);
		setNodeValue(ctx, new FilteredExpression(itemName, null, predicate));
	}
	
	@Override
	public void exitFetchOne(FetchOneContext ctx) {
		CategoryType category = this.<CategoryType>getNodeValue(ctx.typ);
		IExpression filter = this.<IExpression>getNodeValue(ctx.predicate);
		setNodeValue(ctx, new FetchOneExpression(category, filter));
	}
	
	@Override
	public void exitFlush_statement(Flush_statementContext ctx) {
		setNodeValue(ctx, new FlushStatement());
	}
	
	
	@Override
	public void exitFlushStatement(FlushStatementContext ctx) {
		setNodeValue(ctx, getNodeValue(ctx.stmt));
	}
	
	
	
	@Override
	public void exitFetchMany(FetchManyContext ctx) {
		CategoryType category = this.<CategoryType>getNodeValue(ctx.typ);
		IExpression start = this.<IExpression>getNodeValue(ctx.xstart);
		IExpression stop = this.<IExpression>getNodeValue(ctx.xstop);
		IExpression filter = this.<IExpression>getNodeValue(ctx.predicate);
		OrderByClauseList orderBy = this.<OrderByClauseList>getNodeValue(ctx.orderby);
		setNodeValue(ctx, new FetchManyExpression(category, start, stop, filter, orderBy));
	}
	
	@Override
	public void exitFor_each_statement(For_each_statementContext ctx) {
		Identifier name1 = this.<Identifier>getNodeValue(ctx.name1);
		Identifier name2 = this.<Identifier>getNodeValue(ctx.name2);
		IExpression source = this.<IExpression>getNodeValue(ctx.source);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new ForEachStatement(name1, name2, source, stmts));
	}
	
	@Override
	public void exitForEachStatement(ForEachStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitFullDeclarationList(FullDeclarationListContext ctx) {
		DeclarationList items = this.<DeclarationList>getNodeValue(ctx.declarations());
		if(items==null)
			items = new DeclarationList();
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitGetter_method_declaration(Getter_method_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new GetterMethodDeclaration(name, stmts));
	}
	
	@Override
	public void exitGreaterThanExpression(GreaterThanExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new CompareExpression(left, CmpOp.GT, right));
	}
	
	@Override
	public void exitGreaterThanOrEqualExpression(GreaterThanOrEqualExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new CompareExpression(left, CmpOp.GTE, right));
	}

	@Override
	public void exitHexadecimalLiteral(HexadecimalLiteralContext ctx) {
		setNodeValue(ctx, new HexaLiteral(ctx.t.getText()));
	}
	
	
	@Override
	public void exitHtmlType(HtmlTypeContext ctx) {
		setNodeValue(ctx, HtmlType.instance());
	}
	
	@Override
	public void exitIdentifierExpression(IdentifierExpressionContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.exp);
		setNodeValue(ctx, new UnresolvedIdentifier(name));
	}
	
	@Override
	public void exitIf_statement(If_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		IfElementList elseIfs = this.<IfElementList>getNodeValue(ctx.elseIfs);
		StatementList elseStmts = this.<StatementList>getNodeValue(ctx.elseStmts);
		setNodeValue(ctx, new IfStatement(exp, stmts, elseIfs, elseStmts));
	}

	@Override
	public void exitIfStatement(IfStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitImageType(ImageTypeContext ctx) {
		setNodeValue(ctx, ImageType.instance());
	}
	
	@Override
	public void exitInExpression(InExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.IN, right));
	}
	
	@Override
	public void exitInstanceExpression(InstanceExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitIntDivideExpression(IntDivideExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new IntDivideExpression(left, right));
	}
	
	@Override
	public void exitIntegerLiteral(IntegerLiteralContext ctx) {
		setNodeValue(ctx, new IntegerLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitIntegerType(IntegerTypeContext ctx) {
		setNodeValue(ctx, IntegerType.instance());
	}
	
	@Override
	public void exitIsATypeExpression(IsATypeExpressionContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.category_or_any_type());
		IExpression exp = new TypeExpression(type);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitIsExpression(IsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		EqOp op = right instanceof TypeExpression ? EqOp.IS_A : EqOp.IS;
		setNodeValue(ctx, new EqualsExpression(left, op, right));
	}
	
	@Override
	public void exitIsNotExpression(IsNotExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		EqOp op = right instanceof TypeExpression ? EqOp.IS_NOT_A : EqOp.IS_NOT;
		setNodeValue(ctx, new EqualsExpression(left, op, right));
	}
	
	@Override
	public void exitIsOtherExpression(IsOtherExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.expression());
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitItemInstance(ItemInstanceContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new ItemInstance(exp));
	}
	
	@Override
	public void exitItemSelector(ItemSelectorContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new ItemSelector(exp));
	}
	
	@Override
	public void exitIteratorExpression(IteratorExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IExpression source = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new IteratorExpression(name, source, exp));
	}
	
	@Override
	public void exitIteratorType(IteratorTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.i);
		setNodeValue(ctx, new IteratorType(type));
	}

	@Override
	public void exitJava_identifier(Java_identifierContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}
	
	@Override
	public void exitJava_item_expression(Java_item_expressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JavaItemExpression(exp));
	}
	
	@Override
	public void exitJava_method_expression(Java_method_expressionContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		JavaExpressionList args = this.<JavaExpressionList>getNodeValue(ctx.args);
		setNodeValue(ctx, new JavaMethodExpression(name, args));
	}

	@Override
	public void exitJava_parenthesis_expression(Java_parenthesis_expressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJava_primary_expression(Java_primary_expressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJava_this_expression(Java_this_expressionContext ctx) {
		setNodeValue(ctx, new JavaThisExpression());
	}
	
	@Override
	public void exitJavaArgumentList(JavaArgumentListContext ctx) {
		JavaExpression item = this.<JavaExpression>getNodeValue(ctx.item);
		setNodeValue(ctx, new JavaExpressionList(item));
	}
	
	@Override
	public void exitJavaArgumentListItem(JavaArgumentListItemContext ctx) {
		JavaExpression item = this.<JavaExpression>getNodeValue(ctx.item);
		JavaExpressionList items = this.<JavaExpressionList>getNodeValue(ctx.items);
		items.add(item);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitJavaBooleanLiteral(JavaBooleanLiteralContext ctx) {
		setNodeValue(ctx, new JavaBooleanLiteral(ctx.getText()));
	}
	
	@Override
	public void exitJavaCategoryBinding(JavaCategoryBindingContext ctx) {
		JavaIdentifierExpression map = this.<JavaIdentifierExpression>getNodeValue(ctx.binding);
		setNodeValue(ctx, new JavaNativeCategoryBinding(map));
	}
	
	@Override
	public void exitJavaCharacterLiteral(JavaCharacterLiteralContext ctx) {
		setNodeValue(ctx, new JavaCharacterLiteral(ctx.getText()));
	}
	
	@Override
	public void exitJavaChildClassIdentifier(JavaChildClassIdentifierContext ctx) {
		JavaIdentifierExpression parent = this.<JavaIdentifierExpression>getNodeValue(ctx.parent);
		JavaIdentifierExpression child = new JavaIdentifierExpression(parent, ctx.name.getText());
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitJavaChildIdentifier(JavaChildIdentifierContext ctx) {
		JavaIdentifierExpression parent = this.<JavaIdentifierExpression>getNodeValue(ctx.parent);
		String name = this.<String>getNodeValue(ctx.name);
		JavaIdentifierExpression child = new JavaIdentifierExpression(parent, name);
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitJavaClassIdentifier(JavaClassIdentifierContext ctx) {
		JavaIdentifierExpression klass = this.<JavaIdentifierExpression>getNodeValue(ctx.klass);
		setNodeValue(ctx, klass);
	}
	
	@Override
	public void exitJavaDecimalLiteral(JavaDecimalLiteralContext ctx) {
		setNodeValue(ctx, new JavaDecimalLiteral(ctx.getText()));
	}
	
	@Override
	public void exitJavaIdentifier(JavaIdentifierContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		setNodeValue(ctx, new JavaIdentifierExpression(name));
	}
	
	@Override
	public void exitJavaIntegerLiteral(JavaIntegerLiteralContext ctx) {
		setNodeValue(ctx, new JavaIntegerLiteral(ctx.getText()));
	}
	
	@Override
	public void exitJavaItemExpression(JavaItemExpressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJavaMethodExpression(JavaMethodExpressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJavaNativeStatement(JavaNativeStatementContext ctx) {
		JavaStatement stmt = this.<JavaStatement>getNodeValue(ctx.java_statement());
		setNodeValue(ctx, new JavaNativeCall(stmt));
	}
	
	@Override
	public void exitJavaPrimaryExpression(JavaPrimaryExpressionContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}

	@Override
	public void exitJavaReturnStatement(JavaReturnStatementContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JavaStatement(exp,true));
	}
	
	@Override
	public void exitJavascript_category_binding(Javascript_category_bindingContext ctx) {
		String identifier = ctx.javascript_identifier().stream()
				.map(cx->cx.getText())
				.collect(Collectors.joining("."));
		JavaScriptModule module = this.<JavaScriptModule>getNodeValue(ctx.javascript_module());
		JavaScriptNativeCategoryBinding map = new JavaScriptNativeCategoryBinding(identifier, module);
		setNodeValue(ctx, map);
	}
	
	@Override
	public void exitJavascript_identifier(Javascript_identifierContext ctx) {
		String name = ctx.getText();
		setNodeValue(ctx, name);
	}
	
	@Override
	public void exitJavascript_identifier_expression(Javascript_identifier_expressionContext ctx) {
		String name = ctx.name.getText();
		setNodeValue(ctx, new JavaScriptIdentifierExpression(name));
	}
	
	@Override
	public void exitJavascript_method_expression(Javascript_method_expressionContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		JavaScriptMethodExpression method = new JavaScriptMethodExpression(name);
		JavaScriptExpressionList args = this.<JavaScriptExpressionList>getNodeValue(ctx.args);
		method.setArguments(args);
		setNodeValue(ctx, method);
	}
	
	@Override
	public void exitJavascript_module(Javascript_moduleContext ctx) {
		List<String> ids = new ArrayList<String>();
		for(Javascript_identifierContext ic : ctx.javascript_identifier())
			ids.add(ic.getText());
		JavaScriptModule module = new JavaScriptModule(ids);
		setNodeValue(ctx, module);
	}
	
	@Override
	public void exitJavascript_native_statement(Javascript_native_statementContext ctx) {
		JavaScriptStatement stmt = this.<JavaScriptStatement>getNodeValue(ctx.javascript_statement());
		JavaScriptModule module = this.<JavaScriptModule>getNodeValue(ctx.javascript_module());
		stmt.setModule(module);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitJavascript_new_expression(Javascript_new_expressionContext ctx) {
		JavaScriptMethodExpression exp = this.<JavaScriptMethodExpression>getNodeValue(ctx.javascript_method_expression());
		setNodeValue(ctx, new JavaScriptNewExpression(exp));
	}
	
	@Override
	public void exitJavascript_primary_expression(Javascript_primary_expressionContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJavascript_this_expression(Javascript_this_expressionContext ctx) {
		setNodeValue(ctx, new JavaScriptThisExpression());		
	}
	
	@Override
	public void exitJavascriptArgumentList(JavascriptArgumentListContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.item);
		JavaScriptExpressionList list = new JavaScriptExpressionList(exp);
		setNodeValue(ctx, list);
	}
	
	@Override
	public void exitJavascriptArgumentListItem(JavascriptArgumentListItemContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.item);
		JavaScriptExpressionList list = this.<JavaScriptExpressionList>getNodeValue(ctx.items);
		list.add(exp);
		setNodeValue(ctx, list);
	}

	@Override
	public void exitJavascriptBooleanLiteral(JavascriptBooleanLiteralContext ctx) {
		String text = ctx.t.getText();
		setNodeValue(ctx, new JavaScriptBooleanLiteral(text));		
	}

	@Override
	public void exitJavaScriptCategoryBinding(JavaScriptCategoryBindingContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.binding));
	}
	
	@Override
	public void exitJavascriptCharacterLiteral(JavascriptCharacterLiteralContext ctx) {
		String text = ctx.t.getText();
		setNodeValue(ctx, new JavaScriptCharacterLiteral(text));		
	}
	
	@Override
	public void exitJavascriptDecimalLiteral(JavascriptDecimalLiteralContext ctx) {
		String text = ctx.t.getText();
		setNodeValue(ctx, new JavaScriptDecimalLiteral(text));		
	}
	
	@Override
	public void exitJavascriptIntegerLiteral(JavascriptIntegerLiteralContext ctx) {
		String text = ctx.t.getText();
		setNodeValue(ctx, new JavaScriptIntegerLiteral(text));		
	}
	
	@Override
	public void exitJavaScriptMemberExpression(JavaScriptMemberExpressionContext ctx) {
		String name = ctx.name.getText();
		setNodeValue(ctx, new JavaScriptMemberExpression(name));
	}
	
	@Override
	public void exitJavaScriptMethodExpression(JavaScriptMethodExpressionContext ctx) {
		JavaScriptExpression method = this.<JavaScriptExpression>getNodeValue(ctx.method);
		setNodeValue(ctx, method);
	}
	@Override
	public void exitJavaScriptNativeStatement(JavaScriptNativeStatementContext ctx) {
		JavaScriptStatement stmt = this.<JavaScriptStatement>getNodeValue(ctx.javascript_native_statement());
		setNodeValue(ctx, new JavaScriptNativeCall(stmt));
	}
	
	@Override
	public void exitJavascriptPrimaryExpression(JavascriptPrimaryExpressionContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitJavascriptReturnStatement(JavascriptReturnStatementContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JavaScriptStatement(exp, true));
	}
	
	@Override
	public void exitJavascriptSelectorExpression(JavascriptSelectorExpressionContext ctx) {
		JavaScriptExpression parent = this.<JavaScriptExpression>getNodeValue(ctx.parent);
		JavaScriptSelectorExpression child = this.<JavaScriptSelectorExpression>getNodeValue(ctx.child);
		child.setParent(parent);
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitJavascriptStatement(JavascriptStatementContext ctx) {
		JavaScriptExpression exp = this.<JavaScriptExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JavaScriptStatement(exp, false));
	}
	
	@Override
	public void exitJavascriptTextLiteral(JavascriptTextLiteralContext ctx) {
		String text = ctx.t.getText();
		setNodeValue(ctx, new JavaScriptTextLiteral(text));		
	}

	@Override
	public void exitJavaSelectorExpression(JavaSelectorExpressionContext ctx) {
		JavaExpression parent = this.<JavaExpression>getNodeValue(ctx.parent);
		JavaSelectorExpression child = this.<JavaSelectorExpression>getNodeValue(ctx.child);
		child.setParent(parent);
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitJavaStatement(JavaStatementContext ctx) {
		JavaExpression exp = this.<JavaExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JavaStatement(exp,false));
	}
	
	
	@Override
	public void exitJavaTextLiteral(JavaTextLiteralContext ctx) {
		setNodeValue(ctx, new JavaTextLiteral(ctx.getText()));
	}
	

	@Override
	public void exitJsxChild(JsxChildContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.jsx));
	}
	
	
	@Override
	public void exitJsxCode(JsxCodeContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JsxExpression(exp));
	}
	

	@Override
	public void exitJsxElement(JsxElementContext ctx) {
		JsxElement element = this.<JsxElement>getNodeValue(ctx.opening);
		JsxClosing closing = this.<JsxClosing>getNodeValue(ctx.closing);
		element.setClosing(closing);
		List<IJsxExpression> children = this.<List<IJsxExpression>>getNodeValue(ctx.children_);
		element.setChildren(children);
		setNodeValue(ctx, element);
	}
	

	@Override
	public void exitJsxExpression(JsxExpressionContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.exp));
	}
	
	
	@Override
	public void exitJsxSelfClosing(JsxSelfClosingContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.jsx));
	}
	
	
	@Override
	public void exitJsxText(JsxTextContext ctx) {
		String text = ParserUtils.getFullText(ctx.text);
		setNodeValue(ctx, new JsxText(text));
	}
	
	
	@Override
	public void exitJsxValue(JsxValueContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new JsxExpression(exp));
	}

	
	@Override
	public void exitJsx_attribute(Jsx_attributeContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IJsxValue value = this.<IJsxValue>getNodeValue(ctx.value);
		String suite = getJsxWhiteSpace(ctx.jsx_ws());
		setNodeValue(ctx, new JsxAttribute(name, value, suite));
	}
	
	
	@Override
	public void exitJsx_children(Jsx_childrenContext ctx) {
		List<IJsxExpression> list = ctx.jsx_child().stream()
				.map(cx -> this.<IJsxExpression>getNodeValue(cx))
				.collect(Collectors.toList());
		setNodeValue(ctx, list);
	}

	
	@Override
	public void exitJsx_element_name(Jsx_element_nameContext ctx) {
		String name = ctx.getText();
		setNodeValue(ctx, new Identifier(name));
	}
	
	@Override
	public void exitJsx_expression(Jsx_expressionContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.getChild(0)));
	}
	
	@Override
	public void exitJsx_identifier(Jsx_identifierContext ctx) {
		String name = ctx.getText();
		setNodeValue(ctx, new Identifier(name));
	}
	
	@Override
	public void exitJsxLiteral(JsxLiteralContext ctx) {
		String text = ctx.getText();
		setNodeValue(ctx, new JsxLiteral(text));
	}
	

	@Override
	public void exitJsx_opening(Jsx_openingContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		String nameSuite = getJsxWhiteSpace(ctx.jsx_ws());
		List<JsxAttribute> attributes = ctx.jsx_attribute().stream()
				.map(cx->this.<JsxAttribute>getNodeValue(cx))
				.collect(Collectors.toList());
		setNodeValue(ctx, new JsxElement(name, nameSuite, attributes, null));
	}
	
	
	@Override
	public void exitJsx_closing(Jsx_closingContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new JsxClosing(name, null));
	}
	
	@Override
	public void exitJsx_self_closing(Jsx_self_closingContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		String nameSuite = getJsxWhiteSpace(ctx.jsx_ws());
		List<JsxAttribute> attributes = ctx.jsx_attribute().stream()
				.map(cx->this.<JsxAttribute>getNodeValue(cx))
				.collect(Collectors.toList());
		setNodeValue(ctx, new JsxSelfClosing(name, nameSuite, attributes, null));
	}
	
	
	@Override
	public void exitKey_token(Key_tokenContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}
	
	@Override
	public void exitLessThanExpression(LessThanExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new CompareExpression(left, CmpOp.LT, right));
	}
	
	@Override
	public void exitLessThanOrEqualExpression(LessThanOrEqualExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new CompareExpression(left, CmpOp.LTE, right));
	}
	
	@Override
	public void exitList_literal(List_literalContext ctx) {
		boolean mutable = ctx.MUTABLE()!=null;
		ExpressionList items = this.<ExpressionList>getNodeValue(ctx.expression_list());
		IExpression value = items==null ? new ListLiteral(mutable) : new ListLiteral(items, mutable);
		setNodeValue(ctx, value);
	}
	
	
	@Override
	public void exitListType(ListTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.l);
		setNodeValue(ctx, new ListType(type));
	}
	
	@Override
	public void exitLiteral_expression(Literal_expressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitLiteral_list_literal(Literal_list_literalContext ctx) {
		ExpressionList items = new ExpressionList();
		ctx.atomic_literal().forEach((l)->{
			IExpression item = this.<IExpression>getNodeValue(l);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitLiteralExpression(LiteralExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	

	
	@Override
	public void exitLiteralListLiteral(LiteralListLiteralContext ctx) {
		ExpressionList items = this.<ExpressionList>getNodeValue(ctx.literal_list_literal());
		setNodeValue(ctx, new ListLiteral(items, false));
	}
	
	@Override
	public void exitLiteralRangeLiteral(LiteralRangeLiteralContext ctx) {
		IExpression low = this.<IExpression>getNodeValue(ctx.low);
		IExpression high = this.<IExpression>getNodeValue(ctx.high);
		setNodeValue(ctx, new RangeLiteral(low, high));
	}
	
	@Override
	public void exitLiteralSetLiteral(LiteralSetLiteralContext ctx) {
		ExpressionList items = this.<ExpressionList>getNodeValue(ctx.literal_list_literal());
		setNodeValue(ctx, new SetLiteral(items));
	}
	
	@Override
	public void exitMatchingExpression(MatchingExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new MatchingExpressionConstraint(exp));
	}

	@Override
	public void exitMatchingList(MatchingListContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new MatchingCollectionConstraint(exp));
	}
	
	@Override
	public void exitMatchingPattern(MatchingPatternContext ctx) {
		setNodeValue(ctx, new MatchingPatternConstraint(new TextLiteral(ctx.text.getText())));
	}


	@Override
	public void exitMatchingRange(MatchingRangeContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new MatchingCollectionConstraint(exp));
	}

	@Override
	public void exitMatchingSet(MatchingSetContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new MatchingCollectionConstraint(exp));
	}
	
	@Override
	public void exitMaxIntegerLiteral(MaxIntegerLiteralContext ctx) {
		setNodeValue(ctx, new MaxIntegerLiteral());
	}

	@Override
	public void exitMember_method_declaration(Member_method_declarationContext ctx) {
		List<CommentStatement> comments = readComments(ctx.comment_statement());
		List<Annotation> annotations = readAnnotations(ctx.annotation_constructor());
		ParseTree ctx_ = ctx.getChild(ctx.getChildCount()-1);
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx_);
		if(decl!=null) {
			decl.setComments(comments);
			decl.setAnnotations(annotations);
			setNodeValue(ctx, decl);
		}
	}
	
	@Override
	public void exitMember_method_declaration_list(Member_method_declaration_listContext ctx) {
		MethodDeclarationList items = new MethodDeclarationList();
		ctx.member_method_declaration().forEach((m)->{
			IMethodDeclaration item = this.<IMethodDeclaration>getNodeValue(m);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}

	@Override
	public void exitMemberInstance(MemberInstanceContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new MemberInstance(name));
	}
	
	@Override
	public void exitMemberSelector(MemberSelectorContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new MemberSelector(name));
	}
	
	@Override
	public void exitMethod_call(Method_callContext ctx) {
		IExpression method = this.<IExpression>getNodeValue(ctx.method);
		ArgumentAssignmentList args = this.<ArgumentAssignmentList>getNodeValue(ctx.args);
		setNodeValue(ctx, new UnresolvedCall(method, args));
	}
	
	
	@Override
	public void exitMethod_declaration(Method_declarationContext ctx) {
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, decl);
	}
	
	
	@Override
	public void exitMethod_expression(Method_expressionContext ctx) {
		IExpression decl = this.<IExpression>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, decl);
	}
	
	
	@Override
	public void exitMethod_identifier(Method_identifierContext ctx) {
		Object id = this.<Object>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, id);
	}
	
	@Override
	public void exitMethodCallStatement(MethodCallStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitMethodExpression(MethodExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitMethodName(MethodNameContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new UnresolvedIdentifier(name));
	}
	
	@Override
	public void exitMethodParent(MethodParentContext ctx) {
		IExpression parent = this.<IExpression>getNodeValue(ctx.parent);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		setNodeValue(ctx, new MethodSelector(parent, name));
	}
	
			
	@Override
	public void exitMinIntegerLiteral(MinIntegerLiteralContext ctx) {
		setNodeValue(ctx, new MinIntegerLiteral());
	}

	@Override
	public void exitMinusExpression(MinusExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new MinusExpression(exp));
	}
	
	@Override
	public void exitModuloExpression(ModuloExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ModuloExpression(left, right));
	}
	
	@Override
	public void exitMultiplyExpression(MultiplyExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new MultiplyExpression(left, right));
	}
	
	@Override
	public void exitMutable_category_type(Mutable_category_typeContext ctx) {
		CategoryType typ = this.<CategoryType>getNodeValue(ctx.category_type());
		typ.setMutable(ctx.MUTABLE()!=null);
		setNodeValue(ctx, typ);
	}
	
	@Override
	public void exitNamed_argument(Named_argumentContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.variable_identifier());
		UnresolvedArgument arg = new UnresolvedArgument(name);
		IExpression exp = this.<IExpression>getNodeValue(ctx.literal_expression());
		arg.setDefaultExpression(exp);
		setNodeValue(ctx, arg);
	}
	
	@Override
	public void exitNative_category_bindings(Native_category_bindingsContext ctx) {
		NativeCategoryBindingList items = this.<NativeCategoryBindingList>getNodeValue(ctx.items);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitNative_category_declaration(Native_category_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		NativeCategoryBindingList bindings = this.<NativeCategoryBindingList>getNodeValue(ctx.bindings);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		NativeCategoryDeclaration decl = new NativeCategoryDeclaration(name, attrs, bindings, null, methods);
		decl.setStorable(ctx.STORABLE()!=null);
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitNative_widget_declaration(Native_widget_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		NativeCategoryBindingList bindings = this.<NativeCategoryBindingList>getNodeValue(ctx.bindings);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		setNodeValue(ctx, new NativeWidgetDeclaration(name, bindings, methods));
	}
	
	@Override
	public void exitNative_getter_declaration(Native_getter_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new NativeGetterMethodDeclaration(name, stmts));
	}
	
	@Override
	public void exitNative_member_method_declaration(Native_member_method_declarationContext ctx) {
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitNative_method_declaration(Native_method_declarationContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.typ);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		ArgumentList args = this.<ArgumentList>getNodeValue(ctx.args);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new NativeMethodDeclaration(name, args, type, stmts));
	}
	
	@Override
	public void exitNative_resource_declaration(Native_resource_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		NativeCategoryBindingList bindings = this.<NativeCategoryBindingList>getNodeValue(ctx.bindings);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		NativeResourceDeclaration decl = new NativeResourceDeclaration(name, attrs, bindings, null, methods);
		decl.setStorable(ctx.STORABLE()!=null);
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitNative_setter_declaration(Native_setter_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new NativeSetterMethodDeclaration(name, stmts));
	}
	
	@Override
	public void exitNative_symbol(Native_symbolContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new NativeSymbol(name, exp));
	}

	@Override
	public void exitNativeCategoryBindingList(NativeCategoryBindingListContext ctx) {
		NativeCategoryBinding item = this.<NativeCategoryBinding>getNodeValue(ctx.item);
		NativeCategoryBindingList items = new NativeCategoryBindingList(item);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitNativeCategoryBindingListItem(NativeCategoryBindingListItemContext ctx) {
		NativeCategoryBinding item = this.<NativeCategoryBinding>getNodeValue(ctx.item);
		NativeCategoryBindingList items = this.<NativeCategoryBindingList>getNodeValue(ctx.items);
		items.add(item);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitNativeCategoryDeclaration(NativeCategoryDeclarationContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.decl));
	}
	
	@Override
	public void exitNativeWidgetDeclaration(NativeWidgetDeclarationContext ctx) {
		setNodeValue(ctx, this.<Object>getNodeValue(ctx.decl));
	}
	
	@Override
	public void exitNative_member_method_declaration_list(Native_member_method_declaration_listContext ctx) {
		MethodDeclarationList items = new MethodDeclarationList();
		ctx.native_member_method_declaration().forEach((m)->{
			IMethodDeclaration item = this.<IMethodDeclaration>getNodeValue(m);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	
	@Override
	public void exitNative_statement_list(Native_statement_listContext ctx) {
		StatementList items = new StatementList();
		ctx.native_statement().forEach((s)->{
			IStatement item = this.<IStatement>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	

	
	@Override
	public void exitNative_symbol_list(Native_symbol_listContext ctx) {
		NativeSymbolList items = new NativeSymbolList();
		ctx.native_symbol().forEach((s)->{
			NativeSymbol item = this.<NativeSymbol>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitNativeType(NativeTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.n);
		setNodeValue(ctx, type);
	}
	
	@Override
	public void exitNotHasExpression(NotHasExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.NOT_HAS, right));
	}
	
	@Override
	public void exitNotHasAllExpression(NotHasAllExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.NOT_HAS_ALL, right));
	}
	
	@Override
	public void exitNotHasAnyExpression(NotHasAnyExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.NOT_HAS_ANY, right));
	}
	
	@Override
	public void exitNotContainsExpression(NotContainsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new EqualsExpression(left, EqOp.NOT_CONTAINS, right));
	}
	
	@Override
	public void exitNotEqualsExpression(NotEqualsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new EqualsExpression(left, EqOp.NOT_EQUALS, right));
	}
	
	@Override
	public void exitNotExpression(NotExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new NotExpression(exp));
	}
	
	@Override
	public void exitNotInExpression(NotInExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new ContainsExpression(left, ContOp.NOT_IN, right));
	}
	
	@Override
	public void exitNullLiteral(NullLiteralContext ctx) {
		setNodeValue(ctx, NullLiteral.instance());
	}
	
	
	@Override
	public void exitOperator_argument(Operator_argumentContext ctx) {
		IArgument arg = this.<IArgument>getNodeValue(ctx.getChild(0));
		setNodeValue(ctx, arg);
	}
	
	@Override
	public void exitOperator_method_declaration(Operator_method_declarationContext ctx) {
		Operator op = this.<Operator>getNodeValue(ctx.op);
		IArgument arg = this.<IArgument>getNodeValue(ctx.arg);
		IType typ = this.<IType>getNodeValue(ctx.typ);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		OperatorMethodDeclaration decl = new OperatorMethodDeclaration(op, arg, typ, stmts);
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitOperatorArgument(OperatorArgumentContext ctx) {
		boolean mutable = ctx.MUTABLE()!=null;
		IArgument arg = this.<IArgument>getNodeValue(ctx.arg);
		arg.setMutable(mutable);
		setNodeValue(ctx, arg);
	}
	
	@Override
	public void exitOperatorDivide(OperatorDivideContext ctx) {
		setNodeValue(ctx, Operator.DIVIDE);
	}
	
	@Override
	public void exitOperatorIDivide(OperatorIDivideContext ctx) {
		setNodeValue(ctx, Operator.IDIVIDE);
	}
	
	@Override
	public void exitOperatorMinus(OperatorMinusContext ctx) {
		setNodeValue(ctx, Operator.MINUS);
	}
	
	@Override
	public void exitOperatorModulo(OperatorModuloContext ctx) {
		setNodeValue(ctx, Operator.MODULO);
	}
	
	@Override
	public void exitOperatorMultiply(OperatorMultiplyContext ctx) {
		setNodeValue(ctx, Operator.MULTIPLY);
	}
	
	@Override
	public void exitOperatorPlus(OperatorPlusContext ctx) {
		setNodeValue(ctx, Operator.PLUS);
	}
	
	@Override
	public void exitOrder_by(Order_byContext ctx) {
		IdentifierList names = new IdentifierList();
		for(Variable_identifierContext ctx_ : ctx.variable_identifier())
			names.add(this.<Identifier>getNodeValue(ctx_));
		OrderByClause clause = new OrderByClause(names, ctx.DESC()!=null);
		setNodeValue(ctx, clause);
	}
	
	@Override
	public void exitOrder_by_list(Order_by_listContext ctx) {
		OrderByClauseList list = new OrderByClauseList();
		for(Order_byContext ctx_ : ctx.order_by())
			list.add(this.<OrderByClause>getNodeValue(ctx_));
		setNodeValue(ctx, list);
	}

	@Override
	public void exitOrExpression(OrExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new OrExpression(left, right));
	}
	
	@Override
	public void exitParenthesis_expression(Parenthesis_expressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.expression());
		setNodeValue(ctx, new ParenthesisExpression(exp));
	}
	
	@Override
	public void exitParenthesisExpression(ParenthesisExpressionContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPeriodLiteral(PeriodLiteralContext ctx) {
		setNodeValue(ctx, new PeriodLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitPeriodType(PeriodTypeContext ctx) {
		setNodeValue(ctx, PeriodType.instance());
	}

	@Override
	public void exitPrimaryType(PrimaryTypeContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.p);
		setNodeValue(ctx, type);
	}
	
	public void exitPython_category_binding(Python_category_bindingContext ctx) {
		String identifier = ctx.identifier().getText();
		PythonModule module = this.<PythonModule>getNodeValue(ctx.python_module());
		PythonNativeCategoryBinding map = new PythonNativeCategoryBinding(identifier, module);
		setNodeValue(ctx, map);
	}
	
	@Override
	public void exitPython_identifier(Python_identifierContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}
	
	@Override
	public void exitPython_method_expression(Python_method_expressionContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		PythonArgumentList args = this.<PythonArgumentList>getNodeValue(ctx.args);
		PythonMethodExpression method = new PythonMethodExpression(name);
		method.setArguments(args);
		setNodeValue(ctx, method);
	}
	
	@Override
	public void exitPython_module(Python_moduleContext ctx) {
		List<String> ids = new ArrayList<String>();
		for(IdentifierContext ic : ctx.identifier())
			ids.add(ic.getText());
		PythonModule module = new PythonModule(ids);
		setNodeValue(ctx, module);
	}
	
	@Override
	public void exitPython_native_statement(Python_native_statementContext ctx) {
		PythonStatement stmt = this.<PythonStatement>getNodeValue(ctx.python_statement());
		PythonModule module = this.<PythonModule>getNodeValue(ctx.python_module());
		stmt.setModule(module);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitPython2CategoryBinding(Python2CategoryBindingContext ctx) {
		PythonNativeCategoryBinding map = this.<PythonNativeCategoryBinding>getNodeValue(ctx.binding);
		setNodeValue(ctx, new Python2NativeCategoryBinding(map));
	}
	
	@Override
	public void exitPython2NativeStatement(Python2NativeStatementContext ctx) {
		PythonStatement stmt = this.<PythonStatement>getNodeValue(ctx.python_native_statement());
		setNodeValue(ctx, new Python2NativeCall(stmt));
	}
	
	@Override
	public void exitPython3CategoryBinding(Python3CategoryBindingContext ctx) {
		PythonNativeCategoryBinding map = this.<PythonNativeCategoryBinding>getNodeValue(ctx.binding);
		setNodeValue(ctx, new Python3NativeCategoryBinding(map));
	}
	
	@Override
	public void exitPython3NativeStatement(Python3NativeStatementContext ctx) {
		PythonStatement stmt = this.<PythonStatement>getNodeValue(ctx.python_native_statement());
		setNodeValue(ctx, new Python3NativeCall(stmt));
	}
	
	@Override
	public void exitPythonArgumentList(PythonArgumentListContext ctx) {
		PythonArgumentList ordinal = this.<PythonArgumentList>getNodeValue(ctx.ordinal);
		PythonArgumentList named = this.<PythonArgumentList>getNodeValue(ctx.named);
		if(ordinal==null)
			ordinal = new PythonArgumentList();
		if(named!=null)
			ordinal.addAll(named);
		setNodeValue(ctx, ordinal);
	}
	
	@Override
	public void exitPythonBooleanLiteral(PythonBooleanLiteralContext ctx) {
		setNodeValue(ctx, new PythonBooleanLiteral(ctx.getText()));
	}
	
	@Override
	public void exitPythonCharacterLiteral(PythonCharacterLiteralContext ctx) {
		setNodeValue(ctx, new PythonCharacterLiteral(ctx.getText()));
	};
	
	@Override
	public void exitPythonChildIdentifier(PythonChildIdentifierContext ctx) {
		PythonIdentifierExpression parent = this.<PythonIdentifierExpression>getNodeValue(ctx.parent);
		String name = this.<String>getNodeValue(ctx.name);
		PythonIdentifierExpression child = new PythonIdentifierExpression(parent, name);
		setNodeValue(ctx, child);
	}
	
	@Override
	public void exitPythonDecimalLiteral(PythonDecimalLiteralContext ctx) {
		setNodeValue(ctx, new PythonDecimalLiteral(ctx.getText()));
	}
	
	@Override
	public void exitPythonGlobalMethodExpression(PythonGlobalMethodExpressionContext ctx) {
		PythonMethodExpression exp = this.<PythonMethodExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPythonIdentifier(PythonIdentifierContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		setNodeValue(ctx, new PythonIdentifierExpression(name));
	}
	
	@Override
	public void exitPythonIdentifierExpression(PythonIdentifierExpressionContext ctx) {
		PythonIdentifierExpression exp = this.<PythonIdentifierExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPythonIntegerLiteral(PythonIntegerLiteralContext ctx) {
		setNodeValue(ctx, new PythonIntegerLiteral(ctx.getText()));
	}
	
	@Override
	public void exitPythonLiteralExpression(PythonLiteralExpressionContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPythonMethodExpression(PythonMethodExpressionContext ctx) {
		PythonMethodExpression exp = this.<PythonMethodExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPythonNamedArgumentList(PythonNamedArgumentListContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		PythonNamedArgument arg = new PythonNamedArgument(name, exp);
		setNodeValue(ctx, new PythonArgumentList(arg));
	}
	
	@Override
	public void exitPythonNamedArgumentListItem(PythonNamedArgumentListItemContext ctx) {
		String name = this.<String>getNodeValue(ctx.name);
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		PythonNamedArgument arg = new PythonNamedArgument(name, exp);
		PythonArgumentList items = this.<PythonArgumentList>getNodeValue(ctx.items);
		items.add(arg);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitPythonNamedOnlyArgumentList(PythonNamedOnlyArgumentListContext ctx) {
		PythonArgumentList named = this.<PythonArgumentList>getNodeValue(ctx.named);
		setNodeValue(ctx, named);
	}
	
	@Override
	public void exitPythonOrdinalArgumentList( PythonOrdinalArgumentListContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.item);
		PythonOrdinalArgument arg = new PythonOrdinalArgument(exp);
		setNodeValue(ctx, new PythonArgumentList(arg));
	}
	
	@Override
	public void exitPythonOrdinalArgumentListItem( PythonOrdinalArgumentListItemContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.item);
		PythonOrdinalArgument arg = new PythonOrdinalArgument(exp);
		PythonArgumentList items = this.<PythonArgumentList>getNodeValue(ctx.items);
		items.add(arg);
		setNodeValue(ctx, items);
	}
	
	@Override
	public void exitPythonOrdinalOnlyArgumentList(PythonOrdinalOnlyArgumentListContext ctx) {
		PythonArgumentList ordinal = this.<PythonArgumentList>getNodeValue(ctx.ordinal);
		setNodeValue(ctx, ordinal);
	}
	
	@Override
	public void exitPythonPromptoIdentifier(PythonPromptoIdentifierContext ctx) {
		String name = ctx.DOLLAR_IDENTIFIER().getText();
		setNodeValue(ctx, new PythonIdentifierExpression(name));
	}
	
	@Override
	public void exitPythonPrimaryExpression(PythonPrimaryExpressionContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, exp);
	}
	
	@Override
	public void exitPythonReturnStatement(PythonReturnStatementContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new PythonStatement(exp,true));
	}
	
	@Override
	public void exitPythonSelectorExpression(PythonSelectorExpressionContext ctx) {
		PythonExpression parent = this.<PythonExpression>getNodeValue(ctx.parent);
		PythonSelectorExpression selector = this.<PythonSelectorExpression>getNodeValue(ctx.child);
		selector.setParent(parent);
		setNodeValue(ctx, selector);
	}
	
	
	@Override
	public void exitPythonSelfExpression(PythonSelfExpressionContext ctx) {
		setNodeValue(ctx, new PythonSelfExpression());
	}
	
	
	@Override
	public void exitPythonStatement(PythonStatementContext ctx) {
		PythonExpression exp = this.<PythonExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new PythonStatement(exp,false));
	}
	
	@Override
	public void exitPythonTextLiteral(PythonTextLiteralContext ctx) {
		setNodeValue(ctx, new PythonTextLiteral(ctx.getText()));
	}
	
	@Override
	public void exitRaise_statement(Raise_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new RaiseStatement(exp));
	}
	
	@Override
	public void exitRaiseStatement(RaiseStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitRange_literal(Range_literalContext ctx) {
		IExpression low = this.<IExpression>getNodeValue(ctx.low);
		IExpression high = this.<IExpression>getNodeValue(ctx.high);
		setNodeValue(ctx, new RangeLiteral(low, high));
	}
	
	@Override
	public void exitRead_all_expression(Read_all_expressionContext ctx) {
		IExpression source = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new ReadAllExpression(source));
	}
	
	@Override
	public void exitRead_one_expression(Read_one_expressionContext ctx) {
		IExpression source = this.<IExpression>getNodeValue(ctx.source);
		setNodeValue(ctx, new ReadOneExpression(source));
	}
	
	@Override
	public void exitResource_declaration(Resource_declarationContext ctx) {
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx.native_resource_declaration());
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitReturn_statement(Return_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		setNodeValue(ctx, new ReturnStatement(exp));
	}
	
	@Override
	public void exitReturnStatement(ReturnStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitRootInstance(RootInstanceContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.variable_identifier());
		setNodeValue(ctx, new VariableInstance(name));
	}
	
	@Override
	public void exitRoughlyEqualsExpression(RoughlyEqualsExpressionContext ctx) {
		IExpression left = this.<IExpression>getNodeValue(ctx.left);
		IExpression right = this.<IExpression>getNodeValue(ctx.right);
		setNodeValue(ctx, new EqualsExpression(left, EqOp.ROUGHLY, right));
	}
	
	@Override
	public void exitSelectableExpression(SelectableExpressionContext ctx) {
		IExpression parent = this.<IExpression>getNodeValue(ctx.parent);
		setNodeValue(ctx, parent);
	}
	
	@Override
	public void exitSelectorExpression(SelectorExpressionContext ctx) {
		IExpression parent = this.<IExpression>getNodeValue(ctx.parent);
		SelectorExpression selector = this.<SelectorExpression>getNodeValue(ctx.selector);
		selector.setParent(parent);
		setNodeValue(ctx, selector);
	}
	
	@Override
	public void exitSet_literal(Set_literalContext ctx) {
		ExpressionList items = this.<ExpressionList>getNodeValue(ctx.expression_list());
		ContainerLiteral<SetValue> set = items==null ? new SetLiteral() : new SetLiteral(items);
		setNodeValue(ctx, set);
	}
	
	@Override
	public void exitSetter_method_declaration(Setter_method_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new SetterMethodDeclaration(name, stmts));
	}
	
	@Override
	public void exitSetType(SetTypeContext ctx) {
		IType itemType = this.<IType>getNodeValue(ctx.s);
		setNodeValue(ctx, new SetType(itemType));
	}
	
	@Override
	public void exitSingleton_category_declaration(Singleton_category_declarationContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		MethodDeclarationList methods = this.<MethodDeclarationList>getNodeValue(ctx.methods);
		setNodeValue(ctx, new SingletonCategoryDeclaration(name, attrs, methods));
	}
	
	@Override
	public void exitSingletonCategoryDeclaration(SingletonCategoryDeclarationContext ctx) {
		IDeclaration decl = this.<IDeclaration>getNodeValue(ctx.decl);
		setNodeValue(ctx, decl);
	}
	
	@Override
	public void exitSliceFirstAndLast(SliceFirstAndLastContext ctx) {
		IExpression first = this.<IExpression>getNodeValue(ctx.first);
		IExpression last = this.<IExpression>getNodeValue(ctx.last);
		setNodeValue(ctx, new SliceSelector(first, last));
	}
	
	@Override
	public void exitSliceFirstOnly(SliceFirstOnlyContext ctx) {
		IExpression first = this.<IExpression>getNodeValue(ctx.first);
		setNodeValue(ctx, new SliceSelector(first, null));
	}
	
	@Override
	public void exitSliceLastOnly(SliceLastOnlyContext ctx) {
		IExpression last = this.<IExpression>getNodeValue(ctx.last);
		setNodeValue(ctx, new SliceSelector(null, last));
	}
	
	@Override
	public void exitSliceSelector(SliceSelectorContext ctx) {
		IExpression slice = this.<IExpression>getNodeValue(ctx.xslice);
		setNodeValue(ctx, slice);
	}
	
	@Override
	public void exitSorted_expression(Sorted_expressionContext ctx) {
		IExpression source = this.<IExpression>getNodeValue(ctx.source);
		boolean descending = ctx.DESC()!=null;
		IExpression key = this.<IExpression>getNodeValue(ctx.key);
		setNodeValue(ctx, new SortedExpression(source, descending, key));
	}
	

	@Override
	public void exitStatement_list(Statement_listContext ctx) {
		StatementList items = new StatementList();
		ctx.statement().forEach((s)->{
			IStatement item = this.<IStatement>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	
	@Override
	public void exitStore_statement(Store_statementContext ctx) {
		ExpressionList deleted = this.<ExpressionList>getNodeValue(ctx.to_del);
		ExpressionList added = this.<ExpressionList>getNodeValue(ctx.to_add);
		StoreStatement stmt = new StoreStatement(deleted, added);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitStoreStatement(StoreStatementContext ctx) {
		setNodeValue(ctx, getNodeValue(ctx.stmt));
	}

	@Override
	public void exitSwitch_statement(Switch_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		SwitchCaseList cases = this.<SwitchCaseList>getNodeValue(ctx.cases);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		SwitchStatement stmt = new SwitchStatement(exp, cases, stmts);
		setNodeValue(ctx, stmt);
	}
	
	
	@Override
	public void exitSwitch_case_statement_list(Switch_case_statement_listContext ctx) {
		SwitchCaseList items = new SwitchCaseList();
		ctx.switch_case_statement().forEach((s)->{
			SwitchCase item = this.<SwitchCase>getNodeValue(s);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}

	@Override
	public void exitSwitchStatement(SwitchStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}

	@Override
	public void exitSymbol_identifier(Symbol_identifierContext ctx) {
		setNodeValue(ctx, new Identifier(ctx.getText()));
	}
	
	@Override
	public void exitSymbolIdentifier(SymbolIdentifierContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.symbol_identifier());
		setNodeValue(ctx, name);
	}
	
	@Override
	public void exitSymbol_list(Symbol_listContext ctx) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void exitSymbols_token(Symbols_tokenContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}
	
	@Override
	public void exitTernaryExpression(TernaryExpressionContext ctx) {
		IExpression condition = this.<IExpression>getNodeValue(ctx.test);
		IExpression ifTrue = this.<IExpression>getNodeValue(ctx.ifTrue);
		IExpression ifFalse = this.<IExpression>getNodeValue(ctx.ifFalse);
		TernaryExpression exp = new TernaryExpression(condition, ifTrue, ifFalse);
		setNodeValue(ctx, exp);
	};

	@Override
	public void exitTest_method_declaration(Test_method_declarationContext ctx) {
		Identifier name = new Identifier(ctx.name.getText());
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		AssertionList exps = this.<AssertionList>getNodeValue(ctx.exps);
		Identifier errorName = this.<Identifier>getNodeValue(ctx.error);
		SymbolExpression error = errorName==null ? null : new SymbolExpression(errorName);
		setNodeValue(ctx, new TestMethodDeclaration(name, stmts, exps, error));
	}

	@Override
	public void exitTextLiteral(TextLiteralContext ctx) {
		setNodeValue(ctx, new TextLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitTextType(TextTypeContext ctx) {
		setNodeValue(ctx, TextType.instance());
	}
	
	@Override
	public void exitThisExpression(ThisExpressionContext ctx) {
		setNodeValue(ctx, new ThisExpression());
	}
	
	@Override
	public void exitTimeLiteral(TimeLiteralContext ctx) {
		setNodeValue(ctx, new TimeLiteral(ctx.t.getText()));
	}
	
	@Override
	public void exitTimeType(TimeTypeContext ctx) {
		setNodeValue(ctx, TimeType.instance());
	}
	
	@Override
	public void exitTry_statement(Try_statementContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		SwitchCaseList handlers = this.<SwitchCaseList>getNodeValue(ctx.handlers);
		StatementList anyStmts = this.<StatementList>getNodeValue(ctx.anyStmts);
		StatementList finalStmts = this.<StatementList>getNodeValue(ctx.finalStmts);
		SwitchErrorStatement stmt = new SwitchErrorStatement(name, stmts, handlers, anyStmts, finalStmts);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitTryStatement(TryStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitTuple_literal(Tuple_literalContext ctx) {
		boolean mutable = ctx.MUTABLE()!=null;
		ExpressionList items = this.<ExpressionList>getNodeValue(ctx.expression_tuple());
		IExpression value = items==null ? new TupleLiteral(mutable) : new TupleLiteral(items, mutable);
		setNodeValue(ctx, value);
	}
	
	@Override
	public void exitType_identifier(Type_identifierContext ctx) {
		setNodeValue(ctx, new Identifier(ctx.getText()));
	}
	
	@Override
	public void exitTyped_argument(Typed_argumentContext ctx) {
		IType type = this.<IType>getNodeValue(ctx.typ);
		Identifier name = this.<Identifier>getNodeValue(ctx.name);
		IdentifierList attrs = this.<IdentifierList>getNodeValue(ctx.attrs);
		CategoryArgument arg = attrs==null ?
				new CategoryArgument(type, name) : 
				new ExtendedArgument(type, name, attrs); 
		IExpression exp = this.<IExpression>getNodeValue(ctx.value);
		arg.setDefaultExpression(exp);
		setNodeValue(ctx, arg);
	}
	
	@Override
	public void exitTypeIdentifier(TypeIdentifierContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.type_identifier());
		setNodeValue(ctx, name);
	}
	
	@Override
	public void exitType_identifier_list(Type_identifier_listContext ctx) {
		IdentifierList items = new IdentifierList();
		ctx.type_identifier().forEach((i)->{
			Identifier item = this.<Identifier>getNodeValue(i);
			items.add(item);
		});
		setNodeValue(ctx, items);
	}
	
	
	@Override
	public void exitUUIDLiteral(UUIDLiteralContext ctx) {
		setNodeValue(ctx, new UUIDLiteral(ctx.t.getText()));
	}
	
	
	@Override
	public void exitUUIDType(UUIDTypeContext ctx) {
		setNodeValue(ctx, UUIDType.instance());
	}
	
	@Override
	public void exitValue_token(Value_tokenContext ctx) {
		setNodeValue(ctx, ctx.getText());
	}

	
	@Override
	public void exitVariable_identifier(Variable_identifierContext ctx) {
		setNodeValue(ctx, new Identifier(ctx.getText()));
	}
	
	@Override
	public void exitVariableIdentifier(VariableIdentifierContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.variable_identifier());
		setNodeValue(ctx, name);
	}
	
	@Override
	public void exitVariable_identifier_list(Variable_identifier_listContext ctx) {
		IdentifierList list = new IdentifierList();
		for(Variable_identifierContext v : ctx.variable_identifier()){
			Identifier item = this.<Identifier>getNodeValue(v);
			list.add(item);
		}
		setNodeValue(ctx, list);
	}
	
	@Override
	public void exitVersionType(VersionTypeContext ctx) {
		setNodeValue(ctx, VersionType.instance());
	}

	@Override
	public void exitVersionLiteral(VersionLiteralContext ctx) {
		setNodeValue(ctx, new VersionLiteral(ctx.t.getText()));
	}
	
	
	@Override
	public void exitWhile_statement(While_statementContext ctx) {
		IExpression exp = this.<IExpression>getNodeValue(ctx.exp);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new WhileStatement(exp, stmts));
	}
	
	@Override
	public void exitWhileStatement(WhileStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitWith_resource_statement(With_resource_statementContext ctx) {
		AssignVariableStatement stmt = this.<AssignVariableStatement>getNodeValue(ctx.stmt);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new WithResourceStatement(stmt, stmts));
	}
	
	@Override
	public void exitWith_singleton_statement(With_singleton_statementContext ctx) {
		Identifier name = this.<Identifier>getNodeValue(ctx.typ);
		CategoryType type = new CategoryType(name);
		StatementList stmts = this.<StatementList>getNodeValue(ctx.stmts);
		setNodeValue(ctx, new WithSingletonStatement(type, stmts));
	}
	
	@Override
	public void exitWithResourceStatement(WithResourceStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitWithSingletonStatement(WithSingletonStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	@Override
	public void exitWrite_statement(Write_statementContext ctx) {
		IExpression what = this.<IExpression>getNodeValue(ctx.what);
		IExpression target = this.<IExpression>getNodeValue(ctx.target);
		setNodeValue(ctx, new WriteStatement(what, target));
	}
	
	@Override
	public void exitWriteStatement(WriteStatementContext ctx) {
		IStatement stmt = this.<IStatement>getNodeValue(ctx.stmt);
		setNodeValue(ctx, stmt);
	}
	
	private Token findFirstValidToken(int idx) {
		if(idx==-1) // happens because input.index() is called before any other read operation (bug?)
			idx = 0;
		do {
			Token token = readValidToken(idx++);
			if(token!=null)
				return token;
		} while(idx<input.size());
		return null;
	}
	
	private Token findLastValidToken(int idx) {
		if(idx==-1) // happens because input.index() is called before any other read operation (bug?)
			idx = 0;
		while(idx>=0) {
			Token token = readValidToken(idx--);
			if(token!=null)
				return token;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getNodeValue(ParseTree node) {
		return (T)nodeValues.get(node);
	};
	
	private Token readValidToken(int idx) {
		Token token = input.get(idx);
		String text = token.getText();
		if(text!=null && text.length()>0 && !Character.isWhitespace(text.charAt(0)))
			return token;
		else
			return null;
	}
	
	public void setNodeValue(ParserRuleContext node, Section value) {
		nodeValues.put(node, value);
		buildSection(node, value);
	}
	
	public void setNodeValue(ParseTree node, Object value) {
		nodeValues.put(node, value);
	}
	
}