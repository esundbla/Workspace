//MultiSounds.java
//
//Created by: Dustin Clingman
//
//This program shows how to play multiple sounds in JOAL in sequence or all at once. 

 import net.java.games.joal.*;
 import net.java.games.joal.util.*;
 import java.io.*;
 import java.nio.ByteBuffer;


 public class MultiSounds
 {
    final int MAX_SIZE = 3;
    
    //get access to the AL object for further use.
    AL al = ALFactory.getAL();
    
    //Create more buffers to hold the extra sounds.
    int[] buffer = new int[MAX_SIZE];
    
    //This source holds the points that emit the sound
    int[] source = new int[MAX_SIZE];
    
    //the actual position and velocity of the source sound. This could be set for each source differently.
    float[][] sourcePosition = new float[MAX_SIZE][3];
    
    float[][] sourceVelocity = new float[MAX_SIZE][3];
    
    //actual position of the listener position and velocity.
    float[] listenerPosition =  {0.0f,0.0f,0.0f};
    
    float[] listenerVelocity =  {0.0f,0.0f,0.0f};
    
    
    //Orientation of the listener.. first three are the forward vector, last three are the world-up vector
    float[] listenerOrientation = {0.0f,0.0f,-1.0f,0.0f,0.0f,0.0f};
    
    
    //This function will create a buffer  and bind it to a source for later playback usage. 
    int loadWAV()
    {
        
    //variables to load into
        
    int[] format = new int[1];
    ByteBuffer[] data = new ByteBuffer[1];
    int[] size   = new int[1];
    int[] freq   = new int[1];
    int[] loop   = new int[1];
        
    al.alGenBuffers(MAX_SIZE,buffer);
    
    if(al.alGetError() != AL.AL_NO_ERROR)
       return AL.AL_FALSE;
           
    //load wav date of file specified
    ALut.alutLoadWAVFile("outstanding.wav", format, data, size, freq, loop);
    
    //lock in the buffer data
    al.alBufferData(buffer[0],format[0],data[0],size[0],freq[0]);
    
    //let the wav file go since the sound data is already loaded into memory.
    ALut.alutUnloadWAV(format[0],data[0],size[0],freq[0]); 
    
    
    ALut.alutLoadWAVFile("vroom.wav", format, data, size, freq, loop);
    
    //lock in the buffer data
    al.alBufferData(buffer[1],format[0],data[0],size[0],freq[0]);
    
    //let the wav file go since the sound data is already loaded into memory.
    ALut.alutUnloadWAV(format[0],data[0],size[0],freq[0]); 
    
     //Generate a source
    al.alGenSources(MAX_SIZE,source);
       
   //use the existing properties to setup the bufferNumber information
   al.alSourcei(source[0], AL.AL_BUFFER, buffer[0] );
   al.alSourcef(source[0], AL.AL_PITCH,  1.0f);
   al.alSourcef(source[0], AL.AL_GAIN,   1.0f);
   al.alSourcefv(source[0], AL.AL_POSITION, sourcePosition[0]);
   al.alSourcefv(source[0], AL.AL_VELOCITY, sourceVelocity[0]);
   al.alSourcei(source[0], AL.AL_LOOPING, AL.AL_FALSE); //Not looping. 
   
   //use the existing properties to setup the bufferNumber information
   al.alSourcei(source[1], AL.AL_BUFFER, buffer[1] );
   al.alSourcef(source[1], AL.AL_PITCH,  1.5f);
   al.alSourcef(source[1], AL.AL_GAIN,   1.0f);
   al.alSourcefv(source[1], AL.AL_POSITION, sourcePosition[1]);
   al.alSourcefv(source[1], AL.AL_VELOCITY, sourceVelocity[1]);
   al.alSourcei(source[1], AL.AL_LOOPING, AL.AL_FALSE); //Not looping. 
   
   
   //use the existing properties to setup the bufferNumber information
   al.alSourcei(source[2], AL.AL_BUFFER, buffer[0] );
   al.alSourcef(source[2], AL.AL_PITCH,  1.75f);
   al.alSourcef(source[2], AL.AL_GAIN,   1.0f);
   al.alSourcefv(source[2], AL.AL_POSITION, sourcePosition[2]);
   al.alSourcefv(source[2], AL.AL_VELOCITY, sourceVelocity[2]);
   al.alSourcei(source[2], AL.AL_LOOPING, AL.AL_FALSE); //Not looping. 
    
    if(al.alGetError() == AL.AL_NO_ERROR)
        return AL.AL_TRUE;
        
        return AL.AL_FALSE;
    }
    
      
   //Sets up the various Listener properties.  
    void setListenerPosition()
    {
     //Follow up with setting the Listener's attributes as well
     al.alListenerfv(AL.AL_POSITION, listenerPosition);
     al.alListenerfv(AL.AL_VELOCITY, listenerVelocity);
     al.alListenerfv(AL.AL_ORIENTATION, listenerOrientation);
    }
    
    
    //This will let us destroy the buffers and source then exit cleanly    
     void cleanUpAL()
     {
       al.alDeleteBuffers(MAX_SIZE, buffer);
       al.alDeleteSources(MAX_SIZE, source);
       ALut.alutExit();
            
     }
     
  
 public static void main(String[] args)      
 {
    MultiSounds player = new MultiSounds();
    
    System.out.println("Welcome to Example 2: The MultiSounds Player \n"+ "This example showshow to setup and"+
                        " play more than one sound at a time in JOAL \n");

    ALut.alutInit();
    
    player.al.alGetError();
    
       float j = 1;
       
       if(player.loadWAV() == AL.AL_FALSE)
          System.exit(1);
          
    
     player.setListenerPosition();
     
     //characters are used out of convenience, nothing more.
     char[] choice = new char[1];
        
     
        while (choice[0] != 'q') 
        {
           System.out.println(" Select one of the buffers to play with the number keys 1-3");
           System.out.println("Press q to quit"); 
               
             try {
                BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
                buf.read(choice);
                         
                switch (choice[0]) {
                    case '1' :
                        //begin playing sample
                        player.al.alSourcePlay(player.source[0]);
                        break;
                    case '2' :
                        //begin playing sample 2
                        player.al.alSourcePlay(player.source[1]);
                        break;
                    case '3' :
                        //begin playing sample 3
                        player.al.alSourcePlay(player.source[2]);
                        break;
                    case 'q' :
                        player.cleanUpAL();
                        break;
                }
               } 
               catch (IOException e) 
               {
                 System.exit(1);
               }
       }//end while
    
 }//end main
 
}//end MultiSounds.java