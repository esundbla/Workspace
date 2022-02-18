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
package com.imi.j3d.util;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.util.*;
import net.java.games.jogl.*;
import com.imi.jist3d.render.*;
import java.nio.*;

/**
 * @author Shawn Kendall
 *
 */
public class ConvertUtils
{
	final static HashMap sharedJava3DNodeTable = new HashMap();
	final static String prefix = "|- ";
	/*
	static public void setRenderShapes(ArrayList list, GL gl, TransformGroup tg)
	{
		for( int i = 0; i<list.size(); i++)
		{
			Object obj = list.get(i);
			if (obj instanceof Node)
				setRenderShapes( (Node)obj, gl, tg );
		}
	}
	*/
	static public com.imi.jist3d.scenegraph.Node makeJist3DGraphFromJava3DGraph(javax.media.j3d.Node nodeJ3d)
	{
		System.out.println("=======================================");
		System.out.println("Converting Java3D Scene Graph to Jist3D objects...");
		com.imi.jist3d.scenegraph.Node node = makeJist3DGraphFromJava3DGraphInternal( nodeJ3d );
		System.out.println("Conversion finished");
		System.out.println("=======================================");
		return node;
	}

	static public com.imi.jist3d.scenegraph.Node makeJist3DGraphFromJava3DGraphInternal(javax.media.j3d.Node nodeJ3d)
	{
		com.imi.jist3d.scenegraph.Node nodeJist3d = null;

		if (nodeJ3d instanceof javax.media.j3d.TransformGroup)
		{
			com.imi.jist3d.scenegraph.TransformGroup nodeTG = new com.imi.jist3d.scenegraph.TransformGroup();
			javax.media.j3d.TransformGroup tg = (javax.media.j3d.TransformGroup)nodeJ3d;
			Transform3D t3d = new Transform3D();
			tg.getTransform(t3d);
			t3d.transpose();
			t3d.get(nodeTG.matLocal);

			nodeJist3d = nodeTG;
			nodeJ3d.setUserData(nodeTG);
			//System.out.println("Found transformgroup node " + nodeJ3d);
		}
		else if (nodeJ3d instanceof javax.media.j3d.Group)
		{
			nodeJist3d = new com.imi.jist3d.scenegraph.Group();
			//System.out.println("Found group node " + nodeJ3d);
		}
		else
		{
			System.out.println(prefix + "Ignoring node " + nodeJ3d);
		}

		if (nodeJist3d != null)
		{
			com.imi.jist3d.scenegraph.Group nodeG = (com.imi.jist3d.scenegraph.Group)nodeJist3d;
			//System.out.println("Walking Graph for " + nodeJ3d);
			nodeG.setName(nodeJ3d.toString());
			javax.media.j3d.Group groupJ3d = (javax.media.j3d.Group)nodeJ3d;
			//System.out.println("Walking Graph for " + groupJ3d);
			for (Enumeration e = groupJ3d.getAllChildren(); e.hasMoreElements();)
			{
				Object node = e.nextElement();
				if (node instanceof Shape3D)
					nodeG.getChildren().add(makeRenderShapeForShape3D((Shape3D)node));
				else
					nodeG.getChildren().add(makeJist3DGraphFromJava3DGraphInternal((javax.media.j3d.Node)node));
			}
			nodeJist3d = nodeG;
		}
		return nodeJist3d;
	}

	static public RenderShape makeRenderShapeForShape3D(Shape3D shape)
	{
		//System.out.println("Making RenderShape for " + shape);
		RenderShape rShape = null;
		Geometry geom = shape.getGeometry();
		if (geom instanceof IndexedTriangleArray)
		{
			rShape = new RenderShape();
			// OpenGL defaults
			rShape.appearance.cullFace = GL.GL_BACK;

			Object userData = shape.getUserData();
			if (userData != null && userData instanceof String)
			{
				rShape.vertexArraySet.staticGeometry = false;
				rShape.vertexArraySet.faceIndicesStatic = false;
				rShape.vertexArraySet.colorArrayStatic = false;
				rShape.vertexArraySet.textureCoordArrayStatic = false;
				System.out.println(prefix + "FOUND DYNAMIC GEOMETRY");
			}
			//shape.setUserData(rShape);
			Bounds bounds;
			try
			{
				bounds = shape.getBounds();
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				bounds = new BoundingSphere();
			}
			if (bounds instanceof BoundingSphere)
			{
				//System.out.println("BoundingSphere-----------------------");
				Point3d center = new Point3d();
				((BoundingSphere)bounds).getCenter(center);
				rShape.x = (float)center.x;
				rShape.y = (float)center.y;
				rShape.z = (float)center.z;
			}
			else if (bounds instanceof BoundingBox)
			{
				Point3d upper = new Point3d();
				Point3d lower = new Point3d();
				((BoundingBox)bounds).getUpper(upper);
				((BoundingBox)bounds).getLower(lower);
				rShape.x = (float) (upper.x + lower.x / 2.0f);
				rShape.y = (float) (upper.y + lower.y / 2.0f);
				rShape.z = (float) (upper.z + lower.z / 2.0f);
				//System.out.println("BoundingBox-----------------------" + upper);
			}

			IndexedTriangleArray geometry = ((IndexedTriangleArray)shape.getGeometry());
			rShape.vertexArraySet.vertexArray = (FloatBuffer)geometry.getCoordRefBuffer().getBuffer();

			int formatMask = geometry.getVertexFormat();
			if ((formatMask & geometry.COLOR_3) == geometry.COLOR_3)
			{
				rShape.vertexArraySet.colorArray = (FloatBuffer)geometry.getColorRefBuffer().getBuffer();
			}
			if ((formatMask & geometry.NORMALS) == geometry.NORMALS)
			{
				rShape.vertexArraySet.normalArray = (FloatBuffer)geometry.getNormalRefBuffer().getBuffer();
			}
			if ((formatMask & geometry.TEXTURE_COORDINATE_2) == geometry.TEXTURE_COORDINATE_2)
			{
				rShape.vertexArraySet.textureCoordArray = new FloatBuffer[geometry.getTexCoordSetCount()];
				rShape.vertexArraySet.textureCoordArrayID = new int[geometry.getTexCoordSetCount()];
				//rShape.bufferT = new int[geometry.getTexCoordSetCount()];
				for (int i = 0; i < geometry.getTexCoordSetCount(); i++)
				{
					rShape.vertexArraySet.textureCoordArray[i] = (FloatBuffer)geometry.getTexCoordRefBuffer(i).getBuffer();
					rShape.vertexArraySet.textureCoordArrayID[i] = -1;
				}
			}
			/*
			 float[] texCoords = geometry.getTexCoordRefFloat(0);
			 float[] normalsF = geometry.getNormalRefFloat();
			
			 float[] coordsF = geometry.getCoordRefFloat();
			
			 float[] texCoordsNew = new float[texCoords.length];
			
			vertexArray = (ByteBuffer.allocateDirect(coordsF.length*4)).order(ByteOrder.nativeOrder()).asFloatBuffer(); 
			normalArray = (ByteBuffer.allocateDirect(normalsF.length*4)).order(ByteOrder.nativeOrder()).asFloatBuffer(); 
			 //System.out.println("coordsF.length " + coordsF.length);
			 //System.out.println("normalsF.length " + normalsF.length);
			 for (int i=0;i<coordsF.length;i++)
			 {
				vertexArray.put(coordsF[i]);
				normalArray.put(normalsF[i]);
				 //System.out.println("Made vertex and normal " + normalsF[i] + "  " + i);
			 }
			*/
			int[] polys = new int[geometry.getIndexCount()];
			geometry.getCoordinateIndices(0, polys);
			rShape.vertexArraySet.faceIndices = (ByteBuffer.allocateDirect(geometry.getIndexCount() * 4)).order(ByteOrder.nativeOrder()).asIntBuffer();
			//System.out.println("polys.length " + polys.length);
			//for (int i = 0; i < polys.length; i++)
			//{
			rShape.vertexArraySet.faceIndices.put(polys);
			//System.out.println("Made face index " + polys[i] + " " + i );
			//}

			/*
			for (int i=0;i<texCoords.length;i++)
			{
				 texCoordsNew[i] = texCoords[i];
			}
			int[] texCoordSetMap = new int[2];
			texCoordSetMap[0] = 0;
			texCoordSetMap[1] = 1;
			*/

			// Convert and bind up J3D textures
			javax.media.j3d.Appearance appearance = shape.getAppearance();
			com.imi.jist3d.render.Appearance tmpApp = (com.imi.jist3d.render.Appearance)sharedJava3DNodeTable.get(appearance);
			if (tmpApp != null)
			{
				System.out.println(prefix + "Sharing Shape appearance " + tmpApp);
				rShape.appearance = tmpApp;
			}
			else // make new Appearance
				{

				//System.out.println("Shape appearance " + appearance);
				//RenderingAttributes ra = appearance.getRenderingAttributes();
				ColoringAttributes ca = appearance.getColoringAttributes();
				if (ca != null)
				{
					if (ca.getShadeModel() == ca.SHADE_FLAT)
						rShape.appearance.setShadeModel(GL.GL_FLAT);
					else if (ca.getShadeModel() == ca.SHADE_GOURAUD)
						rShape.appearance.setShadeModel(GL.GL_SMOOTH);
					Color3f color = new Color3f();
					ca.getColor(color);
					rShape.appearance.color[0] = color.x;
					rShape.appearance.color[1] = color.y;
					rShape.appearance.color[2] = color.z;
				}
				PolygonAttributes pa = appearance.getPolygonAttributes();
				if (pa != null)
				{
					if (pa.CULL_BACK == pa.getCullFace())
					{
						rShape.appearance.cullFace = GL.GL_BACK;
						rShape.appearance.CULL = true;
					}
					else if (pa.CULL_FRONT == pa.getCullFace())
					{
						rShape.appearance.cullFace = GL.GL_FRONT;
						rShape.appearance.CULL = true;
					}
					else if (pa.CULL_NONE == pa.getCullFace())
					{
						//CULL_FACE = GL.GL_FRONT_AND_BACK;
						rShape.appearance.CULL = false;
						//System.out.println(prefix + "No culling for shape");
					}
					if (pa.getPolygonMode() == pa.POLYGON_FILL)
					{
						rShape.appearance.polygonMode = GL.GL_FILL;
					}
					else if (pa.getPolygonMode() == pa.POLYGON_LINE)
					{
						System.out.println(prefix + "LINE MODE********************************");
						rShape.appearance.polygonMode = GL.GL_LINE;
					}
				}

				if (appearance != null)
				{
					javax.media.j3d.Texture texture = null;
					int textureUnitCount = appearance.getTextureUnitCount();
					//if ( false)
					if (textureUnitCount > 1)
					{
						//rShape.myTexture = new String[textureUnitCount];
						TextureUnitState[] tus = appearance.getTextureUnitState();
						rShape.appearance.textureStageState = (TextureStageState[])sharedJava3DNodeTable.get(tus);
						if (rShape.appearance.textureStageState != null)
						{
							System.out.println(prefix + "Sharing Shape textureStageState " + rShape.appearance.textureStageState);
						}
						else
						{
							rShape.appearance.textureStageState = new TextureStageState[textureUnitCount];
							sharedJava3DNodeTable.put(tus, rShape.appearance.textureStageState);

							for (int i = 0; i < tus.length; i++)
							{
								rShape.appearance.textureStageState[i] = new TextureStageState();
								if (tus[i] != null && tus[i].getTexCoordGeneration() != null)
								{
									rShape.appearance.textureStageState[i].texCoordGeneration = new com.imi.jist3d.render.TexCoordGeneration();
								}
								com.imi.jist3d.render.Texture tex = null;
								if (tus[i] != null)
								{
									texture = tus[i].getTexture();
									try
									{
										tex = (com.imi.jist3d.render.Texture)sharedJava3DNodeTable.get(texture);
										if (tex == null)
										{
											tex = TextureFactory.createTextureUnbound(texture.toString(), texture);
											sharedJava3DNodeTable.put(texture, tex);
										}
										rShape.appearance.hasAlpha = tex.hasAlpha ? true : rShape.appearance.hasAlpha;
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
									//rShape.myTexture[i] = texture.toString();
									rShape.appearance.textureStageState[i].texture = tex;
								}
							}
							// "blocks" second texture layer from demo
							// which is the static shadow map.
							//String tmp = rShape.myTexture[0];
							//rShape.myTexture = new String[1];
							//rShape.myTexture[0] = tmp;
						}
					}
					else
					{
						texture = appearance.getTexture();
						if (texture != null)
						{
							com.imi.jist3d.render.Texture tex = null;
							try
							{
								tex = (com.imi.jist3d.render.Texture)sharedJava3DNodeTable.get(texture);
								if (tex == null)
								{
									tex = TextureFactory.createTextureUnbound(texture.toString(), texture);
									sharedJava3DNodeTable.put(texture, tex);
								}
								rShape.appearance.hasAlpha = tex.hasAlpha ? true : rShape.appearance.hasAlpha;
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							//rShape.myTexture = new String[1];
							//rShape.myTexture[0] = texture.toString();
							rShape.appearance.textureStageState = (TextureStageState[])sharedJava3DNodeTable.get(texture.toString());
							if (rShape.appearance.textureStageState != null)
							{
								System.out.println(prefix + "Sharing Shape textureStageState " + rShape.appearance.textureStageState);
							}
							else
							{
								rShape.appearance.textureStageState = new TextureStageState[1];
								sharedJava3DNodeTable.put(texture.toString(), rShape.appearance.textureStageState);

								rShape.appearance.textureStageState[0] = new TextureStageState();
								rShape.appearance.textureStageState[0].texture = tex;
							}
						}
					}

					if (texture == null && rShape.vertexArraySet.textureCoordArray != null)
						System.out.println(prefix + "No shape texture !!!!");

					javax.media.j3d.Material mat = appearance.getMaterial();
					if (mat != null)
					{
						rShape.appearance.material = getMaterial(mat);
					}
				}
				else
				{
					System.out.println(prefix + "No shape appearance!");
				}

				//RendererUtils.bindArrays(rShape, gl);
				sharedJava3DNodeTable.put(appearance, rShape.appearance);
			}

		}
		else
		{
			System.out.println(prefix + "Ignoring shape not indexed geometry " + shape);
		}
		return rShape;
	}

	static public com.imi.jist3d.render.Material getMaterial(javax.media.j3d.Material mat)
	{
		Color3f color = new Color3f();
		com.imi.jist3d.render.Material matJist = new com.imi.jist3d.render.Material();
		mat.getAmbientColor(color);
		matJist.ambientColor[0] = color.x;
		matJist.ambientColor[1] = color.y;
		matJist.ambientColor[2] = color.z;
		mat.getDiffuseColor(color);
		matJist.diffuseColor[0] = color.x;
		matJist.diffuseColor[1] = color.y;
		matJist.diffuseColor[2] = color.z;
		mat.getSpecularColor(color);
		matJist.specularColor[0] = color.x;
		matJist.specularColor[1] = color.y;
		matJist.specularColor[2] = color.z;
		mat.getEmissiveColor(color);
		matJist.emissiveColor[0] = color.x;
		matJist.emissiveColor[1] = color.y;
		matJist.emissiveColor[2] = color.z;
		matJist.shininess = mat.getShininess();
		matJist.colorTarget = mat.getColorTarget();
		matJist.lightingEnable = mat.getLightingEnable();
		return matJist;
	}
	/*
	static public Shape3D getShape3D(Node node)
	{
		if (node instanceof Group)
		{
			// totally ass slow way
			Group group = (Group)node;
			final int n = group.numChildren();
			 for (int i = 0; i < n; i++)
			 {
				node = group.getChild(i);
				if ( node instanceof Shape3D )
				{
					System.out.println("Found shape3d!!!");
					return (Shape3D)node;
				}
				getShape3D( node );
			 }
			return null;
		}
		return null;
	}
	*/
}
