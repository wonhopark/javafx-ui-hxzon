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
 * Horizontal BarChart Demo
 */
public class BarHorizontalDemo extends ChartDemo<BarChart<Number,String>> {

    public BarHorizontalDemo() {
        super("Horizontal");
    }

    @Override protected BarChart<Number, String> createChart() {
        final String[] years = {"2007", "2008", "2009"};
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        final BarChart<Number,String> bc = new BarChart<Number,String>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Bar Chart Example");
        yAxis.setLabel("Price");
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(years)));
        xAxis.setLabel("Year");
        // add starting data
        XYChart.Series<Number,String> series1 = new XYChart.Series<Number,String>();
        series1.setName("Data Series 1");
        XYChart.Series<Number,String> series2 = new XYChart.Series<Number,String>();
        series2.setName("Data Series 2");
        XYChart.Series<Number,String> series3 = new XYChart.Series<Number,String>();
        series3.setName("Data Series 3");
        series1.getData().add(new XYChart.Data<Number,String>(567, years[0]));
        series1.getData().add(new XYChart.Data<Number,String>(1292, years[1]));
        series1.getData().add(new XYChart.Data<Number,String>(2180, years[2]));
        series2.getData().add(new XYChart.Data<Number,String>(956, years[0]));
        series2.getData().add(new XYChart.Data<Number,String>(1665, years[1]));
        series2.getData().add(new XYChart.Data<Number,String>(2450, years[2]));
        series3.getData().add(new XYChart.Data<Number,String>(800, years[0]));
        series3.getData().add(new XYChart.Data<Number,String>(1000, years[1]));
        series3.getData().add(new XYChart.Data<Number,String>(2800, years[2]));
        bc.getData().add(series1);
        bc.getData().add(series2);
        bc.getData().add(series3);
        return bc;
    }

    @Override protected PropertySheet createPropertySheet() {
        final BarChart<Number,String> bc = getChart();
        final NumberAxis xAxis = (NumberAxis)bc.getXAxis();
        final CategoryAxis yAxis = (CategoryAxis)bc.getYAxis();
        // create actions
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, String> s = bc.getData().get((int)(Math.random()*(bc.getData().size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        XYChart.Data<Number, String> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setXValue(Math.random()*1500d);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                XYChart.Series<Number,String> series = new XYChart.Series<Number,String>();
                series.setName("Data Series 1");
                CategoryAxis cAxis = ((CategoryAxis)bc.getYAxis());
                for (String category : cAxis.getCategories()) {
                    series.getData().add(new XYChart.Data<Number,String>(Math.random()*3800, category));
                }
                bc.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) bc.getData().remove((int)(Math.random()*(bc.getData().size()-1)));
            }
        };
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (bc.getData() == null) bc.setData(FXCollections.<XYChart.Series<Number,String>>observableArrayList());
                if(bc.getData().size() > 0) {
                    int randomYear = 1900 + (int)(Math.round(12*Math.random()) * 10);
                    XYChart.Series<Number,String> series = bc.getData().get((int)(Math.random()*bc.getData().size()));
                    series.getData().add(new XYChart.Data<Number, String>(10+(Math.random()*3800), Integer.toString(randomYear)));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, String> s = bc.getData().get((int)(Math.random()*(bc.getData().size()-1)));
                    if(s!=null && !s.getData().isEmpty()) s.getData().remove((int) (Math.random() * (s.getData().size() - 1)));
                }
            }
        };
        // create property sheet
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
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
                PropertySheet.createProperty("Scale",xAxis.scaleProperty()),
                PropertySheet.createProperty("Lower Bound",xAxis.lowerBoundProperty()),
                PropertySheet.createProperty("Upper Bound",xAxis.upperBoundProperty()),
                PropertySheet.createProperty("Tick Label Formatter",xAxis.tickLabelFormatterProperty()),
                PropertySheet.createProperty("Minor Tick Length",xAxis.minorTickLengthProperty()),
                PropertySheet.createProperty("Minor Tick Count",xAxis.minorTickCountProperty()),
                // Number Axis Properties
                PropertySheet.createProperty("Force Zero In Range",xAxis.forceZeroInRangeProperty()),
                PropertySheet.createProperty("Tick Unit",xAxis.tickUnitProperty())
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
                // Category Axis Properties
                PropertySheet.createProperty("Start Margin", yAxis.startMarginProperty()),
                PropertySheet.createProperty("End Margin", yAxis.endMarginProperty()),
                PropertySheet.createProperty("Gap Start And End", yAxis.gapStartAndEndProperty())
            )
        );
    }
}
