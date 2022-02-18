/*
 * Copyright (c) 2004, Immediate Mode Interactve, LLC. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of Immediate Mode Interactve, LLC. or the names of
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. IMMEDIATE
 * MODE INTERACTIVE, LLC. ("IMI") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL IMI OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * IMI HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.imi.jist3d.render;

import java.nio.ByteBuffer;
import java.util.*;

import vecmath.Util;

import net.java.games.jogl.*;
import net.java.games.jogl.util.BufferUtils;
import net.java.games.jogl.util.GLUT;

public class BasicRenderer implements GLEventListener
{
	public static boolean supportedVBO = true;
	Scene scene = new Scene();
	VertexArraySet lastVertexArraySet = null;
	Appearance lastAppearance = new Appearance();
	int textureShareCount = 0;
	public float[] lightOffset = new float[3];
	double[] offsetMat = new double[16];
	double[] tmpMat = new double[16];

	float[] currentMat = null;

	public List bindBin = Collections.synchronizedList(new ArrayList());

	GLCanvas canvas = null;

	public static RenderBinNode drawListOpaque = new RenderBinNode();
	public static ArrayList drawListTransparent = new ArrayList();

//	public static AppearanceComparator aComparator = new AppearanceComparator();
	public static ZComparator zComparator = new ZComparator();
	public int drawMode = 0;
	boolean globalWireFrameState = false;
	boolean globalTextureState = true;
	public boolean autoLightLookAt = true;

	private GL gl;
	private GLU glu;
	private GLDrawable gldrawable;
	GLUT glut = new GLUT();

	boolean noException = false;
	boolean drawDynamic = true;
	boolean drawStatic = true;
	boolean pause = false;

	// statistic fields
	public static boolean once = true;
	public static int stateChanges = 0;
	boolean dumpStats = false;
	public int stateSwitchCount = 0;
	public int stateShareCount = 0;
	public int alphaStateSwitchCount = 0;
	public int alphaStateShareCount = 0;
	public int shapeRenderStaticCount = 0;
	public int shapeRenderDynamicCount = 0;

	int debugLevel = 0;

	// needed for Vertex Buffer Object offsets
	static ByteBuffer zeroOffsetBuffer = null;
	// can not be set here BufferUtils.bufferOffset(0);

	int DL_SPHERE = 1;

	float[] grayMaterial = { 0.7f, 0.7f, 0.7f, 1.0f };
	float[] whiteMaterial = { 1.0f, 1.0f, 1.0f, 1.0f };
	float LIGHT_DIMMING = 0.4f;
	float lightDimColor[] = { LIGHT_DIMMING, LIGHT_DIMMING, LIGHT_DIMMING, 1.0f };

	float zero[] = { 0.0f, 0.0f, 0.0f, 1.0f };

	int winWidth;
	int winHeight;

	GLUquadric q;
	float winAspectRatio;

	/************************************************************
	* shadow mapping fields
	* for shadow mapping render mode
	* implemented using OpenGL hardware shadow map acceleration
	* based on public nVidia demos, examples, and papers
	* as well as others
	***********************************************************/
//	int requestedDepthMapSize = 256;
	int depthMapSize = 256;

//	int requestedDepthMapRectWidth = 350;
//	int requestedDepthMapRectHeight = 300;
//	int depthMapRectWidth = 350;
//	int depthMapRectHeight = 300;

//	int depthMapPrecision = GL.GL_UNSIGNED_BYTE;
//	int depthMapFormat = GL.GL_LUMINANCE;
//	int depthMapInternalFormat = GL.GL_INTENSITY8;

	//int hwDepthMapPrecision = GL.GL_UNSIGNED_BYTE;
	int hwDepthMapPrecision = GL.GL_UNSIGNED_SHORT;
	int hwDepthMapInternalFormat = GL.GL_DEPTH_COMPONENT;
//	int hwDepthMapInternalFormat = GL.GL_DEPTH_COMPONENT16_ARB;
	//int hwDepthMapInternalFormat = GL.GL_DEPTH_COMPONENT24_SGIX;
	//int hwDepthMapPrecision = GL.GL_UNSIGNED_INT;

	int hwDepthMapFiltering = GL.GL_LINEAR;
//	int hwDepthMapFiltering = GL.GL_NEAREST;

	static int depthBufferSize = 0;
	//static GLubyte *depthBuffer = NULL;

	static int lastHiResHwWidth = 0;
	static int lastHiResHwHeight = 0;
	static int lastHiResHwPrecision = 0;
	static int lastHiResHwFiltering = 0;

	// used in texgen routine
	double[] m3 = new double[16];

	int depthBias8 = 2;
	int depthBias16 = 6;
	int depthBias24 = 8;
	int depthScale8, depthScale16, depthScale24;
	float slopeScale = 1.1f;

	boolean rangeMap = true;

	float textureLodBias = 0.0f;
	int TO_MAP_8BIT = 1; //texture ID
//	int TO_MAP_16BIT = 2; //texture ID
	int TO_HW_DEPTH_MAP = 2;
//	int TO_HW_DEPTH_MAP_RECT = 4;
//	int TO_LOWRES_DEPTH_MAP = 5;

	public BasicRenderer()
	{
		canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());
		
		// Use debug pipeline
		//canvas.setGL(new DebugGL(canvas.getGL()));
		canvas.setGL(canvas.getGL());
		System.err.println("CANVAS GL IS: " + canvas.getGL().getClass().getName());
		System.err.println("CANVAS GLU IS: " + canvas.getGLU().getClass().getName());

		canvas.addGLEventListener(this);
		zeroOffsetBuffer = BufferUtils.bufferOffset(0);
	}

	public GLCanvas getCanvas()
	{
		return canvas;
	}

	public void init(GLDrawable drawable)
	{
		System.out.println("***************************************");
		System.out.println("Initializing Jist3D Renderer...");
		System.out.println("***************************************");
		gl = drawable.getGL();
		glu = drawable.getGLU();
		this.gldrawable = drawable;
		System.err.println("INIT GL IS: " + gl.getClass().getName());

		initShadow(drawable);

		supportedVBO = gl.isExtensionAvailable("GL_ARB_vertex_buffer_object");
		if (supportedVBO)
			System.out.println("Vertex Buffer Objects Supported");
		else
		{
			System.out.println(
				"Vertex Buffer Objects NOT Supported - part of this example will not execute");
		}
		supportedVBO = true;

		gl.glLightModelfv(gl.GL_LIGHT_MODEL_AMBIENT, scene.light.ambientColor);

		//gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, zero);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, scene.light.specularColor);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, scene.light.diffuseColor);

		gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 30.0f);

		gl.glEnable(gl.GL_CULL_FACE);
		//gl.glLightModeli(gl.GL_LIGHT_MODEL_LOCAL_VIEWER, 1);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_COLOR_CONTROL, GL.GL_SEPARATE_SPECULAR_COLOR);

		gl.glEnable(GL.GL_RESCALE_NORMAL_EXT);
		//gl.glEnable(gl.GL_NORMALIZE);

		// make light sphere
		q = glu.gluNewQuadric();
		gl.glNewList(DL_SPHERE, gl.GL_COMPILE);
		glu.gluSphere(q, 0.55, 16, 16);
		gl.glEndList();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthMask(true);

		scene.init(gl, glu);
	}

	public void initShadow(GLDrawable drawable)
	{
		float globalAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		byte[] texmap = new byte[256 * 256 * 2];
		int i, j;
		int depthBits;

		int[] tmp = new int[1];

		gl.glGenTextures(1, tmp);
		TO_MAP_8BIT = tmp[0];
		//gl.glBindTexture(GL.GL_TEXTURE_1D, TO_MAP_8BIT);

//		gl.glGenTextures(1, tmp);
//		TO_MAP_16BIT = tmp[0];
		//gl.glBindTexture(GL.GL_TEXTURE_2D, TO_MAP_16BIT);

		gl.glClearStencil(0);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0);

		gl.glPixelStorei(gl.GL_UNPACK_ALIGNMENT, 1);
		gl.glPixelStorei(gl.GL_PACK_ALIGNMENT, 1);

		gl.glTexGeni(gl.GL_S, gl.GL_TEXTURE_GEN_MODE, gl.GL_EYE_LINEAR);
		gl.glTexGeni(gl.GL_T, gl.GL_TEXTURE_GEN_MODE, gl.GL_EYE_LINEAR);
		gl.glTexGeni(gl.GL_R, gl.GL_TEXTURE_GEN_MODE, gl.GL_EYE_LINEAR);
		gl.glTexGeni(gl.GL_Q, gl.GL_TEXTURE_GEN_MODE, gl.GL_EYE_LINEAR);

		gl.glLightModelfv(gl.GL_LIGHT_MODEL_AMBIENT, globalAmbient);

		/* Make 8-bit identity texture that maps (s)=(z) to [0,255]/255. */

		for (i = 0; i < 256; i++)
		{
			texmap[i] = (byte)i;
		}
		gl.glBindTexture(gl.GL_TEXTURE_1D, TO_MAP_8BIT);
		gl.glTexImage1D(
			gl.GL_TEXTURE_1D,
			0,
			gl.GL_INTENSITY8,
			256,
			0,
			gl.GL_LUMINANCE,
			gl.GL_UNSIGNED_BYTE,
			texmap);
		gl.glTexParameteri(gl.GL_TEXTURE_1D, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(gl.GL_TEXTURE_1D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
		gl.glTexParameteri(gl.GL_TEXTURE_1D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);

		/* Make 16-bit identity texture that maps (s,t)=(z,z*256) to [0,65535]/65535. */

//		for (j = 0; j < 256; j++)
//		{
//			for (i = 0; i < 256; i++)
//			{
//				texmap[j * 512 + i * 2 + 0] = (byte)j;
//				/* Luminance has least sigificant bits. */
//				texmap[j * 512 + i * 2 + 1] = (byte)i;
//				/* Alpha has most sigificant bits. */
//			}
//		}
//		gl.glBindTexture(gl.GL_TEXTURE_2D, TO_MAP_16BIT);
//		gl.glTexImage2D(
//			gl.GL_TEXTURE_2D,
//			0,
//			gl.GL_LUMINANCE8_ALPHA8,
//			256,
//			256,
//			0,
//			gl.GL_LUMINANCE_ALPHA,
//			gl.GL_UNSIGNED_BYTE,
//			texmap);
//		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_EDGE);
//		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);
//		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
//		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);

		/* GL_LEQUAL ensures that when fragments with equal depth are
		   generated within a single rendering pass, the last fragment
		   results. */
		gl.glDepthFunc(gl.GL_LEQUAL);
		gl.glEnable(gl.GL_DEPTH_TEST);

		gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT0);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, zero);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, scene.light.specularColor);
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, scene.light.diffuseColor);

		//gl.glLineStipple(1, 0xf0f0);

		gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 30.0f);

		gl.glEnable(gl.GL_CULL_FACE);
		//gl.glLightModeli(gl.GL_LIGHT_MODEL_LOCAL_VIEWER, 1);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_COLOR_CONTROL, GL.GL_SEPARATE_SPECULAR_COLOR);

		//  #if 000
		//	/* This would work too. */
		//	glEnable(GL_RESCALE_NORMAL_EXT);
		//  #else
		//gl.glEnable(gl.GL_NORMALIZE);
		//  #endif

		/*	gl.glGenTextures(1, tmp);
			TO_MAP_8BIT = tmp[0];
			gl.glBindTexture(GL.GL_TEXTURE_2D, TO_HW_DEPTH_MAP);
		
			gl.glGenTextures(1, tmp);
			TO_MAP_16BIT = tmp[0];
			gl.glBindTexture(GL.GL_TEXTURE_2D, TO_HW_DEPTH_MAP);
		*/
		gl.glGenTextures(1, tmp);
		TO_HW_DEPTH_MAP = tmp[0];
		//gl.glBindTexture(GL.GL_TEXTURE_2D, TO_HW_DEPTH_MAP);
		//System.out.println("TO_HW_DEPTH_MAP " + TO_HW_DEPTH_MAP);

//		gl.glGenTextures(1, tmp);
//		TO_HW_DEPTH_MAP_RECT = tmp[0];
		//gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_NV, TO_HW_DEPTH_MAP_RECT);
//		System.out.println("TO_HW_DEPTH_MAP_RECT " + TO_HW_DEPTH_MAP_RECT);

		//	if (hasRegisterCombiners) {
//		depthMapFormat = GL.GL_LUMINANCE_ALPHA;
//		depthMapInternalFormat = GL.GL_LUMINANCE8_ALPHA8;
//		depthMapPrecision = GL.GL_UNSIGNED_SHORT;
		//	}
		updateDepthScale();
		updateDepthBias(0); /* Update with no offset change. */

		//	if (drawFront) {
		//	  glDrawBuffer(GL_FRONT);
		//	  glReadBuffer(GL_FRONT);
		//	}
		//gl.glEnable(GL.GL_NORMALIZE);
	}

	void updateDepthScale()
	{
		int[] depthBits = new int[1];

		gl.glGetIntegerv(GL.GL_DEPTH_BITS, depthBits);
		if (depthBits[0] < 24)
		{
			depthScale24 = 1;
		}
		else
		{
			depthScale24 = 1 << (depthBits[0] - 24);
		}
		if (depthBits[0] < 16)
		{
			depthScale16 = 1;
		}
		else
		{
			depthScale16 = 1 << (depthBits[0] - 16);
		}
		if (depthBits[0] < 8)
		{
			depthScale8 = 1;
		}
		else
		{
			depthScale8 = 1 << (depthBits[0] - 8);
		}
	}

	void updateDepthBias(int delta)
	{
		float scale, bias;

		//  if (useShadowMapSupport) {
		if (true)
		{
			if (hwDepthMapPrecision == GL.GL_UNSIGNED_SHORT)
			{
				depthBias16 += delta;
				scale = slopeScale;
				bias = depthBias16 * depthScale16;
			}
			else
			{
				depthBias24 += delta;
				scale = slopeScale;
				bias = depthBias24 * depthScale24;
			}
		}
		else
		{
//			if (depthMapPrecision == GL.GL_UNSIGNED_SHORT)
//			{
//				depthBias16 += delta;
//				scale = slopeScale;
//				bias = depthBias16 * depthScale16;
//			}
//			else
//			{
//				depthBias8 += delta;
//				scale = slopeScale;
//				bias = depthBias8 * depthScale8;
//			}
		}
		//gl.glPolygonOffset(scale, bias);
		//System.out.println("scale " + scale + " bias " + bias);
		gl.glPolygonOffset(5.0f, 0.0f);
		//needTitleUpdate = 1;
	}

	public void dumpStats()
	{
		dumpStats = true;
	}

	/*
	public void resetGraph()
	{
		scene.rebind(gl);
		resetGraph = false;
	}
	*/

	public void reshape(GLDrawable drawable, int x, int y, int width, int height)
	{
		winWidth = width;
		winHeight = height;

		//gl.glMatrixMode(GL.GL_PROJECTION);

		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
//		System.err.println();
//		System.err.println(
//			"glLoadTransposeMatrixfARB() supported: "
//				+ gl.isFunctionAvailable("glLoadTransposeMatrixfARB"));

		gl.glViewport(0, 0, width, height);
		winAspectRatio = (float)winHeight / (float)winWidth;
		scene.camera.setAspectRatio(winAspectRatio);
	}
	
	public void init()
	{
		gldrawable.setRenderingThread(Thread.currentThread());
		gldrawable.setNoAutoRedrawMode(true);
	}

	public void render()
	{
		noException = false;
		gldrawable.display();
		noException = true;
	}

	public void exit()
	{
		try
		{
			RendererUtils.unbindArrays(drawListOpaque, gl);
			RendererUtils.unbindArrays(drawListTransparent, gl);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		gldrawable.setNoAutoRedrawMode(false);

		if (noException)
		{
			gldrawable.setRenderingThread(null);
		}
	}

	void renderLightObject()
	{
		gl.glPushMatrix();
		gl.glTranslatef(
			scene.light.lightVector[0],
			scene.light.lightVector[1],
			scene.light.lightVector[2]);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		glu.gluSphere(q, 0.1f, 10, 10);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glPopMatrix();

	}

	private void setupLightView()
	{
		if (autoLightLookAt)
		{
			VectorMatrixUtils.buildLookAtMatrix(
				scene.lightViewMatrix,
				scene.getLight().lightVector[0],
				scene.getLight().lightVector[1],
				scene.getLight().lightVector[2],
				0,
				1,
				0,
				0,
				1,
				0);
		}
		VectorMatrixUtils.buildOrthoMatrix(
			scene.lightFrustumMatrix,
			-scene.lightHalfWidth,
			scene.lightHalfWidth,
			-scene.lightHalfHeight,
			scene.lightHalfHeight,
			scene.lightNear,
			scene.lightFar);

		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadMatrixd(scene.lightFrustumMatrix);
		//	gl.glOrtho(-2.0,2.0,-2.0,2.0,lightNear,lightFar);
		//	} else {
		//	gl.glLoadIdentity();
		//	gl.glScalef(winAspectRatio, 1, 1);
		//	gl.glMultMatrixd(lightFrustumMatrix);
		//	}

		gl.glMatrixMode(gl.GL_MODELVIEW);
		gl.glLoadMatrixd(scene.lightViewMatrix);
	}

	public void displayChanged(GLDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
	}

	void loadProjectionThenModelView(GL gl, Camera camera)
	{
		camera.rebuildPerspectiveMatrix();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadMatrixd(camera.eyeFrustumMatrix);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadMatrixd(camera.eyeViewMatrix);
		//vecmath.Util.printMatrix4d(camera.eyeViewMatrix);
	}
	
	/*
		public void sortDrawListOpaqueByState()
		{
			for (ListIterator e = drawListOpaque.listIterator(); e.hasNext();)
			{
				RenderShape renderShape = ((RenderShape)e.next());
				setRenderStateMask(renderShape);
			}
			Collections.sort(drawListOpaque, aComparator);
		}
	*/

	public void sortDrawListTransparentByDepth()
	{
		Collections.sort(drawListTransparent, zComparator);
	}

	synchronized public void pause()
	{
		pause = true;
		try
		{
			synchronized(this)
			{
				wait();
			}
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	synchronized public void resume()
	{
		pause = false;
		notifyAll();
	}

	public void display(GLDrawable drawable)
	{
		if ( pause )
		{
			System.out.println("Renderer Pausing...");
			try
			{
				synchronized(this)
				{
					notifyAll();
					wait();
				}
			}
			catch (Exception e){e.printStackTrace();}
		}
		
		if ( !bindBin.isEmpty() )
		{
			System.out.println("Renderer binding shapes = " + bindBin.size());
			try
			{
				RendererUtils.bindArrays(bindBin,gl);
				bindBin.clear();
			}
			catch(Exception e){e.printStackTrace();}
		}
		
		if (dumpStats)
		{
			once = true;
			stateChanges = 0;
			dumpStats = false;
		}
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		if (globalWireFrameState)
		{
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
			//gl.glEnable(GL.GL_LINE_SMOOTH);
		}
		else
		{
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		}
		
		gl.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		// for stats
		stateSwitchCount = 0;
		stateShareCount = 0;
		alphaStateSwitchCount = 0;
		alphaStateShareCount = 0;
		shapeRenderStaticCount = 0;
		shapeRenderDynamicCount = 0;

		switch (drawMode)
		{
			default :
			case 0 : // Regular camera view
				loadProjectionThenModelView(gl, scene.camera);
				scene.skyBox.drawSkybox(gl, scene.camera.eyeViewMatrix);
				renderSimple();
				break;
			case 1 : // From light view
				setupLightView();
				scene.skyBox.drawSkybox(gl, scene.lightViewMatrix);
				renderSimple();
				break;
			case 2 : // View depth map
				renderDepthMap();
				break;
			case 3 : // View scene depth map textured
				renderEyeViewDepthTextured(false);
				break;
			case 4 : // View scene true depth textured
				renderEyeViewDepthTextured(true);
				break;
			case 5 : // View depth mapped shadowed
				renderEyeViewShadowed();
				break;
		}
		gl.glPopAttrib();
		renderFPS(gl, glu);
		once = false;
	}

	void renderSimple()
	{
		shapeRenderStaticCount = 0;
		shapeRenderDynamicCount = 0;

		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		disableTexgen();
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, scene.light.ambientColor);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, scene.light.diffuseColor);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, scene.light.specularColor);

		scene.light.lightVector[3] = 0.0f;

		gl.glEnable(gl.GL_LIGHT0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, scene.light.lightVector);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, scene.light.ambientColor);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, whiteMaterial);

		gl.glPushMatrix();
		gl.glMultMatrixf(scene.sceneMatrix);

		render(drawListOpaque, true && globalTextureState, 0, null);

		gl.glDepthMask(false);

		render(drawListTransparent, true && globalTextureState, 0);

		gl.glPopMatrix();
		gl.glDepthMask(true);
	}

	void render(RenderBinNode node, boolean textured, int textureShift, ShapeRenderer shapeRenderer )
	{
		// resent/clear current state globals
		currentMat = null;
		lastVertexArraySet = null;
		lastAppearance = null;
		gl.glPushMatrix();
		Object object = node.getBin();
		if (object instanceof RenderShape)
		{
			if ( shapeRenderer == null )
			render((RenderShape)object, gl, textured, textureShift);
			else
			shapeRenderer.render((RenderShape)object, gl, glu);
		}
		else if (object instanceof ArrayList)
		{
			ArrayList list = (ArrayList)object;
			int size = list.size();
			for (int i = 0; i < size; i++)
			{
				Object object2 = list.get(i);
				//if ( object2 instanceof RenderBinNode)
				{
					gl.glPopMatrix();
					render((RenderBinNode)object2, textured, textureShift, shapeRenderer);
					gl.glPushMatrix();
				}
			}
		}
		gl.glPopMatrix();
	}

	void render(ArrayList list, boolean textured, int textureShift)
	{
		currentMat = null;
		lastVertexArraySet = null;
		lastAppearance = null;
		gl.glPushMatrix();
		int size = list.size();
		for (int i = 0; i < size; i++)
		{
			Object object = list.get(i);
			if (object instanceof RenderShape)
			{
				render(((RenderShape)object), gl, textured, textureShift);
			}
		}
		gl.glPopMatrix();
	}

	public void render(
		RenderShape shape,
		GL gl,
		boolean textured,
		int textureShift)
	{
		VertexArraySet shapeVAS = shape.vertexArraySet;
		Appearance shapeApp = shape.appearance;
		if (!shapeApp.bound)
			RendererUtils.bindAppearance(shape, gl, glu);
		if (!shapeVAS.bound)
			RendererUtils.bindArrays(shape, gl);
		setRenderState(shapeApp, shapeVAS, gl, textured, textureShift);

//			if (!supportedVBO)
//			{
//				if (shapeVAS.displayListID == -1)
//				{
//					shapeVAS.displayListID = gl.glGenLists(1);
//					gl.glNewList(shapeVAS.displayListID, GL.GL_COMPILE);
//					System.out.println("Building display list - " + shapeVAS.displayListID);
//					//renderVertexArrays(shapeVAS,shapeApp,gl,textured,textureSwift);
//					gl.glEndList();
//				}
//				else
//				{
//					//gl.glCallList(shapeVAS.displayListID);
//				}
//			}
//			else
		// check if same matrix state as last shape's
		if (currentMat != shape.myMatrix)
		{
			if (shape.myMatrix != null)
			{
				// pop last shapes matrix state
				gl.glPopMatrix();
				// push in this shape's matrix state
				gl.glPushMatrix();
				gl.glMultMatrixf(shape.myMatrix);
			}
			else
			{
				// pop last shapes matrix state
				gl.glPopMatrix();
				// push in this shape's matrix state
				gl.glPushMatrix();
			}
			//System.out.println("Error: RenderShape null matrix");
			currentMat = shape.myMatrix;
		}

		if (lastVertexArraySet != shapeVAS)
		{
			//System.out.println("should see this");
			if (shapeVAS.staticGeometry)
			{
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.vertexArrayID);
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, zeroOffsetBuffer);
			}
			else
			{
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, shapeVAS.vertexArray);
			}
			gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
			//System.out.println("Not shared " + lastVertexArraySet.vertexArrayID + " " + shape.vertexArrayID);

			if (shapeVAS.colorArray != null)
			{
				//System.out.println("Has colorarrays");
				if (shapeVAS.staticGeometry)
				{
					gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.colorArrayID);
					gl.glColorPointer(3, GL.GL_FLOAT, 0, zeroOffsetBuffer);
				}
				else
				{
					gl.glColorPointer(3, GL.GL_FLOAT, 0, shapeVAS.colorArray);
				}
				gl.glEnableClientState(GL.GL_COLOR_ARRAY);
			}
			else
			{
				gl.glDisableClientState(GL.GL_COLOR_ARRAY);
			}

			if (shapeApp.material != null
				&& shapeApp.material.lightingEnable
				&& shapeVAS.normalArray != null)
			{
				if (shapeVAS.staticGeometry)
				{
					gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.normalArrayID);
					gl.glNormalPointer(GL.GL_FLOAT, 0, zeroOffsetBuffer);
				}
				else
				{
					gl.glNormalPointer(GL.GL_FLOAT, 0, shapeVAS.normalArray);
				}
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
			}
			else
			{
				gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
				//gl.glDisable(GL.GL_LIGHTING);
				//gl.glColor3f(1.0f, 1.0f, 1.0f);
			}

			//textured = false;
			if (textured)
			{
			// set up texture geometry state
			if ((shapeVAS.textureCoordArray != null) && (shapeApp.textureStageState != null))
			{
				gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB + textureShift);
				if (shapeVAS.staticGeometry)
				{
					gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.textureCoordArrayID[0]);
					gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, zeroOffsetBuffer);
				}
				else
				{
					gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, shapeVAS.textureCoordArray[0]);
				}
				gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glEnable(GL.GL_TEXTURE_2D);

				if (shapeApp.textureStageState.length > 1)
				{
					gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB + textureShift);
					if (shape.appearance.textureStageState[1].texture != null)
					{
						if (shapeVAS.staticGeometry)
						{
							gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.textureCoordArrayID[1]);
							gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, zeroOffsetBuffer);
						}
						else
						{
							gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, shapeVAS.textureCoordArray[1]);
						}
						gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
					}
					else
					{
						//System.out.println("Error: Missing Texture in textureStageState[1] for shape " + shape);
						gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
					}
				}
				gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB + shapeApp.textureStageState.length + textureShift);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB + shapeApp.textureStageState.length + textureShift);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			}
			else
			{
				gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB + textureShift);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB + textureShift);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glClientActiveTextureARB(GL.GL_TEXTURE2_ARB + textureShift);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
			}
		}
		lastVertexArraySet = shapeVAS;
		}
		try
		{
			if (shapeVAS.staticGeometry)
			{
				shapeRenderStaticCount++;
				gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, shapeVAS.faceIndicesID);
				gl.glDrawElements(
					GL.GL_TRIANGLES,
					shapeVAS.faceIndices.capacity(),
					GL.GL_UNSIGNED_INT,
					zeroOffsetBuffer);
			}
			else
			{
				gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
				shapeRenderDynamicCount++;
				gl.glDrawElements(
					GL.GL_TRIANGLES,
					shapeVAS.faceIndices.capacity(),
					GL.GL_UNSIGNED_INT,
					shapeVAS.faceIndices);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	static public void setRenderStateMask(Appearance shapeApp)
	{
		int bits = 0;

		if (shapeApp.material != null && shapeApp.material.lightingEnable)
			bits |= Appearance.lightMask;
		if (shapeApp.shadeModel == Appearance.SHADE_SMOOTH)
			bits |= Appearance.shadeModelMask;
		if (shapeApp.textureEnable)
			bits |= Appearance.textureMask;
		if (shapeApp.material != null)
			bits |= Appearance.materialMask;
		if (shapeApp.CULL)
			bits |= Appearance.cullMask;
		//		if (app.colorArrayEnable == true)
		//			bits |= Appearance.colorArrayMask;

		shapeApp.stateBitSet = bits;
		if (shapeApp.textureStageState != null)
			shapeApp.textureCount = shapeApp.textureStageState.hashCode();
		else
			shapeApp.textureCount = 0;
	}

	// Sets the current OpenGL render state attribtues
	// based on the Appearance and VertexArraySet passed in
	public void setRenderState(
		Appearance shapeApp,
		VertexArraySet shapeVAS,
		GL gl,
		boolean textured,
		int textureShift)
	{
		setRenderStateMask(shapeApp);
		if (lastAppearance != shapeApp)
		{
			if (shapeApp.hasAlpha)
			{
				alphaStateSwitchCount++;
				gl.glEnable(GL.GL_BLEND);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				if (debugLevel == 1)
					System.out.println("On");
			}
			else
			{
				stateSwitchCount++;
				gl.glDisable(GL.GL_BLEND);
				if (debugLevel == 1)
					System.out.println("off");
			}
			if (shapeApp.material != null)
			{
				if (shapeApp.material.lightingEnable)
				{
					//System.out.println("setting material alpha " + shapeApp.material.diffuseColor[3]);
					gl.glMaterialfv(
						GL.GL_FRONT,
						GL.GL_AMBIENT_AND_DIFFUSE,
						shapeApp.material.diffuseColor);
				}
				else
				{
					//gl.glColor3f(shapeApp.material.diffuseColor[0], shapeApp.material.diffuseColor[1], shapeApp.material.diffuseColor[2]);
					gl.glColor4fv(shapeApp.material.diffuseColor);
				}
				if (shapeVAS.colorArray != null
					&& !shapeApp.ignoreVertexColors)
				{
					//System.out.println("setting colorArray ");
					gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
					gl.glEnable(GL.GL_COLOR_MATERIAL);
				}
				else
				{
					gl.glDisable(GL.GL_COLOR_MATERIAL);
				}
			}
			else
			{
				//System.out.println("WHOA " + shape);
				//gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, whiteMaterial);
				//.glDisable(GL.GL_LIGHTING);
				gl.glColor3f(
					shapeApp.color[0],
					shapeApp.color[1],
					shapeApp.color[2]);
				gl.glDisable(GL.GL_COLOR_MATERIAL);
			}
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, shapeApp.polygonMode);
			if (shapeApp.polygonMode == GL.GL_LINE)
			{
				//System.out.println("LINES");
				gl.glLineWidth(4.0f);
				gl.glEnable(GL.GL_CULL_FACE);
				gl.glCullFace(shapeApp.cullFace);
			}
			gl.glShadeModel(shapeApp.shadeModel);

			if (shapeApp.CULL)
			{
				gl.glEnable(GL.GL_CULL_FACE);
				gl.glCullFace(shapeApp.cullFace);
			}
			else
				gl.glDisable(GL.GL_CULL_FACE);

			if (shapeApp.material != null)
			{
				if (shapeApp.material.lightingEnable)
				{
					//System.out.println("setting material alpha " + shapeApp.material.diffuseColor[3]);
					gl.glEnable(GL.GL_LIGHTING);
				}
				else
				{
					gl.glDisable(GL.GL_LIGHTING);
				}
			}
			else
			{
				//System.out.println("WHOA " + shape);
				//gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, whiteMaterial);
				gl.glDisable(GL.GL_LIGHTING);
			}
			if (textured)
			{
				// set up texture render state
				if (shapeApp.textureStageState != null)
				{
					gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB + textureShift);
					if (shapeApp.textureStageState[0].texture != null)
					{
						gl.glEnable(GL.GL_TEXTURE_2D);
						shapeApp.textureStageState[0].texture.bind(gl);
					}
					else
					{
						gl.glDisable(GL.GL_TEXTURE_2D);
					}
				
					if (shapeApp.textureStageState.length > 1)
					{
						gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB + textureShift);
						if (shapeApp.textureStageState[1].texture != null)
						{
							gl.glEnable(GL.GL_TEXTURE_2D);
							shapeApp.textureStageState[1].texture.bind(gl);
						}
						else
						{
							//System.out.println("Error: Missing Texture in textureStageState[1] for shape " + shape);
							gl.glDisable(GL.GL_TEXTURE_2D);
						}
					}
					gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB + shapeApp.textureStageState.length + textureShift);
					gl.glDisable(GL.GL_TEXTURE_2D);
					gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB + shapeApp.textureStageState.length + textureShift);
					gl.glDisable(GL.GL_TEXTURE_2D);		
				}
				else
				{
					gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB + textureShift);
					gl.glDisable(GL.GL_TEXTURE_2D);
					gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB + textureShift);
					gl.glDisable(GL.GL_TEXTURE_2D);		
					gl.glActiveTextureARB(GL.GL_TEXTURE2_ARB + textureShift);
					gl.glDisable(GL.GL_TEXTURE_2D);		
				}
			}
		}
		else
		{
			if (shapeApp.hasAlpha)
			{
				alphaStateShareCount++;
			}
			else
			{
				stateShareCount++;
			}
		}
		lastAppearance = shapeApp;
	}

	double start;
	public static int count_fps, fps;
	void renderFPS(GL gl, GLU glu)
	{
		int viewport[] = new int[4];

		if (start == 0)
			start = System.currentTimeMillis();

		if (System.currentTimeMillis() - start >= 1000)
		{
			fps = count_fps;
			start = 0;
			count_fps = 0;
		}
		count_fps++;
		gl.glDisable(GL.GL_LIGHTING);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glGetIntegerv(gl.GL_VIEWPORT, viewport);
		glu.gluOrtho2D(0, viewport[2], viewport[3], 0);
		gl.glDepthFunc(gl.GL_ALWAYS);
		gl.glColor3f(1, 1, 1);
		gl.glRasterPos2f(15, 15);

		glut.glutBitmapString(gl, glut.BITMAP_HELVETICA_18, "FPS: " + fps);

		gl.glDepthFunc(gl.GL_LESS);
		gl.glPopMatrix();
		gl.glMatrixMode(gl.GL_MODELVIEW);
		gl.glPopMatrix();
		gl.glEnable(GL.GL_LIGHTING);
	}

	void updateDepthMap()
	{
		drawDynamic = true;

		int bytesPerDepthValue;
		int requiredSize;

		int width, height, lowRes;
		int target;
		int texobj;

		width = depthMapSize;
		height = depthMapSize;

		gl.glViewport(0, 0, width, height);

		// Setup light view with a square aspect ratio
		// since the texture is square.
		setupLightView();

		// Disable everything but vertices for speed.
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	
		gl.glDisable(GL.GL_LIGHTING);
		
		gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE2_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE2_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE3_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE3_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);

		// Only need depth values so avoid writing colors for speed.
		gl.glColorMask(false, false, false, false);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
		
		// shouldn't matter, but juse in case
		gl.glShadeModel(GL.GL_FLAT);

		gl.glPushMatrix();
		gl.glMultMatrixf(scene.sceneMatrix);
		
		// render using alternate renderer class
		render(drawListOpaque, false, 0, ShapeRenderer.getInstance());

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_GREATER, 0.5f);

		// render using normal render here because we want
		// alpha textures to render to the depth buffer for 
		// the correct shadows
		render(drawListTransparent, true, 0);
		gl.glPopMatrix();

		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_ALPHA_TEST);
		gl.glDisable(gl.GL_STENCIL_TEST);

		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glEnable(GL.GL_TEXTURE_2D);

		float[] hwBorderColor = { 1.0f, 0.0f, 0.0f, 0.0f };

		target = GL.GL_TEXTURE_2D;
		texobj = TO_HW_DEPTH_MAP;
		gl.glBindTexture(target, texobj);
		
		// case we chaneg the depth map sizes
		// during run-time for testing
		if ((width != lastHiResHwWidth)
			|| (height != lastHiResHwHeight)
			|| (hwDepthMapPrecision != lastHiResHwPrecision)
			|| (hwDepthMapFiltering != lastHiResHwFiltering))
		{
			gl.glTexParameterfv(target, gl.GL_TEXTURE_BORDER_COLOR, hwBorderColor);
			gl.glTexParameteri(target, gl.GL_TEXTURE_MIN_FILTER, hwDepthMapFiltering);

			gl.glTexParameteri(target, gl.GL_TEXTURE_MAG_FILTER, hwDepthMapFiltering);
			gl.glTexParameteri(target, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_BORDER);
			gl.glTexParameteri(target, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_BORDER);
			
			// Shadow parameters = hardware magic ;-)
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_DEPTH_TEXTURE_MODE_ARB, gl.GL_INTENSITY);
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE_ARB, gl.GL_COMPARE_R_TO_TEXTURE);
    		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_FUNC_ARB, gl.GL_LEQUAL);

			gl.glCopyTexImage2D(target, 0, hwDepthMapInternalFormat, 0, 0, width, height, 0);
			lastHiResHwWidth = width;
			lastHiResHwHeight = height;
			lastHiResHwPrecision = hwDepthMapPrecision;
			lastHiResHwFiltering = hwDepthMapFiltering;
			System.out.println("Setting up depth map..................");
		}
		else
		{
			// Texture object dimensions already initialized.
			gl.glCopyTexSubImage2D(target, 0, 0, 0, 0, 0, width, height);
		}

		/* Reset state back to normal. */
		gl.glDisable(gl.GL_POLYGON_OFFSET_FILL);
		gl.glEnable(gl.GL_LIGHTING);
		gl.glColorMask(true, true, true, true);
		gl.glEnable(gl.GL_CULL_FACE);
		gl.glViewport(0, 0, winWidth, winHeight);
	}

	void renderDepthMap()
	{
		int target;
		int texobj;

		updateDepthMap();

		gl.glViewport(0, 0, winWidth, winHeight);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glScalef(winAspectRatio, 1.0f, 1.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		//gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL.GL_TEXTURE_2D);

		target = GL.GL_TEXTURE_2D;
		texobj = TO_HW_DEPTH_MAP;

		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE_EXT);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB_EXT, GL.GL_REPLACE);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_SOURCE0_RGB_EXT, GL.GL_TEXTURE);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_OPERAND0_RGB_EXT, GL.GL_SRC_COLOR);

		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE_ARB, gl.GL_NONE);

		gl.glEnable(target);
		gl.glBindTexture(target, texobj);
		gl.glTexEnvf(GL.GL_TEXTURE_FILTER_CONTROL_EXT, GL.GL_TEXTURE_LOD_BIAS_EXT, textureLodBias);

		/* Standard 2D textures use normalized texture coordinates in the
		   [0..1]x[0..1] rnage. */
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(-1, -1);
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(1, -1);
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(1, 1);
		gl.glTexCoord2f(0, 1);
		gl.glVertex2f(-1, 1);
		gl.glEnd();

		gl.glTexEnvf(GL.GL_TEXTURE_FILTER_CONTROL_EXT, GL.GL_TEXTURE_LOD_BIAS_EXT, 0.0f);
		gl.glDisable(target);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LIGHTING);
	}

	void renderEyeViewDepthTextured(boolean rangeMap)
	{
		int target;

//		gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB);
//		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
//		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
//		gl.glDisable(GL.GL_TEXTURE_2D);
//		gl.glClientActiveTextureARB(GL.GL_TEXTURE2_ARB);
//		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
//		gl.glActiveTextureARB(GL.GL_TEXTURE2_ARB);
//		gl.glDisable(GL.GL_TEXTURE_2D);
//		gl.glClientActiveTextureARB(GL.GL_TEXTURE3_ARB);
//		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
//		gl.glActiveTextureARB(GL.GL_TEXTURE3_ARB);
//		gl.glDisable(GL.GL_TEXTURE_2D);
//		
//		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glEnable(GL.GL_TEXTURE_2D);

		if (!rangeMap)
			updateDepthMap();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		//  if (wireFrame) {
		//	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		//  }

		loadProjectionThenModelView(gl, scene.camera);

		gl.glPushMatrix();
		gl.glMultMatrixf(scene.sceneMatrix);

		configTexgen(rangeMap, 0);
		gl.glDisable(GL.GL_LIGHTING);

		target = GL.GL_TEXTURE_2D;

			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE_EXT);
			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB_EXT, GL.GL_REPLACE);
			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_SOURCE0_RGB_EXT, GL.GL_TEXTURE);
						gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_OPERAND0_RGB_EXT, GL.GL_SRC_COLOR);

		render(drawListOpaque, false, 0, null);
		render(drawListTransparent, false, 0);
		gl.glPopMatrix();

		disableTexgen();

		renderLightObject();
		gl.glEnable(GL.GL_LIGHTING);

	}

	/* Scale and bias [-1,1]^3 clip space into [0,1]^3 texture space. */
	double[] Smatrix = { 0.5, 0, 0, 0, 0, 0.5, 0, 0, 0, 0, 0.5, 0, 0.5, 0.5, 0.5, 1.0 };
	/* Scale and bias [-1,1]^3 clip space into [0,1]^3 texture space, and then
	   move LightSpace Z to S, move 256 * LightSpace Z to T, zero out R, and 
	   leave LightSpace Q alone. */
	double[] RSmatrix = { 0, 0, 0, 0, 0, 0, 0, 0, 0.5, 128, 0, 0, 0.5, 128, 0, 1.0 };

	// Exact configuration from nVidia demos/examples
	// with the exception of adding in the Scene's root transform
	void configTexgen(boolean _rangeMap, int bits)
	{
		float[] p = new float[4];
		float wrapScale;
		double[] m1 = new double[16], m2 = new double[16];

		if (_rangeMap)
		{
			/* Generate the depth map space planar distance as the S/Q texture
			   coordinate. */

			gl.glBindTexture(GL.GL_TEXTURE_1D, TO_MAP_8BIT);
			gl.glEnable(GL.GL_TEXTURE_1D);
			gl.glDisable(GL.GL_TEXTURE_2D);

			VectorMatrixUtils.copyMatrix(m1, RSmatrix);
			wrapScale = (1 << bits);
			m1[0] *= wrapScale;
			m1[4] *= wrapScale;
			m1[8] *= wrapScale;
			m1[12] *= wrapScale;
		}
		else
		{
			int target;
			int texobj;

			VectorMatrixUtils.copyMatrix(m1, Smatrix);

			target = GL.GL_TEXTURE_2D;
			texobj = TO_HW_DEPTH_MAP;
			gl.glBindTexture(target, texobj);
			gl.glEnable(target);
		}

		VectorMatrixUtils.multMatrices(m2, m1, scene.lightFrustumMatrix);
		VectorMatrixUtils.multMatrices(m1, m2, scene.lightViewMatrix);
		VectorMatrixUtils.copyMatrix(m3, scene.sceneMatrix);
		VectorMatrixUtils.multMatrices(m2, m1, m3);
		VectorMatrixUtils.copyMatrix(m1, m2);

		/* S and Q are always needed */
		p[0] = (float)m1[0];
		p[1] = (float)m1[4];
		p[2] = (float)m1[8];
		p[3] = (float)m1[12];
		gl.glTexGenfv(GL.GL_S, GL.GL_EYE_PLANE, p);
		p[0] = (float)m1[3];
		p[1] = (float)m1[7];
		p[2] = (float)m1[11];
		p[3] = (float)m1[15];
		gl.glTexGenfv(GL.GL_Q, GL.GL_EYE_PLANE, p);

		gl.glEnable(GL.GL_TEXTURE_GEN_S);
		gl.glEnable(GL.GL_TEXTURE_GEN_Q);

		if (_rangeMap && (bits == 0))
		{
			/* rangeMap is a 1D mode so it does not need to generate T or R. */
			gl.glDisable(GL.GL_TEXTURE_GEN_T);
			gl.glDisable(GL.GL_TEXTURE_GEN_R);
		}
		else
		{
			/* Hardware shadow mapping is a 2D texturing mode (uses S and T) that
				 also compares with R so S, T, and R must be generated. */
			p[0] = (float)m1[1];
			p[1] = (float)m1[5];
			p[2] = (float)m1[9];
			p[3] = (float)m1[13];
			gl.glTexGenfv(GL.GL_T, GL.GL_EYE_PLANE, p);

			p[0] = (float)m1[2];
			p[1] = (float)m1[6];
			p[2] = (float)m1[10];
			p[3] = (float)m1[14];
			gl.glTexGenfv(GL.GL_R, GL.GL_EYE_PLANE, p);

			gl.glEnable(GL.GL_TEXTURE_GEN_S);
			gl.glEnable(GL.GL_TEXTURE_GEN_T);
			gl.glEnable(GL.GL_TEXTURE_GEN_R);
		}
	}

	void disableTexgen()
	{
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		gl.glDisable(GL.GL_TEXTURE_GEN_R);
		gl.glDisable(GL.GL_TEXTURE_GEN_Q);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glDisable(GL.GL_TEXTURE_1D);
	}
	
	void renderEyeViewShadowed()
	{
		//System.out.println("shadows");
		updateDepthMap();

		gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

		loadProjectionThenModelView(gl, scene.camera);
		scene.skyBox.drawSkybox(gl, scene.camera.eyeViewMatrix);
		scene.light.lightVector[3] = 0.0f;
		gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, scene.light.lightVector);

		gl.glClientActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE1_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE2_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE2_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glClientActiveTextureARB(GL.GL_TEXTURE3_ARB);
		gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glActiveTextureARB(GL.GL_TEXTURE3_ARB);
		gl.glDisable(GL.GL_TEXTURE_2D);

		renderHardwareShadowPass();
		renderLightObject();
	}

	void renderHardwareShadowPass()
	{
		shapeRenderStaticCount = 0;
		shapeRenderDynamicCount = 0;
		drawStatic = true;
		drawDynamic = true;

		gl.glActiveTextureARB(GL.GL_TEXTURE1_ARB);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		disableTexgen();


		//gl.glColor3f(0.0f, 0.0f, 0.0f);
		//gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_LIGHT0);

		gl.glLightModeli(GL.GL_LIGHT_MODEL_COLOR_CONTROL, GL.GL_SEPARATE_SPECULAR_COLOR);

		float ambientColorH[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, scene.light.ambientColor);
		float diffuseColorH[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, scene.light.diffuseColor);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, scene.light.specularColor);
		gl.glEnable(GL.GL_LIGHTING);
		
		gl.glPushMatrix();
		gl.glMultMatrixf(scene.sceneMatrix);
		configTexgen(false, 0);


		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);

		// Enable shadow comparison
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE_ARB, gl.GL_COMPARE_R_TO_TEXTURE);

		// Shadow comparison should be true (ie not in shadow) if r<=texture
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_FUNC_ARB, gl.GL_LEQUAL);

		// Shadow comparison should generate an INTENSITY result
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_DEPTH_TEXTURE_MODE_ARB, gl.GL_INTENSITY);
		
		// Set ARB extension for ambient color
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_FAIL_VALUE_ARB, scene.light.ambientColor[0]);

		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		//float ambientColorT[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		//gl.glTexEnvfv(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_COLOR, ambientColorT);
		
		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE_ARB, gl.GL_COMPARE_R_TO_TEXTURE);
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_FUNC_ARB, gl.GL_LEQUAL);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_COMPARE_FAIL_VALUE_ARB, 0.5f);

		render(drawListOpaque, true, 1, null);

		gl.glDepthMask(false);

		render(drawListTransparent, true, 1);

		gl.glPopMatrix();

		drawStatic = true;
		drawDynamic = true;

		gl.glActiveTextureARB(GL.GL_TEXTURE0_ARB);
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE_ARB, gl.GL_NONE);
	}

	/**
	 * @return
	 */
	public Scene getScene()
	{
		return scene;
	}

	/**
	 * @param scene
	 */
	public void setScene(Scene scene)
	{
		this.scene = scene;
	}

	/**
	 * @return
	 */
	public boolean isGlobalWireFrameState()
	{
		return globalWireFrameState;
	}

	/**
	 * @param b
	 */
	public void setGlobalWireFrameState(boolean b)
	{
		globalWireFrameState = b;
	}

	/**
	 * @return
	 */
	public boolean isGlobalTextureState()
	{
		return globalTextureState;
	}

	/**
	 * @param b
	 */
	public void setGlobalTextureState(boolean b)
	{
		globalTextureState = b;
	}

}
class ShapeRenderer
{
	static ShapeRenderer instance;
	float[] currentMat = null;
	
	static public ShapeRenderer getInstance()
	{
		if (instance == null )
			instance = new ShapeRenderer();
		return instance;
	}
	
	public void render(RenderShape shape, GL gl, GLU glu)
	{
		VertexArraySet shapeVAS = shape.vertexArraySet;
		Appearance shapeApp = shape.appearance;

		// skipping polylines
		// remove to include them
		if ( shapeApp.polygonMode == GL.GL_LINE)
		{
			return;
		}
		
		// TODO optimization not possible with current ShapeRenderer design
//		if (currentMat != shape.myMatrix)
//		{
			if (shape.myMatrix != null)
			{
				// pop last shapes matrix state
				gl.glPopMatrix();
				// push in this shape's matrix state
				gl.glPushMatrix();
				gl.glMultMatrixf(shape.myMatrix);
			}
//			else
//			{
//				// pop last shapes matrix state
//				gl.glPopMatrix();
//				// push in this shape's matrix state
//				gl.glPushMatrix();
//			}
//			//System.out.println("Error: RenderShape null matrix");
//			currentMat = shape.myMatrix;
//			// stats
//		}
		{
			if (shapeVAS.staticGeometry)
			{
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, shapeVAS.vertexArrayID);
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, BasicRenderer.zeroOffsetBuffer);
			}
			else
			{
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, shapeVAS.vertexArray);
			}

			try
			{
				if (shapeVAS.staticGeometry)
				{
					gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, shapeVAS.faceIndicesID);
					gl.glDrawElements(
						GL.GL_TRIANGLES,
						shapeVAS.faceIndices.capacity(),
						GL.GL_UNSIGNED_INT,
						BasicRenderer.zeroOffsetBuffer);
				}
				else
				{
					gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
					gl.glDrawElements(
						GL.GL_TRIANGLES,
						shapeVAS.faceIndices.capacity(),
						GL.GL_UNSIGNED_INT,
						shapeVAS.faceIndices);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
//class AppearanceComparator implements Comparator
//{
//	public int compare(Object o1, Object o2)
//	{
//		Appearance app1 = (Appearance) ((RenderShape)o1).appearance;
//		Appearance app2 = (Appearance) ((RenderShape)o2).appearance;
//		return (app2.stateBitSet - app1.stateBitSet);
//	}
//
//	public boolean equals(Object obj)
//	{
//		return equals(obj);
//	}
//}

class ZComparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		float obZ1 = ((RenderShape)o1).cameraZ;
		float obZ2 = ((RenderShape)o2).cameraZ;
		return ((obZ1 - obZ2) > 0) ? 1 : -1;
	}

	public boolean equals(Object obj)
	{
		return equals(obj);
	}
}
