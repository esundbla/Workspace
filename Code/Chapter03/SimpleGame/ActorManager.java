package book.fullscreen;

import java.util.*;


public class ActorManager
{
	
  LinkedList actorList;
  
  ActorManager()
  {
	//make a linked list to hold all the actors.
	actorList = new LinkedList();
  }
  
  public void createEntity(String s, int number)
  {
  	for (int i = 0; i<number; i++) 
  	{
	 actorList.add(entityFactory(s));
	}	  
  }
  
  Object entityFactory(String name) 
  {

 	Class temp;

	Object targetClass =null;
	
	//small workaround for working with packages.
	String tempString = new String("book.fullscreen."+name);
	
	try 
	{
	  //Find the class in question
	  temp = Class.forName(tempString); 
	  

	  // Make an instance of the desired class
	  targetClass = temp.newInstance(); 
  	}
  	catch (Exception e) 
  	{
		e.printStackTrace();
	System.out.println("Cannot create insance of" +tempString);
    }
         
   return targetClass;
  }
	
  public void clearEntities()
  {
  	for (int i = 0; i<actorList.size(); i++) 
  	{
  		actorList.remove(i);
  	
	  }
	System.out.println("The entity list has been cleared");
  }
  
  public void updateAll()
  {
  	Updater u;
  		
	for (int i = 0; i<actorList.size(); i++) 
	{
		u = (Updater)actorList.get(i);
		u.update();
	}
  	
  }
  
  public void drawAll(){}
  	
  
}//end ActorManager.java

