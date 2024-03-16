package ru.hvayon.compilers;

import ru.hvayon.compilers.RPN.RPN;
import ru.hvayon.compilers.graph.GraphCreator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //String expression = "(a|b)*.(c|d)+";

        String exp = "a+";

        RPN RPN = new RPN(exp);

        System.out.println("Ввод: " + RPN.infixExpr);
        System.out.println(("Постфиксная форма: " + RPN.postfixExpr));

        GraphCreator graphCreator = new GraphCreator();

        graphCreator.createGraph(RPN.postfixExpr);
        }
    }
