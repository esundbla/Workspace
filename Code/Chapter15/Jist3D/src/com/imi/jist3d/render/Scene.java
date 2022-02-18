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
import java.util.ArrayList;

public class Scene
{

	private ArrayList sceneList = new ArrayList();
	public float[] sceneMatrix = new float[]
	{	1,0,0,0,
		0,1,0,0,
		0,0,1,0,
		0,0,0,1
	};
	public double[] lightViewMatrix = new double[16];
	public double[] lightInverseViewMatrix = new double[16];
	public double[] lightFrustumMatrix = new double[16];
	public double[] lightInverseFrustumMatrix = new double[16];
	double lightFieldOfView = 10.0;
	double lightNear = 0.01;
	double lightFar = 25.0; //24.0;
	double lightHalfWidth = 10.0;
	double lightHalfHeight = 10.0;
	Light light = new Light();
	Camera camera = new Camera();
	SkyBox skyBox = new SkyBox();

	public void init(GL gl, GLU glu)
	{
		// = _gl;
		//RendererUtils.setRenderShapes(sceneList, gl, null);
		skyBox.initSkybox(gl, glu);
	}
	
	/*
	public void rebind(GL gl)
	{
		RendererUtils.unbindRenderShapes(sceneList, gl);
		//RendererUtils.setRenderShapes(sceneList, gl, null);
	}
	*/
	/**
	 * @return
	 */
	public Camera getCamera()
	{
		return camera;
	}

	/**
	 * @param camera
	 */
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	/**
	 * @return
	 */
	public Light getLight()
	{
		return light;
	}

	/**
	 * @param light
	 */
	public void setLight(Light light)
	{
		this.light = light;
	}

	/**
	 * @return
	 */
	public SkyBox getSkyBox()
	{
		return skyBox;
	}

	/**
	 * @param box
	 */
	public void setSkyBox(SkyBox box)
	{
		skyBox = box;
	}

	/**
	 * @return
	 */
	public ArrayList getSceneList()
	{
		return sceneList;
	}

}

