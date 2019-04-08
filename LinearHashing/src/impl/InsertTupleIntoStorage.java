package impl;

import java.util.HashMap;

import ptcFramework.ConsumerIterator;
import utilities.LHConfig;

public class InsertTupleIntoStorage implements ConsumerIterator<byte[]> {
	
	private static LHConfig linearHashConfig;
	private static HashMap<Integer, Integer> lToPMap;
	
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

	@Override
	public void open() throws Exception {
		// TODO Auto-generated method stub
		linearHashConfig = PBStorageHelper.fetchConfigFile();
		lToPMap = PBStorageHelper.fetchLToPMapFromFile(linearHashConfig);
		System.out.println("L to P Map :");
		System.out.println(lToPMap);
		System.out.println("Linear Hash Config :");
		System.out.println(linearHashConfig);
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next(byte[] c) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
