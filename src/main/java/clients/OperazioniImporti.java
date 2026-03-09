package clients;

import macchinette.*;
import java.util.Scanner;

public class OperazioniImporti {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                if (line.contains(" + ")) {
                    String[] parts = line.split(" \\+ ");
                    Importo i1 = InputUtils.parseImporto(parts[0]);
                    Importo i2 = InputUtils.parseImporto(parts[1]);
                    System.out.println(i1.somma(i2));
                } else if (line.contains(" - ")) {
                    String[] parts = line.split(" - ");
                    Importo i1 = InputUtils.parseImporto(parts[0]);
                    Importo i2 = InputUtils.parseImporto(parts[1]);
                    try {
                        System.out.println(i1.sottrazione(i2));
                    } catch (NegativeResultException e) {
                        System.out.println("negative-result");
                    }
                } else if (line.contains(" * ")) {
                    String[] parts = line.split(" \\* ");
                    Importo i1 = InputUtils.parseImporto(parts[0]);
                    int n = Integer.parseInt(parts[1]);
                    try {
                        System.out.println(i1.moltiplicazione(n));
                    } catch (IllegalArgumentException e) {
                        System.out.println("negative-result");
                    }
                } else if (line.contains(" / ")) {
                    String[] parts = line.split(" / ");
                    Importo i1 = InputUtils.parseImporto(parts[0]);
                    Importo i2 = InputUtils.parseImporto(parts[1]);
                    System.out.println(i1.divisione(i2));
                } else {
                    System.out.println("invalid-result");
                }
            } catch (Exception e) {
                System.out.println("invalid-result");
            }
        }
    }
}