package ams;

import java.util.LinkedList;
import java.util.List;

public class AMSAverage implements AMS {
    private final List<AMSBase> ams = new LinkedList<>();

    public AMSAverage(int alphaEstimators) {
        for (int i=0; i<alphaEstimators; i++) {
            ams.add(new AMSBase());
        }
    }

    @Override
    public long process() throws Exception {
        return Math.round(ams.parallelStream().mapToLong(ams -> {
            try {
                return ams.process();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).average().getAsDouble());
    }
}
