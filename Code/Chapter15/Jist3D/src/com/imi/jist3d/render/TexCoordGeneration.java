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
public class TexCoordGeneration
{
	public static final int OBJECT_LINEAR = 0;
	public static final int EYE_LINEAR = 1;
	public static final int SPHERE_MAP = 2;
	public static final int NORMAL_MAP = 3;
	public static final int REFLECTION_MAP = 4;
	public static final int TEXTURE_COORDINATE_2 = 0;
	public static final int TEXTURE_COORDINATE_3 = 1;
	public static final int TEXTURE_COORDINATE_4 = 2;

	int genMode;
	int format;
	float[] planeS;
	float[] planeT;
	float[] planeR;
	float[] planeQ;
	boolean enable;

	public TexCoordGeneration()
	{
		genMode = 0;
		format = 0;
		planeS = new float[] { 1.0F, 0.0F, 0.0F, 0.0F };
		planeT = new float[] { 0.0F, 1.0F, 0.0F, 0.0F };
		planeR = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
		planeQ = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
		enable = true;
	}
}
