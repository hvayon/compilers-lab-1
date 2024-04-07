package ru.hvayon.compilers.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HopcroftAlgorithm {

    //Set<Set<State>>
    private static Set<Set<Integer>> splitGraph(Set<Integer> vertexes, Set<Character> alphabet, Graph graph, ArrayList<Set<Integer>> groups) {
        Set<Integer> newVertexes = new HashSet<>();

        int idx;


        HashMap<Integer, HashMap<Character, Integer>> res = new HashMap<>();

        for (Character c : alphabet) {
            for (Integer vtx : vertexes) {
                // найти состояние, в которое мы перейдем из s по символу c
                    idx = graph.mRules.get(vtx).indexOf(c);
                    System.out.println("Индекс =" + graph.mRules.get(vtx).indexOf(c));
                    if (idx != -1) {
                        System.out.println("Значение по индексу в mGraph =" + graph.mGraph.get(vtx).get(idx));
                        // если оно находится в данной группе, то добавляем его в новый набор
                        Integer i = 0;
                        for (Set<Integer> group : groups) {
                            if (group.contains(graph.mGraph.get(vtx).get(idx))) {
                                if (!res.containsKey(vtx)) {
                                    res.put(vtx, new HashMap<Character, Integer>());
                                }
                                res.get(vtx).put(c, i);
                            }
                            i++;
                        }
                    }
            }
        }
        Set<Integer> keys = res.keySet();
        HashMap<HashMap<Character, Integer>, Set<Integer>> map = new HashMap<>();

        for (Integer xkey : keys) {
            if (!map.containsKey(res.get(xkey))) {
                map.put(res.get(xkey), new HashSet<>());
            }

            map.get(res.get(xkey)).add(xkey);
        }

        Set<Set<Integer>> sub_groups = new HashSet<>();
        if (map.isEmpty()) {
            sub_groups.add(vertexes);
        }
        for (Set<Integer> sub_group : map.values()) {
            sub_groups.add(sub_group);
        }

        return sub_groups;
    }


    // АЛГОРИТМ ХОПКРОФТА
    public Graph minimizeDfaHopcroftAlgorithm(Graph graph) {

        Set<Character> alphabet = new HashSet<>();
        // вынести в Graph
        for (ArrayList<Character> arrC : graph.mRules) {
            for ( Character c : arrC )
                alphabet.add(c);
        }

        //System.out.println("Алфавит =" + alphabet);

        // индексы вершин с конечными состояниями
        Set<Integer> finalGroup = new HashSet<>();
        // индексы вершин с неконечными состояниями
        Set<Integer> nonFinalGroup = new HashSet<>();

        for (int i = 0; i < graph.mGraph.size(); i++) {
            if (graph.mEndVertexes.contains(i))
                finalGroup.add(i);
            else
                nonFinalGroup.add(i);
            ;
        }

        // добавляем их в сет сетов (current) и проходимся по нему в цикле
        ArrayList<Set<Integer>> current = Stream.of(finalGroup, nonFinalGroup)
                .collect(Collectors.toCollection(ArrayList::new));

        // создаем новый сет сетов
        ArrayList<Set<Integer>> P;

        while (true) {
            P = new ArrayList<>();
            // для каждой группы состояний
            for (Set<Integer> vertexes : current) {
                System.out.println("States = " + vertexes);
                P.addAll(splitGraph(vertexes, alphabet, graph, current));
                P.forEach(set -> {
                    if (!set.isEmpty()) {
                        System.out.println("set:" + set + ", items: ");
                        set.forEach(Integer -> System.out.println(Integer));
                    }
                });
                System.out.println("---");
            }

            if (current.size() == P.size())
                break;

            current = P;
        }
        System.out.println("Итоговые состояния:");
        System.out.println(current);

        Graph newGraph = new Graph();
        for (Set<Integer> sub_set : current) {
            newGraph.mGraph.add(new ArrayList<>());
            newGraph.mRules.add(new ArrayList<>());
        }

        class Pr {
            Pr(Integer f, Character c) {
                first = f;
                second = c;
            }
            Integer first;
            Character second;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Pr pr)) return false;
                return Objects.equals(first, pr.first) && Objects.equals(second, pr.second);
            }

            @Override
            public int hashCode() {
                return Objects.hash(first, second);
            }
        }


        //Integer i = 0;
    //graph.toDot();
        for (Set<Integer> crt : current) {
            ArrayList<Pr> newg = new ArrayList<>();

            Integer i = current.indexOf(crt);

            if (crt.contains(graph.mBeginVertex)) {
                newGraph.mBeginVertex = i;
            }

            for (Integer vtx : crt) {
                if (graph.mEndVertexes.contains(vtx)) {
                    newGraph.mEndVertexes.add(i);
                    break;
                }
            }

            //System.out.println("Вершины =" + current.indexOf(crt));
            for (Integer underlying_vertex: crt) {
                Integer z = 0;
                for (Integer edge : graph.mGraph.get(underlying_vertex)) {
                    for (Integer j = 0; j < current.size(); j++) {
                        if (current.get(j).contains(edge)) {
                            Pr p = new Pr(j, graph.mRules.get(underlying_vertex).get(z));
                            if (!newg.contains(p)) {
                                newg.add(p);
                            }

                        }
                    }
                    z++;
                }
            }

            for (Pr pr : newg) {
                newGraph.mRules.get(i).add(pr.second);
                newGraph.mGraph.get(i).add(pr.first);
            }
        }
        //newGraph.toDot();
        return newGraph;
    }
}