package com.gdssecurity.pmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;

public class TypeUtils {

	public static final TypeUtils INSTANCE = new TypeUtils();

	private Map<String, Class<?>> hits = new HashMap<>();
	private Set<String> misses = new HashSet<String>();

	private TypeUtils() {
		super();
	}

	public Class<?> getClassForName(String className, ASTName astName) {
		if (StringUtils.isBlank(className)) {
			return null;
		}
		List<String> names = generateClassNames(className, astName);

		for (String name : names) {
			Class<?> retVal = getClassForName(name);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	private List<String> generateClassNames(String className, ASTName astName) {
		List<String> names = new ArrayList<String>();
		if (StringUtils.isBlank(className)) {
			return names;
		}
		names.add(className);
		if (!className.contains(".")) {
			names.add("java.lang." + className);
		}
		if (className.contains(".")) {
			return names;
		}
		List<ASTCompilationUnit> parent = astName.getParentsOfType(ASTCompilationUnit.class);
		if (!parent.isEmpty()) {
			// Imports
			List<ASTImportDeclaration> imports = parent.get(0).findChildrenOfType(ASTImportDeclaration.class);

			for (ASTImportDeclaration imp : imports) {
				ASTName importName = imp.getFirstChildOfType(ASTName.class);
				if (importName != null) {
					String importedPackageName = importName.getImage();
					if (!StringUtils.isBlank(importedPackageName)) {
						if (imp.isImportOnDemand()) {
							names.add(importedPackageName + "." + className);
						}
						else if (importedPackageName.endsWith("." + className)) {
							names.add(importedPackageName);
						}
					}
				}
			}
			
			// package
			List<ASTPackageDeclaration> pacage = parent.get(0).findChildrenOfType(ASTPackageDeclaration.class);
			if (!pacage.isEmpty()) {
				ASTPackageDeclaration dec = pacage.get(0);
				ASTName packageName = dec.getFirstChildOfType(ASTName.class);
				if (packageName != null && !StringUtils.isBlank(packageName.getImage())) {
					names.add(packageName.getImage() + "." + className);
				}
			}
		}
		

		

		return names;
	}

	/**
	 * Finds a class. Uses cache to store misses and hits
	 * 
	 * @param className class name to search for
	 * @return the class
	 */
	public Class<?> getClassForName(String className) {
		if (StringUtils.isBlank(className)) {
			return null;
		}
		if (misses.contains(className)) {
			return null;
		}
		Class<?> cached = hits.get(className);
		if (cached != null) {
			return cached;
		}

		try {
			Class<?> clazz = Class.forName(className, false, this.getClass().getClassLoader());
			hits.put(className, clazz);
			return clazz;

		} catch (NoClassDefFoundError | ExceptionInInitializerError | ClassNotFoundException err) {
			misses.add(className);
			return null;
		}
	}

}
