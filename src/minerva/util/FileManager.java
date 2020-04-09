package minerva.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import minerva.schema.Column;
import minerva.schema.Schema;

public class FileManager {
	// Attributes ------------------------------------------------------------------------------------
	protected static String strUserDirectory = System.getProperty("user.dir");
	protected static String strTablesDirectory = strUserDirectory + "/data/tables/";
	private static File tempFile;
	
	// Methods ---------------------------------------------------------------------------------------
		// Methods.Group 1 ---------------------------------------------------------------------------
	public static Hashtable<String, Hashtable<String, Column>> readSchema() throws IOException {
		/*
		 * f() => {g(String: tableName) => {h(String: colName) => Column: Col}}
		 */
		Hashtable<String, Hashtable<String, Column>> htblSchema = new Hashtable<String, Hashtable<String, Column>>();
		File schemaFile = new File(strUserDirectory + "/data/metadata.csv");
		BufferedReader bf = new BufferedReader(new FileReader(schemaFile));
		String line;
		while(((line = bf.readLine()) != null) && (line.length() > 0)) {
			String[] data = line.split(", ");
			String strTableName = data[0];
			if(!htblSchema.containsKey(strTableName))
				htblSchema.put(strTableName, new Hashtable<String, Column>());
			Hashtable<String, Column> htblCurrTableColumns = htblSchema.get(strTableName);
			Column currColumn = new Column(data[1], data[2], data[3].equals("True"), data[4].equals("True"));
			htblCurrTableColumns.put(data[1], currColumn);
		}
		bf.close();
		return htblSchema;
	}
	
	public static void writeSchema() throws IOException {
		String[] data = Schema.getSchemaCSV();
		tempFile = new File(strUserDirectory + "/data/metadata.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
		for (String curr: data) {
			bw.write(curr);
			bw.newLine();
		}
		bw.close();
	}
		// Methods.Group 2 ----------------------------------------------------------------------------
	public static void createNewTable(String strTableName) {
		String strDirectory = strTablesDirectory + strTableName;
		createNewDirectory(strDirectory);
		createNewDirectory(strDirectory + "/pages");
		createNewFile(strDirectory + "/pages/" + "0.ser");
	}
	
	public static boolean createNewFile(String strPath) {
		tempFile = new File(strPath);
		try {
			tempFile.createNewFile();
		} catch (IOException e) {
			System.out.println("File: " + strPath + "; Failed to be created due to:-\n");
			e.printStackTrace();
			return false;
		}
		System.out.println("File: " + strPath + "; Was created Successfully.");
		return true;	
	}
	
		// Methods.Group 3 -----------------------------------------------------------------------------
	public static boolean createNewDirectory(String strPath) {
		tempFile = new File(strPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
			System.out.println("Directory: " + strPath + "; Was created Successfully.");
			return true;
		}
		System.out.println("Directory: " + strPath + "; Already exists!");
		return false;
	}
	
	public static boolean deleteDirectory(String strPath) {
		File directory = new File(strPath);
		if(directory.exists()) {
			File[] files = directory.listFiles();
			if(files != null) 
				for(int i = 0; i < files.length; i++) {
					if(files[i].isDirectory())
						deleteDirectory(files[i].getPath());
					else
						files[i].delete();
			}
		}
		return (directory.delete());
	}
	
		// Mehods.Group 4 --------------------------------------------------------------------------------
	public static File[] getFiles(String strPath) {
		tempFile = new File(strPath);
		return tempFile.listFiles();
	}
	
	public static int getPageFileCount(String strTableName) {
		tempFile = new File(strTablesDirectory + "/" + strTableName + "/pages");
		return tempFile.listFiles().length;
	}
	
	public static String getTablesDirectory() {
		return strTablesDirectory;
	}
}










