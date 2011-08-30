package chartssampler;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

/**
 * Scatter Chart Demo
 */
public class ScatterLiveDemo extends ChartDemo<ScatterChart<Number,Number>> {

    private ScatterChart.Series<Number,Number> series;
    private double nextX = 0;
    private SequentialTransition animation;

    public ScatterLiveDemo() {
        super("Animated Sine Wave");
        // create animation
        Timeline timeline1 = new Timeline();
        timeline1.getKeyFrames().add(
            new KeyFrame(Duration.valueOf(20), new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
//
                    series.getData().add(new XYChart.Data<Number, Number>(
                            nextX,
                            Math.sin(Math.toRadians(nextX)) * 100
                    ));
                    nextX += 10;
                }
            })
        );
        timeline1.setCycleCount(200);
        Timeline timeline2 = new Timeline();
        timeline2.getKeyFrames().add(
                new KeyFrame(Duration.valueOf(50), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        series.getData().add(new XYChart.Data<Number, Number>(
                                nextX,
                                Math.sin(Math.toRadians(nextX)) * 100
                        ));
                        if (series.getData().size() > 54) series.getData().remove(0);
                        nextX += 10;
                    }
                })
        );
        timeline2.setCycleCount(Animation.INDEFINITE);
        animation = new SequentialTransition();
        animation.getChildren().addAll(timeline1,timeline2);
    }

    @Override protected ScatterChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        final NumberAxis yAxis = new NumberAxis(-100,100,10);
        final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);
        // setup chart
        sc.setId("liveScatterChart");
        sc.setTitle("Animated Sine Wave ScatterChart");
        xAxis.setLabel("X Axis");
        xAxis.setAnimated(false);
        yAxis.setLabel("Y Axis");
        yAxis.setAutoRanging(false);
        // add starting data
        series = new ScatterChart.Series<Number,Number>();
        series.setName("Sine Wave");
        series.getData().add(new ScatterChart.Data<Number, Number>(5d, 5d));
        sc.getData().add(series);
        return sc;
    }

    @Override public void start() {
        animation.play();
    }

    @Override public void stop() {
        animation.pause();
    }

}
