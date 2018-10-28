package prompto.grammar;

import java.util.Collection;
import java.util.LinkedList;

import prompto.argument.AttributeArgument;
import prompto.argument.IArgument;
import prompto.declaration.IMethodDeclaration;
import prompto.error.SyntaxError;
import prompto.expression.AndExpression;
import prompto.expression.IExpression;
import prompto.expression.UnresolvedIdentifier;
import prompto.runtime.Context;
import prompto.transpiler.Transpiler;
import prompto.utils.CodeWriter;
import prompto.value.ContextualExpression;


public class ArgumentAssignmentList extends LinkedList<ArgumentAssignment> {

	private static final long serialVersionUID = 1L;

	public ArgumentAssignmentList() {
		
	}
	
	public ArgumentAssignmentList(Collection<ArgumentAssignment> assignments) {
		super(assignments);
	}

	/* post-fix expression priority for final assignment in E dialect */
	/* 'xyz with a and b as c' should read 'xyz with a, b as c' NOT 'xyz with (a and b) as c' */
	public void checkLastAnd() {
		ArgumentAssignment assignment = this.getLast();
		if(assignment!=null && assignment.getArgument()!=null && assignment.getExpression() instanceof AndExpression) {
			AndExpression and = (AndExpression)assignment.getExpression();
			if(and.getLeft() instanceof UnresolvedIdentifier) {
				Identifier id = ((UnresolvedIdentifier)and.getLeft()).getId();
				if(Character.isLowerCase(id.toString().charAt(0))) {
					this.removeLast();
					// add AttributeArgument
					AttributeArgument argument = new AttributeArgument(id);
					ArgumentAssignment attribute = new ArgumentAssignment(argument, null);
					this.add(attribute);
					// fix last assignment
					assignment.setExpression(and.getRight());
					this.add(assignment);
				}
			}
		}
	}
	
	public int findIndex(Identifier name) {
		for(int i=0;i<this.size();i++) {
			if(name.equals(this.get(i).getArgumentId())) {
				return i;
			}
		}
		return -1;
	}

	public ArgumentAssignment find(Identifier name) {
		for(ArgumentAssignment assignment : this) {
			if(name.equals(assignment.getArgumentId()))
				return assignment;
		}
		return null;
	}

	public ArgumentAssignmentList resolveAndCheck(Context context, IMethodDeclaration declaration) {
		ArgumentAssignmentList assignments = new ArgumentAssignmentList();
		for(ArgumentAssignment assignment : this)
			assignments.add(assignment.resolveAndCheck(context, declaration.getArguments()));
		return assignments;
	}
	
	public void toDialect(CodeWriter writer) {
		switch(writer.getDialect()) {
		case E:
			toEDialect(writer);
			break;
		case O:
			toODialect(writer);
			break;
		case M:
			toMDialect(writer);
			break;
		}
	}
	
	private void toEDialect(CodeWriter writer) {
		int idx = 0;
		// anonymous argument before 'with'
		if(this.size()>0 && this.get(0).getArgument()==null) {
			writer.append(' ');
			this.get(idx++).toDialect(writer);
		}
		if(idx<this.size()) {
			writer.append(" with ");
			this.get(idx++).toDialect(writer);
			writer.append(", ");
			while(idx<this.size()-1) {
				this.get(idx++).toDialect(writer);
				writer.append(", ");
			}
			writer.trimLast(2);
			if(idx<this.size()) {
				writer.append(" and ");				
				this.get(idx++).toDialect(writer);
			}
		}
	}
	
	private void toODialect(CodeWriter writer) {
		writer.append("(");
		for(ArgumentAssignment as : this) {
			as.toDialect(writer);
			writer.append(", ");
		}
		if(this.size()>0)
			writer.trimLast(2);
		writer.append(")");
	}

	private void toMDialect(CodeWriter writer) {
		toODialect(writer);
	}

	public void declare(Transpiler transpiler) {
		for(ArgumentAssignment as : this) {
			as.declare(transpiler);
		}
		
	}

	public ArgumentAssignmentList makeAssignments(Context context, IMethodDeclaration declaration) {
		ArgumentAssignmentList local = new ArgumentAssignmentList(this);
		ArgumentAssignmentList assignments = new ArgumentAssignmentList();
		for(int i=0;i<declaration.getArguments().size();i++) {
		    IArgument argument = declaration.getArguments().get(i);
		    ArgumentAssignment assignment = null;
	        int index = local.findIndex(argument.getId());
		    if(index<0 && i==0 && this.size()>0 && this.get(0).getArgument()==null)
		        index = 0;
		    if(index>=0) {
	            assignment = local.get(index);
	            local.remove(index);
	        }
	        if(assignment==null) {
	            if (argument.getDefaultExpression() != null)
	                assignments.add(new ArgumentAssignment(argument, argument.getDefaultExpression()));
	            else
	                throw new SyntaxError("Missing argument:" + argument.getName());
	        } else {
	            IExpression expression = new ContextualExpression(context, assignment.getExpression());
	            assignments.add(new ArgumentAssignment(argument, expression));
	        }
	    }
	    if(local.size() > 0)
	        throw new SyntaxError("Method has no argument:" + local.get(0).getArgument().getName());
		return assignments;
	}

}
