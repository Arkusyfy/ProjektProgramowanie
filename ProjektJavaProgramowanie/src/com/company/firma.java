package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class firma implements java.io.Serializable {
    private pojazd[] flotaFirmy = new pojazd[100];
    private kierowca[] kierowcyFirmy = new kierowca[50];

    public String sortujPojazdyWgKursow(){
        StringBuilder out = new StringBuilder();
        ArrayList<pojazd> pojazdy = new ArrayList<pojazd>();
        for (pojazd i : flotaFirmy){
            if(i!= null)
                pojazdy.add(i);
        }
        pojazdy.sort(new Comparator<pojazd>() {
            @Override
            public int compare(pojazd o1, pojazd o2) {
                return o2.getLiczbaOdbytychKursow()-o1.getLiczbaOdbytychKursow();
            }
        });

        for (pojazd i :pojazdy){
            out.append(i.getIdentyfikator()+": "+i.getNazwa()+"\nOdbyte kursy: "+i.getLiczbaOdbytychKursow()+'\n');
        }

        return out.toString();
    }

    public String listWTrasie(){
        StringBuilder out = new StringBuilder();

        for (pojazd i : flotaFirmy){
            if(i!=null && i.getCzyWTrasie())out.append(i.getIdentyfikator()+": "+i.getNazwa()+"\nŁadowność: "+i.getLadownosc()+"\nPojemność: "+i.getPojemnosc()+'\n');
        }

        return out.toString();
    }

    public String wyszukajPojazd(double ladownosc, double pojemnosc){
        StringBuilder out = new StringBuilder();
        ArrayList<pojazd> matchedPojazdy = new ArrayList<pojazd>();
        for(pojazd i : flotaFirmy){
            if(i!=null){
                if(i.getLadownosc()>ladownosc && i.getPojemnosc() > pojemnosc){
                    matchedPojazdy.add(i);
                }
            }
        }
        if(matchedPojazdy.size() == 0)
            System.out.println("Brak pojazdów");

        for (pojazd i :matchedPojazdy){
            out.append(i.getIdentyfikator()+": "+i.getNazwa()+"\nLadowność: "+i.getLadownosc()+"\nPojemność: "+i.getPojemnosc()+"\n\n");
        }

        return out.toString();
    }

    public String wyszukajKierowce(String kierowcaMatch){
        StringBuilder out = new StringBuilder();
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(kierowcaMatch.toLowerCase().split("\\s+")));
        ArrayList<kierowca> matchedKierowcy = new ArrayList<kierowca>();
        for (kierowca i : kierowcyFirmy){
            if(i!=null){
                for (String word : keywords){
                    if(i.getImie().toLowerCase().contains(word)){
                        matchedKierowcy.add(i);
                        break;
                    }
                    else if(i.getNazwisko().toLowerCase().contains(word)){
                        matchedKierowcy.add(i);
                        break;
                    }
                }
            }
        }
        if (matchedKierowcy.size()==0)
        System.out.println("Nie znaleziono żadnego kierowcy");


        for (kierowca i : matchedKierowcy){
            out.append(i.getIdentyfikator()+": "+i.getImie()+" "+i.getNazwisko()+'\n');
        }

        return out.toString();
    }

    public boolean wyjazdPojazdu(int id){
        for (int i=0; i<flotaFirmy.length; i++){
            if(flotaFirmy[i]!= null && flotaFirmy[i].getIdentyfikator()==id){
                if(flotaFirmy[i].getCzyWTrasie()==true){
                    System.out.println("Wybrany pojazd jest już w trasie");
                    return false;
                }
                flotaFirmy[i].setCzyWTrasie(true);
                return true;
            }
        }
        System.out.println("Brak pojazdu o danym id");
        return false;
    }

    public boolean przyjazdPojazdu(int id){
        for (int i=0; i<flotaFirmy.length; i++){
            if(flotaFirmy[i]!= null && flotaFirmy[i].getIdentyfikator()==id){
                if(flotaFirmy[i].getCzyWTrasie()==false){
                    System.out.println("Wybrany pojazd nie jest w trasie");
                    return false;
                }
                flotaFirmy[i].setCzyWTrasie(false);
                flotaFirmy[i].addLiczbaOdbytychKursow();
                return true;
            }
        }
        System.out.println("Brak pojazdu o danym id");
        return false;
    }

    public boolean editPojazd(int id, String nazwa, double ladownosc, double pojemnosc){
        boolean flag=false;
        for(int i=0; i<flotaFirmy.length; i++){
            if(flotaFirmy[i]!= null && flotaFirmy[i].getIdentyfikator() == id){
                flotaFirmy[i].setNazwa(nazwa);
                flotaFirmy[i].setLadownosc(ladownosc);
                flotaFirmy[i].setPojemnosc(pojemnosc);
                flag=true;
                break;
            }
        }
        return flag;
    }

    public boolean kierowcaDoPojazdu(int idKierowcy, int idPojazdu){
        for (int i =0;i<kierowcyFirmy.length; i++){
            if(kierowcyFirmy[i]!=null && kierowcyFirmy[i].getIdentyfikator()==idKierowcy){
                for (int j = 0; j<flotaFirmy.length; j++){
                    if(flotaFirmy[j]!= null && flotaFirmy[j].getIdentyfikator()==idPojazdu){
                        if(flotaFirmy[j].przypiszKierowce(kierowcyFirmy[i]))
                        return true;
                        else return false;
                    }
                }
                System.out.println("Brak pojazdu o danym id");
                return false;
            }
        }
        System.out.println("Brak kierowcy o danym id");
        return false;
    }

    public boolean editKierowca(int id, String imie, String nazwisko){
        boolean flag=false;
        for (int i = 0; i<kierowcyFirmy.length; i++){

            if(kierowcyFirmy[i]!=null && kierowcyFirmy[i].getIdentyfikator() == id){
                kierowcyFirmy[i].setImie(imie);
                kierowcyFirmy[i].setNazwisko(nazwisko);
                flag=true;
                break;
            }
        }
        return flag;
    }

    public boolean dodajPojazd(String nazwa, double ladownosc, double pojemosc) {
        boolean dodano = false;
        for (int i = 0; i < flotaFirmy.length; i++) {
            if (flotaFirmy[i] == null) {
                flotaFirmy[i] = new pojazd(ilePojazdow(), nazwa, ladownosc, pojemosc);
                dodano = true;
                break;
            }
        }
        if (dodano) return dodano;
        else {
            System.out.println("Brak miejsca w bazie");
            return dodano;
        }
    }

    public boolean dodajKierowce(String imie, String nazwisko) {
        boolean dodano = false;
        for (int i = 0; i < kierowcyFirmy.length; i++) {
            if (kierowcyFirmy[i] == null) {
                kierowcyFirmy[i] = new kierowca(ileKierowcow(), imie, nazwisko);
                dodano = true;
                break;
            }
        }
        if (dodano) return dodano;
        else {
            System.out.println("Brak miejsca w bazie");
            return dodano;
        }
    }

    public String listaPojazdow(boolean extendedInfo) {
        ArrayList<String> outputData = new ArrayList<String>();
        for (pojazd i : flotaFirmy) {
            if (i != null) {
                outputData.add(i.getIdentyfikator() + ": " + i.getNazwa() + ", Ładowność: " + i.getLadownosc() + ", Pojemność: " + i.getPojemnosc() + "\nOdbyte kursy: " + i.getLiczbaOdbytychKursow() + "\n" + (i.getCzyWTrasie() ? "W trasie" : "Dostępny") + '\n');
            }
        }
        if (extendedInfo) {
            int listCounter = 0;
            for (int i = 0; i < flotaFirmy.length; i++) {
                if (flotaFirmy[i] != null) {
                    StringBuilder sb = new StringBuilder();
                    for (kierowca k : flotaFirmy[i].getPrzypisaniKierowcy()) {
                        if(k != null)
                        sb.append(k.getIdentyfikator() + ": " + k.getImie() + " " + k.getNazwisko() + "\n");
                    }
                    outputData.set(listCounter++, outputData.get(i) + "\nPrzypisani kierowcy:\n" + sb.toString() + '\n');
                }
            }
        }


        return String.join("\n", outputData);
    }

    public int ileKierowcow() {
        int tempcounter = 0;
        for (kierowca i : kierowcyFirmy) {
            if (i != null) tempcounter++;
        }
        return tempcounter;
    }

    public int ilePojazdow() {
        int tempcounter = 0;
        for (pojazd i : flotaFirmy) {
            if (i != null) tempcounter++;
        }
        return tempcounter;
    }

    public String listaKierowcow() {
        StringBuilder tempLista = new StringBuilder();
        for (kierowca i : kierowcyFirmy) {
            if (i != null) {
                tempLista.append(i.getIdentyfikator() + ": " + i.getImie() + " " + i.getNazwisko() + "\n");
            }
        }
        return tempLista.toString();
    }


}
