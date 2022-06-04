package co.thecodest.conversationbuilder.util;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ListUtil {

    private ListUtil() {
    }

    public static <T> Stream<List<T>> batches(List<T> source, int batchLength) {
        if (batchLength <= 0)
            throw new IllegalArgumentException("Batch length less than one: " + batchLength);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / batchLength;
        return IntStream.range(0, fullChunks + 1)
                .mapToObj(n -> source.subList(n * batchLength, n == fullChunks ? size : (n + 1) * batchLength));
    }

}
