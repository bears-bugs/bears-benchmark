package prompto.libraries;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import prompto.utils.ResourceUtils;

public abstract class Libraries {

	public static Collection<URL> getPromptoLibraries(Class<?> ... klassesInJar) {
		if(klassesInJar.length==0)
			throw new RuntimeException("No Prompto libraries to bootstrap from!");
		return Stream.of(klassesInJar)
				.map(Libraries::getPromptoLibraries)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
	
	public static Collection<URL> getPromptoLibraries(Class<?> klassInJar) {
		try {
			String thisClassName = klassInJar.getName().replace('.', '/') + ".class";
			URL thisResourceUrl = klassInJar.getClassLoader().getResource(thisClassName);
			String thisResourceName = thisResourceUrl.toExternalForm();
			URL parentUrl = new URL(thisResourceName.substring(0, thisResourceName.indexOf(thisClassName)));
			URL url = new URL(parentUrl.toExternalForm() + "libraries/");
			return ResourceUtils.listResourcesAt(url, ResourceUtils::isPromptoLibrary);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
