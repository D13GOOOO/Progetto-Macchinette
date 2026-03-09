package clients;

import macchinette.*;
import java.util.Scanner;

public class OperazioniAggregati {
    public static void main(String[] args) {
        Aggregato agg = new Aggregato();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            try {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                char op = line.charAt(0);
                Aggregato delta = InputUtils.parseAggregato(line.substring(1).trim());
                if (op == '+') {
                    agg.aggiungi(delta);
                    System.out.println(agg);
                } else if (op == '-') {
                    try {
                        agg.rimuovi(delta);
                        System.out.println(agg);
                    } catch (MissingValueException e) {
                        System.out.println("missing-value");
                    } catch (MissingCoinsException e) {
                        System.out.println("missing-coins");
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}