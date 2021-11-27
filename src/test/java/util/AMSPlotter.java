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
    private final int[] copies = {10, 25, 50};

    @SafeVarargs
    public final void addResultSeries(String name, ExperimentResult<?>... results) throws IOException, URISyntaxException {
        XYSeries expectationSeries = new XYSeries(name);
        XYSeries normalizedVarianceSeries = new XYSeries(name);
        XYSeries usedMemSeries = new XYSeries(name);
        int copyInd = 0;
        for (ExperimentResult<?> result : results) {
            long expectation = CalculationUtil.calculateExpectation(result.getResults().stream());
            long normalizedVariance = CalculationUtil.calculateNormalizedVariance(result.getResults().stream(), expectation);
            expectationSeries.add(copies[copyInd], expectation);
            normalizedVarianceSeries.add(copies[copyInd], normalizedVariance);
            usedMemSeries.add(copies[copyInd], result.getMem());
            copyInd++;
        }
        expectations.add(expectationSeries);
        normalizedVariances.add(normalizedVarianceSeries);
        usedMem.add(usedMemSeries);
    }

    public void plot() throws IOException {
        plot("Expectation", getDataset(expectations));
        plot("Normalized-Variance", getDataset(normalizedVariances));
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
