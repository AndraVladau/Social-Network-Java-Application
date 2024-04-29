package com.social.socialnetwork.Domain;

import java.util.Objects;

public class Tuplu<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * constructor pentru un tuplu
     * @param e1 first
     * @param e2 second
     */
    public Tuplu(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * @return primul element
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * setter pentru primul element
     * @param e1 noul primul element
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * setter pentur al doilea element
     * @param e2 noul al doilea element
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    /**
     * getter pentru al doilea element
     * @return al doilea element
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * suprascrierea functiei de equals a.i. sa nu conteze ordinea elementelor
     * @param o alt obiect
     * @return true sau false daca sunt egale sau nu
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuplu<?, ?> tuplu = (Tuplu<?, ?>) o;
        return Objects.equals(e1, tuplu.e1) && Objects.equals(e2, tuplu.e2)
                || Objects.equals(e2, tuplu.e1) && Objects.equals(e1, tuplu.e2);
    }

    /**
     * suprascrierea functiei de hashcode a.i. sa nu conteze ordinea elementelor
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(Objects.hash(e1, e2) + Objects.hash(e2, e1));
    }

    /**
     * metoda toString
     * @return basic
     */
    @Override
    public String toString() {
        return "Tuple{" +
                "e1=" + e1 +
                ", e2=" + e2 +
                '}';
    }
}
