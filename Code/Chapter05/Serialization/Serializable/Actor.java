
import java.io.*;

class Actor implements Serializable{
    int     id;
    String  name;
    Point   position;
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
