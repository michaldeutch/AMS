
import ams.AMSAverage;
import ams.AMSBase;
import ams.AMSMedian;
import org.junit.jupiter.api.Test;
import util.AMSPlotter;
import util.CalculationUtil;
import util.ExperimentResult;
import java.lang.reflect.Constructor;

class AMSTest {
    public static final int[] copies = {10, 25, 50};

    @Test
    public void testAlpha() throws Exception {
        ExperimentResult<AMSBase> experimentResults = ExperimentResult.getExperimentResults(AMSBase.class.getConstructor());
        long expectation =  CalculationUtil.calculateExpectation(experimentResults.getResults().stream());
        System.out.println("Used Mem=" + experimentResults.getMem());
        System.out.println("Expectation=" + expectation);
        System.out.println("Normalized Variance=" + CalculationUtil.calculateNormalizedVariance(experimentResults.getResults()));
    }

    @Test
    public void testBetaFinalVersion() throws Exception {
        AMSPlotter plotter = new AMSPlotter(copies);

        Constructor<AMSAverage> betaConstructor = AMSAverage.class.getConstructor(Integer.TYPE);
        ExperimentResult<AMSAverage> beta10 = ExperimentResult.getExperimentResults(betaConstructor, copies[0]);
        ExperimentResult<AMSAverage> beta25 = ExperimentResult.getExperimentResults(betaConstructor, copies[1]);
        ExperimentResult<AMSAverage> beta50 = ExperimentResult.getExperimentResults(betaConstructor, copies[2]);
        plotter.addResultSeries("AMS Beta", beta10, beta25, beta50);

        Constructor<AMSMedian> finalConstructor = AMSMedian.class.getConstructor(Integer.TYPE, Integer.TYPE);
        ExperimentResult<AMSMedian> final1010 = ExperimentResult.getExperimentResults(finalConstructor, 10, copies[0]);
        ExperimentResult<AMSMedian> final1025 = ExperimentResult.getExperimentResults(finalConstructor, 10, copies[1]);
        ExperimentResult<AMSMedian> final1050 = ExperimentResult.getExperimentResults(finalConstructor, 10, copies[2]);
        plotter.addResultSeries("AMS Final (10 Beta)", final1010, final1025, final1050);

        ExperimentResult<AMSMedian> final5010 = ExperimentResult.getExperimentResults(finalConstructor, 50, copies[0]);
        ExperimentResult<AMSMedian> final5025 = ExperimentResult.getExperimentResults(finalConstructor, 50, copies[1]);
        ExperimentResult<AMSMedian> final5050 = ExperimentResult.getExperimentResults(finalConstructor, 50, copies[2]);
        plotter.addResultSeries("AMS Final (50 Beta)", final5010, final5025, final5050);

        plotter.plot();
    }
}