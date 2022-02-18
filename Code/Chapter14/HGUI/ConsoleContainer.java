
import java.awt.event.*;



class ConsoleContainer extends HContainer implements HListener{

	HTextField textField;
	HTextList textList;
	HButton button;


	ConsoleContainer(){

		yInitial = 100;
		yDelta = -5;


		textField = new HTextField();
		textField.setBounds(0, 460, 640, 20);
		textField.textureId = Sample.textureIdColorBlue;
		textField.textureIdFont = Sample.textureIdFont;
		textField.listener = this;
		this.addComponent(textField);
		setFocus(textField);


		textList = new HTextList();
		textList.setBounds(0, 380, 640, 80);
		textList.textureId = Sample.textureIdColorYellow;
		textList.textureIdFont = Sample.textureIdFont;
		textList.enabled = false;
		this.addComponent(textList);

	}
	
	public void onShow(){
		textField.text = "Type text here. ";	
	}

	public void processInputEvent(InputEvent event){

		if (event instanceof KeyEvent){

			switch(event.getID()){
				case KeyEvent.KEY_RELEASED:

					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_CONTROL){
						System.out.println("================= ConsoleContainer ignoring VK_CONTROL ");
						return;	
					}
					break;	
			}
		}		

		super.processInputEvent(event);
	}


	public void actionPerformed(HComponent component, int action){
		System.out.println("************** ConsoleContainer, actionPerformed. action: " + action );
		textList.texts.add(textField.text);
		String string  = textField.text;
		textField.text = "";
		
		System.out.println("string: [" + string + "]");

		if(string.equals("quit")){
			Sample.instance.quitGame();
		}
	}			

}