


import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class Sample{
        
    static HighResolutionTimer timer = new HighResolutionTimer(); 
        
    Sample(){       
    }
    
    // word count
    public void test4() throws Exception{
        
        FileInputStream fis = new FileInputStream("input.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StreamTokenizer tokenizer = new StreamTokenizer(inputStreamReader);
        
        int wordCount = 0;
        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
            if (tokenizer.ttype == StreamTokenizer.TT_WORD){
                wordCount++;
            }
        }

        System.out.println("wordCount: " + wordCount);
    }
    
    // convert the chars of a text 
    public void test5() throws Exception{
        
        File inFile = new File("input.txt");
        File outFile = new File("output.txt");
       	File outFile8 = new File("output8.txt");
        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        FileOutputStream fos8 = new FileOutputStream(outFile8);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-16"));
        OutputStreamWriter osw8 = new OutputStreamWriter(fos8, Charset.forName("UTF-8"));
        
        char[] chars = new char[8*1024];
        
        timer.reset();  
        
        while (true){
            int count = isr.read(chars);
            
            if (count <= 0)
                break;
                
            osw.write(chars, 0, count);
            osw8.write(chars, 0, count);
        }
        

        osw.flush();
        osw8.flush();
        
        System.out.println("test5 Done: " + timer.getElapsedTimeInSeconds());
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
    }
    
    public static void main(String args[]) {
        
        Sample sample = new Sample();
        
        try {
            sample.test5();
        }catch (Exception e){
            e.printStackTrace();        
        }       
    }
}
