package impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import PBStorage.PBStorage;
import ptcFramework.ConsumerIterator;
import utilities.LHConfig;

public class InsertTupleIntoStorage implements ConsumerIterator<byte[]> {
	
	private static LHConfig linearHashConfig;
	private static HashMap<Integer, Integer> lToPMap;
	private static PBStorage pbStorage = new PBStorage();
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] next() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ptcFramework.ConsumerIterator#open()
	 *  Reads Config files and Loads PBStorage
	 */
	@Override
	public void open() throws Exception {
		// TODO Auto-generated method stub
		linearHashConfig = PBStorageHelper.fetchConfigFile();
		lToPMap = PBStorageHelper.fetchLToPMapFromFile(linearHashConfig);
		pbStorage.LoadStorage(linearHashConfig.getStorageDir());
		/*
		System.out.println("L to P Map :");
		System.out.println(lToPMap);
		System.out.println("Linear Hash Config :");
		System.out.println(linearHashConfig);
		*/
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		PBStorageHelper.updateLHConfigFile(linearHashConfig);
		PBStorageHelper.updateLToPMap(linearHashConfig, lToPMap);
	}

	@Override
	public void next(byte[] c, int id) throws Exception {
		// TODO Auto-generated method stub		
		int chainHead = getChainAddress(id);
		int pageAddress = canInsertRecord(chainHead);
		// -1 : new page has to be created else add to existing page
		// System.out.println("Can insertRecord " + pageAddress);
		if(pageAddress >= 0) { 
			byte[] page = new byte[linearHashConfig.getPageSize()];
			pbStorage.ReadPage(pageAddress, page);
			byte[] recordsCountArr = new byte[4];
			for(int i = 0; i < 4; i++) recordsCountArr[i] = page[i];
			int recordsCount = ByteBuffer.wrap(recordsCountArr).getInt();
			int offSet = 24 + (recordsCount * linearHashConfig.getRecordSize());
			// System.out.println("Current Number of Records : " + recordsCount + " Offset " + offSet);
			for(int i = 0; i < c.length; i++) { 
				page[i + offSet] = c[i];
			}
			recordsCount++;
			recordsCountArr = ByteBuffer.allocate(4).putInt(recordsCount).array();
			for(int i = 0; i < 4; i++) { 
				page[i] = recordsCountArr[i];
			}
			for(int i = 0; i < 4; i++) { 
				recordsCountArr[i] = page[i];
			}
			//System.out.println("Incrementing Number of Records : " + ByteBuffer.wrap(recordsCountArr).getInt());
			pbStorage.WritePage(pageAddress, page);
		} else { 
			if(!haveToSplit()) { 
				System.out.println("# Creating a new page with out splitting");
				int newPageAddress = pbStorage.AllocatePage();
				updateNextPageAddress(newPageAddress, -1);
				int getLastPageInChain = getLastPageInChain(chainHead);
				//System.out.println("Last Page in the Chain " + getLastPageInChain);
				updateNextPageAddress(getLastPageInChain, newPageAddress);
				next(c, id);
				linearHashConfig.setCurrentNumOfPages(linearHashConfig.getCurrentNumOfPages() + 1);
			} else { 
				System.out.println("# Creating a new page with splitting");
				splitTheChain(linearHashConfig.getsP());
				next(c, id);
			}
		}
	}
	
	
	/**
	 * @param chainHead
	 * Splits the chain, reinserts all the records in the chain
	 */
	public void splitTheChain(int chainHead) { 
		System.out.println("# Spliting the Chain " + chainHead);
		List<Integer> pagesToDeallocate = new ArrayList<Integer>();
		List<byte[]> recordsToReInsert = new ArrayList<byte[]>();
		insertRecordBytesToArray(chainHead, recordsToReInsert, pagesToDeallocate);
		for(int pageId : pagesToDeallocate) { 
			System.out.println("# Deallocating the Chain " + pageId);
			deallocatePage(pageId);
			linearHashConfig.setCurrentNumOfPages(linearHashConfig.getCurrentNumOfPages() - 1);
		}
		try {
			int newChainHead = pbStorage.AllocatePage();
			updateNextPageAddress(newChainHead, -1);
			lToPMap.put(chainHead, newChainHead);
			linearHashConfig.setCurrentNumOfPages(linearHashConfig.getCurrentNumOfPages() + 1);
			int previousM = linearHashConfig.getM();
			for(int i = previousM; i < 2*previousM; i++) { 
				lToPMap.put(i, pbStorage.AllocatePage());
				//System.out.println(i + " " + lToPMap.get(i));
				updateNextPageAddress(lToPMap.get(i), -1);
				linearHashConfig.setCurrentNumOfPages(linearHashConfig.getCurrentNumOfPages() + 1);
			}
			linearHashConfig.setM(linearHashConfig.getM() * 2);
			linearHashConfig.setsP(linearHashConfig.getsP() + 1);
			linearHashConfig.setACL(getACL());
			for(byte[] rec : recordsToReInsert) { 
				byte[] id = new byte[4];
				for(int i = 0; i < 4; i++) id[i] = rec[i];
				next(rec, ByteBuffer.wrap(id).getInt());
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deallocatePage(int pageId) { 
		try {
			pbStorage.WritePage(pageId, new byte[linearHashConfig.getPageSize()]);
			pbStorage.DeAllocatePage(pageId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getACL() { 
		return (1.0 * linearHashConfig.getCurrentNumOfPages())/(linearHashConfig.getM() + linearHashConfig.getsP());
	}
	
	
	/**
	 * @param pageAddress
	 * @param recordsList
	 * @param pagesToDeallocate
	 *  Traverses through the chain, stores all the records along with the pages that need to be deallocated
	 */
	public void insertRecordBytesToArray(int pageAddress, List<byte[]> recordsList, List<Integer> pagesToDeallocate) { 
		pagesToDeallocate.add(pageAddress);
		byte[] page = new byte[linearHashConfig.getPageSize()];
		try {
			pbStorage.ReadPage(pageAddress, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] nextPageAddressArr = new byte[4];
		for(int i = 0; i < 4; i++) nextPageAddressArr[i] = page[i + 4];
		int nextPageAddress = ByteBuffer.wrap(nextPageAddressArr).getInt();
		byte[] recordsCountArr = new byte[4];
		for(int i = 0; i < 4; i++) recordsCountArr[i] = page[i];
		int recordsCount = ByteBuffer.wrap(recordsCountArr).getInt();
		for(int i = 24; i < (recordsCount * 42) + 24; i += 42) { 
			byte[] record = new byte[42];
			for(int j = 0; j < 42; j++) { 
				record[j] = page[i + j];
			}
			recordsList.add(record);
		}
		if(nextPageAddress != -1) insertRecordBytesToArray(nextPageAddress, recordsList, pagesToDeallocate);
	}
	
	
	
	/**
	 * @param pageAddress
	 * @return Take Page address and return the last page in the chain, Traverses through the chain and returns the last page
	 */
	public int getLastPageInChain(int pageAddress) { 
		byte[] page = new byte[linearHashConfig.getPageSize()];
		try {
			pbStorage.ReadPage(pageAddress, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] nextPageAddressArr = new byte[4];
		for(int i = 0; i < 4; i++) nextPageAddressArr[i] = page[i + 4];
		int nextPageAddress = ByteBuffer.wrap(nextPageAddressArr).getInt();
		byte[] recordsCountArr = new byte[4];
		for(int i = 0; i < 4; i++) recordsCountArr[i] = page[i];
		int recordsCount = ByteBuffer.wrap(recordsCountArr).getInt();
		//System.out.println("Records Count in Page : " + recordsCount + " page address " + pageAddress + " Next Page " + nextPageAddress);
		/*
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		if(nextPageAddress == -1) return pageAddress;
		else return getLastPageInChain(nextPageAddress);
		//return recordsCount == linearHashConfig.getMAX_RECORDS_PER_PAGE() && nextPageAddress == 0 && recordsCount != 0 ? pageAddress : getLastPageInChain(nextPageAddress);
	}
	
	
	/**
	 * @param pageToBeUpdated
	 * @param nextPageAddress
	 * 
	 * Updates the next page bytes with the given next page address
	 */
	public static void updateNextPageAddress(int pageToBeUpdated, int nextPageAddress) { 
		byte[] page = new byte[linearHashConfig.getPageSize()];
		try {
			pbStorage.ReadPage(pageToBeUpdated, page);
			byte[] nextPageByteArr = ByteBuffer.allocate(4).putInt(nextPageAddress).array();
			for(int i = 0; i < 4; i++) { 
				page[i + 4] = nextPageByteArr[i];
			}
			pbStorage.WritePage(pageToBeUpdated, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return check whether we have to split when adding a new page
	 */
	public boolean haveToSplit() { 
		return linearHashConfig.getACL_Max() <= (1.0 * (linearHashConfig.getCurrentNumOfPages() + 1))/(linearHashConfig.getM() + linearHashConfig.getsP());
	}
	
	/**
	 * @param pageAddress
	 * @return Checks if a record can be inserted into the given page address
	 */
	public int canInsertRecord(int pageAddress) { 
		byte[] page = new byte[linearHashConfig.getPageSize()];		
		try {
			pbStorage.ReadPage(pageAddress, page);
			byte[] nextPageAddressArr = new byte[4];
			for(int i = 0; i < 4; i++) nextPageAddressArr[i] = page[i + 4];
			int nextPageAddress = ByteBuffer.wrap(nextPageAddressArr).getInt();
			byte[] recordsCountArr = new byte[4];
			for(int i = 0; i < 4; i++) recordsCountArr[i] = page[i];
			int recordsCount = ByteBuffer.wrap(recordsCountArr).getInt();
			//System.out.println("Records persent in the page " + recordsCount + " Page Address " + nextPageAddress);
			if(nextPageAddress == -1) { 
				if(recordsCount < linearHashConfig.getMAX_RECORDS_PER_PAGE()) return pageAddress;
				else return -1;
			} else return canInsertRecord(nextPageAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;
	}
	
	
	/**
	 * @param id
	 * @return Based on id returns the chain head address
	 */
	public int getChainAddress(int id) { 
		int chainIndex = id % (linearHashConfig.getM());
		if(lToPMap == null) System.out.println("L to p map is null");
		//System.out.println("id " + id + " Chain Index " + chainIndex + " Page Address " + lToPMap.get(chainIndex));
		return (lToPMap.get(chainIndex));
	}

}
