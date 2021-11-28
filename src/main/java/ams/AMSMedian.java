package ams;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class AMSMedian implements AMS {
    private final List<AMSAverage> ams = new LinkedList<>();

    public AMSMedian(int betaEstimators, int alphaEstimators) {
        for (int i=0; i<betaEstimators; i++) {
            ams.add(new AMSAverage(alphaEstimators));
        }
    }

    @Override
    public long process() throws Exception {
        int size = ams.size();
        return Math.round(ams.parallelStream().mapToLong(ams -> {
            try {
                return ams.process();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).sorted().skip((size-1)/2).limit(2-size%2).average().getAsDouble());
    }

}
