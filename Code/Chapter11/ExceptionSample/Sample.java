
class Sample{
	
	Sample(){
		System.loadLibrary("MyNativeLibrary");
	}
	
	private native void myNativeFunction();

	public void myMethod() throws NullPointerException{
		throw new NullPointerException("Sample.myMethod...");	
	}
		
	public static void main(String args[]) {
		Sample sample = new Sample();
		sample.myNativeFunction();		
	}
}
