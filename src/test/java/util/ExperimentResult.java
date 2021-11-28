package util;

import ams.AMS;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ExperimentResult<T extends AMS> {
    private static final int RUN_EXPERIMENT_TIMES = 50;

    private final List<Long> results;
    private final long mem;

    public ExperimentResult(List<Long> results, long mem) {
        this.results = results;
        this.mem = mem;
    }

    public static <T extends AMS> ExperimentResult<T> getExperimentResults(Constructor<T> amsConstructor, Object... k) throws Exception{
        List<Long> results = getExperimentParallelStream(amsConstructor, k)
                .map(ExperimentResult::process)
                .collect(toList());
        System.out.println("******* Finished processing "
                + amsConstructor.getName() + ", params=" + Arrays.stream(k).toList());
        return new ExperimentResult<>(results, getNumOfBaseEstimators(k));
    }

    public List<Long> getResults() {
        return results;
    }

    public long getMem() {
        return mem;
    }

    private static <T extends AMS> Stream<T> getExperimentParallelStream(Constructor<T> constructor, Object... k) throws Exception{
        List<T> experiments = new ArrayList<>(RUN_EXPERIMENT_TIMES);
        for (int i=0; i < RUN_EXPERIMENT_TIMES; i++) {
            experiments.add(constructor.newInstance(k));
        }
        return experiments.parallelStream();
    }

    private static long process(AMS ams) {
        try {
            return ams.process();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getNumOfBaseEstimators(Object... k) {
        int mul = 1;
        for(int i=0; i<k.length; i++) {
            mul *= (int)k[i];
        }
        return mul;
    }

}
