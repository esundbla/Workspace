
import java.awt.event.*;
//import net.java.games.jogl.*;



class OptionsMenuContainer extends HContainer implements HListener{

	
	HComponent 	componentBackground;
	HComponent 	componentLabel1;
	HComponent 	componentLabel2;

	HButton 	buttonSound;
	HButton 	buttonDisplay;	
	HButton 	buttonBack;	
	
	HSlider 	soundSliderVolume;
	HButton 	displayUseTextures;
	HButton 	displayShowSpacialEffects;
	
	final static int	STATE_EMPTY_MENU 	= 0,
						STATE_DISPLAY_MENU 	= 1,
						STATE_SOUND_MENU 	= 2;
						
	int state = STATE_DISPLAY_MENU;					

	public OptionsMenuContainer(){

		yInitial = -480;
		yDelta = +10;		

		componentBackground = new HComponent();
		componentBackground.setBounds(100, 120, 440, 280);
		componentBackground.textureId = Sample.textureIdColorYellow;
		componentBackground.textureIdFont = Sample.textureIdFont;
		componentBackground.text = "Options";
		componentBackground.xText = 20;
		componentBackground.yText = 10;
		componentBackground.enabled = false;
		this.addComponent(componentBackground);




		buttonDisplay = new HButton();
		buttonDisplay.setBounds(140, 160, 100, 25);
		buttonDisplay.textureId = Sample.textureIdColorBlue;
		buttonDisplay.textureIdPressed = Sample.textureIdColorBlack;
		buttonDisplay.toggleButton = true;
		this.addComponent(buttonDisplay);		
		buttonDisplay.listener = this;
		
		buttonSound = new HButton();
		buttonSound.setBounds(140, 195, 100, 25);
		buttonSound.textureId = Sample.textureIdColorBlue;
		buttonSound.textureIdPressed = Sample.textureIdColorBlack;
		buttonSound.toggleButton = true;
		this.addComponent(buttonSound);		
		buttonSound.listener = this;
		
		buttonBack = new HButton();
		buttonBack.setBounds(140, 230, 100, 25);
		buttonBack.textureId = Sample.textureIdColorRed;
		buttonBack.textureIdPressed = Sample.textureIdColorBlack;
		this.addComponent(buttonBack);		
		buttonBack.listener = this;
		

		
		
		componentLabel1 = new HComponent();
		componentLabel1.setBounds(300, 160, 7*16, 20);
		componentLabel1.textureId = Sample.textureIdColorBlue;
		componentLabel1.textureIdFont = Sample.textureIdFont;
		componentLabel1.text = "Label1:";
		componentLabel1.xText = 2;
		componentLabel1.yText = 2;
		componentLabel1.enabled = false;
		this.addComponent(componentLabel1);

		displayUseTextures = new HButton();
		displayUseTextures.setBounds(440, 160, 20, 20);
		displayUseTextures.textureId = Sample.textureIdColorGreen;
		displayUseTextures.textureIdPressed = Sample.textureIdColorBlack;
		this.addComponent(displayUseTextures);		
		displayUseTextures.listener = this;
		

		componentLabel2 = new HComponent();
		componentLabel2.setBounds(300, 210, 7*16, 20);
		componentLabel2.textureId = Sample.textureIdColorBlue;
		componentLabel2.textureIdFont = Sample.textureIdFont;
		componentLabel2.text = "Label2:";
		componentLabel2.xText = 2;
		componentLabel2.yText = 2;
		componentLabel2.enabled = false;
		this.addComponent(componentLabel2);

		displayShowSpacialEffects = new HButton();
		displayShowSpacialEffects.setBounds(440, 210, 20, 20);
		displayShowSpacialEffects.textureId = Sample.textureIdColorGreen;
		displayShowSpacialEffects.textureIdPressed = Sample.textureIdColorBlack;
		displayShowSpacialEffects.toggleButton = true;
		this.addComponent(displayShowSpacialEffects);		
		displayShowSpacialEffects.listener = this;
				
		

		
		soundSliderVolume = new HSlider();
		soundSliderVolume.setBounds(300, 160, 200, 40);
		soundSliderVolume.textureId = Sample.textureIdColorRed;
		soundSliderVolume.textureIdIndicator = Sample.textureIdColorBlue;
		this.addComponent(soundSliderVolume);

		soundSliderVolume.listener = this; 
	}
	
	public void onShow(){
		setState(STATE_DISPLAY_MENU);		
	}
	

	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ESCAPE){
						HContainerManager.instance.popContainer();
						HContainerManager.instance.pushContainer(Sample.instance.containerMainMenu);
						return;
					}
			}
		}		

		super.processInputEvent(event);
	}



	public void actionPerformed(HComponent component, int action){
		
		if(component == buttonBack){
			setState(STATE_EMPTY_MENU);
			HContainerManager.instance.popContainer();
			HContainerManager.instance.pushContainer(Sample.instance.containerMainMenu);
			return;
		}
		
		
		if(component == buttonSound){
			setState(STATE_SOUND_MENU);
			return;
		}
		
		if(component == buttonDisplay){
			setState(STATE_DISPLAY_MENU);
			return;
		}
		
		if(component == soundSliderVolume){
			Sample.instance.rotXVel = soundSliderVolume.value * 2;
			return;
		}
		
		if(component == displayShowSpacialEffects){
			System.out.println("************** component == displayShowSpacialEffects, displayShowSpacialEffects.pressed: " + displayShowSpacialEffects.pressed );	
		
			if (action == HButton.ACTION_CHECKED){
				Sample.instance.rotXVel /= 4.0f;
				Sample.instance.rotYVel /= 4.0f;
			}else if (action == HButton.ACTION_UNCHECKED){
				Sample.instance.rotXVel *= 4.0f;
				Sample.instance.rotYVel *= 4.0f;

			}
			
			return;
		}
		
		if(component == displayUseTextures){
			Sample.instance.rotX = 0;
			Sample.instance.rotY = 0;
		}
		
		
		System.out.println("************** optionsMenu, actionPerformed. action: " + action );	
	}		
	
	public void setState(int state){
		
		switch(state){
			
			case STATE_EMPTY_MENU:
			showDisplayOptions(false);
			showSoundOptions(false);
			buttonDisplay.pressed = false;
			buttonSound.pressed = false;
			break;

			case STATE_DISPLAY_MENU:
			showDisplayOptions(true);
			showSoundOptions(false);
			buttonDisplay.pressed = true;
			buttonSound.pressed = false;
			break;

			case STATE_SOUND_MENU:
			showDisplayOptions(false);
			showSoundOptions(true);
			buttonDisplay.pressed = false;
			buttonSound.pressed = true;
			break;
		}
	
	}
	
	
	public void showSoundOptions(boolean show){
		soundSliderVolume.visible = show;
	} 	

	public void showDisplayOptions(boolean show){
		displayUseTextures.visible = show;		
		displayShowSpacialEffects.visible = show;
		componentLabel1.visible = show;		
		componentLabel2.visible = show;		
	} 	

}