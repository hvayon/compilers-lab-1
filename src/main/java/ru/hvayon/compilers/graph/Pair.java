package ru.hvayon.compilers.graph;

import java.util.ArrayList;

public class Pair {
    ArrayList<Integer> resIntList;
    ArrayList<Character> resCharList;
    boolean isEnd = false;

    public Pair() {
        resIntList = new ArrayList<>();
        resCharList = new ArrayList<>();
    }

    public Pair(ArrayList<Integer> resIntList, ArrayList<Character> resCharList) {
        this.resIntList = resIntList;
        this.resCharList = resCharList;
    }

    public ArrayList<Integer> getResIntList() {
        return resIntList;
    }

    public void setResIntList(ArrayList<Integer> resIntList) {
        this.resIntList = resIntList;
    }

    public ArrayList<Character> getResCharList() {
        return resCharList;
    }

    public void setResCharList(ArrayList<Character> resCharList) {
        this.resCharList = resCharList;
    }
}
