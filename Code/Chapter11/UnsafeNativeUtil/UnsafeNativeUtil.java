
/**
 *	Unsafe Native Util is intended for use by an API internally and should not 
 *
 */

import java.nio.*;

class UnsafeNativeUtil{
	
	static{
		System.loadLibrary("UnsafeNativeUtil");
	}

	final static int 	SEEK_SET = 0,
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
	static native ByteBuffer 	wrapbuf(long pointer, long size);
	static native long 			getbuf(ByteBuffer byteBuffer);		// assumes the buffer is Direct

	//---------- file bindings ------------
	static native FilePointer fopen(String filename, String mode);	
	static native int fclose(FilePointer stream);
	static native int fread(long buffer, int size, int count, FilePointer stream);
	static native int fwrite(long buffer, int size, int count, FilePointer stream);
	static native int feof(FilePointer stream); 
	static native int fseek(FilePointer stream, int offset, int origin); 
	static native int ftell(FilePointer stream); 
}

// provides type safty for a 64 bit value that is a memory address
class FilePointer{
	private long pointer; 
	private FilePointer(){}; // can only be instantiated from native code
}