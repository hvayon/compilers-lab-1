package ru.hvayon.compilers.graph;

import java.util.ArrayList;
import java.util.Stack;

public class GraphCreator {
    void createGraph(String polishRegex) {
        Stack<Graph> stack;

        for (int i = 0; i < polishRegex.length(); i++) {
            polishRegex.charAt(i);
            // createOneSymbolGraph
        }
    }

    public Graph CreateUnionStateMachineTemplate() {
        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.get(0).add(3);
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());
        res.mGraph.get(2).add(3);

        return res;
    }

    public Graph CreateConcatSymbolStateMachine(char symbol) {

        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.add(1, new ArrayList<>());
        res.mRules.add(2, new ArrayList<>());

        res.mUnfinishedVertexes.add(0,1);
        res.mUnfinishedVertexes.add(1,2);

        res.beginVertex = 0;
        res.endVertex = 2;

        return res;
    }

    public Graph CreateOneSymbolStateMachine(char symbol) {

        Graph res = new Graph();

        res.mGraph.add(0,new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.add(1, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add(symbol);
        res.mRules.add(1, new ArrayList<>());

        res.beginVertex = 0;
        res.endVertex = 1;

        res.toDot();

        return res;
    }
}
