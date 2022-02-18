
#include "Sample.h"
#include "HighResolutionTimer.h"
#include <stdio.h>
#include <stdlib.h>
#include <io.h>


//#define USE_READ_BUFFER_SIZE
#define READ_BUFFER_SIZE (1024*64)

//#define USE_STDIO_BUFFER
#define STDIO_BUFFER_SIZE (1024*64)

//64K gave best results 0.05 

HighResolutionTimer timer;

JNIEXPORT void JNICALL Java_Sample_nativeTest(JNIEnv *env, jclass, jstring inputFilename, jstring outputFilename){	

	const char *inputFilenameUTF = env->GetStringUTFChars(inputFilename, NULL);
	const char *outputFilenameUTF = env->GetStringUTFChars(outputFilename, NULL);


	FILE *streamIn, *streamOut;
	bool error = 0;

	if( ((streamIn = fopen( inputFilenameUTF, "rb" )) == NULL) || 
		((streamOut = fopen( outputFilenameUTF, "wb" )) == NULL))
	{
		error = 1;
		printf( "ERROR: Java_Sample_nativeTest, Problem opening file.\n" );
	}

	env->ReleaseStringUTFChars(inputFilename, inputFilenameUTF);
	env->ReleaseStringUTFChars(outputFilename, outputFilenameUTF);

	if(error){
		return;
	}

	unsigned char *bytes = 0;
	long fileSize = 0;
	long readCount = 0;


#ifdef USE_STDIO_BUFFER
	printf("---- USE_STDIO_BUFFER, STDIO_BUFFER_SIZE: %d\n", STDIO_BUFFER_SIZE);

	char *ioBufferIn = (char*)malloc(STDIO_BUFFER_SIZE);	
	char *ioBufferOut = (char*)malloc(STDIO_BUFFER_SIZE);
	if( setvbuf( streamIn, ioBufferIn, _IOFBF, STDIO_BUFFER_SIZE ) != 0 )
		printf( "err streamIn\n" );

	if( setvbuf( streamOut, ioBufferOut, _IOFBF, STDIO_BUFFER_SIZE ) != 0 )
		printf( "err streamOut\n" );
#else
	printf("---- do not USE_STDIO_BUFFER. NO BUFFER\n");
	if( setvbuf( streamIn, NULL, _IONBF, 0 ) != 0 )
		printf( "err streamIn\n" );
	
	if( setvbuf( streamOut, NULL, _IONBF, 0 ) != 0 )
		printf( "err streamOut\n" );
#endif


#ifdef USE_READ_BUFFER_SIZE
	bytes = (unsigned char*)malloc(READ_BUFFER_SIZE);	
	printf("---- READ_BUFFER_SIZE: %d\n", READ_BUFFER_SIZE);
#else
	// compute file size
	fseek(streamIn, 0, SEEK_END);
	fileSize = ftell(streamIn);
	fseek(streamIn, 0, SEEK_SET);
	fileSize -= ftell(streamIn); 
	bytes = (unsigned char *)malloc(fileSize);
	printf("---- READ_BUFFER_SIZE is same as file size: %d\n", fileSize);
#endif 

	timer.reset();

	while(feof(streamIn) == 0){

#ifdef USE_READ_BUFFER_SIZE
		readCount = fread(bytes, sizeof(unsigned char), READ_BUFFER_SIZE, streamIn);
#else
		readCount = fread(bytes, sizeof(unsigned char), fileSize, streamIn);
#endif			
		fwrite(bytes, sizeof(unsigned char), readCount, streamOut);
	}
		
	printf("nativeTest time: %g \n", timer.getElapsedTimeInSeconds());

	fclose( streamIn );
	fclose( streamOut );

#ifdef USE_STDIO_BUFFER
	free(streamIn);
	free(streamOut);
#endif

	free( bytes );
}
