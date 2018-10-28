package prompto.problem;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import prompto.error.SyntaxError;
import prompto.parser.ISection;
import prompto.type.IType;

public class ProblemListener implements ANTLRErrorListener, IProblemListener {

	@Override
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
	}
	
	@Override
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
	}
	
	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
	}
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int column, String msg, RecognitionException e) {
		throw e;
	}
	
	@Override
	public void reportDuplicate(String name, ISection section, ISection existing) {
		throw new SyntaxError("Duplicate name: \"" + name + "\"");
	}
	
	@Override
	public void reportIllegalNonBoolean(ISection section, IType type) {
		throw new SyntaxError("Illegal expression type, expected Boolean, got:" + type.getTypeName());
	}
	
	@Override
	public void reportIllegalReturn(ISection section) {
		throw new SyntaxError("Illegal return statement in test method!");
	}
	
	@Override
	public void reportUnknownIdentifier(String name, ISection section) {
		throw new SyntaxError("Unknown identifier: \"" + name + "\"");
	}
	
	@Override
	public void reportAmbiguousIdentifier(String name, ISection section) {
		throw new SyntaxError("Ambiguous identifier: \"" + name + "\"");
	}
	
	@Override
	public void reportUnknownAttribute(String name, ISection section) {
		throw new SyntaxError("Unknown attribute: \"" + name + "\"");
	}
	
	@Override
	public void reportUnknownMethod(String name, ISection section) {
		throw new SyntaxError("Unknown method: \"" + name + "\"");
	}
	
	@Override
	public void reportNoMatchingPrototype(String proto, ISection section) {
		throw new SyntaxError("No matching prototype: \"" + proto + "\"");
	}
	
	@Override
	public void reportIllegalComparison(IType type, IType other, ISection section) {
		throw new SyntaxError("Cannot compare " +type.getTypeName() + " to " + other.getTypeName());
	}
	
	@Override
	public void reportIllegalMember(String name, ISection section) {
		throw new SyntaxError("Cannot read member from " + name);
	}
	
	@Override
	public void reportIllegalOperation(String message, ISection section) {
		throw new SyntaxError(message);
	}
}
