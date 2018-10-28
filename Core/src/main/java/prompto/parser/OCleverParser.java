package prompto.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import prompto.declaration.DeclarationList;
import prompto.problem.IProblemListener;

public class OCleverParser extends OParser implements IParser {

	IProblemListener problemListener;
	String path = "";

	public OCleverParser(String input) {
		this(CharStreams.fromString(input));
	}
	
	public OCleverParser(InputStream input) throws IOException {
		this(CharStreams.fromStream(input));
	}
	
	public OCleverParser(String path, InputStream input) throws IOException {
		this(CharStreams.fromStream(input));
		setPath(path);
	}

	public OCleverParser(CharStream input) {
		this(new ONamingLexer(input));
	}
	
	public OCleverParser(ONamingLexer lexer) {
		this(new CommonTokenStream(lexer));
	}

	public OCleverParser(TokenStream input) {
		super(input);
		this.setErrorHandler(new ErrorStrategy());
	}

	@Override
	public void setProblemListener(IProblemListener problemListener) {
		this.removeErrorListeners();
		if(problemListener!=null)
			this.addErrorListener((ANTLRErrorListener)problemListener);
		getLexer().setProblemListener(problemListener);
		this.problemListener = problemListener;
	}

	@Override
	public ONamingLexer getLexer() {
		return (ONamingLexer)this.getInputStream().getTokenSource();
	}

	public int equalToken() {
		return OParser.EQ;
	};

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public DeclarationList parse(String path, InputStream input) throws Exception {
		this.path = path;
		getLexer().reset(input);
		getInputStream().seek(0);
		return parse_declaration_list();
	}
		
	public DeclarationList parse_declaration_list() throws Exception {
		return this.<DeclarationList>doParse(this::declaration_list);
	}
	
	public <T extends Object> T doParse(Supplier<ParseTree> method) {
		ParseTree tree = method.get();
		OPromptoBuilder builder = new OPromptoBuilder(this);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		return builder.<T>getNodeValue(tree);
	}
}
