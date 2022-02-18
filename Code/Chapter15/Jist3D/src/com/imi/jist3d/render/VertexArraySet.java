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

import java.nio.*;

/**
 * @author Shawn Kendall
 *
 */
public class VertexArraySet implements Cloneable
{
	int displayListID = -1;

	public FloatBuffer vertexArray = null;
	public IntBuffer faceIndices = null;
	public FloatBuffer colorArray = null;
	public FloatBuffer normalArray = null;
	public FloatBuffer[] textureCoordArray = null;

	public boolean bound = false;
	int vertexArrayID = -1;
	int faceIndicesID = -1;
	int colorArrayID = -1;
	int normalArrayID = -1;
	public int[] textureCoordArrayID = null;

	public boolean faceIndicesStatic = true;
	public boolean textureCoordArrayStatic = true;
	public boolean colorArrayStatic = true;
	public boolean staticGeometry = true;

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch ( Exception e)
		{
			return null;
		}
	}

	/**
	 * @return
	 */
	public int getTextureCoordArrayID(int index)
	{
		return textureCoordArrayID[index];
	}

	public void setTextureCoordArrayID(int index, int id)
	{
		textureCoordArrayID[index] = id;
	}

	/**
	 * @param is
	 */
	public void setTextureCoordArrayID(int[] is)
	{
		textureCoordArrayID = is;
	}

}
