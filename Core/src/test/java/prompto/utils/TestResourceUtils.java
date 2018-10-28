package prompto.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.junit.Test;

public class TestResourceUtils {

	@Test
	public void testListResourcesAtRoot() throws IOException {
		Collection<URL> names = ResourceUtils.listResourcesAt("/", null);
		assertNotNull(names);
		assertFalse(names.isEmpty());
	}

	@Test
	public void testListResourcesAtFolder() throws IOException {
		Collection<URL> names = ResourceUtils.listResourcesAt("prompto/", null);
		assertNotNull(names);
		assertFalse(names.isEmpty());
	}
	
	@Test
	public void testListResourcesAtJarFolder() throws IOException {
		Collection<URL> names = ResourceUtils.listResourcesAt("org/antlr/v4/runtime/", null);
		assertNotNull(names);
		assertFalse(names.isEmpty());
	}


}
