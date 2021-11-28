package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class HarryPotter {
    private static final AtomicLong distinctWords = new AtomicLong(0);
    private static final AtomicLong f2 = new AtomicLong(0);

    public static Stream<String> getWordsStream() throws IOException, URISyntaxException {
        URL book = Thread.currentThread().getContextClassLoader().getResource("hp.txt");
        Path bookPath = Paths.get(book.toURI());
        return Files.lines(bookPath)
                .parallel()
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .filter(word -> !word.isEmpty());
    }

    public static long countWords() throws IOException, URISyntaxException {
        return getWordsStream().count();
    }

    public static long calculateF2() throws IOException, URISyntaxException {
        return f2.updateAndGet(val -> {
            Map<String, Integer> frequencies = new ConcurrentHashMap<>();
            try {
                if (val > 0) return val;
                getWordsStream().parallel()
                        .forEach(word -> frequencies.compute(word, (w, freq) -> freq == null ? 1 : freq + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return frequencies.values().parallelStream().mapToLong(f -> Math.round(Math.pow(f, 2))).sum();
        });
    }

    public static long countDistinctWords() throws IOException, URISyntaxException {
        return distinctWords.updateAndGet(l -> {
            try {
                if (l == 0) l = getWordsStream().distinct().count();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return l;
        });
    }

}
