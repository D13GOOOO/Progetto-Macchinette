package clients;

import macchinette.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class OrdinaProdotti {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Prodotto> list = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                list.add(InputUtils.parseProdotto(line));
            } catch (Exception e) {
            }
        }
        Collections.sort(list);
        for (Prodotto p : list) System.out.println(p);
    }
}