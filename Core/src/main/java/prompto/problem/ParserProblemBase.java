package prompto.problem;



public abstract class ParserProblemBase implements IProblem {
	
	String path;
	int line;
	int column;
	int hashCode = -1;
	
	public ParserProblemBase(String path, int line, int column) {
		this.path = path;
		this.line = line;
		this.column = column;
	}

	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public int getStartLine() {
		return line;
	}
	
	@Override
	public int getStartColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		if(hashCode==-1)
			hashCode = (String.valueOf(getStartIndex()) + "/" + getMessage()).hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(!(obj instanceof ParserProblemBase))
			return false;
		ParserProblemBase other = (ParserProblemBase)obj;
		return this.getStartIndex()==other.getStartIndex()
				&& this.getMessage().equals(other.getMessage());
	}
	
	@Override
	public String toString() {
		return "{ startIndex:" + getStartIndex() + ", message:" + getMessage() + " }";
	}


}
