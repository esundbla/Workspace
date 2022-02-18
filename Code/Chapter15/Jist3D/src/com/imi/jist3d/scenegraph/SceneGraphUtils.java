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
package com.imi.jist3d.scenegraph;

import java.util.*;
import com.imi.jist3d.render.*;
import net.java.games.jogl.*;

public class SceneGraphUtils
{
	//static float[] mat = new float[16];

	static public void generateDrawList(
		RenderBinNode drawListOpaque,
		ArrayList drawListTransparent,
		ArrayList list)
	{
		//if ( resetGraph )
		//	resetGraph();
		//drawListOpaque.clear();
		drawListTransparent.clear();
		loadDrawLists(drawListOpaque, drawListTransparent, list, null);
		//sortDrawList();	
	}

	static public void loadDrawLists(
		RenderBinNode drawListOpaque,
		ArrayList drawListTransparent,
		ArrayList list,
		float[] mat)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Object node = list.get(i);
			if (node instanceof RenderShape)
			{
				loadDrawLists(
					drawListOpaque,
					drawListTransparent,
					node,
					((RenderShape)node).myMatrix);
			}
			else
				loadDrawLists(drawListOpaque, drawListTransparent, node, mat);
		}
	}

	static public void loadDrawLists(
		RenderBinNode drawListOpaque,
		ArrayList drawListTransparent,
		Object node,
		float[] mat)
	{
		if (node instanceof RenderShape)
		{
			RenderShape renderShape = ((RenderShape)node);
			if (mat == null)
			{
				renderShape.myMatrix = null;
			}
			else
			{
				//if ( renderShape.myMatrix == null )
				//	renderShape.myMatrix = new float[16];
				//copyMatrix( renderShape.myMatrix, mat );
				renderShape.myMatrix = mat;
			}
			if (renderShape.appearance.hasAlpha)
				drawListTransparent.add(renderShape);
			else
			{
				if (!renderShape.inView)
				{
					//if ( renderShape.appearance.hasAlpha )
					//	drawListTransparent.add( renderShape );
					//else
					{
						RendererUtils.insert(drawListOpaque, renderShape);
					}
					renderShape.inView = true;
				}
			}
			//drawListOpaque.add( renderShape );				
		}
		if (node instanceof TransformGroup)
		{
			//if (once)
			if (false)
				System.out.println("Pushing transform ");
			TransformGroup tg = (TransformGroup)node;
			//float[] matG = new float[16];
			if (mat == null)
				VectorMatrixUtils.copyMatrix(tg.matGlobal, tg.matLocal);
			else
				VectorMatrixUtils.multMatrices(tg.matGlobal, mat, tg.matLocal);

			Group group = (Group)node;
			int size = group.getChildren().size();
			for (int i = 0; i < size; i++)
			{
				loadDrawLists(
					drawListOpaque,
					drawListTransparent,
					group.getChildren().get(i),
					tg.matGlobal);
			}
			//if (once)
			if (false)
				System.out.println("Poping transform ");

		}
		else if (node instanceof Group)
		{
			Group group = (Group)node;
			int size = group.getChildren().size();
			for (int i = 0; i < size; i++)
			{
				loadDrawLists(
					drawListOpaque,
					drawListTransparent,
					group.getChildren().get(i),
					mat);
			}
		}
	}

	static public void loadBindBin(List bin, Object node)
	{
		if (node instanceof Group)
		{
			Group group = (Group)node;
			for (int i = 0; i < group.getChildren().size(); i++)
			{
				loadBindBin(bin, group.getChildren().get(i));
			}
		}
		else if (node instanceof RenderShape)
		{
			bin.add((RenderShape) (node));
		}
	}

	static public void unbindRenderShapes(ArrayList list, GL gl)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			unbindRenderShapes(obj, gl);
		}
	}

	static public void unbindRenderShapes(Object node, GL gl)
	{
		if (node instanceof Group)
		{
			Group group = (Group)node;
			for (int i = 0; i < group.getChildren().size(); i++)
			{
				unbindRenderShapes(group.getChildren().get(i), gl);
			}
		}
		else if (node instanceof RenderShape)
		{
			RendererUtils.unbindArrays((RenderShape) (node), gl);
		}
	}

	public static void dumpScene(Object object)
	{

		if (object instanceof RenderShape)
		{
			System.out.println("RenderShape");
			return;

		}
		else if (object instanceof Group)
		{
			if (object instanceof TransformGroup)
			{
				System.out.println("TransformGroup");
			}
			else
			{
				System.out.println("Group");
			}

			for (int i = 0; i < ((Group)object).children.size(); i++)
			{
				dumpScene(((Group)object).children.get(i));
			}

		}
		else
		{
			System.out.println(
				"Error: dumpScene. should not reach this condition");
		}

	}

	/* Render while traversing graph and pushing transforms
	
			public void renderGraph(Node node, GL gl, boolean textured, int mode)
		{
			if (node instanceof Shape3D)
			{
				Shape3D shape = (Shape3D)node;
				if (once)
				{
					System.out.println("Rendering Shape " + node);
					Object object = shape.getUserData();
					System.out.println("Rendering Shape data " + object);
				}
				Object userObject = shape.getUserData();
				if ( userObject != null && userObject instanceof RenderShape )
				{
					RenderShape renderShape = ((RenderShape)userObject);
					switch (mode)
					{
						case 0: //original
							if ( textured )
							{
								int textureCount = 0;
								if (renderShape.myTexture != null)
								{
									textureCount = renderShape.myTexture.length;
									configCombinersForHardwareShadowPass(textureCount);
								}
							}
							renderShape.render(gl,textured);
							break;
						case 1: // opaque only
							if ( !renderShape.hasAlpha )
							{
							if ( textured )
							{
								int textureCount = 0;
								if (renderShape.myTexture != null)
								{
									textureCount = renderShape.myTexture.length;
									configCombinersForHardwareShadowPass(textureCount);
								}
	
							}
							renderShape.render(gl,textured);
							}
							break;
						case 2: // transparent only
							if ( renderShape.hasAlpha )
							{
								//System.out.println("Alpha shape....");
							if ( textured )
							{
								int textureCount = 0;
								if (renderShape.myTexture != null)
									textureCount = renderShape.myTexture.length;
								configCombinersForHardwareShadowPass(textureCount);
							}
							renderShape.render(gl,textured);
							}
							break;
						case 3: // transparent only
							if ( renderShape.hasAlpha )
							{
								//System.out.println("Alpha shape....");
							if ( textured )
							{
								int textureCount = 0;
								if (renderShape.myTexture != null)
									textureCount = renderShape.myTexture.length;
								//configCombinersForHardwareShadowPass(textureCount);
							}
	
							renderShape.render(gl,textured);
							}
							break;
						default:
							if ( textured )
							{
							int textureCount = 0;
							if (renderShape.myTexture != null)
								textureCount = renderShape.myTexture.length;
							configCombinersForHardwareShadowPass(textureCount);
							}
							renderShape.render(gl,textured);
							break;
					}
				}		
			}
			if (node instanceof TransformGroup)
			{
				if (once)
					System.out.println("Pushing transform ");
				TransformGroup tg = (TransformGroup)node;
				Transform3D t3d = new Transform3D();
				float[] mat = new float[16];
				tg.getTransform(t3d);
				t3d.transpose();
				t3d.get(mat);
				gl.glPushMatrix();
	
				gl.glMultMatrixf(mat);
				Group group = (Group)node;
				for (Enumeration e = group.getAllChildren(); e.hasMoreElements();)
				{
					renderGraph((Node)e.nextElement(), gl,textured, mode);
				}
				gl.glPopMatrix();
				if (once)
					System.out.println("Poping transform ");
	
			}
			else if (node instanceof Group)
			{
				Group group = (Group)node;
				for (Enumeration e = group.getAllChildren(); e.hasMoreElements();)
				{
					renderGraph((Node)e.nextElement(), gl,textured, mode);
				}
			}
		}
	*/
}
