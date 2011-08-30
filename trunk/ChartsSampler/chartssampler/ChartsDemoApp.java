package chartssampler;

import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

@SuppressWarnings({"unchecked"})
public class ChartsDemoApp extends Application {
    public static final String AUDIO_URI = System.getProperty("demo.audio.url","http://webcast-west.sun.com/oow2010.flv");
    private static MediaPlayer audioMediaPlayer;
    public static final boolean PLAY_AUDIO = Boolean.parseBoolean(System.getProperty("demo.play.audio","true"));
    private Image starImage;

    public static MediaPlayer getAudioMediaPlayer() {
        if (audioMediaPlayer == null) {
            Media audioMedia = new Media(AUDIO_URI);
            audioMediaPlayer = new MediaPlayer(audioMedia);
        }
        return audioMediaPlayer;
    }

    private TreeItem createCategoryTreeItem(String name) {
        Label label = new Label(name);
        label.getStyleClass().add("category-label");
        return new TreeItem(label);
    }

    private ImageView createStarGraphic(boolean highlight) {
        if (starImage==null) {
            starImage = new Image(getClass().getResourceAsStream("full-star.png"));
        }
        ImageView iv = new ImageView(starImage);
        if(!highlight) iv.setOpacity(0.3);
        return iv;
    }

    @Override public void start(Stage stage) {
        try {
            final SplitPane mainSplitPane = createContent(stage);
            Scene scene  = new Scene(mainSplitPane,1000,700);
            scene.getStylesheets().addAll(
                    "/chartssampler/charts.css",
                    "/chartssampler/demoapp.css"
            );
            stage.setScene(scene);
            stage.setVisible(true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private SplitPane createContent(Stage stage) {
        stage.setTitle("JavaFX 2.0 Charts Sampler");
        // create tree items for demos tree
        TreeItem scatter = createCategoryTreeItem("ScatterChart Demos");
        scatter.setExpanded(true);
        scatter.getChildren().addAll(
                new TreeItem(new ScatterDemo(),createStarGraphic(false)),
                new TreeItem(new ScatterLiveDemo(),createStarGraphic(true))
        );
        TreeItem line = createCategoryTreeItem("LineChart Demos");
        line.setExpanded(true);
        line.getChildren().addAll(
                new TreeItem(new LineDemo(),createStarGraphic(false)),
                new TreeItem(new LineCategoryDemo(),createStarGraphic(false)),
                new TreeItem(new StockLineDemo(),createStarGraphic(true))
        );
        TreeItem area = createCategoryTreeItem("AreaChart Demos");
        area.setExpanded(true);
        area.getChildren().addAll(
                new TreeItem(new AreaDemo(),createStarGraphic(false)),
                new TreeItem(new AreaAudioDemo(),createStarGraphic(true))
        );
        TreeItem bubble = createCategoryTreeItem("BubbleChart Demos");
        bubble.setExpanded(true);
        bubble.getChildren().addAll(
                new TreeItem(new BubbleDemo(),createStarGraphic(false))
        );
        TreeItem bar = createCategoryTreeItem("BarChart Demos");
        bar.setExpanded(true);
        bar.getChildren().addAll(
                new TreeItem(new BarDemo(),createStarGraphic(false)),
                new TreeItem(new BarHorizontalDemo(),createStarGraphic(false)),
                new TreeItem(new BarAudioDemo(),createStarGraphic(true))
        );
        TreeItem pie = createCategoryTreeItem("PieChart Demos");
        pie.setExpanded(true);
        pie.getChildren().addAll(
                new TreeItem(new PieDemo(),createStarGraphic(false))
        );
        TreeItem custom = createCategoryTreeItem("Custom Chart Demos");
        custom.setExpanded(true);
        custom.getChildren().addAll(
                new TreeItem(new CandleStickDemo(),createStarGraphic(true)),
                new TreeItem(new StackedBarDemo(),createStarGraphic(true))
        );
        TreeItem root = new TreeItem("root");
        root.getChildren().addAll( scatter, line, area, bubble, bar, pie, custom );
        // create demos tree for left side of split
        TreeView tree = new TreeView(root);
        tree.setShowRoot(false);
        // pane to fill right side of split
        final BorderPane rightPane = new BorderPane();
        final Label placeHolder = new Label("...");
        rightPane.setCenter(placeHolder);
        // pane to fill right side of split
        final ScrollPane propertiesScrollPane = new ScrollPane();
        propertiesScrollPane.setFitToWidth(true);
        final Region propertiesPlaceHolder = new Region();
        propertiesScrollPane.setNode(propertiesPlaceHolder);
        // create left split
        final SplitPane leftSplitPane = new SplitPane();
        leftSplitPane.getItems().addAll(tree, propertiesScrollPane);
        leftSplitPane.setOrientation(Orientation.VERTICAL);
        // create main splitter
        final SplitPane mainSplitPane = new SplitPane();
        mainSplitPane.getItems().addAll(leftSplitPane, rightPane);
        mainSplitPane.setOrientation(Orientation.HORIZONTAL);
        // set initial split locations
        mainSplitPane.setDividerPosition(0, 0.32);
        // listen to tree selection and update the chart demo that is showing
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(oldValue instanceof TreeItem) {
                    TreeItem newItem = (TreeItem)oldValue;
                    if(newItem.getValue() instanceof ChartDemo) {
                        ((ChartDemo)newItem.getValue()).stop();
                    }
                }
                Node newSplitChild = placeHolder;
                Node propertySheet = propertiesPlaceHolder;
                if(newValue instanceof TreeItem) {
                    TreeItem newItem = (TreeItem)newValue;
                    if(newItem.getValue() instanceof ChartDemo) {
                        ChartDemo demo = (ChartDemo) newItem.getValue();
                        if(demo.getPropertySheet() != null) propertySheet = demo.getPropertySheet();
                        newSplitChild = demo.getChart();
                        demo.start();
                    }
                }
                rightPane.setCenter(newSplitChild);
                propertiesScrollPane.setNode(propertySheet);
            }
        });
        // select first demo
        tree.getSelectionModel().select(scatter.getChildren().get(0));
        return mainSplitPane;
    }

    public static void main(String[] args) {
        Application.launch(ChartsDemoApp.class, args);
    }
}
