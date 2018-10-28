package prompto.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;

import prompto.problem.IProblemListener;

public class EIndentingLexer extends ELexer implements ILexer {

	IProblemListener problemListener;
	List<Token> tokens = new LinkedList<Token>();
	Stack<Integer> indents = new Stack<Integer>();
	boolean wasLF = false;
	boolean addLF = true;
	
    public EIndentingLexer(CharStream input) {
    	super(input);
    	indents.push(0);
    }
    
	@Override
	public void setProblemListener(IProblemListener problemListener) {
		this.removeErrorListeners();
		if(problemListener!=null)
			this.addErrorListener((ANTLRErrorListener)problemListener);
		this.problemListener = problemListener;
	}

	@Override
    public void reset(InputStream input) throws IOException {
    	setInputStream(CharStreams.fromStream(input));
    	tokens = new LinkedList<Token>();
    	indents = new Stack<Integer>();
    	wasLF = false;
    	// let user reset addLF
    	indents.push(0);
    }
    
    public void setAddLF(boolean addLF) {
		this.addLF = addLF;
	}
    
	@Override
	public Dialect getDialect() {
		return Dialect.E;
	}

	@Override
	public Token nextToken() {
		Token t = getNextToken();
		wasLF = t.getType()==LF;
		return t;
	}

	private Token getNextToken() {
		if(tokens.size()>0)
			return tokens.remove(0);
		interpret(super.nextToken());
		return nextToken();
	}
	
	void interpret(Token token) {
		switch(token.getType()) {
		case EOF:
			interpretEOF(token);
			break;
		case LF_TAB:
			interpretLFTAB(token);
			break;			
		default:
			interpretAnyToken(token);
		}
	}

	void interpretEOF(Token eof) {
		// gracefully handle missing dedents
		while(indents.size()>1) {
			tokens.add(deriveToken(eof, DEDENT));
			tokens.add(deriveToken(eof, LF));
			wasLF = true;
			indents.pop();
		}
		// gracefully handle missing lf
		if(!wasLF && addLF)
			tokens.add(deriveToken(eof, LF));
		tokens.add(eof);
	}
  
	void interpretLFTAB(Token lftab) {
		// count TABs following LF
		int indentCount = countIndents(lftab.getText());
		Token next = super.nextToken();
		// if this was an empty line, simply skip it
		if(next.getType()==EOF || next.getType()==LF_TAB) {
			tokens.add(deriveToken(lftab, LF));
			interpret(next);
		} else if(indentCount==indents.peek()) {
			tokens.add(deriveToken(lftab, LF));
			interpret(next);
		} else if(indentCount>indents.peek()) {
			tokens.add(deriveToken(lftab, LF));
			tokens.add(deriveToken(lftab, INDENT));
			indents.push(indentCount);
			interpret(next);
		} else {
			while(indents.size()>1 && indentCount<indents.peek()) {
				tokens.add(deriveToken(lftab, DEDENT));
				tokens.add(deriveToken(lftab, LF));
				indents.pop();
			}
			if(indentCount>indents.peek())
				; // TODO, fire an error through token
			interpret(next);
		}
			
	}
  
	private CommonToken deriveToken(Token token, int type) {
		CommonToken res = new CommonToken(token);
		res.setType(type);
		return res;
	}

	private int countIndents(String text) {
		int count = 0;
		for(char c : text.toCharArray()) switch(c) {
		case ' ':
			count += 1;
			break;
		case '\t':
			count += 4;
			break;
		}
		return count/4;
	}

	void interpretAnyToken(Token token) {
		tokens.add(token);
	}


}
