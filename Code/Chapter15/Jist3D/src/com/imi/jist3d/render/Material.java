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

/**
 * @author Shawn Kendall
 *
 */
public class Material
{
	final static int AMBIENT = 1;
	final static int AMBIENT_AND_DIFFUSE = 2;
	final static int DIFFUSE = 3;
	final static int EMISSIVE = 4;
	final static int SPECULAR = 5;

	public float[] ambientColor = { 0.0f, 0.0f, 0.0f, 1.0f };
	public float[] emissiveColor = { 0.0f, 0.0f, 0.0f, 1.0f };
	public float[] diffuseColor = { 1.0f, 1.0f, 1.0f, 1.0f };
	public float[] specularColor = { 1.0f, 1.0f, 1.0f, 1.0f };
	public float shininess;
	public int colorTarget;
	public boolean lightingEnable = true;

	static final int AMBIENT_COLOR_CHANGED = 1;
	static final int EMISSIVE_COLOR_CHANGED = 2;
	static final int DIFFUSE_COLOR_CHANGED = 4;
	static final int SPECULAR_COLOR_CHANGED = 8;
	static final int SHININESS_CHANGED = 16;
	static final int ENABLE_CHANGED = 32;
	static final int COLORTARGET_CHANGED = 64;
	
	/**
	 * @return
	 */
	public boolean isLightingEnable()
	{
		return lightingEnable;
	}

	/**
	 * @param b
	 */
	public void setLightingEnable(boolean b)
	{
		lightingEnable = b;
	}

	/**
	 * @return
	 */
	public int getColorTarget()
	{
		return colorTarget;
	}

	/**
	 * @param i
	 */
	public void setColorTarget(int i)
	{
		colorTarget = i;
	}

	/**
	 * @return
	 */
	public float getShininess()
	{
		return shininess;
	}

	/**
	 * @param f
	 */
	public void setShininess(float f)
	{
		shininess = f;
	}

}
