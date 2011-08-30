package chartssampler;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.media.AudioSpectrumListener;
import static chartssampler.ChartsDemoApp.PLAY_AUDIO;
import static chartssampler.ChartsDemoApp.getAudioMediaPlayer;

/**
 * Area Audio Chart Demo
 */
public class AreaAudioDemo extends ChartDemo<AreaChart<Number,Number>> {
    private XYChart.Data<Number,Number>[] series1Data;
    private AudioSpectrumListener audioSpectrumListener;

    public AreaAudioDemo() {
        super("Live Audio Spectrum Data");
        audioSpectrumListener = new AudioSpectrumListener() {
            @Override public void spectrumDataUpdate(double timestamp, double duration,
                    float[] magnitudes, float[] phases) {
                for (int i = 0; i < series1Data.length; i++) {
                    series1Data[i].setYValue(magnitudes[i] + 60);
                }
            }
        };
    }

    @Override protected AreaChart<Number,Number> createChart() {
        final NumberAxis xAxis = new NumberAxis(0,128,8);
        final NumberAxis yAxis = new NumberAxis(0,50,10);
        final AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis,yAxis);
        // setup chart
        ac.setId("audioAreaDemo");
        ac.setLegendVisible(false);
        ac.setTitle("Live Audio Spectrum Data");
        ac.setAnimated(false);
        xAxis.setLabel("Frequency Bands");
        yAxis.setLabel("Magnitudes");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,null,"dB"));
        // add starting data
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        series.setName("Audio Spectrum");
        //noinspection unchecked
        series1Data = new XYChart.Data[(int)xAxis.getUpperBound()];
        for (int i=0; i<series1Data.length; i++) {
            series1Data[i] = new XYChart.Data<Number,Number>(i,50);
            series.getData().add(series1Data[i]);
        }
        ac.getData().add(series);
        return ac;
    }

    @Override public void start() {
        if (PLAY_AUDIO) {
            getAudioMediaPlayer().setAudioSpectrumListener(audioSpectrumListener);
            getAudioMediaPlayer().play();
        }
    }

    @Override public void stop() {
        if (getAudioMediaPlayer().getAudioSpectrumListener() == audioSpectrumListener) {
            getAudioMediaPlayer().pause();
        }
    }
}
