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

import java.util.*;
import net.java.games.jogl.*;


public class RendererUtils
{
	static float[] mat = new float[16];

	static public void computeDrawListTransparentZDepth(
		Scene scene,
		ArrayList drawListTransparent)
	{
		for (int i = 0; i < drawListTransparent.size(); i++)
		{
			float x,y,z;
			RenderShape renderShape = (RenderShape)drawListTransparent.get(i);
			if (renderShape.myMatrix != null)
			{
				VectorMatrixUtils.multMatrices(
					mat,
					scene.sceneMatrix,
					renderShape.myMatrix);
				x = mat[12] + renderShape.x;
				y = mat[13] + renderShape.y;
				z = mat[14] + renderShape.z;
			}
			else
			{
				//System.out.println("Empty mat");
				VectorMatrixUtils.copyMatrix(mat, scene.sceneMatrix);
				x = renderShape.x;
				y = renderShape.y;
				z = renderShape.z;			
			}
			//VecMatUtils.invert(scene.getCamera().inverseEyeViewMatrix, scene.getCamera().eyeViewMatrix);
			renderShape.cameraZ =
				VectorMatrixUtils.transformZ(
					scene.getCamera().eyeViewMatrix,
					x,
					y,
					z);
			//System.out.println( renderShape.cameraZ );
		}
	}

	static public void unbindArrays(ArrayList drawList, GL gl)
	{
		for (int i = 0; i < drawList.size(); i++)
		{
			RenderShape renderShape = (RenderShape)drawList.get(i);
			unbindArrays(renderShape, gl);
		}
	}
	
	static public void unbindArrays(RenderBinNode node, GL gl)
	{
		Object object = node.getBin();
		if ( object instanceof RenderShape)
		{
			unbindArrays((RenderShape)object, gl);
		}
		else if ( object instanceof ArrayList)
		{
			ArrayList list = (ArrayList)object;
			for( int i = 0; i<list.size(); i++)
			{
				Object object2 = list.get(i);
				//if ( object2 instanceof RenderBinNode)
				{
					unbindArrays((RenderBinNode)object2,gl);
				}
			}
		}
	}
	

	static public void unbindArrays(RenderShape shape, GL gl)
	{
		VertexArraySet shapeVAS = shape.vertexArraySet;
		if (shape == null)
			return;
		if (shapeVAS.bound == false)
			return;
		int[] bufferID = new int[1];
		if (shapeVAS.staticGeometry)
		{
			if (shapeVAS.vertexArrayID > 0)
			{
				bufferID[0] = shapeVAS.vertexArrayID;
				//System.out.println("unbinding " + shape + " ID " + bufferID[0]);
				gl.glDeleteBuffersARB(1, bufferID);
			}
			if (shapeVAS.normalArrayID > 0)
			{
				bufferID[0] = shapeVAS.normalArrayID;
				gl.glDeleteBuffersARB(1, bufferID);
			}
			if (shapeVAS.colorArrayID > 0)
			{
				bufferID[0] = shapeVAS.colorArrayID;
				gl.glDeleteBuffersARB(1, bufferID);
			}
			if (shapeVAS.textureCoordArrayID != null)
			{
				if (shapeVAS.textureCoordArrayID[0] > 0)
				{
					bufferID[0] = shapeVAS.textureCoordArrayID[0];
					gl.glDeleteBuffersARB(1, bufferID);
				}
				if (shapeVAS.textureCoordArrayID.length > 1)
					if (shapeVAS.textureCoordArrayID[1] > 0)
					{
						bufferID[0] = shapeVAS.textureCoordArrayID[1];
						gl.glDeleteBuffersARB(1, bufferID);
					}
			}
		}
		shapeVAS.bound = false;
	}

	static public void bindAppearance(RenderShape shape, GL gl, GLU glu)
	{
		if (shape.appearance == null || shape.appearance.bound) return;
		//System.out.println("bindAppearance " + shape);
	
		if (shape.appearance.textureStageState != null)
		{
			try
			{
			for (int i = 0; i < shape.appearance.textureStageState.length; i++)
			{
				if (shape.appearance.textureStageState[i].texture != null)
				if (shape.appearance.textureStageState[i].texture.textureID == -1)
					TextureFactory.getFactory(gl).bindTexture(
						shape.appearance.textureStageState[i].texture, glu);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		shape.appearance.bound = true;
	}
	
	static public void bindArrays(List drawList, GL gl)
	{
		for (int i = 0; i < drawList.size(); i++)
		{
			RenderShape renderShape = (RenderShape)drawList.get(i);
			if ( !renderShape.vertexArraySet.bound )
			bindArrays(renderShape, gl);
		}
	}

	static public void bindArrays(RenderShape shape, GL gl)
	{
		if (shape.vertexArraySet.bound) return;
		//gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		// bind texture images
		//if (true) return;
		VertexArraySet shapeVAS = shape.vertexArraySet;
		//System.out.println("bindArrays shapeVAS " + shapeVAS);
		int[] bufferID = new int[1];
		//if (shape.faceIndicesStatic)
        //if (true)
        //if (false)//( !gl.isExtensionAvailable("GL_ARB_vertex_buffer_object"))
        if ( !BasicRenderer.supportedVBO )
        {
        	System.out.println("Using Display List for object " + shapeVAS);
            shapeVAS.faceIndicesStatic = false;
            shapeVAS.bound = true;
            return;
        }
		//shapeVAS.staticGeometry = true;
		if ( !shapeVAS.staticGeometry )
		{
			System.out.println("Using Vertex Array for DYNAMIC object " + shapeVAS);
			shapeVAS.faceIndicesStatic = false;
			shapeVAS.bound = true;
//			gl.glGenBuffersARB(1, bufferID);
//			shapeVAS.faceIndicesID = bufferID[0];
//			gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, bufferID[0]);
			//System.out.println(
			//	"Binding " + shapeVAS + " ID " + bufferID[0]);
			//System.out.println("Binding " + shape.faceIndices + " shape.faceIndices " + shape.faceIndices.capacity()/3);
//			gl.glBufferDataARB(
//				GL.GL_ELEMENT_ARRAY_BUFFER_ARB,
//				shapeVAS.faceIndices.capacity() * 4,
//				shapeVAS.faceIndices,
//				GL.GL_STATIC_DRAW_ARB);
			return;
		}
 		{
			System.out.println("Using Vertex Buffer Object for object " + shapeVAS);
			//if (!shapeVAS.faceIndicesStatic)
			shapeVAS.faceIndicesStatic = true;
			if (shapeVAS.faceIndicesID == -1)
			{
				gl.glGenBuffersARB(1, bufferID);
				shapeVAS.faceIndicesID = bufferID[0];
				gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, bufferID[0]);
				//System.out.println(
				//	"Binding " + shapeVAS + " ID " + bufferID[0]);
				//System.out.println("Binding " + shape.faceIndices + " shape.faceIndices " + shape.faceIndices.capacity()/3);
				gl.glBufferDataARB(
					GL.GL_ELEMENT_ARRAY_BUFFER_ARB,
					shapeVAS.faceIndices.capacity() * 4,
					shapeVAS.faceIndices,
					GL.GL_STATIC_DRAW_ARB);
			}
		}
		if (shapeVAS.colorArrayID == -1)
			if (shapeVAS.colorArray != null) //&& shape.colorArrayStatic)
			{
				gl.glGenBuffersARB(1, bufferID);
				shapeVAS.colorArrayID = bufferID[0];
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferID[0]);
				gl.glBufferDataARB(
					GL.GL_ARRAY_BUFFER_ARB,
					shapeVAS.colorArray.capacity() * 4,
					shapeVAS.colorArray,
					GL.GL_STATIC_DRAW_ARB);
				//upload data
			}
		if (shapeVAS.textureCoordArray != null)
			for (int i = 0; i < shapeVAS.textureCoordArray.length; i++)
			{
				//if (shapeVAS.textureCoordArrayID[i] == -1)
				if (shapeVAS.textureCoordArray[i] != null)
					// && shape.textureCoordArrayStatic)
				{
					//System.out.println("Binding texCoords " + i);
					gl.glGenBuffersARB(1, bufferID);
					shapeVAS.textureCoordArrayID[i] = bufferID[0];
					gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferID[0]);
					gl.glBufferDataARB(
						GL.GL_ARRAY_BUFFER_ARB,
						shapeVAS.textureCoordArray[i].capacity() * 4,
						shapeVAS.textureCoordArray[i],
						GL.GL_STATIC_DRAW_ARB);
				}

			}
		//if (shape.staticGeometry)
		{
			if (shapeVAS.vertexArrayID == -1)
			{
				//System.out.println("Binding...");
				gl.glGenBuffersARB(1, bufferID);
				shapeVAS.vertexArrayID = bufferID[0];
				//System.out.println("Binding " + shape + " ID " + bufferID[0]);
				//System.out.println("Binding " + shape.vertexArrayID + " shape.vertexArrayID");
				gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferID[0]);
				gl.glBufferDataARB(
					GL.GL_ARRAY_BUFFER_ARB,
					shapeVAS.vertexArray.capacity() * 4,
					shapeVAS.vertexArray,
					GL.GL_STATIC_DRAW_ARB);
				//upload data
			}

			//gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
			if (shapeVAS.normalArrayID == -1)
				if (shapeVAS.normalArray != null)
				{
					gl.glGenBuffersARB(1, bufferID);
					shapeVAS.normalArrayID = bufferID[0];
					gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, bufferID[0]);
					gl.glBufferDataARB(
						GL.GL_ARRAY_BUFFER_ARB,
						shapeVAS.normalArray.capacity() * 4,
						shapeVAS.normalArray,
						GL.GL_STATIC_DRAW_ARB);
					//upload data
				}

			//upload data
			//	gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, buffer[0]);
			//	gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, 15*4*4, thing, GL.GL_STATIC_DRAW_ARB);
		}
		shapeVAS.bound = true;
	}

	static public void generateRenderBins(
		RenderBinNode drawListOpaque,
		ArrayList drawListTransparent,
		ArrayList list)
	{
		//drawListOpaque.clear();
		//drawListTransparent.clear();
		for (int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			if (obj instanceof RenderShape)
			{
				RenderShape renderShape = ((RenderShape)obj);
				if (!renderShape.inView)
				{
					if (renderShape.appearance.hasAlpha)
						drawListTransparent.add(renderShape);
					else
					{
						insert(drawListOpaque, renderShape);
					}
					renderShape.inView = true;
					//System.out.println("adding shape to renderBin");
				}
			}
		}
	}
	static public void insertSimple(RenderBinNode node, RenderShape shape)
	{
		BasicRenderer.setRenderStateMask(shape.appearance);
		Object object = node.getBin();
		if (object == null)
		{
			object = new ArrayList();
			node.setBin(object);
		}
		RenderBinNode subBin = new RenderBinNode();
		subBin.index = shape.appearance.textureCount;
		subBin.setBin(shape);

		((ArrayList)object).add(subBin);
		//System.out.println("Creating new renderbinnode");
	}

	static public void insert(RenderBinNode node, RenderShape shape)
	{
		BasicRenderer.setRenderStateMask(shape.appearance);
		Object object = node.getBin();
		if (object == null)
		{
			object = new ArrayList();
			node.setBin(object);

			RenderBinNode insertBin = new RenderBinNode();
			insertBin.index = shape.appearance.stateBitSet;
			insertBin.setBin(new ArrayList());

			RenderBinNode subBin = new RenderBinNode();
			subBin.index = shape.appearance.textureCount;
			subBin.setBin(shape);

			((ArrayList)object).add(insertBin);
			((ArrayList)insertBin.getBin()).add(subBin);

			//System.out.println("Creating new renderbinnode");
		}
		else
		{
			ArrayList list = (ArrayList)object;
			ArrayList list2 = null;
			for (int i = 0; i < list.size(); i++)
			{
				RenderBinNode bin2 = (RenderBinNode)list.get(i);
				if (bin2.index == shape.appearance.stateBitSet)
				{
					list2 = (ArrayList)bin2.getBin();
					Object objectFinal = null;
					for (int j = 0; j < list2.size(); j++)
					{
						RenderBinNode bin3 = (RenderBinNode)list2.get(j);
						if (bin3.index == shape.appearance.textureCount)
						{
							objectFinal = bin3.getBin();
							if (objectFinal instanceof RenderShape)
							{
								ArrayList tmpList = new ArrayList();
								RenderBinNode tmpBin1 = new RenderBinNode();
								RenderBinNode tmpBin2 = new RenderBinNode();
								tmpBin1.setBin(objectFinal);
								tmpBin2.setBin(shape);
								tmpList.add(tmpBin1);
								tmpList.add(tmpBin2);
								bin3.setBin(tmpList);
							}
							else if (objectFinal instanceof ArrayList)
							{
								RenderBinNode tmpBin = new RenderBinNode();
								tmpBin.setBin(shape);				
								((ArrayList)objectFinal).add(tmpBin);
							}
							//System.out.println("Found textureState bin");
						}
					}
					if (objectFinal == null)
					{
						RenderBinNode subBin = new RenderBinNode();
						subBin.index = shape.appearance.textureCount;
						subBin.setBin(shape);

						list2.add(subBin);

						//System.out.println("Creating new renderbinnode");
					}
				}
			}
			if (list2 == null)
			{
				RenderBinNode insertBin = new RenderBinNode();
				insertBin.index = shape.appearance.stateBitSet;
				insertBin.setBin(new ArrayList());

				RenderBinNode subBin = new RenderBinNode();
				subBin.index = shape.appearance.textureCount;
				subBin.setBin(shape);

				((ArrayList)object).add(insertBin);
				((ArrayList)insertBin.getBin()).add(subBin);

				//System.out.println("Creating new renderbinnode");
			}
		}
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
