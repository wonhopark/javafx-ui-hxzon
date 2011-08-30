package chartssampler;

import javafx.scene.chart.Chart;
import javafx.scene.layout.Region;

/**
 * Base class for all chart demos
 */
public abstract class ChartDemo<C extends Chart>{

    private final String name;
    private final C chart;
    private final PropertySheet propertySheet;

    protected ChartDemo(String name) {
        this.name = name;
        this.chart = createChart();
        this.propertySheet = createPropertySheet();
    }

    protected abstract C createChart();
    public C getChart(){ return chart; }
    protected PropertySheet createPropertySheet(){ return null; }
    public PropertySheet getPropertySheet() { return propertySheet; }

    /**
     * Called to start any animations or live data
     */
    public void start(){}

    /**
     * Called to stop any animations or live data
     */
    public void stop(){}

    @Override public String toString() {
        return name;
    }
}
