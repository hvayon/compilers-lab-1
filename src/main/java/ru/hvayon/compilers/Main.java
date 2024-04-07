package ru.hvayon.compilers;

import ru.hvayon.compilers.RPN.RPN;
import ru.hvayon.compilers.graph.Graph;
import ru.hvayon.compilers.graph.GraphCreator;
import ru.hvayon.compilers.graph.HopcroftAlgorithm;

public class Main {
    public static void main(String[] args) {

        //String exp = "(a.b)*.(c|d)+";
        //String exp = "(a.b)*.a.b.b";
        String exp = "(a|b)*.a.b.b";
//        (a|b)*abb
        //String exp = "(a.b)*";
        //String exp = "(c|d)+";

        RPN RPN = new RPN(exp);

        System.out.println("Ввод: " + RPN.infixExpr);
        System.out.println(("Постфиксная форма: " + RPN.postfixExpr));

        GraphCreator graphCreator = new GraphCreator();

        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps();
        graph.toDot();

        graph = graph.Determine();

        System.out.println("ДКА");
        //graph.toDot();

        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        System.out.println("Мин автомат:");
        hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph).toDot();
       // graph.toDot();

//        System.out.println("Мин автомат:");
//        graph.toDot();
        //System.out.println(graph.checkIfStringRegex("ababcd"));
        //System.out.println(graph.checkIfStringRegex("bbabcd"));

        //System.out.println(graph.checkIfStringRegex(""));
        //System.out.println(graph.checkIfStringRegex("cc"));

        }
    }
