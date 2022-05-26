package co.thecodest.conversationbuilder.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.thecodest.conversationbuilder.util.ListUtil.batches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListUtilTest {

    @Test
    void throwExceptionWhenLengthLessThanOne() {
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            batches(Collections.emptyList(), 0);
        });
        assertThat(exception).hasMessageContaining("Batch length less than one");
    }

    @Test
    void returnsEmptyStreamWhenListEmpty() {
        final Stream<List<Object>> batches = batches(Collections.emptyList(), 1);
        assertThat(batches).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForSuccessfullyReturnsStreamWithBatches")
    void successfullyReturnsStreamWithBatches(List<Integer> source, int batchLength, int expectedNumberOfBatches) {
        final List<List<Integer>> batches = batches(source, batchLength)
                .collect(Collectors.toList());

        assertThat(batches.size()).isEqualTo(expectedNumberOfBatches);

        //all batches except last should be of size equal to batchLength
        for (int i = 0; i < batches.size() - 2; i++) {
            List<Integer> batch = batches.get(i);
            assertThat(batch.size()).isEqualTo(batchLength);
        }

    }

    private static Stream<Arguments> provideArgumentsForSuccessfullyReturnsStreamWithBatches() {
        return Stream.of(
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),1,9),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),2,5),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),3,3),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),4,3),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),5,2),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),6,2),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),7,2),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),8,2),
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9),9,1),
                Arguments.of(List.of(1,2,3,4,5,6,7,8),2,4)
        );
    }


}