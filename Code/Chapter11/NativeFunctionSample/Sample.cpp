
#include "Sample.h"
#include <stdio.h>

JNIEXPORT void JNICALL 
Java_Sample_MyNativeFunction(JNIEnv *env, jclass clazz){


	printf("Java_Sample_MyNativeFunction\n");
	
	jclass intArrayClass = env->FindClass("[I");
	jarray myArray = env->AllocObject(intArrayClass);
	jint l = env->GetArrayLength(myArray);
	printf("%d", l);

/*	C++
	jclass intArrayClass = env->FindClass("[I");
	jobject myArray = env->AllocObject(intArrayClass);
	jint l = env->GetArrayLength(myArray);
	printf("%d", l);
*/
/*	
	//------------ Equality Test
	jobject myObject = env->NewLocalRef(clazz);
	jobject reference1 = env->NewLocalRef(myObject);
	jobject reference2 = env->NewLocalRef(myObject);
	jobject reference3 = reference1;

	// this will evaluate to **false**
	if (reference1 == reference2){
		printf("reference1 == reference2\n");
	}

	// this will evaluate to true
	if (env->IsSameObject(reference1, reference2)){
		printf("env->IsSameObject(reference1, reference2)\n");
	}

	// obviously both of these are true
	if (reference1 == reference3){
		printf("reference1 == reference3\n");
	}
	if (env->IsSameObject(reference1, reference3)){
		printf("env->IsSameObject(reference1, reference3)\n");
	}

	/*
	// this will evaluate to true if the object referred 
	// to by myWeakGlobalReferece is not live
	if (env->IsSameObject(myWeakGlobalReferece, NULL)){
		printf("env->IsSameObject(myWeakGlobalReferece, NULL)\n");
	}
	///
	printf("reference1: %d, reference2: %d, reference3: %d, myObject: %d", reference1, reference2, reference3, myObject);
*/
}
