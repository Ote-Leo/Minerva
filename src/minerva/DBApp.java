package minerva;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import minerva.exceptions.DBAppException;
import minerva.schema.Record;
import minerva.schema.SQLTerm;
import minerva.schema.Schema;
import minerva.schema.Table;
import minerva.util.FileManager;

public class DBApp {
	// Attributes ------------------------------------------------------------------------------------
	public Hashtable<String, Table> htblCachedTables = new Hashtable<String, Table>();
	
	// Methods ---------------------------------------------------------------------------------------
	public void init() {
		// TODO: Think of Something
	}
	
	public void createTable(String strTableName, String strClusteringKeyCol, Hashtable<String, String> htblColNameType) throws DBAppException {
		if(Schema.checkTableExists(strTableName))
			throw new DBAppException("Table " + strTableName + " already exists!");
		Table table = new Table(strTableName, htblColNameType, strClusteringKeyCol);
		this.htblCachedTables.put(strTableName, table);
	}
	
	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if(!Schema.checkTableExists(strTableName))
			throw new DBAppException("Table " + strTableName + " does not exists.");
		if(!htblCachedTables.contains(strTableName))
			htblCachedTables.put(strTableName, new Table(strTableName));
		Table table = htblCachedTables.get(strTableName);
		table.insertIntoTable(htblColNameValue);
		saveAll();
	}
	
	public void createBTreeIndex(String strTableName, String strColName) throws DBAppException {
		// TODO in Milestone 2
	}
	
	public void createRTreeIndex(String strTableName, String strColName) throws DBAppException {
		// TODO in Milestone 2
	}
	
	// TODO
	public void updateTable(String strTableName, String strClusteringKey, Hashtable<String,Object> htblColNameValue) throws DBAppException {
		// updateTable notes:
			// htblColNameValue holds the key and new value
			// htblColNameValue will not include clustering key as column name
			// htblColNameValue enteries are ANDED together	
		if(!Schema.checkTableExists(strTableName))
			throw new DBAppException("Table " + strTableName + "does not exists.");
		if(!htblCachedTables.contains(strTableName))
			htblCachedTables.put(strTableName, new Table(strTableName));
		Table table = htblCachedTables.get(strTableName);
		Record record = table.selectFromTable(strClusteringKey);
		if(record == null)
			return;
		record.update(htblColNameValue);
		saveAll();
	}
	
	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		// deleteFromTable notes:
		// htblColNameValue holds the key and value. This will be used in search to identify which rows/tuples to delete.
		// htblColNameValue enteries are ANDED together	
		if(!Schema.checkTableExists(strTableName))
			throw new DBAppException("Table " + strTableName + " does not exists.");
		if(!htblCachedTables.contains(strTableName))
			htblCachedTables.put(strTableName, new Table(strTableName));
		Table table = htblCachedTables.get(strTableName);
		table.deleteFromTable(htblColNameValue, "AND");
		saveAll();
	}
	
	@SuppressWarnings("rawtypes")
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms, String[] strarrOperators) throws DBAppException {
		return null;		// For Milestone 2
	}
	
	@SuppressWarnings("rawtypes")
	public Iterator selectFromTable(String strTableName, Hashtable<String, Object> htblColNameValue, String strOperator) throws DBAppException {
		if(!Schema.checkTableExists(strTableName))
			throw new DBAppException("Table " + strTableName + " does not exists.");
		if(!htblCachedTables.contains(strTableName))
			htblCachedTables.put(strTableName, new Table(strTableName));
		Table table = htblCachedTables.get(strTableName);
		saveAll();
		return table.selectFromTable(htblColNameValue, strOperator);
	}
	
	public void saveAll() throws DBAppException {
		try {
			FileManager.writeSchema();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<Table> it = htblCachedTables.values().iterator();
		while(it.hasNext())
			it.next().saveAllPages();
	}
	
	// Panel Render ----------------------------------------------------------------------------------
	public String renderPanel() {
		String r = "";
		
		r += " ____    ____  _____  ____  _____  ________  _______  ____   ____  _       \n";
		r += "|_   \\  /   _||_   _||_   \\|_   _||_   __  ||_   __ \\|_  _| |_  _|/ \\      \n";
		r += "  |   \\/   |    | |    |   \\ | |    | |_ \\_|  | |__) | \\ \\   / / / _ \\     \n";
		r += "  | |\\  /| |    | |    | |\\ \\| |    |  _| _   |  __ /   \\ \\ / / / ___ \\    \n";
		r += " _| |_\\/_| |_  _| |_  _| |_\\   |_  _| |__/ | _| |  \\ \\_  \\ ' /_/ /   \\ \\_  \n";
		r += "|_____||_____||_____||_____|\\____||________||____| |___|  \\_/|____| |____| \n";
		r += "                                                                            \n";
		
		return r;
	}
}


















