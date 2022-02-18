
public class QueryPerformanceCounter
{
	static {
		System.loadLibrary("QueryPerformanceCounter");
	}	
	
	public static native long nativeQueryPerformanceFrequency();
	public static native long nativeQueryPerformanceCounter();
	public static native void nativeShowInfo();	
}
