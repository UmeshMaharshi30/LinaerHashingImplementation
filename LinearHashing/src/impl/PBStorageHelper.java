package impl;

import utilities.LHConfig;
import utilities.UserInput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import PBStorage.PBStorage;


/**
 * @author umesh
 *
 */
public class PBStorageHelper {
	private LHConfig LHConfiguration;
	private PBStorage pbStorage = new PBStorage();
	private int currentPageCount = 0;
	static HashMap<Integer, Integer> lToPMap; 
	static String LinearConfigFile = "LHConfig.json";
	
	public PBStorageHelper() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return Checks whether ACL 
	 */
	public boolean checkACL() { 
		return LHConfiguration.getACL_Max() >= (1.0 * currentPageCount)/(LHConfiguration.getM() + LHConfiguration.getsP());
	}
	
	
	/**
	 * @param userInput
	 *  Using userInput configuration file creates the storage
	 */
	public void createStorage(UserInput userInput) { 
		try {
			LHConfig lhConfig = fetchConfigFile();
			lhConfig.setTotalPages(userInput.getTotalPages());
			lhConfig.setPageSize(userInput.getPageSize());
			lhConfig.setStorageDir(userInput.getStorageFolder());	
			lhConfig.setCurrentNumOfPages(0);
			lhConfig.setMAX_RECORDS_PER_PAGE((userInput.getPageSize() - 24)/lhConfig.getRecordSize());
			lhConfig.setACL(0.0);
			lhConfig.setsP(0);
			lhConfig.setM(3);
			//updateLHConfigFile(lhConfig);
			//lhConfig = fetchConfigFile();
			pbStorage.CreateStorage(userInput.getStorageFolder(), userInput.getPageSize(), userInput.getTotalPages());
			lToPMap = new HashMap<Integer, Integer>();
			for(int i = 0; i < lhConfig.getM(); i++) { 
				lToPMap.put(i, pbStorage.AllocatePage());
				updateNextPageAddress(lToPMap.get(i), -1, lhConfig, pbStorage);
				lhConfig.setCurrentNumOfPages(i + 1);
			}
			updateLHConfigFile(lhConfig);
			updateLToPMap(lhConfig, lToPMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void updateNextPageAddress(int pageToBeUpdated, int nextPageAddress, LHConfig linearHashConfig, PBStorage pbStorage) { 
		byte[] page = new byte[linearHashConfig.getPageSize()];
		try {
			pbStorage.ReadPage(pageToBeUpdated, page);
			byte[] nextPageByteArr = ByteBuffer.allocate(4).putInt(nextPageAddress).array();
			byte[] recordCountArr = ByteBuffer.allocate(4).putInt(0).array();
			for(int i = 0; i < 4; i++) { 
				page[i + 4] = nextPageByteArr[i];
			}
			for(int i = 0; i < 4; i++) { 
				page[i] = recordCountArr[i];
			}
			pbStorage.WritePage(pageToBeUpdated, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @param config
	 * @return Reads and returns the LToPMap from LToPFile
	 */
	public static HashMap<Integer, Integer> fetchLToPMapFromFile(LHConfig config) {
		String path = config.getLtoP_File();
        BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
			Gson gson = new Gson();
			//HashMap<Integer, Integer> map = (HashMap<Integer, Integer>)gson.fromJson(bufferedReader, HashMap.class);
			Type type = new TypeToken<HashMap<Integer, Integer>>(){}.getType();
	        HashMap<Integer, Integer> clonedMap = gson.fromJson(bufferedReader, type);
			//System.out.println("# Reading from previous LToPMap file");
	        return clonedMap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File is not found");
		}
		return null;
	} 
	
	/**
	 * @param map Will update and write the LToP Map file to disk
	 */
	public static void updateLToPMap(LHConfig lhConfig, HashMap<Integer, Integer> map) { 
		try (Writer writer = new FileWriter(lhConfig.getLtoP_File())) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(map, writer);
		    System.out.println("# Successfully Updated LtoP file");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("# Error while writing LtoPMap !");
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
			System.out.println("# File is not found");
		}
		return null;
	}
	
	
	/**
	 * @param lhConfig
	 *  Updates the linear hash config file
	 */
	public static void updateLHConfigFile(LHConfig lhConfig) { 
		lhConfig.setACL(((1.0 * lhConfig.getCurrentNumOfPages()))/(lhConfig.getM() + lhConfig.getsP()));
		try (Writer writer = new FileWriter(LinearConfigFile)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(lhConfig, writer);
		    System.out.println("# Successfully Updated LHConfig file");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("# Error while writing LtoPMap !");
		}
	}
	
}
