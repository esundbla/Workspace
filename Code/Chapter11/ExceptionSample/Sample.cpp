
#include "Sample.h"
#include <stdio.h>

JNIEXPORT void JNICALL 
Java_Sample_myNativeFunction(JNIEnv *env, jobject obj){
    printf("Java_Sample_MyNativeFunction\n");
    
    jclass clazz = env->GetObjectClass(obj);
    jmethodID mid = env->GetMethodID(clazz, "myMethod", "()V");

    env->CallVoidMethod(obj, mid);

    if (env->ExceptionCheck()){
        printf("An Exception has occured\n");
        env->ExceptionDescribe();
        printf("clearing Exception\n");
        env->ExceptionClear();
    }
}
