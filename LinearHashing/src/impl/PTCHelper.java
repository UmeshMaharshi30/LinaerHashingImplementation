package impl;

import ptcFramework.ConsumerIterator;
import ptcFramework.PTCFramework;
import ptcFramework.ProducerIterator;

public class PTCHelper extends PTCFramework<byte [], byte []> {

	public PTCHelper(ProducerIterator<byte[]> producerIterator, ConsumerIterator<byte[]> consumerIterator) {
		super(producerIterator, consumerIterator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			producerIterator.open();
			consumerIterator.open();
			while(producerIterator.hasNext()) { 
				byte[] record = producerIterator.next();
				System.out.println(record.toString());
				consumerIterator.next(record);
			}
			producerIterator.close();
			consumerIterator.close();
			System.out.println("Finished loading and writing");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Errow while loading and writing");
		}
	}

}
