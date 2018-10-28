package prompto.declaration;

import java.util.Map;

import prompto.expression.Symbol;
import prompto.grammar.SymbolList;

public interface IEnumeratedDeclaration<T extends Symbol> extends IDeclaration {
	
	SymbolList<T> getSymbolsList(); // as they appear in the declaration
	Map<String, T> getSymbolsMap(); // for fast access
	default T getSymbol(String name) {
		return getSymbolsMap().get(name);
	}
	default boolean hasSymbol(String name) {
		return getSymbolsMap().containsKey(name);
	}
}
