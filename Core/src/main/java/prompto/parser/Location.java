package prompto.parser;

import org.antlr.v4.runtime.Token;


public class Location implements ILocation {
	int index;
	int line;
	int column;
	
	public Location() {
	}
	
	public Location(Token token) {
		this(token, false);
	}
	
	public Location(Token token, boolean isEnd) {
		this.index = token.getStartIndex();
		this.line = token.getLine();
		this.column = token.getCharPositionInLine();
		if(isEnd && token.getText()!=null) {
			this.index += token.getText().length();
			this.column += token.getText().length();
		}
	}
	
	public Location(ILocation location) {
		if(location==null)
			location = null;
		this.index = location.getIndex();
		this.line = location.getLine();
		this.column = location.getColumn();
	}

	public Location(int index, int line, int column) {
		this.index = index;
		this.line = line;
		this.column = column;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	@Override
	public int getColumn() {
		return column;
	}

	public boolean isNotAfter(ILocation other) {
		return this.line<other.getLine()
				|| (this.line==other.getLine() && this.column<=other.getColumn());
	}
	
	public boolean isNotBefore(ILocation other) {
		return this.line>other.getLine()
				|| (this.line==other.getLine() && this.column>=other.getColumn());
	}
}
