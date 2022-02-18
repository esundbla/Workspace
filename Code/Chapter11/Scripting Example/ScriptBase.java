

public abstract class ScriptBase{
	
		
	protected ScriptBase(){
		System.out.println("ScriptBase.ScriptBase()");				
	}
	
	// ------------ defines ------------------------
	final static int 	UNIT_TYPE_PEON 		= 1,
						UNIT_TYPE_FOOTMAN 	= 2,
						UNIT_TYPE_FARM   	= 3;
	
	
	
	
	// ------------ abstract methods that have to be over written by scripts ----------------
	abstract void run();
	
	
	// ------------  native methods  --------------------
	protected final static native Unit createUnit(int type);
	protected final static native void destroyUnit(Unit unit);
	
	
	// ------------ convenient non-native methods -----------
	protected final static void print(String string){
		System.out.println(string);
	}
	
	
	// ------------ initializer called from native code before any java objects are created ------------------
	private final static void initialize(){
		System.out.println("ScriptBase.initialize()");		
	}
}

class Unit{
	private int id;		// the id can only be set from native code
	private Unit(){}	// private constructor. instances can only be made from native code.
	public int getId(){
		return id;	
	}
}