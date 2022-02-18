
import java.io.*;

class Actor implements Serializable{
    int     id;
    String  name;
    Point   position;

    // alternative approach can be used instead of having to explicitly 
    // use transient to skip a member. 
    private static final ObjectStreamField[] serialPersistentFields = {
        new ObjectStreamField("id", int.class),
        new ObjectStreamField("name", String.class),
        new ObjectStreamField("position", Point.class)
    };  
}

class Point implements Serializable{
    float x;
    float y;
    float z;
    
    Point(float x,float y,float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
