


import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class Sample{

	byte bytes[] = new byte[bufferSize];
	ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
	ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(bufferSize);
		
	static HighResolutionTimer timer = new HighResolutionTimer(); 	
	static final int bufferSize = 8*1024*1024;
	
	Sample(){}

	public byte bytesGet(int index){
		return bytes[index];	
	}
	
	public void bytesPut(int index, byte value){
		bytes[index] = value;	
	}
	
	
	public void run(){	
						
		byteBuffer.clear();			
		timer.reset();
		for(int i=0; i<bufferSize; i++){
			byteBuffer.put(i, byteBuffer.get(i));
		}
		System.out.println("byteBuffer, timer: " + timer.getElapsedTimeInSeconds());
				
		directByteBuffer.clear();			
		timer.reset();
		for(int i=0; i<bufferSize; i++){
			directByteBuffer.put(i, directByteBuffer.get(i));
		}
		System.out.println("directByteBuffer, timer: " + timer.getElapsedTimeInSeconds());
				
		timer.reset();
		for(int i=0; i<bufferSize; i++){
			bytesPut(i, bytesGet(i));
		}
		System.out.println("bytes[] using accessors, timer: " + timer.getElapsedTimeInSeconds());
	}

    public static void waitForGc(){
        System.out.println("waitForGc ...");
        try{
            Thread.sleep(2000);
            System.gc();
            System.runFinalization();
            Thread.sleep(2000);     
        }catch(Exception e){
            e.printStackTrace();    
        }
//      System.out.println("waitForGc end");
    }
				
	public static void main(String args[]) {
		Sample sample = new Sample();
		
		for (int c=0; c<20; c++){
			sample.run();
			System.out.println();
		}
		
		waitForGc();
		waitForGc();
		sample.run();
		System.out.println();
	}
}
