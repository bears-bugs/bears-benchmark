package prompto.utils;

import prompto.grammar.Identifier;
import prompto.parser.Dialect;
import prompto.runtime.Context;
import prompto.type.CategoryType;

public class CodeWriter {
	
	class Indenter {
		
		public String indents = "";
		public boolean isStartOfLine = true;
		
		public void appendTabsIfRequired(String s) {
			appendTabsIfRequired(s.charAt(s.length()-1));
		}

		public void appendTabsIfRequired(char c) {
			if(isStartOfLine)
				sb.append(indents);
			isStartOfLine = c=='\n';
		}
		
		public void indent() {
			indents += '\t';
		}

		public void dedent() {
			if(indents.isEmpty())
				throw new RuntimeException("Illegal dedent!");
			indents = indents.substring(1);
		}
	}
	
	Dialect dialect;
	Context context;
	StringBuilder sb;
	Indenter indenter;
	
	public CodeWriter(Dialect dialect, Context context) {
		this.dialect = dialect;
		this.context = context;
		this.sb = new StringBuilder();
		indenter = new Indenter();
	}
	
	public CodeWriter(Dialect dialect, Context context, StringBuilder sb, Indenter indenter) {
		this.dialect = dialect;
		this.context = context;
		this.sb = sb;
		this.indenter = indenter;
	}

	public Context getContext() {
		return context;
	}
	
	public CodeWriter append(Identifier id) {
		append(id.toString());
		return this;
	}
	
	public CodeWriter append(String s) {
		indenter.appendTabsIfRequired(s);
		sb.append(s);
		return this;
	}

	public CodeWriter appendRaw(String s) {
		sb.append(s);
		return this;
	}

	public CodeWriter append(char c) {
		indenter.appendTabsIfRequired(c);
		sb.append(c);
		return this;
	}

	public CodeWriter trimLast(int count) {
		sb.setLength(sb.length()-count);
		return this;
	}

	public CodeWriter indent() {
		indenter.indent();
		return this;
	}

	public CodeWriter dedent() {
		indenter.dedent();
		return this;
	}

	public CodeWriter newLine() {
		append('\n');
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	public Dialect getDialect() {
		return dialect;
	}


	public boolean isGlobalContext() {
		return context.isGlobalContext();
	}

	public CodeWriter newLocalWriter() {
		return new CodeWriter(dialect, context.newLocalContext(), sb, indenter);
	}

	public CodeWriter newChildWriter() {
		return new CodeWriter(dialect, context.newChildContext(), sb, indenter);
	}

	public CodeWriter newMemberWriter() {
		Context context = this.context.newLocalContext();
		context.setParentContext(this.context);
		return new CodeWriter(dialect, context, sb, indenter);
	}

	public CodeWriter newInstanceWriter(CategoryType type) {
		return new CodeWriter(dialect, context.newInstanceContext(type, false), sb, indenter);
	}


}
