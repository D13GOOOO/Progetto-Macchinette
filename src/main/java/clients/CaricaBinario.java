package clients;

import macchinette.*;
import java.util.Scanner;

public class CaricaBinario {
    public static void main(String[] args) {
        if (args.length < 2) return;
        try {
            int cap = Integer.parseInt(args[0]);
            Taglia sz = InputUtils.parseTaglia(args[1]);
            Binario b = new Binario(cap, sz);
            System.out.println(b);
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                try {
                    String[] parts = sc.nextLine().split(";");
                    int qta = Integer.parseInt(parts[0].trim());
                    Prodotto p = InputUtils.parseProdottoAt(parts[1].trim());
                    b.carica(p, qta);
                    System.out.println(b);
                } catch (SizeMismatchException e) {
                    System.out.println("size");
                } catch (CapacityExceededException e) {
                    System.out.println("capacity");
                } catch (ItemMismatchException e) {
                    System.out.println("item");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}