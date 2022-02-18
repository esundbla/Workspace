
#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include "UnsafeNativeUtil.h"

//-------  globals
jclass classFilePointer;   // GlobalReference to class FilePointer 
jmethodID methodFilePointer_constructor;
jfieldID fieldFilePointer_pointer;


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){

	JNIEnv *env = 0;
	vm->GetEnv((void**)&env, JNI_VERSION_1_4);

	classFilePointer = (jclass)env->NewGlobalRef(env->FindClass("FilePointer"));
	methodFilePointer_constructor = env->GetMethodID(classFilePointer, "<init>", "()V");
	fieldFilePointer_pointer = env->GetFieldID(classFilePointer, "pointer", "J");

	return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved){
	JNIEnv *env = 0;
	vm->GetEnv((void**)&env, JNI_VERSION_1_4);
	env->DeleteGlobalRef(classFilePointer);
}


JNIEXPORT jlong JNICALL Java_UnsafeNativeUtil_malloc
(JNIEnv *env, jclass clazz, jint size){
	return (jlong)(malloc(size));
}

JNIEXPORT void JNICALL Java_UnsafeNativeUtil_free
(JNIEnv *env, jclass clazz, jlong pointer){
	free((void *)pointer);
}

JNIEXPORT jbyte JNICALL Java_UnsafeNativeUtil_read
(JNIEnv *env, jclass clazz, jlong pointer){
	return	(*(unsigned char*)pointer);
}

JNIEXPORT void JNICALL Java_UnsafeNativeUtil_write
(JNIEnv *, jclass clazz, jlong pointer, jbyte value){
	(*(unsigned char*)pointer) = value;
}

JNIEXPORT void JNICALL Java_UnsafeNativeUtil_memcpy
(JNIEnv *env, jclass clazz, jlong dest, jlong src, jint size){
    memcpy((void *) dest,(void *) src, size);
}


JNIEXPORT void JNICALL Java_UnsafeNativeUtil_memset
(JNIEnv *env, jclass clazz, jlong dest, jint value, jint size){
	memset((void*)dest, value, size);
}


JNIEXPORT jobject JNICALL Java_UnsafeNativeUtil_wrapbuf
(JNIEnv *env, jclass clazz, jlong pointer, jlong size){
	return env->NewDirectByteBuffer((void*)pointer, size);
}

JNIEXPORT jlong JNICALL Java_UnsafeNativeUtil_getbuf
(JNIEnv *env, jclass, jobject object){
	return (jlong)(env->GetDirectBufferAddress(object));
}


JNIEXPORT jobject JNICALL Java_UnsafeNativeUtil_fopen
(JNIEnv *env, jclass clazz, jstring filename, jstring mode){

	jsize filenameLength = env->GetStringLength(filename); 
	char* cname = (char*)calloc(filenameLength + 1, 1);
	env->GetStringUTFRegion(filename, 0, filenameLength, cname);

	jsize modeLength = env->GetStringLength(mode); 
	char* cmode = (char*)calloc(modeLength + 1, 1);
	env->GetStringUTFRegion(mode, 0, modeLength, cmode);

	FILE *stream = fopen( cname, cmode );

	free(cname);
	free(cmode);

	//TODO: check!! return null?
	if (stream == NULL){
		printf("=====[ ERROR: file was not opened. native Java_UnsafeNativeUtil_fopen\n");
		printf("=====[ name: %s, mode: %s\n", cname, cmode);
		return NULL;
	}

	jobject obj =  env->NewObject(classFilePointer, methodFilePointer_constructor);
	env->SetLongField(obj, fieldFilePointer_pointer, (jlong)stream);
	return obj;
}


JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_fclose
(JNIEnv *env, jclass clazz, jobject filePointer){

	jlong pointer = env->GetLongField(filePointer, fieldFilePointer_pointer);	
	return fclose((FILE *)pointer);
}


JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_fread
(JNIEnv *env, jclass clazz, jlong buffer, jint size, jint count, jobject stream){

	jlong pointer = env->GetLongField(stream, fieldFilePointer_pointer);	
	return (jint)fread((void*)buffer, size, count, (FILE*)pointer);   
}


JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_fwrite
(JNIEnv *env, jclass clazz, jlong buffer, jint size, jint count, jobject stream){

	jlong pointer = env->GetLongField(stream, fieldFilePointer_pointer);	
	return (jint)fwrite((void*)buffer, size, count, (FILE*)pointer);   
}

JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_feof
(JNIEnv *env, jclass clazz, jobject stream){
	jlong pointer = env->GetLongField(stream, fieldFilePointer_pointer);	
	return feof((FILE*)pointer);
}

JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_fseek
(JNIEnv *env, jclass, jobject stream, jint offset, jint origin){
	jlong pointer = env->GetLongField(stream, fieldFilePointer_pointer);	
	return fseek((FILE*)pointer, offset, origin);
}

JNIEXPORT jint JNICALL Java_UnsafeNativeUtil_ftell
(JNIEnv *env, jclass, jobject stream){
	jlong pointer = env->GetLongField(stream, fieldFilePointer_pointer);	
	return ftell((FILE*)pointer);
}