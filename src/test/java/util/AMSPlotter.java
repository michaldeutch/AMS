package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class AMSPlotter {
    private final List<XYSeries> expectations = new ArrayList<>();
    private final List<XYSeries> normalizedVariances = new ArrayList<>();
    private final List<XYSeries> usedMem = new ArrayList<>();
    private final int[] copies;

    public AMSPlotter(int[] copies) {
        this.copies = copies;
        try {
            XYSeries F2series = new XYSeries("Real F2");
            long realF2 = HarryPotter.calculateF2();
            for (int i: copies) {
                F2series.add(i, realF2);
            }
            expectations.add(F2series);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public final void addResultSeries(String name, ExperimentResult<?>... results) throws IOException, URISyntaxException {
        XYSeries expectationSeries = new XYSeries(name);
        XYSeries normalizedVarianceSeries = new XYSeries(name);
        XYSeries usedMemSeries = new XYSeries(name);
        int copyInd = 0;
        for (ExperimentResult<?> result : results) {
            long expectation = CalculationUtil.calculateExpectation(result.getResults().stream());
            double normalizedVariance = CalculationUtil.calculateNormalizedVariance(result.getResults());
            expectationSeries.add(copies[copyInd], expectation);
            normalizedVarianceSeries.add(copies[copyInd], Math.abs(normalizedVariance));
            usedMemSeries.add(copies[copyInd], result.getMem());
            copyInd++;
        }
        expectations.add(expectationSeries);
        normalizedVariances.add(normalizedVarianceSeries);
        usedMem.add(usedMemSeries);
    }

    public void plot() throws IOException {
        plot("Expectation", getDataset(expectations));
        plot("Normalized-Variance (Absolute)", getDataset(normalizedVariances));
        plot("Used Memory", getDataset(usedMem));
    }

    private void plot(String chartTile, XYDataset dataset) throws IOException {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTile,
                "Number Of Copies",
                "Result",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File XYChart = new File( chartTile + ".jpeg" );
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
    }

    private XYSeriesCollection getDataset(List<XYSeries> series) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        series.forEach(dataset::addSeries);
        return dataset;
    }

}
