

import java.nio.*;

class Sample{
    
    public static void main(String args[]) {

        // -------------- duplicate file ----------------
        
        FilePointer fileInput = UnsafeNativeUtil.fopen("Input.24bit.bmp", "rb");
        FilePointer fileOutput = UnsafeNativeUtil.fopen("Output.bmp", "wb");
        
        // compute file size
        int bufferSize = 0;         
        UnsafeNativeUtil.fseek(fileInput, 0, UnsafeNativeUtil.SEEK_END);
        bufferSize = UnsafeNativeUtil.ftell(fileInput);
        UnsafeNativeUtil.fseek(fileInput, 0, UnsafeNativeUtil.SEEK_SET);

        // allocate memory and set its content to 0
        long buffer = UnsafeNativeUtil.malloc(bufferSize);
        UnsafeNativeUtil.memset(buffer, (byte)0, bufferSize);
        
        // read from input file into buffer, then write buffer to output file
        UnsafeNativeUtil.fread(buffer, 1, bufferSize, fileInput);
        UnsafeNativeUtil.fwrite(buffer, 1, bufferSize, fileOutput);
        
        UnsafeNativeUtil.fclose(fileInput);
        UnsafeNativeUtil.fclose(fileOutput);        


        
        //-------------- modify buffer data 
        ByteBuffer byteBuffer = UnsafeNativeUtil.wrapbuf(buffer, bufferSize);
        
        // modify the data starting at 1/3 into teh file and stop at 2/3
        for(int i=byteBuffer.capacity()/3; i<byteBuffer.capacity()*2/3 ; i++){
            
            // read/write memory through the direct buffer object
            if (byteBuffer.get(i) == (byte)255){
                byteBuffer.put(i, (byte)192);
                
            // read/write memory directly by using the actual memory address        
            }else if (UnsafeNativeUtil.read(buffer+i) == (byte)0){
                UnsafeNativeUtil.write(buffer+i, (byte)255);                        
            }
            
        }

        // write the modified buffer to a file
        FilePointer fileOutput2 = UnsafeNativeUtil.fopen("Output2.bmp", "wb");
        UnsafeNativeUtil.fwrite(buffer, 1, bufferSize, fileOutput2);
        UnsafeNativeUtil.fclose(fileOutput2);       


    
        //---- create a direct buffer. memcpy a buffer to its buffer, 
        //     and set the middle 3rd to 255
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(bufferSize);
        long byteBuffer2Buffer = UnsafeNativeUtil.getbuf(byteBuffer2);
        UnsafeNativeUtil.memcpy(byteBuffer2Buffer, buffer, byteBuffer2.capacity());
        UnsafeNativeUtil.memset(byteBuffer2Buffer + bufferSize/3, (byte)255, bufferSize/3);
        
        // write the buffer to a file 
        FilePointer fileOutput3 = UnsafeNativeUtil.fopen("Output3.bmp", "wb");
        UnsafeNativeUtil.fwrite(byteBuffer2Buffer, 1, byteBuffer2.capacity(), fileOutput3);
        UnsafeNativeUtil.fclose(fileOutput3);           
        
        
        
        // free memory that was allocated using malloc
        UnsafeNativeUtil.free(buffer);
    }
}