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
 * A 4 element unit quaternion represented by single precision floating point x,y,z,w coordinates. The quaternion is always normalized.
 * @author Shawn Kendall
 */
final public class Quat4f implements Cloneable, java.io.Serializable
{
	public float x;
	public float y;
	public float z;
	public float w;

	public Quat4f(float xx, float yy, float zz, float ww)
	{
		float inverseMag = 1.0f / (float)Math.sqrt(xx * xx + yy * yy + zz * zz + ww * ww);
		x = xx * inverseMag;
		y = yy * inverseMag;
		z = zz * inverseMag;
		w = ww * inverseMag;
	}

	public Quat4f(float quat[])
	{
		float inverseMag = 1.0f / (float)Math.sqrt(quat[0] * quat[0] + quat[1] * quat[1] + quat[2] * quat[2] + quat[3] * quat[3]);
		x = quat[0] * inverseMag;
		y = quat[1] * inverseMag;
		z = quat[2] * inverseMag;
		w = quat[3] * inverseMag;
	}

	public Quat4f(Quat4f quat)
	{
		x = quat.x;
		y = quat.y;
		z = quat.z;
		w = quat.w;
	}

	public Quat4f()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
		w = 0.0f;
	}

	public final void set(float xx, float yy, float zz, float ww)
	{
		x = xx;
		y = yy;
		z = zz;
		w = ww;
	}

	public final void set(float quat[])
	{
		x = quat[0];
		y = quat[1];
		z = quat[2];
		w = quat[3];
	}

	public final void set(Quat4f quat)
	{
		x = quat.x;
		y = quat.y;
		z = quat.z;
		w = quat.w;
	}

	public final void get(float quat[])
	{
		quat[0] = x;
		quat[1] = y;
		quat[2] = z;
		quat[3] = w;
	}

	public final void get(Quat4f quat)
	{
		quat.x = x;
		quat.y = y;
		quat.z = z;
		quat.w = w;
	}

	public final void add(Quat4f quat1, Quat4f quat2)
	{
		x = quat1.x + quat2.x;
		y = quat1.y + quat2.y;
		z = quat1.z + quat2.z;
		w = quat1.w + quat2.w;
	}

	public final void add(Quat4f quat)
	{
		x += quat.x;
		y += quat.y;
		z += quat.z;
		w += quat.w;
	}

	public final void sub(Quat4f quat1, Quat4f quat2)
	{
		x = quat1.x - quat2.x;
		y = quat1.y - quat2.y;
		z = quat1.z - quat2.z;
		w = quat1.w - quat2.w;
	}

	public final void sub(Quat4f quat)
	{
		x -= quat.x;
		y -= quat.y;
		z -= quat.z;
		w -= quat.w;
	}

	public final void negate(Quat4f quat)
	{
		x = -quat.x;
		y = -quat.y;
		z = -quat.z;
		w = -quat.w;
	}

	public final void negate()
	{
		x = -x;
		y = -y;
		z = -z;
		w = -w;
	}

	public final void scale(float scale, Quat4f quat)
	{
		x = scale * quat.x;
		y = scale * quat.y;
		z = scale * quat.z;
		w = scale * quat.w;
	}

	public final void scale(float scale)
	{
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
	}

	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

	public final void absolute(Quat4f quat)
	{
		x = Math.abs(quat.x);
		y = Math.abs(quat.y);
		z = Math.abs(quat.z);
		w = Math.abs(quat.w);
	}

	public final void absolute()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		w = Math.abs(w);
	}

	public void interpolateLinear(Quat4f quat1, Quat4f quat2, float alpha)
	{
		x = (1.0f - alpha) * quat1.x + alpha * quat2.x;
		y = (1.0f - alpha) * quat1.y + alpha * quat2.y;
		z = (1.0f - alpha) * quat1.z + alpha * quat2.z;
		w = (1.0f - alpha) * quat1.w + alpha * quat2.w;
	}

	public void interpolateLinear(Quat4f quat, float alpha)
	{
		x = (1.0f - alpha) * x + alpha * quat.x;
		y = (1.0f - alpha) * y + alpha * quat.y;
		z = (1.0f - alpha) * z + alpha * quat.z;
		w = (1.0f - alpha) * w + alpha * quat.w;
	}

	/* Quat specific methods
	 * 
	 */
	public final void conjugate(Quat4f quat)
	{
		x = -quat.x;
		y = -quat.y;
		z = -quat.z;
		w = quat.w;
	}

	public final void conjugate()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	public final void mul(Quat4f quat1, Quat4f quat2)
	{
		if (this != quat1 && this != quat2)
		{
			w = quat1.w * quat2.w - quat1.x * quat2.x - quat1.y * quat2.y - quat1.z * quat2.z;
			x = (quat1.w * quat2.x + quat2.w * quat1.x + quat1.y * quat2.z) - quat1.z * quat2.y;
			y = ((quat1.w * quat2.y + quat2.w * quat1.y) - quat1.x * quat2.z) + quat1.z * quat2.x;
			z = (quat1.w * quat2.z + quat2.w * quat1.z + quat1.x * quat2.y) - quat1.y * quat2.x;
		}
		else
		{
			float f2 = quat1.w * quat2.w - quat1.x * quat2.x - quat1.y * quat2.y - quat1.z * quat2.z;
			float f = (quat1.w * quat2.x + quat2.w * quat1.x + quat1.y * quat2.z) - quat1.z * quat2.y;
			float f1 = ((quat1.w * quat2.y + quat2.w * quat1.y) - quat1.x * quat2.z) + quat1.z * quat2.x;
			z = (quat1.w * quat2.z + quat2.w * quat1.z + quat1.x * quat2.y) - quat1.y * quat2.x;
			w = f2;
			x = f;
			y = f1;
		}
	}

	public final void mul(Quat4f quat)
	{
		float ww = w * quat.w - x * quat.x - y * quat.y - z * quat.z;
		float xx = (w * quat.x + quat.w * x + y * quat.z) - z * quat.y;
		float yy = ((w * quat.y + quat.w * y) - x * quat.z) + z * quat.x;
		z = (w * quat.z + quat.w * z + x * quat.y) - y * quat.x;
		w = ww;
		x = xx;
		y = yy;
	}

	public final void inverse(Quat4f quat)
	{
		float inverseMag = 1.0f / (quat.w * quat.w + quat.x * quat.x + quat.y * quat.y + quat.z * quat.z);
		w = inverseMag * quat.w;
		x = -inverseMag * quat.x;
		y = -inverseMag * quat.y;
		z = -inverseMag * quat.z;
	}

	public final void inverse()
	{
		float inverseMag = 1.0f / (w * w + x * x + y * y + z * z);
		w *= inverseMag;
		x *= -inverseMag;
		y *= -inverseMag;
		z *= -inverseMag;
	}

	public final void normalize(Quat4f quat)
	{
		float scalar = quat.x * quat.x + quat.y * quat.y + quat.z * quat.z + quat.w * quat.w;
		if (scalar > 0.0F)
		{
			scalar = 1.0F / (float)Math.sqrt(scalar);
			x = scalar * quat.x;
			y = scalar * quat.y;
			z = scalar * quat.z;
			w = scalar * quat.w;
		}
		else
		{
			x = 0.0F;
			y = 0.0F;
			z = 0.0F;
			w = 0.0F;
		}
	}

	public final void normalize()
	{
		float scalar = x * x + y * y + z * z + w * w;
		if (scalar > 0.0F)
		{
			scalar = 1.0f / (float)Math.sqrt(scalar);
			x *= scalar;
			y *= scalar;
			z *= scalar;
			w *= scalar;
		}
		else
		{
			x = 0.0F;
			y = 0.0F;
			z = 0.0F;
			w = 0.0F;
		}
	}
}
