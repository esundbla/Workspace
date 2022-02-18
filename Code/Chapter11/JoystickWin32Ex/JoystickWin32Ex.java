
 // Author: Syrus Mesdaghi, syrusm@fullsail.com
 
/**
 * This is the struct that gets passed to GetJoyPosEx.
 * When you fill in the byteBuffer, besure to fill it in accordingly.
 *
 * 
 *	// for more information see Microsoft's Documentation on JOYINFOEX and GetJoyPosEx
 *  // and MMSYSTEM.H which is part of WINAPI
 *
 *	typedef struct joyinfoex_tag { 
 *	    DWORD dwSize; 				// size of the struct
 *	    DWORD dwFlags; 				// flags that specify which data should be read,.....
 *	    DWORD dwXpos; 				// axis 1
 *	    DWORD dwYpos; 				// axis 2
 *	    DWORD dwZpos; 				// axis 3
 *	    DWORD dwRpos; 				// axis 4	
 *	    DWORD dwUpos; 				// axis 5
 *	    DWORD dwVpos; 				// axis 6
 *	    DWORD dwButtons; 			// masked button states
 *	    DWORD dwButtonNumber; 		// number of buttons held down
 *	    DWORD dwPOV; 				// point of view (8 digital)
 *	    DWORD dwReserved1; 			
 *	    DWORD dwReserved2; 
 *	} JOYINFOEX; 
 *	
 **/
  
 //Note: if you wish to query the device capabilities, you can wrap JOYCAPS and joyGetDevCaps similar to how i've wrapped JOYINFOEX and GetJoyPosEx.
 //Note: for more error info you can make the bytebuffer one DWORD larger and use the last fied to store the error from the c side and read it from the java side
 
  
import java.nio.*;    

public class JoystickWin32Ex {
	
	
	// NOTE: instad of using a constructor, the following is ran when the class is loaded	
	static {
		System.loadLibrary("joystickWin32Ex");
		initialize();
	}	
	
	
 	// these IDs are computed from the JOYINFOEX struct, since every field is a DWORD (4 bytes), the IDs are
 	// increments of 4 from 
 	public static final int	FIELD_INDEX_dwSize			= 0,
 							FIELD_INDEX_dwFlags 		= 4,
 							FIELD_INDEX_dwXpos 		= 8,
 							FIELD_INDEX_dwYpos 		= 12,
 							FIELD_INDEX_dwZpos 		= 16,
 							FIELD_INDEX_dwRpos 		= 20,
 							FIELD_INDEX_dwUpos 		= 24,
 							FIELD_INDEX_dwVpos 		= 28,
 							FIELD_INDEX_dwButtons 		= 32,
 							FIELD_INDEX_dwButtonNumber = 36,
 							FIELD_INDEX_dwPOV 			= 40,
 							FIELD_INDEX_dwReserved1 	= 44,
 							FIELD_INDEX_dwReserved2 	= 48;
 							
 							
 	
								//------------ Button Masks ----	
	public static final int 	JOY_BUTTON1         = 0x0001,
								JOY_BUTTON2         = 0x0002,
								JOY_BUTTON3         = 0x0004,
								JOY_BUTTON4         = 0x0008, 
								JOY_BUTTON5         = 0x00000010,
								JOY_BUTTON6         = 0x00000020,
								JOY_BUTTON7         = 0x00000040,
								JOY_BUTTON8         = 0x00000080,
								JOY_BUTTON9         = 0x00000100,
								JOY_BUTTON10        = 0x00000200,
								JOY_BUTTON11        = 0x00000400,
								JOY_BUTTON12        = 0x00000800,
								JOY_BUTTON13        = 0x00001000,
								JOY_BUTTON14        = 0x00002000,
								JOY_BUTTON15        = 0x00004000,
								JOY_BUTTON16        = 0x00008000,
								JOY_BUTTON17        = 0x00010000,
								JOY_BUTTON18        = 0x00020000,
								JOY_BUTTON19        = 0x00040000,
								JOY_BUTTON20        = 0x00080000,
								JOY_BUTTON21        = 0x00100000,
								JOY_BUTTON22        = 0x00200000,
								JOY_BUTTON23        = 0x00400000,
								JOY_BUTTON24        = 0x00800000,
								JOY_BUTTON25        = 0x01000000,
								JOY_BUTTON26        = 0x02000000,
								JOY_BUTTON27        = 0x04000000,
								JOY_BUTTON28        = 0x08000000,
								JOY_BUTTON29        = 0x10000000,
								JOY_BUTTON30        = 0x20000000,
								JOY_BUTTON31        = 0x40000000,
								JOY_BUTTON32        = 0x80000000,
								
								//------------ Axis Masks ------
								JOY_AXISX			= FIELD_INDEX_dwXpos,
								JOY_AXISY			= FIELD_INDEX_dwYpos,
								JOY_AXISZ			= FIELD_INDEX_dwZpos,
								JOY_AXISR			= FIELD_INDEX_dwRpos,
								JOY_AXISU			= FIELD_INDEX_dwUpos,
								JOY_AXISV			= FIELD_INDEX_dwVpos,
								
								// ----------- Flags -----------
								JOY_RETURNX			= 0x00000001,
								JOY_RETURNY			= 0x00000002,
								JOY_RETURNZ			= 0x00000004,
								JOY_RETURNR			= 0x00000008,
								JOY_RETURNU			= 0x00000010,
								JOY_RETURNV			= 0x00000020,
								JOY_RETURNPOV		= 0x00000040,
								JOY_RETURNBUTTONS	= 0x00000080,
								JOY_RETURNRAWDATA	= 0x00000100,
								JOY_RETURNPOVCTS	= 0x00000200,
								JOY_RETURNCENTERED	= 0x00000400,
								JOY_USEDEADZONE		= 0x00000800,
								JOY_RETURNALL		=  (JOY_RETURNX | JOY_RETURNY | JOY_RETURNZ | 
														JOY_RETURNR | JOY_RETURNU | JOY_RETURNV | 
														JOY_RETURNPOV | JOY_RETURNBUTTONS);								
							
							
	public static final int JOYERR_NOERROR 			= 0,
							JOYERR_UNPLUGGED		= 160,
							MMSYSERR_BADDEVICEID	= 2,
							MMSYSERR_NODRIVER		= 6,
							MMSYSERR_INVALPARAM		= 11;


	public static final float 	AXIS_NORMALIZATION_FACTOR = 2.0f/((float)0xffff);
 	

	// buffers mapped to an instance of the JOYINFOEX struct
	private static ByteBuffer 	joyInfoStructs[];

	//errors from last call to pollData
	private static int errors;
	
	
	// dont allow an instance of this class to be made
	private JoystickWin32Ex(){}
	
	
	private static void initialize(){
		joyInfoStructs = new ByteBuffer[2];
		
		
		for(int i=0; i<2; i++){
			// get a directBuffer mapped to an instance of the JOYINFOEX struct.
			// NOTE: changing values in teh buffer changes the fields of JOYINFOEX directly. (and vice versa)
			joyInfoStructs[i] = (ByteBuffer)nativeWrapJoyInfoStruct(i);
			
			// set it's byte order so when we read multibyte values such as ints, 
			// the data integrity is maintained 
			joyInfoStructs[i].order(ByteOrder.nativeOrder());
			
			// set the size and the DEFAULT value for the flags field of the struct
			joyInfoStructs[i].putInt(FIELD_INDEX_dwSize, joyInfoStructs[i].capacity());
			joyInfoStructs[i].putInt(FIELD_INDEX_dwFlags, JOY_RETURNALL);
		}
	}	

 
 	// returns success of failure
	public static boolean poll(boolean pollBoth){
		errors = nativePoll(pollBoth);	
		return (errors == JOYERR_NOERROR)? true: false;
	}
	
	// joyAxis parameter can be JOY_AXISX, ...
	public static float getAxis(int joyId, int joyAxis){
		return normalizeAxisValue(joyInfoStructs[joyId].getInt(joyAxis));
	}

	// joyAxis parameter can be JOY_BUTTON1, ...
	public static boolean getButton(int joyId, int joyButton){
		return ((joyInfoStructs[joyId].getInt(FIELD_INDEX_dwButtons) & joyButton) != 0)? true: false;	
	}

	public static float normalizeAxisValue(int value){
		return ((float)value*AXIS_NORMALIZATION_FACTOR - 1);
	}

	public static void showInfo() {
		nativeShowInfo();
	}

	public static void dumpJoyInfoExStruct(int joyId){
		System.out.println("-------------- JOYINFOEX Struct --------------");
		System.out.println("dwSize:         " + joyInfoStructs[joyId].getInt(FIELD_INDEX_dwSize));
		System.out.println("dwFlags:        " + Integer.toBinaryString(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwFlags)));
		System.out.println("dwXpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwXpos)));
		System.out.println("dwYpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwYpos)));
		System.out.println("dwZpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwZpos)));
		System.out.println("dwRpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwRpos)));
		System.out.println("dwUpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwUpos)));
		System.out.println("dwVpos:         " + normalizeAxisValue(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwVpos)));
		System.out.println("dwButtons:      " + Integer.toBinaryString(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwButtons)));
		System.out.println("dwButtonNumber: " + joyInfoStructs[joyId].getInt(FIELD_INDEX_dwButtonNumber));
		System.out.println("dwPOV:          " + Integer.toBinaryString(joyInfoStructs[joyId].getInt(FIELD_INDEX_dwPOV)));
		System.out.println("dwReserved1:    " + joyInfoStructs[joyId].getInt(FIELD_INDEX_dwReserved1));
		System.out.println("dwReserved2:    " + joyInfoStructs[joyId].getInt(FIELD_INDEX_dwReserved2));
		System.out.println("----------------------------------------------");
	}
	
	
	
	public static void dumpErrors(){
				
		String errorString = "ERROR: ";
		
		if ((errors & JOYERR_UNPLUGGED) == JOYERR_UNPLUGGED)
			errorString += "JOYERR_UNPLUGGED ";
		if ((errors & MMSYSERR_NODRIVER) == MMSYSERR_NODRIVER)
			errorString += "MMSYSERR_NODRIVER ";
		if ((errors & MMSYSERR_BADDEVICEID) == MMSYSERR_BADDEVICEID)
			errorString += "MMSYSERR_BADDEVICEID ";
		if ((errors & MMSYSERR_INVALPARAM) == MMSYSERR_INVALPARAM)
			errorString += "MMSYSERR_INVALPARAM ";
		
		System.out.println(errorString);
	}
	
	
	
	/**
	 *	Native functions implemented in joystickWin32Ex.dll
	 */
	
	// returns a ByteBuffers mapped to an instance of the JOYINFOEX struct.
	private static native Object 	nativeWrapJoyInfoStruct(int joyId);
	
	// calls GetJoyPosEx (which is part of WINAPI). 
	// if pollBoth is true, it calls teh function for each joystic (0 and 1)
	private static native int	 	nativePoll(boolean pollBoth);
	
	// pops up a dialog box with information regarding the dll
	private static native void	 	nativeShowInfo();
}
