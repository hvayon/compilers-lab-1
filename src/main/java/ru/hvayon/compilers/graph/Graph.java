package ru.hvayon.compilers.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Graph {

    int nodes;

    int beginVertex;

    int endVertex;
    List<List<Integer>> adjlist;

    ArrayList<ArrayList<Integer>> mGraph;
    ArrayList<ArrayList<Character>> mRules;

    ArrayList<Integer> mUnfinishedVertexes;

    HashMap<Character,Graph> graphOperators;

    Graph (int arg_nodes) {
        nodes = arg_nodes;
//        adjlist = new ArrayList<>(nodes);
//        for (int i=0; i<nodes; i++)
//            adjlist.add(new ArrayList<>());
        mGraph = new ArrayList<>(nodes);
        for (int i=0; i<nodes; i++)
            mGraph.add(new ArrayList<>());

        mRules = new ArrayList<>(nodes);
        for (int i=0; i<nodes; i++)
            mRules.add(new ArrayList<>());

    }

    Graph() {
        mGraph = new ArrayList<>();
        mRules = new ArrayList<>();
        mUnfinishedVertexes = new ArrayList<Integer>();
    }


//    void CreateGraphOperators(List<List<Integer>> adjlist) {
//        graphOperators = new HashMap<>();
//        graphOperators.put('+', createPlusOperatorGraph(adjlist));
//        graphOperators.put('*', createStarOperatorGraph(adjlist));
//    }

     void createPlusOperatorGraph(List<List<Integer>> adjlist) {
        System.out.println('+');
        //return adjlist;
    }

    void createStarOperatorGraph(List<List<Integer>> adjlist) {
        System.out.println('+');
    }
    void AddEdge (int src, int dst) {
        adjlist.get(src).add(dst);
        //adjlist.get(dst).add(src);
    }

    void Iterate (int src) {
        //System.out.print("\n" + src + " : ");
        for (Integer adj_node : adjlist.get(src)) {
            System.out.print(adj_node);
        }
    }

    void IterateChar (int src) {
        //System.out.print("\n" + src + " : ");
        for (int adj_node : adjlist.get(src)) {
            System.out.print("[label=\"");
            if (adj_node > 32 && adj_node < 127)
                System.out.print((char) adj_node );
            else
                System.out.print(adj_node);
            System.out.print("\"]\n");
        }
    }

    void toDot() {
        try {
            File file = new File("src/main/resources/graph.dot");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println("digraph G {");
            bw.write("digraph G {\n");
            for (int i = 0; i < mGraph.size(); i++) {
                for (int j = 0; j < mGraph.get(i).size(); j++) {
                    System.out.print(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n}\n");
                    bw.write(i + " -> " + mGraph.get(i).get(j) + "[label=\"" + mRules.get(i).get(j) + "\"]\n}\n");
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {

        Graph g = new Graph(3);
        g.AddEdge(0,2 );
        g.AddEdge(1,2 );
        g.AddEdge(2,1 );


        Graph c = new Graph(3);
        c.AddEdge(0,'a' );
        c.AddEdge(1,'a' );
        c.AddEdge(2,'b' );


        System.out.println("digraph StateMachine {");
        for (int i = 0; i < 3; i++) {
            System.out.print(i);
            System.out.print(" -> ");
            g.Iterate(i);
            c.IterateChar(i);
        }

    }
}