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

package com.imi.jist3d.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.imi.jist3d.render.Scene;
import com.imi.jist3d.render.VectorMatrixUtils;
import vecmath.*;


import com.imi.j3d.util.*;

/**
 * @author Shawn Kendall
 *
 */
	
public class MouseConstrainedTumble extends MouseInputAdapter
{
	private int prevMouseX, prevMouseY;
	private boolean mouseRButtonDown = false;
	float eyeAngle = 0.0f;
	float eyeHeight = 0.0f;
	int xEyeBegin, yEyeBegin, movingEye = 0;
	int xLightBegin, yLightBegin, movingLight = 0;
	public float[] translation = new float[] { 0.0f, 0.0f, 0.0f };
	public float globalScale = 0.6f;
	public float z_factor = .04f;
	public float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
	public float angle = 0.0f;
	public Scene scene;
	public int mode = 0;
	public float[] targetMat = null;
	Vector3f pos = new Vector3f();
	Vector3f delta = new Vector3f();
	Vector3f targetPos = new Vector3f();
	Vector3f cameraXVec = new Vector3f();
	Vector3f cameraYVec = new Vector3f();
	Vector3f cameraZVec = new Vector3f();
	Vector3f worldUpVec = new Vector3f(0,1,0);
	Matrix4f cameraMat = new Matrix4f();
	Matrix4f sceneMat = new Matrix4f();
	Vector3f cameraPos = new Vector3f();
	Matrix4f testMat = new Matrix4f();
	javax.vecmath.Matrix4f testMat2 = new javax.vecmath.Matrix4f();



	public MouseConstrainedTumble(Scene sceneIn)
	{
		scene = sceneIn;
		cameraMat.setIdentity();
	}
	// Methods required for the implementation of MouseListener
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			mouseRButtonDown = true;
		}
		xEyeBegin = e.getX();
		yEyeBegin = e.getY();
		xLightBegin = e.getX();
		yLightBegin = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
		if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			mouseRButtonDown = false;
		}
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	// Methods required for the implementation of MouseMotionListener
	public void mouseDragged(MouseEvent e)
	{

		int x = e.getX();
		int y = e.getY();

		if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
		{
			Dimension size = e.getComponent().getSize();

			float thetaY =
				360.0f * ((float) (x - prevMouseX) / (float)size.width);
			float thetaX =
				-360.0f * ((float) (prevMouseY - y) / (float)size.height);

			view_rotx += thetaX;
			view_roty += thetaY;
			int dy = y - prevMouseY;
			translation[1] += -dy * z_factor;
		}
		else if ((e.getModifiers() & e.BUTTON2_MASK) != 0)
		{
			int dx = x - prevMouseX;
			int dy = y - prevMouseY;
			if ((e.getModifiers() & e.SHIFT_MASK) != 0)
			{
				scene.getCamera().setFieldOfView(
					scene.getCamera().getFieldOfView() + dy * z_factor);
			}
			else
			{
				//translation[0] += dx * z_factor;
				//translation[1] += -dx * z_factor;
				//translation[2] += dy * z_factor;
				globalScale += dy * z_factor * .1;
				//System.out.println(e);
			}
		}
		else if ((e.getModifiers() & e.BUTTON3_MASK) != 0)
		{
			int dx = x - prevMouseX;
			int dy = y - prevMouseY;
			eyeAngle = (float) (eyeAngle - 0.005 * (x - xEyeBegin));
			eyeHeight = (float) (eyeHeight + 0.15 * (y - yEyeBegin));
			if (eyeHeight > 20.0)
				eyeHeight = 20.0f;
			if (eyeHeight < -12.0)
				eyeHeight = -12.0f;
			xEyeBegin = x;
			yEyeBegin = y;
		}

		//if (movingEye) {
		//needMatrixUpdate = 1;
		//glutPostRedisplay();
		//}

		prevMouseX = x;
		prevMouseY = y;

		//System.out.println(e);
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void update()
	{
		if ( mode == 0 )
		{
		VectorMatrixUtils.buildLookAtMatrix(
			scene.getCamera().eyeViewMatrix,
			15 * Math.sin(eyeAngle),
			eyeHeight,
			15 * Math.cos(eyeAngle),
			0,0,0,0,1,0);
			
		VectorMatrixUtils.rotY(scene.sceneMatrix, view_roty);
		VectorMatrixUtils.scale(scene.sceneMatrix, globalScale);
		scene.sceneMatrix[12] = translation[0];
		scene.sceneMatrix[13] = translation[1];
		scene.sceneMatrix[14] = translation[2];
		//System.out.println("Determinant1 = " + Matrix4f.determinant(scene.sceneMatrix,4));
		//System.out.println("Determinant2 = " + testMat.determinant());
		//System.out.println("Before invert()");
		//vecmath.Util.printMatrix4f(testMat);
		//System.out.println(testMat2);
		testMat.set(scene.sceneMatrix);
		testMat2.set(scene.sceneMatrix);
		testMat.invert();
		testMat2.invert();
		//System.out.println("After invert()");
		//vecmath.Util.printMatrix4f(testMat);
		//System.out.println(testMat2);

		
		}
		else if ( mode == 1 )
		{
			//cameraMat.set(scene.sceneMatrix);
			//cameraMat.transpose();
			//cameraMat.invert();
			//pos.set( cameraMat.m03, cameraMat.m13, cameraMat.m23 );

			//pos.set( cameraMat, scene.sceneMatrix[13], scene.sceneMatrix[14] );  
			//System.out.println(targetMat)      
			//PuppyGameManager.printMatrix4f(targetMat);
			targetPos.x = targetMat[12];
			targetPos.y = targetMat[13];
			targetPos.z = targetMat[14];
			
			sceneMat.set(scene.sceneMatrix);
			sceneMat.orthonormalInvert();
			//sceneMat.transpose();
			Vector3f tmpVec = new Vector3f(targetPos);
			sceneMat.transform(tmpVec);
			targetPos.set(tmpVec);
			
			pos.set( cameraPos );
			pos.y = targetPos.y+1;
			
			cameraZVec.sub(pos, targetPos);
			//System.out.println("targetPos " + targetPos);
			float length = cameraZVec.length();
			cameraZVec.normalize();
			length -= 5;
			delta.set(cameraZVec);
			delta.scale( -length );

			pos.add( delta );
			pos.y = targetPos.y+1;

			cameraZVec.sub(pos, targetPos);
			cameraZVec.normalize();

			cameraXVec.cross(worldUpVec, cameraZVec);
			cameraXVec.normalize();

			cameraYVec.cross(cameraZVec, cameraXVec);
			cameraYVec.normalize();
			
			cameraMat.m00 = (float)cameraXVec.x;
			cameraMat.m10 = (float)cameraXVec.y;
			cameraMat.m20 = (float)cameraXVec.z;

			cameraMat.m01 = (float)cameraYVec.x;
			cameraMat.m11 = (float)cameraYVec.y;
			cameraMat.m21 = (float)cameraYVec.z;

			cameraMat.m02 = (float)cameraZVec.x;
			cameraMat.m12 = (float)cameraZVec.y;
			cameraMat.m22 = (float)cameraZVec.z;
			
			cameraMat.m03 = (float)pos.x;
			cameraMat.m13 = (float)pos.y;
			cameraMat.m23 = (float)pos.z;

			cameraPos.x = pos.x;
			cameraPos.y = pos.y;
			cameraPos.z = pos.z;

			cameraMat.orthonormalInvert();
			//System.out.println(cameraMat);
			//cameraMat.transpose();
			//System.out.println(cameraMat);
			vecmath.Util.Mat4fTodArray16( scene.getCamera().eyeViewMatrix, cameraMat );
		}

	}

}
