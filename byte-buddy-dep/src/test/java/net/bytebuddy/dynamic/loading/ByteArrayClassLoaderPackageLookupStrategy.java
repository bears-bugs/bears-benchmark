package net.bytebuddy.dynamic.loading;

import net.bytebuddy.test.utility.ClassFileExtraction;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ByteArrayClassLoaderPackageLookupStrategy {

    @Test
    public void testGetPackage() throws Exception {
        ByteArrayClassLoader byteArrayClassLoader = new ByteArrayClassLoader(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassFileExtraction.of(Foo.class));
        byteArrayClassLoader.loadClass(Foo.class.getName());
        assertThat(ByteArrayClassLoader.PackageLookupStrategy.ForLegacyVm.INSTANCE.apply(byteArrayClassLoader, Foo.class.getPackage().getName()).getName(),
                is(Foo.class.getPackage().getName()));
    }

    private static class Foo {
        /* empty */
    }
}
