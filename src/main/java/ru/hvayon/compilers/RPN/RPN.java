package ru.hvayon.compilers.RPN;

import java.util.ArrayList;
import java.util.Stack;

public class RPN {
    /* хранит инфиксное выражение */
    public String infixExpr;
    /* хранит постфиксное выражение */
    public String postfixExpr;
    /* cписок и приоритет операторов */
    ArrayList<Operator> operators = new ArrayList<>();

    public void addToArrayList() {
        operators.add(new Operator('(', 0, 0));
        operators.add(new Operator(')', 0, 0));
        operators.add(new Operator('+', 3, 1));
        operators.add(new Operator('*', 3, 1));
        operators.add(new Operator('|', 1, 2));
        operators.add(new Operator('.', 2, 2));
    }

    public Boolean checkIfOperandExist(Character opr) {
        for (Operator op : operators) {
            if (op.getName().equals(opr)) {
                return true;
            }
        }
        return false;
    }

    // Возвращает приоритет оператора
    public int checkPriority(Character opr) {
        for (Operator op : operators) {
            if (op.getName().equals(opr)) {
                //System.out.println("Символ содержится в операции.");
                return op.getPriority();
            }
        }
        System.out.println("Оператор не найден");
        return -1;
    }

    //	Конструктор класса
    public RPN(String expression) {
        //	Инициализируем поля
        infixExpr = expression;
        postfixExpr = ToPostfix(infixExpr);
    }

    private String GetStringNumber(String expr, Integer pos) {
        /*	Хранит число */
        String strNumber = "";

        /*	Перебираем строку */
        for (; pos < expr.length(); pos++) {
            /*	Разбираемый символ строки */
            Character num = expr.charAt(pos);

            /*	Проверяем, есть ли в мапе */
            if (!checkIfOperandExist(num)) {
                /*	Если да - прибавляем к строке */
                strNumber += num;
            } else {
                /* Если нет, то перемещаем счётчик к предыдущему символу */
                pos--;
                break;
            }
        }
        /*	Возвращаем число */
        return strNumber;
    }

    private String ToPostfix(String infixExpr) {
        addToArrayList();
        System.out.println(operators);
        /*	Выходная строка, содержащая постфиксную запись */
        String postfixExpr = "";
        /*	Инициализация стека, содержащий операторы в виде символов */
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infixExpr.length(); i++) {
            /*	Текущий символ */
            Character c = infixExpr.charAt(i);

            /*	Если симовол - цифра */
            if (!checkIfOperandExist(c)) {
                /*	Парсим его, передав строку и текущую позицию, и заносим в выходную строку */
                postfixExpr += c.toString();
            }
            else if (c == '(') {
                stack.push(c);
            }
            else if (c == ')') {
                // Заносим в выходную строку из стека всё вплоть до открывающей скобки
                while (stack.size() > 0 && stack.peek() != '(')
                    postfixExpr += stack.pop();
                // Удаляем открывающуюся скобку из стека
                stack.pop();
            }
            // Проверяем, содержится ли символ в списке операторов
            else if (checkIfOperandExist(c)) {

                Character op = c;

                // Заносим в выходную строку все операторы из стека, имеющие более высокий приоритет
                while (stack.size() > 0 && ( checkPriority(stack.peek()) > checkPriority(op))) {
                    postfixExpr += stack.pop();
                }
                // Заносим в стек оператор
                stack.push(op);
            }
     }
//        // Заносим все оставшиеся операторы из стека в выходную строку
        while (stack.size() > 0) {
            postfixExpr += stack.pop();
        }
            // Возвращаем выражение в постфиксной записи
        	return postfixExpr;
    }
}
