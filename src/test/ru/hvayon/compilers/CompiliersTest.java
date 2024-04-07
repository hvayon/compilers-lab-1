package ru.hvayon.compilers;

import org.junit.jupiter.api.Test;
import ru.hvayon.compilers.RPN.RPN;
import ru.hvayon.compilers.graph.Graph;
import ru.hvayon.compilers.graph.GraphCreator;
import ru.hvayon.compilers.graph.HopcroftAlgorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompiliersTest {
    String expectedResult;
    @Test
    void test1() {
        String exp = "(a.b)*";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        String actualResult = graphCreator.createGraph(RPN.postfixExpr).deleteEps().toDot();
        expectedResult = "digraph G {\n" +
                "0 -> 2[label=\"a\"]\n" +
                "1 -> 2[label=\"a\"]\n" +
                "2 -> 1[label=\"b\"]\n" +
                "}";

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void test2() {
        String exp = "(a.b)*.(c|d)+";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        String actualResult = graphCreator.createGraph(RPN.postfixExpr).deleteEps().toDot();
        expectedResult = "digraph G {\n" +
                "0 -> 4[label=\"a\"]\n" +
                "0 -> 2[label=\"c\"]\n" +
                "0 -> 1[label=\"d\"]\n" +
                "1 -> 2[label=\"c\"]\n" +
                "1 -> 1[label=\"d\"]\n" +
                "2 -> 2[label=\"c\"]\n" +
                "2 -> 1[label=\"d\"]\n" +
                "3 -> 4[label=\"a\"]\n" +
                "3 -> 2[label=\"c\"]\n" +
                "3 -> 1[label=\"d\"]\n" +
                "4 -> 3[label=\"b\"]\n" +
                "}";

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void test3() {
        String exp = "a+";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        String actualResult = graphCreator.createGraph(RPN.postfixExpr).deleteEps().toDot();
        expectedResult = "digraph G {\n" +
                "0 -> 1[label=\"a\"]\n" +
                "1 -> 1[label=\"a\"]\n" +
                "}";

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void test4() {
        String exp = "(a.b)*";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps();


        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        graph = hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph);

        assertEquals(true, graph.checkIfStringRegex("abab"));
    }

    @Test
    void test5() {
        String exp = "(a.b)*.(c|d)+";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps();

        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        graph = hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph);

        assertEquals(true, graph.checkIfStringRegex("ababcd"));
    }

    @Test
    void test6() {
        String exp = "(a.b)+";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps();

        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        graph = hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph);

        assertEquals(false, graph.checkIfStringRegex("cc"));
    }

    @Test
    void test7() {
        String exp = "(c|d)+";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps();

        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        graph = hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph);

        assertEquals(true, graph.checkIfStringRegex("cdddd"));
    }

    @Test
    void test8() {
        String exp = "(a|b)*.a.b.b";

        RPN RPN = new RPN(exp);
        GraphCreator graphCreator = new GraphCreator();
        Graph graph = graphCreator.createGraph(RPN.postfixExpr).deleteEps().Determine();
        graph.toDot();

        HopcroftAlgorithm hopcroftAlgorithm = new HopcroftAlgorithm();
        graph = hopcroftAlgorithm.minimizeDfaHopcroftAlgorithm(graph);
        graph.toDot();

        assertEquals(false, graph.checkIfStringRegex("cdddd"));
        assertEquals(true, graph.checkIfStringRegex("ababb"));
        assertEquals(true, graph.checkIfStringRegex("abb"));
        assertEquals(true, graph.checkIfStringRegex("aabb"));
        assertEquals(true, graph.checkIfStringRegex("babb"));
        assertEquals(false, graph.checkIfStringRegex(""));
    }
}
