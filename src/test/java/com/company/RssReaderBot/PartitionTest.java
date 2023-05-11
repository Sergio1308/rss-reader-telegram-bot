package com.company.RssReaderBot;

import com.company.RssReaderBot.utils.Partition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PartitionTest {
    @Test
    void testPartitionWithValidInput() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Partition<Integer> partition = Partition.toChunk(input, 3);

        assertEquals(3, partition.size());
        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                List.of(7)
        ), partition);
        assertEquals(Arrays.asList(1, 2, 3), partition.get(0));
        assertEquals(Arrays.asList(4, 5, 6), partition.get(1));
    }

    @Test
    void testPartitionWithEmptyList() {
        List<Integer> input = List.of();
        Partition<Integer> partition = Partition.toChunk(input, 2);

        assertEquals(0, partition.size());
    }

    @Test
    void testPartitionWithLargeChunkSize() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Partition<Integer> partition = Partition.toChunk(input, 20);

        assertEquals(1, partition.size());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), partition.get(0));
    }

    @Test
    void testPartitionWithNegativeChunkSize() {
        List<Integer> input = Arrays.asList(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> Partition.toChunk(input, 0));
        assertThrows(IllegalArgumentException.class, () -> Partition.toChunk(input, -1));
    }

    @Test
    void testPartitionGetWithValidIndex() {
        List<Integer> input = Arrays.asList(1, 2, 3);
        Partition<Integer> partition = Partition.toChunk(input, 2);

        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(List.of(3),partition.get(1));
    }

    @Test
    void testPartitionGetWithInvalidIndex() {
        List<Integer> input = Arrays.asList(1, 2, 3);
        Partition<Integer> partition = Partition.toChunk(input, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            List<Integer> getSubList = partition.get(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            List<Integer> getSubList = partition.get(2);
        });
    }

    @Test
    void testClear() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        Partition<Integer> partition = Partition.toChunk(input, 2);

        assertEquals(6, partition.size());
        partition.clear();
        assertEquals(0, partition.size());
    }
}
