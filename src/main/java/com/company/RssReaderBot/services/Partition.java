package com.company.RssReaderBot.services;

import java.util.*;

public class Partition<T> extends AbstractList<List<T>> {

    private final int chunkSize;
    private List<T> list;

    public Partition(List<T> list, int chunkSize) {
        this.list = new ArrayList<>(list);
        this.chunkSize = chunkSize;
    }

    public static <T> Partition<T> toChunk(List<T> list, int chunkSize) {
        return new Partition<>(list, chunkSize);
    }

    @Override
    public List<T> get(int index) {
        int start = index * chunkSize;
        int end = Math.min(start + chunkSize, list.size());
        if (start > end) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of the list range <0," + (size() - 1) + ">"
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

    public static int getCustomSize(int listSize, int chunkSize) {
        return (int) Math.ceil((double) listSize / (double) chunkSize);
    }
}
