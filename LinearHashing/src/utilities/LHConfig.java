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
	private int CurrentNumOfPages;
	private double ACL_MIN;
	private double ACL_MAX;
	private double acl;
	private int recordSize;
	private int MAX_RECORDS_PER_PAGE;
	
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
	public int getCurrentNumOfPages() {
		return CurrentNumOfPages;
	}
	public void setCurrentNumOfPages(int pages) {
		CurrentNumOfPages = pages;
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
	public int getMAX_RECORDS_PER_PAGE() {
		return MAX_RECORDS_PER_PAGE;
	}
	public void setMAX_RECORDS_PER_PAGE(int mAX_RECORDS_PER_PAGE) {
		MAX_RECORDS_PER_PAGE = mAX_RECORDS_PER_PAGE;
	}
	public int getRecordSize() {
		return recordSize;
	}
	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
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
		res += "\"numOfPages\" : " + this.CurrentNumOfPages + ",";
		res += "\"ACL_MIN\" : " + this.ACL_MIN + ",";
		res += "\"ACL_MAX\" : " + this.ACL_MAX + ",";
		res += "\"acl\" : " + this.acl + ",";
		res += "\"Total Pages \" :" + this.totalPages + ",";
		res += "\"Max record Per Page \" :" + this.MAX_RECORDS_PER_PAGE + ",";
		res += "\"Record size\" :" + this.recordSize + "}";
		return res;
	}
	
}
