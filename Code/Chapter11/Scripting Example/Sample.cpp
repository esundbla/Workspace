
#include <jni.h>


#define OPTIONS_SIZE 2

JavaVM* g_jvm = 0;
JNIEnv* g_env = 0;

int StartVM();
void StopVM();
void InitializeJavaConstants();
void InitializeJavaScriptBase();
int LaunchScript(char* scriptname);
void RegisterNativeMethods(jclass clazz);

//----------------- Engine data ------------------------
int nextUnitId = 1000;

//----------------- Java class, method, field ----------------------------
jclass classScriptBase = 0;
jclass classScript = 0;
jclass classUnit = 0;
jmethodID methodUnit_Unit_default = 0;
jmethodID methodScriptBase_run = 0;
jfieldID fieldUnit_id = 0;

jobject objectScript = 0;

//----------------- functions mapped to java native methods ---------------
jobject Java_ScriptBase_createUnit(JNIEnv *, jclass, jint);
void Java_ScriptBase_destroyUnit(JNIEnv *, jclass, jobject);


int main()
{
    printf("---- main\n");

    int result = StartVM();
    if (result){
        printf("==== StartVM returned error\n");
        return 2;
    } 

    InitializeJavaConstants();
    InitializeJavaScriptBase();


    LaunchScript("Script1");    

    StopVM();
    
    return 0;
}


int StartVM(){
    printf("---- StartVM\n");
    jint result = 0;


    
    JavaVM* createdVms[1] = {0};
    jsize numberOfVMs;
    result = JNI_GetCreatedJavaVMs(createdVms, 1, &numberOfVMs);
    if (result)
    {
        printf("Error: JNI_GetCreatedJavaVMs failed\n");
        return -1;
    }
    
    if (numberOfVMs > 0) {
        printf("Note: existing vm is being reused\n");
        g_jvm = createdVms[0];
        g_jvm->GetEnv((void**)&g_env, JNI_VERSION_1_4);

        if (numberOfVMs > 1) {
            printf("Warning: JNI_GetCreatedJavaVMs indicated multiple existing vms. reusing createdVms[0]");
        }
        return 0;
    }
    
//  JavaVMOption jvmOptions[OPTIONS_SIZE] = {   
//      { "classpath", (void*)";.;" }, 
//      { "verbose", (void*)"jni" }
//  };

    JavaVMInitArgs jvmArgs;
    jvmArgs.version = JNI_VERSION_1_4; 
    jvmArgs.ignoreUnrecognized = JNI_TRUE; 
//  jvmArgs.options = jvmOptions;
//  jvmArgs.nOptions = OPTIONS_SIZE;


    result = JNI_CreateJavaVM(&g_jvm, (void**)&g_env, &jvmArgs );

    if (result) {
        printf("Error:  JNI_CreateJavaVM failed, result %d\n", result);
        return -1;
    }

    return 0;
}

void StopVM(){
    printf("---- StopVM\n");
    if (g_env){
        if (g_env->ExceptionOccurred()) {
            g_env->ExceptionDescribe();
        }
    }

    if (g_jvm) {
        /**
         *  NOTE:
         *  due to a bug in Invocation API, 
         *  Destroying the VM does not allow the same process to 
         *  create a VM again (or retrieve it)
         */
        
        jint result = g_jvm->DestroyJavaVM();

        if(result){
            printf("Error: g_jvm->DestroyJavaVM() FAILED\n");
        }
    }

    g_jvm = 0;
    g_env = 0;
}

void InitializeJavaConstants(){
    classScriptBase =  g_env->FindClass("ScriptBase");
    methodScriptBase_run = g_env->GetMethodID(classScriptBase, "run", "()V");

    classUnit = g_env->FindClass("Unit");
    methodUnit_Unit_default = g_env->GetMethodID(classUnit, "<init>", "()V");
    fieldUnit_id = g_env->GetFieldID(classUnit, "id", "I");
}

void InitializeJavaScriptBase(){
    RegisterNativeMethods(classScriptBase);
    jmethodID mid = g_env->GetStaticMethodID(classScriptBase, "initialize", "()V");
    g_env->CallStaticVoidMethod(classScriptBase, mid);
}

int LaunchScript(char* scriptname){
    
    classScript =  g_env->FindClass(scriptname);
    objectScript = g_env->NewObject(classScript, g_env->GetMethodID(classScript, "<init>", "()V"));
    jmethodID mid = g_env->GetMethodID(classScript, "run", "()V");

    g_env->CallVoidMethod(objectScript, mid);

    return 0; 
}

void RegisterNativeMethods(jclass clazz){

    const int nMethods = 2;

    JNINativeMethod methods[nMethods] = {0};

    methods[0].name = "createUnit";
    methods[0].signature = "(I)LUnit;";
    methods[0].fnPtr = Java_ScriptBase_createUnit;

    methods[1].name = "destroyUnit";
    methods[1].signature = "(LUnit;)V";
    methods[1].fnPtr = Java_ScriptBase_destroyUnit;


    jint result = g_env->RegisterNatives(clazz, methods, nMethods); 

    if(result){
        printf("Error: failed to register native methods");
    }

}

jobject Java_ScriptBase_createUnit(JNIEnv *env, jclass clazz, jint type){
    printf("Java_ScriptBase_createUnit was called. type %d\n", type);
    
    jobject unit = g_env->NewObject(classUnit, methodUnit_Unit_default);
    g_env->SetIntField(unit, fieldUnit_id, nextUnitId++);
    return unit;
}

void Java_ScriptBase_destroyUnit(JNIEnv *, jclass, jobject unit){
    printf("Java_ScriptBase_destroyUnit was called.\n");

    printf("deleting unit with id: %d\n", g_env->GetIntField(unit, fieldUnit_id));
}

/*
In chapter [XXXX] we talk about using Java as the scripting language for a game. 


Lets say that you have embedded the JVM in your game engine that is written in C/C++ and you have launched the VM from native code using StartVM().  and with to use load a class called Script

 */
/****************   this is an attempt to manually free g_jvm.dll      *******************
//#include "windows.h"

//There's a comment in the g_jvm code:
//   At the moment it's only possible to have one Java VM,
//   since some of the runtime state is in global variables.


HMODULE jvmDll = GetModuleHandle("g_jvm.dll");
    if (jvmDll == NULL){
        printf("jvmDll module handle was not found\n");
        return;
    }else{
        printf("trying to free\n");
        
        for(int i=0; i< 1000000; i++){
            //decrements the reference count. When it is zero, it should be removed from the process's address space
            bool fr = FreeLibrary(jvmDll); 
            if (fr == 0) {
                printf("jvmDll module was not freed. i: %d\n", i);
            }
        }
    }

    HMODULE jvmDll2 = GetModuleHandle("g_jvm.dll");
    if (jvmDll2 != NULL){
        printf("jvmDll was not successfully freed. last error: %d,  jvmDll: %d, jvmDll2: %d\n", GetLastError(), jvmDll,  jvmDll2);
    }
*/