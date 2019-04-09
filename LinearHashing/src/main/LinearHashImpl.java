package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import impl.InsertTupleIntoStorage;
import impl.PBStorageHelper;
import impl.PTCHelper;
import impl.RecordIterator;
import impl.StoragePrinter;
import ptcFramework.ConsumerIterator;
import ptcFramework.PTCFramework;
import ptcFramework.ProducerIterator;
import utilities.LHConfig;
import utilities.Log;
import utilities.UserInput;

/**
 * @author umesh
 *
 */
public class LinearHashImpl {
	
	public LHConfig LHConfiguration;
	public long[] lTopPMap;
	public UserInput userInput;
	static PBStorageHelper pbStorageHelper = new PBStorageHelper();
	
	public static void main(String[] args) { 
		Scanner input = new Scanner(System.in);
		int menuIndex = -1;
		while(true) { 
			displayCommands();
			switch (menuIndex = input.nextInt()) {
			case 1:				
				creatingStorageFromUserConfig();
				break;
			case 2:
				loadAndWriteToStorage();
				System.out.println("#####################################################");
				break;
			case 3:
				loadAndRead();
				break;
			case 4:
				System.out.println("# Performing all operations inorder");		
				creatingStorageFromUserConfig();
				loadAndWriteToStorage();
				loadAndRead();
				break;
			case 5:
				System.out.println("# Exiting the program");		
				input.close();
				System.exit(0);
				break;
			default:
				System.out.println("# Invalid Command");
				displayCommands();
				break;
			}
		}
	}
	
	
	public static void creatingStorageFromUserConfig() { 
		System.out.println("#####################################################");
		System.out.println("# Creating Storage based on the userinput file");
		UserInput userInput = readUserConfig();
		pbStorageHelper.createStorage(userInput);
	}
	
	public static void loadAndWriteToStorage() { 
		System.out.println("#####################################################");
		System.out.println("# Loading and writing into Storage");
		ProducerIterator<byte[]> recordIterator = new RecordIterator();
		ConsumerIterator<byte[]> insertTupleIntoStorage = new InsertTupleIntoStorage();
		PTCFramework<byte[], byte[]> ptcFramework = new PTCHelper(recordIterator, insertTupleIntoStorage);
		ptcFramework.run();
	}
	
	public static void loadAndRead() { 
		System.out.println("#####################################################");
		System.out.println("# Loading and reading");
		StoragePrinter printer = new StoragePrinter();
		printer.printAllChains();
	}
	
	public static void displayCommands() {
		Log.i("#####################################################");
		Log.i("# Use the userConfig.json to enter all the user inputs");
		Log.i("# List of commands available :");
		Log.i("# Enter the index corresponding command");
		String[] commands = new String[] { "Create Storage", "Load and Write", "Load and Read", "Run Everything ", "Exit"};
		for(int i = 0; i < commands.length; i++) { 
			Log.i("# " + commands[i] + " : " + (i + 1));
		}
		Log.i("#####################################################");
	}
	
	
	/**
	 * @return Reads the User Config file
	 */
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

}
