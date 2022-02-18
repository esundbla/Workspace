    public class JoystickWin32Ex {
        ... 
        public static final int 
            FIELD_INDEX_dwSize          = 0,
            FIELD_INDEX_dwFlags         = 4,
            FIELD_INDEX_dwXpos          = 8,
            FIELD_INDEX_dwYpos          = 12,
            ...
            JOY_BUTTON1         = 0x0001,
            JOY_AXISX           = FIELD_INDEX_dwXpos,
            JOY_RETURNX         = 0x00000001,
            ...

        private static ByteBuffer   joyInfoStructs[];
        
        private static void initialize(){
            joyInfoStructs = new ByteBuffer[2];
        
            for(int i=0; i<2; i++){
                joyInfoStructs[i] = 
                    (ByteBuffer)nativeWrapJoyInfoStruct(i);
            
                joyInfoStructs[i].order(ByteOrder.nativeOrder());
            
                joyInfoStructs[i].putInt(FIELD_INDEX_dwSize, 
                    joyInfoStructs[i].capacity());
                joyInfoStructs[i].putInt(FIELD_INDEX_dwFlags,
                    JOY_RETURNALL);
                }
        }   
    
        // joyAxis parameter can be JOY_AXISX, ...
        public static 
            float getAxis(int joyId, int joyAxis){
            return normalizeAxisValue(
                joyInfoStructs[joyId].getInt(joyAxis));
        }

        // joyAxis parameter can be JOY_BUTTON1, ...
        public static 
            boolean getButton(int joyId, int joyButton){
            return ((joyInfoStructs[joyId].getInt(
                FIELD_INDEX_dwButtons) & joyButton) != 0)?true:false;
        }

        public static void dumpJoyInfoExStruct(int joyId){
            System.out.println("----- JOYINFOEX Struct ------");
            System.out.println("dwSize:         " + 
                joyInfoStructs[joyId].
                getInt(FIELD_INDEX_dwSize));
            System.out.println("dwFlags:        " + 
                Integer.toBinaryString(joyInfoStructs[joyId].
                getInt(FIELD_INDEX_dwFlags)));
            System.out.println("dwXpos:         " + 
                normalizeAxisValue(joyInfoStructs[joyId].
                getInt(FIELD_INDEX_dwXpos)));
                ...
        }
        ...
  
        // returns a ByteBuffers mapped to a JOYINFOEX instance
        private static native 
            Object nativeWrapJoyInfoStruct(int joyId);

        private static native int 
            nativePoll(boolean pollBoth);
    }
