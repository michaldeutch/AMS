package util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class SeedGenerator {
    private static Iterator<Integer> seeds;

    static {
        initStream();
    }

    public static synchronized int getSeed() {
        try {
            return seeds.next();
        } catch (Exception e) {
            System.out.println("got error in getSeed() " + e);
            initStream();
            return seeds.next();
        }
    }

    public static void initStream() {
        URL url = SeedGenerator.class.getClassLoader().getResource("seeds.txt");
        assert url != null;
        try {
            Stream<String> stream = Files.lines(Paths.get(Paths.get(url.toURI()).toString()));
            seeds = stream.map(Integer::parseInt).iterator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
