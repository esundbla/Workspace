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

import com.imi.util.MathUtils;

/**
 * @author Shawn Kendall, IMI, LLC
 *
 * Current mostly adpated from demos source
 * and modified where needed for order or possible optimizations
 * (such as removing loops)
 * 
 * 
 * 	  	vector.x = (float)Math.sin(Math.toRadians(x));
 * 		vs.
 * 		vector3.x = VecMath.sin(vector3.x);
		~3.15 times faster
 */

final public class VectorMatrixUtils
{
	//private static final int sinScaleInverse = 1/sinScale;
	private static double[] x = new double[3], y = new double[3], z = new double[3];

	static final public float[] newMatIdentity()
	{
		return new float[] {
		1, 0, 0, 0,
		0, 1, 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1};
	}

	static final public float[] newMat()
	{
		return new float[] {
		0, 0, 0, 0,
		0, 0, 0, 0,
		0, 0, 0, 0,
		0, 0, 0, 0};
	}

	static final public void setIdentity(float[] mat)
	{
		mat[0] = 1.0f;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 1.0f;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10] = 1.0f;
		mat[11] = 0.0f;
		mat[12] = 0.0f;
		mat[13] = 0.0f;
		mat[14] = 0.0f;
		mat[15] = 1.0f;
	}

	static final public void setIdentity(double[] mat)
	{
		mat[0] = 1.0;
		mat[1] = 0.0;
		mat[2] = 0.0;
		mat[3] = 0.0;
		mat[4] = 0.0;
		mat[5] = 1.0;
		mat[6] = 0.0;
		mat[7] = 0.0;
		mat[8] = 0.0;
		mat[9] = 0.0;
		mat[10] = 1.0;
		mat[11] = 0.0;
		mat[12] = 0.0;
		mat[13] = 0.0;
		mat[14] = 0.0;
		mat[15] = 1.0;
	}

	static final public void buildPerspectiveMatrix(
		double[] m,
		double fovy,
		double aspect,
		double zNear,
		double zFar)
	{
		double xmin, xmax, ymin, ymax;

		ymax = zNear * Math.tan(fovy * Math.PI / 360.0);
		ymin = -ymax;

		xmin = ymin * aspect;
		xmax = ymax * aspect;

		buildFrustumMatrix(m, xmin, xmax, ymin, ymax, zNear, zFar);
	}
	static final public void buildFrustumMatrix(
		double[] m,
		double l,
		double r,
		double b,
		double t,
		double n,
		double f)
	{
		m[0] = (2.0 * n) / (r - l);
		m[1] = 0.0;
		m[2] = 0.0;
		m[3] = 0.0;

		m[4] = 0.0;
		m[5] = (2.0 * n) / (t - b);
		m[6] = 0.0;
		m[7] = 0.0;

		m[8] = (r + l) / (r - l);
		m[9] = (t + b) / (t - b);
		m[10] = - (f + n) / (f - n);
		m[11] = -1.0;

		m[12] = 0.0;
		m[13] = 0.0;
		m[14] = - (2.0 * f * n) / (f - n);
		m[15] = 0.0;
	}

	static final public void buildOrthoMatrix(
		double[] m,
		double l,
		double r,
		double b,
		double t,
		double n,
		double f)
	{
		m[0] = 2.0 / (r - l);
		m[1] = 0.0;
		m[2] = 0.0;
		m[3] = 0.0;

		m[4] = 0.0;
		m[5] = 2.0 / (t - b);
		m[6] = 0.0;
		m[7] = 0.0;

		m[8] = 0.0;
		m[9] = 0.0;
		m[10] = -2.0 / (f - n);
		m[11] = 0.0;

		m[12] = - (r + l) / (r - l);
		m[13] = - (t + b) / (t - b);
		m[14] = - (f + n) / (f - n);
		m[15] = 1.0;
	}

	/* Build a 4x4 matrix transform based on the parameters for gluLookAt.
	 * Code lifted from Brian Paul's MesaGLU.
	 */
	static final public void buildLookAtMatrix(
		double[] m,
		double eyex,
		double eyey,
		double eyez,
		double centerx,
		double centery,
		double centerz,
		double upx,
		double upy,
		double upz)
	{
		double mag;

		/* Make rotation matrix */

		/* Z vector */
		z[0] = eyex - centerx;
		z[1] = eyey - centery;
		z[2] = eyez - centerz;
		mag = Math.sqrt(z[0] * z[0] + z[1] * z[1] + z[2] * z[2]);
		if (mag > 0.0)
		{ /* mpichler, 19950515 */
			z[0] /= mag;
			z[1] /= mag;
			z[2] /= mag;
		}

		/* Y vector */
		y[0] = upx;
		y[1] = upy;
		y[2] = upz;

		/* X vector = Y cross Z */
		x[0] = y[1] * z[2] - y[2] * z[1];
		x[1] = -y[0] * z[2] + y[2] * z[0];
		x[2] = y[0] * z[1] - y[1] * z[0];

		/* Recompute Y = Z cross X */
		y[0] = z[1] * x[2] - z[2] * x[1];
		y[1] = -z[0] * x[2] + z[2] * x[0];
		y[2] = z[0] * x[1] - z[1] * x[0];

		/* mpichler, 19950515 */
		/* cross product gives area of parallelogram, which is < 1.0 for
			* non-perpendicular unit-length vectors; so normalize x, y here
			*/

		mag = Math.sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
		if (mag > 0.0)
		{
			x[0] /= mag;
			x[1] /= mag;
			x[2] /= mag;
		}

		mag = Math.sqrt(y[0] * y[0] + y[1] * y[1] + y[2] * y[2]);
		if (mag > 0.0)
		{
			y[0] /= mag;
			y[1] /= mag;
			y[2] /= mag;
		}

		//	  #define M(row,col)  m[col*4+row]
		m[0] = x[0];
		m[4] = x[1];
		m[8] = x[2];
		m[12] = -x[0] * eyex + -x[1] * eyey + -x[2] * eyez;

		m[1] = y[0];
		m[5] = y[1];
		m[9] = y[2];
		m[13] = -y[0] * eyex + -y[1] * eyey + -y[2] * eyez;

		m[2] = z[0];
		m[6] = z[1];
		m[10] = z[2];
		m[14] = -z[0] * eyex + -z[1] * eyey + -z[2] * eyez;

		m[3] = 0.0;
		m[7] = 0.0;
		m[11] = 0.0;
		m[15] = 1.0;
	}

//	TODO unroll this mess of loops and tmp local variables
	/* dst = a * b */
	
	static final public void multMatrices(double[] dst, double[] a, double[] b)
	{
		/*
		int i, j;

		for (i = 0; i < 4; i++)
		{
			for (j = 0; j < 4; j++)
			{
				dst[i * 4 + j] =
					  b[i * 4 + 0] * a[0 + j] 
					+ b[i * 4 + 1] * a[4 + j]
					+ b[i * 4 + 2] * a[8 + j]
					+ b[i * 4 + 3] * a[12 + j];
			}
		}
		*/
		//dst[i * 4 + j]
		
		dst[0] = b[0]*a[0] + b[1]*a[4] + b[2]*a[8] + b[3]*a[12];
		dst[1] = b[0]*a[1] + b[1]*a[5] + b[2]*a[9] + b[3]*a[13];
		dst[2] = b[0]*a[2] + b[1]*a[6] + b[2]*a[10] + b[3]*a[14];
		dst[3] = b[0]*a[3] + b[1]*a[7] + b[2]*a[11] + b[3]*a[15];

		dst[4] = b[4]*a[0] + b[5]*a[4] + b[6]*a[8] + b[7]*a[12];
		dst[5] = b[4]*a[1] + b[5]*a[5] + b[6]*a[9] + b[7]*a[13];
		dst[6] = b[4]*a[2] + b[5]*a[6] + b[6]*a[10] + b[7]*a[14];
		dst[7] = b[4]*a[3] + b[5]*a[7] + b[6]*a[11] + b[7]*a[15];

		dst[8] = b[8]*a[0] + b[9]*a[4] + b[10]*a[8] + b[11]*a[12];
		dst[9] = b[8]*a[1] + b[9]*a[5] + b[10]*a[9] + b[11]*a[13];
		dst[10] = b[8]*a[2] + b[9]*a[6] + b[10]*a[10] + b[11]*a[14];
		dst[11] = b[8]*a[3] + b[9]*a[7] + b[10]*a[11] + b[11]*a[15];

		dst[12] = b[12]*a[0] + b[13]*a[4] + b[14]*a[8] + b[15]*a[12];
		dst[13] = b[12]*a[1] + b[13]*a[5] + b[14]*a[9] + b[15]*a[13];
		dst[14] = b[12]*a[2] + b[13]*a[6] + b[14]*a[10] + b[15]*a[14];
		dst[15] = b[12]*a[3] + b[13]*a[7] + b[14]*a[11] + b[15]*a[15];
		
	}
	
	static final public void multMatrices(float[] dst, float[] a, float[] b)
	{
		/*
		int i, j;

		for (i = 0; i < 4; i++)
		{
			for (j = 0; j < 4; j++)
			{
				dst[i * 4 + j] =
					b[i * 4 + 0] * a[0 + j] 
				  + b[i * 4 + 1] * a[4 + j]
				  + b[i * 4 + 2] * a[8 + j]
				  + b[i * 4 + 3] * a[12 + j];
			}
		}
		*/
		dst[0] = b[0]*a[0] + b[1]*a[4] + b[2]*a[8] + b[3]*a[12];
		dst[1] = b[0]*a[1] + b[1]*a[5] + b[2]*a[9] + b[3]*a[13];
		dst[2] = b[0]*a[2] + b[1]*a[6] + b[2]*a[10] + b[3]*a[14];
		dst[3] = b[0]*a[3] + b[1]*a[7] + b[2]*a[11] + b[3]*a[15];

		dst[4] = b[4]*a[0] + b[5]*a[4] + b[6]*a[8] + b[7]*a[12];
		dst[5] = b[4]*a[1] + b[5]*a[5] + b[6]*a[9] + b[7]*a[13];
		dst[6] = b[4]*a[2] + b[5]*a[6] + b[6]*a[10] + b[7]*a[14];
		dst[7] = b[4]*a[3] + b[5]*a[7] + b[6]*a[11] + b[7]*a[15];

		dst[8] = b[8]*a[0] + b[9]*a[4] + b[10]*a[8] + b[11]*a[12];
		dst[9] = b[8]*a[1] + b[9]*a[5] + b[10]*a[9] + b[11]*a[13];
		dst[10] = b[8]*a[2] + b[9]*a[6] + b[10]*a[10] + b[11]*a[14];
		dst[11] = b[8]*a[3] + b[9]*a[7] + b[10]*a[11] + b[11]*a[15];

		dst[12] = b[12]*a[0] + b[13]*a[4] + b[14]*a[8] + b[15]*a[12];
		dst[13] = b[12]*a[1] + b[13]*a[5] + b[14]*a[9] + b[15]*a[13];
		dst[14] = b[12]*a[2] + b[13]*a[6] + b[14]*a[10] + b[15]*a[14];
		dst[15] = b[12]*a[3] + b[13]*a[7] + b[14]*a[11] + b[15]*a[15];
	}

	static final public void copyMatrix(double[] dst, double[] src)
	{
		dst[0] = src[0];
		dst[1] = src[1];
		dst[2] = src[2];
		dst[3] = src[3];
		dst[4] = src[4];
		dst[5] = src[5];
		dst[6] = src[6];
		dst[7] = src[7];
		dst[8] = src[8];
		dst[9] = src[9];
		dst[10] = src[10];
		dst[11] = src[11];
		dst[12] = src[12];
		dst[13] = src[13];
		dst[14] = src[14];
		dst[15] = src[15];

/* kept here because it may actually be faster on
 * optimization VMs because the VM can unroll the loop
 * and remove bound checking that it may stil be doing
 * in a set of inlined access/assigns like above.
		int i;

		for (i = 0; i < 16; i++)
		{
			dst[i] = src[i];
		}
*/
	}

	static final public void copyMatrix(float[] dst, float[] src)
	{
		dst[0] = src[0];
		dst[1] = src[1];
		dst[2] = src[2];
		dst[3] = src[3];
		dst[4] = src[4];
		dst[5] = src[5];
		dst[6] = src[6];
		dst[7] = src[7];
		dst[8] = src[8];
		dst[9] = src[9];
		dst[10] = src[10];
		dst[11] = src[11];
		dst[12] = src[12];
		dst[13] = src[13];
		dst[14] = src[14];
		dst[15] = src[15];

/* kept here because it may actually be faster on
 * optimization VMs because the VM can unroll the loop
 * and remove bound checking that it may stil be doing
 * in a set of inlined access/assigns like above.
		int i;

 		for (i = 0; i < 16; i++)
		{
			dst[i] = src[i];
		}
*/
	}
	
	static final public void copyMatrix(float[] dst, double[] src)
	{
		dst[0] = (float)src[0];
		dst[1] = (float)src[1];
		dst[2] = (float)src[2];
		dst[3] = (float)src[3];
		dst[4] = (float)src[4];
		dst[5] = (float)src[5];
		dst[6] = (float)src[6];
		dst[7] = (float)src[7];
		dst[8] = (float)src[8];
		dst[9] = (float)src[9];
		dst[10] = (float)src[10];
		dst[11] = (float)src[11];
		dst[12] = (float)src[12];
		dst[13] = (float)src[13];
		dst[14] = (float)src[14];
		dst[15] = (float)src[15];
	}

	static final public void copyMatrix(double[] dst, float[] src)
	{
		dst[0] = src[0];
		dst[1] = src[1];
		dst[2] = src[2];
		dst[3] = src[3];
		dst[4] = src[4];
		dst[5] = src[5];
		dst[6] = src[6];
		dst[7] = src[7];
		dst[8] = src[8];
		dst[9] = src[9];
		dst[10] = src[10];
		dst[11] = src[11];
		dst[12] = src[12];
		dst[13] = src[13];
		dst[14] = src[14];
		dst[15] = src[15];
	}

	static final public void transposeZero(float[] mat)
	{
		float tmp;
		//mat[0] = 1.0f;
		tmp = mat[1];
		mat[1] = mat[4];
		mat[4] = tmp;
		
		tmp = mat[2];
		mat[2] = mat[8];
		mat[8] = tmp;

		tmp = mat[6];
		mat[6] = mat[9];
		mat[9] = tmp;

		tmp = mat[1];
		//mat[5] = 1.0f;
		//mat[10] = 1.0f;
		mat[3] = 0.0f;
		mat[7] = 0.0f;
		mat[11] = 0.0f;
		mat[12] = 0.0f;
		mat[13] = 0.0f;
		mat[14] = 0.0f;
		mat[15] = 1.0f;
	}
	static final public void transposeZero(double[] mat)
	{
		double tmp;
		//mat[0] = 1.0f;
		tmp = mat[1];
		mat[1] = mat[4];
		mat[4] = tmp;
		
		tmp = mat[2];
		mat[2] = mat[8];
		mat[8] = tmp;

		tmp = mat[6];
		mat[6] = mat[9];
		mat[9] = tmp;

		//tmp = mat[1];
		//mat[5] = 1.0f;
		//mat[10] = 1.0f;
		mat[3] = -mat[3];
		mat[7] = -mat[7];
		mat[11] = -mat[11];
		mat[12] = 0;//-mat[12];
		mat[13] = 0;//-mat[13];
		mat[14] = 0;//-mat[14];
		mat[15] = 1.0f;
}

 /**
  * Sets the value of this matrix to its inverse.
  */
 static final public void invert(double[] dst, double[] src)
 {
 double s = determinant(src);
 if (s == 0.0)
	 return;
 s = 1/s;
 // alias-safe way.
 // less *,+,- calculation than expanded expression.
// set(
   dst[0] =   src[5]*(src[10]*src[15] - src[14]*src[11]) + src[9]*(src[14]*src[7] - src[6]*src[15]) + src[13]*(src[6]*src[11] - src[10]*src[7]);
   dst[4] =   src[6]*(src[8]*src[15] - src[12]*src[11]) + src[10]*(src[12]*src[7] - src[4]*src[15]) + src[14]*(src[4]*src[11] - src[8]*src[7]);
   dst[8] =src[7]*(src[8]*src[13] - src[12]*src[9]) + src[11]*(src[12]*src[5] - src[4]*src[13]) + src[15]*(src[4]*src[9] - src[8]*src[5]);
   dst[12] =src[4]*(src[13]*src[10] - src[9]*src[14]) + src[8]*(src[5]*src[14] - src[13]*src[6]) + src[12]*(src[9]*src[6] - src[5]*src[10]);

   dst[1] =src[9]*(src[2]*src[15] - src[14]*src[3]) + src[13]*(src[10]*src[3] - src[2]*src[11]) + src[1]*(src[14]*src[11] - src[10]*src[15]);
   dst[5] =src[10]*(src[0]*src[15] - src[12]*src[3]) + src[14]*(src[8]*src[3] - src[0]*src[11]) + src[2]*(src[12]*src[11] - src[8]*src[15]);
   dst[9] =src[11]*(src[0]*src[13] - src[12]*src[1]) + src[15]*(src[8]*src[1] - src[0]*src[9]) + src[3]*(src[12]*src[9] - src[8]*src[13]);
   dst[13] =src[8]*(src[13]*src[2] - src[1]*src[14]) + src[12]*(src[1]*src[10] - src[9]*src[2]) + src[0]*(src[9]*src[14] - src[13]*src[10]);

   dst[2] =src[13]*(src[2]*src[7] - src[6]*src[3]) + src[1]*(src[6]*src[15] - src[14]*src[7]) + src[5]*(src[14]*src[3] - src[2]*src[15]);
   dst[6] =src[14]*(src[0]*src[7] - src[4]*src[3]) + src[2]*(src[4]*src[15] - src[12]*src[7]) + src[6]*(src[12]*src[3] - src[0]*src[15]);
   dst[10] =src[15]*(src[0]*src[5] - src[4]*src[1]) + src[3]*(src[4]*src[13] - src[12]*src[5]) + src[7]*(src[12]*src[1] - src[0]*src[13]);
   dst[14] =src[12]*(src[5]*src[2] - src[1]*src[6]) + src[0]*(src[13]*src[6] - src[5]*src[14]) + src[4]*(src[1]*src[14] - src[13]*src[2]);

   dst[3] = src[1]*(src[10]*src[7] - src[6]*src[11]) + src[5]*(src[2]*src[11] - src[10]*src[3]) + src[9]*(src[6]*src[3] - src[2]*src[7]);
   dst[7] =src[2]*(src[8]*src[7] - src[4]*src[11]) + src[6]*(src[0]*src[11] - src[8]*src[3]) + src[10]*(src[4]*src[3] - src[0]*src[7]);
   dst[11] =src[3]*(src[8]*src[5] - src[4]*src[9]) + src[7]*(src[0]*src[9] - src[8]*src[1]) + src[11]*(src[4]*src[1] - src[0]*src[5]);
   dst[15] =src[0]*(src[5]*src[10] - src[9]*src[6]) + src[4]*(src[9]*src[2] - src[1]*src[10]) + src[8]*(src[1]*src[6] - src[5]*src[2]);
//	 );

 mul(dst, s);
 }

 /**
  * Computes the determinant of this matrix. 
  * @return the determinant of the matrix 
  */
 static final public double determinant(double[] mat)  {
 // less *,+,- calculation than expanded expression.
 return
	 (mat[0]*mat[5] - mat[4]*mat[1])*(mat[10]*mat[15] - mat[14]*mat[11])
	-(mat[0]*mat[9] - mat[8]*mat[1])*(mat[6]*mat[15] - mat[14]*mat[7])
	+(mat[0]*mat[13] - mat[12]*mat[1])*(mat[6]*mat[11] - mat[10]*mat[7])
	+(mat[4]*mat[9] - mat[8]*mat[5])*(mat[2]*mat[15] - mat[14]*mat[3])
	-(mat[4]*mat[13] - mat[12]*mat[5])*(mat[2]*mat[11] - mat[10]*mat[3])
	+(mat[8]*mat[13] - mat[12]*mat[9])*(mat[2]*mat[7] - mat[6]*mat[3]);

 }

 /**
	* Multiplies each element of this matrix by a scalar.
	* @param scalar The scalar multiplier.
	*/
  static final public void mul(double[] mat, double scalar) {
  mat[0] *= scalar; mat[1] *= scalar;  mat[2] *= scalar; mat[3] *= scalar;
  mat[4] *= scalar; mat[5] *= scalar;  mat[6] *= scalar; mat[7] *= scalar;
  mat[8] *= scalar; mat[9] *= scalar;  mat[10] *= scalar; mat[11] *= scalar;
  mat[12] *= scalar; mat[13] *= scalar;  mat[14] *= scalar; mat[15] *= scalar;
/*
  m00 *= scalar; m01 *= scalar;  m02 *= scalar; m03 *= scalar;
  m10 *= scalar; m11 *= scalar;  m12 *= scalar; m13 *= scalar;
  m20 *= scalar; m21 *= scalar;  m22 *= scalar; m23 *= scalar;
  m30 *= scalar; m31 *= scalar;  m32 *= scalar; m33 *= scalar;
*/
      }

	static final public void scale(double[] mat, double scalar) {
	mat[0] *= scalar; mat[1] *= scalar;  mat[2] *= scalar; 
	mat[4] *= scalar; mat[5] *= scalar;  mat[6] *= scalar; 
	mat[8] *= scalar; mat[9] *= scalar;  mat[10] *= scalar; 
	}
	static final public void scale(float[] mat, float scalar) {
	mat[0] *= scalar; mat[1] *= scalar;  mat[2] *= scalar; 
	mat[4] *= scalar; mat[5] *= scalar;  mat[6] *= scalar; 
	mat[8] *= scalar; mat[9] *= scalar;  mat[10] *= scalar; 
	}


static final public float transformZ(double[] mat, double x, double y, double z) 
{
	return (float)(mat[2]*x + mat[6]*y + mat[10]*z + mat[14]);
}


static public final void rotX(float[] mat, float f)
 {
	 float f1 = MathUtils.sin(f);//(float)Math.sin(f);
	 float f2 = MathUtils.cos(f);//;(float)Math.cos(f);

	mat[0] = 1.0F;
	mat[1] = 0.0F;
	mat[2] = 0.0F;
	mat[3] = 0.0F;
	mat[4] = 0.0F;
	mat[5] = f2;
	mat[6] = -f1;
	mat[7] = 0.0F;
	mat[8] = 0.0F;
	mat[9] = f1;
	mat[10] = f2;
	mat[11] = 0.0F;
	mat[12] = 0.0F;
	mat[13] = 0.0F;
	mat[14] = 0.0F;
	mat[15] = 1.0F;
 }
 
 
  static public final void rotY(float[] mat, float f)
   {
	   float f1 = MathUtils.sin(f);//(float)Math.sin(f);
	   float f2 = MathUtils.cos(f);//;(float)Math.cos(f);

	  mat[0] = f2;
	  mat[1] = 0.0F;
	  mat[2] = -f1;
	  mat[3] = 0.0F;
	  mat[4] = 0.0F;
	  mat[5] = 1.0F;
	  mat[6] = 0.0f;
	  mat[7] = 0.0f;
	  mat[8] = f1;
	  mat[9] = 0.0f;
	  mat[10] = f2;
	  mat[11] = 0.0F;
	  mat[12] = 0.0F;
	  mat[13] = 0.0F;
	  mat[14] = 0.0F;
	  mat[15] = 1.0F;
   }
}

