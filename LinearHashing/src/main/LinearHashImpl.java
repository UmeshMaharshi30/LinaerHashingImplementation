package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import impl.PBStorageHelper;
import utilities.LHConfig;
import utilities.UserInput;

/**
 * @author umesh
 *
 */
public class LinearHashImpl {
	
	public LHConfig LHConfiguration;
	public long[] lTopPMap;
	public UserInput userInput;
	/*
	public static void mainOld(String[] args) {
		// TODO Auto-generated method stub
		LinearHashImpl hashImpl = new LinearHashImpl();
		// Step 1 Reading LH Configuration file and storing it in LHConfig class
		hashImpl.LHConfiguration = hashImpl.fetchConfigFile();
		if(hashImpl.LHConfiguration == null) {
			System.out.println("Invalid LHConfig file !");
			System.out.println("Exiting the program");
			System.exit(1);
		}
		//System.out.println(hashImpl.LHConfiguration);
		// Step 2 Creating/Reading lTopFile 
		hashImpl.lTopPMap = hashImpl.fetchLToPMap();
		if(hashImpl.lTopPMap == null) System.exit(2);
		for(long i : hashImpl.lTopPMap) System.out.print(i + " ");
		System.out.println("");
		hashImpl.startMagic(hashImpl);
	}
	*/
	
	public static void main(String[] args) { 
		Scanner input = new Scanner(System.in);
		PBStorageHelper pbStorageHelper = new PBStorageHelper();
		int menuIndex = -1;
		displayCommands();
		while(true) { 
			switch (menuIndex = input.nextInt()) {
			case 1:
				System.out.println("Creating Storage based on the userinput file");
				UserInput userInput = readUserConfig();
				pbStorageHelper.createStorage(userInput);
				break;
			case 2:
				System.out.println("Loading and writing into Storage");
				break;
			case 3:
				System.out.println("Loading and reading");
				break;
			case 4:
				System.out.println("Deallocating Storage");
				break;
			case 5:
				System.out.println("Exiting the program");		
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Command");
				displayCommands();
				break;
			}
		}
	}
	
	
	public static void displayCommands() {
		System.out.println("Use the userConfig.json to enter all the user inputs");
		System.out.println("List of commands available :");
		System.out.println("Enter the index corresponding command");
		String[] commands = new String[] { "Create Storage", "Load and Write", "Load and Read", "Deallocate Storage", "Exit"};
		for(int i = 0; i < commands.length; i++) { 
			System.out.println(commands[i] + " : " + (i + 1));
		}
	}
	
	
	/**
	 * @param impl LinearHash Object Containing all the configurations
	 */
	private void startMagic(LinearHashImpl impl) {
		// TODO Auto-generated method stub
		//PBStorageHelper helper = new PBStorageHelper(impl);
		// Step 3 Allocating M number of pages
		//helper.allocateMPages();
		updateLToPMap(lTopPMap);
	}

	
	
	public static UserInput readUserConfig() { 
		String path = "userConfig.json";
        BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
			Gson gson = new Gson();
	        UserInput userConfig = gson.fromJson(bufferedReader, UserInput.class);
	        //System.out.println(config.getClass());
	        //System.out.println(config.toString());
	        return userConfig;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("User Config File is not found");
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
		//int totalPages = (int)Math.ceil((1.0*totalRecords)/recordsPerPage);
		int totalPages = LHConfiguration.getM() + LHConfiguration.getsP();
		System.out.println("Total chains :" + totalPages);
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
