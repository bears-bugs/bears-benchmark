package org.imdea.vcd.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Based on https://gist.github.com/jaycobbcruz/cbabc1eb49f51bfe2ed1db10a06a2b26
 *
 * @author Vitor Enes
 */
public class Permutations {

    public static <T> List<List<T>> of(final List<T> items) {
        List<List<T>> result = new ArrayList<>();

        for (int i = 0; i < factorial(items.size()); i++) {
            result.add(permutation(i, items));
        }

        return result;
    }

    private static int factorial(final int num) {
        return IntStream.rangeClosed(2, num).reduce(1, (x, y) -> x * y);
    }

    private static <T> List<T> permutation(final int count, final LinkedList<T> input, final List<T> output) {
        if (input.isEmpty()) {
            return output;
        }

        final int factorial = factorial(input.size() - 1);
        output.add(input.remove(count / factorial));
        return permutation(count % factorial, input, output);
    }

    private static <T> List<T> permutation(final int count, final List<T> items) {
        return permutation(count, new LinkedList<>(items), new ArrayList<>());
    }
}
