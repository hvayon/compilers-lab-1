package ru.hvayon.compilers;

import ru.hvayon.compilers.RPN.RPN;
import ru.hvayon.compilers.graph.GraphCreator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String expression = "(a|b)*.(c|d)+";

        RPN RPN = new RPN(expression);

        System.out.println("Ввод: " + RPN.infixExpr);
        System.out.println(("Постфиксная форма: " + RPN.postfixExpr));

        GraphCreator graphCreator = new GraphCreator();

        graphCreator.CreateOneSymbolStateMachine('a');

//        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
//
//        //graph.addEdge("a", "b");
//        graph.addVertex("a");
//        graph.addVertex("b");
//        graph.addVertex("+");
//
//
//        JGraphXAdapter<String, DefaultEdge> jGraphXAdapter = new JGraphXAdapter<>(graph);
//
//        mxIGraphLayout mxIGraphLayout = new mxCircleLayout(jGraphXAdapter);
//        mxIGraphLayout.execute(jGraphXAdapter.getDefaultParent());
//
//        BufferedImage bufferedImage =
//                mxCellRenderer.createBufferedImage(jGraphXAdapter, null, 3, Color.WHITE, true, null);
//
//        File newFIle = new File("graph.png");
//        ImageIO.write(bufferedImage, "PNG", newFIle);
        }
    }
