
public class QueryPerformanceCounter
{
    private QueryPerformanceCounter(){} // cannot be instantiated

    public static boolean loadNativeLibrary(){
        try{
            System.loadLibrary("QueryPerformanceCounter");
        }catch(UnsatisfiedLinkError e){
            e.printStackTrace();
            return false;   
        }
        return true;
    }
            
    public static native long nativeQueryPerformanceFrequency();
    public static native long nativeQueryPerformanceCounter();
    public static native void nativeShowInfo(); 
}
