package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            RandomAccessFile baza;
            File plikBazy = new File("./baza");
            File toImport = new File("./import");
            File imported = new File("./imported");

            importFolders(toImport, imported);

            if (!plikBazy.exists()) {
                baza = initBaza(plikBazy);
            } else {
                baza = new RandomAccessFile(plikBazy.getPath(), "rw");
            }
            firma mainFirma = readFirma(baza);


            menu(mainFirma, baza);


            baza.close();

        } catch (IOException e) {
            System.out.println("Błąd dostepu do pliku bazy");
            System.exit(-1);
        }


    }

    private static RandomAccessFile initBaza(File plikBazy) {
        try {

            plikBazy.createNewFile();

            RandomAccessFile baza = new RandomAccessFile(plikBazy.getPath(), "rw");
            firma tempFirma = new firma();
            return writeFirma(tempFirma, baza);
        } catch (IOException e) {
            System.out.println("Błąd dostępu do pliku");
            System.exit(-1);
            return initBaza(plikBazy);
        }

    }

    private static firma readFirma(RandomAccessFile baza) {
        try {

            baza.seek(0);
            int firmaOffset = baza.readInt();
            byte[] firmaBytes = new byte[firmaOffset];
            for (int i = 0; i < firmaBytes.length; i++) {
                firmaBytes[i] = baza.readByte();
            }
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(firmaBytes));
            firma tempFirma = (firma) in.readObject();
            in.close();
            return tempFirma;
        } catch (IOException e) {
            System.out.println("Błąd przy czytaniu z pliku" +
                    "Używam pustej firmy" +
                    "Aby nie stracić poprzednich danych, zamknij program i spróbuj ponownie");

            return new firma();
        } catch (ClassNotFoundException e) {
            System.out.println("Błąd przy wczytywaniu klasy firmy z pliku" +
                    "Używam pustej firmy" +
                    "Aby nie stracić poprzednich danych, zamknij program i spróbuj ponownie");
            return new firma();
        }

    }

    private static RandomAccessFile writeFirma(firma tempFirma, RandomAccessFile baza) {
        RandomAccessFile baza1 = baza;
        try {

            baza1.seek(0);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(tempFirma);
            byte[] bytes = bos.toByteArray();
            baza1.writeInt(bytes.length);
            baza1.write(bytes);
            out.close();
            bos.close();
            return baza1;
        } catch (IOException e) {
            System.out.println("Błąd przy zapisywaniu pliku bazy");
            return baza;
        }
    }


    private static void cls() {
        System.out.println(new String(new char[25]).replace("\0", "\r\n"));
    }

    ;


    private static double getDouble() {
        Scanner tempsc = new Scanner(System.in);
        String tempdouble;
        tempdouble = tempsc.nextLine();
        if (isDouble(tempdouble)) {
            return Double.parseDouble(tempdouble.replace(',', '.'));
        } else {
            System.out.println("Podaj wartość liczbową");
            return getDouble();
        }


    }

    private static int getInt() {
        Scanner tempsc = new Scanner(System.in);
        int tempint;
        try {
            tempint = tempsc.nextInt();
            return tempint;
        } catch (Exception e) {
            System.out.println("Podaj liczbę całkowitą");
            return getInt();
        }
    }

    private static boolean isDouble(String input) {
        if (input.matches("\\d*[\\.,]?\\d+"))
            return true;
        else
            return false;
    }

    private static boolean isInt(String input) {
        if (input.matches("\\d+\\.\\d+"))
            return false;
        else
            return true;
    }

    private static void importFolders(File toImport, File imported) {

        if (!toImport.exists()) {
            toImport.mkdir();
        }
        if (!imported.exists()) {
            imported.mkdir();
        }
    }

    private static boolean importKierowcow(firma mainFirma, RandomAccessFile baza) {

        final File toImport = new File("./import");
        final File imported = new File("./imported");

        importFolders(toImport, imported);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(toImport.listFiles()));

        if (files.isEmpty()) {
            System.out.println("Brak plików do importu");
            return false;
        }
        ArrayList<kierowca> importedKierowcy = new ArrayList<kierowca>();
        try {
            for (File i : files) {
                boolean isImported = false;
                FileReader f = new FileReader(i.getPath());
                BufferedReader reader = new BufferedReader(f);
                String line;
                int counter = 0;
                while ((line = reader.readLine()) != null) {
                    counter++;
                    if (line.contains(";")) {
                        ArrayList<String> lineSplit = new ArrayList<String>(Arrays.asList(line.split("\\s*;\\s*")));
                        if (lineSplit.size() == 2) {
                            if (mainFirma.dodajKierowce(lineSplit.get(0), lineSplit.get(1))) {
                                System.out.println("Poyślnie dodano kierowce: " + lineSplit.get(0) + " " + lineSplit.get(1));
                                isImported = true;
                            }
                            continue;
                        }
                    }
                    System.out.println("Błąd w linijce nr " + counter + ": " + line);
                }

                reader.close();
                f.close();

                if (isImported) {
                    if (new File(imported.getPath() + "/" + i.getName()).exists()) {
                        File tmp = new File(imported.getPath() + "/" + i.getName());
                        Files.move(Paths.get(tmp.getPath()), Paths.get(tmp.getPath() + ".old." + new Date().getTime()));
                    }

                    Files.move(Paths.get(i.getPath()), Paths.get(imported.getPath() + "/" + i.getName()));
                }
            }

        } catch (IOException e) {
            System.out.println("Błąd dostępu do pliku");
            return false;
        }

        writeFirma(mainFirma, baza);
        return true;
    }


    private static void menu(firma mainFirma, RandomAccessFile baza) {
        String selector;
        do {
            cls();
            System.out.println("Menu główne");
            System.out.println();
            System.out.println("1 Wyświetl liczbe kierowców zatrudnionych w firmie");
            System.out.println("2 Wyświetl liczbe pojadzów należących do floty firmy");
            System.out.println("3 Wyświetl listę kierowców");
            System.out.println("4 Wyświetl listę pojazdów");
            System.out.println("5 Dodaj kierowcę");
            System.out.println("6 Dodaj pojazd");
            System.out.println("7 Edytuj kierowcę");
            System.out.println("8 Edytuj pojazd");
            System.out.println("9 Przypisz kierowcę do pojazdu");
            System.out.println("10 Odnotuj wyjazd pojazdu");
            System.out.println("11 Odnotuj powrót pojazdu");
            System.out.println("12 Wyszukaj kierowce");
            System.out.println("13 Wyszukaj dostępny pojazd o większej ładowności i pojemności od podanych");
            System.out.println("14 Wypisz listę pojazdów w trasie");
            System.out.println("15 Posortuj pojazdy wg liczby odbytych kursów");
            System.out.println("16 Importuj kierowców z pliku xDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            System.out.println();
            System.out.println("0 Wyjście");
            System.out.println();
            System.out.println("Wybierz funkcję wpisując jej numer i zatwierdzając enterem");

            Scanner sc = new Scanner(System.in);
            selector = sc.nextLine();

            switch (selector) {
                default: {
                    cls();
                    System.out.println("Funkcja nierozpoznana");
                    sc.nextLine();
                    break;
                }
                case "0": {
                    cls();
                    System.out.println("Zamykanie programu...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    break;
                }
                case "1": {
                    cls();
                    System.out.println("Ilość zatrudnionych kierowców: " + mainFirma.ileKierowcow());
                    sc.nextLine();
                    break;
                }
                case "2": {
                    cls();
                    System.out.println("Ilość pojazdów we flocie formy: " + mainFirma.ilePojazdow());
                    sc.nextLine();
                    break;
                }
                case "3": {
                    cls();
                    System.out.println("Lista kierowców:\n");
                    System.out.println(mainFirma.listaKierowcow());
                    sc.nextLine();
                    break;
                }
                case "4": {
                    cls();
                    System.out.println("Czy wyświetlić również listę przypisanych do danego pojazdu kierowców?\n1 - TAK\n0 - NIE\n");
                    String tempScan = sc.nextLine();
                    while (!tempScan.equals("1") && !tempScan.equals("0")) {
                        System.out.println("Zła komenda");
                        tempScan = sc.nextLine();
                    }
                    if (tempScan.equals("1")) System.out.println(mainFirma.listaPojazdow(true));
                    else System.out.println(mainFirma.listaPojazdow(false));
                    sc.nextLine();
                    break;
                }
                case "5": {
                    cls();
                    String tempImie, tempNazwisko;
                    System.out.println("Podaj imię kierowcy");
                    tempImie = sc.nextLine();
                    System.out.println("Podaj nazwisko kierowcy");
                    tempNazwisko = sc.nextLine();
                    if (mainFirma.dodajKierowce(tempImie, tempNazwisko)) {
                        System.out.println("Pomyślnie dodano kierowce");
                        baza = writeFirma(mainFirma, baza);
                    } else System.out.println("Nie udało sie dodać kierowcy");
                    sc.nextLine();
                    break;
                }
                case "6": {
                    cls();
                    String tempNazwa;
                    System.out.println("Podaj nazwe pojazdu");
                    tempNazwa = sc.nextLine();
                    System.out.println("Podaj ładowność pojazdu");
                    double tempLadownosc = getDouble();
                    System.out.println("Podaj pojemność pojazdu");
                    double tempPojemnosc = getDouble();
                    if (mainFirma.dodajPojazd(tempNazwa, tempLadownosc, tempPojemnosc)) {
                        System.out.println("Pomyślnie dodano pojazd");
                        baza = writeFirma(mainFirma, baza);
                    } else System.out.println("Nie udało sie dodać pojazdu");
                    sc.nextLine();
                    break;
                }
                case "7": {
                    cls();
                    System.out.println(mainFirma.listaKierowcow() + "\n");
                    System.out.println("Podaj id kierowcy, którego chcesz edytować");
                    int id = getInt();
                    System.out.println("Podaj nowe imie");
                    String imie = sc.nextLine();
                    System.out.println("Podaj nowe nazwisko");
                    String nazwisko = sc.nextLine();
                    if (mainFirma.editKierowca(id, imie, nazwisko)) {
                        System.out.println("Pomyślnie zedytowano kierowce");
                        baza = writeFirma(mainFirma, baza);
                    } else
                        System.out.println("Nie znaleziono kierowcy o podanym identyfikatorze");

                    sc.nextLine();
                    break;
                }
                case "8": {
                    cls();
                    System.out.println(mainFirma.listaPojazdow(false) + "\n");
                    System.out.println("Podaj id pojazdu, który chcesz edytować");
                    int id = getInt();
                    System.out.println("Podaj nową nazwę");
                    String nazwa = sc.nextLine();
                    System.out.println("Podaj nową ładowność");
                    double ladownosc = getDouble();
                    System.out.println("Podaj nową pojemność");
                    double pojemnosc = getDouble();

                    if (mainFirma.editPojazd(id, nazwa, ladownosc, pojemnosc)) {
                        System.out.println("Pomyślnie zedytowano pojazd");
                        baza = writeFirma(mainFirma, baza);
                    } else System.out.println("Nie znaleziono pojazdu o podanym identyfikatorze");

                    sc.nextLine();
                    break;
                }
                case "9": {
                    cls();
                    System.out.println(mainFirma.listaKierowcow());
                    System.out.println("Podaj id kierowcy któego chcesz przypisać");
                    int kieroID = getInt();
                    System.out.println(mainFirma.listaPojazdow(true));
                    System.out.println("Podaj id pojazdu do któego chcesz dopisać kierowce");
                    int pojazdID = getInt();

                    if (mainFirma.kierowcaDoPojazdu(kieroID, pojazdID)) {
                        System.out.println("Pomyślnie przypisano kierowce do pojazdu");
                        baza = writeFirma(mainFirma, baza);
                    }
                    sc.nextLine();
                    break;
                }
                case "10": {
                    cls();
                    System.out.println(mainFirma.listaPojazdow(false));
                    System.out.println("Podaj id pojazdu, który wyjechał");
                    int id = getInt();
                    if (mainFirma.wyjazdPojazdu(id)) {
                        System.out.println("Pomyślnie odnotowano wyjazd pojazdu");
                        baza = writeFirma(mainFirma, baza);
                    }
                    sc.nextLine();
                    break;
                }
                case "11": {
                    cls();
                    System.out.println(mainFirma.listaPojazdow(false));
                    System.out.println("Podaj id pojazdu, który przyjechał");
                    int id = getInt();
                    if (mainFirma.przyjazdPojazdu(id)) {
                        System.out.println("Pomyślnie odnotowano przyjazd pojazdu");
                        baza = writeFirma(mainFirma, baza);
                    }
                    sc.nextLine();
                    break;
                }
                case "12": {
                    cls();
                    System.out.println("Podaj dane kierowcy, któego chcesz wyszukać");
                    String kierowcaMatch = sc.nextLine();
                    System.out.println(mainFirma.wyszukajKierowce(kierowcaMatch));
                    sc.nextLine();
                    break;
                }
                case "13": {
                    cls();
                    System.out.println("Podaj ładowność");
                    double ladownosc = getDouble();
                    System.out.println("Podaj pojemność");
                    double pojemnosc = getDouble();

                    System.out.println('\n' + mainFirma.wyszukajPojazd(ladownosc, pojemnosc));

                    sc.nextLine();
                    break;
                }
                case "14": {
                    cls();
                    String get = mainFirma.listWTrasie();
                    if (get.equals(""))
                        System.out.println("Brak pojazdów w trasie");
                    else
                        System.out.println("Pojazdy w trasie:\n" + get);

                    sc.nextLine();
                    break;
                }
                case "15": {
                    cls();
                    String get = mainFirma.sortujPojazdyWgKursow();
                    if (get.equals(""))
                        System.out.println("Brak pojazdów");
                    else
                        System.out.println("Pojazdy posortowane wg ilości kusrów (malejąco):\n" + get);

                    sc.nextLine();
                    break;
                }
                case "16": {
                    cls();
                    System.out.println("Umieść plik z kierowcami w folderze \"import\", stworzonym w katalogu, w którym wywołany został program" +
                            "\nFormat pliku wygląda następująco:\n\n" +
                            "imię1;nazwisko1\n" +
                            "imię2;nazwisko2\n" +
                            "imie3;nazwisko3\n\n" +
                            "Jeśli import się powiedzie, plik zostanie przeniesiony do katalogu \"imported\"\n\nEnter aby kontynuować");
                    importFolders(new File("./import"), new File("./imported"));
                    sc.nextLine();
                    cls();
                    System.out.println("Rozpoczynam import...\n");
                    if (importKierowcow(mainFirma, baza))
                        System.out.println("\nUkończono proces importowania\n" +
                                "Naciśnij Enter aby kontynuować");
                    else
                        System.out.println("Kierowcy nie zostali zaimporotwani");
                    sc.nextLine();
                    break;
                }

            }


        } while (!selector.equals("0"));
    }
}

