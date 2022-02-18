
    class Script1 extends ScriptBase{
        void run(){ 
            
            print("Script1.run()");
    
            Unit peons[] = new Unit[10];
        
            for(int i=0; i<peons.length; i++){
                peons[i] = createUnit(UNIT_TYPE_PEON);
            }
            
            for(int i=0; i<peons.length; i++){
                 destroyUnit(peons[i]);
            }
        }
    }