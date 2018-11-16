// Generated by reducer.js
/**
 * Copyright 2018 Shape Security, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.shapesecurity.shift.es2017.reducer;

import com.shapesecurity.functional.data.ImmutableList;
import com.shapesecurity.functional.data.Maybe;
import com.shapesecurity.shift.es2017.ast.*;
import com.shapesecurity.shift.es2017.ast.Module;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface ThunkedReducer<State> {
    @Nonnull
    State reduceArrayAssignmentTarget(
            @Nonnull ArrayAssignmentTarget node,
            @Nonnull ImmutableList<Maybe<Supplier<State>>> elements,
            @Nonnull Maybe<Supplier<State>> rest);

    @Nonnull
    State reduceArrayBinding(
            @Nonnull ArrayBinding node,
            @Nonnull ImmutableList<Maybe<Supplier<State>>> elements,
            @Nonnull Maybe<Supplier<State>> rest);

    @Nonnull
    State reduceArrayExpression(
            @Nonnull ArrayExpression node,
            @Nonnull ImmutableList<Maybe<Supplier<State>>> elements);

    @Nonnull
    State reduceArrowExpression(
            @Nonnull ArrowExpression node,
            @Nonnull Supplier<State> params,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceAssignmentExpression(
            @Nonnull AssignmentExpression node,
            @Nonnull Supplier<State> binding,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceAssignmentTargetIdentifier(@Nonnull AssignmentTargetIdentifier node);

    @Nonnull
    State reduceAssignmentTargetPropertyIdentifier(
            @Nonnull AssignmentTargetPropertyIdentifier node,
            @Nonnull Supplier<State> binding,
            @Nonnull Maybe<Supplier<State>> init);

    @Nonnull
    State reduceAssignmentTargetPropertyProperty(
            @Nonnull AssignmentTargetPropertyProperty node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> binding);

    @Nonnull
    State reduceAssignmentTargetWithDefault(
            @Nonnull AssignmentTargetWithDefault node,
            @Nonnull Supplier<State> binding,
            @Nonnull Supplier<State> init);

    @Nonnull
    State reduceAwaitExpression(
            @Nonnull AwaitExpression node,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceBinaryExpression(
            @Nonnull BinaryExpression node,
            @Nonnull Supplier<State> left,
            @Nonnull Supplier<State> right);

    @Nonnull
    State reduceBindingIdentifier(@Nonnull BindingIdentifier node);

    @Nonnull
    State reduceBindingPropertyIdentifier(
            @Nonnull BindingPropertyIdentifier node,
            @Nonnull Supplier<State> binding,
            @Nonnull Maybe<Supplier<State>> init);

    @Nonnull
    State reduceBindingPropertyProperty(
            @Nonnull BindingPropertyProperty node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> binding);

    @Nonnull
    State reduceBindingWithDefault(
            @Nonnull BindingWithDefault node,
            @Nonnull Supplier<State> binding,
            @Nonnull Supplier<State> init);

    @Nonnull
    State reduceBlock(
            @Nonnull Block node,
            @Nonnull ImmutableList<Supplier<State>> statements);

    @Nonnull
    State reduceBlockStatement(
            @Nonnull BlockStatement node,
            @Nonnull Supplier<State> block);

    @Nonnull
    State reduceBreakStatement(@Nonnull BreakStatement node);

    @Nonnull
    State reduceCallExpression(
            @Nonnull CallExpression node,
            @Nonnull Supplier<State> callee,
            @Nonnull ImmutableList<Supplier<State>> arguments);

    @Nonnull
    State reduceCatchClause(
            @Nonnull CatchClause node,
            @Nonnull Supplier<State> binding,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceClassDeclaration(
            @Nonnull ClassDeclaration node,
            @Nonnull Supplier<State> name,
            @Nonnull Maybe<Supplier<State>> _super,
            @Nonnull ImmutableList<Supplier<State>> elements);

    @Nonnull
    State reduceClassElement(
            @Nonnull ClassElement node,
            @Nonnull Supplier<State> method);

    @Nonnull
    State reduceClassExpression(
            @Nonnull ClassExpression node,
            @Nonnull Maybe<Supplier<State>> name,
            @Nonnull Maybe<Supplier<State>> _super,
            @Nonnull ImmutableList<Supplier<State>> elements);

    @Nonnull
    State reduceCompoundAssignmentExpression(
            @Nonnull CompoundAssignmentExpression node,
            @Nonnull Supplier<State> binding,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceComputedMemberAssignmentTarget(
            @Nonnull ComputedMemberAssignmentTarget node,
            @Nonnull Supplier<State> object,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceComputedMemberExpression(
            @Nonnull ComputedMemberExpression node,
            @Nonnull Supplier<State> object,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceComputedPropertyName(
            @Nonnull ComputedPropertyName node,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceConditionalExpression(
            @Nonnull ConditionalExpression node,
            @Nonnull Supplier<State> test,
            @Nonnull Supplier<State> consequent,
            @Nonnull Supplier<State> alternate);

    @Nonnull
    State reduceContinueStatement(@Nonnull ContinueStatement node);

    @Nonnull
    State reduceDataProperty(
            @Nonnull DataProperty node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceDebuggerStatement(@Nonnull DebuggerStatement node);

    @Nonnull
    State reduceDirective(@Nonnull Directive node);

    @Nonnull
    State reduceDoWhileStatement(
            @Nonnull DoWhileStatement node,
            @Nonnull Supplier<State> body,
            @Nonnull Supplier<State> test);

    @Nonnull
    State reduceEmptyStatement(@Nonnull EmptyStatement node);

    @Nonnull
    State reduceExport(
            @Nonnull Export node,
            @Nonnull Supplier<State> declaration);

    @Nonnull
    State reduceExportAllFrom(@Nonnull ExportAllFrom node);

    @Nonnull
    State reduceExportDefault(
            @Nonnull ExportDefault node,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceExportFrom(
            @Nonnull ExportFrom node,
            @Nonnull ImmutableList<Supplier<State>> namedExports);

    @Nonnull
    State reduceExportFromSpecifier(@Nonnull ExportFromSpecifier node);

    @Nonnull
    State reduceExportLocalSpecifier(
            @Nonnull ExportLocalSpecifier node,
            @Nonnull Supplier<State> name);

    @Nonnull
    State reduceExportLocals(
            @Nonnull ExportLocals node,
            @Nonnull ImmutableList<Supplier<State>> namedExports);

    @Nonnull
    State reduceExpressionStatement(
            @Nonnull ExpressionStatement node,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceForInStatement(
            @Nonnull ForInStatement node,
            @Nonnull Supplier<State> left,
            @Nonnull Supplier<State> right,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceForOfStatement(
            @Nonnull ForOfStatement node,
            @Nonnull Supplier<State> left,
            @Nonnull Supplier<State> right,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceForStatement(
            @Nonnull ForStatement node,
            @Nonnull Maybe<Supplier<State>> init,
            @Nonnull Maybe<Supplier<State>> test,
            @Nonnull Maybe<Supplier<State>> update,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceFormalParameters(
            @Nonnull FormalParameters node,
            @Nonnull ImmutableList<Supplier<State>> items,
            @Nonnull Maybe<Supplier<State>> rest);

    @Nonnull
    State reduceFunctionBody(
            @Nonnull FunctionBody node,
            @Nonnull ImmutableList<Supplier<State>> directives,
            @Nonnull ImmutableList<Supplier<State>> statements);

    @Nonnull
    State reduceFunctionDeclaration(
            @Nonnull FunctionDeclaration node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> params,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceFunctionExpression(
            @Nonnull FunctionExpression node,
            @Nonnull Maybe<Supplier<State>> name,
            @Nonnull Supplier<State> params,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceGetter(
            @Nonnull Getter node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceIdentifierExpression(@Nonnull IdentifierExpression node);

    @Nonnull
    State reduceIfStatement(
            @Nonnull IfStatement node,
            @Nonnull Supplier<State> test,
            @Nonnull Supplier<State> consequent,
            @Nonnull Maybe<Supplier<State>> alternate);

    @Nonnull
    State reduceImport(
            @Nonnull Import node,
            @Nonnull Maybe<Supplier<State>> defaultBinding,
            @Nonnull ImmutableList<Supplier<State>> namedImports);

    @Nonnull
    State reduceImportNamespace(
            @Nonnull ImportNamespace node,
            @Nonnull Maybe<Supplier<State>> defaultBinding,
            @Nonnull Supplier<State> namespaceBinding);

    @Nonnull
    State reduceImportSpecifier(
            @Nonnull ImportSpecifier node,
            @Nonnull Supplier<State> binding);

    @Nonnull
    State reduceLabeledStatement(
            @Nonnull LabeledStatement node,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceLiteralBooleanExpression(@Nonnull LiteralBooleanExpression node);

    @Nonnull
    State reduceLiteralInfinityExpression(@Nonnull LiteralInfinityExpression node);

    @Nonnull
    State reduceLiteralNullExpression(@Nonnull LiteralNullExpression node);

    @Nonnull
    State reduceLiteralNumericExpression(@Nonnull LiteralNumericExpression node);

    @Nonnull
    State reduceLiteralRegExpExpression(@Nonnull LiteralRegExpExpression node);

    @Nonnull
    State reduceLiteralStringExpression(@Nonnull LiteralStringExpression node);

    @Nonnull
    State reduceMethod(
            @Nonnull Method node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> params,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceModule(
            @Nonnull Module node,
            @Nonnull ImmutableList<Supplier<State>> directives,
            @Nonnull ImmutableList<Supplier<State>> items);

    @Nonnull
    State reduceNewExpression(
            @Nonnull NewExpression node,
            @Nonnull Supplier<State> callee,
            @Nonnull ImmutableList<Supplier<State>> arguments);

    @Nonnull
    State reduceNewTargetExpression(@Nonnull NewTargetExpression node);

    @Nonnull
    State reduceObjectAssignmentTarget(
            @Nonnull ObjectAssignmentTarget node,
            @Nonnull ImmutableList<Supplier<State>> properties);

    @Nonnull
    State reduceObjectBinding(
            @Nonnull ObjectBinding node,
            @Nonnull ImmutableList<Supplier<State>> properties);

    @Nonnull
    State reduceObjectExpression(
            @Nonnull ObjectExpression node,
            @Nonnull ImmutableList<Supplier<State>> properties);

    @Nonnull
    State reduceReturnStatement(
            @Nonnull ReturnStatement node,
            @Nonnull Maybe<Supplier<State>> expression);

    @Nonnull
    State reduceScript(
            @Nonnull Script node,
            @Nonnull ImmutableList<Supplier<State>> directives,
            @Nonnull ImmutableList<Supplier<State>> statements);

    @Nonnull
    State reduceSetter(
            @Nonnull Setter node,
            @Nonnull Supplier<State> name,
            @Nonnull Supplier<State> param,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceShorthandProperty(
            @Nonnull ShorthandProperty node,
            @Nonnull Supplier<State> name);

    @Nonnull
    State reduceSpreadElement(
            @Nonnull SpreadElement node,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceStaticMemberAssignmentTarget(
            @Nonnull StaticMemberAssignmentTarget node,
            @Nonnull Supplier<State> object);

    @Nonnull
    State reduceStaticMemberExpression(
            @Nonnull StaticMemberExpression node,
            @Nonnull Supplier<State> object);

    @Nonnull
    State reduceStaticPropertyName(@Nonnull StaticPropertyName node);

    @Nonnull
    State reduceSuper(@Nonnull Super node);

    @Nonnull
    State reduceSwitchCase(
            @Nonnull SwitchCase node,
            @Nonnull Supplier<State> test,
            @Nonnull ImmutableList<Supplier<State>> consequent);

    @Nonnull
    State reduceSwitchDefault(
            @Nonnull SwitchDefault node,
            @Nonnull ImmutableList<Supplier<State>> consequent);

    @Nonnull
    State reduceSwitchStatement(
            @Nonnull SwitchStatement node,
            @Nonnull Supplier<State> discriminant,
            @Nonnull ImmutableList<Supplier<State>> cases);

    @Nonnull
    State reduceSwitchStatementWithDefault(
            @Nonnull SwitchStatementWithDefault node,
            @Nonnull Supplier<State> discriminant,
            @Nonnull ImmutableList<Supplier<State>> preDefaultCases,
            @Nonnull Supplier<State> defaultCase,
            @Nonnull ImmutableList<Supplier<State>> postDefaultCases);

    @Nonnull
    State reduceTemplateElement(@Nonnull TemplateElement node);

    @Nonnull
    State reduceTemplateExpression(
            @Nonnull TemplateExpression node,
            @Nonnull Maybe<Supplier<State>> tag,
            @Nonnull ImmutableList<Supplier<State>> elements);

    @Nonnull
    State reduceThisExpression(@Nonnull ThisExpression node);

    @Nonnull
    State reduceThrowStatement(
            @Nonnull ThrowStatement node,
            @Nonnull Supplier<State> expression);

    @Nonnull
    State reduceTryCatchStatement(
            @Nonnull TryCatchStatement node,
            @Nonnull Supplier<State> body,
            @Nonnull Supplier<State> catchClause);

    @Nonnull
    State reduceTryFinallyStatement(
            @Nonnull TryFinallyStatement node,
            @Nonnull Supplier<State> body,
            @Nonnull Maybe<Supplier<State>> catchClause,
            @Nonnull Supplier<State> finalizer);

    @Nonnull
    State reduceUnaryExpression(
            @Nonnull UnaryExpression node,
            @Nonnull Supplier<State> operand);

    @Nonnull
    State reduceUpdateExpression(
            @Nonnull UpdateExpression node,
            @Nonnull Supplier<State> operand);

    @Nonnull
    State reduceVariableDeclaration(
            @Nonnull VariableDeclaration node,
            @Nonnull ImmutableList<Supplier<State>> declarators);

    @Nonnull
    State reduceVariableDeclarationStatement(
            @Nonnull VariableDeclarationStatement node,
            @Nonnull Supplier<State> declaration);

    @Nonnull
    State reduceVariableDeclarator(
            @Nonnull VariableDeclarator node,
            @Nonnull Supplier<State> binding,
            @Nonnull Maybe<Supplier<State>> init);

    @Nonnull
    State reduceWhileStatement(
            @Nonnull WhileStatement node,
            @Nonnull Supplier<State> test,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceWithStatement(
            @Nonnull WithStatement node,
            @Nonnull Supplier<State> object,
            @Nonnull Supplier<State> body);

    @Nonnull
    State reduceYieldExpression(
            @Nonnull YieldExpression node,
            @Nonnull Maybe<Supplier<State>> expression);

    @Nonnull
    State reduceYieldGeneratorExpression(
            @Nonnull YieldGeneratorExpression node,
            @Nonnull Supplier<State> expression);
}
