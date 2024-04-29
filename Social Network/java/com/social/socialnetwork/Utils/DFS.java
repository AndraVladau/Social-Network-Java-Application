package com.social.socialnetwork.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DFS {
    private static final int ALB = 0;
    private static final int GRI = 1;
    private static final int NEGRU = 2;
    private static final int INFINIT = 999999;
    private static final long NIL = -1;

    private HashMap<Long, Vector<Long>> listaAdiacenta;
    private HashMap<Long, Long> parinti;
    private HashMap<Long, Integer> culoare;
    private Long start;

    public DFS(HashMap<Long, Vector<Long>> listaAdiacenta) {
        parinti = new HashMap<>();
        culoare = new HashMap<>();
        this.listaAdiacenta = listaAdiacenta;
        start = listaAdiacenta.keySet().iterator().next();
    }

    /**
     * main algorithm la dfs
     * @return lista care contine liste pentru fiecare componenta
     */
    public ArrayList<ArrayList<Long>> mainAlgorithm(){
        listaAdiacenta.keySet().forEach(i->{
            culoare.put(i, ALB);
            parinti.put(i, NIL);
        });
        ArrayList<ArrayList<Long>> toateComponentele = new ArrayList<>();
        ArrayList<Long> componentaConexa = new ArrayList<>();
        toateComponentele.add(componentaConexa);
        vizitare(componentaConexa, start);
        listaAdiacenta.keySet().forEach(i->{
            if(culoare.get(i) == ALB){
                ArrayList<Long> altaComponenta = new ArrayList<>();
                vizitare(altaComponenta, i);
                toateComponentele.add(altaComponenta);
            }
        });
        return toateComponentele;
    }

    /**
     * algortimul de vizitare
     * @param componentaConexa in care ne aflam
     * @param varf varful ce urmeaza a fi vizitat
     */
    private void vizitare(ArrayList<Long>componentaConexa, Long varf){
        componentaConexa.add(varf);
        culoare.put(varf, GRI);
        listaAdiacenta.get(varf).forEach(item->{
            if(culoare.get(item) == ALB){
                parinti.put(item, varf);
                vizitare(componentaConexa, item);
            }
        });
        culoare.put(varf, NEGRU);
    }
}
