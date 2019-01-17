package com.company;

public class firma {
    pojazd[] flotaFirmy = new pojazd[100];
    kierowca[] kierowcyFirmy = new kierowca[50];

    int ileKierowcow(){
        int counter = 0;
        for (kierowca i : kierowcyFirmy) {
            if (i != null) counter++;
        }
        return counter;
    }

    int ilePojazdow(){
        int counter = 0;
        for (pojazd i : flotaFirmy) {
            if (i != null) counter++;
        }
        return counter;
    }




}
