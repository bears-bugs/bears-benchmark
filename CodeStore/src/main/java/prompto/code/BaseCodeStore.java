package prompto.code;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import prompto.declaration.AttributeDeclaration;
import prompto.declaration.IDeclaration;
import prompto.error.PromptoError;
import prompto.intrinsic.PromptoVersion;
import prompto.parser.ISection;

/* a code store which simply links to another one */
/* enables multiple code store implementations (resource, classpath, store...) */
/* also enables versioning, by having the latest version as root */
public abstract class BaseCodeStore implements ICodeStore {

	ICodeStore next;
	
	protected BaseCodeStore(ICodeStore next) {
		this.next = next;
	}
	
	public ICodeStore getNext() {
		return next;
	}
	
	public void setNext(ICodeStore next) {
		this.next = next;
	}
	
	@Override
	public ISection findSection(ISection section) { 
		return next==null ? null : next.findSection(section);
	}
	
	@Override
	public Collection<String> fetchDeclarationNames() {
		return next==null ? Collections.emptyList() : next.fetchDeclarationNames();
	}
	
	@Override
	public Iterable<IDeclaration> fetchLatestDeclarations(String name) throws PromptoError {
		Iterable<IDeclaration> decls = ICodeStore.super.fetchLatestDeclarations(name);
		return decls != null ? decls : (next==null ? null : next.fetchLatestDeclarations(name));
	}
	
	@Override
	public Iterable<IDeclaration> fetchSpecificDeclarations(String name, PromptoVersion version) throws PromptoError {
		return next==null ? null : next.fetchSpecificDeclarations(name, version);
	}
	
	@Override
	public Resource fetchLatestResource(String path) throws PromptoError {
		Resource res = ICodeStore.super.fetchLatestResource(path);
		return res != null ? res : (next==null ? null : next.fetchLatestResource(path));
	}
	
	@Override
	public Resource fetchSpecificResource(String path, PromptoVersion version) {
		return next==null ? null : next.fetchSpecificResource(path, version);
	}
	
	@Override
	public IDeclaration fetchLatestSymbol(String name) throws PromptoError {
		IDeclaration decl = ICodeStore.super.fetchLatestSymbol(name);
		return decl != null ? decl : (next==null ? null : next.fetchLatestSymbol(name));
	}
	
	@Override
	public IDeclaration fetchSpecificSymbol(String name, PromptoVersion version) throws PromptoError {
		return next==null ? null : next.fetchSpecificSymbol(name, version);
	}

	@Override
	public void collectStorableAttributes(Map<String, AttributeDeclaration> columns) throws PromptoError {
		if(next!=null)
			next.collectStorableAttributes(columns);
		
	}

}
