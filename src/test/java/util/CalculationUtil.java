package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public enum CalculationUtil {;
    public static long calculateExpectation(Stream<Long> experiments) {
        return Math.round(experiments.mapToLong(i -> i).average().getAsDouble());
    }

    public static long calculateNormalizedVariance(Stream<Long> experiments, long expectation) throws IOException, URISyntaxException {
        long expectationOfSquares = calculateExpectation(experiments.map(l -> (long) Math.pow(l, 2)));
        long expectationSquared = (long) Math.pow(expectation, 2);
        long n = Math.round(Math.pow(HarryPotter.countDistinctWords(), 2));
        return (expectationOfSquares - expectationSquared) / n;
    }

}
