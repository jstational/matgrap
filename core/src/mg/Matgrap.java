package mg;

import java.nio.file.*;
import java.util.*;

public class Matgrap {
    public static void main(String[] args) {
        if(args.length >= 1) {
            float lpi = (float) Math.PI;
            float le = (float) Math.E;

            String fileName = args[0];
            Path file = Path.of(fileName);

            String[] mod = Arrays.copyOfRange(args, 1, args.length);

            Optional<String> pimod = Arrays.stream(mod).filter(s -> s != null && s.startsWith("/pi")).findFirst();
            Optional<String> emod = Arrays.stream(mod).filter(s -> s != null && s.startsWith("/e")).findFirst();

            pimod.ifPresent(dec -> {
                String newpi = dec.substring(3);
                try {
                    lpi = Float.parseFloat(newpi);
                } catch(NumberFormatException err) {}
            });

            emod.ifPresent(dec -> {
                String newe = dec.substring(3);
                try {
                    le = Float.parseFloat(newe);
                } catch(NumberFormatException err) {}
            });

            Vars.pi = lpi;
            Vars.e = le;
        }
    }
}