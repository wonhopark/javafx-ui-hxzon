/*
 * Copyright (c) 2008, 2011 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package chartssampler;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Basic StackedBarChart Demo
 */
public class StackedBarDemo extends ChartDemo<StackedBarChart<String,Number>> {

    public StackedBarDemo() {
        super("Stacked Bar Chart");
    }

    @Override protected StackedBarChart<String, Number> createChart() {
        final String[] years = {"2007", "2008", "2009"};
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,"$",null));
        final StackedBarChart<String,Number> bc = new StackedBarChart<String,Number>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Stacked Bar Chart");
        xAxis.setLabel("Year");
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
        series1.setName("Data Series 1");
        XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();
        series2.setName("Data Series 2");
        XYChart.Series<String,Number> series3 = new XYChart.Series<String,Number>();
        series3.setName("Data Series 3");
        // create sample data
        series1.getData().add(new XYChart.Data<String,Number>(years[0], 567));
        series1.getData().add(new XYChart.Data<String,Number>(years[1], 1292));
        series1.getData().add(new XYChart.Data<String,Number>(years[2], 2180));
        series2.getData().add(new XYChart.Data<String,Number>(years[0], 956));
        series2.getData().add(new XYChart.Data<String,Number>(years[1], 1665));
        series2.getData().add(new XYChart.Data<String,Number>(years[2], 2450));
        series3.getData().add(new XYChart.Data<String,Number>(years[0], 800));
        series3.getData().add(new XYChart.Data<String,Number>(years[1], 1000));
        series3.getData().add(new XYChart.Data<String,Number>(years[2], 2800));
        bc.getData().add(series1);
        bc.getData().add(series2);
        bc.getData().add(series3);
        return bc;
    }

    @Override protected PropertySheet createPropertySheet() {
        // create actions
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!getChart().getData().isEmpty()) {
                    XYChart.Series<String, Number> s = getChart().getData().get((int)(Math.random()*(getChart().getData().size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        XYChart.Data<String, Number> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setYValue(Math.random()*1500d);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
                series.setName("Data Series 1");
                CategoryAxis cAxis = ((CategoryAxis)getChart().getXAxis());
                for (String category : cAxis.getCategories()) {
                    series.getData().add(new XYChart.Data<String,Number>(category, Math.random()*3800));
                }
                getChart().getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!getChart().getData().isEmpty()) getChart().getData().remove((int)(Math.random()*(getChart().getData().size()-1)));
            }
        };
        // create property sheet
        StackedBarChart<String,Number> bc = getChart();
        final CategoryAxis xAxis = (CategoryAxis)bc.getXAxis();
        final NumberAxis yAxis = (NumberAxis)bc.getYAxis();
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Add Series",addSeries),
                PropertySheet.createProperty("Delete Series",deleteSeries)
            ),
            new PropertySheet.PropertyGroup("Chart Properties",
                PropertySheet.createProperty("Title",bc.titleProperty()),
                PropertySheet.createProperty("Title Side",bc.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",bc.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("XY Chart Properties",
                PropertySheet.createProperty("Vertical Grid Line Visible",bc.verticalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Horizontal Grid Line Visible",bc.horizontalGridLinesVisibleProperty()),
                PropertySheet.createProperty("Alternative Column Fill Visible",bc.alternativeColumnFillVisibleProperty()),
                PropertySheet.createProperty("Alternative Row Fill Visible",bc.alternativeRowFillVisibleProperty()),
                PropertySheet.createProperty("Vertical Zero Line Visible",bc.verticalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Horizontal Zero Line Visible",bc.horizontalZeroLineVisibleProperty()),
                PropertySheet.createProperty("Animated",bc.animatedProperty())
            ),
            new PropertySheet.PropertyGroup("X Axis Properties",
                PropertySheet.createProperty("Side",xAxis.sideProperty()),
                PropertySheet.createProperty("Label",xAxis.labelProperty()),
                PropertySheet.createProperty("Label",xAxis.labelProperty()),
                PropertySheet.createProperty("Tick Mark Length",xAxis.tickLengthProperty()),
                PropertySheet.createProperty("Auto Ranging",xAxis.autoRangingProperty()),
                PropertySheet.createProperty("Tick Label Font",xAxis.tickLabelFontProperty()),
                PropertySheet.createProperty("Tick Label Fill",xAxis.tickLabelFillProperty()),
                PropertySheet.createProperty("Tick Label Gap",xAxis.tickLabelGapProperty()),
                // Value Axis Props
                PropertySheet.createProperty("Scale",yAxis.scaleProperty()),
                PropertySheet.createProperty("Lower Bound",yAxis.lowerBoundProperty()),
                PropertySheet.createProperty("Upper Bound",yAxis.upperBoundProperty()),
                PropertySheet.createProperty("Tick Label Formatter",yAxis.tickLabelFormatterProperty()),
                PropertySheet.createProperty("Minor Tick Length",yAxis.minorTickLengthProperty()),
                PropertySheet.createProperty("Minor Tick Count",yAxis.minorTickCountProperty()),
                // Number Axis Properties
                PropertySheet.createProperty("Force Zero In Range",yAxis.forceZeroInRangeProperty()),
                PropertySheet.createProperty("Tick Unit",yAxis.tickUnitProperty()),
                // Category Axis Properties
                PropertySheet.createProperty("Start Margin", xAxis.startMarginProperty()),
                PropertySheet.createProperty("End Margin", xAxis.endMarginProperty()),
                PropertySheet.createProperty("Gap Start And End", xAxis.gapStartAndEndProperty())
            ),
            new PropertySheet.PropertyGroup("Y Axis Properties",
                PropertySheet.createProperty("Side",yAxis.sideProperty()),
                PropertySheet.createProperty("Label",yAxis.labelProperty()),
                PropertySheet.createProperty("Label",yAxis.labelProperty()),
                PropertySheet.createProperty("Tick Mark Length",yAxis.tickLengthProperty()),
                PropertySheet.createProperty("Auto Ranging",yAxis.autoRangingProperty()),
                PropertySheet.createProperty("Tick Label Font",yAxis.tickLabelFontProperty()),
                PropertySheet.createProperty("Tick Label Fill",yAxis.tickLabelFillProperty()),
                PropertySheet.createProperty("Tick Label Gap",yAxis.tickLabelGapProperty()),
                // Value Axis Props
                PropertySheet.createProperty("Scale",yAxis.scaleProperty()),
                PropertySheet.createProperty("Lower Bound",yAxis.lowerBoundProperty()),
                PropertySheet.createProperty("Upper Bound",yAxis.upperBoundProperty()),
                PropertySheet.createProperty("Tick Label Formatter",yAxis.tickLabelFormatterProperty()),
                PropertySheet.createProperty("Minor Tick Length",yAxis.minorTickLengthProperty()),
                PropertySheet.createProperty("Minor Tick Count",yAxis.minorTickCountProperty()),
                // Number Axis Properties
                PropertySheet.createProperty("Force Zero In Range",yAxis.forceZeroInRangeProperty()),
                PropertySheet.createProperty("Tick Unit",yAxis.tickUnitProperty())
            )
        );
    }
}
