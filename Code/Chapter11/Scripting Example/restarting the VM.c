HRESULT CMyClass::startJVM()
{
HRESULT hResult = S_OK;

JavaVMInitArgs vm_args;
JavaVMOption options[2];
jint res;
char classpath[1024];
JavaVMAttachArgs attArgs;

attArgs.version = JNI_VERSION_1_2;
attArgs.name = NULL;
attArgs.group = NULL;

sprintf(classpath, "-Djava.class.path=%s", USER_CLASSPATH);

options[0].optionString = "-Djava.compiler=NONE";
options[1].optionString = classpath;
options[2].optionString = "-Djava.library.path=";
options[3].optionString = "-verbose:jni";

vm_args.version = JNI_VERSION_1_2;
vm_args.options = options;
vm_args.nOptions = sizeof(options)/sizeof(options[0]);
vm_args.ignoreUnrecognized = TRUE;

JavaVM* vmBuf[10];
jsize bufLen = 10;
jsize nVMs;
res = JNI_GetCreatedJavaVMs(vmBuf, bufLen, &nVMs);
if (res < 0)
{
TRACE("None Java VM is created");
}
else if (nVMs == 1)
{
TRACE("A Java VM is already created.");
m_jvm = vmBuf[0];
}
else if (nVMs == 0)
{
/* Note that in JDK 1.2, there is no longer any need to call 
* JNI_GetDefaultJavaVMInitArgs. 
*/
// Create the Java VM 
res = JNI_CreateJavaVM(&m_jvm,(void**)&m_env, &vm_args);
if (res < 0)
{
TRACE("Can't create Java VM");
return CMyHResults::E_CANNOT_CREATE_JVM;
}
}
else if (nVMs > 1)
{
CString strOut;
strOut.Format("Java VMs created %d", nVMs);
TRACE(strOut);
return CMyHResults::E_MORE_THAN_ONE_JVM_EXISTS;
}

res = getJVM()->AttachCurrentThread((void**)&m_env, &attArgs);
if (res < 0) {
TRACE("Can't attach current thread");
return CMyHResults::E_CANNOT_ATTACH_CURRENT_THREAD;
} 

return hResult;
}
