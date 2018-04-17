package guibuild;

import javax.swing.table.AbstractTableModel;

public class CustomDataModel extends AbstractTableModel {
    private Object [] columnNames;
    private Object [][] data;

    public CustomDataModel(Object[][] rowData, Object[] columns) {
        this.data=rowData;
        this.columnNames=columns;
    }


    public Object[][] getData() {
        return data;
    }



    public void setData(Object[][] data) {
        this.data = data;
    }


    public Object[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Object[] columnNames) {
        this.columnNames = columnNames;
    }

    public void refresh(){
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int findColumn(String columnName) {
        return super.findColumn(columnName);
    }

    @Override
	public Class<?> getColumnClass(int column) {
		// TODO Auto-generated method stub
		return super.getColumnClass(column);
	}


	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return super.getColumnName(column);
	}


	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
//end of class
}
