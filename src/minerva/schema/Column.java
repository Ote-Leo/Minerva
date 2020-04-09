package minerva.schema;

public class Column {
	// Attributes ------------------------------------------------------------------------------------
	private String strColName, strColType;
	private boolean clusteringKey, indexed;
	
	// Constructor -----------------------------------------------------------------------------------
	public Column(String strColName, String strColType, boolean clusteringKey, boolean indexed) {
		this.strColName = strColName;
		this.strColType = strColType;
		
		this.clusteringKey = clusteringKey;
		this.indexed = indexed;
	}
	
	// Methods ---------------------------------------------------------------------------------------
	public String toString() {
		return strColName + ", " + strColType + ", " + clusteringKey + ", " + indexed;
	}
	
	// Setters ---------------------------------------------------------------------------------------
	
	public void setColName (String strColName) {
		this.strColName = strColName;
	}
	
	public void setColType (String strColType) {
		this.strColType = strColType;
	}
	
	public void setClusteringKey(boolean clusteringKey) {
		this.clusteringKey = clusteringKey;
	}
	
	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}
	
	// Getters ---------------------------------------------------------------------------------------
	
	public String getColName() {
		return this.strColName;
	}
	
	public String getColType() {
		return this.strColType;
	}
	
	public boolean isClusteringKey() {
		return this.clusteringKey;
	}
	
	public boolean isIndexed() {
		return this.indexed;
	}
}
























