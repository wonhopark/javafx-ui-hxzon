package chartssampler;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.util.Duration;

/**
 * Basic BubbleChart Demo
 */
public class BubbleDemo extends ChartDemo<BubbleChart<Number,Number>> {

    public BubbleDemo() {
        super("Basic BubbleChart");
    }

    @Override protected BubbleChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BubbleChart<Number,Number> bc = new BubbleChart<Number,Number>(xAxis,yAxis);
        // setup chart
        bc.setTitle("Basic BubbleChart");
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");
        // add starting data
        XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();
        series1.setName("Data Series 1");
        for (int i=0; i<20; i++) series1.getData().add(
                new XYChart.Data<Number,Number>(Math.random()*100, Math.random()*100, (Math.random()*10)));
        XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
        series2.setName("Data Series 2");
        for (int i=0; i<20; i++) series2.getData().add(
                new XYChart.Data<Number,Number>(Math.random()*100, Math.random()*100, (Math.random()*10)));
        bc.getData().addAll(series1, series2);
        return bc;
    }

    @Override protected PropertySheet createPropertySheet() {
        final BubbleChart<Number,Number> bc = getChart();
        final NumberAxis xAxis = (NumberAxis)bc.getXAxis();
        final NumberAxis yAxis = (NumberAxis)bc.getYAxis();
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random()*(bc.getData().size()-1)));
                    if(s!=null) s.getData().add(
                            new BubbleChart.Data<Number,Number>(Math.random()*1000, Math.random()*1000, Math.random()*100));
                }
            }
        };
        EventHandler<ActionEvent> addDataItemNegative = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random()*(bc.getData().size()-1)));
                    if(s!=null) s.getData().add(
                            new BubbleChart.Data<Number,Number>(Math.random()*-1000, Math.random()*-1000, Math.random()*100));
                }
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random()*(bc.getData().size()-1)));
                    if(s!=null && !s.getData().isEmpty()) s.getData().remove((int) (Math.random() * (s.getData().size() - 1)));
                }
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) {
                    XYChart.Series<Number, Number> s = bc.getData().get((int)(Math.random()*(bc.getData().size())));
                    if(s!=null && !s.getData().isEmpty()) {
                        BubbleChart.Data<Number,Number> d = s.getData().get((int)(Math.random()*(s.getData().size())));
                        if (d!=null) {
                            d.setXValue(Math.random() * 1000);
                            d.setYValue(Math.random() * 1000);
                            d.setExtraValue(Math.random() * 100);
                        }
                    }
                }
            }
        };
        EventHandler<ActionEvent> addSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ScatterChart.Series<Number,Number> series = new ScatterChart.Series<Number,Number>();
                series.setName("Data Series 1");
                for (int i=0; i<10; i++) series.getData().add(
                        new BubbleChart.Data<Number,Number>(Math.random()*800, Math.random()*800, Math.random()*100));
                bc.getData().add(series);
            }
        };
        EventHandler<ActionEvent> deleteSeries = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!bc.getData().isEmpty()) bc.getData().remove((int)(Math.random()*(bc.getData().size()-1)));
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.valueOf(500), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: bc.getData()) {
                                for (XYChart.Data<Number, Number> data: series.getData()) {
                                    data.setXValue(Math.random()*1000);
                                    data.setYValue(Math.random()*1000);
                                }
                            }
                        }
                    })
                );
                tl.setCycleCount(30);
                tl.play();
            }
        };
        EventHandler<ActionEvent> animateDataFast = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                bc.setAnimated(false);
                Timeline tl = new Timeline();
                tl.getKeyFrames().add(
                    new KeyFrame(Duration.valueOf(50), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent actionEvent) {
                            for (XYChart.Series<Number, Number> series: bc.getData()) {
                                for (XYChart.Data<Number, Number> data: series.getData()) {
                                    data.setXValue(Math.random()*200);
                                    data.setYValue(Math.random()*200);
                                }
                            }
                        }
                    })
                );
                tl.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        bc.setAnimated(true);
                    }
                });
                tl.setCycleCount(100);
                tl.play();
            }
        };
        EventHandler<ActionEvent> removeAllData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                bc.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                ObservableList<XYChart.Series<Number,Number>> data = FXCollections.observableArrayList();
                for (int j=0; j<5;j++) {
                    BubbleChart.Series<Number,Number> series = new BubbleChart.Series<Number,Number>();
                    series.setName("Data Series "+j);
                    double x = 0;
                    for (int i=0; i<10; i++) {
                        x += Math.random()*100;
                        series.getData().add(new BubbleChart.Data<Number,Number>(x, Math.random()*800, Math.random()*100));
                    }
                    data.add(series);
                }
                bc.setData(data);
            }
        };
        // create property editor
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Add Data Item Negative",addDataItemNegative),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Add Series",addSeries),
                PropertySheet.createProperty("Delete Series",deleteSeries),
                PropertySheet.createProperty("Animate Data",animateData),
                PropertySheet.createProperty("Animate Data Fast",animateDataFast),
                PropertySheet.createProperty("Remove All Data",removeAllData),
                PropertySheet.createProperty("Set New Data",setNewData)
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
