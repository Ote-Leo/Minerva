package minerva.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import minerva.schema.Page;

public class ObjectManager {
	// Methods ---------------------------------------------------------------------------------------
	public static Page readPage(String strTableName, int pageNumber) {
		File[] flTablePagesPathes = FileManager.getFiles(FileManager.strTablesDirectory + "/" + strTableName + "/pages");
		File flTemp = new File(flTablePagesPathes[pageNumber].toString());
		ObjectInputStream ois;
		Page pgResult = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(flTemp));
			pgResult = (Page) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return pgResult;
	}
	
	public static boolean writePage(String strTableName, int pageNumber, Page page) {
		File[] flTablePagesPathes = FileManager.getFiles(FileManager.strTablesDirectory + "/" + strTableName + "/pages");
		File flTemp = new File(flTablePagesPathes[pageNumber].toString());
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(flTemp));
			oos.writeObject(page);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Page: " + strTableName + "/" + pageNumber + ".ser; Was written on Successfully.");
		return true;
	}
}
