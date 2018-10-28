package prompto.problem;

import prompto.parser.ISection;
import prompto.type.IType;

public interface IProblemListener {

	default boolean isCheckNative() { return true; };
	void reportDuplicate(String name, ISection section, ISection existing);
	void reportIllegalReturn(ISection section);
	void reportIllegalNonBoolean(ISection section, IType type);
	void reportUnknownIdentifier(String name, ISection section);
	void reportUnknownAttribute(String name, ISection section);
	void reportUnknownMethod(String name, ISection section);
	void reportNoMatchingPrototype(String proto, ISection section);
	void reportIllegalComparison(IType type, IType other, ISection section);
	void reportIllegalMember(String name, ISection section);
	void reportIllegalOperation(String message, ISection section);
	void reportAmbiguousIdentifier(String name, ISection section);

}
