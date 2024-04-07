package ru.hvayon.compilers.graph;

import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Data
public class Graph {

    int nodes;
    /* индекс начальной вершины */
    int mBeginVertex;

    /* индекс конечной вершины */
    int mEndVertex;
    ArrayList<Integer> mBeginVertexes;
    ArrayList<Integer> mEndVertexes;
    ArrayList<ArrayList<Integer>> mGraph;
    ArrayList<ArrayList<Character>> mRules;
    // индексы вершин с конечными состояниями
    ArrayList<ArrayList<Integer>> mUnfinishedVertexes;
    HashMap<Character,Graph> graphOperators;

    public Graph() {
        mGraph = new ArrayList<>();
        mRules = new ArrayList<>();
        mUnfinishedVertexes = new ArrayList<>();
        mBeginVertexes = new ArrayList<>();
        mEndVertexes = new ArrayList<>();
    }

//    public Character getNextStateSymbol()
//        return false;
//    }
//    private static Set<Set<State>> splitStates(Set<State> states) {
//        Set<State> newStates = new HashSet<>();
//        for (String c : alphabet) {
//            for (State s : states) {
//                // найти состояние, в которое мы перейдем из s по символу c
//                // если оно находится в данной группе, то добавляем его в новый набор
//                if (states.contains(s.getNextStateBySymbol(c))) {
//                    newStates.add(s);
//                }
//            }
//        }
//        // удаляем все элементы из набора states, которые есть в newStates
//        states.removeAll(newStates);
//        return Stream.of(states, newStates).collect(Collectors.toCollection(HashSet::new));
//    }
    public Graph insertGraph(Graph rhs) {
        assert(mUnfinishedVertexes.size() >= 1);
        assert(rhs.mUnfinishedVertexes.size() == 0);

        int vertexesCount = mGraph.size();
        rhs.mBeginVertex += vertexesCount;
        rhs.mEndVertex += vertexesCount;
        for (List<Integer> list : rhs.mGraph) {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, list.get(i) + vertexesCount);
            }
        }

        mGraph.addAll(rhs.mGraph);
        mRules.addAll(rhs.mRules);

        // mUnfinishedVertexes[][]
        mGraph.get(mUnfinishedVertexes.get(mUnfinishedVertexes.size() - 1).get(0)).add(rhs.mBeginVertex);
        mRules.get(mUnfinishedVertexes.get(mUnfinishedVertexes.size() - 1).get(0)).add('E');
        mGraph.get(rhs.mEndVertex).add(mUnfinishedVertexes.get(mUnfinishedVertexes.size() - 1).get(1));
        mRules.get(rhs.mEndVertex).add('E');

        mUnfinishedVertexes.remove(mUnfinishedVertexes.get(mUnfinishedVertexes.size() - 1));

        return this;
    }

    public Pair findEdges(int vertexElem, List<Integer> vertexesInd ) {

        Pair res = new Pair();

        // ребра графа
        ArrayList<Integer> edges = mGraph.get(vertexElem);

        for (int i = 0; i < edges.size(); i++) {
            // если не равно E
            if (!mRules.get(vertexElem).get(i).equals('E')) {
                res.getResIntList().add(vertexesInd.indexOf(edges.get(i)));
                res.getResCharList().add(mRules.get(vertexElem).get(i));
            } else {
                Pair tmp = findEdges(edges.get(i), vertexesInd);
                res.isEnd = tmp.isEnd;
                res.getResIntList().addAll(tmp.resIntList);
                res.getResCharList().addAll(tmp.resCharList);
                if (edges.get(i).equals(mEndVertex)) {
                    res.isEnd = true;
                }
            }
        }
        return res;
    }

    public Graph deleteEps() {
        Graph graph = new Graph();

        List<Integer> vertexesInd = new ArrayList<>();
        {
            // находим все вершины, у которых хотя бы одно не E ребро
            vertexesInd.add(mBeginVertex);
            for (int i = 0; i < mGraph.size(); i++) {
                for (int j = 0; j < mGraph.get(i).size(); j++) {
                    if (mRules.get(i).get(j) != 'E') {
                        vertexesInd.add(mGraph.get(i).get(j));
                    }
                }
            }
        }

        Function<Integer, Integer> indexTrasform = (oldIdx) -> vertexesInd.indexOf(oldIdx);

        for (int vertexElem : vertexesInd) {
            // для каждой из вершин находим все новые рёбра
            Pair newEdges = findEdges(vertexElem, vertexesInd);
            graph.mGraph.add(newEdges.getResIntList());
            graph.mRules.add(newEdges.getResCharList());
            graph.mBeginVertex = indexTrasform.apply(mBeginVertex);
            if (newEdges.isEnd) {
                graph.mEndVertexes.add(vertexesInd.indexOf(vertexElem));
            }
        }
        return graph;
    }

    public Graph Determine() {

        Set<Character> alphabet = new HashSet<>();
        for (List<Character> row : mRules) {
            for (Character elem : row) {
                alphabet.add(elem);
            }
        }

        if (mBeginVertexes.isEmpty()) {
            mBeginVertexes = new ArrayList<>();
            mBeginVertexes.add(mBeginVertex);
        }

        ArrayList<Set<Integer>> vertexesUnions = new ArrayList<>();
        vertexesUnions.add(new HashSet(mBeginVertexes));

        ArrayList<ArrayList<Integer>> newGraph = new ArrayList<>();
        newGraph.add(new ArrayList<>());

        ArrayList<ArrayList<Character>> newRules = new ArrayList<>();
        newRules.add(new ArrayList<>());

        boolean end = false;
        int vertexesUnionsIdx = 0;
        while (vertexesUnionsIdx < vertexesUnions.size()) {
            end = true;

            List<Set<Integer>> newVertexesUnions = new ArrayList<>();


            for (char symbol : alphabet) {
                Set<Integer> newVertexesUnion = new HashSet<>();
                for (int vertex : vertexesUnions.get(vertexesUnionsIdx)) {


                    for (int i = 0; i < mRules.get(vertex).size(); i++) {
                        if (mRules.get(vertex).get(i) == symbol) {
                            newVertexesUnion.add(mGraph.get(vertex).get(i));
                        }
                    }


                }

                if (!newVertexesUnion.isEmpty()) {
                    int size1;
                    int size2;

                    // возвращаем итератор элемента newVertexesUnion в vertexesUnions
                    int iter1 = vertexesUnions.indexOf(newVertexesUnion);
                    // если найден
                    if (iter1 != -1) {
                        newGraph.get(vertexesUnionsIdx).add(iter1);
                        newRules.get(vertexesUnionsIdx).add(symbol);
                    } else {
                        int iter2 = newVertexesUnions.indexOf(newVertexesUnion);
                        newGraph.get(vertexesUnionsIdx).add(
                                vertexesUnions.size()
                                        + ((iter2 == -1) ? newVertexesUnions.size() : iter2));
                        newRules.get(vertexesUnionsIdx).add(symbol);
                        if (iter2 == -1) {
                            newVertexesUnions.add(newVertexesUnion);
                        }
                        end = false;
                    }
                }
            }
            // добавляем новые элементы в конец массива
            vertexesUnions.addAll(newVertexesUnions);
            newGraph.add(new ArrayList<>());
            newRules.add(new ArrayList<>());
            ++vertexesUnionsIdx;
        }

        Graph res = new Graph();
        res.mBeginVertexes = new ArrayList<>();
        res.mBeginVertexes.add(0);
        res.mBeginVertex = 0;

        res.mGraph = newGraph;
        res.mRules = newRules;
        for (int i = 0; i < vertexesUnions.size(); i++) {
            for (int vertex : vertexesUnions.get(i)) {
                if (mEndVertexes.contains(vertex)) {
                    res.mEndVertexes.add(i);
                }
            }
        }
        return res;
    }

    public boolean checkIfStringRegex(String str) {
            int curState = this.mBeginVertex;
            for (int i = 0; i < str.length(); i++) {
                Character symbol = str.charAt(i);
                if (this.mRules.get(curState).indexOf(symbol) == -1) {
                    return false;
                }
                curState = this.mGraph.get(curState).get(this.mRules.get(curState).indexOf(symbol));
            }
            return this.mEndVertexes.contains(curState);
    }

    public String toDot() {
        String res = "";
        try {
            File file = new File("src/main/resources/graph.dot");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            System.out.println("digraph G {");
            bw.write("digraph G {\n");
            res = "digraph G {\n";
            for (int i = 0; i < mGraph.size(); i++) {
                for (int j = 0; j < mGraph.get(i).size(); j++) {
                    System.out.print(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n");
                    bw.write(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n");
                    res += i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n";
                }
            }
            System.out.println("}");
            bw.write("}");
            res += "}";

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}