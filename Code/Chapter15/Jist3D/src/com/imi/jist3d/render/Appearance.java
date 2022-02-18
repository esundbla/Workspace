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
import net.java.games.jogl.*;

/**
 * @author Shawn Kendall
 *
 */
public class Appearance implements Cloneable
{
	// For state ordering
	public static final int lightMask = 1 << 30;
	public static final int shadeModelMask = 1 << 29;
	public static final int textureMask = 1 << 28;
	public static final int materialMask = 1 << 27;
	public static final int cullMask = 1 << 26;
	public static final int colorArrayMask = 1 << 25;
	int stateBitSet = 0;
	int textureCount = 0;

	// For Coloring Attributes
	public static final int SHADE_FLAT = GL.GL_FLAT;
	public static final int SHADE_SMOOTH = GL.GL_SMOOTH;

	// For Line Attributes
	public static final int PATTERN_SOLID = 0;
	public static final int PATTERN_DASH = 1;
	public static final int PATTERN_DOT = 2;
	public static final int PATTERN_DASH_DOT = 3;
	public static final int PATTERN_USER_DEFINED = 4;

	// For Polygon Attributes
	public static final int POLYGON_POINT = 0;
	public static final int POLYGON_LINE = 1;
	public static final int POLYGON_FILL = 2;
	public static final int CULL_NONE = 0;
	public static final int CULL_BACK = 1;
	public static final int CULL_FRONT = 2;

	// For Rendering Attributes
	public static final int ALWAYS = 0;
	public static final int NEVER = 1;
	public static final int EQUAL = 2;
	public static final int NOT_EQUAL = 3;
	public static final int LESS = 4;
	public static final int LESS_OR_EQUAL = 5;
	public static final int GREATER = 6;
	public static final int GREATER_OR_EQUAL = 7;
	public static final int ROP_COPY = 3;
	public static final int ROP_XOR = 6;

	// From ColoringAttributes in Java3D
	int shadeModel = SHADE_SMOOTH;
	public float[] color = { 1.0f, 1.0f, 1.0f, 1.0f };

	// From LineAttributes in Java3D
	float lineWidth;
	int linePattern;
	boolean lineAntialiasing;
	int linePatternMask;
	int linePatternScaleFactor;

	// From PointAttributes in Java3D
	float pointSize;
	boolean pointAntialiasing;

	// From PolygonAttributes in Java3D
	public boolean CULL = true;
	public int polygonMode = GL.GL_FILL;
	public int cullFace = -1;
	public boolean backFaceNormalFlip;
	float polygonOffset;
	float polygonOffsetFactor;

	// From Rendering Attributes in Java3D
	boolean depthBufferEnable = true;
	boolean depthBufferWriteEnable = true;
	float alphaTestValue = 0.0f;
	int alphaTestFunction = 0;
	boolean visible = true;
	public boolean ignoreVertexColors = false;
	boolean rasterOpEnable = false;
	//int rasterOp;

	public boolean textureEnable = true;
	public boolean hasAlpha = false;

	public Material material = null;
	public TextureStageState textureStageState[] = null;
	public boolean bound = false;

	/**
		* @return
		*/
	public int getShadeModel()
	{
		return shadeModel;
	}

	/**
		* @param i
		*/
	public void setShadeModel(int i)
	{
		shadeModel = i;
	}

}
