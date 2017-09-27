package com.jb;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

public class LBChartCreator {

    public static final String X_AXIS_LABEL = "TTLB in ms (logarithmic scale base 2)";
    public static final String Y_AXIS_LABEL = "% of TTLB times in sample";

    public static File createXYChart(Map<LBClass, Map<Long, Double>> distribution, String fileName) throws IOException {
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
        File file = new File(fileName);
        ChartUtilities.saveChartAsPNG(file, xyBarChart, 1024, 740);
        return file;
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
