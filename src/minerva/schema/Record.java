package minerva.schema;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class Record implements Serializable, Comparable<Record> {
	// Attributes ------------------------------------------------------------------------------------
	private static final long serialVersionUID = 6134379594931530550L;
	private Hashtable<String, Object> htblColNameValue;
	private boolean deleted;
	
	public Date touchDate;				// TODO Work on touchDate
	public Comparable<Object> clusteringKey;
	
	// Constructor -----------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public Record(Hashtable<String, Object> htblColNameValue, String strClusteringKeyColName) {
		this.htblColNameValue = htblColNameValue;
		this.deleted = false;
		this.clusteringKey = (Comparable<Object>) htblColNameValue.get(strClusteringKeyColName);
		touchDate = new Date();
	}
	
	// Methods ---------------------------------------------------------------------------------------
	public boolean containsColumnValue (String strColName, Object value) {
		return htblColNameValue.get(strColName).equals(value);
	}

	public boolean containsAllColumnValues (Hashtable<String, Object> htblColNameValue) {
		Iterator<Entry<String, Object>> itrCurrColVal = htblColNameValue.entrySet().iterator();
		while(itrCurrColVal.hasNext()) {
			Entry<String, Object> entCurrVal = itrCurrColVal.next();
			if(!containsColumnValue(entCurrVal.getKey(), entCurrVal.getValue()))
				return false;
		}
		return true;
	}
	
	public boolean containsAnyColumnValue (Hashtable<String, Object> htblColNameValue) {
		Iterator<Entry<String, Object>> itrCurrColVal = htblColNameValue.entrySet().iterator();
		while(itrCurrColVal.hasNext()) {
			Entry<String, Object> entCurrVal= itrCurrColVal.next();
			if(containsColumnValue(entCurrVal.getKey(), entCurrVal.getValue()))
				return true;
		}
		return false;
	}
	
	public void update(Hashtable<String, Object> htblColNameValue) {
		Iterator<Entry<String, Object>> it = htblColNameValue.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, Object> temp = it.next();
			this.htblColNameValue.replace(temp.getKey(), temp.getValue());
		}
		this.touchDate = new Date();
	}
	
	// Overwriting -----------------------------------------------------------------------------------
	public int compareTo(Record r) {
		return this.clusteringKey.compareTo(r.clusteringKey);
	}
	
	public String toString() {
		return htblColNameValue.toString();
	}
	
	// Setters ---------------------------------------------------------------------------------------
	public void setDeleted (boolean deleted) {
		this.deleted = deleted;
	}
	
	// Getters ---------------------------------------------------------------------------------------
	public boolean isDeleted () {
		return this.deleted;
	}
}
