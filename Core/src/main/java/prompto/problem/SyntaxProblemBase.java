package prompto.problem;

import prompto.parser.ISection;


public abstract class SyntaxProblemBase implements IProblem {
	
	ISection section;
	int hashCode = -1;
	
	public SyntaxProblemBase(ISection section) {
		this.section = section;
	}

	@Override
	public String getPath() {
		return section.getFilePath();
	}
	
	@Override
	public int getStartLine() {
		return section.getStart().getLine();
	}
	
	@Override
	public int getStartColumn() {
		return section.getStart().getColumn();
	}

	@Override
	public int getStartIndex() {
		return section.getStart().getIndex();
	}
	
	@Override
	public int getEndIndex() {
		return section.getEnd().getIndex();
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
		if(!(obj instanceof SyntaxProblemBase))
			return false;
		SyntaxProblemBase other = (SyntaxProblemBase)obj;
		return this.getStartIndex()==other.getStartIndex()
				&& this.getMessage().equals(other.getMessage());
	}
	
	@Override
	public String toString() {
		return "{ startIndex:" + getStartIndex() + ", message:" + getMessage() + " }";
	}


}
