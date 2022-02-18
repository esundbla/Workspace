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
import java.io.File;

public class SkyBox
{
	Texture myTextures[] = new Texture[3];
	boolean enable = true;
	public void initSkybox(GL gl, GLU glu)
	{
		try
		{
			myTextures[0] =
				TextureFactory.getFactory(gl).createTexture(
					"skybox12",
					"." + File.separatorChar + "data" + File.separatorChar + "12.rgb",
					GL.GL_TEXTURE_2D,
					GL.GL_RGB,
					GL.GL_RGB,
					GL.GL_LINEAR,
					GL.GL_LINEAR,
					false,
					false,
					glu);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_S,
				GL.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_T,
				GL.GL_CLAMP_TO_EDGE);

			myTextures[1] =
				TextureFactory.getFactory(gl).createTexture(
					"skybox34",
					"." + File.separatorChar + "data" + File.separatorChar + "34.rgb",
					GL.GL_TEXTURE_2D,
					GL.GL_RGB,
					GL.GL_RGB,
					GL.GL_LINEAR,
					GL.GL_LINEAR,
					false,
					false,
					glu);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_S,
				GL.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_T,
				GL.GL_CLAMP_TO_EDGE);

			myTextures[2] =
				TextureFactory.getFactory(gl).createTexture(
					"skybox5",
					"." + File.separatorChar + "data" + File.separatorChar + "5.rgb",
					GL.GL_TEXTURE_2D,
					GL.GL_RGB,
					GL.GL_RGB,
					GL.GL_LINEAR,
					GL.GL_LINEAR,
					false,
					false,
					glu);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_S,
				GL.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(
				GL.GL_TEXTURE_2D,
				GL.GL_TEXTURE_WRAP_T,
				GL.GL_CLAMP_TO_EDGE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			enable = false;
		}

	}
	public void drawSkybox(GL gl, double[] viewMat)
	{
		if (!enable)
			return;
		// draw the sky box in the following order:
		// front, right, back, left, top, bottom

		// disable 2nd texture unit, set texture matrix to identity, disable
		// automatc texgen

		gl.glActiveTexture(GL.GL_TEXTURE3);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glActiveTexture(GL.GL_TEXTURE2);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glActiveTexture(GL.GL_TEXTURE1);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glMatrixMode(GL.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glDisable(GL.GL_TEXTURE_GEN_S);
		gl.glDisable(GL.GL_TEXTURE_GEN_T);
		//gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
		gl.glDisable(GL.GL_BLEND);
		gl.glDisable(GL.GL_ALPHA_TEST);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDepthMask(false);

		// set up the modelview matrix
		double[] mat = new double[16];
		VectorMatrixUtils.copyMatrix(mat, viewMat);
		//VecMath.transposeZero(mat);
		mat[12] = 0;
		mat[13] = 0;
		mat[14] = 0;

		gl.glPushMatrix();
		gl.glLoadMatrixd(mat);

		gl.glDisable(GL.GL_LIGHTING);
		gl.glColor3f(1.0f,1.0f,1.0f);

		// bind the front texture
		myTextures[0].bind(gl);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		// Forward or Zneg
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.5f);
		gl.glVertex3f(-1, -0.1f, -1);

		gl.glTexCoord2f(1.0f, 0.5f);
		gl.glVertex3f(1, -0.1f, -1);

		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, -1);

		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(-1, 1, -1);

		gl.glEnd();

		// Right or Xpos
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0.5f);
		gl.glVertex3f(1, -0.1f, -1);

		gl.glTexCoord2f(1, 0.5f);
		gl.glVertex3f(1, -0.1f, 1);

		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 1, 1);

		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(1, 1, -1);
		gl.glEnd();

		myTextures[1].bind(gl);
		// Back or Zpos
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0.5f);
		gl.glVertex3f(1, -0.1f, 1);

		gl.glTexCoord2f(1, 0.5f);
		gl.glVertex3f(-1, -0.1f, 1);

		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(-1, 1, 1);

		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(1, 1, 1);
		gl.glEnd();

		// left or Xneg
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0.5f);
		gl.glVertex3f(-1, -0.1f, 1);

		gl.glTexCoord2f(1, 0.5f);
		gl.glVertex3f(-1, -0.1f, -1);

		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(-1, 1, -1);

		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(-1, 1, 1);
		gl.glEnd();

		myTextures[2].bind(gl);
		// top
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(-1, 1, -1);

		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(1, 1, -1);

		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(1, 1, 1);

		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(-1, 1, 1);
		gl.glEnd();

		// bottom
//		gl.glBegin(GL.GL_QUADS);
//		gl.glTexCoord2f(1, 1);
//		gl.glVertex3f(-1, -1, 1);
//
//		gl.glTexCoord2f(1, 0);
//		gl.glVertex3f(1, -1, 1);
//
//		gl.glTexCoord2f(0, 0);
//		gl.glVertex3f(1, -1, -1);
//
//		gl.glTexCoord2f(0, 1);
//		gl.glVertex3f(-1, -1, -1);
//		gl.glEnd();

		gl.glPopMatrix();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthMask(true);
		gl.glEnable(GL.GL_CULL_FACE);
	}

	/**
	 * @return
	 */
	public boolean isEnable()
	{
		return enable;
	}

	/**
	 * @param b
	 */
	public void setEnable(boolean b)
	{
		enable = b;
	}

}
