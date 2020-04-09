package minerva;

import java.util.Hashtable;

import minerva.exceptions.DBAppException;

public class DBAppTest {
	public static void main(String[] args) throws DBAppException {
		String strTableName = "Student";
		
		DBApp dbApp = new DBApp();
		
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable(strTableName, "id", htblColNameType);
		
		Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
		htblColNameValue.put("id", 2343432);
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", 0.95);
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		htblColNameValue.clear();
		htblColNameValue.put("id", 453455);
		htblColNameValue.put("name", new String("Ahmed Noor"));
		htblColNameValue.put("gpa", 0.95);
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		htblColNameValue.clear();
		htblColNameValue.put("id", 5674567);
		htblColNameValue.put("name", new String("Dalia Noor"));
		htblColNameValue.put("gpa", 1.25);
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		htblColNameValue.clear();
		htblColNameValue.put("id", 23498);
		htblColNameValue.put("name", new String("John Noor"));
		htblColNameValue.put("gpa", 1.5);
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		htblColNameValue.clear();
		htblColNameValue.put("id", 78452);
		htblColNameValue.put("name", new String("Zaky Noor"));
		htblColNameValue.put("gpa", 0.88);
		dbApp.insertIntoTable(strTableName, htblColNameValue);
		
		/*
		SQLTerm[] arrSQLTerms;
		arrSQLTerms = new SQLTerm[2];
		arrSQLTerms[0]._strTableName = "Student";
		arrSQLTerms[0]._strColumnName= "name";
		arrSQLTerms[0]._strOperator = "=";
		arrSQLTerms[0]._objValue = "John Noor";
		
		arrSQLTerms[1]._strTableName = "Student";
		arrSQLTerms[1]._strColumnName= "gpa";
		arrSQLTerms[1]._strOperator = "=";
		arrSQLTerms[1]._objValue = new Double(1.5);
		
		String[]strarrOperators = new String[1];
		strarrOperators[0] = "OR";
		// select * from Student where name = “John Noor” or gpa = 1.5;
		Iterator resultSet = dbApp.selectFromTable(arrSQLTerms, strarrOperators); 
		*/
	}
}
