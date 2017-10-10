package com.jb;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

public class LBChartCreator {
    private static final String X_AXIS_LABEL = "TTLB in ms (logarithmic scale base 2)";
    private static final String Y_AXIS_LABEL = "% of TTLB times in sample";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 740;

    public static File createChartFile(Map<LBClass, Map<Long, Double>> distribution, String fileName) throws IOException {
        File file = new File(fileName);
        ChartUtilities.saveChartAsPNG(file, getChart(distribution), WIDTH, HEIGHT);
        return file;
    }

    public static ByteArrayOutputStream createChartStream(Map<LBClass, Map<Long, Double>> distribution) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(out, getChart(distribution), WIDTH, HEIGHT);
        return out;
    }

    public static JFreeChart getChart(Map<LBClass, Map<Long, Double>> distribution) {
        IntervalXYDataset dataset = getIntervalXYDataset(distribution);
        JFreeChart xyBarChart = ChartFactory.createXYBarChart("Time to last byte distribution per object size class",
                X_AXIS_LABEL,
                false,
                Y_AXIS_LABEL,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        LogAxis axis = new LogAxis(X_AXIS_LABEL);
        axis.setBase(2);
        axis.setSmallestValue(1.0);
        axis.setNumberFormatOverride(NumberFormat.getIntegerInstance());
        xyBarChart.getXYPlot().setDomainAxis(axis);
        return xyBarChart;
    }

    public static IntervalXYDataset getIntervalXYDataset(Map<LBClass, Map<Long, Double>> distribution) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        distribution.forEach((lbClass, ttlbToCount) -> {
            XYSeries series = new XYSeries(lbClass.getDescription());
            ttlbToCount.forEach(series::add);
            dataset.addSeries(series);
        });
        return dataset;
    }
}
