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
 * A single precision floating point 3 by 3 matrix. Primarily to support 3D transformations.
 * @author Shawn Kendall
 */
final public class Matrix3f implements Cloneable, java.io.Serializable
{
	public float m00;
	public float m01;
	public float m02;
	public float m10;
	public float m11;
	public float m12;
	public float m20;
	public float m21;
	public float m22;

	public Matrix3f(
		float mat00,
		float mat01,
		float mat02,
		float mat10,
		float mat11,
		float mat12,
		float mat20,
		float mat21,
		float mat22)
	{
		m00 = mat00;
		m01 = mat01;
		m02 = mat02;
		m10 = mat10;
		m11 = mat11;
		m12 = mat12;
		m20 = mat20;
		m21 = mat21;
		m22 = mat22;
	}

	public Matrix3f(float mat[])
	{
		m00 = mat[0];
		m01 = mat[1];
		m02 = mat[2];
		m10 = mat[3];
		m11 = mat[4];
		m12 = mat[5];
		m20 = mat[6];
		m21 = mat[7];
		m22 = mat[8];
	}

	public Matrix3f(Matrix3f mat)
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

	public Matrix3f()
	{
		m00 = 0.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m10 = 0.0f;
		m11 = 0.0f;
		m12 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 0.0f;
	}

	public String toString()
	{
		return m00
			+ ", "
			+ m01
			+ ", "
			+ m02
			+ "\n"
			+ m10
			+ ", "
			+ m11
			+ ", "
			+ m12
			+ "\n"
			+ m20
			+ ", "
			+ m21
			+ ", "
			+ m22
			+ "\n";
	}

	public final void setIdentity()
	{
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
	}

	public final void add(float val)
	{
		m00 += val;
		m01 += val;
		m02 += val;
		m10 += val;
		m11 += val;
		m12 += val;
		m20 += val;
		m21 += val;
		m22 += val;
	}

	public final void add(Matrix3f mat1, Matrix3f mat2)
	{
		m00 = mat1.m00 + mat2.m00;
		m01 = mat1.m01 + mat2.m01;
		m02 = mat1.m02 + mat2.m02;
		m10 = mat1.m10 + mat2.m10;
		m11 = mat1.m11 + mat2.m11;
		m12 = mat1.m12 + mat2.m12;
		m20 = mat1.m20 + mat2.m20;
		m21 = mat1.m21 + mat2.m21;
		m22 = mat1.m22 + mat2.m22;
	}

	public final void add(Matrix3f mat)
	{
		m00 += mat.m00;
		m01 += mat.m01;
		m02 += mat.m02;
		m10 += mat.m10;
		m11 += mat.m11;
		m12 += mat.m12;
		m20 += mat.m20;
		m21 += mat.m21;
		m22 += mat.m22;
	}

	public final void sub(Matrix3f mat1, Matrix3f mat2)
	{
		m00 = mat1.m00 - mat2.m00;
		m01 = mat1.m01 - mat2.m01;
		m02 = mat1.m02 - mat2.m02;
		m10 = mat1.m10 - mat2.m10;
		m11 = mat1.m11 - mat2.m11;
		m12 = mat1.m12 - mat2.m12;
		m20 = mat1.m20 - mat2.m20;
		m21 = mat1.m21 - mat2.m21;
		m22 = mat1.m22 - mat2.m22;
	}

	public final void sub(Matrix3f mat)
	{
		m00 -= mat.m00;
		m01 -= mat.m01;
		m02 -= mat.m02;
		m10 -= mat.m10;
		m11 -= mat.m11;
		m12 -= mat.m12;
		m20 -= mat.m20;
		m21 -= mat.m21;
		m22 -= mat.m22;
	}

	public final void transpose()
	{
		float swap = m10;
		m10 = m01;
		m01 = swap;
		swap = m20;
		m20 = m02;
		m02 = swap;
		swap = m21;
		m21 = m12;
		m12 = swap;
	}

	public final void transpose(Matrix3f mat)
	{
		// very nasty errors if this
		// check is not made
		if (this != mat)
		{
			m00 = mat.m00;
			m01 = mat.m10;
			m02 = mat.m20;
			m10 = mat.m01;
			m11 = mat.m11;
			m12 = mat.m21;
			m20 = mat.m02;
			m21 = mat.m12;
			m22 = mat.m22;
		}
		else
		{
			transpose();
		}
	}

	/* 
	 * Implemented as defined in 
	 * The Matrix and Quaternions FAQ
	 * Version 1.19  20th March 2002
	*/
	public final void set(Quat4f quat4f)
	{
		float xx = quat4f.x * quat4f.x;
		float xy = quat4f.x * quat4f.y;
		float xz = quat4f.x * quat4f.z;
		float xw = quat4f.x * quat4f.w;

		float yy = quat4f.y * quat4f.y;
		float yz = quat4f.y * quat4f.z;
		float yw = quat4f.y * quat4f.w;

		float zz = quat4f.z * quat4f.z;
		float zw = quat4f.z * quat4f.w;

		m00 = 1 - 2 * (yy + zz);
		m01 = 2 * (xy - zw);
		m02 = 2 * (xz + yw);

		m10 = 2 * (xy + zw);
		m11 = 1 - 2 * (xx + zz);
		m12 = 2 * (yz - xw);

		m20 = 2 * (xz - yw);
		m21 = 2 * (yz + xw);
		m22 = 1 - 2 * (xx + yy);
	}

	public final void set(float mat[])
	{
		m00 = mat[0];
		m01 = mat[1];
		m02 = mat[2];
		m10 = mat[3];
		m11 = mat[4];
		m12 = mat[5];
		m20 = mat[6];
		m21 = mat[7];
		m22 = mat[8];
	}

	public final void set(Matrix3f mat)
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

	public final float determinant()
	{
		return m00 * (m11 * m22 - m12 * m21)
			+ m01 * (m12 * m20 - m10 * m22)
			+ m02 * (m10 * m21 - m11 * m20);
	}

	public final void setScaleIndentity(float scale)
	{
		m00 = scale;
		m01 = 0.0F;
		m02 = 0.0F;
		m10 = 0.0F;
		m11 = scale;
		m12 = 0.0F;
		m20 = 0.0F;
		m21 = 0.0F;
		m22 = scale;
	}

	public final void rotX(float f)
	{
		float sin = (float)Math.sin(f);
		float cos = (float)Math.cos(f);
		m00 = 1.0F;
		m01 = 0.0F;
		m02 = 0.0F;
		m10 = 0.0F;
		m11 = cos;
		m12 = -sin;
		m20 = 0.0F;
		m21 = sin;
		m22 = cos;
	}

	public final void rotY(float f)
	{
		float sin = (float)Math.sin(f);
		float cos = (float)Math.cos(f);
		m00 = cos;
		m01 = 0.0F;
		m02 = sin;
		m10 = 0.0F;
		m11 = 1.0F;
		m12 = 0.0F;
		m20 = -sin;
		m21 = 0.0F;
		m22 = cos;
	}

	public final void rotZ(float f)
	{
		float sin = (float)Math.sin(f);
		float cos = (float)Math.cos(f);
		m00 = cos;
		m01 = -sin;
		m02 = 0.0F;
		m10 = sin;
		m11 = cos;
		m12 = 0.0F;
		m20 = 0.0F;
		m21 = 0.0F;
		m22 = 1.0F;
	}

	public final void mul(float val)
	{
		m00 *= val;
		m01 *= val;
		m02 *= val;
		m10 *= val;
		m11 *= val;
		m12 *= val;
		m20 *= val;
		m21 *= val;
		m22 *= val;
	}

	public final void mul(Matrix3f mat)
	{
		float tmp1 = m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20;
		float tmp2 = m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21;
		float tmp3 = m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22;
		float tmp4 = m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20;
		float tmp5 = m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21;
		float tmp6 = m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22;
		float tmp7 = m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20;
		float tmp8 = m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21;
		float tmp9 = m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22;
		m00 = tmp1;
		m01 = tmp2;
		m02 = tmp3;
		m10 = tmp4;
		m11 = tmp5;
		m12 = tmp6;
		m20 = tmp7;
		m21 = tmp8;
		m22 = tmp9;
	}

	public final void mul(Matrix3f mat1, Matrix3f mat2)
	{
		// very nasty errors if this check is not made
		// comment in if your usage could do this
		//if ( this != mat1 && this != mat2 )
		//{
		m00 = mat1.m00 * mat2.m00 + mat1.m01 * mat2.m10 + mat1.m02 * mat2.m20;
		m01 = mat1.m00 * mat2.m01 + mat1.m01 * mat2.m11 + mat1.m02 * mat2.m21;
		m02 = mat1.m00 * mat2.m02 + mat1.m01 * mat2.m12 + mat1.m02 * mat2.m22;
		m10 = mat1.m10 * mat2.m00 + mat1.m11 * mat2.m10 + mat1.m12 * mat2.m20;
		m11 = mat1.m10 * mat2.m01 + mat1.m11 * mat2.m11 + mat1.m12 * mat2.m21;
		m12 = mat1.m10 * mat2.m02 + mat1.m11 * mat2.m12 + mat1.m12 * mat2.m22;
		m20 = mat1.m20 * mat2.m00 + mat1.m21 * mat2.m10 + mat1.m22 * mat2.m20;
		m21 = mat1.m20 * mat2.m01 + mat1.m21 * mat2.m11 + mat1.m22 * mat2.m21;
		m22 = mat1.m20 * mat2.m02 + mat1.m21 * mat2.m12 + mat1.m22 * mat2.m22;
		/*
		} 
		else
		{
		    float tmp1 = mat1.m00 * mat2.m00 + mat1.m01 * mat2.m10 + mat1.m02 * mat2.m20;
		    float tmp2 = mat1.m00 * mat2.m01 + mat1.m01 * mat2.m11 + mat1.m02 * mat2.m21;
		    float tmp3 = mat1.m00 * mat2.m02 + mat1.m01 * mat2.m12 + mat1.m02 * mat2.m22;
		    float tmp4 = mat1.m10 * mat2.m00 + mat1.m11 * mat2.m10 + mat1.m12 * mat2.m20;
		    float tmp5 = mat1.m10 * mat2.m01 + mat1.m11 * mat2.m11 + mat1.m12 * mat2.m21;
		    float tmp6 = mat1.m10 * mat2.m02 + mat1.m11 * mat2.m12 + mat1.m12 * mat2.m22;
		    float tmp7 = mat1.m20 * mat2.m00 + mat1.m21 * mat2.m10 + mat1.m22 * mat2.m20;
		    float tmp8 = mat1.m20 * mat2.m01 + mat1.m21 * mat2.m11 + mat1.m22 * mat2.m21;
		    float tmp9 = mat1.m20 * mat2.m02 + mat1.m21 * mat2.m12 + mat1.m22 * mat2.m22;
		    m00 = tmp1;
		    m01 = tmp2;
		    m02 = tmp3;
		    m10 = tmp4;
		    m11 = tmp5;
		    m12 = tmp6;
		    m20 = tmp7;
		    m21 = tmp8;
		    m22 = tmp9;
		}
		*/
	}

	public final void setZero()
	{
		m00 = 0.0F;
		m01 = 0.0F;
		m02 = 0.0F;
		m10 = 0.0F;
		m11 = 0.0F;
		m12 = 0.0F;
		m20 = 0.0F;
		m21 = 0.0F;
		m22 = 0.0F;
	}

	public final void negate()
	{
		m00 = -m00;
		m01 = -m01;
		m02 = -m02;
		m10 = -m10;
		m11 = -m11;
		m12 = -m12;
		m20 = -m20;
		m21 = -m21;
		m22 = -m22;
	}

	public final void negate(Matrix3f mat)
	{
		m00 = -mat.m00;
		m01 = -mat.m01;
		m02 = -mat.m02;
		m10 = -mat.m10;
		m11 = -mat.m11;
		m12 = -mat.m12;
		m20 = -mat.m20;
		m21 = -mat.m21;
		m22 = -mat.m22;
	}

	public final void transform(Vector3f vec)
	{
		float x = m00 * vec.x + m01 * vec.y + m02 * vec.z;
		float y = m10 * vec.x + m11 * vec.y + m12 * vec.z;
		vec.z = m20 * vec.x + m21 * vec.y + m22 * vec.z;
		vec.x = x;
		vec.y = y;
	}

	public final void transform(Vector3f vec1, Vector3f vec2)
	{
		vec2.x = m00 * vec1.x + m01 * vec1.y + m02 * vec1.z;
		vec2.y = m10 * vec1.x + m11 * vec1.y + m12 * vec1.z;
		vec2.z = m20 * vec1.x + m21 * vec1.y + m22 * vec1.z;
	}
}
