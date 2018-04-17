package editdata;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataDatabase{
	
	private static Connection conn;
	public int numberOfColumns;
	public int numberOfRows;
	public int maxpage;
	public Object[] namesOfColumnsFromDatabase;
	public Object[][]rowDataFromDatabase;
	public String tablename="dbo.PROFILING2";
	public List<Object> diagnostics_name;
	public List<Object> diagnostics_surname;
	public List<Object>	diagnostics_email;
	public List<Object> diagnostics_birthnumber;
	public List<Object> diagnostics_zipcode;
	public List<Object> diagnostics_wedonotknow;
	public List<Object> evaluate;
	
	

	public int getMaxpage() {
		return maxpage;
	}

	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	
	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	
	public Object[] getNamesOfColumnsFromDatabase() {
		return namesOfColumnsFromDatabase;
	}

	public void setNamesOfColumnsFromDatabase(Object[] namesOfColumnsFromDatabase) {
		this.namesOfColumnsFromDatabase = namesOfColumnsFromDatabase;
	}

	public Object[][] getRowDataFromDatabase() {
		return rowDataFromDatabase;
	}

	public void setRowDataFromDatabase(Object[][] rowDataFromDatabase) {
		this.rowDataFromDatabase = rowDataFromDatabase;
	}

	//konstruktor
	public DataDatabase() {
		// Constructor
		
	}
	
	public void  loadDatabase(int strana,int pocetriadkov) {
		
		loadDriver();
		connectToDatabase();
		getRowsCount();
		maxpage=getMaxPageNumber(pocetriadkov);
		//getSelectedData();
		getSelectedDataFromInterval(strana,pocetriadkov);
		stopConnection();
	}

	private void loadDriver(){
		System.out.println("Loading driver...");
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
        	System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
        	throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }        
	}
	
	private void connectToDatabase(){
		System.out.println("Starting Connect");
		System.out.println("Connecting database...");
        String connection="jdbc:sqlserver://xkranec.database.windows.net:1433;databaseName=dbbakalar;user=xkranec;password=GA.bi.ka.123";
        try {
			conn=DriverManager.getConnection(connection);
			System.out.println("Database connected succesfully");
			getRowsCount();
		} catch (SQLException e) {
			System.out.println("Database not connected Error: "+e);
		}
    }
	
	private void stopConnection () {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Spojenie sa nepodarilo ukoncit");
			e.printStackTrace();
		}
	}
	
	private void getSelectedData() {
		System.out.println("Starting Select");
		List<List<Object>> tmpMultipleData=new ArrayList<>();
		try {
			getNames();
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select * from dbo.PROFILING2");
			while(rs.next()) {
				List<Object> tmpData=new ArrayList();
				for(int columnIndex=1;columnIndex<=numberOfColumns;columnIndex++) {
					tmpData.add(rs.getObject(columnIndex));
				}			
				tmpMultipleData.add(tmpData);
			}			
		} catch (SQLException e) {			
			System.out.println("Select nebol vykonany");
			e.printStackTrace();
		}
		rowDataFromDatabase=convertObjectListToArray(tmpMultipleData);//TODO CHYBA
	}
	
	/*
	 * vola sa iba v pripade strankovania
	 */
	private void getSelectedDataFromInterval(int strana,int pocetriadkov) {
		List interval=getInterval(strana,pocetriadkov);
		getNames();
		
		String tmpcolumn=(String)namesOfColumnsFromDatabase[0];
		System.out.println("Stlpec: "+tmpcolumn);
		List<List<Object>> tmpMultipleData=new ArrayList<>();
		System.out.println(interval.get(0)+" : "+interval.get(1));
		try {
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select * from (select ROW_NUMBER() OVER  ( ORDER BY "+tmpcolumn+" ASC) AS rownumber,* from "+tablename+" ) temptablename where rownumber >"+interval.get(0)+" AND rownumber<"+interval.get(1)+"");
			while(rs.next()) {
				List<Object> tmpData=new ArrayList();
				for(int columnIndex=2;columnIndex<=numberOfColumns+1;columnIndex++) {
					tmpData.add(rs.getObject(columnIndex));
				}			
				tmpMultipleData.add(tmpData);
			}			
		} catch (SQLException e) {			
			System.out.println("Select nebol vykonany");
			e.printStackTrace();
		}
		rowDataFromDatabase=convertObjectListToArray(tmpMultipleData);		
	}
	
	private List<Integer> getInterval(int strana,int pocetriadkov) {
		int vrchnahranica=(strana+1)*pocetriadkov;
		int spodnahranica=(strana)*pocetriadkov;
		List<Integer> interval=new ArrayList<>();
		interval.add(spodnahranica);
		interval.add(vrchnahranica);
		
		System.out.println(interval.get(0)+" : "+interval.get(1));

		return interval;
	}
	
	private void getNames() {
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
		
		ResultSet rs=stmt.executeQuery("SELECT TOP 1 * FROM dbo.PROFILING2");
		ResultSetMetaData rsmd=rs.getMetaData();
		numberOfColumns=rsmd.getColumnCount();
		System.out.println("Pocet stlpcov"+numberOfColumns);
		List<Object> tmpNames=new ArrayList();
		for(int columnIndex=1;columnIndex<=numberOfColumns;columnIndex++) {
			tmpNames.add(rsmd.getColumnName(columnIndex));			
		}
		namesOfColumnsFromDatabase = convertOneListToArray(tmpNames);
		} catch (SQLException e) {
			System.out.println("Mena neboli nacitane");
			e.printStackTrace();
		}
		
		
	}
	
	//nastavuje pocet riadkov 
	private void getRowsCount() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery("select count(*) from dbo.PROFILING2");
			while(rs.next()) {
				numberOfRows=(int) rs.getObject(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Select Count Nepresiel");
			e.printStackTrace();
		}
		
		
	}
	
	//convert STRING [][]
    public String[][] convertListToArray(List<String[]> list){
        String[][] array=new String[list.size()][(list.get(0)).length];
        for(int i=0;i<list.size();i++){
            String[] row=list.get(i);
            array[i]=row;
        }
        return array;
    }
    
    //convert OBJECT [][]
    public Object[][] convertObjectListToArray(List<List<Object>> list){
    	Object[][] array = list.stream().map(u -> u.toArray(new Object[0])).toArray(Object[][]::new);        
    	return array;
    }
    
    //convert OBJECT []
    public Object[] convertOneListToArray(List<Object> list) {
    	Object [] array= list.toArray();
    	return array;
    }
	
    public int getMaxPageNumber( int zobrazenypocetriadkov) {
    	
    	int maxpage=numberOfRows/zobrazenypocetriadkov;
    	int tmpmodulo=numberOfRows%zobrazenypocetriadkov;
    	if(tmpmodulo>0)
    	return maxpage+1;
    	
    	return maxpage;
    }
    
    public void diagnosticsDatabase() {
    	int odhad=Statistics.getSampleSize(numberOfRows);
    	diagnostics_name=new ArrayList<>();
    	diagnostics_surname=new ArrayList<>();
    	diagnostics_zipcode=new ArrayList<>();
    	diagnostics_email=new ArrayList<>();
    	diagnostics_birthnumber=new ArrayList<>();
    	diagnostics_wedonotknow=new ArrayList<>();
    	connectToDatabase();
    	Statement stmt;		
    	try {
    		stmt = conn.createStatement();
    		String tmpcolumn=(String)namesOfColumnsFromDatabase[0];
    		System.out.println("Starting Diagnostics");
			ResultSet rs=stmt.executeQuery("select * from (select ROW_NUMBER() OVER  ( ORDER BY "+tmpcolumn+" ASC) AS rownumber,* from "+tablename+" ) temptablename where rownumber >"+0+" AND rownumber<"+odhad+"");
			while(rs.next()) {
				for(int columnindex=2;columnindex<=numberOfColumns;columnindex++) {//zmenil som columnindex z 2 na 1 a numberofcolumns z +1 na obycajny
					System.out.println("rs:"+rs.getObject(columnindex));
					String diagnostics_pointer=Statistics.Diagnostics(rs.getObject(columnindex));
					switch(diagnostics_pointer) {
					case "name":
						diagnostics_name.add(namesOfColumnsFromDatabase[columnindex-2]);						
						break;
					case "surname":
						diagnostics_surname.add(namesOfColumnsFromDatabase[columnindex-2]);
						break;
					case "zipcode":
						diagnostics_zipcode.add(namesOfColumnsFromDatabase[columnindex-2]);
						break;
					case "email":
						diagnostics_email.add(namesOfColumnsFromDatabase[columnindex-2]);
						break;
					case "birthnumber":
						diagnostics_birthnumber.add(namesOfColumnsFromDatabase[columnindex-2]);
						break;
					default:
						diagnostics_wedonotknow.add(namesOfColumnsFromDatabase[columnindex-2]);
						break;
					}
				//TODO
				
				
				}
			}
			System.out.println("BirthNumber:"+diagnostics_birthnumber.size());
			System.out.println("Email:"+diagnostics_email.size());
			System.out.println("Zipcode:"+diagnostics_zipcode.size());
			System.out.println("Name:"+diagnostics_name.size());
			System.out.println("Surname:"+diagnostics_surname.size());
			System.out.println("We dont know:"+diagnostics_wedonotknow.size());
			evaluate=new ArrayList<>();
		} catch (SQLException e) {
			System.out.println("Nepodarilo sa nacitat data pre diagnostiku");
			e.printStackTrace();
		}
    	stopConnection();
    }
    
    public void evaluateDiagnosticsDatabase() {
    	//for(int i=0;i<numberOfColumns;i++) {
    		//List<HashMap>list=new ArrayList<>();
    		//evaluate.add(list);
    		
    	//}
    }
    
    
	//end of class
}
