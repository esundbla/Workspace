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
 * A 3-element vector that is represented by single-precision floating point x,y,z coordinates. If this value represents a normal, then it should be normalized.
 * @author Shawn Kendall
 */
final public class Vector3f implements Cloneable, java.io.Serializable
{
	public float x;
	public float y;
	public float z;

	public Vector3f(float xx, float yy, float zz)
	{
		x = xx;
		y = yy;
		z = zz;
	}

	public Vector3f(float vec[])
	{
		x = vec[0];
		y = vec[1];
		z = vec[2];
	}

	public Vector3f(Vector3f vec)
	{
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3f()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public final void set(float xx, float yy, float zz)
	{
		x = xx;
		y = yy;
		z = zz;
	}

	public final void set(float vec[])
	{
		x = vec[0];
		y = vec[1];
		z = vec[2];
	}

	public final void set(Vector3f vec)
	{
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public final void get(float vec[])
	{
		vec[0] = x;
		vec[1] = y;
		vec[2] = z;
	}

	public final void get(Vector3f vec)
	{
		vec.x = x;
		vec.y = y;
		vec.z = z;
	}

	public final void add(Vector3f vec1, Vector3f vec2)
	{
		x = vec1.x + vec2.x;
		y = vec1.y + vec2.y;
		z = vec1.z + vec2.z;
	}

	public final void add(Vector3f vec)
	{
		x += vec.x;
		y += vec.y;
		z += vec.z;
	}

	public final void sub(Vector3f vec1, Vector3f vec2)
	{
		x = vec1.x - vec2.x;
		y = vec1.y - vec2.y;
		z = vec1.z - vec2.z;
	}

	public final void sub(Vector3f vec)
	{
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
	}

	public final void negate(Vector3f vec)
	{
		x = -vec.x;
		y = -vec.y;
		z = -vec.z;
	}

	public final void negate()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	public final void scale(float scalar, Vector3f vec)
	{
		x = scalar * vec.x;
		y = scalar * vec.y;
		z = scalar * vec.z;
	}

	public final void scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	public final void absolute(Vector3f vec)
	{
		x = Math.abs(vec.x);
		y = Math.abs(vec.y);
		z = Math.abs(vec.z);
	}

	public final void absolute()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
	}

	public final void interpolate(Vector3f vec1, Vector3f vec2, float alpha)
	{
		x = (1.0f - alpha) * vec1.x + alpha * vec2.x;
		y = (1.0f - alpha) * vec1.y + alpha * vec2.y;
		z = (1.0f - alpha) * vec1.z + alpha * vec2.z;
	}

	public final void interpolate(Vector3f vec, float alpha)
	{
		x = (1.0f - alpha) * x + alpha * vec.x;
		y = (1.0f - alpha) * y + alpha * vec.y;
		z = (1.0f - alpha) * z + alpha * vec.z;
	}

	public final float lengthSquared()
	{
		return x * x + y * y + z * z;
	}

	public final float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	public final void cross(Vector3f vec1, Vector3f vec2)
	{
		float xx = vec1.y * vec2.z - vec1.z * vec2.y;
		float yy = vec2.x * vec1.z - vec2.z * vec1.x;
		z = vec1.x * vec2.y - vec1.y * vec2.x;
		x = xx;
		y = yy;
	}

	public final float dot(Vector3f vec)
	{
		return x * vec.x + y * vec.y + z * vec.z;
	}

	public final void normalize(Vector3f vec)
	{
		float inverseMag = 1.0f / (float)Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
		x = vec.x * inverseMag;
		y = vec.y * inverseMag;
		z = vec.z * inverseMag;
	}

	public final void normalize()
	{
		float inverseMag = 1.0f / (float)Math.sqrt(x * x + y * y + z * z);
		x *= inverseMag;
		y *= inverseMag;
		z *= inverseMag;
	}

	public final float angle(Vector3f vec)
	{
		double d = dot(vec) / (length() * vec.length());
		if (d < -1D)
			d = -1D;
		if (d > 1.0D)
			d = 1.0D;
		return (float)Math.acos(d);
	}

	/* Convience methods for when using a Vector3f as a AWT Color
	 * 
	 * @author Shawn Kendall
	 *
	 */
	/* import java.awt.Color to use
	public final void set(Color color)
	{
		x = (float)color.getRed() / 255f;
		y = (float)color.getGreen() / 255f;
		z = (float)color.getBlue() / 255f;
	}
	
	public final Color get()
	{
		int r = (int)((x * 255f) + 0.5f);
		int g = (int)((y * 255f) + 0.5f);
		int b = (int)((z * 255f) + 0.5f);
		return new Color(r, g, b);
	}
	
	/* Convience Methods from vecmath Point3f
	* These treat this Vector as a Point
	* 
	* @author Shawn Kendall
	*
	*/
	public final float distanceSquared(Vector3f point)
	{
		float dx = x - point.x;
		float dy = y - point.y;
		float dz = z - point.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public final float distance(Vector3f point)
	{
		float dx = x - point.x;
		float dy = y - point.y;
		float dz = z - point.z;
		return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/*
	 * L1 or Manhattan distance
	 */
	public final float distanceL1(Vector3f point)
	{
		return Math.abs(x - point.x) + Math.abs(y - point.y) + Math.abs(z - point.z);
	}

	/*
	 * Implemented as described in J3D vecmath docs
	 */
	public final float distanceLinf(Vector3f point)
	{
		float max = Math.max(Math.abs(x - point.x), Math.abs(y - point.y));
		return Math.max(max, Math.abs(z - point.z));
	}

}
