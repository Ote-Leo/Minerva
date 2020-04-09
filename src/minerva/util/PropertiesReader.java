package minerva.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	// Attributes ------------------------------------------------------------------------------------
	public static final int MAX_ROW_COUNT_IN_PAGE = readPropertyIntValue("MaximumRowsCountinPage");
	
	private static FileReader propertiesFile;
	private static Properties properties;
	private static boolean initialized = false;
	
	// Methods ---------------------------------------------------------------------------------------
	public static String readProperty(String property) {
		if(!initialized)
			init();
		return properties.getProperty(property);
	}
	
	public static int readPropertyIntValue(String property) {
		int n = Integer.parseInt(readProperty(property));
		return n;
	}
	
	private static void init() { 
		try {
			propertiesFile = new FileReader(new File(FileManager.strUserDirectory + "/config/DBApp.properties"));
			properties = new Properties();
			properties.load(propertiesFile);
			initialized = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
