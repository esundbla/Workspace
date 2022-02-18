
class Sample{
	
	Sample(){
		System.loadLibrary("MyNativeLibrary");
	}
	
	private static native void MyNativeFunction();
		
	public static void main(String args[]) {
		Sample sample = new Sample();
		Sample.MyNativeFunction();
		
		int array[];
		array = new int[3];
		
	}
	
    Object ReferenceTest(){
        ClassA reference1 = new ClassA();
 //       ClassB referenceB = reference1;
        return reference1;
    }
    
}

class ClassA{
    int x, y, z;
}

class ClassB{
    String name;
}