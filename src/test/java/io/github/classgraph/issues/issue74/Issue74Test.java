package io.github.classgraph.issues.issue74;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class Issue74Test {
    public interface Function {
    }

    public abstract class FunctionAdapter implements Function {
    }

    public class ExtendsFunctionAdapter extends FunctionAdapter {
    }

    public class ImplementsFunction implements Function {
    }

    @Test
    public void issue74() {
        try (ScanResult scanResult = new ClassGraph().whitelistPackages(Issue74Test.class.getPackage().getName())
                .scan()) {
            assertThat(scanResult.getClassesImplementing(Function.class.getName()).getNames())
                    .containsExactlyInAnyOrder(FunctionAdapter.class.getName(), ImplementsFunction.class.getName(),
                            ExtendsFunctionAdapter.class.getName());
        }
    }
}
