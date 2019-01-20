package com.company;

public class pojazd implements java.io.Serializable {
    static int count;
    private int identyfikator;
    private int liczbaOdbytychKursow;
    private String nazwa;
    private double ladownosc, pojemnosc;
    private kierowca[] przypisaniKierowcy = new kierowca[3];
    private boolean czyWTrasie;

    pojazd(int id, String nazwa, double ladownosc, double pojemnosc) {
        this.nazwa = nazwa;
        this.ladownosc = ladownosc;
        this.pojemnosc = pojemnosc;
        this.identyfikator = id;
    }

    public void addLiczbaOdbytychKursow() {
        this.liczbaOdbytychKursow ++;
    }

    public void setCzyWTrasie(boolean czyWTrasie) {
        this.czyWTrasie = czyWTrasie;
    }

    public boolean przypiszKierowce(kierowca kiero){
        for (int i =0; i<przypisaniKierowcy.length; i++){
            if(przypisaniKierowcy[i] == null){
                przypisaniKierowcy[i]=kiero;
                return true;
            }else
            {
                if (przypisaniKierowcy[i].getIdentyfikator()==kiero.getIdentyfikator()){
                    System.out.println("Ten kierowca jest już przypisany do tego pojazdu");
                    return false;
                }
            }
        }
        System.out.println("Pojazd może mieć maksymalnie trzech kierowców");
        return false;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public void setLadownosc(double ladownosc) {
        this.ladownosc = ladownosc;
    }

    public void setPojemnosc(double pojemnosc) {
        this.pojemnosc = pojemnosc;
    }

    public int getIdentyfikator() {
        return identyfikator;
    }

    public int getLiczbaOdbytychKursow() {
        return liczbaOdbytychKursow;
    }

    public double getLadownosc() {
        return ladownosc;
    }

    public double getPojemnosc() {
        return pojemnosc;
    }

    public kierowca[] getPrzypisaniKierowcy() {
        return przypisaniKierowcy;
    }

    public String getNazwa() {
        return nazwa;
    }

    public boolean getCzyWTrasie() {
        return czyWTrasie;
    }
}
