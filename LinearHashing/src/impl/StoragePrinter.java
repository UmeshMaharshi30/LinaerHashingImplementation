package impl;

import java.nio.ByteBuffer;
import java.util.HashMap;

import PBStorage.PBStorage;
import utilities.Employee;
import utilities.LHConfig;

public class StoragePrinter {
	
	LHConfig linearHashConfig = PBStorageHelper.fetchConfigFile();
	PBStorage pbStorage = new PBStorage();
	HashMap<Integer, Integer> lToPMap = PBStorageHelper.fetchLToPMapFromFile(linearHashConfig);
	HashMap<Integer, Integer> recordsCountsInEachPage = new HashMap<Integer, Integer>();
	int[] recordsInEachChain = new int[lToPMap.keySet().size()];
	int currentChain = 0;
	int totalRecords = 0;
	
	public StoragePrinter() { 
		super();
		try {
			pbStorage.LoadStorage(linearHashConfig.getStorageDir());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printAllChains() { 
		System.out.println("# Printing All Chains ");
		for(int c :  lToPMap.keySet()) { 
			//System.out.println("Printing Chain " + c + " Chain Address " + lToPMap.get(c));
			currentChain = c;
			printChain(lToPMap.get(c));
		}
		printStats();
	}
	
	public void printChain(int chainHead) {
		byte[] page = new byte[linearHashConfig.getPageSize()];
		try {
			pbStorage.ReadPage(chainHead, page);
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
		// System.out.println("Page id " + chainHead + " next page " + nextPageAddress + " records counts " + recordsCount);
		recordsCountsInEachPage.put(chainHead, recordsCount);
		recordsInEachChain[currentChain] += recordsCount;
		totalRecords += recordsCount;
		for(int i = 24; i < (recordsCount * 42) + 24; i += 42) { 
			byte[] record = new byte[42];
			for(int j = 0; j < 42; j++) { 
				record[j] = page[i + j];
			}
			//System.out.println(new Employee(record, 42).toString());
		}
		if(nextPageAddress != -1) printChain(nextPageAddress);
	}
	
	public void printStats() { 
		System.out.println("#####################################################");
		System.out.println("# Records in Each Page");
		for(int pageAdd : recordsCountsInEachPage.keySet()) { 
			System.out.println("# " + pageAdd + " " + recordsCountsInEachPage.get(pageAdd));
		}
		System.out.println("#####################################################");
		System.out.println("# Records in Each Chain");
		for(int i = 0; i < recordsInEachChain.length; i++) { 
			System.out.println("# Chain no " + i + " " + recordsInEachChain[i]);
		}
		System.out.println("# Total no of records " + totalRecords);
		System.out.println("#####################################################");
	}
	
	public StoragePrinter(LHConfig config) { 
		linearHashConfig = config;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
