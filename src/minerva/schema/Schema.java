package minerva.schema;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import minerva.util.FileManager;

public class Schema {
	// Attributes ------------------------------------------------------------------------------------
	private static Hashtable<String, Hashtable<String, Column>> htblSchema;
	private static boolean initialized;
	
	// Constructor -----------------------------------------------------------------------------------
	public Schema() {
		init();
	}

	/*
	public static void main(String[] args) {
		Hashtable<String, String> ColNameType;
		ColNameType = new Hashtable<String, String>();
		
		ColNameType.put("Specialization", "java.lang.String");
		ColNameType.put("Name", "java.lang.String");
		ColNameType.put("ID", "java.lang.Double");
		ColNameType.put("Z", "java.lang.Double");
		ColNameType.put("Y", "java.lang.String");
		ColNameType.put("Address", "java.lang.String");
		ColNameType.put("X", "java.lang.Double");
		
		Schema schema = new Schema();
		addNewTable("CityShop", ColNameType, "ID");
		print();
		System.out.println("==========================================================================\n");
		String[] strSchema = getSchemaCSV();
		for(int i = 0; i < strSchema.length; i++)
			System.out.println("schema[" + i + "]:\t" + strSchema[i]);

		System.out.println("checkTableExists(\"CityShop\"): " + checkTableExists("CityShop"));
		//System.out.println(getTableSchema("CityShop"));
		System.out.println("getClusteringKeyColName(\"CityShop\"): " + getClusteringKeyColName("CityShop"));
		
	}
	*/
	
	// Methods ---------------------------------------------------------------------------------------
	public static void init() {
		try {
			htblSchema = FileManager.readSchema();
			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print() {
		if(!initialized)
			init();
		Iterator<String> itTables = htblSchema.keySet().iterator();
		while(itTables.hasNext()) {
			String strTableName = itTables.next();
			System.out.println(strTableName + ", Columns: ");
			Hashtable<String, Column> currColumns = htblSchema.get(strTableName);
			Iterator<String> itColumn = currColumns.keySet().iterator();
			while(itColumn.hasNext())
				System.out.println("\t" + currColumns.get(itColumn.next()));
			System.out.println("-----------------------------------------------------------------------");
		}
	}
	
	public static boolean checkTableExists(String strTableName) {
		if(!initialized)
			init();
		return htblSchema.containsKey(strTableName);
	}
	
	public static boolean checkColExists(String strTableName, String strColName) {
		if(!initialized)
			init();
		if(!checkTableExists(strTableName))
			return false;
		return htblSchema.get(strTableName).containsKey(strColName);
	}
	
	public static Hashtable<String, Column> getTableSchema(String strTableName) {
		if(!initialized)
			init();
		return htblSchema.get(strTableName);
	}
	
	public static String getClusteringKeyColName(String strTableName) {
		Iterator<Column> it = getTableSchema(strTableName).values().iterator();
		while(it.hasNext()) {
			Column temp = it.next();
			if(temp.isClusteringKey())
				return temp.getColName();
		}
		return null;
	}
	
	public static String[] getSchemaCSV() {
		if (!initialized)
			init();
		int resultLen = 0;
		Iterator<Hashtable<String, Column>> itrSizeCounter = htblSchema.values().iterator();
		while (itrSizeCounter.hasNext()) 
			resultLen += itrSizeCounter.next().size();
		String[] strResult = new String[resultLen];
		int index = 0;
		Iterator<String> itrTable = htblSchema.keySet().iterator();
		while (itrTable.hasNext()) {
			String strTableName = itrTable.next();
			Hashtable<String, Column> htblCurrColumns = htblSchema.get(strTableName);
			Iterator<String> itrColumn = htblCurrColumns.keySet().iterator();
			while (itrColumn.hasNext())
				strResult[index++] = strTableName + ", " + htblCurrColumns.get(itrColumn.next());
		}
		return strResult;
	}
	
	public static void addNewTable(String strTableName, Hashtable<String, String> htblColNameType, String strClusteringKeyCol) {
		Hashtable<String, Column> htblTable = new Hashtable<String, Column>();
		Iterator<Entry<String, String>> itrColNameType = htblColNameType.entrySet().iterator();
		while (itrColNameType.hasNext()) {
			Entry<String, String> entCurr = itrColNameType.next();
			htblTable.put(entCurr.getKey(), new Column(entCurr.getKey(), entCurr.getValue(), entCurr.getKey().equals(strClusteringKeyCol), false));		// False for indexed; Milestone 2
		}
		htblSchema.put(strTableName, htblTable);
	}
}






























