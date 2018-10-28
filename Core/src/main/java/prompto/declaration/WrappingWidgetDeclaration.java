package prompto.declaration;

public class WrappingWidgetDeclaration extends ConcreteWidgetDeclaration {

	public WrappingWidgetDeclaration(CategoryDeclaration wrapped) {
		super(wrapped.getId(), wrapped.getDerivedFrom().get(0), wrapped.getLocalMethods());
	}



}
