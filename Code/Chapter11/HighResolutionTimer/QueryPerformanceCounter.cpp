
// Author: Syrus Mesdaghi, syrusm@fullsail.com

#include "windows.h"
#include "QueryPerformanceCounter.h"

LARGE_INTEGER	largeInt;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
	return JNI_VERSION_1_1;
}

JNIEXPORT jlong JNICALL Java_QueryPerformanceCounter_nativeQueryPerformanceFrequency(JNIEnv *, jclass){
	QueryPerformanceFrequency(&largeInt);
	return largeInt.QuadPart;
}

JNIEXPORT jlong JNICALL Java_QueryPerformanceCounter_nativeQueryPerformanceCounter(JNIEnv *, jclass){
	QueryPerformanceCounter(&largeInt);
	return largeInt.QuadPart;
}

JNIEXPORT void JNICALL Java_QueryPerformanceCounter_nativeShowInfo(JNIEnv *, jclass){
	MessageBox(NULL, "This library exposes the QueryPerformanceFrequency and QueryPerformanceCounter functions through JNI\n Author: Syrus Mesdaghi, syrusm@fullsail.com", "QueryPerformanceCounter", MB_OK|MB_ICONINFORMATION);
}
