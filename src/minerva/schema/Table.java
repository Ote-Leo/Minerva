package minerva.schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import minerva.exceptions.DBAppException;
import minerva.util.FileManager;
import minerva.util.ObjectManager;

public class Table {
	// Attributes ------------------------------------------------------------------------------------
	private int pageCount;
	private String strTableName;
	private String strClusteringKeyColName;
	private Page pgCurr;
	private Vector<Page> vecTablePages;
	
	@SuppressWarnings("unused")
	private static Hashtable<String, Column> htblTableSchema;
	
	// Constructor -----------------------------------------------------------------------------------
	public Table(String strTableName, Hashtable<String, String> htblColNameType, String strClusteringKeyColName) {
		/**
		 * Creates a Table and inizializes it's files (This constructor will only be
		 * called once a table is created for the first time)
		 * 
		 * @param strTableName
		 *            Table name (the folder of the table will be named as this
		 *            value)
		 * @param htblColNameType
		 *            Table Columns which will be stored in the metadata.csv file
		 * @param strClusteringKeyName
		 * @throws DBAppException
		 */
		this.vecTablePages = new Vector<Page>();
		this.strTableName = strTableName;
		this.strClusteringKeyColName = strClusteringKeyColName;
		this.pageCount = 0;
		
		FileManager.createNewTable(strTableName);		// Creating Directories and Files
		Schema.addNewTable(strTableName, htblColNameType, strClusteringKeyColName);		// Saving the meta-data
		try {
			FileManager.writeSchema();			// Writing the meta-data in metadata.csv
		} catch (IOException e) {
			e.printStackTrace();
		}
		htblTableSchema = Schema.getTableSchema(strTableName);
		pgCurr = new Page(strTableName, pageCount);
		ObjectManager.writePage(strTableName, pageCount, pgCurr);
	}
	
	public Table(String strTableName) {
		/**
		 * This constructor is called to create a table that already exists in the
		 * data file
		 * 
		 * @param strTableName
		 */
		this.strTableName = strTableName;
		this.pageCount = FileManager.getPageFileCount(strTableName) - 1;
		this.vecTablePages = new Vector<Page>();
		htblTableSchema = Schema.getTableSchema(strTableName);
		this.pgCurr = ObjectManager.readPage(strTableName, pageCount);
		this.vecTablePages.add(pageCount, pgCurr);
		this.strClusteringKeyColName = Schema.getClusteringKeyColName(strTableName);
	}
	
	// Methods ---------------------------------------------------------------------------------------
	public void insertIntoTable(Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if(pgCurr.isFull()) {
			pgCurr = new Page(strTableName, ++pageCount);
			ObjectManager.writePage(strTableName, pageCount, pgCurr);
		}
		try {
			pgCurr.insertInPage(new Record(htblColNameValue, strClusteringKeyColName));
			pgCurr.save();	
		}
		catch(DBAppException e) {		// Since there is one exception only !
			e.printStackTrace();
		}
	}
	
	public void deleteFromTable(Hashtable<String, Object> htblColNameValue, String strOperator) throws DBAppException {
		for(int i = 0; i <= pageCount; i++) {
			Page page;
			if(vecTablePages.elementAt(i) == null) {
				page = ObjectManager.readPage(strTableName, i);
				vecTablePages.add(i, page);
			}
			else
				page = vecTablePages.get(i);
			page.deleteFromPage(htblColNameValue, strOperator);
			if(page.getRowCount() == 0) {
				vecTablePages.remove(page);
				FileManager.deleteDirectory(FileManager.getTablesDirectory() + "/" + strTableName + "/pages/" + pgCurr.getPageNumber() + ".ser");
				pgCurr = vecTablePages.lastElement();
			}
			else
				page.save();
		}
	}
	
	public Record selectFromTable(String strClusteringKey) {
		//return pgCurr.selectFromPage(strClusteringKey);
		for(int i = 0; i <= pageCount; i++) {
			Page page;
			if(vecTablePages.elementAt(i) == null) {
				page = ObjectManager.readPage(strTableName, i);
				vecTablePages.add(i, page);
			}
			else
				page = vecTablePages.get(i);
			Record record = page.selectFromPage(strClusteringKey);
			if(record != null)
				return record;
		}
		return null;
	}
	
	public Iterator<Record> selectFromTable(Hashtable<String, Object> htblColNameValue, String strOperator) throws DBAppException {
		ArrayList<Record> recResult = new ArrayList<Record>();
		for(int i = 0; i <= pageCount; i++) {
			Page page;
			if(vecTablePages.elementAt(i) == null) {
				page = ObjectManager.readPage(strTableName, i);
				vecTablePages.add(i, page);
			}
			else
				page = vecTablePages.get(i);
			Iterator<Record> temp = page.selectFromPage(htblColNameValue, strOperator);
			while(temp.hasNext())
				recResult.add(temp.next());
		}
		return recResult.iterator();
	}
	
	public void deletePage(int pageNumber) throws DBAppException {
		if(vecTablePages.elementAt(pageNumber) == null)
			throw new DBAppException("Page " + strTableName + "/" + pageNumber + ".ser. Does not exists.");
		
		vecTablePages.remove(vecTablePages.get(pageNumber));
		FileManager.deleteDirectory(FileManager.getTablesDirectory() + "/" + strTableName + "/pages/" + pgCurr.getPageNumber() + ".ser");
		pgCurr = vecTablePages.lastElement();
	}
	
	public void clear() throws DBAppException {
		for(Page page : vecTablePages) {
			deletePage(page.getPageNumber());
		}
	}
	
	public void saveAllPages() {
		Iterator<Page> pgItr = vecTablePages.iterator();
		while(pgItr.hasNext())
			pgItr.next().save();
	}
}
































