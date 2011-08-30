/*
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 */

package com.javafx.experiments.swingjavafxcharts;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import javax.swing.table.JTableHeader;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import javafx.beans.value.InvalidationListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.embed.swing.JFXPanel;

import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javax.swing.JApplet;

/**
 * Sample showing a JavaFX chart linked to a Swing JTable
 *
 */
public class Main extends JApplet {

    private static JFXPanel javafxPanel;
    private static SampleTableModel tableModel;

    private Scene scene;
    private Chart chart;

    //init GUI
    public void init() {
        // create swing table
        tableModel = new SampleTableModel();

        JTable table = new JTable(tableModel);
        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new SampleTableSorter(table));

        JScrollPane panel = new JScrollPane(table);
        panel.setPreferredSize(new Dimension(550,100));
        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);

        // create javafx panel
        javafxPanel = new JFXPanel();
        javafxPanel.setPreferredSize(new Dimension(550,400));
        add(javafxPanel, BorderLayout.CENTER);

        // create JavaFX scene
        Platform.runLater(new Runnable() {
            public void run() {
                createScene();
            }
        });

    }

    public void createScene() {
        Group root = new Group();
        scene = new Scene(root);

        root.getChildren().add(chart = createBarChart());
        // add scene to panel
        javafxPanel.setScene(scene);

        scene.widthProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(ObservableValue ov) {
                chart.setLayoutX((scene.getWidth() - chart.getWidth()) / 2);
            }
        });
        scene.heightProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(ObservableValue ov) {
                chart.setLayoutY((scene.getHeight() - chart.getHeight()) / 2);
            }
        });
    }

    private BarChart createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(tableModel.getColumnNames()));
        xAxis.setLabel("Year");

        double lowerBound = tableModel.getLowerValue();
        double upperBound = tableModel.getUpperValue();
        double tickUnit = (upperBound - lowerBound) / 3;

        NumberAxis yAxis = new NumberAxis("Units Sold", lowerBound, upperBound, tickUnit);

        final BarChart chart = new BarChart(xAxis, yAxis, tableModel.getBarChartData());
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    final int row = e.getFirstRow();
                    final int column = e.getColumn();
                    final double value = ((SampleTableModel)e.getSource()).getValueAt(row, column);

                    Platform.runLater(new Runnable() {
                        public void run() {
                            XYChart.Series<String, Number> s = (XYChart.Series<String, Number>)chart.getData().get(row);
                            BarChart.Data data = s.getData().get(column);
                            data.setYValue(value);
                        }
                    });

                    //chart.repaint();
                }
            }
        });

        return chart;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               JFrame frame = new JFrame("Swing JavaFX Chart");
               frame.setResizable(false);
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

               JApplet applet = new Main();
               applet.init();

               frame.setContentPane(applet.getContentPane());

               frame.pack();
               frame.setLocationRelativeTo(null);
               frame.setVisible(true);

               applet.start();
            }
        });
    }
}
