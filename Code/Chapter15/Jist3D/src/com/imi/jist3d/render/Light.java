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

public class Light
{
	public float lightVector[] = { -50.0f, 50.0f, 100.0f, 0.0f };
	float LIGHT_DIMMING = 0.2f;
	float ambientColor[] =
		{ LIGHT_DIMMING, LIGHT_DIMMING, LIGHT_DIMMING, 1.0f };
	float diffuseColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float specularColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	public float[] getLightVector()
	{
		return lightVector;
	}

	public void setLightVector(float i, float j, float k)
	{
		lightVector[0] = i;
		lightVector[1] = j;
		lightVector[2] = k;
		//System.out.println(this);
	}
	public void setLightVector(float i, float j, float k, float l)
	{
		lightVector[0] = i;
		lightVector[1] = j;
		lightVector[2] = k;
		lightVector[3] = l;
		//System.out.println(lightVector[0] + " " + lightVector[1] + " " + lightVector[2]);
	}
}
