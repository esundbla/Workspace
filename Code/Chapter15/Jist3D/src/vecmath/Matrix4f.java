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
 * A single precision floating point 4 by 4 matrix. Primarily to support 3D transformations.
 * @author Shawn Kendall
 */
final public class Matrix4f implements Cloneable, java.io.Serializable
{
	public float m00;
	public float m01;
	public float m02;
	public float m03;
	public float m10;
	public float m11;
	public float m12;
	public float m13;
	public float m20;
	public float m21;
	public float m22;
	public float m23;
	public float m30;
	public float m31;
	public float m32;
	public float m33;

	/**
	 * Constructs and initializes a Matrix4f to all zeros.
	 */
	public Matrix4f()
	{
		m00 = 0.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 0.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 0.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 0.0f;
	}

	/**
	 * Constructs and initializes a Matrix4f from the specified 16 values.
	 * @param mat00 - the [0][0] element
	 * @param mat01 - the [0][1] element
	 * @param mat02 - the [0][2] element
	 * @param mat03 - the [0][3] element
	 * @param mat10 - the [1][0] element
	 * @param mat11 - the [1][1] element
	 * @param mat12 - the [1][2] element
	 * @param mat13 - the [1][3] element
	 * @param mat20 - the [2][0] element
	 * @param mat21 - the [2][1] element
	 * @param mat22 - the [2][2] element
	 * @param mat23 - the [2][3] element
	 * @param mat30 - the [3][0] element
	 * @param mat31 - the [3][1] element
	 * @param mat32 - the [3][2] element
	 * @param mat33 - the [3][3] element
	 */
	public Matrix4f(
		float mat00,
		float mat01,
		float mat02,
		float mat03,
		float mat10,
		float mat11,
		float mat12,
		float mat13,
		float mat20,
		float mat21,
		float mat22,
		float mat23,
		float mat30,
		float mat31,
		float mat32,
		float mat33)
	{
		m00 = mat00;
		m01 = mat01;
		m02 = mat02;
		m03 = mat03;
		m10 = mat10;
		m11 = mat11;
		m12 = mat12;
		m13 = mat13;
		m20 = mat20;
		m21 = mat21;
		m22 = mat22;
		m23 = mat23;
		m30 = mat30;
		m31 = mat31;
		m32 = mat32;
		m33 = mat33;
	}

	/**
	 * Constructs and initializes a Matrix4f from the specified 16 element array. this.m00 =v[0], this.m01=v[1], etc.
	 * @param mat - the array of length 16 containing in order
	 */
	public Matrix4f(float mat[])
	{
		m00 = mat[0];
		m01 = mat[1];
		m02 = mat[2];
		m03 = mat[3];
		m10 = mat[4];
		m11 = mat[5];
		m12 = mat[6];
		m13 = mat[7];
		m20 = mat[8];
		m21 = mat[9];
		m22 = mat[10];
		m23 = mat[11];
		m30 = mat[12];
		m31 = mat[13];
		m32 = mat[14];
		m33 = mat[15];
	}

	/**
	 * Constructs and initializes a Matrix4f from the rotation matrix, translation, and scale values; the scale is applied only to the rotational components of the matrix (upper 3x3) and not to the translational components of the matrix.
	 * @param mat - the rotation matrix representing the rotational components
	 * @param vec - the translational components of the matrix
	 * @param scale - the scale value applied to the rotational components
	 */
	public Matrix4f(Matrix3f mat, Vector3f vec, float scale)
	{
		m00 = mat.m00 * scale;
		m01 = mat.m01 * scale;
		m02 = mat.m02 * scale;
		m03 = vec.x;
		m10 = mat.m10 * scale;
		m11 = mat.m11 * scale;
		m12 = mat.m12 * scale;
		m13 = vec.y;
		m20 = mat.m20 * scale;
		m21 = mat.m21 * scale;
		m22 = mat.m22 * scale;
		m23 = vec.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Constructs a new matrix with the same values as the Matrix4f parameter.
	 * @param mat - the source matrix
	 */
	public Matrix4f(Matrix4f mat)
	{
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = mat.m03;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = mat.m13;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = mat.m23;
		m30 = mat.m30;
		m31 = mat.m31;
		m32 = mat.m32;
		m33 = mat.m33;
	}

	/**
	 * Constructs and initializes a Matrix4f from the quaternion, translation, and scale values; the scale is applied only to the rotational components of the matrix (upper 3x3) and not to the translational components.
	 * Implemented as defined in The Matrix and Quaternions FAQ Version 1.19  20th March 2002
	 * @param quat - the quaternion value representing the rotational component
	 * @param vector3f - the translational component of the matrix
	 * @param scale - the scale value applied to the rotational components
	 */
	public Matrix4f(Quat4f quat, Vector3f vector3f, float scale)
	{
		float xx = quat.x * quat.x;
		float xy = quat.x * quat.y;
		float xz = quat.x * quat.z;
		float xw = quat.x * quat.w;

		float yy = quat.y * quat.y;
		float yz = quat.y * quat.z;
		float yw = quat.y * quat.w;

		float zz = quat.z * quat.z;
		float zw = quat.z * quat.w;

		m00 = scale * (1 - 2 * (yy + zz));
		m01 = scale * (2 * (xy - zw));
		m02 = scale * (2 * (xz + yw));

		m10 = scale * (2 * (xy + zw));
		m11 = scale * (1 - 2 * (xx + zz));
		m12 = scale * (2 * (yz - xw));

		m20 = scale * (2 * (xz - yw));
		m21 = scale * (2 * (yz + xw));
		m22 = scale * (1 - 2 * (xx + yy));

		m03 = vector3f.x;
		m13 = vector3f.y;
		m23 = vector3f.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Adds a scalar to each component of this matrix.
	 * @param scalar
	 */
	public final void add(float scalar)
	{
		m00 += scalar;
		m01 += scalar;
		m02 += scalar;
		m03 += scalar;
		m10 += scalar;
		m11 += scalar;
		m12 += scalar;
		m13 += scalar;
		m20 += scalar;
		m21 += scalar;
		m22 += scalar;
		m23 += scalar;
		m30 += scalar;
		m31 += scalar;
		m32 += scalar;
		m33 += scalar;
	}

	/**
	 * Adds a scalar to each component of the matrix mat and places the result into this.
	 * @param scalar
	 * @param mat
	 */
	public final void add(float scalar, Matrix4f mat)
	{
		m00 = mat.m00 + scalar;
		m01 = mat.m01 + scalar;
		m02 = mat.m02 + scalar;
		m03 = mat.m03 + scalar;
		m10 = mat.m10 + scalar;
		m11 = mat.m11 + scalar;
		m12 = mat.m12 + scalar;
		m13 = mat.m13 + scalar;
		m20 = mat.m20 + scalar;
		m21 = mat.m21 + scalar;
		m22 = mat.m22 + scalar;
		m23 = mat.m23 + scalar;
		m30 = mat.m30 + scalar;
		m31 = mat.m31 + scalar;
		m32 = mat.m32 + scalar;
		m33 = mat.m33 + scalar;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix mat.
	 * @param mat
	 */
	public final void add(Matrix4f mat)
	{
		m00 += mat.m00;
		m01 += mat.m01;
		m02 += mat.m02;
		m03 += mat.m03;
		m10 += mat.m10;
		m11 += mat.m11;
		m12 += mat.m12;
		m13 += mat.m13;
		m20 += mat.m20;
		m21 += mat.m21;
		m22 += mat.m22;
		m23 += mat.m23;
		m30 += mat.m30;
		m31 += mat.m31;
		m32 += mat.m32;
		m33 += mat.m33;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices mat1 and mat2.
	 * @param mat1
	 * @param mat2
	 */
	public final void add(Matrix4f mat1, Matrix4f mat2)
	{
		m00 = mat1.m00 + mat2.m00;
		m01 = mat1.m01 + mat2.m01;
		m02 = mat1.m02 + mat2.m02;
		m03 = mat1.m03 + mat2.m03;
		m10 = mat1.m10 + mat2.m10;
		m11 = mat1.m11 + mat2.m11;
		m12 = mat1.m12 + mat2.m12;
		m13 = mat1.m13 + mat2.m13;
		m20 = mat1.m20 + mat2.m20;
		m21 = mat1.m21 + mat2.m21;
		m22 = mat1.m22 + mat2.m22;
		m23 = mat1.m23 + mat2.m23;
		m30 = mat1.m30 + mat2.m30;
		m31 = mat1.m31 + mat2.m31;
		m32 = mat1.m32 + mat2.m32;
		m33 = mat1.m33 + mat2.m33;
	}

	/**
	 * Creates a new object of the same class as this object.
	 * @see Cloneable
	 * @return a clone of this instance.
	 * @throws java.lang.OutOfMemoryError - if there is not enough memory.
	 */
	public Object clone()
	{
		Matrix4f matrix4f = null;
		try
		{
			matrix4f = (Matrix4f)super.clone();
		}
		catch (CloneNotSupportedException clonenotsupportedexception)
		{
			throw new InternalError();
		}
		return matrix4f;
	}

	/**
	 * Computes the determinate of this matrix.
	 * @return the determinate of this matrix.
	 */
	public final float determinant()
	{
		float det =
			m00
				* ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
					- m13 * m22 * m31
					- m11 * m23 * m32
					- m12 * m21 * m33);
		det -= m01
			* ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
				- m13 * m22 * m30
				- m10 * m23 * m32
				- m12 * m20 * m33);
		det += m02
			* ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
				- m13 * m21 * m30
				- m10 * m23 * m31
				- m11 * m20 * m33);
		det -= m03
			* ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
				- m12 * m21 * m30
				- m10 * m22 * m31
				- m11 * m20 * m32);
		return det;
	}

	/**
	 * Returns true if the L-infinite distance between this matrix and matrix m1 is less than or equal to the epsilon parameter, otherwise returns false.
	 * @param mat
	 * @param epsilon
	 * @return true or false
	 */
	public boolean epsilonEquals(Matrix4f mat, float epsilon)
	{
		if (Math.abs(m00 - mat.m00) > epsilon)
			return false;
		if (Math.abs(m01 - mat.m01) > epsilon)
			return false;
		if (Math.abs(m02 - mat.m02) > epsilon)
			return false;
		if (Math.abs(m03 - mat.m03) > epsilon)
			return false;
		if (Math.abs(m10 - mat.m10) > epsilon)
			return false;
		if (Math.abs(m11 - mat.m11) > epsilon)
			return false;
		if (Math.abs(m12 - mat.m12) > epsilon)
			return false;
		if (Math.abs(m13 - mat.m13) > epsilon)
			return false;
		if (Math.abs(m20 - mat.m20) > epsilon)
			return false;
		if (Math.abs(m21 - mat.m21) > epsilon)
			return false;
		if (Math.abs(m22 - mat.m22) > epsilon)
			return false;
		if (Math.abs(m23 - mat.m23) > epsilon)
			return false;
		if (Math.abs(m30 - mat.m30) > epsilon)
			return false;
		if (Math.abs(m31 - mat.m31) > epsilon)
			return false;
		if (Math.abs(m32 - mat.m32) > epsilon)
			return false;
		if (Math.abs(m33 - mat.m33) > epsilon)
			return false;
		return true;
	}

	/**
	 * Returns true if all of the data members of Matrix4f mat are equal to the corresponding data members in this Matrix4f. 
	 * @param mat - the matrix with which the comparison is made. 
	 * @return true or false
	 */
	public boolean equals(Matrix4f mat)
	{
		return m00 == mat.m00
			&& m01 == mat.m01
			&& m02 == mat.m02
			&& m03 == mat.m03
			&& m10 == mat.m10
			&& m11 == mat.m11
			&& m12 == mat.m12
			&& m13 == mat.m13
			&& m20 == mat.m20
			&& m21 == mat.m21
			&& m22 == mat.m22
			&& m23 == mat.m23
			&& m30 == mat.m30
			&& m31 == mat.m31
			&& m32 == mat.m32
			&& m33 == mat.m33;
	}

	/**
	 * Returns true if the Object obj is of type Matrix4f and all of the data members of obj are equal to the corresponding data members in this Matrix4f. 
	 * @overrides equals in class java.lang.Object
	 * @param obj - the matrix with which the comparison is made. 
	 * @return true or false
	 */
	public boolean equals(Object obj)
	{
		Matrix4f mat = (Matrix4f)obj;
		return m00 == mat.m00
			&& m01 == mat.m01
			&& m02 == mat.m02
			&& m03 == mat.m03
			&& m10 == mat.m10
			&& m11 == mat.m11
			&& m12 == mat.m12
			&& m13 == mat.m13
			&& m20 == mat.m20
			&& m21 == mat.m21
			&& m22 == mat.m22
			&& m23 == mat.m23
			&& m30 == mat.m30
			&& m31 == mat.m31
			&& m32 == mat.m32
			&& m33 == mat.m33;
	}

	/**
	 * Retrieves the translational components of this matrix.
	 * @param vec - the vector that will receive the translational component
	 */
	public final void get(Vector3f vec)
	{
		vec.x = m03;
		vec.y = m13;
		vec.z = m23;
	}

	/**
	 * Gets the upper 3x3 values of this matrix and places them into the matrix mat.
	 * @param mat - the matrix that will hold the values
	 */
	public final void getRotationScale(Matrix3f mat)
	{
		mat.m00 = m00;
		mat.m01 = m01;
		mat.m02 = m02;
		mat.m10 = m10;
		mat.m11 = m11;
		mat.m12 = m12;
		mat.m20 = m20;
		mat.m21 = m21;
		mat.m22 = m22;
	}

	/**
	 * Multiplies each element of this matrix by a scalar.
	 * @param scalar - the scalar multiplier.
	 */
	public final void mul(float scalar)
	{
		m00 *= scalar;
		m01 *= scalar;
		m02 *= scalar;
		m03 *= scalar;
		m10 *= scalar;
		m11 *= scalar;
		m12 *= scalar;
		m13 *= scalar;
		m20 *= scalar;
		m21 *= scalar;
		m22 *= scalar;
		m23 *= scalar;
		m30 *= scalar;
		m31 *= scalar;
		m32 *= scalar;
		m33 *= scalar;
	}

	/**
	 * Multiplies each element of matrix mat by a scalar and places the result into this. Matrix mat is not modified. 
	 * @param scalar - the scalar multiplier.
	 * @param mat - the original matrix.
	 */
	public final void mul(float scalar, Matrix4f mat)
	{
		m00 = mat.m00 * scalar;
		m01 = mat.m01 * scalar;
		m02 = mat.m02 * scalar;
		m03 = mat.m03 * scalar;
		m10 = mat.m10 * scalar;
		m11 = mat.m11 * scalar;
		m12 = mat.m12 * scalar;
		m13 = mat.m13 * scalar;
		m20 = mat.m20 * scalar;
		m21 = mat.m21 * scalar;
		m22 = mat.m22 * scalar;
		m23 = mat.m23 * scalar;
		m30 = mat.m30 * scalar;
		m31 = mat.m31 * scalar;
		m32 = mat.m32 * scalar;
		m33 = mat.m33 * scalar;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself with matrix mat. 
	 * @param mat - the other matrix
	 */
	public final void mul(Matrix4f mat)
	{
		float tmp1 = m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20 + m03 * mat.m30;
		float tmp2 = m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21 + m03 * mat.m31;
		float tmp3 = m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22 + m03 * mat.m32;
		float tmp4 = m00 * mat.m03 + m01 * mat.m13 + m02 * mat.m23 + m03 * mat.m33;

		float tmp5 = m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20 + m13 * mat.m30;
		float tmp6 = m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21 + m13 * mat.m31;
		float tmp7 = m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22 + m13 * mat.m32;
		float tmp8 = m10 * mat.m03 + m11 * mat.m13 + m12 * mat.m23 + m13 * mat.m33;

		float tmp9 = m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20 + m23 * mat.m30;
		float tmp10 = m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21 + m23 * mat.m31;
		float tmp11 = m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22 + m23 * mat.m32;
		float tmp12 = m20 * mat.m03 + m21 * mat.m13 + m22 * mat.m23 + m23 * mat.m33;

		float tmp13 = m30 * mat.m00 + m31 * mat.m10 + m32 * mat.m20 + m33 * mat.m30;
		float tmp14 = m30 * mat.m01 + m31 * mat.m11 + m32 * mat.m21 + m33 * mat.m31;
		float tmp15 = m30 * mat.m02 + m31 * mat.m12 + m32 * mat.m22 + m33 * mat.m32;
		float tmp16 = m30 * mat.m03 + m31 * mat.m13 + m32 * mat.m23 + m33 * mat.m33;

		m00 = tmp1;
		m01 = tmp2;
		m02 = tmp3;
		m03 = tmp4;
		m10 = tmp5;
		m11 = tmp6;
		m12 = tmp7;
		m13 = tmp8;
		m20 = tmp9;
		m21 = tmp10;
		m22 = tmp11;
		m23 = tmp12;
		m30 = tmp13;
		m31 = tmp14;
		m32 = tmp15;
		m33 = tmp16;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying the two argument matrices together.
	 * @param mat1 - the first matrix
	 * @param mat2 - the second matrix
	 */
	public final void mul(Matrix4f mat1, Matrix4f mat2)
	{
		// very nasty errors if this check is not made
		// comment in if your usage could do this
		//if(this != mat1 && this != mat2)
		//{
		m00 = mat1.m00 * mat2.m00 + mat1.m01 * mat2.m10 + mat1.m02 * mat2.m20 + mat1.m03 * mat2.m30;
		m01 = mat1.m00 * mat2.m01 + mat1.m01 * mat2.m11 + mat1.m02 * mat2.m21 + mat1.m03 * mat2.m31;
		m02 = mat1.m00 * mat2.m02 + mat1.m01 * mat2.m12 + mat1.m02 * mat2.m22 + mat1.m03 * mat2.m32;
		m03 = mat1.m00 * mat2.m03 + mat1.m01 * mat2.m13 + mat1.m02 * mat2.m23 + mat1.m03 * mat2.m33;

		m10 = mat1.m10 * mat2.m00 + mat1.m11 * mat2.m10 + mat1.m12 * mat2.m20 + mat1.m13 * mat2.m30;
		m11 = mat1.m10 * mat2.m01 + mat1.m11 * mat2.m11 + mat1.m12 * mat2.m21 + mat1.m13 * mat2.m31;
		m12 = mat1.m10 * mat2.m02 + mat1.m11 * mat2.m12 + mat1.m12 * mat2.m22 + mat1.m13 * mat2.m32;
		m13 = mat1.m10 * mat2.m03 + mat1.m11 * mat2.m13 + mat1.m12 * mat2.m23 + mat1.m13 * mat2.m33;

		m20 = mat1.m20 * mat2.m00 + mat1.m21 * mat2.m10 + mat1.m22 * mat2.m20 + mat1.m23 * mat2.m30;
		m21 = mat1.m20 * mat2.m01 + mat1.m21 * mat2.m11 + mat1.m22 * mat2.m21 + mat1.m23 * mat2.m31;
		m22 = mat1.m20 * mat2.m02 + mat1.m21 * mat2.m12 + mat1.m22 * mat2.m22 + mat1.m23 * mat2.m32;
		m23 = mat1.m20 * mat2.m03 + mat1.m21 * mat2.m13 + mat1.m22 * mat2.m23 + mat1.m23 * mat2.m33;

		m30 = mat1.m30 * mat2.m00 + mat1.m31 * mat2.m10 + mat1.m32 * mat2.m20 + mat1.m33 * mat2.m30;
		m31 = mat1.m30 * mat2.m01 + mat1.m31 * mat2.m11 + mat1.m32 * mat2.m21 + mat1.m33 * mat2.m31;
		m32 = mat1.m30 * mat2.m02 + mat1.m31 * mat2.m12 + mat1.m32 * mat2.m22 + mat1.m33 * mat2.m32;
		m33 = mat1.m30 * mat2.m03 + mat1.m31 * mat2.m13 + mat1.m32 * mat2.m23 + mat1.m33 * mat2.m33;
		/* 
		} 
		else
		{
		    float tmp1 = mat1.m00 * mat2.m00 + mat1.m01 * mat2.m10 + mat1.m02 * mat2.m20 + mat1.m03 * mat2.m30;
		    float tmp2 = mat1.m00 * mat2.m01 + mat1.m01 * mat2.m11 + mat1.m02 * mat2.m21 + mat1.m03 * mat2.m31;
		    float tmp3 = mat1.m00 * mat2.m02 + mat1.m01 * mat2.m12 + mat1.m02 * mat2.m22 + mat1.m03 * mat2.m32;
		    float tmp4 = mat1.m00 * mat2.m03 + mat1.m01 * mat2.m13 + mat1.m02 * mat2.m23 + mat1.m03 * mat2.m33;
		    float tmp5 = mat1.m10 * mat2.m00 + mat1.m11 * mat2.m10 + mat1.m12 * mat2.m20 + mat1.m13 * mat2.m30;
		    float tmp6 = mat1.m10 * mat2.m01 + mat1.m11 * mat2.m11 + mat1.m12 * mat2.m21 + mat1.m13 * mat2.m31;
		    float tmp7 = mat1.m10 * mat2.m02 + mat1.m11 * mat2.m12 + mat1.m12 * mat2.m22 + mat1.m13 * mat2.m32;
		    float tmp8 = mat1.m10 * mat2.m03 + mat1.m11 * mat2.m13 + mat1.m12 * mat2.m23 + mat1.m13 * mat2.m33;
		    float tmp9 = mat1.m20 * mat2.m00 + mat1.m21 * mat2.m10 + mat1.m22 * mat2.m20 + mat1.m23 * mat2.m30;
		    float tmp10 = mat1.m20 * mat2.m01 + mat1.m21 * mat2.m11 + mat1.m22 * mat2.m21 + mat1.m23 * mat2.m31;
		    float tmp11 = mat1.m20 * mat2.m02 + mat1.m21 * mat2.m12 + mat1.m22 * mat2.m22 + mat1.m23 * mat2.m32;
		    float tmp12 = mat1.m20 * mat2.m03 + mat1.m21 * mat2.m13 + mat1.m22 * mat2.m23 + mat1.m23 * mat2.m33;
		    float tmp13 = mat1.m30 * mat2.m00 + mat1.m31 * mat2.m10 + mat1.m32 * mat2.m20 + mat1.m33 * mat2.m30;
		    float tmp14 = mat1.m30 * mat2.m01 + mat1.m31 * mat2.m11 + mat1.m32 * mat2.m21 + mat1.m33 * mat2.m31;
		    float tmp15 = mat1.m30 * mat2.m02 + mat1.m31 * mat2.m12 + mat1.m32 * mat2.m22 + mat1.m33 * mat2.m32;
		    float tmp16 = mat1.m30 * mat2.m03 + mat1.m31 * mat2.m13 + mat1.m32 * mat2.m23 + mat1.m33 * mat2.m33;
		    m00 = tmp1;
		    m01 = tmp2;
		    m02 = tmp3;
		    m03 = tmp4;
		    m10 = tmp5;
		    m11 = tmp6;
		    m12 = tmp7;
		    m13 = tmp8;
		    m20 = tmp9;
		    m21 = tmp10;
		    m22 = tmp11;
		    m23 = tmp12;
		    m30 = tmp13;
		    m31 = tmp14;
		    m32 = tmp15;
		    m33 = tmp16;
		}
		*/
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate()
	{
		m00 = -m00;
		m01 = -m01;
		m02 = -m02;
		m03 = -m03;
		m10 = -m10;
		m11 = -m11;
		m12 = -m12;
		m13 = -m13;
		m20 = -m20;
		m21 = -m21;
		m22 = -m22;
		m23 = -m23;
		m30 = -m30;
		m31 = -m31;
		m32 = -m32;
		m33 = -m33;
	}

	/*	
		public int hashCode()
		{
		}
	*/

	/**
	 * Sets the value of this matrix equal to the negation of of the Matrix4f parameter.
	 * @param mat the source matrix
	 */
	public final void negate(Matrix4f mat)
	{
		m00 = -mat.m00;
		m01 = -mat.m01;
		m02 = -mat.m02;
		m03 = -mat.m03;
		m10 = -mat.m10;
		m11 = -mat.m11;
		m12 = -mat.m12;
		m13 = -mat.m13;
		m20 = -mat.m20;
		m21 = -mat.m21;
		m22 = -mat.m22;
		m23 = -mat.m23;
		m30 = -mat.m30;
		m31 = -mat.m31;
		m32 = -mat.m32;
		m33 = -mat.m33;
	}

	/**
	 * Fast inverts this matrix correctly if and only if this matrix is orthonormal.
	 * This method is significantly faster and simplier than this.invert()
	 * but only correct if the matrix is orthonormal (i.e. unit scale and 
	 * orthogonal axes, a.k.a. a rigid body transformstion, or a translation and rotation only matirx).
	 * Object and camera transforms are often maintained as orthonormal so this can be used as a very fast
	 * alternative to invert() in those cases.
	 */
	public final void orthonormalInvert()
	{
		// transpose rotation part
		float swap = m10;
		m10 = m01;
		m01 = swap;

		swap = m20;
		m20 = m02;
		m02 = swap;

		swap = m21;
		m21 = m12;
		m12 = swap;

		//Compute translation part
		float x, y, z;
		x = -m00 * m03 - m01 * m13 - m02 * m23;
		y = -m10 * m03 - m11 * m13 - m12 * m23;
		z = -m20 * m03 - m21 * m13 - m22 * m23;
		m03 = x;
		m13 = y;
		m23 = z;

		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;

	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed (user declared) matrix mat.
	 * This makes a copy then uses the this.invert() method.
	 * The copy should really be eliminated since the process of invert makes
	 * a copy as well.  For this reason this.invert() is preferred in this implementation.
	 * @param mat - the matrix from which this is to be inverted
	 */
	public final void invert(Matrix4f mat)
	{
		set(mat);
		invert();
	}
	
	/**
	 * Inverts this matrix in place.  This implementation uses the Determinant and the Adjoint Matrix (transposed cofactor) for speed.
	 * The transposed cofactor and detereminant are unrolled and using local variable for speed.
	 * This implementation has tested to on average 2-3 (sometimes > 10) times faster than javax.vecmath.Matrix4f.invert(), which
	 * uses Single Value Decomposition and LU back substitution.
	 */
	/*
	 * However, this is ugly and unmaintainanable!;-)
	 */
	public final void invert()
	{
		float tmpMat_m00 = m00;
		float tmpMat_m01 = m01;
		float tmpMat_m02 = m02;
		float tmpMat_m03 = m03;

		float tmpMat_m10 = m10;
		float tmpMat_m11 = m11;
		float tmpMat_m12 = m12;
		float tmpMat_m13 = m13;

		float tmpMat_m20 = m20;
		float tmpMat_m21 = m21;
		float tmpMat_m22 = m22;
		float tmpMat_m23 = m23;

		float tmpMat_m30 = m30;
		float tmpMat_m31 = m31;
		float tmpMat_m32 = m32;
		float tmpMat_m33 = m33;

		float inverseDet = 1.0f / (m00
			* ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
				- m13 * m22 * m31
				- m11 * m23 * m32
				- m12 * m21 * m33)
			- m01
				* ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
					- m13 * m22 * m30
					- m10 * m23 * m32
					- m12 * m20 * m33)
			+ m02
				* ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
					- m13 * m21 * m30
					- m10 * m23 * m31
					- m11 * m20 * m33)
			- m03
				* ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
					- m12 * m21 * m30
					- m10 * m22 * m31
					- m11 * m20 * m32));

		float a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16;
		a1 = tmpMat_m22 * tmpMat_m33 - tmpMat_m23 * tmpMat_m32;
		a2 = tmpMat_m23 * tmpMat_m31 - tmpMat_m21 * tmpMat_m33;
		a3 = tmpMat_m21 * tmpMat_m32 - tmpMat_m22 * tmpMat_m31;
		a4 = tmpMat_m12 * tmpMat_m33 - tmpMat_m13 * tmpMat_m32;
		a5 = tmpMat_m13 * tmpMat_m31 - tmpMat_m11 * tmpMat_m33;
		a6 = tmpMat_m12 * tmpMat_m23 - tmpMat_m13 * tmpMat_m22;
		a7 = tmpMat_m11 * tmpMat_m22 - tmpMat_m12 * tmpMat_m21;
		a8 = tmpMat_m23 * tmpMat_m30 - tmpMat_m20 * tmpMat_m33;
		a9 = tmpMat_m20 * tmpMat_m32 - tmpMat_m22 * tmpMat_m30;
		a10 = tmpMat_m13 * tmpMat_m30 - tmpMat_m10 * tmpMat_m33;
		a11 = tmpMat_m13 * tmpMat_m20 - tmpMat_m10 * tmpMat_m23;
		a12 = tmpMat_m21 * tmpMat_m33 - tmpMat_m23 * tmpMat_m31;
		a13 = tmpMat_m20 * tmpMat_m31 - tmpMat_m21 * tmpMat_m30;
		a14 = tmpMat_m10 * tmpMat_m31 - tmpMat_m11 * tmpMat_m30;
		a15 = tmpMat_m10 * tmpMat_m21 - tmpMat_m11 * tmpMat_m20;
		a16 = tmpMat_m22 * tmpMat_m30 - tmpMat_m20 * tmpMat_m32;

		this.m00 = inverseDet 
			* tmpMat_m11 * a1 
			+ tmpMat_m12 * a2 
			+ tmpMat_m13 * a3;
		this.m01 = -inverseDet 
			* tmpMat_m01 * a1 
			+ tmpMat_m02 * a2 
			+ tmpMat_m03 * a3;
		this.m02 = inverseDet 
			* tmpMat_m01 * a4
			+ tmpMat_m02 * (tmpMat_m13 * tmpMat_m31 - tmpMat_m11 * tmpMat_m33)
			+ tmpMat_m03 * a5;
		this.m03 = -inverseDet 
			* tmpMat_m01 * a6
			+ tmpMat_m02 * (tmpMat_m13 * tmpMat_m21 - tmpMat_m11 * tmpMat_m23)
			+ tmpMat_m03 * a7;
		this.m10 = -inverseDet 
			* tmpMat_m10 * a1 
			+ tmpMat_m12 * a8 
			+ tmpMat_m13 * a9;
		this.m11 = inverseDet 
			* tmpMat_m00 * a1 
			+ tmpMat_m02 * a8 
			+ tmpMat_m03 * a9;
		this.m12 = -inverseDet 
			* tmpMat_m00 * a4
			+ tmpMat_m02 * a10
			+ tmpMat_m03 * (tmpMat_m10 * tmpMat_m32 - tmpMat_m12 * tmpMat_m30);
		this.m13 = -inverseDet 
			* tmpMat_m00 * a6
			+ tmpMat_m02 * a11
			+ tmpMat_m03 * (tmpMat_m10 * tmpMat_m22 - tmpMat_m12 * tmpMat_m20);
		this.m20 = inverseDet 
			* tmpMat_m10 * a12 
			+ tmpMat_m11 * a8 
			+ tmpMat_m13 * a13;
		this.m21 = -inverseDet 
			* tmpMat_m00 * a12 
			+ tmpMat_m01 * a8 
			+ tmpMat_m03 * a13;
		this.m22 = -inverseDet 
			* tmpMat_m00 * (tmpMat_m11 * tmpMat_m33 - tmpMat_m13 * tmpMat_m31)
			+ tmpMat_m01 * a10
			+ tmpMat_m03 * a14;
		this.m23 = inverseDet 
			* tmpMat_m00 * (tmpMat_m11 * tmpMat_m23 - tmpMat_m13 * tmpMat_m21)
			+ tmpMat_m01 * a11
			+ tmpMat_m03 * a15;
		this.m30 = -inverseDet 
			* tmpMat_m10 * a3 
			+ tmpMat_m11 * a16 
			+ tmpMat_m12 * a13;
		this.m31 = -inverseDet 
			* tmpMat_m00 * a3 
			+ tmpMat_m01 * a16 
			+ tmpMat_m02 * a13;
		this.m32 = inverseDet 
			* tmpMat_m00 * a5
			+ tmpMat_m01 * (tmpMat_m12 * tmpMat_m30 - tmpMat_m10 * tmpMat_m32)
			+ tmpMat_m02 * a14;
		this.m33 = -inverseDet 
			* tmpMat_m00 * a7
			+ tmpMat_m01 * (tmpMat_m12 * tmpMat_m20 - tmpMat_m10 * tmpMat_m22)
			+ tmpMat_m02 * a15;
	}

	/**
	 * Sets the specified element of this matrix4f to the value provided.
	 * @param row - the row number to be modified (zero indexed)
	 * @param column - the column number to be modified (zero indexed)
	 * @param value - the new value
	 */
	public final void setElement(int row, int column, float value)
	{
		switch (row)
		{
			case 0 :
				switch (column)
				{
					case 0 :
						m00 = value;
						break;
					case 1 :
						m01 = value;
						break;
					case 2 :
						m02 = value;
						break;
					case 3 :
						m03 = value;
						break;
					default :
						throw new ArrayIndexOutOfBoundsException();
				}
				break;
			case 1 :
				switch (column)
				{
					case 0 :
						m10 = value;
						break;
					case 1 :
						m11 = value;
						break;
					case 2 :
						m12 = value;
						break;
					case 3 :
						m13 = value;
						break;
					default :
						throw new ArrayIndexOutOfBoundsException();
				}
				break;
			case 2 :
				switch (column)
				{
					case 0 :
						m20 = value;
						break;
					case 1 :
						m21 = value;
						break;
					case 2 :
						m22 = value;
						break;
					case 3 :
						m23 = value;
						break;
					default :
						throw new ArrayIndexOutOfBoundsException();
				}
				break;
			case 3 :
				switch (column)
				{
					case 0 :
						m30 = value;
						break;
					case 1 :
						m31 = value;
						break;
					case 2 :
						m32 = value;
						break;
					case 3 :
						m33 = value;
						break;
					default :
						throw new ArrayIndexOutOfBoundsException();
				}
				break;
			default :
				throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Retrieves the value at the specified row and column of this matrix.
	 * @param row - the row number to be retrieved (zero indexed)
	 * @param column - the column number to be retrieved (zero indexed)
	 * @return the value at the indexed element
	 */
	public final float getElement(int row, int column)
	{
		switch (row)
		{
			default :
				break;
			case 0 :
				switch (column)
				{
					case 0 :
						return m00;
					case 1 :
						return m01;
					case 2 :
						return m02;
					case 3 :
						return m03;
				}
				break;
			case 1 :
				switch (column)
				{
					case 0 :
						return m10;
					case 1 :
						return m11;
					case 2 :
						return m12;
					case 3 :
						return m13;
				}
				break;
			case 2 :
				switch (column)
				{
					case 0 : 
						return m20;
					case 1 :
						return m21;
					case 2 :
						return m22;
					case 3 :
						return m23;
				}
				break;
			case 3 :
				switch (column)
				{
					case 0 :
						return m30;
					case 1 :
						return m31;
					case 2 :
						return m32;
					case 3 :
						return m33;
				}
				break;
		}
		return Float.NaN;
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation about the X axis.
	 * @param angle - the angle to rotate about the X axis in radians
	 */
	public final void rotX(float angle)
	{
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = cos;
		m12 = -sin;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = sin;
		m22 = cos;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/*
		public final void rotXFast(float angle)
		{
			float sin = (float)VecMath.sin(angle);
			float cos = (float)VecMath.cos(angle);
			m00 = 1.0f;
			m01 = 0.0f;
			m02 = 0.0f;
			m03 = 0.0f;
			m10 = 0.0f;
			m11 = cos;
			m12 = -sin;
			m13 = 0.0f;
			m20 = 0.0f;
			m21 = sin;
			m22 = cos;
			m23 = 0.0f;
			m30 = 0.0f;
			m31 = 0.0f;
			m32 = 0.0f;
			m33 = 1.0f;
		}
	*/
	/**
	 * Sets the value of this matrix to a counter clockwise rotation about the Y axis.
	 * @param angle - the angle to rotate about the Y axis in radians
	 */
	public final void rotY(float angle)
	{
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		m00 = cos;
		m01 = 0.0f;
		m02 = sin;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = -sin;
		m21 = 0.0f;
		m22 = cos;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation about the Z axis.
	 * @param angle - the angle to rotate about the Z axis in radians
	 */
	public final void rotZ(float angle)
	{
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		m00 = cos;
		m01 = -sin;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = sin;
		m11 = cos;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix to a scale matrix with the the passed scale amount.
	 * @param scale - the scale factor for the matrix
	 */
	public final void set(float scale)
	{
		m00 = scale;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = scale;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = scale;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this transform to a scale and translation matrix; the scale is not applied to the translation and all of the matrix values are modified.
	 * @param scale - the scale factor for the matrix
	 * @param vec - the translation amount
	 */
	public final void set(float scale, Vector3f vec)
	{
		m00 = scale;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = vec.x;
		m10 = 0.0f;
		m11 = scale;
		m12 = 0.0f;
		m13 = vec.y;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = scale;
		m23 = vec.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the values in this Matrix4f equal to the row-major array parameter (ie, the first four elements of the array will be copied into the first row of this matrix, etc.).
	 * @param mat - the single precision array of length 16
	 */
	public final void set(float mat[])
	{
		m00 = mat[0];
		m01 = mat[1];
		m02 = mat[2];
		m03 = mat[3];
		m10 = mat[4];
		m11 = mat[5];
		m12 = mat[6];
		m13 = mat[7];
		m20 = mat[8];
		m21 = mat[9];
		m22 = mat[10];
		m23 = mat[11];
		m30 = mat[12];
		m31 = mat[13];
		m32 = mat[14];
		m33 = mat[15];
	}

	/**
	 * Sets the rotational component (upper 3x3) of this matrix to the matrix values in the single precision Matrix3f argument; the other elements of this matrix are initialized as if this were an identity matrix (i.e., affine matrix with no translational component).
	 * @param mat - the single-precision 3x3 matrix
	 */
	public final void set(Matrix3f mat)
	{
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = 0.0f;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = 0.0f;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix from the rotation expressed by the rotation matrix mat, the translation vec, and the scale factor. The translation is not modified by the scale.
	 * @param mat - the rotation component
	 * @param vec - the translation component
	 * @param scale - the scale component
	 */
	public final void set(Matrix3f mat, Vector3f vec, float scale)
	{
		m00 = mat.m00 * scale;
		m01 = mat.m01 * scale;
		m02 = mat.m02 * scale;
		m03 = vec.x;
		m10 = mat.m10 * scale;
		m11 = mat.m11 * scale;
		m12 = mat.m12 * scale;
		m13 = vec.y;
		m20 = mat.m20 * scale;
		m21 = mat.m21 * scale;
		m22 = mat.m22 * scale;
		m23 = vec.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this matrix to a copy of the passed matrix mat.
	 * @param mat - the matrix to be copied
	
	 */
	public final void set(Matrix4f mat)
	{
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = mat.m03;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = mat.m13;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = mat.m23;
		m30 = mat.m30;
		m31 = mat.m31;
		m32 = mat.m32;
		m33 = mat.m33;
	}

	/**
	 * Sets the value of this matrix to the matrix conversion of the single precision quaternion argument.
	 * @param quat - the quaternion to be converted
	 */
	public final void set(Quat4f quat)
	{
		float xx = quat.x * quat.x;
		float xy = quat.x * quat.y;
		float xz = quat.x * quat.z;
		float xw = quat.x * quat.w;

		float yy = quat.y * quat.y;
		float yz = quat.y * quat.z;
		float yw = quat.y * quat.w;

		float zz = quat.z * quat.z;
		float zw = quat.z * quat.w;

		m00 = 1 - 2 * (yy + zz);
		m01 = 2 * (xy - zw);
		m02 = 2 * (xz + yw);

		m10 = 2 * (xy + zw);
		m11 = 1 - 2 * (xx + zz);
		m12 = 2 * (yz - xw);

		m20 = 2 * (xz - yw);
		m21 = 2 * (yz + xw);
		m22 = 1 - 2 * (xx + yy);

		m03 = 0.0F;
		m13 = 0.0F;
		m23 = 0.0F;
		m30 = 0.0F;
		m31 = 0.0F;
		m32 = 0.0F;
		m33 = 1.0F;
	}

	/**
	 * Sets the value of this matrix from the rotation expressed by the quaternion quat, the translation vec, and the scale scale.
	 * @param quat - the rotation expressed as a quaternion
	 * @param vec - the translation
	 * @param scale - the scale value
	 */

	public final void set(Quat4f quat, Vector3f vec, float scale)
	{
		float xx = quat.x * quat.x;
		float xy = quat.x * quat.y;
		float xz = quat.x * quat.z;
		float xw = quat.x * quat.w;

		float yy = quat.y * quat.y;
		float yz = quat.y * quat.z;
		float yw = quat.y * quat.w;

		float zz = quat.z * quat.z;
		float zw = quat.z * quat.w;

		m00 = scale * (1 - 2 * (yy + zz));
		m01 = scale * (2 * (xy - zw));
		m02 = scale * (2 * (xz + yw));

		m10 = scale * (2 * (xy + zw));
		m11 = scale * (1 - 2 * (xx + zz));
		m12 = scale * (2 * (yz - xw));

		m20 = scale * (2 * (xz - yw));
		m21 = scale * (2 * (yz + xw));
		m22 = scale * (1 - 2 * (xx + yy));

		m03 = vec.x;
		m13 = vec.y;
		m23 = vec.z;
		m30 = 0.0F;
		m31 = 0.0F;
		m32 = 0.0F;
		m33 = 1.0F;
	}

	/**
	 * Sets the value of this matrix to a translate matrix with the passed translation value.
	 * @param vec - the translation amount
	 */
	public final void set(Vector3f vec)
	{
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = vec.x;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = vec.y;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = vec.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets the value of this transform to a scale and translation matrix; the scale is not applied to the translation and all of the matrix values are modified.
	 * @param vec  - the translation amount
	 * @param scale - the scale factor for the matrix
	 */

	public final void set(Vector3f vec, float scale)
	{
		m00 = scale;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = scale * vec.x;
		m10 = 0.0f;
		m11 = scale;
		m12 = 0.0f;
		m13 = scale * vec.y;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = scale;
		m23 = scale * vec.z;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Sets this Matrix4f to identity.
	 */
	public final void setIdentity()
	{
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
	}

	/**
	 * Replaces the upper 3x3 matrix values of this matrix with the values in the matrix m1.
	 * @param mat - the matrix that will be the new upper 3x3
	 */
	public final void setRotationScale(Matrix3f mat)
	{
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
	}

	/**
	 * Modifies the translational components of this matrix to the values of the Vector3f argument; the other values of this matrix are not modified.
	 * @param vec - the translational component
	 */
	public final void setTranslation(Vector3f vec)
	{
		m03 = vec.x;
		m13 = vec.y;
		m23 = vec.z;
	}

	/**
	 * Sets this matrix to all zeros.
	 */
	public final void setZero()
	{
		m00 = 0.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 0.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 0.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 0.0f;
	}

	/**
	 * Sets this matrix to the matrix difference of itself and matrix m1 (this = this - mat).
	 * @param mat
	 */
	public final void sub(Matrix4f mat)
	{
		m00 -= mat.m00;
		m01 -= mat.m01;
		m02 -= mat.m02;
		m03 -= mat.m03;
		m10 -= mat.m10;
		m11 -= mat.m11;
		m12 -= mat.m12;
		m13 -= mat.m13;
		m20 -= mat.m20;
		m21 -= mat.m21;
		m22 -= mat.m22;
		m23 -= mat.m23;
		m30 -= mat.m30;
		m31 -= mat.m31;
		m32 -= mat.m32;
		m33 -= mat.m33;
	}

	/**
	 * Performs an element-by-element subtraction of matrix mat2 from matrix mat1 and places the result into matrix this (this = mat1 - mat2).
	 * @param mat1 - the first matrix
	 * @param mat2 - the second matrix
	 */
	public final void sub(Matrix4f mat1, Matrix4f mat2)
	{
		m00 = mat1.m00 - mat2.m00;
		m01 = mat1.m01 - mat2.m01;
		m02 = mat1.m02 - mat2.m02;
		m03 = mat1.m03 - mat2.m03;
		m10 = mat1.m10 - mat2.m10;
		m11 = mat1.m11 - mat2.m11;
		m12 = mat1.m12 - mat2.m12;
		m13 = mat1.m13 - mat2.m13;
		m20 = mat1.m20 - mat2.m20;
		m21 = mat1.m21 - mat2.m21;
		m22 = mat1.m22 - mat2.m22;
		m23 = mat1.m23 - mat2.m23;
		m30 = mat1.m30 - mat2.m30;
		m31 = mat1.m31 - mat2.m31;
		m32 = mat1.m32 - mat2.m32;
		m33 = mat1.m33 - mat2.m33;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * Returns a string that contains the values of this Matrix4f.
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return m00
			+ ", "
			+ m01
			+ ", "
			+ m02
			+ ", "
			+ m03
			+ "\n"
			+ m10
			+ ", "
			+ m11
			+ ", "
			+ m12
			+ ", "
			+ m13
			+ "\n"
			+ m20
			+ ", "
			+ m21
			+ ", "
			+ m22
			+ ", "
			+ m23
			+ "\n"
			+ m30
			+ ", "
			+ m31
			+ ", "
			+ m32
			+ ", "
			+ m33
			+ "\n";
	}

	/**
	 * Transforms the vector parameter by this Matrix4f and places the value into vecOut. The fourth element of the normal is assumed to be zero. 
	 * @param vec
	 */
	public final void transform(Vector3f vec)
	{
		float x = m00 * vec.x + m01 * vec.y + m02 * vec.z;
		float y = m10 * vec.x + m11 * vec.y + m12 * vec.z;
		vec.z = m20 * vec.x + m21 * vec.y + m22 * vec.z;
		vec.x = x;
		vec.y = y;
	}

	/**
	 * Transforms the vecIn parameter by this transform and places the value into vecOut. The fourth element of the vector is assumed to be zero. 
	 * @param vecIn
	 * @param vecOut
	 */
	public final void transform(Vector3f vecIn, Vector3f vecOut)
	{
		vecOut.x = m00 * vecIn.x + m01 * vecIn.y + m02 * vecIn.z;
		vecOut.y = m10 * vecIn.x + m11 * vecIn.y + m12 * vecIn.z;
		vecOut.z = m20 * vecIn.x + m21 * vecIn.y + m22 * vecIn.z;
	}

	/**
	 * Sets the value of this matrix to its transpose in place.
	 */
	public final void transpose()
	{
		float swap = m10;
		m10 = m01;
		m01 = swap;
		swap = m20;
		m20 = m02;
		m02 = swap;
		swap = m30;
		m30 = m03;
		m03 = swap;
		swap = m21;
		m21 = m12;
		m12 = swap;
		swap = m31;
		m31 = m13;
		m13 = swap;
		swap = m32;
		m32 = m23;
		m23 = swap;
	}

	/**
	 * Sets the value of this matrix to its transpose in place.
	 */

	static public final void transpose(float[] mat)
	{
		float swap = mat[4];
		mat[4] = mat[1];
		mat[1] = swap;
		swap = mat[8];
		mat[8] = mat[2];
		mat[2] = swap;
		swap = mat[12];
		mat[12] = mat[3];
		mat[3] = swap;
		swap = mat[9];
		mat[9] = mat[6];
		mat[6] = swap;
		swap = mat[13];
		mat[13] = mat[7];
		mat[7] = swap;
		swap = mat[14];
		mat[14] = mat[11];
		mat[11] = swap;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 * @param mat - the matrix to be transposed
	 */
	public final void transpose(Matrix4f mat)
	{
		if (this != mat)
		{
			m00 = mat.m00;
			m01 = mat.m10;
			m02 = mat.m20;
			m03 = mat.m30;
			m10 = mat.m01;
			m11 = mat.m11;
			m12 = mat.m21;
			m13 = mat.m31;
			m20 = mat.m02;
			m21 = mat.m12;
			m22 = mat.m22;
			m23 = mat.m32;
			m30 = mat.m03;
			m31 = mat.m13;
			m32 = mat.m23;
			m33 = mat.m33;
		}
		else
		{
			transpose();
		}
	}
}
