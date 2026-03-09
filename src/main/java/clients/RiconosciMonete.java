package clients;

import macchinette.*;
import java.util.Scanner;

public class RiconosciMonete {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                Importo imp = InputUtils.parseImporto(line);
                boolean valid = false;
                for (Moneta m : Moneta.values()) {
                    if (m.getValore().equals(imp)) {
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    System.out.println(imp);
                } else {
                    System.out.println("invalid-amount");
                }
            } catch (Exception e) {
                System.out.println("invalid-amount");
            }
        }
    }
}