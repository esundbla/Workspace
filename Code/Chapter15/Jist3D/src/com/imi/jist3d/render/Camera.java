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
public class Camera
{
	public double[] eyeViewMatrix = new double[16];
	public double[] inverseEyeViewMatrix = new double[16];
	double[] eyeFrustumMatrix = new double[16];
	double fieldOfView = 70.0;
	double nearClip = 0.1f;
	double farClip = 1000.0f;
	private boolean dirty = false;
	boolean autoUpdatePerspectiveMatrix = true;
	double aspectRatio = 1.0;

	public void rebuildPerspectiveMatrix()
	{
		if (dirty && autoUpdatePerspectiveMatrix)
		{
			VectorMatrixUtils.buildPerspectiveMatrix(
				eyeFrustumMatrix,
				fieldOfView,
				1.0 / aspectRatio,
				nearClip,
				farClip);
			dirty = false;
		}
	}

	public void setFieldOfView(double FOV)
	{
		fieldOfView = FOV;
		dirty = true;
	}

	public double getFieldOfView()
	{
		return fieldOfView;
	}

	public void setAspectRatio(double aspect)
	{
		aspectRatio = aspect;
		dirty = true;
	}
	/**
	 * @return
	 */
	public double getAspectRatio()
	{
		return aspectRatio;
	}

	/**
	 * @return
	 */
	public double getFarClip()
	{
		return farClip;
	}

	/**
	 * @return
	 */
	public double getNearClip()
	{
		return nearClip;
	}

	/**
	 * @param d
	 */
	public void setFarClip(double d)
	{
		farClip = d;
		dirty = true;
	}

	/**
	 * @param d
	 */
	public void setNearClip(double d)
	{
		nearClip = d;
		dirty = true;
	}

	/**
	 * @return
	 */
	public double[] getEyeFrustumMatrix()
	{
		return eyeFrustumMatrix;
	}

	/**
	 * @param ds
	 */
	public void setEyeFrustumMatrix(double[] ds)
	{
		eyeFrustumMatrix = ds;
	}

	/**
	 * @return
	 */
	public boolean isAutoUpdatePerspectiveMatrix()
	{
		return autoUpdatePerspectiveMatrix;
	}

	/**
	 * @param b
	 */
	public void setAutoUpdatePerspectiveMatrix(boolean b)
	{
		autoUpdatePerspectiveMatrix = b;
	}

}
