package ru.hvayon.compilers.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Graph {

    int nodes;

    /* индекс начальной вершины */
    int mBeginVertex;

    /* индекс конечной вершины */
    int mEndVertex;

    ArrayList<Object> mBeginVertexes;
    ArrayList<Integer> mEndVertexes;


    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public void setmBeginVertex(int mBeginVertex) {
        this.mBeginVertex = mBeginVertex;
    }

    public void setmEndVertex(int mEndVertex) {
        this.mEndVertex = mEndVertex;
    }

    public void setAdjlist(List<List<Integer>> adjlist) {
        this.adjlist = adjlist;
    }

    public void setmGraph(ArrayList<ArrayList<Integer>> mGraph) {
        this.mGraph = mGraph;
    }

    public void setmRules(ArrayList<ArrayList<Character>> mRules) {
        this.mRules = mRules;
    }

    public void setmUnfinishedVertexes(ArrayList<ArrayList<Integer>> mUnfinishedVertexes) {
        this.mUnfinishedVertexes = mUnfinishedVertexes;
    }

    public void setGraphOperators(HashMap<Character, Graph> graphOperators) {
        this.graphOperators = graphOperators;
    }

    public int getNodes() {
        return nodes;
    }

    public int getmBeginVertex() {
        return mBeginVertex;
    }

    public int getmEndVertex() {
        return mEndVertex;
    }

    public List<List<Integer>> getAdjlist() {
        return adjlist;
    }

    public ArrayList<ArrayList<Integer>> getmGraph() {
        return mGraph;
    }

    public ArrayList<ArrayList<Character>> getmRules() {
        return mRules;
    }

    public ArrayList<ArrayList<Integer>> getmUnfinishedVertexes() {
        return mUnfinishedVertexes;
    }

    public HashMap<Character, Graph> getGraphOperators() {
        return graphOperators;
    }

    List<List<Integer>> adjlist;

    ArrayList<ArrayList<Integer>> mGraph;
    ArrayList<ArrayList<Character>> mRules;

    ArrayList<ArrayList<Integer>> mUnfinishedVertexes;

    HashMap<Character,Graph> graphOperators;

    public Graph() {
        mGraph = new ArrayList<>();
        mRules = new ArrayList<>();
        mUnfinishedVertexes = new ArrayList<>();
        mBeginVertexes = new ArrayList<>();
        mEndVertexes = new ArrayList<>();
    }

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
                if (edges.get(i).equals(mEndVertex)) {
                    this.mEndVertexes.add(vertexesInd.indexOf(vertexElem));
                }
                Pair tmp = findEdges(edges.get(i), vertexesInd);
                res.getResIntList().addAll(tmp.resIntList);
                res.getResCharList().addAll(tmp.resCharList);
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
        }
        return graph;
    }

    public Graph Determine() {
        List<Character> alphabet = new ArrayList<>();
        for (List<Character> row : mRules) {
            for (Character elem : row) {
                alphabet.add(elem);
            }
        }

        if (mBeginVertexes.isEmpty()) {
            mBeginVertexes = new ArrayList<>();
            mBeginVertexes.add(mBeginVertex);
        }

        ArrayList<ArrayList<Integer>> vertexesUnions = new ArrayList<>();
        vertexesUnions.add(new ArrayList(mBeginVertexes));
        ArrayList<ArrayList<Integer>> newGraph = new ArrayList<>();
        newGraph.add(new ArrayList<>());
        ArrayList<ArrayList<Character>> newRules = new ArrayList<>();
        newRules.add(new ArrayList<>());

        boolean end = false;
        int vertexesUnionsIdx = 0;
        while (vertexesUnionsIdx < vertexesUnions.size()) {
            end = true;

            List<ArrayList<Integer>> newVertexesUnions = new ArrayList<>();
            for (int vertex : vertexesUnions.get(vertexesUnionsIdx)) {
                for (char symbol : alphabet) {
                    ArrayList<Integer> newVertexesUnion = new ArrayList<>();

                    for (int i = 0; i < mRules.get(vertex).size(); i++) {
                        if (mRules.get(vertex).get(i) == symbol) {
                            newVertexesUnion.add(mGraph.get(vertex).get(i));
                        }
                    }

                    if (!newVertexesUnion.isEmpty()) {
                        int idx1 = -1;
                        int idx2 = -1;

                        for (int j = 0; j < vertexesUnions.size(); j++) {
                            if (vertexesUnions.get(j).equals(newVertexesUnion)) {
                                idx1 = j;
                                break;
                            }
                        }

                        for (int k = 0; k < newVertexesUnions.size(); k++) {
                            if (newVertexesUnions.get(k).equals(newVertexesUnion)) {
                                idx2 = k;
                                break;
                            }
                        }

                        if (idx1 != -1) {
                            newGraph.get(vertexesUnionsIdx).add(idx1);
                            newRules.get(vertexesUnionsIdx).add(symbol);
                        } else {
                            if (idx2 == -1) {
                                newVertexesUnions.add(newVertexesUnion);
                            }
                            newGraph.get(vertexesUnionsIdx).add(idx1 + idx2);
                            newRules.get(vertexesUnionsIdx).add(symbol);
                            end = false;
                        }
                    }
                }
            }
            vertexesUnions.addAll(newVertexesUnions);
            newGraph.add(new ArrayList<>());
            newRules.add(new ArrayList<>());
            vertexesUnionsIdx++;
        }

        Graph res = new Graph();
        res.mBeginVertexes = new ArrayList<>();
        res.mBeginVertexes.add(0);
        res.mGraph = newGraph;
        res.mRules = newRules;
        for (int i = 0; i < vertexesUnions.size(); i++) {
            for (int vertex : vertexesUnions.get(i)) {
                if (mEndVertexes.contains(vertex)) {
                    res.mEndVertexes.add(i);
                }
            }
        }

        return this;
    }

    public void toDot() {
        try {
            File file = new File("src/main/resources/graph.dot");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println("digraph G {");
            bw.write("digraph G {\n");
            for (int i = 0; i < mGraph.size(); i++) {
                for (int j = 0; j < mGraph.get(i).size(); j++) {
                    System.out.print(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n");
                    bw.write(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n");
                }
            }
            System.out.println("}");
            bw.write("}\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}