package utilities;

public class LHConfig {
	private String fileName;
	private String storageFolder;
	private int pageSize;
	private int totalPages;
	private String homePage;
	private String lTopFile;
	private int M;
	private int sP;
	private int numOfPages;
	private double ACL_MIN;
	private double ACL_MAX;
	private double acl;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		fileName = fileName;
	}
	
	public String getStorageDir() {
		return storageFolder;
	}
	public void setStorageDir(String folder) {
		storageFolder = folder;
	}
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public String getLtoP_File() {
		return lTopFile;
	}
	public void setLtoP_File(String ltoP_File) {
		lTopFile = ltoP_File;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		M = m;
	}
	public int getsP() {
		return sP;
	}
	public void setsP(int sP) {
		this.sP = sP;
	}
	public int getNumOfPages() {
		return numOfPages;
	}
	public void setNumOfPages(int pages) {
		numOfPages = pages;
	}
	public double getACL_Min() {
		return ACL_MIN;
	}
	public void setACL_Min(double aCL_Min) {
		ACL_MIN = aCL_Min;
	}
	public double getACL_Max() {
		return ACL_MAX;
	}
	public void setACL_Max(double aCL_Max) {
		ACL_MAX = aCL_Max;
	}
	public double getACL() {
		return acl;
	}
	public void setACL(double aCL) {
		acl = aCL;
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
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	@Override
	public String toString() {
		String res = "";
		res = "{ \"FileName\" : " + this.fileName + ",";
		res += "\"homePage\" : " + this.homePage + ",";
		res += "\"storageFolder\" : " + this.storageFolder + ",";
		res += "\"PageSize\" : " + this.pageSize + ",";
		res += "\"TotalPages\" : " + this.totalPages + ",";
		res += "\"LtoP_File\" : " + this.lTopFile + ",";
		res += "\"M\" : " + this.M + ",";
		res += "\"sP\" : " + this.sP + ",";
		res += "\"numOfPages\" : " + this.numOfPages + ",";
		res += "\"ACL_MIN\" : " + this.ACL_MIN + ",";
		res += "\"ACL_MAX\" : " + this.ACL_MAX + ",";
		res += "\"acl\" : " + this.acl + "}";
		return res;
	}
	
}
