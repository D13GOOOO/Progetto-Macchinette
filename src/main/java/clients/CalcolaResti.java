package clients;

import macchinette.*;
import java.util.Scanner;

public class CalcolaResti {
    public static void main(String[] args) {
        StrategiaResto strategia = args[0].equals("H") ? new StrategiaMinima() : new StrategiaMassima();
        Importo resto = InputUtils.parseImporto(args[1]);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            try {
                Aggregato cassa = InputUtils.parseAggregatoStar(sc.nextLine());

                if (cassa.getValoreTotale().compareTo(resto) < 0) {
                    System.out.println("insufficient-value");
                } else {
                    try {
                        System.out.println(strategia.calcolaResto(resto, cassa));
                    } catch (ChangeNotPossibleException e) {
                        System.out.println("change-not-possible");
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}