
 // for more information on the functions used, please see Microsoft's documentation on
 // JOYINFOEX and GetJoyPosEx()

 // Author: Syrus Mesdaghi, syrusm@fullsail.com


#include <windows.h>
#include <iostream>
using namespace std;

#include "JoystickWin32Ex.h"

// structs that are passed to GetJoyPosEx and to which the directBuffers are mapped.
JOYINFOEX joyInfoExs[2];


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
	return JNI_VERSION_1_4;
}

JNIEXPORT jobject JNICALL Java_JoystickWin32Ex_nativeWrapJoyInfoStruct(JNIEnv *env, jclass, jint joyId){

	//TODO: throw exception instead
	if (joyId < 0 || joyId > 1){
		cout << "invalid joyId: " << joyId << endl;
		return NULL;
	}

	memset(&joyInfoExs[joyId], 0, sizeof(JOYINFOEX));
	jobject jDirectByteBuffer = env->NewDirectByteBuffer(&joyInfoExs[joyId], sizeof(JOYINFOEX)); 
	return jDirectByteBuffer;
}



JNIEXPORT jint JNICALL Java_JoystickWin32Ex_nativePoll(JNIEnv *env, jclass, jboolean pollBoth){
	
	if (pollBoth){
		int error = joyGetPosEx(JOYSTICKID1, &joyInfoExs[0]);
		error |= joyGetPosEx(JOYSTICKID2, &joyInfoExs[1]);
		return error;
	}
	
	return joyGetPosEx(JOYSTICKID1, &joyInfoExs[0]);
}

JNIEXPORT void JNICALL Java_JoystickWin32Ex_nativeShowInfo(JNIEnv *, jclass){
	MessageBox(NULL, "This dll exposes the GetJoyPosEx() and JOYINFOEX function through JNI \nThis dll demonstrates the use of DirectByteBuffers introduced in JNI 1.4 \n DirectByteBuffers are used to directly read/write two instances of the WINAPI JOYINFOEX struct \nYou can calibrate the controller from the Control Panel \n\nAuthor: Syrus Mesdaghi, syrusm@fullsail.com", "JoystickWin32Ex.dll", MB_OK|MB_ICONINFORMATION);
}
