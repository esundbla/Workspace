


import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class Sample{
        
    static HighResolutionTimer timer = new HighResolutionTimer(); 
                
    static{
        System.loadLibrary("Test"); 
    }
    
    public static native void nativeTest(String inputFilename, String outputFilename);
            

    // you can see the file get created and grow while copy is in progress. 
    // not a good idea. 
    public static void test0(String inputFilename, String outputFilename) throws Exception{     
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        
        int aByte;

        timer.reset();

        while((aByte = fis.read()) > -1){
            fos.write(aByte);
        }
            
        System.out.println("test0 time: " + timer.getElapsedTimeInSeconds() + ", 1 byte");

    }   
    
    // better than test 0, buffered 
    public static void test1(String inputFilename, String outputFilename) throws Exception{     
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        BufferedInputStream bis = new BufferedInputStream(fis, 1024*16);
        BufferedOutputStream bos = new BufferedOutputStream(fos, 1024*16);
        
        timer.reset();

        int aByte;

        while((aByte = bis.read()) >= 0){
            bos.write(aByte);
        }
            
        System.out.println("test1 time: " + timer.getElapsedTimeInSeconds() + ", 1 byte w/ Buffered Streams");

        // must call
        bos.flush();
    }   
    
    
    // when possible use your own buffer.
    // does the data simply get copied to the file? no, has to be moved to system meory first!
    public static void test2(String inputFilename, String outputFilename) throws Exception{
        
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        
        byte[] bytes = new byte[(int)inFile.length()];

        timer.reset();
        
        fis.read(bytes);
        
        fos.write(bytes);
        
        System.out.println("test2 time: " + timer.getElapsedTimeInSeconds() + ", bytes[] file.length");
        
    }
    
    // way faster than others! even faster than the buffer same size of the file. 16K is optimized. 
    public static void test3(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        
        byte[] bytes = new byte[1024*16];

        timer.reset();
        
        while (true){
            int count = fis.read(bytes);
            
            if (count <= 0)
                break;
            
            fos.write(bytes, 0, count);
        }
        
        System.out.println("test3 time: " + timer.getElapsedTimeInSeconds() + ", bytes[] length = " + bytes.length);
            
    }   
    
    public static void test4(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        ByteBuffer bytes = ByteBuffer.allocate((int)ifc.size());
        
        timer.reset();
        
        ifc.read(bytes);
        bytes.flip();
        ofc.write(bytes);           
        
        System.out.println("test4 time: " + timer.getElapsedTimeInSeconds() + ", non-direct buffer size of file");
            
    }   
    
    public static void test5(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        ByteBuffer bytes = ByteBuffer.allocateDirect((int)ifc.size());
        
        timer.reset();
        
        ifc.read(bytes);
        bytes.flip();
        ofc.write(bytes);           
        
        System.out.println("test5 time: " + timer.getElapsedTimeInSeconds() + ", direct buffer size of file");
            
    }   
    
    public static void test6(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        ByteBuffer bytes = ByteBuffer.allocateDirect(1024*64);
        
        timer.reset();
        
        while (true){
            bytes.clear();
            int count = ifc.read(bytes);
            
            if (count <= 0)
                break;
            
            bytes.flip();           
            ofc.write(bytes);
        }

        System.out.println("test6 time: " + timer.getElapsedTimeInSeconds() + ", direct buffer size = " + bytes.capacity());
    }   
    
    public static void test7(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        ByteBuffer bytes = ByteBuffer.allocate(1024*16);
        
        timer.reset();
        
        while (true){
            bytes.clear();
            int count = ifc.read(bytes);
            
            if (count <= 0)
                break;
            
            bytes.flip();           
            ofc.write(bytes);           
        }

        System.out.println("test7 time: " + timer.getElapsedTimeInSeconds() + ", non-direct buffer size = " + bytes.capacity());
            
    }   
    
    public static void test8(String inputFilename, String outputFilename) throws Exception{
    
        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        timer.reset();
        
        ifc.transferTo(0, ifc.size(), ofc);
        
        System.out.println("test8 time: " + timer.getElapsedTimeInSeconds() + ", file channel transfer");
    }   
    
    
    public static void test11(String inputFilename, String outputFilename) throws Exception{

        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        RandomAccessFile fos = new RandomAccessFile(outFile, "rw");
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        

        timer.reset();

        ifc.position(5);
        
         

        System.out.println("test9 time: " + timer.getElapsedTimeInSeconds() + ", MappedByteBuffer size of file");
        
        System.out.println("fis:" + fis.read());
        System.out.println("fis:" + fis.read());
        System.out.println("fis:" + fis.read());
        System.out.println("ifc pos:" + ifc.position());
    }   

 public static void test9(String inputFilename, String outputFilename) throws Exception{

        File inFile = new File(inputFilename);
        File outFile = new File(outputFilename);
        FileInputStream fis = new FileInputStream(inFile);
        RandomAccessFile fos = new RandomAccessFile(outFile, "rw");
        FileChannel ifc = fis.getChannel(); 
        FileChannel ofc = fos.getChannel(); 
        
        long sourceSize = ifc.size();
        
        MappedByteBuffer bytes1 = ifc.map(FileChannel.MapMode.READ_ONLY, 0, sourceSize);
        bytes1.load();

        MappedByteBuffer bytes2 = ofc.map(FileChannel.MapMode.READ_WRITE, 0, sourceSize);
        bytes2.load();

        timer.reset();

        bytes2.put(bytes1);

        System.out.println("test9 time: " + timer.getElapsedTimeInSeconds() + ", MappedByteBuffer size of file");
    }   

    public static void waitForGc(){
//        System.out.println("-- waitForGc begin");
        try{
            Thread.sleep(2000);
            System.gc();
            System.runFinalization();
            Thread.sleep(2000);     
        }catch(Exception e){
            e.printStackTrace();    
        }
//        System.out.println("-- waitForGc end");
    }
    

    public static void main(String args[]) {
        
        String inputFilename = "inputFile";
        String outputFilename = "outputFile";
        
        try {
            System.out.println("-----------------warmup ...");

            Sample.nativeTest(inputFilename, outputFilename);
            Sample.nativeTest(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test1(inputFilename, outputFilename);
            Sample.test1(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test2(inputFilename, outputFilename);
            Sample.test2(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test3(inputFilename, outputFilename);
            Sample.test3(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test4(inputFilename, outputFilename);
            Sample.test4(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test5(inputFilename, outputFilename);
            Sample.test5(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test6(inputFilename, outputFilename);
            Sample.test6(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test7(inputFilename, outputFilename);
            Sample.test7(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test8(inputFilename, outputFilename);
            Sample.test8(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            Sample.test9(inputFilename, outputFilename);
            Sample.test9(inputFilename, outputFilename);
            System.in.read();
            System.in.read();
            
            waitForGc();
            waitForGc();

            System.out.println("------------------benchmarks ...");

            waitForGc();
            Sample.nativeTest(inputFilename, outputFilename);

            waitForGc();
            Sample.nativeTest(inputFilename, outputFilename);



            waitForGc();
            Sample.test1(inputFilename, outputFilename);

            waitForGc();
            Sample.test1(inputFilename, outputFilename);


            
            waitForGc();
            Sample.test2(inputFilename, outputFilename);

            waitForGc();
            Sample.test2(inputFilename, outputFilename);


            
            waitForGc();
            Sample.test3(inputFilename, outputFilename);

            waitForGc();
            Sample.test3(inputFilename, outputFilename);



            waitForGc();
            Sample.test4(inputFilename, outputFilename);

            waitForGc();
            Sample.test4(inputFilename, outputFilename);



            waitForGc();
            Sample.test5(inputFilename, outputFilename);

            waitForGc();
            Sample.test5(inputFilename, outputFilename);


            waitForGc();
            Sample.test6(inputFilename, outputFilename);

            waitForGc();
            Sample.test6(inputFilename, outputFilename);


            waitForGc();
            Sample.test7(inputFilename, outputFilename);

            waitForGc();
            Sample.test7(inputFilename, outputFilename);



            waitForGc();
            Sample.test8(inputFilename, outputFilename);

            waitForGc();
            Sample.test8(inputFilename, outputFilename);



            waitForGc();
            Sample.test9(inputFilename, outputFilename);

            waitForGc();
            Sample.test9(inputFilename, outputFilename);

      }catch (Exception e){
            e.printStackTrace();        
        }       
    }
    

}
