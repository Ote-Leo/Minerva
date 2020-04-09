package minerva.schema;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import minerva.exceptions.DBAppException;
import minerva.util.ObjectManager;
import minerva.util.PropertiesReader;

public class Page implements Serializable {
	// Attributes ------------------------------------------------------------------------------------
	private static final long serialVersionUID = 3201741970615432801L;
	private String strTableName;
	private int pageNumber, rowCount;
	private Vector<Record> vecPageData;
	
	// Constructor -----------------------------------------------------------------------------------
	public Page(String strTableName, int pageNumber) {
		this.strTableName = strTableName;
		this.pageNumber = pageNumber;
		this.rowCount = 0;
		vecPageData = new Vector<Record>();
		save();
	}
	
	public static void main(String[] args) {
		Page page = ObjectManager.readPage("Student", 0);
		System.out.println(page);
	}
	
	// Methods ---------------------------------------------------------------------------------------
	public boolean isFull() {
		return vecPageData.size() == PropertiesReader.MAX_ROW_COUNT_IN_PAGE;
	}
	
	public void insertInPage(Record record) throws DBAppException {
		/**
		 * inserts a record to the current row count, throws an exception if the
		 * page is full
		 * 
		 * @param record
		 * @throws DBAppException
		 */
		if(!isFull())
			vecPageData.add(record);
		else
			throw new DBAppException("Table " + strTableName + "'sPage (" + pageNumber + ") is full!");
		rowCount++; 
		save();
	}
	
	public void deleteFromPage(int rowNumber) {
		this.vecPageData.get(rowNumber).setDeleted(true);
		this.vecPageData.remove(rowNumber);
		rowCount--;
	}
	
	public void deleteFromPage(Hashtable<String, Object> htblColNameValue, String strOperator) {
		boolean and = strOperator.equalsIgnoreCase("AND");
		for(int i = 0; i < rowCount; i++) {
			Record record = vecPageData.get(i);
			if(and && record.containsAllColumnValues(htblColNameValue))
				deleteFromPage(i);
			else if(record.containsAnyColumnValue(htblColNameValue))
				deleteFromPage(i);
		}
		save();
	}
	
	public Iterator<Record> selectFromPage(Hashtable<String, Object> htblColNameValue, String strOperator) {
		boolean and = strOperator.equalsIgnoreCase("AND");
		Vector<Record> vecRecords = new Vector<Record>();
		for(int i = 0; i < rowCount; i++) {
			Record record = vecPageData.get(i);
			if(and && record.containsAllColumnValues(htblColNameValue))
				vecRecords.add(selectFromPage(i));
			else if(record.containsAnyColumnValue(htblColNameValue))
				vecRecords.add(selectFromPage(i));
		}
		return vecRecords.iterator();
	}
	
	public void sortRecords() {
		Comparator<Record> c = new Comparator<Record>() {
			public int compare(Record e1, Record e2) {
				return e1.compareTo(e2);
			}
		};
		this.vecPageData.sort(c);
		
	}
	
	public String toString() {
		String r = "=======================================================================================\n";
		r += "Table: " + strTableName + ", Page Number: " + pageNumber + ", Row Count: " + rowCount;
		r += "\n---------------------------------------------------------------------------------------\n";
		for(Record item : vecPageData)
			r += item.toString() + "\n";
		r += "=======================================================================================\n";
		return r;
	}
	
	public Record selectFromPage(int i) {
		return vecPageData.get(i);	
	}
	
	public Record selectFromPage(String strClusteringKey) {
		for(Record record : vecPageData) 
			if(record.clusteringKey.toString() == strClusteringKey)
				return record;
		return null;
	}
	
	public void save() {
		sortRecords();
		ObjectManager.writePage(strTableName, pageNumber, this);
	}
	
	// Getters ---------------------------------------------------------------------------------------
	public int getRowCount() {
		return this.rowCount;
	}
	
	public Enumeration<Record> getData() {
		return this.vecPageData.elements();
	}
	
	public int getPageNumber() {
		return this.pageNumber;
	}
}

































