package editdata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
	
	 public static final Pattern VALID_EMAIL_ADDRESS_REGEX= Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	 public static final Pattern VALID_BIRTH_NUMBER_REGEX=Pattern.compile("\\A[0-9][0-9][0-9][0-9][0-9][0-9][\\/][0-9][0-9][0-9][0-9]\\z",Pattern.CASE_INSENSITIVE);
	 public static String lastnames="CSV_Database_of_Last_Names.csv";
	 public static String zipcodes="sk_postal_codes.csv";
	    
	
	public Statistics() {
		// TODO Auto-generated constructor stub
		
		
	}
	
	public static String Diagnostics(Object text){
		if(text instanceof String) {
			if(controlSurname(text)) 
				return "surname";
			else if(controlZipCode(text))
				return "zipcode";
			else if(controlEmail(text))
				return "email";
			else if(controlBirthNumber(text))
				return "birth";
			else return "nothing";
		}
		else return "nothing";
	}
	
	public static int getSampleSize(int rowcount) {
		double z_score=1.96;
        double p_coinfidence=0.5;
        double e=0.05;
        double citatel=(z_score*z_score*p_coinfidence*(1-p_coinfidence))/(e*e);
        double menovatel=1+((z_score*z_score*p_coinfidence*(1-p_coinfidence))/(e*e*rowcount));
        double samplesize=(citatel/menovatel);
        System.out.println("SampleSize="+samplesize);
        return (int) samplesize;
	}
	
	public static boolean controlZipCode(Object zipcode) {
		if(!(zipcode instanceof String)) {
			return false;
		}
		BufferedReader br;
		String line;
		String splitby=";";
		try {
			br=new BufferedReader(new FileReader(zipcodes));
			while((line=br.readLine())!=null) {
				String[]cols=line.split(splitby);
				if(compareTwoObjects((String)zipcode,cols[0]))
				return true;
			
			}
		}catch (FileNotFoundException ex) {
            Logger.getLogger(Statistics.class.getName()).log(null);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(null);
        }
		return false;
		
	}
	
	public static boolean controlEmail(Object emailStr){
        if(!(emailStr instanceof String))
            return false;
        Matcher matcher=VALID_EMAIL_ADDRESS_REGEX .matcher((String)emailStr);
        return matcher.find();
   }
	
	public static int calculatePercent(int cislo,int celkom){
        double tmp=celkom/100.0;
        double pocet_percent=(cislo/tmp);
        return (int) Math.round(pocet_percent);
    }
	
	public static boolean controlBirthNumber(Object birthNumber){
        if(!(birthNumber instanceof String))
            return false;
        Matcher matcher =VALID_BIRTH_NUMBER_REGEX .matcher((String)birthNumber);
        return matcher.find();
    }
	
	public static boolean controlSurname(Object surname) {
		if(!(surname instanceof String)) {
			return false;
		}
		BufferedReader br;
		String line;
		String splitby=";";
		try {
			br=new BufferedReader(new FileReader(lastnames));
			while((line=br.readLine())!=null) {
				if(compareTwoObjects((String)surname,line))
				return true;
			
			}
		}catch (FileNotFoundException ex) {
            Logger.getLogger(Statistics.class.getName()).log(null);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(null);
        }
		return false;
	}
	
	private static boolean compareTwoObjects(String string1,String string2){
        return string1.equals(string2);
    }
	
	private static boolean compareTwoObjects(Object object1,Object object2) {
		return object1.equals(object2);
	}
	
	private void evaluateDiagnostics(List<Object> list,int numberofcolumns) {
		
		
	}
//end of class
}
