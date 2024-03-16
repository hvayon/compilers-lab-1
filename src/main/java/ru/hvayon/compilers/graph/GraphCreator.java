package ru.hvayon.compilers.graph;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class GraphCreator {

    public void createGraph(String polishRegex) {

        Map<Character, Function<Graph, Graph>> twoOperandCreators = Map.of(
                '|', GraphCreator::createUnionGraph,
                '.', GraphCreator::CreateConcatGraph
        );

        Map<Character, Function<Graph, Graph>> oneOperandCreators = Map.of(
                '*', GraphCreator::CreateIterationGraph,
                '+', GraphCreator::CreatePositiveIterationGraph
        );

        //Map<Character, Graph> map = new HashMap<>();

        //map.put('+', this.CreateIterationGraph());

        Stack<Graph> stack = new Stack<>();

        for (int i = 0; i < polishRegex.length(); i++) {
            Character elem = polishRegex.charAt(i);
            switch (elem) {
                case ('+'):
                    //System.out.println(stack);
                    Graph gr1 = stack.pop();
                    //Graph gr2 = stack.pop();
                    stack.push(oneOperandCreators.get(elem).apply(gr1));
                    //stack.push(twoOperandCreators.get(elem).apply(gr2));
                    System.out.println(stack);
                    break;
                case ('*'):
                    Graph gr2 = stack.pop();
                    //Graph gr = new Graph();
                    stack.push(oneOperandCreators.get(elem).apply(gr2));
                    //CreateIterationGraph();
                    System.out.println(stack);
                    //System.out.println('*');
                    break;
                case ('|'):
                    Graph gr3 = stack.pop();
                    Graph gr5 = stack.pop();
                    stack.push(oneOperandCreators.get(elem).apply(gr3));
                    stack.push(twoOperandCreators.get(elem).apply(gr5));
                    System.out.println(stack);
                    //System.out.println('|');
                    break;
                case ('.'):
                    Graph gr4 = stack.pop();
                    Graph gr6 = stack.pop();
                    stack.push(oneOperandCreators.get(elem).apply(gr4));
                    stack.push(twoOperandCreators.get(elem).apply(gr6));
                    System.out.println(stack);
                    //CreateConcatGraph();
                    //System.out.println('.');
                    break;
                default:
                    stack.push(CreateOneSymbolGraph(elem));
                    System.out.println("CreateOneSymbolGraph =" + stack);
                    //System.out.println("default");

            }
        }
    }

    public Graph CreateIterationGraphTemplate() {
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

        res.toDot();
        return res;
    }

    public Graph CreatePositiveIterationGraphTemplate() {
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
        res.mGraph.add(5, new ArrayList<>());

        res.mUnfinishedVertexes.add(0, new ArrayList<>());
        res.mUnfinishedVertexes.get(0).add(1);
        res.mUnfinishedVertexes.get(0).add(2);
        res.mUnfinishedVertexes.add(1, new ArrayList<>());
        res.mUnfinishedVertexes.get(1).add(3);
        res.mUnfinishedVertexes.get(1).add(4);

        return res;
    }

    public Graph CreateConcatGraphTemplate() {

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

    public Graph CreateOneSymbolGraph(char symbol) {

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

    public Graph CreateUnionStateMachine(Graph lhs, Graph rhs) {
        return createUnionGraphTemplate();
    }
}
