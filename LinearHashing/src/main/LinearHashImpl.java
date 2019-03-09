package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import impl.PBStorageHelper;
import utilities.LHConfig;

/**
 * @author umesh
 *
 */
public class LinearHashImpl {
	
	public LHConfig LHConfiguration;
	public long[] lTopPMap;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinearHashImpl hashImpl = new LinearHashImpl();
		// Step 1 Reading LH Configuration file and storing it in LHConfig class
		hashImpl.LHConfiguration = hashImpl.fetchConfigFile();
		if(hashImpl.LHConfiguration == null) {
			System.out.println("Invalid LHConfig file !");
			System.out.println("Exiting the program");
			System.exit(1);
		}
		System.out.println(hashImpl.LHConfiguration);
		// Step 2 Creating/Reading lTopFile 
		hashImpl.lTopPMap = hashImpl.fetchLToPMap();
		if(hashImpl.lTopPMap == null) System.exit(2);
		for(long i : hashImpl.lTopPMap) System.out.print(i + " ");
		System.out.println("");
		hashImpl.startMagic(hashImpl);
	}
	
	
	/**
	 * @param impl LinearHash Object Containing all the configurations
	 */
	private void startMagic(LinearHashImpl impl) {
		// TODO Auto-generated method stub
		PBStorageHelper helper = new PBStorageHelper(impl);
		// Step 3 Allocating M number of pages
		helper.allocateMPages();
		updateLToPMap(lTopPMap);
	}


	/**
	 * @return Will Read/Create Linear Hash Configuration and store it in LHConfig variable
	 */
	private LHConfig fetchConfigFile() {
		String path = "LHConfig.json";
        BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
			Gson gson = new Gson();
	        LHConfig config = gson.fromJson(bufferedReader, LHConfig.class);
	        //System.out.println(config.getClass());
	        //System.out.println(config.toString());
	        return config;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File is not found");
		}
		return null;
	}
	
	public Long[] fetchLToPMapFromFile() {
		String path = LHConfiguration.getLtoP_File();
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
	public void updateLToPMap(long[] map) { 
		try (Writer writer = new FileWriter(LHConfiguration.getLtoP_File())) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(map, writer);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while writing LtoPMap !");
		}
	}
	
	public long[] fetchLToPMap() {
		int totalRecords = 300;
		int sizeOfRecord = 100;
		int pageSize = 1024;
		int recordsPerPage = (int)Math.floor((1.0*pageSize)/sizeOfRecord);
		int totalPages = (int)Math.ceil((1.0*totalRecords)/recordsPerPage);
		long[] lToPMap = new long[totalPages];
		Arrays.fill(lToPMap, -1);
		Long[] fromFile = fetchLToPMapFromFile();
		if(fromFile == null || fromFile.length == 0) {
			System.out.println("Creating a new LtoPMap file");
			updateLToPMap(lToPMap);
			return lToPMap;
		}
		lToPMap = new long[fromFile.length];
		for(int i = 0; i < fromFile.length; i++) {
			lToPMap[i] = fromFile[i];
		}
		return lToPMap;
	}

}
