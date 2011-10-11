/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javafx.experiments.ensemble2.samples.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import com.javafx.experiments.ensemble2.Sample;

/**
 * BubbleChart - A Chart that plots bubbles for data points in a series.
 *
 * @see javafx.scene.chart.BubbleChart
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.NumberAxis
 */
public class BubbleChartSample extends Sample {

    public BubbleChartSample() {
        NumberAxis xAxis = new NumberAxis("X", 0d, 150d, 20d);

        NumberAxis yAxis = new NumberAxis("Y", 0d, 150d, 20d);

        ObservableList<BubbleChart.Series> bubbleChartData = FXCollections.observableArrayList(
                new BubbleChart.Series("Series 1", FXCollections.observableArrayList(new XYChart.Data(30d, 40d, 10d), new XYChart.Data(60d, 20d, 13d), new XYChart.Data(10d, 90d, 7d),
                        new XYChart.Data(100d, 40d, 10d), new XYChart.Data(50d, 23d, 5d))),
                new BubbleChart.Series("Series 2", FXCollections.observableArrayList(new XYChart.Data(13d, 100d, 7d), new XYChart.Data(20d, 80d, 13d), new XYChart.Data(100d, 60d, 10d),
                        new XYChart.Data(30d, 40d, 6d), new XYChart.Data(50d, 20d, 12d))));

        BubbleChart chart = new BubbleChart(xAxis, yAxis, bubbleChartData);
        getChildren().add(chart);
    }
}
