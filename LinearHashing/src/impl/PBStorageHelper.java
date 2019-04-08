package impl;

import main.LinearHashImpl;
import utilities.LHConfig;
import utilities.UserInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import PBStorage.PBStorage;

/**
 * @author umesh
 *
 */
/**
 * @author umesh
 *
 */
/**
 * @author umesh
 *
 */
public class PBStorageHelper {
	private LinearHashImpl hashImpl;
	private LHConfig LHConfiguration;
	private PBStorage pbStorage = new PBStorage();
	private int currentPageCount = 0;
	private int numberOfPages;
	private int sizeOfPage;
	
	static HashMap<Integer, Integer> lToPMap; 
	static String LinearConfigFile = "LHConfig.json";
	
	/*
	public PBStorageHelper(LinearHashImpl impl) {
		LHConfiguration = impl.LHConfiguration;
		lToPMap = impl.lTopPMap;
		numberOfPages = LHConfiguration.getTotalPages();
		sizeOfPage = LHConfiguration.getPageSize();
	} 
	*/
	
	public PBStorageHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	public void allocateMPages() {
		// Creating a storage based on the input params
		try {
			pbStorage.CreateStorage(LHConfiguration.getStorageDir(), sizeOfPage, numberOfPages);
			while(currentPageCount < LHConfiguration.getM()) {
				lToPMap[currentPageCount++] = pbStorage.AllocatePage();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * @return Checks whether ACL 
	 */
	public boolean checkACL() { 
		return LHConfiguration.getACL_Max() >= (1.0 * currentPageCount)/(LHConfiguration.getM() + LHConfiguration.getsP());
	}
	
	
	/**
	 * Creates a new page, add entry to the LTOP json file
	 */
	/*
	public void addNewChain() { 
		long[] ltopNew = new long[lToPMap.length + 1];
		for(int i = 0; i < lToPMap.length; i++) { 
			ltopNew[i] = lToPMap[i];
		}
		try {
			ltopNew[lToPMap.length] = pbStorage.AllocatePage();
			currentPageCount++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	*/
	
	/**
	 * Creates a new page and  
	 */
	public void addNewPage() { 
		
	}
	
	public void createStorage(UserInput userInput) { 
		try {
			LHConfig lhConfig = fetchConfigFile();
			lhConfig.setTotalPages(userInput.getTotalPages());
			lhConfig.setPageSize(userInput.getPageSize());
			lhConfig.setStorageDir(userInput.getStorageFolder());			
			lhConfig = fetchConfigFile();
			pbStorage.CreateStorage(userInput.getStorageFolder(), userInput.getPageSize(), userInput.getTotalPages());
			lToPMap = new HashMap<Integer, Integer>();
			for(int i = 0; i < lhConfig.getM(); i++) { 
				lToPMap.put(i, pbStorage.AllocatePage());
				lhConfig.setCurrentNumOfPages(i + 1);
			}
			updateLHConfigFile(lhConfig);
			updateLToPMap(lhConfig, lToPMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Long[] fetchLToPMapFromFile(LHConfig config) {
		String path = config.getLtoP_File();
        BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
			Gson gson = new Gson();
	        Long[] map = gson.fromJson(bufferedReader, Long[].class);
	        System.out.println("Reading from previous LToPMap file");
	        return map;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File is not found");
		}
		return null;
	} 
	
	/**
	 * @param map Will update and write the LHConfig file to disk
	 */
	public void updateLToPMap(LHConfig lhConfig, HashMap<Integer, Integer> map) { 
		try (Writer writer = new FileWriter(lhConfig.getLtoP_File())) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(map, writer);
		    System.out.println("Successfully Updated LtoP file");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while writing LtoPMap !");
		}
	}
	
	
	/**
	 * @return Will Read/Create Linear Hash Configuration and store it in LHConfig variable
	 */
	public static LHConfig fetchConfigFile() {
        BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(LinearConfigFile));
			Gson gson = new Gson();
	        LHConfig config = gson.fromJson(bufferedReader, LHConfig.class);
	        return config;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File is not found");
		}
		return null;
	}
	
	public static void updateLHConfigFile(LHConfig lhConfig) { 
		try (Writer writer = new FileWriter(LinearConfigFile)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(lhConfig, writer);
		    System.out.println("Successfully Updated LHConfig file");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while writing LtoPMap !");
		}
	}
	
}
