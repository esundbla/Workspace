
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import javax.imageio.*;
import net.java.games.jogl.util.*;


import net.java.games.jogl.*;

public class Sample implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {
    
    static Animator animator;

    float rotX = 0.0f;
    float rotY = 0.0f;
    
    float 	rotXVel = 1.0f, 
			rotYVel = 0.5f;

	float	colorBlue[] = {.38f, .72f, 1.0f, 0.5f};
	boolean gameStartEnter = false;
	
	GLDrawable	glDrawable;
	
    //----
    static Sample instance = null;

    static int textureIdColorRed= -1;
    static int textureIdColorGreen= -1;
    static int textureIdColorBlue= -1;
    static int textureIdColorYellow= -1;
    static int textureIdColorBlack= -1;
    static int textureIdFont= -1;
    static int textureIdEmpty= -1;
    static int textureIdMouseOver= -1;
    static int textureIdFocus= -1;


    static float matrixProjectionOld[] = new float[16];
    

	HContainer          containerQuit;
    HudContainer        containerHud;
    ConsoleContainer    containerConsole;
	MainMenuContainer	containerMainMenu;
	OptionsMenuContainer	containerOptionsMenu;
    //----

    Sample(){
        instance = this;
        Frame frame = new Frame("");
        GLCanvas canvas =
            GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
        canvas.addGLEventListener(this);

        //use debug GL
        canvas.setGL(new DebugGL(canvas.getGL()));
        
        frame.add(canvas);
        frame.setSize(640 + 9, 480 + 35); // hard coded width/height addition to compensate for boarder and title
        animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				quitGame();
            }
        });
        frame.show();
        animator.start();
        canvas.requestFocus();      
    }
    
    public void quitGame(){
    	System.out.println("====> quitGame()");
		animator.stop();
		System.exit(0);   
    }

    /** Called by the drawable to initiate OpenGL rendering by the client.
     * After all GLEventListeners have been notified of a display event, the 
     * drawable will swap its buffers if necessary. 
     * @param gLDrawable The GLDrawable object.
     */
    public void display(GLDrawable gLDrawable) {
	    GL gl = gLDrawable.getGL();
		
		// set the color to blue if the game has started
		if (gameStartEnter){
			gl.glClearColor(.38f, .72f, 1.0f, 0.5f);			
			gameStartEnter = false;
		}	    
	    
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
       
        renderCube(gl);
        HContainerManager.instance.paint(gl);
        //renderTest(gl);
    }

    public void renderQuad(GL gl){

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorRed);          

        gl.glBegin(GL.GL_QUADS);       
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2i(100, 200); // SW
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2i(200, 200); // SE
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2i(200, 100); // NE
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2i(100, 100); // NW
        gl.glEnd(); 

    }

    public void renderCube(GL gl){
        
        //NOTE: assumes gl.glDisable(GL.GL_TEXTURE_2D);
                
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);

        rotX += rotXVel;
        rotY += rotYVel;

        gl.glBegin(GL.GL_QUADS); // Draw Quads
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)

        gl.glColor3f(0.0f, 1.0f, 1.0f); // Set The Color To Yellow
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad (Bottom)
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad (Bottom)
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Bottom)

        gl.glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Front)
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Front)

        gl.glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Back)
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)

        gl.glColor3f(0.0f, 0.0f, 1.0f); // Set The Color To Blue
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Left)

        gl.glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Right)
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Right)
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Right)
        gl.glEnd();
        
    }



    public void renderTest(GL gl){

        //----- store current OGL state
        gl.glPushMatrix();
        gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS); 

        gl.glGetFloatv(GL.GL_PROJECTION_MATRIX, matrixProjectionOld);



        //----- setup OGL state for GUI rendering
        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_DEPTH_TEST);             
        gl.glEnable(GL.GL_TEXTURE_2D); 
        
        // set the frustum 
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0,640,480,0,-1,1);
        gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glLoadIdentity();


        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);  // 0.5 needed for GL_ONE_MINUS_SRC_ALPHA
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);


        //==== render 2D stuff
        // renderQuad(gl);
        
        //==== render Text
        String string = "Hello";
        renderText(gl, 10, 10, string, string.length(), textureIdFont);

        //==== render raster
        //renderRaster(gl);


        //---- restore OGL state
        gl.glPopMatrix();
        gl.glPopAttrib();

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadMatrixf(matrixProjectionOld);

        gl.glMatrixMode(GL.GL_MODELVIEW);

    }
    
    public void renderRaster(GL gl){
        byte[] bitmap = {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0xFF,
            (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0xFF,
            (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0xFF, 
            (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
        };
        gl.glRasterPos2i( 300, 300 );
        gl.glBitmap(4*8, 8, 0, 0, 100, 0, bitmap);  // draw the raster and then move 128 pixels in x
        gl.glBitmap(4*8, 8, 0, 0, 128, 0, bitmap);
    }

    public void renderText(GL gl, int x, int y, String string, int length, int fontTexture){
        gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_COLOR_BUFFER_BIT);

        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
        gl.glEnable(GL.GL_BLEND);

        gl.glBindTexture(GL.GL_TEXTURE_2D, fontTexture);

        int dim = 16;

        for(int i=0; i<string.length() && i<length; i++){

            float cxIndex = ((string.charAt(i)-32)%16)/16.0f;
            float cyIndex = ((string.charAt(i)-32)/16)/16.0f;

            gl.glBegin(GL.GL_QUADS);   

            gl.glTexCoord2f(cxIndex,1-cyIndex-0.0625f);     
            gl.glVertex3f(x, y+dim,  0.0f);             // SW

            gl.glTexCoord2f(cxIndex+0.0625f,1-cyIndex-0.0625f);
            gl.glVertex3f(x+dim, y+dim,  0.0f);         // SE

            gl.glTexCoord2f(cxIndex+0.0625f,1-cyIndex); 
            gl.glVertex3f(x+dim, y,  0.0f);             // NE

            gl.glTexCoord2f(cxIndex,1-cyIndex);  
            gl.glVertex3f(x, y,  0.0f);                 // NW

            gl.glEnd();

            x += dim;
        }

        gl.glPopAttrib();       
    }

    private BufferedImage readPNGImage(String resourceName)
    {
        try
        {
            URL url = new URL("file", "localhost", resourceName);
            if (url == null)
            {
                throw new RuntimeException("Error reading resource " + resourceName);
            }
            BufferedImage img = ImageIO.read(url);
            java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance(1, -1); 
            tx.translate(0, -img.getHeight(null)); 
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
            img = op.filter(img, null); 
            return img;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void makeRGBTexture(GL gl, GLU glu, BufferedImage img, int target, boolean mipmapped)
    {
        ByteBuffer dest = null;
        switch (img.getType())
        {
        case BufferedImage.TYPE_3BYTE_BGR:
        case BufferedImage.TYPE_CUSTOM:
            {
                byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
                dest = ByteBuffer.allocateDirect(data.length);
                dest.order(ByteOrder.nativeOrder());
                dest.put(data, 0, data.length);
                break;
            }
        case BufferedImage.TYPE_INT_RGB:
            {
                int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
                dest = ByteBuffer.allocateDirect(data.length * BufferUtils.SIZEOF_INT);
                dest.order(ByteOrder.nativeOrder());
                dest.asIntBuffer().put(data, 0, data.length);
                break;
            }
        default:
            throw new RuntimeException("Unsupported image type " + img.getType());
        }

        if (mipmapped)
        {
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, dest);
        }
        else
        {
            gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, dest);
        }
    }

    private int genTexture(GL gl)
    {
        final int[] tmp = new int[1];
        gl.glGenTextures(1, tmp);
        return tmp[0];
    } 
    

    /** Called when the display mode has been changed.  <B>!! CURRENTLY UNIMPLEMENTED IN JOGL !!</B>
    * @param gLDrawable The GLDrawable object.
    * @param modeChanged Indicates if the video mode has changed.
    * @param deviceChanged Indicates if the video device has changed.
    */
    public void displayChanged(
        GLDrawable gLDrawable,
        boolean modeChanged,
        boolean deviceChanged) {
    }

    /** Called by the drawable immediately after the OpenGL context is 
    * initialized for the first time. Can be used to perform one-time OpenGL 
    * initialization such as setup of lights and display lists.
    * @param gLDrawable The GLDrawable object.
    */
    public void init(GLDrawable gLDrawable) {
        GL gl = gLDrawable.getGL();
		this.glDrawable = gLDrawable;


        gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0, 0, 0, 0.5f); // Blue Background
//		gl.glClearColor(.38f, .72f, 1.0f, 0.5f); // Blue Background
        gl.glClearDepth(1.0f); // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);


        BufferedImage img;

        textureIdFont = genTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdFont);
        img = readPNGImage("data/font.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		
		//TODO: set clamp to not wrap	
//		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);


        textureIdColorBlue = genTexture(gl); 
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorBlue);
        img = readPNGImage("data/ColorBlue.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdColorRed = genTexture(gl); 
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorRed);
        img = readPNGImage("data/ColorRed.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdColorGreen = genTexture(gl); 
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorGreen);
        img = readPNGImage("data/ColorGreen.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdColorYellow = genTexture(gl); 
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorYellow);
        img = readPNGImage("data/ColorYellow.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdColorBlack = genTexture(gl); 
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdColorBlack);
        img = readPNGImage("data/ColorBlack.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdFocus = genTexture(gl);       
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdFocus);
        img = readPNGImage("data/focus.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        textureIdMouseOver = genTexture(gl);       
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdMouseOver);
        img = readPNGImage("data/mouseOver.png");
        makeRGBTexture(gl, gLDrawable.getGLU(), img, GL.GL_TEXTURE_2D, false);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);


		//---------------
		initGUI();
		
		
        //==============================
        gLDrawable.addMouseListener(this);
        gLDrawable.addMouseMotionListener(this);
        gLDrawable.addKeyListener(this);    
        
    }
	
	public void initGUI(){
        
		//---- init container manager
		new HContainerManager(); //singleton
		HContainerManager.instance.textureIdFocus = textureIdFocus;
		HContainerManager.instance.textureIdMouseOver = textureIdMouseOver;



		containerHud 			= new HudContainer();  
		containerConsole 		= new ConsoleContainer();
		containerMainMenu 		= new MainMenuContainer();
		containerOptionsMenu 	= new OptionsMenuContainer();


		//--------------- Quit Dialog

		containerQuit = new HContainer();
		containerQuit.xInitial 		= 640;
		containerQuit.xDestination 	= 170;
		containerQuit.xDelta 		= -10;
		
		containerQuit.yInitial 		= 160;
		containerQuit.yDestination 	= 160;
//		containerQuit.yDelta 		= -1;


		HComponent background = new HComponent();
		background.setBounds(0, 0, 300, 100);
		background.textureId = textureIdColorYellow;
		background.textureIdFont = textureIdFont;
		background.text = "Do you really want to Quit?";
		background.xText = 10;
		background.yText = 10;
		background.enabled = false;
		background.focusable = false;
		containerQuit.addComponent(background);

		HButton buttonYes = new HButton();
		buttonYes.setBounds(75, 40, 50, 20);
		buttonYes.textureId = textureIdColorGreen;
		buttonYes.textureIdPressed = textureIdColorBlack;
		containerQuit.addComponent(buttonYes);

		buttonYes.listener = new HListener(){
			public void actionPerformed(HComponent component, int action){
				quitGame();
			}
		};          

		HButton buttonNo = new HButton();
		buttonNo.setBounds(175, 40, 50, 20);
		buttonNo.textureId = textureIdColorRed;
		buttonNo.textureIdPressed = textureIdColorBlack;
		containerQuit.addComponent(buttonNo);

		buttonNo.listener = new HListener() {
			public void actionPerformed(HComponent component, int action){
				System.out.println("************** buttonNo  HListener, actionPerformed");
				HContainerManager.instance.popContainer(); // close the top container
			}
		};
		
		
		//===========================================
//		HContainerManager.instance.pushContainer(containerHud);
		HContainerManager.instance.pushContainer(containerMainMenu);
		
		
	}
	
    /** Called by the gLDrawable during the first repaint after the component has 
    * been resized. The client can update the viewport and view volume of the 
    * window appropriately, for example by a call to 
    * GL.glViewport(int, int, int, int); note that for convenience the component
    * has already called GL.glViewport(int, int, int, int)(x, y, width, height)
    * when this method is called, so the client may not have to do anything in
    * this method.
    * @param gLDrawable The GLDrawable object.
    * @param x The X Coordinate of the viewport rectangle.
    * @param y The Y coordinate of the viewport rectanble.
    * @param width The new width of the window.
    * @param height The new height of the window.
    */
    public void reshape(
        GLDrawable gLDrawable,
        int x,
        int y,
        int width,
        int height) {

        final GL gl = gLDrawable.getGL();
        final GLU glu = gLDrawable.getGLU();


        // container manager needs to know teh width and height so that it can convert mouse coords
        HContainerManager.instance.windowWidth = width;
        HContainerManager.instance.windowHeight = height;

            
        if (height <= 0) // avoid a divide by zero error!
            height = 1;
        final float h = (float) width / (float) height;
        
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    // Methods required for the implementation of KeyListener
    public void keyPressed(KeyEvent e){
        HContainerManager.instance.processInputEvent(e);        
    }

    public void keyReleased(KeyEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    public void keyTyped(KeyEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    public void mouseReleased(MouseEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    public void mouseClicked(MouseEvent e) {
        HContainerManager.instance.processInputEvent(e);        
    }

    // Methods required for the implementation of MouseMotionListener
    public void mouseDragged(MouseEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    public void mouseMoved(MouseEvent e) {
        HContainerManager.instance.processInputEvent(e);
    }

    // Methods required for the implementation of MouseListener
    public void mouseEntered(MouseEvent e) {
        // not needed by the container manager
    }

    // Methods required for the implementation of MouseListener
    public void mouseExited(MouseEvent e) {
        // not needed by the container manager
    }


    public static void main(String[] args) {    
        new Sample();
    }
}