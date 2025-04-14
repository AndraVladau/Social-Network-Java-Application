package com.social.socialnetwork.Domain;

import java.io.Serializable;
import java.util.Objects;

public class Entitate<ID> implements Serializable {
    protected ID id;

    /**
     * getter pentru ID
     * @return id de tipul ID
     */
    public ID getId(){
        return id;
    }

    /**
     * setter pentru ID
     * @param id ID
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * suprascrierea functiei de equals
     * @param o obiect de comparat
     * @return true daca sunt egale, false altfel
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entitate<?> entitate = (Entitate<?>) o;
        return Objects.equals(id, entitate.id);
    }

    /**
     * functia de hash
     * @return hashcodeul
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * suprascrierea functiei de toString
     * @return entitatea si id-ul
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
