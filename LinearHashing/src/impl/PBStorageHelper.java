package impl;

import main.LinearHashImpl;
import PBStorage.PBStorage;

public class PBStorageHelper {
	private LinearHashImpl hashImpl;
	private PBStorage pbStorage = new PBStorage();
	private int currentPage = 0;
	
	public PBStorageHelper(LinearHashImpl impl) {
		hashImpl = impl;
	} 
	
	public void allocateMPages() {
		// Creating a storage based on the input params
		try {
			pbStorage.CreateStorage(hashImpl.LHConfiguration.getStorageDir(), hashImpl.LHConfiguration.getPageSize(), hashImpl.LHConfiguration.getTotalPages());
			while(currentPage < hashImpl.LHConfiguration.getM()) {
				hashImpl.lTopPMap[currentPage++] = pbStorage.AllocatePage();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
