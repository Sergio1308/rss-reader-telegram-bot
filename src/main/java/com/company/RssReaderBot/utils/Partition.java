package com.company.RssReaderBot.utils;

import java.util.*;

public class Partition<T> extends AbstractList<List<T>> {

    private final int chunkSize;
    private List<T> list;

    public Partition(List<T> list, int chunkSize) {
        this.list = new ArrayList<>(list);
        this.chunkSize = chunkSize;
    }

    public static <T> Partition<T> toChunk(List<T> list, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("ChunkSize must be greater than 0");
        }
        return new Partition<>(list, chunkSize);
    }

    @Override
    public List<T> get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        int listSize = list.size();
        int start = index * chunkSize;
        int end = Math.min(start + chunkSize, listSize);
        if (start > end) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of the list range <0," + (listSize - 1) + ">"
            );
        }
        return new ArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) chunkSize);
    }

    @Override
    public void clear() {
        list = new ArrayList<>();
    }
}
