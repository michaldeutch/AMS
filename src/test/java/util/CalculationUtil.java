package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

public enum CalculationUtil {;
    public static long calculateExpectation(Stream<Long> experiments) {
        return Math.round(experiments.mapToLong(i -> i).average().getAsDouble());
    }

    public static double calculateNormalizedVariance(List<Long> experiments) throws IOException, URISyntaxException {
        double sumDif2 = 0.0;
        double f2 = HarryPotter.calculateF2();
        for (long val: experiments) {
            double normalized = val / f2;
            double diff = normalized - 1.0;
            diff *= diff;
            sumDif2 += diff;
        }
        return sumDif2 / (experiments.size() > 1? experiments.size() - 1 : 1.0);
    }

}
