package clients;

import macchinette.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UsaDistributore {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        List<Binario> listaBinari = new ArrayList<>();
        try {
            String line1 = sc.nextLine().trim();
            String[] bins = line1.split(",");
            for (String bs : bins) {
                String[] v = bs.trim().split("\\|");
                listaBinari.add(new Binario(Integer.parseInt(v[0].trim()), InputUtils.parseTaglia(v[1].trim())));
            }
        } catch (Exception e) {
            return;
        }
        Aggregato cassa = new Aggregato();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals(".")) break;
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                try {
                    int q = Integer.parseInt(parts[0]);
                    Importo val = InputUtils.parseImporto(parts[1]);
                    for (Moneta m : Moneta.values()) {
                        if (m.getValore().equals(val)) {
                            cassa.aggiungiMoneta(m, q);
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        String line3 = sc.hasNextLine() ? sc.nextLine().trim() : "";
        StrategiaResto str;
        if (line3.equals("H")) {
            str = new StrategiaMinima();
        } else if (line3.equals("L")) {
            str = new StrategiaMassima();
        } else {
            str = new StrategiaMista();
        }
        Distributore d = new Distributore(listaBinari, cassa, str);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("+ ")) {
                try {
                    String content = line.substring(2).trim();
                    String[] parts = content.split("!");
                    if (parts.length >= 2) {
                        int qta = Integer.parseInt(parts[0].trim());
                        Prodotto p = InputUtils.parseProdottoSemicolon(parts[1].trim());
                        int avanzati = d.carica(p, qta);
                        System.out.println("+ " + avanzati);
                    }
                } catch (Exception e) {
                }
            } else if (line.startsWith("- ")) {
                try {
                    String content = line.substring(2).trim();
                    String[] parts = content.split("!");
                    if (parts.length >= 2) {
                        int idx = Integer.parseInt(parts[0].trim());
                        Aggregato pag = InputUtils.parseAggregatoStar(parts[1].trim());
                        Aggregato resto = d.eroga(idx, pag);
                        System.out.println("- " + resto);
                    }
                } catch (ChangeNotPossibleException e) {
                    System.out.println("- change");
                } catch (DistributoreException e) {
                    System.out.println("- " + e.getMessage());
                } catch (Exception e) {
                }
            } else if (line.equals("?")) {
                for (int i = 0; i < listaBinari.size(); i++) {
                    try {
                        Binario b = d.getBinario(i);
                        if (!b.isVuoto()) {
                            System.out.println("? " + i + " | " + b.peek().getNome() + " | " + b.peek().getPrezzo());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}