
    import java.io.*;
    
    class Actor implements Externalizable{
        int     id;
        String  name;
        Point   position;
        
        public Actor(){} // must be public
        
        public void writeExternal(ObjectOutput out) throws IOException{
            out.writeInt(id);
            out.writeUTF(name);
            out.writeObject(position);
        }
    
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
            id = in.readInt();
            name = in.readUTF();
            position = (Point)in.readObject();
        }
        
    }
    
    class Point implements Externalizable{
        float x;
        float y;
        float z;
        
        public Point(){} // must be public
            
        
        Point(float x,float y,float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public void writeExternal(ObjectOutput out) throws IOException{
            out.writeFloat(x);
            out.writeFloat(y);
            out.writeFloat(z);
        }
    
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
            x = in.readFloat();
            y = in.readFloat();
            z = in.readFloat();
        }
    }
