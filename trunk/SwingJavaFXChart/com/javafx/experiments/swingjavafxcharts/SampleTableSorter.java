/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 */

package com.javafx.experiments.swingjavafxcharts;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

/**
 * SampleTableSorter
 */
public class SampleTableSorter extends MouseAdapter
{
    private JTable table;
    
    private int prevColumnIndex = 0;
    private boolean isAscending = true;

    public SampleTableSorter(JTable table) {
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TableColumnModel columnModel = table.getColumnModel();
        int currentColumnIndex = columnModel.getColumnIndexAtX(e.getX());

        if (currentColumnIndex < 0)
            return;

        if (currentColumnIndex != prevColumnIndex) {
            prevColumnIndex = currentColumnIndex;
            isAscending = true;
        } else {
            isAscending = !isAscending;
        }

        SampleTableModel model = (SampleTableModel)table.getModel();
        model.sortData(currentColumnIndex, isAscending);

        table.tableChanged(new TableModelEvent(model));
        table.repaint();
    }
}
