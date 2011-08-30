package chartssampler;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.media.AudioSpectrumListener;

/**
 * BarChart Audio Spectrum Demo
 */
public class BarAudioDemo extends ChartDemo<BarChart<String,Number>> {

    private XYChart.Data<String, Number>[] series1Data;
    private AudioSpectrumListener audioSpectrumListener;

    public BarAudioDemo() {
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

    @Override protected BarChart<String, Number> createChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0,50,10);
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setId("barAudioDemo");
        bc.setLegendVisible(false);
        bc.setAnimated(false);
        bc.setBarGap(0);
        bc.setCategoryGap(1);
        bc.setVerticalGridLinesVisible(false);
        // setup chart
        bc.setTitle("Live Audio Spectrum Data");
        xAxis.setLabel("Frequency Bands");
        yAxis.setLabel("Magnitudes");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,null,"dB"));
        // add starting data
        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
        series1.setName("Data Series 1");
        //noinspection unchecked
        series1Data = new XYChart.Data[128];
        String[] categories = new String[128];
        for (int i=0; i<series1Data.length; i++) {
            categories[i] = Integer.toString(i+1);
            series1Data[i] = new XYChart.Data<String,Number>(categories[i],50);
            series1.getData().add(series1Data[i]);
        }
        bc.getData().add(series1);
        return bc;
    }

    @Override public void start() {
        if (ChartsDemoApp.PLAY_AUDIO) {
            ChartsDemoApp.getAudioMediaPlayer().setAudioSpectrumListener(audioSpectrumListener);
            ChartsDemoApp.getAudioMediaPlayer().play();
        }
    }

    @Override public void stop() {
        if (ChartsDemoApp.getAudioMediaPlayer().getAudioSpectrumListener() == audioSpectrumListener) {
            ChartsDemoApp.getAudioMediaPlayer().pause();
        }
    }

}
