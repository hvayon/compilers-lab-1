package ru.hvayon.compilers.RPN;
public class Operator {
    private Character name;
    private Integer priority;
    private Integer numOperands;

    public Operator(Character name, Integer priority, Integer numOperands) {
        this.name = name;
        this.priority = priority;
        this.numOperands = numOperands;
    }

    public Character getName() {
        return name;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getNumOperands() {
        return numOperands;
    }
}
