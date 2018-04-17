package editdata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Data extends DataDatabase {
	public Object[] namesOfColumns;
	public Object[][] rowData;
    public Object[] rowPSC;
    public Object[] checkOutput;
    private String csvfileData="data.csv";
    private String csvfilePSC="ULICE.csv";
    private int maxpagenumber;


    public Data() {
    	
    }

    public void loadData(){
        loadNamesFromCSV();
        loadDataFromCSV();
        loadPSCFromCSV();
        rowPSC=findAndDeleteElement(rowPSC,"");
    }

    public int getMaxpagenumber() {
		return maxpagenumber;
	}

	public void setMaxpagenumber(int maxpagenumber) {
		this.maxpagenumber = maxpagenumber;
	}

	public Object[] getNamesOfColumns() {
        return namesOfColumns;
    }

    public Object[] getRowPSC() {
        return rowPSC;
    }

    public void setRowPSC(Object[] rowPSC) {
        this.rowPSC = rowPSC;
    }

 

	public Object[][] getRowData() {

        return rowData;
    }

    public void setRowData(Object[][] rowData) {
        this.rowData = rowData;
    }

    public void setNamesOfColumns(Object[] namesOfColumns) {
        this.namesOfColumns = namesOfColumns;
    }

    private void loadNamesFromCSV(){
        BufferedReader br;
        String line;

        String splitby=";";

        try {
            br=new BufferedReader(new FileReader(csvfileData));
            line=br.readLine();
            namesOfColumns= line.split(splitby);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadDataFromCSV(){
        BufferedReader br;
        String line;
        String splitby=";";
        List<String[]> tmpList=new ArrayList<String[]>();
        try {
            br=new BufferedReader(new FileReader(csvfileData));
            while((line=br.readLine())!=null){
                String []arrayline=line.split(splitby);
                tmpList.add(arrayline);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        tmpList.remove(0);
        rowData=convertListToArray(tmpList);
    }

    private void loadPSCFromCSV(){
        BufferedReader br;
        String line;
        String splitby=";";
        Set<Object> tmpSet=new TreeSet<>();
        List<Object> tmpList=new ArrayList<>();
        try {
            br=new BufferedReader(new FileReader(csvfilePSC));
            while((line=br.readLine())!=null){
                String[]arrayline=line.split(splitby);
                tmpSet.add(arrayline[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rowPSC=tmpSet.toArray();
    }

    private Object[] findAndDeleteElement(Object[] array, String element){
        ArrayList arrayList=new ArrayList(Arrays.asList(array));
        int index=arrayList.indexOf(element);
        if(index!=-1){
            arrayList.remove(index);
        }
        arrayList.toArray(array);
        return array;
    }

    private void cleanRowPSC(){

    }

    public void eraseAllQuotes(Object [][] list){
        for(int i=0;i<list.length;i++){
            for(int j=0;j<namesOfColumns.length;j++){
                list[i][j]=(list[i][j].toString()).replaceAll("\"","");
            }
        }
    }
    
    public void eraseAllQuotes(Object []list){
        for(int j=0;j<list.length;j++){
            list[j]=(list[j].toString()).replaceAll("\"","");
        }
    }

    public void eraseAllSpaces(Object [][]list){
        for(int i=0;i<list.length;i++){
            for(int j=0;j<namesOfColumns.length;j++){
                list[i][j]=(list[i][j].toString()).replaceAll(" ","");
            }
        }
    }
    
    public void callDatabaseLoadFromMain(int strana,Integer pocetriadkov) {
    	loadDatabase(strana,(int)pocetriadkov); 	
    	namesOfColumns=getNamesOfColumnsFromDatabase();
    	rowData=getRowDataFromDatabase();
    	maxpagenumber=getMaxpage();
    }

    public void diagnosticsCSV(){
    	
    }
    
    public void spustiDiagnostikuCSV() {
    	diagnosticsCSV();
    }
    
    public void spustiDiagnostikuDatabaza() {
    	
    diagnosticsDatabase();	
    }


//end of class
}
