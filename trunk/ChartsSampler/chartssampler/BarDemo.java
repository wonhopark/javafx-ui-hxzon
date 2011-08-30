package chartssampler;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import java.util.Arrays;

/**
 * Basic BarChart Demo
 */
public class BarDemo extends ChartDemo<BarChart<String,Number>> {

    public BarDemo() {
        super("Basic BarChart");
    }

    @Override protected BarChart<String, Number> createChart() {
        final String[] years = {"2007", "2008", "2009"};
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,"$",null));
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Basic BarChart");
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
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (getChart().getData() == null) getChart().setData(FXCollections.<XYChart.Series<String, Number>>observableArrayList());
                if(getChart().getData().size() > 0) {
                    int randomYear = 1900 + (int)(Math.round(12*Math.random()) * 10);
                    XYChart.Series<String,Number> series = getChart().getData().get((int)(Math.random()*getChart().getData().size()));
                    series.getData().add(new XYChart.Data<String, Number>(Integer.toString(randomYear), 10+(Math.random()*3800)));
                }
            }
        };
        EventHandler<ActionEvent> addNegativeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (getChart().getData() == null) getChart().setData(FXCollections.<XYChart.Series<String, Number>>observableArrayList());
                if(getChart().getData().size() > 0) {
                    int randomYear = 1900 + (int)(Math.round(12*Math.random()) * 10);
                    XYChart.Series<String,Number> series = getChart().getData().get((int)(Math.random()*getChart().getData().size()));
                    series.getData().add(new XYChart.Data<String, Number>(Integer.toString(randomYear), -10+(Math.random()*-1000)));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!getChart().getData().isEmpty()) {
                    XYChart.Series<String, Number> s = getChart().getData().get((int)(Math.random()*(getChart().getData().size()-1)));
                    if(s!=null && !s.getData().isEmpty()) s.getData().remove((int) (Math.random() * (s.getData().size() - 1)));
                }
            }
        };
        // create property sheet
        BarChart<String,Number> bc = getChart();
        final CategoryAxis xAxis = (CategoryAxis)bc.getXAxis();
        final NumberAxis yAxis = (NumberAxis)bc.getYAxis();
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Add Negative Data Item",addNegativeDataItem),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
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
