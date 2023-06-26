package ru.prog.itmo.gui;

import ru.prog.itmo.speaker.Speaker;

import javax.swing.table.AbstractTableModel;

public class SpaceMarineTableModel extends AbstractTableModel {
    private Store store;
    private Speaker speaker;

    public SpaceMarineTableModel(Store store, Speaker speaker) {
        this.store = store;
        this.speaker = speaker;
    }

    @Override
    public int getRowCount() {
        return store.size();
    }

    @Override
    public int getColumnCount() {
        return store.getMarineFieldsCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return speaker.speak(store.getValueAt(rowIndex, columnIndex));
    }

    @Override
    public String getColumnName(int column) {
        return speaker.speak(store.getFieldName(column));
    }
}
