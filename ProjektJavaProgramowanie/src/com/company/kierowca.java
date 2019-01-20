package com.company;

public class kierowca implements java.io.Serializable{
    static int count;
    private int identyfikator;
    private String imie, nazwisko;

    kierowca(int id, String imie, String nazwisko){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.identyfikator = id;
    }

    public int getIdentyfikator(){
        return identyfikator;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
}