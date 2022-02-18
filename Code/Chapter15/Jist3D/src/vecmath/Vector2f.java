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
 * A 2 element point that is represented by single precision floating point x,y coordinates.
 * @author Shawn Kendall
 */
final public class Vector2f implements Cloneable, java.io.Serializable
{
	public float x;
	public float y;

	public Vector2f(float xx, float yy)
	{
		x = xx;
		y = yy;
	}

	public Vector2f(float vec[])
	{
		x = vec[0];
		y = vec[1];
	}

	public Vector2f(Vector2f vec)
	{
		x = vec.x;
		y = vec.y;
	}

	public Vector2f()
	{
		x = 0.0F;
		y = 0.0F;
	}

	public final void set(float xx, float yy)
	{
		x = xx;
		y = yy;
	}

	public final void set(float vec[])
	{
		x = vec[0];
		y = vec[1];
	}

	public final void set(Vector2f vec)
	{
		x = vec.x;
		y = vec.y;
	}

	public final void get(float vec[])
	{
		vec[0] = x;
		vec[1] = y;
	}

	public final void add(Vector2f vec1, Vector2f vec2)
	{
		x = vec1.x + vec2.x;
		y = vec1.y + vec2.y;
	}

	public final void add(Vector2f vec)
	{
		x += vec.x;
		y += vec.y;
	}

	public final void sub(Vector2f vec1, Vector2f vec2)
	{
		x = vec1.x - vec2.x;
		y = vec1.y - vec2.y;
	}

	public final void sub(Vector2f vec)
	{
		x -= vec.x;
		y -= vec.y;
	}

	public final void negate(Vector2f vec)
	{
		x = -vec.x;
		y = -vec.y;
	}

	public final void negate()
	{
		x = -x;
		y = -y;
	}

	public final void scale(float scalar, Vector2f vec)
	{
		x = scalar * vec.x;
		y = scalar * vec.y;
	}

	public final void scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
	}

	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}

	public final void absolute(Vector2f vec)
	{
		x = Math.abs(vec.x);
		y = Math.abs(vec.y);
	}

	public final void absolute()
	{
		x = Math.abs(x);
		y = Math.abs(y);
	}

	public final void interpolate(Vector2f vec1, Vector2f vec2, float alpha)
	{
		x = (1.0f - alpha) * vec1.x + alpha * vec2.x;
		y = (1.0f - alpha) * vec1.y + alpha * vec2.y;
	}

	public final void interpolate(Vector2f vec, float alpha)
	{
		x = (1.0f - alpha) * x + alpha * vec.x;
		y = (1.0f - alpha) * y + alpha * vec.y;
	}

	public final float dot(Vector2f vec)
	{
		return x * vec.x + y * vec.y;
	}

	public final float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

	public final float lengthSquared()
	{
		return x * x + y * y;
	}

	public final void normalize(Vector2f vec)
	{
		float inverseMag = 1.0f / (float)Math.sqrt(vec.x * vec.x + vec.y * vec.y);
		x = vec.x * inverseMag;
		y = vec.y * inverseMag;
	}

	public final void normalize()
	{
		float inverseMag = 1.0f / (float)Math.sqrt(x * x + y * y);
		x *= inverseMag;
		y *= inverseMag;
	}

	public final float angle(Vector2f vec)
	{
		float d = dot(vec) / (length() * vec.length());
		if (d < -1.0f)
			d = -1.0f;
		if (d > 1.0f)
			d = 1.0f;
		return (float)Math.acos(d);
	}

	/* Convience Methods from vecmath Point2f
	 * These treat this Vector as a Point
	 * 
	 * @author Shawn Kendall
	 *
	 */

	public final float distanceSquared(Vector2f point)
	{
		float dx = x - point.x;
		float dy = y - point.y;
		return dx * dx + dy * dy;
	}

	public final float distance(Vector2f point)
	{
		float dx = x - point.x;
		float dy = y - point.y;
		return (float)Math.sqrt(dx * dx + dy * dy);
	}

	/*
	 * L1 or Manhattan distance
	 */
	public final float distanceL1(Vector2f point)
	{
		return Math.abs(x - point.x) + Math.abs(y - point.y);
	}

	/*
	 * Implemented as described in J3D vecmath docs
	 */
	public final float distanceLinf(Vector2f point)
	{
		return Math.max(Math.abs(x - point.x), Math.abs(y - point.y));
	}
}
