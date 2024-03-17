package ru.hvayon.compilers.graph;

import java.util.ArrayList;
import java.util.Stack;

public class GraphCreator {

    public Graph createGraph(String polishRegex) {

        Stack<Graph> stack = new Stack<>();

        for (int i = 0; i < polishRegex.length(); i++) {
            Character elem = polishRegex.charAt(i);
            if (elem.equals('+')) {
                Graph gr1 = stack.pop();
                stack.push(createPositiveIterationGraph(gr1));
            } else if (elem.equals('*')) {
                Graph gr2 = stack.pop();
                stack.push(createIterationGraph(gr2));
            } else if (elem.equals('|')) {
                Graph gr3 = stack.pop();
                Graph gr4 = stack.pop();
                stack.push(createUnionGraph(gr3, gr4));
            } else if (elem.equals('.')) {
                Graph gr5 = stack.pop();
                Graph gr6 = stack.pop();
                stack.push(createConcatGraph(gr5, gr6));
            } else {
                stack.push(createOneSymbolGraph(elem));
            }
      }
        assert(stack.size() == 1);
        Graph res = stack.pop();

        return res;
    }

    public Graph сreateIterationGraphTemplate() {
        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.get(0).add(3);
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());
        res.mGraph.get(2).add(1);
        res.mGraph.get(2).add(3);
        res.mGraph.add(3, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add('E');
        res.mRules.get(0).add('E');
        res.mRules.add(1, new ArrayList<>());
        res.mRules.add(2, new ArrayList<>());
        res.mRules.get(2).add('E');
        res.mRules.get(2).add('E');
        res.mRules.add(3, new ArrayList<>());

        res.mUnfinishedVertexes.add(0, new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);

        res.mBeginVertex = 0;
        res.mEndVertex = 3;

        return res;
    }

    public Graph createPositiveIterationGraphTemplate() {
        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());
        res.mGraph.get(2).add(1);
        res.mGraph.get(2).add(3);
        res.mGraph.add(3, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add('E');
        res.mRules.add(1, new ArrayList<>());
        res.mRules.add(2, new ArrayList<>());
        res.mRules.get(2).add('E');
        res.mRules.get(2).add('E');
        res.mRules.add(3, new ArrayList<>());

        res.mUnfinishedVertexes.add(0, new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);

        res.mBeginVertex = 0;
        res.mEndVertex = 3;

        return res;
    }

    public Graph createUnionGraphTemplate() {
        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.get(0).add(3);
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());
        res.mGraph.get(2).add(5);
        res.mGraph.add(3, new ArrayList<>());
        res.mGraph.add(4, new ArrayList<>());
        res.mGraph.get(4).add(5);
        res.mGraph.add(5, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add('E');
        res.mRules.get(0).add('E');
        res.mRules.add(1, new ArrayList<>());
        res.mRules.add(2, new ArrayList<>());
        res.mRules.get(2).add('E');
        res.mRules.add(3, new ArrayList<>());
        res.mRules.add(4, new ArrayList<>());
        res.mRules.get(4).add('E');
        res.mRules.add(5, new ArrayList<>());

        res.mUnfinishedVertexes.add(0, new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);
        res.mUnfinishedVertexes.add(1, new ArrayList<>());
        res.mUnfinishedVertexes.get(1).add(3);
        res.mUnfinishedVertexes.get(1).add(4);

        res.mBeginVertex = 0;
        res.mEndVertex = 5;

        return res;
    }

    public Graph createConcatGraphTemplate() {

        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.add(1, new ArrayList<>());
        res.mGraph.add(2, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.add(1, new ArrayList<>());
        res.mRules.add(2, new ArrayList<>());

        res.mUnfinishedVertexes.add(0, new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(0);
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.add(1, new ArrayList<>());
        res.mUnfinishedVertexes.get(1).add(1);
        res.mUnfinishedVertexes.get(1).add(2);

        res.mBeginVertex = 0;
        res.mEndVertex = 2;

        return res;
    }

    public Graph createOneSymbolGraph(char symbol) {

        Graph res = new Graph();

        res.mGraph.add(0, new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.add(1, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add(symbol);
        res.mRules.add(1, new ArrayList<>());

        res.mBeginVertex = 0;
        res.mEndVertex = 1;

        return res;
    }

    public Graph createIterationGraph(Graph gr) {
        return сreateIterationGraphTemplate().insertGraph(gr);
    }

    public Graph createPositiveIterationGraph(Graph gr) {
        return createPositiveIterationGraphTemplate().insertGraph(gr);
    }

    public Graph createConcatGraph(Graph lhs, Graph rhs){
        return createConcatGraphTemplate().insertGraph(lhs).insertGraph(rhs);
    }

    public Graph createUnionGraph(Graph lhs, Graph rhs) {
        return createUnionGraphTemplate().insertGraph(lhs).insertGraph(rhs);
    }
}
