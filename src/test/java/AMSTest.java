
import ams.AMSAverage;
import ams.AMSBase;
import ams.AMSMedian;
import org.junit.jupiter.api.Test;
import util.AMSPlotter;
import util.CalculationUtil;
import util.ExperimentResult;
import util.SeedGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Random;

class AMSTest {

    @Test
    public void testAlpha() throws Exception {
        ExperimentResult<AMSBase> experimentResults = ExperimentResult.getExperimentResults(AMSBase.class.getConstructor());
        long expectation =  CalculationUtil.calculateExpectation(experimentResults.getResults().stream());
        System.out.println("Used Mem=" + experimentResults.getMem());
        System.out.println("Expectation=" + expectation);
        System.out.println("Normalized Variance=" + CalculationUtil.calculateNormalizedVariance(experimentResults.getResults().stream(), expectation));
    }

    @Test
    public void testBetaFinalVersion() throws Exception {
        AMSPlotter plotter = new AMSPlotter();

        Constructor<AMSAverage> betaConstructor = AMSAverage.class.getConstructor(Integer.TYPE);
        ExperimentResult<AMSAverage> beta10 = ExperimentResult.getExperimentResults(betaConstructor, 1);
        ExperimentResult<AMSAverage> beta25 = ExperimentResult.getExperimentResults(betaConstructor, 2);
        ExperimentResult<AMSAverage> beta50 = ExperimentResult.getExperimentResults(betaConstructor, 3);
        plotter.addResultSeries("AMS Beta", beta10, beta25, beta50);

        Constructor<AMSMedian> finalConstructor = AMSMedian.class.getConstructor(Integer.TYPE, Integer.TYPE);
        ExperimentResult<AMSMedian> final1010 = ExperimentResult.getExperimentResults(finalConstructor, 1, 1);
        ExperimentResult<AMSMedian> final1025 = ExperimentResult.getExperimentResults(finalConstructor, 1, 2);
        ExperimentResult<AMSMedian> final1050 = ExperimentResult.getExperimentResults(finalConstructor, 1, 3);
        plotter.addResultSeries("AMS Final (10 Beta)", final1010, final1025, final1050);

        ExperimentResult<AMSMedian> final5010 = ExperimentResult.getExperimentResults(finalConstructor, 2, 1);
        ExperimentResult<AMSMedian> final5025 = ExperimentResult.getExperimentResults(finalConstructor, 2, 2);
        ExperimentResult<AMSMedian> final5050 = ExperimentResult.getExperimentResults(finalConstructor, 2, 3);
        plotter.addResultSeries("AMS Final (50 Beta)", final5010, final5025, final5050);

        plotter.plot();
    }

    @Test
    public void test() throws IOException {
        URL url = SeedGenerator.class.getClassLoader().getResource("seeds.txt");
        assert url != null;
        Random random = new Random();
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("/Users/michaldeutch/IdeaProjects/AMS/src/main/resources/seeds.txt"));
        for (int i=0; i<1000000; i++) {
            outputWriter.write(random.nextInt()+"");
            outputWriter.newLine();
        }
    }

}