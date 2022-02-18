
import java.awt.event.*;



class HudContainer extends HContainer implements HListener{

	HComponent image;
	HButton button;
	HButton button1;
	HSlider slider;	

	public HudContainer(){

		image = new HComponent();
		image.setBounds(500, 20, 100, 20);
		image.textureId = Sample.textureIdColorBlue;
		image.textureIdFont = Sample.textureIdFont;
		image.text = "Hello";
		image.xText = 10;
		image.yText = 2;
		image.enabled = false;
		this.addComponent(image);

		button = new HButton();
		button.setBounds(20, 20, 50, 50);
		button.textureId = Sample.textureIdColorRed;
		button.textureIdPressed = Sample.textureIdColorBlack;
		button.enabled = true;
		this.addComponent(button);		
		button.listener = this;

		button1 = new HButton();
		button1.setBounds(20, 90, 50, 50);
		button1.textureId = Sample.textureIdColorRed;
		button1.textureIdPressed = Sample.textureIdColorBlack;
		button1.toggleButton = true;
		button1.enabled = true;
		button1.pressed = true;
		this.addComponent(button1);		

	}
	
	public void addComponent(HComponent component){
		component.focusable = false;
		super.addComponent(component);
	}

	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:

//					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_CONTROL){
//						System.out.println("================= HudContainer ignoring VK_CONTROL ");
//						return;	
//					}

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ESCAPE){
//						HContainerManager.instance.popContainer();
						HContainerManager.instance.pushContainer(Sample.instance.containerMainMenu);
						return;
					}

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_C){
						HContainerManager.instance.pushContainer(Sample.instance.containerConsole);
						return;	
					}
	
					break;	
			}
		}		

		super.processInputEvent(event);
	}



	public void actionPerformed(HComponent component, int action){
		System.out.println("************** containerHud, actionPerformed. action: " + action );
	}			

}