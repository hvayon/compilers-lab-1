package ru.hvayon.compilers.graph;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class GraphCreator {

    public void createGraph(String polishRegex) {

        Stack<Graph> stack = new Stack<>();

        for (int i = 0; i < polishRegex.length(); i++) {
            Character elem = polishRegex.charAt(i);
            switch (elem) {
                case ('+'):
                    Graph gr = stack.pop();
                    stack.push(gr);
                    System.out.println('+');
                    break;
                case ('*'):
                    Graph gr = new Graph();
                    CreateIterationGraph();
                    System.out.println('*');
                    break;
                case ('|'):
                    CreateUnionStateGraph();
                    System.out.println('|');
                    break;
                case ('.'):
                    CreateConcatGraph();
                    System.out.println('.');
                    break;
                default:
                    stack.push(CreateOneSymbolGraph(elem));
                    System.out.println("default");

            }
        }
    }

    public Graph CreateIterationGraph() {
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

        res.mUnfinishedVertexes.add(0,new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);

        res.beginVertex = 0;
        res.endVertex = 3;

        res.toDot();
        return res;
    }

    public Graph CreatePositiveIterationGraph() {
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

        res.mUnfinishedVertexes.add(0,new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);

        res.beginVertex = 0;
        res.endVertex = 3;

        return res;
    }

    public Graph CreateUnionStateGraph() {
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
        res.mGraph.add(5, new ArrayList<>());

        res.mUnfinishedVertexes.add(0,new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);
        res.mUnfinishedVertexes.add(1,new ArrayList<>());
        res.mUnfinishedVertexes.get(1).add(3);
        res.mUnfinishedVertexes.get(1).add(4);

        return res;
    }

    public Graph CreateConcatGraph() {

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

        res.beginVertex = 0;
        res.endVertex = 2;

        return res;
    }

    public Graph CreateOneSymbolGraph(char symbol) {

        Graph res = new Graph();

        res.mGraph.add(0,new ArrayList<>());
        res.mGraph.get(0).add(1);
        res.mGraph.add(1, new ArrayList<>());

        res.mRules.add(0, new ArrayList<>());
        res.mRules.get(0).add(symbol);
        res.mRules.add(1, new ArrayList<>());

        res.beginVertex = 0;
        res.endVertex = 1;

        return res;
    }
}
