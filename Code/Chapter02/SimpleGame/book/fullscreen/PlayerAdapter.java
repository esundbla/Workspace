package book.fullscreen;

import java.awt.event.*;

class PlayerAdapter extends KeyAdapter
 {
	private Player player;
	
	public PlayerAdapter(Player p)
	{
		player = p;
	}
	
	public void keyPressed(KeyEvent e){
		
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_UP:
			player.setY(player.getY()-5);
			break;
			case KeyEvent.VK_DOWN:
			player.setY(player.getY()+5);
			break;
			case KeyEvent.VK_LEFT:
			player.setX(player.getX()-5);
			break;
			case KeyEvent.VK_RIGHT:
			player.setX(player.getX()+5);
			break;
			
			default:
			 break;	
		}
	
	}
}
