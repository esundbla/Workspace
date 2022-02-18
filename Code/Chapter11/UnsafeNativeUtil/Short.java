    class UnsafeNativeUtil{
        static{
            System.loadLibrary("UnsafeNativeUtil");
        }

        final static int    SEEK_SET = 0,
                            SEEK_CUR = 1,
                            SEEK_END = 2;
                            
        //---------- memory bindings ----------
        static native long malloc(int size);
        static native void free(long pointer);
        static native byte read(long pointer);  
        static native void write(long pointer, byte value); 
        static native void memcpy(long dest, long src, int count);
        static native void memset(long dest, int value, int count);
        
        //---------- ByteBuffer ---------------
        static native ByteBuffer    wrapbuf(long pointer, long size);
        static native long          getbuf(ByteBuffer byteBuffer);
        
        //---------- file bindings ------------
        static native FilePointer fopen(String filename, String mode);
        static native int fclose(FilePointer stream);
        static native int fread(long buffer, int size, int count,
            FilePointer stream);
        static native int fwrite(long buffer, int size, int count,
            FilePointer stream);
        static native int feof(FilePointer stream); 
        static native int fseek(FilePointer stream, int offset,
            int origin); 
        static native int ftell(FilePointer stream); 
    }

    //---------- memory bindings ----------
    long malloc(int size);
    void free(long pointer);
    byte read(long pointer);  
    void write(long pointer, byte value); 
    void memcpy(long dest, long src, int count);
    void memset(long dest, int value, int count);
    
    //---------- ByteBuffer ---------------
    ByteBuffer    wrapbuf(long pointer, long size);
    long          getbuf(ByteBuffer byteBuffer);

    //---------- file bindings ------------
    FilePointer fopen(String filename, String mode);  
    int fclose(FilePointer stream);
    int fread(long buffer, int size, int count, FilePointer stream);
    int fwrite(long buffer, int size, int count, FilePointer stream);
    int feof(FilePointer stream); 
    int fseek(FilePointer stream, int offset, int origin); 
    int ftell(FilePointer stream); 


    ByteBuffer bBuffer = UnsafeNativeUtil.wrapbuf(buffer, bufferSize)
    
    // modify the data starting at 1/3 into teh file and stop at 2/3
    for(int i=bBuffer.capacity()/3; i<bBuffer.capacity()*2/3; i++){
        
        // read/write memory through the direct buffer object
        if (bBuffer.get(i) == (byte)255){
            bBuffer.put(i, (byte)192);
        // read/write mem directly by using the actual memory address
        }else if (UnsafeNativeUtil.read(buffer+i) == (byte)0){
            UnsafeNativeUtil.write(buffer+i, (byte)255);
        }
    }

    // write the modified buffer to a file
    FilePointer fileOutput2 = UnsafeNativeUtil.fopen("Output2.bmp", "wb");
    UnsafeNativeUtil.fwrite(buffer, 1, bufferSize, fileOutput2);
    UnsafeNativeUtil.fclose(fileOutput2);       



    ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(bufferSize);
    long byteBuffer2Buffer = UnsafeNativeUtil.getbuf(byteBuffer2);
    UnsafeNativeUtil.memcpy(byteBuffer2Buffer, buffer, 
        byteBuffer2.capacity());
    UnsafeNativeUtil.memset(byteBuffer2Buffer + bufferSize/3, 
        (byte)255, bufferSize/3);
    
    // write the buffer to a file 
    FilePointer fileOutput3 = 
        UnsafeNativeUtil.fopen("Output3.bmp", "wb");
    UnsafeNativeUtil.fwrite(byteBuffer2Buffer, 1, 
        byteBuffer2.capacity(), fileOutput3);
    UnsafeNativeUtil.fclose(fileOutput3);
    
    // free memory that was allocated using malloc
    UnsafeNativeUtil.free(buffer);
