package com.social.socialnetwork.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entitate<Long> {
    private String firstName;
    private String lastName;
    private String hashedPassword;
    private String username;

    public Utilizator(String prenumeUtilizator, String numeUtilizator, String username, String hashedPassword) {
       this.firstName=prenumeUtilizator;
       this.lastName=numeUtilizator;
       this.hashedPassword=hashedPassword;
       this.username=username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private List<Utilizator> friends;

    /**
     * constructor utilizator
     * @param firstName prenume
     * @param lastName nume utilizator
     */
    public Utilizator(String firstName, String lastName) {
        friends = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Utilizator(String firstName, String lastName,String username) {
        friends = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username=username;
    }

    /**
     * getter pentru prenume
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * adaugarea unui prieten
     * @param prieten alt utilizator
     */
    public void addFriend(Utilizator prieten){
        this.friends.add(prieten);
    }

    /**
     * stergerea unui prieten
     * @param prieten
     */
    public void deleteFriend(Utilizator prieten){
        this.friends.remove(prieten);
    }

    /**
     * getter pentru nume
     * @return numele utilizatorului
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter pentru prenume
     * @param firstName noul prenume
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * setter pentru nume
     * @param lastName noul nume
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * lista de prieteni
     * @return List de utilizatori reprezentand prietenii
     */
    public List<Utilizator> getFriends() {
        return friends;
    }

    /**
     * metoda de toString
     * @return
     */
    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName;//+ "\t" + friends;
    }

    /**
     * equals pentru 2 utilizatori
     * @param o obiect de comparat
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Utilizator user = (Utilizator) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(friends, user.friends);
    }

    /**
     * functie de hashcode
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, friends);
    }

    public int compareTo(Utilizator y) {
        if(this.getFirstName().equals(y.getFirstName())){
            return this.getLastName().compareTo(y.getLastName());
        }
        return this.getFirstName().compareTo(y.getFirstName());
    }
}
