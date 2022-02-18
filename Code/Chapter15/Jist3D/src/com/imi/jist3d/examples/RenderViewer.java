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
package com.imi.jist3d.examples;
import java.awt.*;
import java.awt.event.*;

import net.java.games.jogl.*;
import com.imi.jist3d.render.*;
import com.imi.jist3d.util.*;

import java.util.*;
import javax.swing.event.MouseInputAdapter;

import java.nio.*;
import java.io.*;

public class RenderViewer extends MouseInputAdapter implements KeyListener
{
	BasicRenderer renderer = null;

	static Scene scene = new Scene();
	static String TXTPATH = "";
	static String[] TXTPATHS = null;

	private Runnable runnable;
	private Thread thread;
	private boolean shouldStop;
	private boolean shuffle = true;
	private boolean sortEnabled = true;

	float lightAngle = 135.0f * (float)Math.PI / 180.0f;
	float lightHeight = 8.0f;

	private int prevMouseX, prevMouseY;
	private boolean mouseRButtonDown = false;
	float eyeAngle = -60.0f;
	float eyeHeight = 2.0f;
	int xEyeBegin, yEyeBegin, movingEye = 0;
	int xLightBegin, yLightBegin, movingLight = 0;
	public float[] translation = new float[] { 0.0f, 0.0f, 0.0f };
	public float globalScale = 0.6f;
	public float z_factor = .04f;
	public float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
	public float angle = 0.0f;
	protected int x, y;
	protected int x_last, y_last;

	public static void main(String[] args)
	{
		RenderViewer viewer = new RenderViewer(args);
	}

	public RenderViewer(String[] args)
	{
		Frame frame = new Frame("Jist3D Sample and Performance Test");

		renderer = new BasicRenderer();
		renderer.setScene(scene);

		GLCanvas canvas = renderer.getCanvas();
		frame.add(canvas);

		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);

		frame.setSize(800, 600);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				stop();
				System.exit(0);
			}
		});
		frame.show();
		makeSphereTest(renderer);
		renderer.getScene().getSkyBox().setEnable(false);
		
		// options for testing renderer
		//renderer.sortDrawListOpaqueByState();
		//Collections.shuffle(renderer.drawListOpaque);
		//Collections.shuffle(scene.getSceneList());
		start();
	}

	/** Starts the render loop. */
	public synchronized void start()
	{
		if (thread != null)
		{
			throw new GLException("Already started");
		}
		if (runnable == null)
		{
			runnable = new Runnable()
			{
				public void run()
				{
					long startTime, endTime, deltaTime = 0;
					double time1 = 0.0f;
					int frameCount = 0;
					final int rollover = 30;
					float[] mat = new float[16];
					try
					{
						renderer.init();

						while (!shouldStop)
						{
							updateScene();

							RendererUtils.generateRenderBins(
								renderer.drawListOpaque,
								renderer.drawListTransparent,
								scene.getSceneList());

							RendererUtils.computeDrawListTransparentZDepth(
								renderer.getScene(),
								renderer.drawListTransparent);
								
							//renderer.dumpStats();

							//if ( shuffle )
							//		Collections.shuffle(renderer.drawListOpaque);
							startTime =
								sun.misc.Perf.getPerf().highResCounter();
							if (sortEnabled)
							{
								//renderer.sortDrawListOpaqueByState();
								renderer.sortDrawListTransparentByDepth();
							}
							endTime = sun.misc.Perf.getPerf().highResCounter();
							
							deltaTime = endTime - startTime;

							//System.out.println("sort " + deltaTime/100000000.0);

							//startTime = sun.misc.Perf.getPerf().highResCounter();
							renderer.render();
							time1 += deltaTime / 1000000.0;
							if (frameCount > rollover)
							{
								if (shuffle)
									System.out.println(
										"Shuffled render "
											+ time1 / rollover
											+ "m/s");
								else
									System.out.println(
										"No shuffle render "
											+ time1 / rollover
											+ "m/s");
								if (sortEnabled)
									System.out.println("Sort render ");
								else
									System.out.println("No sort render ");
								frameCount = 0;
								time1 = 0.0;
								System.out.println("Shape and State stats: ");
								System.out.println("  Shapes static = "
										+ renderer.shapeRenderStaticCount);
								System.out.println("  Shapes dynamic = "
										+ renderer.shapeRenderDynamicCount);
								System.out.println("  Opaque Render State switches = "
										+ renderer.stateSwitchCount
										+ " Shared = "
										+ renderer.stateShareCount);
								System.out.println(
									"  Alpha Render State switches = "
										+ renderer.alphaStateSwitchCount
										+ " Shared = "
										+ renderer.alphaStateShareCount);
								System.out.println();
							}
							renderer.stateSwitchCount = 0;
							renderer.stateShareCount = 0;
							renderer.alphaStateSwitchCount = 0;
							renderer.alphaStateShareCount = 0;
							frameCount++;
							//System.out.println("New");
						}
					}
					finally
					{
						shouldStop = false;
						try
						{
							renderer.exit();
						}
						finally
						{
							synchronized (RenderViewer.this)
							{
								thread = null;
								RenderViewer.this.notify();
							}
						}
					}
				}
			};
		}
		thread = new Thread(runnable);
		thread.start();
	}

	/** Stops the render loop, blocking until the render thread has finished. */
	public synchronized void stop()
	{
		shouldStop = true;
		while (shouldStop && thread != null)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	void updateScene()
	{
		//Update light position vector
		scene.getLight().setLightVector(
			(float) (8.0 * Math.sin(lightAngle)),
			lightHeight,
			(float) (8 * Math.cos(lightAngle)),
			1.0f);

		//Update camera view matrix
		VectorMatrixUtils.buildLookAtMatrix(
			scene.getCamera().eyeViewMatrix,
			15 * Math.sin(eyeAngle),
			eyeHeight,
			15 * Math.cos(eyeAngle),
			0,
			3,
			0,
			0,
			1,
			0);

		//Update scene/world matrix
		VectorMatrixUtils.rotY(scene.sceneMatrix, view_roty);
		VectorMatrixUtils.scale(scene.sceneMatrix, globalScale);
		scene.sceneMatrix[12] = translation[0];
		scene.sceneMatrix[13] = translation[1];
		scene.sceneMatrix[14] = translation[2];
	}
	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_F1 :
				renderer.drawMode = 0;
				break;
			case KeyEvent.VK_F2 :
				renderer.drawMode = 1;
				break;
			case KeyEvent.VK_F3 :
				renderer.drawMode = 2;
				break;
			case KeyEvent.VK_F4 :
				renderer.drawMode = 3;
				break;
			case KeyEvent.VK_F5 :
				renderer.drawMode = 4;
				break;
			case KeyEvent.VK_F6 :
				renderer.drawMode = 5;
				break;
			case KeyEvent.VK_W :
				renderer.setGlobalWireFrameState(
					!renderer.isGlobalWireFrameState());
				break;
			case KeyEvent.VK_S :
				stop();
				break;
			case KeyEvent.VK_SPACE :
				start();
				break;
			case KeyEvent.VK_T :
				renderer.setGlobalTextureState(
					!renderer.isGlobalTextureState());
				System.out.println("Switching wireframe state");
				break;
			case KeyEvent.VK_M :
				shuffle = !shuffle;
				break;
			case KeyEvent.VK_N :
				sortEnabled = !sortEnabled;
				break;
		}
	}

	// Methods required for the implementation of MouseMotionListener
	public void mousePressed(MouseEvent e)
	{
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			mouseRButtonDown = true;
		}
		xEyeBegin = e.getX();
		yEyeBegin = e.getY();
		xLightBegin = e.getX();
		yLightBegin = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
		if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			mouseRButtonDown = false;
		}
	}

	public void mouseDragged(MouseEvent e)
	{

		int x = e.getX();
		int y = e.getY();

		if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
		{
			Dimension size = e.getComponent().getSize();

			float thetaY =
				360.0f * ((float) (x - prevMouseX) / (float)size.width);
			float thetaX =
				-360.0f * ((float) (prevMouseY - y) / (float)size.height);

			view_rotx += thetaX;
			view_roty += thetaY;
			int dy = y - prevMouseY;
			translation[1] += -dy * z_factor;
		}
		else if ((e.getModifiers() & e.BUTTON2_MASK) != 0)
		{
			int dx = x - prevMouseX;
			int dy = y - prevMouseY;
			if ((e.getModifiers() & e.SHIFT_MASK) != 0)
			{
				scene.getCamera().setFieldOfView(
					scene.getCamera().getFieldOfView() + dy * z_factor);
			}
			else
			{
				globalScale += dy * z_factor * .1;
				//System.out.println(e);
			}
		}
		else if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			int dx = x - prevMouseX;
			int dy = y - prevMouseY;
			eyeAngle = (float) (eyeAngle - 0.005 * (x - xEyeBegin));
			eyeHeight = (float) (eyeHeight + 0.15 * (y - yEyeBegin));
			if (eyeHeight > 20.0)
				eyeHeight = 20.0f;
			if (eyeHeight < -12.0)
				eyeHeight = -12.0f;
			xEyeBegin = x;
			yEyeBegin = y;
		}
		prevMouseX = x;
		prevMouseY = y;
		//System.out.println(e);
	}

	static public void makeSphereTest(BasicRenderer renderer)
	{
		Scene sceneIn = renderer.getScene();
		RenderShape rShape = new RenderShape();
		Triangle[] triangle = GeoUtils.makeSphereTriangles(3);
		int[] faceIndices = new int[triangle.length * 3];
		float[] verts = GeoUtils.compactAndIndexify(triangle, faceIndices);

		FloatBuffer floats =
			ByteBuffer
				.allocateDirect(verts.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		floats.put(verts);
		rShape.vertexArraySet.vertexArray = floats;

		FloatBuffer normals =
			ByteBuffer
				.allocateDirect(verts.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		normals.put(verts);
		rShape.vertexArraySet.normalArray = normals;

		float[] texCoords = GeoUtils.generateTextureCoordsSphere(verts);
		FloatBuffer texCoordBs =
			ByteBuffer
				.allocateDirect(texCoords.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		texCoordBs.put(texCoords);
		rShape.vertexArraySet.textureCoordArray = new FloatBuffer[1];
		rShape.vertexArraySet.textureCoordArray[0] = texCoordBs;
		rShape.vertexArraySet.textureCoordArrayID = new int[1];
		rShape.vertexArraySet.textureCoordArrayID[0] = -1;

		TextureStageState[] textureStageState1 = new TextureStageState[1];
		textureStageState1[0] = new TextureStageState();
		try
		{
			textureStageState1[0].texture =
				TextureFactory.createTextureUnbound(
					"Earth",
					"data"
						+ File.separatorChar
						+ "sphere1.rgb");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		textureStageState1[0].texCoordGeneration = new TexCoordGeneration();

		TextureStageState[] textureStageState2 = new TextureStageState[1];
		textureStageState2[0] = new TextureStageState();
		try
		{
			textureStageState2[0].texture =
				TextureFactory.createTextureUnbound(
					"Mars",
					"data" + File.separatorChar + "sphere2.rgb");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		FloatBuffer colors =
			ByteBuffer
				.allocateDirect(verts.length * 4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		for (int i = 0; i < verts.length; i += 6)
		{
			verts[i + 0] = 1.0f;
			verts[i + 1] = 1.0f;
			verts[i + 2] = 1.0f;
		}
		colors.put(verts);
		rShape.vertexArraySet.colorArray = colors;
		rShape.vertexArraySet.faceIndices =
			(ByteBuffer.allocateDirect(faceIndices.length * 4))
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		//for (int i = 0; i < faceIndices.length; i++)
		//{
		rShape.vertexArraySet.faceIndices.put(faceIndices);
		//}

		rShape.appearance.cullFace = GL.GL_BACK;
		//rShape.appearance.polygonMode = 
		RenderShape rShape2 = null;
		//ArrayList sceneList = new ArrayList();

		int rowX = 5, rowY = 5, rowZ = 5;
		for (int i = 0; i < rowX; i++)
		{
			for (int j = 0; j < rowY; j++)
			{
				for (int k = 0; k < rowZ; k++)
				{
					rShape2 = (RenderShape)rShape.clone();
					//System.out.println("rShape2.appearance " + rShape2.appearance);
					//System.out.println("rShape2.appearance.myTextures " + rShape2.appearance.myTextures);
					//rShape2 = new RenderShape();
					//rShape2.vertexArraySet = (VertexArraySet)rShape.vertexArraySet.clone();
					//System.out.println("rShape2.vertexArraySet" + rShape2.vertexArraySet);
					rShape2.myMatrix =
						new float[] { 
							1,0,0,0,
							0,1,0,0,
							0,0,1,0,
							(i - (rowX / 2)) * 2.5f,
							(j - (rowY / 2)) * 2.5f,
							(k - (rowZ / 2)) * 2.5f,
							1 };

					//				Appearance app = (Appearance)(rShape.appearance.clone());
					Appearance app = new Appearance();
					app.textureEnable = true;
					app.cullFace = GL.GL_BACK;
					Material ma = new Material();
					app.material = ma;
					rShape2.appearance = app;
					//System.out.println(" color " + (float)(i*row+j)/(float)(row*row) );
					(
						Color.getHSBColor(
							(float) (i * rowX + j) / (float) (rowX * rowX),
							0.5f,
							1.0f)).getRGBComponents(
						ma.diffuseColor);
					ma.diffuseColor[3] = 1.0f;
					//System.out.println(" color " + ma.diffuseColor[0] + " " + ma.diffuseColor[1] + " " + ma.diffuseColor[2] + " " + ma.diffuseColor[3]);

					if (j % 2 == 1)
					{
						app.textureStageState = textureStageState1;
					}
					else
					{
						//app.myTextures = null;
						app.textureStageState = textureStageState2;
					}
					if (i == 0)
					{
						app.setShadeModel(Appearance.SHADE_SMOOTH);
						app.material.lightingEnable = true;
						app.textureEnable = true;
					}
					if (i == 1)
					{
						app.setShadeModel(Appearance.SHADE_SMOOTH);
						app.material.lightingEnable = true;
						app.textureEnable = false;
					}
					if (i == 2)
					{
						app.setShadeModel(Appearance.SHADE_FLAT);
						app.material.lightingEnable = true;
						app.textureEnable = true;
					}
					if (i == 3)
					{
						app.setShadeModel(Appearance.SHADE_FLAT);
						app.material.lightingEnable = true;
						app.textureEnable = false;
					}
					if (i == 4)
					{
						app.setShadeModel(Appearance.SHADE_SMOOTH);
						app.material.lightingEnable = false;
						app.textureEnable = true;
					}
					if (i == 5)
					{
						app.setShadeModel(Appearance.SHADE_SMOOTH);
						app.material.lightingEnable = false;
						app.textureEnable = false;
					}
					if (i == 6)
					{
						app.setShadeModel(Appearance.SHADE_FLAT);
						app.material.lightingEnable = false;
						app.textureEnable = true;
					}
					if (i == 7)
					{
						app.setShadeModel(Appearance.SHADE_FLAT);
						app.material.lightingEnable = false;
						app.textureEnable = false;
					}
					if (k % 2 == 0)
					{
						ma.diffuseColor[3] = 0.51f;
						app.hasAlpha = true;
						app.ignoreVertexColors = true;
						app.material.lightingEnable = true;
					}
					if (k % 2 == 0)
					{
						//app.textureEnable = false;
					}
					//app.textureEnable = false;
					//rShape2.staticGeometry = false;
					//rShape2.faceIndicesStatic = false;
					//rShape2.colorArrayStatic = false;
					//app.lightingEnable = false;
					//rShape2.colorArray = null;
					//rShape2.normalArray = null;
					renderer.bindBin.add(rShape2);
					sceneIn.getSceneList().add(rShape2);
				}
			}
		}
	}

}
