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
 
package vecmath;

/**
 * @author Shawn Kendall
 *
 */
public class Util
{
	//private temp objects	
	private static Matrix4f tempMat4f_private = new Matrix4f();
	private static Vector3f tempVec3f_private = new Vector3f();
	private static Vector3f tempVec3f_2_private = new Vector3f();
	private static Vector3f tempVec3f_3_private = new Vector3f();

	public static final Vector3f yVecf = new Vector3f(0, 1, 0);
	public static final Vector3f zeroVecf = new Vector3f(0, 0, 0);
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX vect/array/mats Util methods XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	//NOTE: this method returns a float array in openGL format. first 3 is the x vector, ...	
	public static void Mat3fVec3fTofArray16(
		float[] array,
		Matrix3f mat,
		Vector3f vec) {

		array[0] = mat.m00;
		array[1] = mat.m10;
		array[2] = mat.m20;
		array[3] = 0;

		array[4] = mat.m01;
		array[5] = mat.m11;
		array[6] = mat.m21;
		array[7] = 0;

		array[8] = mat.m02;
		array[9] = mat.m12;
		array[10] = mat.m22;
		array[11] = 0;

		array[12] = vec.x;
		array[13] = vec.y;
		array[14] = vec.z;
		array[15] = 1;
	}
	public static void Mat3fTofArray16(float[] array, Matrix3f mat) {

		array[0] = mat.m00;
		array[1] = mat.m10;
		array[2] = mat.m20;
		array[3] = 0;

		array[4] = mat.m01;
		array[5] = mat.m11;
		array[6] = mat.m21;
		array[7] = 0;

		array[8] = mat.m02;
		array[9] = mat.m12;
		array[10] = mat.m22;
		array[11] = 0;

		array[12] = 0;
		array[13] = 0;
		array[14] = 0;
		array[15] = 1;
	}

	public static void Mat4fTofArray16(float[] array, Matrix4f mat) {

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

	public static void Mat4fTodArray16(double[] array, Matrix4f mat) {

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

	public static void printMatrix4f(float mat[]) {
		System.out.println("\tOpenGL format  X.x, X.y, X.z, 0");
		System.out.println(
			"\t" + mat[0] + ", " + mat[1] + ", " + mat[2] + ", " + mat[3]);
		System.out.println(
			"\t" + mat[4] + ", " + mat[5] + ", " + mat[6] + ", " + mat[7]);
		System.out.println(
			"\t" + mat[8] + ", " + mat[9] + ", " + mat[10] + ", " + mat[11]);
		System.out.println(
			"\t" + mat[12] + ", " + mat[13] + ", " + mat[14] + ", " + mat[15]);
		System.out.println("");
	}

	public static void printMatrix4d(double mat[]) {
		System.out.println("\tOpenGL format  X.x, X.y, X.z, 0");
		System.out.println(
			"\t" + mat[0] + ", " + mat[1] + ", " + mat[2] + ", " + mat[3]);
		System.out.println(
			"\t" + mat[4] + ", " + mat[5] + ", " + mat[6] + ", " + mat[7]);
		System.out.println(
			"\t" + mat[8] + ", " + mat[9] + ", " + mat[10] + ", " + mat[11]);
		System.out.println(
			"\t" + mat[12] + ", " + mat[13] + ", " + mat[14] + ", " + mat[15]);
		System.out.println("");
	}

	public static void printMatrix3f(Matrix3f mat) {
		System.out.println("\tVecMath format  X.x, Y.x, Z.x");
		System.out.println("\t" + mat.m00 + ", " + mat.m01 + ", " + mat.m02);
		System.out.println("\t" + mat.m10 + ", " + mat.m11 + ", " + mat.m12);
		System.out.println("\t" + mat.m20 + ", " + mat.m21 + ", " + mat.m22);
		System.out.println("");
	}

	public static void printMatrix4f(Matrix4f mat) {
		System.out.println("\tVecMath format  X.x, Y.x, Z.x, T.x");
		System.out.println(
			"\t" + mat.m00 + ", " + mat.m01 + ", " + mat.m02 + ", " + mat.m03);
		System.out.println(
			"\t" + mat.m10 + ", " + mat.m11 + ", " + mat.m12 + ", " + mat.m13);
		System.out.println(
			"\t" + mat.m20 + ", " + mat.m21 + ", " + mat.m22 + ", " + mat.m23);
		System.out.println(
			"\t" + mat.m30 + ", " + mat.m31 + ", " + mat.m32 + ", " + mat.m33);
		System.out.println("");
	}

	public static void printVec3f(float v[]) {
		System.out.println("Vec: \t" + v[0] + ", " + v[1] + ", " + v[2]);
		System.out.println("");
	}

	public static void printVec3f(Vector3f v) {
		System.out.println("Vec: \t" + v.x + ", " + v.y + ", " + v.z);
		System.out.println("");
	}

	static public void computeLookAt(Vector3f zVec, Matrix3f mat) {
		zVec.normalize();

		tempVec3f_2_private.cross(yVecf, zVec);
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

	static public void getZVec(Matrix3f m, Vector3f v3f) {
		v3f.set(m.m02, m.m12, m.m22);
	}

	static public void getYVec(Matrix3f m, Vector3f v3f) {
		v3f.set(m.m01, m.m11, m.m21);
	}

	static public void getForwardVec(Matrix3f m, Vector3f v3f) {
		v3f.set(-m.m02, -m.m12, -m.m22);
	}

	static public void getXVec(Matrix3f m, Vector3f v3f) {
		v3f.set(m.m00, m.m10, m.m20);
	}

}
