package impl;

import main.LinearHashImpl;
import utilities.LHConfig;
import PBStorage.PBStorage;

public class PBStorageHelper {
	private LinearHashImpl hashImpl;
	private LHConfig LHConfiguration;
	private long[] lToPMap; 
	private PBStorage pbStorage = new PBStorage();
	private int currentPage = 0;
	private int numberOfPages;
	private int sizeOfPage;
	
	public PBStorageHelper(LinearHashImpl impl) {
		LHConfiguration = impl.LHConfiguration;
		lToPMap = impl.lTopPMap;
		numberOfPages = LHConfiguration.getTotalPages();
		sizeOfPage = LHConfiguration.getPageSize();
	} 
	
	public void allocateMPages() {
		// Creating a storage based on the input params
		try {
			pbStorage.CreateStorage(LHConfiguration.getStorageDir(), sizeOfPage,numberOfPages);
			while(currentPage < LHConfiguration.getM()) {
				lToPMap[currentPage++] = pbStorage.AllocatePage();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
