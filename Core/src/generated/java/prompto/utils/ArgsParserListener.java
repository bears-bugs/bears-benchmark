// Generated from ArgsParser.g4 by ANTLR 4.7.1
package prompto.utils;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArgsParser}.
 */
public interface ArgsParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArgsParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ArgsParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ArgsParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArgsParser#entry}.
	 * @param ctx the parse tree
	 */
	void enterEntry(ArgsParser.EntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsParser#entry}.
	 * @param ctx the parse tree
	 */
	void exitEntry(ArgsParser.EntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArgsParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(ArgsParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArgsParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(ArgsParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ELEMENT}
	 * labeled alternative in {@link ArgsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterELEMENT(ArgsParser.ELEMENTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ELEMENT}
	 * labeled alternative in {@link ArgsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitELEMENT(ArgsParser.ELEMENTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code STRING}
	 * labeled alternative in {@link ArgsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterSTRING(ArgsParser.STRINGContext ctx);
	/**
	 * Exit a parse tree produced by the {@code STRING}
	 * labeled alternative in {@link ArgsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitSTRING(ArgsParser.STRINGContext ctx);
}