package com.chickasaw.text2braille;
public class Pair implements Comparable<Pair> {
    public final int index;
    public final int value;

    public Pair(int index, int value) {
        this.index = index;
        this.value = value;
    }
    public int getVal() {
        return this.value;
    }
    public int getInd() {
        return this.index;
    }

    @Override
    public int compareTo(Pair other) {
        return -1 * Integer.valueOf(this.value).compareTo(other.value);
    }
}