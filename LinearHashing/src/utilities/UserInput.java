package utilities;

public class UserInput {
	private String storageFolder;
	private int pageSize;
	private int totalPages;
	private String dataDirectory;
	private String dataFile;
	
	public String getStorageFolder() {
		return storageFolder;
	}
	public void setStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public String getDataDirectory() {
		return dataDirectory;
	}
	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}
	public String getDataFile() {
		return dataFile;
	}
	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	@Override
	public String toString() {
		String res = "";
		res += "{ storageFolder : " + this.storageFolder + ",";
		res += "\"PageSize\" : " + this.pageSize + ",";
		res += "\"TotalPages\" : " + this.totalPages +  "}";
		return res;
	}
}
