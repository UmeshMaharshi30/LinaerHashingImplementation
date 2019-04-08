package impl;

import java.io.File;
import java.util.Scanner;

import main.LinearHashImpl;
import ptcFramework.ProducerIterator;
import utilities.UserInput;

public class RecordIterator implements ProducerIterator<byte[]> {
	
	private static UserInput userInput = LinearHashImpl.readUserConfig();
	private static String recordsFileName = userInput.getDataDirectory() + File.pathSeparator + userInput.getDataFile(); 
	private static Scanner fileReader;
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return fileReader != null ? fileReader.hasNext() : false;
	}

	@Override
	public byte[] next() {
		// TODO Auto-generated method stub
		return fileReader.nextLine().getBytes();
	}

	@Override
	public void open() throws Exception {
		// TODO Auto-generated method stub
		fileReader = new Scanner(recordsFileName); 
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if(fileReader != null) fileReader.close(); 
	}

}
