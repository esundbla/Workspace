import java.awt.*;
import java.awt.event.*;

// Needed for ByteBuffers
import java.nio.*;

// The main JOGL import
import net.java.games.jogl.*;

// Needed for special type of empty ByteBuffer
// used of Vertex Buffer Objects
import net.java.games.jogl.util.*;

public class Sample implements GLEventListener, KeyListener
{
	public static Animator animator;

	private float rotX = 0.0f;
	private float rotY = 0.0f;
	float scale = 1.0f;

	// used for switching between rendering types
	final static int rtVertex = 0;
	final static int rtDisplayList = 1;
	final static int rtVertexArray = 2;
	final static int rtVertexBufferObject = 3;
	final static int rtMax = rtVertexBufferObject;
	int renderType = rtVertex;

	// used for Display List rendering
	private int gear1;

	//  used for Vertex Array rendering
	int bufferV;
	int bufferI;
	int bufferC;
	static ByteBuffer zeroOffsetBuffer = null;

	boolean supportedVBO = true;
	FloatBuffer vertexArray = null;
	IntBuffer faceIndices = null;
	FloatBuffer colorArray = null;
	FloatBuffer normalArray = null;
	FloatBuffer[] textureCoordArray = null;

	/** Called by the drawable to initiate OpenGL rendering by the client.
	 * After all GLEventListeners have been notified of a display event, the 
	 * drawable will swap its buffers if necessary. 
	 * @param gLDrawable The GLDrawable object.
	 */
	public void display(GLDrawable gLDrawable)
	{
		GL gl = gLDrawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -6.0f);

		gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);

		rotX += 1.0f * scale;
		rotY += 0.5f * scale;
		
		// uncomment for performance tests
		//for(int i = 0;i<10; i++)
		{
			switch (renderType)
			{
				case rtVertex :
					drawCubeVertex(gl);
					break;
				case rtDisplayList :
					gl.glCallList(gear1);
					break;
				case rtVertexArray :
					drawCubeVertexArrays(gl);
					break;
				case rtVertexBufferObject :
					if (supportedVBO)
						drawCubeVertexBufferObjects(gl);
					break;
			}
		}

	}

	/** Called when the display mode has been changed.  <B>!! CURRENTLY UNIMPLEMENTED IN JOGL !!</B>
	* @param gLDrawable The GLDrawable object.
	* @param modeChanged Indicates if the video mode has changed.
	* @param deviceChanged Indicates if the video device has changed.
	*/
	public void displayChanged(GLDrawable gLDrawable, boolean modeChanged, boolean deviceChanged)
	{
	}
	
	/** Called by the drawable immediately after the OpenGL context is 
	* initialized for the first time. Can be used to perform one-time OpenGL 
	* initialization such as setup of lights and display lists.
	* @param gLDrawable The GLDrawable object.
	*/
	public void init(GLDrawable gLDrawable)
	{
		final GL gl = gLDrawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glClearDepth(1.0f); // Depth Buffer Setup
		gl.glEnable(GL.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		// Really Nice Perspective Calculations
		gLDrawable.addKeyListener(this);
		// Use Debug pipeline
		gLDrawable.setGL(new DebugGL(gLDrawable.getGL()));
	
		supportedVBO = gl.isExtensionAvailable("GL_ARB_vertex_buffer_object");
		if (supportedVBO)
			System.out.println("Vertex Buffer Objects Supported");
		else
		{
			System.out.println(
				"Vertex Buffer Objects NOT Supported - part of this example will not execute");
	
		}
		buildCubeDisplayList(gl);
		buildCubeVertexArrays(gl);
		Thread.currentThread().dumpStack();
	}

	/** Called by the drawable during the first repaint after the component has 
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
	public void reshape(GLDrawable gLDrawable, int x, int y, int width, int height)
	{

		final GL gl = gLDrawable.getGL();
		final GLU glu = gLDrawable.getGLU();

		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
		System.err.println();

		if (height <= 0) // avoid a divide by zero error!
			height = 1;
		final float h = (float)width / (float)height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void nextRenderMode()
	{
		renderType++;
		if (renderType > rtMax)
			renderType = 0;
	}

	public String printRenderMode()
	{
		switch (renderType)
		{
			case rtVertex :
				return "Vertex";
			case rtDisplayList :
				return "DisplayList";
			case rtVertexArray :
				return "VertexArray";
			case rtVertexBufferObject :
				return "VertexBufferObject";
		}
		return null;
	}

	/** Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 * @param e The KeyEvent.
	 */
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			animator.stop();
			System.exit(0);
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			nextRenderMode();
			System.out.println("Render type is : " + printRenderMode());
			if (renderType == rtVertexBufferObject && !supportedVBO)
				System.out.println("NOT SUPPORTED ON THIS SYSTEM");
		}
	}

	/** Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 * @param e The KeyEvent.
	 */
	public void keyReleased(KeyEvent e)
	{
	}

	/** Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 * @param e The KeyEvent.
	 */
	public void keyTyped(KeyEvent e)
	{
	}
	
	/** Program's main entry point
	 * @param args command line arguments.
	 */
	public static void main(String[] args)
	{
		Frame frame = new Frame("Spinning Cube Four JOGL Ways!");
		GLCanvas canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
		canvas.addGLEventListener(new Sample());

		System.err.println("CANVAS GL IS: " + canvas.getGL().getClass().getName());
		System.err.println("CANVAS GLU IS: " + canvas.getGLU().getClass().getName());

		frame.add(canvas);
		frame.setSize(500, 500); // hard coded width/height
		animator = new Animator(canvas);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				animator.stop();
				System.exit(0);
			}
		});
		frame.show();
		animator.start();
		canvas.requestFocus();
	}
	
	public void drawCubeVertexArrays(GL gl)
	{
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexArray);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);

		gl.glColorPointer(3, GL.GL_FLOAT, 0, colorArray);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);

		gl.glDrawElements(
			GL.GL_TRIANGLES,
			faceIndices.capacity(),
			GL.GL_UNSIGNED_INT,
			faceIndices);
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	}

	public void drawCubeVertexBufferObjects(GL gl)
	{
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferV);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, zeroOffsetBuffer);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);

		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferC);
		gl.glColorPointer(3, GL.GL_FLOAT, 0, zeroOffsetBuffer);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);

		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, bufferI);

		gl.glDrawElements(
			GL.GL_TRIANGLES,
			faceIndices.capacity(),
			GL.GL_UNSIGNED_INT,
			zeroOffsetBuffer);

		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	}
	
	/* The remaining methods construct geoemetry for the different rendering types
	 *
	 */	
	public void drawCubeVertex(GL gl)
	{
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
	
	public void buildCubeDisplayList(GL gl)
	{
		gear1 = gl.glGenLists(1);
		gl.glNewList(gear1, GL.GL_COMPILE);
		drawCubeVertex(gl);
		gl.glEndList();
	}
	
    public void buildCubeVertexArrays(GL gl)
    {
		float[] verts = new float[]
		{ 
			1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,

			1.0f, -1.0f, 1.0f,
			-1.0f,-1.0f, 1.0f,
			-1.0f,-1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,

			1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,
			-1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,

			1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,

			-1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, 1.0f,

			1.0f, 1.0f, -1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, -1.0f
		};
		int[] faceIndicesI = new int[]
		{
			0,1,2,
			0,2,3,
			4,5,6,
			4,6,7,
			8,9,10,
			8,10,11,
			12,13,14,
			12,14,15,
			16,17,18,
			16,18,19,
			20,21,22,
			20,22,23	
		};
		// the colors that match with each vertex
		float floor = 0.6f;
		float[] colors = new float[]
		{ 
			floor, 1.0f, floor,
			floor, 1.0f, floor,
			floor, 1.0f, floor,
			floor, 1.0f, floor,
			
			floor, 1.0f, 1.0f,
			floor, 1.0f, 1.0f,
			floor, 1.0f, 1.0f,
			floor, 1.0f, 1.0f,

			1.0f, floor, floor,
			1.0f, floor, floor,
			1.0f, floor, floor,
			1.0f, floor, floor,

			1.0f, 1.0f, floor,
			1.0f, 1.0f, floor,
			1.0f, 1.0f, floor,
			1.0f, 1.0f, floor,

			floor, floor, 1.0f,
			floor, floor, 1.0f,
			floor, floor, 1.0f,
			floor, floor, 1.0f,

			1.0f, floor, 1.0f,
			1.0f, floor, 1.0f,
			1.0f, floor, 1.0f,
			1.0f, floor, 1.0f,
		};
		
		vertexArray =
			ByteBuffer
				.allocateDirect(verts.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		vertexArray.put(verts);
		
		faceIndices =
			(ByteBuffer.allocateDirect(faceIndicesI.length * 4))
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		faceIndices.put(faceIndicesI);
		
		colorArray =
			ByteBuffer
				.allocateDirect(colors.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		colorArray.put(colors);
		
		normalArray =
			ByteBuffer
				.allocateDirect(verts.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		normalArray.put(verts);
		
		if (supportedVBO)
		{
			zeroOffsetBuffer = BufferUtils.bufferOffset(0);
			
			// bind vertex buffer objects
			int[] tmpID = new int[1];
			gl.glGenBuffersARB(1, tmpID);
			gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, tmpID[0]);
			gl.glBufferDataARB(
				GL.GL_ARRAY_BUFFER_ARB,
				vertexArray.capacity() * 4,
				vertexArray,
				GL.GL_STATIC_DRAW_ARB);
			bufferV = tmpID[0];
	
			//upload data 
			gl.glGenBuffersARB(1, tmpID);
			gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, tmpID[0]);
			gl.glBufferDataARB(
				GL.GL_ELEMENT_ARRAY_BUFFER_ARB,
				faceIndices.capacity() * 4,
				faceIndices,
				GL.GL_STATIC_DRAW_ARB);
			bufferI = tmpID[0];
			
			//upload data 
			gl.glGenBuffersARB(1, tmpID);
			gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, tmpID[0]);
			gl.glBufferDataARB(
				GL.GL_ARRAY_BUFFER_ARB,
				colorArray.capacity() * 4,
				colorArray,
				GL.GL_STATIC_DRAW_ARB);
			bufferC = tmpID[0];
		
			gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
			gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
		}
	}
}
