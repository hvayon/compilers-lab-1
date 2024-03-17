package ru.hvayon.compilers.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {

    int nodes;

    /* индекс начальной вершины */
    int mBeginVertex;

    /* индекс конечной вершины */
    int mEndVertex;

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