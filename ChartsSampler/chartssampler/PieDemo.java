package chartssampler;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.util.Duration;

/**
 * Pie Chart Demo
 */
public class PieDemo extends ChartDemo<PieChart> {
    private int itemNameIndex = 1;

    public PieDemo() {
        super("Basic PieChart");
    }

    @Override protected PieChart createChart() {
        final PieChart pc = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("Sun", 20),
            new PieChart.Data("IBM", 12),
            new PieChart.Data("HP", 25),
            new PieChart.Data("Dell", 22),
            new PieChart.Data("Apple", 30)
        ));
        // setup chart
        pc.setId("BasicPie");
        pc.setTitle("Pie Chart Example");
        return pc;
    }

    @Override protected PropertySheet createPropertySheet() {
        final PieChart pc = getChart();
        // create actions
        EventHandler<ActionEvent> addDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() == null) pc.setData(FXCollections.<PieChart.Data>observableArrayList());
                pc.getData().add(new PieChart.Data("item"+(itemNameIndex++), (int)(Math.random()*50)+10));
            }
        };
        EventHandler<ActionEvent> deleteDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null && !pc.getData().isEmpty()) {
                    pc.getData().remove((int) (Math.random() * (pc.getData().size() - 1)));
                }
            }
        };
        EventHandler<ActionEvent> changeDataItem = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null && !pc.getData().isEmpty()) {
                    PieChart.Data d = pc.getData().get((int)(Math.random()*(pc.getData().size())));
                    if (d!=null) {
                        d.setName("newItem"+(itemNameIndex++));
                        d.setPieValue((int)(Math.random()*50)+10);
                    }
                }
            }
        };
        EventHandler<ActionEvent> animateData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null) {
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(
                        new KeyFrame(Duration.valueOf(500), new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                for (PieChart.Data d: pc.getData()) {
                                     d.setName("newItem"+(itemNameIndex++));
                                     d.setPieValue((int)(Math.random()*50)+10);
                                }
                            }
                    }));
                    tl.setCycleCount(30);
                    tl.play();
                }
            }
        };
        EventHandler<ActionEvent> animateDataFast = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (pc.getData() != null) {
                    Timeline tl = new Timeline();
                    tl.getKeyFrames().add(
                        new KeyFrame(Duration.valueOf(50), new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                for (PieChart.Data d: pc.getData()) {
                                     d.setName("newItem"+(itemNameIndex++));
                                     d.setPieValue((int)(Math.random()*50)+10);
                                }
                            }
                    }));
                    tl.setCycleCount(100);
                    tl.play();
                }
            }
        };
        EventHandler<ActionEvent> clearData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                pc.setData(null);
            }
        };
        EventHandler<ActionEvent> setNewData = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                pc.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Monday", 20),
                    new PieChart.Data("Tuesday", 12),
                    new PieChart.Data("Wednesday", 25),
                    new PieChart.Data("Thursday", 22),
                    new PieChart.Data("Friday", 30)
                ));
            }
        };
        // create property editor
        return new PropertySheet(
            new PropertySheet.PropertyGroup("Actions",
                PropertySheet.createProperty("Add Data Item",addDataItem),
                PropertySheet.createProperty("Delete Data Item",deleteDataItem),
                PropertySheet.createProperty("Change Data Item",changeDataItem),
                PropertySheet.createProperty("Animate Data",animateData),
                PropertySheet.createProperty("Animate Data Fast",animateDataFast),
                PropertySheet.createProperty("Clear Data",clearData),
                PropertySheet.createProperty("Set New Data",setNewData)
            ),
            new PropertySheet.PropertyGroup("Chart Properties",
                PropertySheet.createProperty("Title",pc.titleProperty()),
                PropertySheet.createProperty("Title Side",pc.titleSideProperty()),
                PropertySheet.createProperty("Legend Side",pc.legendSideProperty())
            ),
            new PropertySheet.PropertyGroup("PieChart Properties",
                PropertySheet.createProperty("Label Line Length",pc.labelLineLengthProperty()),
                PropertySheet.createProperty("Labels Visible",pc.labelsVisibleProperty()),
                PropertySheet.createProperty("Start Angle",pc.startAngleProperty()),
                PropertySheet.createProperty("Clockwise",pc.clockwiseProperty())
            )
        );
    }
}
