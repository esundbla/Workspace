
import java.awt.event.*;



class MainMenuContainer extends HContainer implements HListener{

	HComponent componentBackground;
	HButton buttonQuit;
	HButton buttonOptions;
	HButton buttonStart;	
	
	boolean gameStarted;

	public MainMenuContainer(){

		yInitial = -480;
		yDelta = +10;		

		componentBackground = new HComponent();
		componentBackground.setBounds(100, 100, 440, 280);
		componentBackground.textureId = Sample.textureIdColorYellow;
		componentBackground.textureIdFont = Sample.textureIdFont;
		componentBackground.text = "Main Menu";
		componentBackground.xText = 20;
		componentBackground.yText = 10;
		componentBackground.enabled = false;
		this.addComponent(componentBackground);

		buttonStart = new HButton();
		buttonStart.setBounds(220, 150, 200, 50);
		buttonStart.textureId = Sample.textureIdColorGreen;
		buttonStart.textureIdPressed = Sample.textureIdColorBlack;
		this.addComponent(buttonStart);		
		buttonStart.listener = this;

		buttonOptions = new HButton();
		buttonOptions.setBounds(220, 220, 200, 50);
		buttonOptions.textureId = Sample.textureIdColorBlue;
		buttonOptions.textureIdPressed = Sample.textureIdColorBlack;
		this.addComponent(buttonOptions);		
		buttonOptions.listener = this;
		
		buttonQuit = new HButton();
		buttonQuit.setBounds(220, 290, 200, 50);
		buttonQuit.textureId = Sample.textureIdColorRed;
		buttonQuit.textureIdPressed = Sample.textureIdColorBlack;
		this.addComponent(buttonQuit);		
		buttonQuit.listener = this;
		
	}

	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:

					// catch and ignore Esc so that is not handled by the super class' processInputEvent.
					// if teh game has been started, just pop the main menu
					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ESCAPE){
						if (gameStarted){
							HContainerManager.instance.popContainer();
						}
						return;
					}

					break;	
			}
		}		

		super.processInputEvent(event);
	}



	public void actionPerformed(HComponent component, int action){
		
		if(component == buttonQuit){
			if(gameStarted){
				HContainerManager.instance.pushContainer(Sample.instance.containerQuit);
			}else{
				Sample.instance.quitGame();
			}
			return;
		}
		
		if(component == buttonOptions){
			HContainerManager.instance.popContainer();
			HContainerManager.instance.pushContainer(Sample.instance.containerOptionsMenu);
			return;
		}
		
		if(component == buttonStart){
			HContainerManager.instance.popContainer();
			HContainerManager.instance.pushContainer(Sample.instance.containerHud);
			gameStarted = true;
			return;
		}		
		
		System.out.println("************** mainMenu, actionPerformed. action: " + action );		
	}		
	
	public void onHide(){
		if(gameStarted){
			Sample.instance.gameStartEnter = true;
		}
	}	

}