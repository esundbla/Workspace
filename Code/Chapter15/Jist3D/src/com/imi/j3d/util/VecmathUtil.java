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

import javax.vecmath.*;

public class VecmathUtil
{
	public static final double toDegrees = 180.0 / Math.PI;
	public static final double toRadians = Math.PI / 180.0;
	public static final double oneOverOneEighty = 1.0 / 180.0;
	public static final double oneOverNinty = 1.0 / 90.0;

	public static final Vector3d xVecd = new Vector3d(1, 0, 0);
	public static final Vector3d yVecd = new Vector3d(0, 1, 0);
	public static final Vector3d zVecd = new Vector3d(0, 0, 1);
	public static final Vector3d xVecNegatived = new Vector3d(-1, 0, 0);
	public static final Vector3d yVecNegatived = new Vector3d(0, -1, 0);
	public static final Vector3d zVecNegatived = new Vector3d(0, 0, -1);
	public static final Vector3d zeroVecd = new Vector3d(0, 0, 0);

	public static final Vector3f yVecf = new Vector3f(0, 1, 0);
	public static final Vector3f zeroVecf = new Vector3f(0, 0, 0);

	//public temporary objects for IMMEDIATE set and retrival by other classes.
	//WARNING: these are obviously not thread safe!!. also, dont put values here and expect them to stay here
	// these are here only for prototyping 
	public static Matrix4d tempMat4d = new Matrix4d();
	public static Matrix4f tempMat4f = new Matrix4f();
	public static Matrix3f tempMat3f = new Matrix3f();
	public static Matrix3d tempMat3d = new Matrix3d();
	public static Vector3d tempVec3d = new Vector3d();
	public static Vector3f tempVec3f = new Vector3f();
	public static Point3d tempPoint3d = new Point3d();
	public static Point3f tempPoint3f = new Point3f();

	public static Matrix4d tempMat4d_2 = new Matrix4d();
	public static Matrix4f tempMat4f_2 = new Matrix4f();
	public static Matrix4d tempMat4d_3 = new Matrix4d();
	public static Matrix4f tempMat4f_3 = new Matrix4f();
	public static Matrix3f tempMat3f_2 = new Matrix3f();
	public static Matrix3d tempMat3d_2 = new Matrix3d();
	public static Vector3d tempVec3d_2 = new Vector3d();
	public static Vector3d tempVec3d_3 = new Vector3d();
	public static Vector3f tempVec3f_2 = new Vector3f();
	public static Point3d tempPoint3d_2 = new Point3d();
	public static Point3f tempPoint3f_2 = new Point3f();

	//private temp objects	
	private static Matrix4d tempMat4d_private = new Matrix4d();
	private static Matrix4f tempMat4f_private = new Matrix4f();
	private static Point3d tempPoint3d_private = new Point3d();
	private static Vector3d tempVec3d_private = new Vector3d();
	private static Vector3d tempVec3d_2_private = new Vector3d();
	private static Vector3d tempVec3d_3_private = new Vector3d();
	private static Vector3f tempVec3f_private = new Vector3f();
	private static Vector3f tempVec3f_2_private = new Vector3f();
	private static Vector3f tempVec3f_3_private = new Vector3f();

	private static Vector3f tempVecX = new Vector3f();
	private static Vector3d tempVecA = new Vector3d();
	private static Vector3d tempVecB = new Vector3d();

	static public void dirToMatrix3f(Vector3f zAxis, Vector3f yAxis, Matrix3f mat)
	{
		tempVecX.cross(yAxis, zAxis);
		yAxis.cross(zAxis, tempVecX);

		mat.m00 = tempVecX.x;
		mat.m10 = tempVecX.y;
		mat.m20 = tempVecX.z;

		mat.m01 = yAxis.x;
		mat.m11 = yAxis.y;
		mat.m21 = yAxis.z;

		mat.m02 = zAxis.x;
		mat.m12 = zAxis.y;
		mat.m22 = zAxis.z;
	}

	static public void computeNormal(Point3d[] points, Vector3d normal)
	{
		tempVecA.sub(points[0], points[1]);
		tempVecB.sub(points[2], points[1]);
		tempVecA.normalize();
		tempVecB.normalize();
		normal.cross(tempVecB, tempVecA);
		normal.normalize();
	}

	static public void computeNormal(Vector3f vecA, Vector3f vecB, Vector3f vecOut)
	{
		tempVecX.cross(vecA, vecB);
		vecOut.cross(vecA, tempVecX);
	}

	static public void computeLookAt(Vector3d pos, Vector3d targetPos, Matrix4d mat)
	{
		tempVec3d_private.sub(pos, targetPos);
		tempVec3d_private.normalize();

		tempVec3d_2_private.cross(VecmathUtil.yVecd, tempVec3d_private);
		tempVec3d_2_private.normalize();

		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);
		tempVec3d_3_private.normalize();

		mat.m00 = tempVec3d_2_private.x;
		mat.m10 = tempVec3d_2_private.y;
		mat.m20 = tempVec3d_2_private.z;

		mat.m01 = tempVec3d_3_private.x;
		mat.m11 = tempVec3d_3_private.y;
		mat.m21 = tempVec3d_3_private.z;

		mat.m02 = tempVec3d_private.x;
		mat.m12 = tempVec3d_private.y;
		mat.m22 = tempVec3d_private.z;

		mat.m03 = pos.x;
		mat.m13 = pos.y;
		mat.m23 = pos.z;
	}
	/*	
		
		static public void computeLookAt(Vector3d zVec, Matrix3d mat){
			zVec.normalize();
	
			tempVec3d_2_private.cross(VecMathUtilOde.yVecd, zVec);
			tempVec3d_2_private.normalize();
	
			tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);
			tempVec3d_3_private.normalize();
	
			mat.m00 = tempVec3d_2_private.x;
			mat.m10 = tempVec3d_2_private.y;
			mat.m20 = tempVec3d_2_private.z;
	
			mat.m01 = tempVec3d_3_private.x;
			mat.m11 = tempVec3d_3_private.y;
			mat.m21 = tempVec3d_3_private.z;
	
			mat.m02 = zVec.x;
			mat.m12 = zVec.y;
			mat.m22 = zVec.z;
		}
	*/

	static public void computeLookAt(Vector3f zVec, Matrix3f mat)
	{
		zVec.normalize();

		tempVec3f_2_private.cross(VecmathUtil.yVecf, zVec);
		tempVec3f_2_private.normalize();

		tempVec3f_3_private.cross(zVec, tempVec3f_2_private);
		tempVec3f_3_private.normalize();

		mat.m00 = tempVec3f_2_private.x;
		mat.m10 = tempVec3f_2_private.y;
		mat.m20 = tempVec3f_2_private.z;

		mat.m01 = tempVec3f_3_private.x;
		mat.m11 = tempVec3f_3_private.y;
		mat.m21 = tempVec3f_3_private.z;

		mat.m02 = zVec.x;
		mat.m12 = zVec.y;
		mat.m22 = zVec.z;
	}

	/**
	 * WARNING: depricated!!!! use getSignedAngle instead!!!!!!
	 * NOTE: this version treats vectors as vecs in x,z plane!
	 * 
	 * @deprecated 
	 */
	static public double computeSignedAngle(Vector3d v1, Vector3d v2)
	{

		tempVec3d_private.set(v1.x, 0, v1.z);
		tempVec3d_2_private.set(v2.x, 0, v2.z);

		double angle = tempVec3d_private.angle(tempVec3d_2_private);

		// catch Nan case
		if (!(Math.abs(angle) <= Math.PI))
		{
			angle = 0;
		}

		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);

		if (tempVec3d_3_private.y < 0)
			angle = -angle;

		return angle;
	}

	/*
	  Tested for:
			VecMathUtilOde.tempVec3d.set(1,0,0);
			VecMathUtilOde.tempVec3d_2.set(-1,0,0);
	
			VecMathUtilOde.tempVec3d.set(1,0,0);
			VecMathUtilOde.tempVec3d.set(1,0,0);
	
			VecMathUtilOde.tempVec3d_2.set(0,0,1);
			VecMathUtilOde.tempVec3d_2.set(0,1,0);
	
			VecMathUtilOde.tempVec3d.set(.2,0,-.2);
			VecMathUtilOde.tempVec3d_2.set(-.2,0,-.2);
	
			VecMathUtilOde.tempVec3d.set(0,-1,0);
			VecMathUtilOde.tempVec3d_2.set(0,0,-1);
	 */
	/**
	 * signed angle between to vectors. it returns the smaller angle
	 */
	static public double computeAngleGlobal(Vector3d v1, Vector3d v2)
	{

		tempVec3d_private.set(v1);
		tempVec3d_2_private.set(v2);

		double angle = tempVec3d_private.angle(tempVec3d_2_private);

		// catch Nan case
		if ((Math.abs(angle) > Math.PI))
		{
			angle = 0;
		}

		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);

		if (tempVec3d_3_private.y < 0)
		{
			angle = -angle;
			//System.out.println("Y");

		}
		else if (tempVec3d_3_private.y == 0)
		{
			if (tempVec3d_3_private.x < 0)
			{
				angle = -angle;
				//System.out.println("X");

			}
			else if (tempVec3d_3_private.x == 0)
			{
				if (tempVec3d_3_private.z < 0)
				{
					angle = -angle;
					//System.out.println("Z");
				}
			}
		}

		return angle;
	}

	static public double computeAngle(Vector3d v1, Vector3d v2, Vector3d positiveHalfSpace)
	{

		tempVec3d_private.set(v1);
		tempVec3d_2_private.set(v2);

		double angle = tempVec3d_private.angle(tempVec3d_2_private);

		// catch Nan case
		if (Math.abs(angle) > Math.PI)
		{
			angle = 0;
		}

		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);

		if (tempVec3d_3_private.dot(positiveHalfSpace) < 0)
			angle = -angle;

		return angle;
	}

	/**
	 * projects arg1 onto arg2, store in arg3
	 **/
	static public void projectVec(Vector3d vecA, Vector3d vecB, Vector3d vecOut)
	{
		double scale = vecB.dot(vecA) / vecB.lengthSquared();
		vecOut.scale(scale, vecB);
	}

	static public void getZVec(Matrix3f m, Vector3f v3f)
	{
		v3f.set(m.m02, m.m12, m.m22);
	}

	static public void getYVec(Matrix3f m, Vector3f v3f)
	{
		v3f.set(m.m01, m.m11, m.m21);
	}

	static public void getForwardVec(Matrix3f m, Vector3f v3f)
	{
		v3f.set(-m.m02, -m.m12, -m.m22);
	}

	static public void getXVec(Matrix3f m, Vector3f v3f)
	{
		v3f.set(m.m00, m.m10, m.m20);
	}

	static public void getZVec(Matrix4d m, Vector3d v3d)
	{
		v3d.set(m.m02, m.m12, m.m22);
	}

	static public void getYVec(Matrix4d m, Vector3d v3d)
	{
		v3d.set(m.m01, m.m11, m.m21);
	}

	static public void getForwardVec(Matrix4d m, Vector3d v3d)
	{
		v3d.set(-m.m02, -m.m12, -m.m22);
	}

	static public void getXVec(Matrix4d m, Vector3d v3d)
	{
		v3d.set(m.m00, m.m10, m.m20);
	}

	/**
	 * given a vector, a matrix is computed that would represent that vector.
	 * if a "geometry with length 1, at the origin, facing -z" is placed under this matrix, 
	 * it will have the size and direction of the passed in vector
	 */
	static public void computeMatFromVac(Vector3d v3d, Matrix4d m)
	{
		m.setIdentity();

		tempVec3d_private.set(v3d);
		tempVec3d_private.negate();

		//z vec
		m.m02 = tempVec3d_private.x;
		m.m12 = tempVec3d_private.y;
		m.m22 = tempVec3d_private.z;

		tempVec3d_private.normalize();

		//x vec
		tempVec3d_2_private.cross(yVecd, tempVec3d_private);
		m.m00 = tempVec3d_2_private.x;
		m.m10 = tempVec3d_2_private.y;
		m.m20 = tempVec3d_2_private.z;

		//y vec
		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);
		m.m01 = tempVec3d_3_private.x;
		m.m11 = tempVec3d_3_private.y;
		m.m21 = tempVec3d_3_private.z;

	}

	static public void computeMatFromRay(Vector3d v3d, Matrix4d m, double length)
	{
		m.setIdentity();

		tempVec3d_private.set(v3d);
		tempVec3d_private.negate();
		tempVec3d_private.normalize();
		tempVec3d_private.scale(length, tempVec3d_private);

		//z vec
		m.m02 = tempVec3d_private.x;
		m.m12 = tempVec3d_private.y;
		m.m22 = tempVec3d_private.z;

		tempVec3d_private.normalize();

		//x vec
		tempVec3d_2_private.cross(yVecd, tempVec3d_private);
		m.m00 = tempVec3d_2_private.x;
		m.m10 = tempVec3d_2_private.y;
		m.m20 = tempVec3d_2_private.z;

		//y vec
		tempVec3d_3_private.cross(tempVec3d_private, tempVec3d_2_private);
		m.m01 = tempVec3d_3_private.x;
		m.m11 = tempVec3d_3_private.y;
		m.m21 = tempVec3d_3_private.z;

	}

	/**
	 * vectors x, y, z will be between 0 and 1
	 */
	static public void computeRandomVector(Vector3d vector)
	{
		vector.set(Math.random(), Math.random(), Math.random());
	}

	public static void Mat4fTofArray16(float[] array, Matrix4f mat)
	{

		array[0] = mat.m00;
		array[1] = mat.m10;
		array[2] = mat.m20;
		array[3] = mat.m30;

		array[4] = mat.m01;
		array[5] = mat.m11;
		array[6] = mat.m21;
		array[7] = mat.m31;

		array[8] = mat.m02;
		array[9] = mat.m12;
		array[10] = mat.m22;
		array[11] = mat.m32;

		array[12] = mat.m03;
		array[13] = mat.m13;
		array[14] = mat.m23;
		array[15] = mat.m33;
	}

	public static void Mat4dTodArray16(double[] array, Matrix4d mat)
	{

		array[0] = mat.m00;
		array[1] = mat.m10;
		array[2] = mat.m20;
		array[3] = mat.m30;

		array[4] = mat.m01;
		array[5] = mat.m11;
		array[6] = mat.m21;
		array[7] = mat.m31;

		array[8] = mat.m02;
		array[9] = mat.m12;
		array[10] = mat.m22;
		array[11] = mat.m32;

		array[12] = mat.m03;
		array[13] = mat.m13;
		array[14] = mat.m23;
		array[15] = mat.m33;
	}

	public static void Mat4fTodArray16(double[] array, Matrix4f mat)
	{

		array[0] = mat.m00;
		array[1] = mat.m10;
		array[2] = mat.m20;
		array[3] = mat.m30;

		array[4] = mat.m01;
		array[5] = mat.m11;
		array[6] = mat.m21;
		array[7] = mat.m31;

		array[8] = mat.m02;
		array[9] = mat.m12;
		array[10] = mat.m22;
		array[11] = mat.m32;

		array[12] = mat.m03;
		array[13] = mat.m13;
		array[14] = mat.m23;
		array[15] = mat.m33;
	}

	public static void printVec3d(Vector3d v)
	{
		System.out.println("Vec: \t" + v.x + ", " + v.y + ", " + v.z);
		System.out.println("");
	}

}