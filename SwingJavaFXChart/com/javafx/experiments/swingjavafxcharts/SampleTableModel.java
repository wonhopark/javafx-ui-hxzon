/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 */

package com.javafx.experiments.swingjavafxcharts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;

/**
 * SampleTableModel
 */
public class SampleTableModel extends AbstractTableModel {

    private final String[] names = {"2007", "2008", "2009"};
    private final List<Double> data[];

    private RowComparator comparator = new RowComparator();

    public SampleTableModel() {
        data = new List[3];
        data[0] = new ArrayList(Arrays.asList(567d,  956d,  1154d));
        data[1] = new ArrayList(Arrays.asList(1292d, 1665d, 1927d));
        data[2] = new ArrayList(Arrays.asList(1292d, 2559d, 2774d));
    }

    public void sortData(int column, boolean isAscending) { // sorts rows
        comparator.setColumn(column);
        comparator.setAscending(isAscending);
        Arrays.sort(data, comparator);
    }

    public double getLowerValue() {
        return 0;
    }

    public double getUpperValue() {
        return 3000;
    }

    public List<String> getColumnNames() {
        return Arrays.asList(names);
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return names.length;
    }

    @Override
    public Double getValueAt(int row, int column) {
        return data[row].get(column);
    }

    @Override
    public String getColumnName(int column) {
        return names[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        data[row].set(column, Double.parseDouble((String)value));

        fireTableCellUpdated(row, column);
    }

    private static ObservableList<BarChart.Series> bcData;

    public ObservableList<BarChart.Series> getBarChartData() {
        if (bcData == null) {
            bcData = FXCollections.<BarChart.Series>observableArrayList();
            for (int row = 0; row < getRowCount(); row++) {
                ObservableList<BarChart.Data> series = FXCollections.<BarChart.Data>observableArrayList();
                for (int column = 0; column < getColumnCount(); column++) {
                    series.add(new BarChart.Data(getColumnName(column), getValueAt(row, column)));
                }
                bcData.add(new BarChart.Series(series));
            }
        }
        return bcData;
    }
}

class RowComparator implements Comparator {

    private boolean isAscending = true;
    private int columnIndex = 0;

    public RowComparator() {
    }

    public void setColumn(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setAscending(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public int compare(Object o1, Object o2) { // rows
        if (!(o1 instanceof List) || !(o2 instanceof List))
            return 0;

        Double d1 = (Double)((List)o1).get(columnIndex);
        Double d2 = (Double)((List)o2).get(columnIndex);

        if (isAscending)
            return d1.compareTo(d2);

        return d2.compareTo(d1);
    }
}