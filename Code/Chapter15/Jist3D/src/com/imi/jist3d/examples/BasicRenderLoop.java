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
import com.imi.jist3d.render.*;
import com.imi.jist3d.scenegraph.*;
import net.java.games.jogl.*;
/**
 * @author Shawn Kendall
 *
 */
public class BasicRenderLoop
{
	private Runnable runnable;
	private Thread thread;
	private boolean shouldStop = false;
	public EngineUpdate preUpdate;
	public EngineUpdate postUpdate;
	public BasicRenderer renderer;
	//public boolean wait = false;
	//float[] mat = new float[16];

	/** Creates a new BasicRenderLoop for a particular Renderer. */
	public BasicRenderLoop(BasicRenderer r)
	{
		renderer = r;
	}

	/** Starts this BasicRenderLoop. */
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
					try
					{
						renderer.init();
						while (!shouldStop)
						{
							try
							{
							if (preUpdate != null)
								preUpdate.update();

							SceneGraphUtils.generateDrawList(renderer.drawListOpaque,renderer.drawListTransparent,renderer.getScene().getSceneList());
							
							RendererUtils.computeDrawListTransparentZDepth(renderer.getScene(), renderer.drawListTransparent);
						  
							//renderer.sortDrawListOpaqueByState();
							renderer.sortDrawListTransparentByDepth();

							renderer.render();

							if (postUpdate != null)
								postUpdate.update();
							//System.out.println("New");
//							if (wait)
//							{
//								System.out.println("Animator waiting...");
//								try{
//									Thread.currentThread().suspend();
//								}
//								catch (Exception e){e.printStackTrace();}
//								wait = false;
//								System.out.println("Animator done waiting...");
//							}
							}
							catch( Exception e )
							{
								e.printStackTrace();	
							}
						}
					}
					catch( Exception e )
					{
						e.printStackTrace();	
					}
					finally
					{
						shouldStop = false;
						try
						{
							renderer.exit();
						}
						catch( Exception e )
						{
							e.printStackTrace();	
						}
						finally
						{
							synchronized (BasicRenderLoop.this)
							{
								thread = null;
								BasicRenderLoop.this.notifyAll();
								System.out.println("Frame ended, notifying...");
							}
						}
					}
				}
			};
		}
		thread = new Thread(runnable);
		thread.start();
	}

	/** Stops this animator, blocking until the animation thread has
		  finished. */
	public synchronized void stop()
	{
		shouldStop = true;
		while (shouldStop && thread != null)
		{
			System.out.println("waiting for frame to end...");
			try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("Done waiting for frame to end...");
	}
}

interface EngineUpdate
{
	public void update();
}