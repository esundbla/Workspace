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
 
package com.imi.util;

/**
 * @author Shawn Kendall, IMI, LLC
 *
 */

final public class MathUtils
{
	//private static final int sinScaleInverse = 1/sinScale;
	static final double factor = Math.sqrt(2.0) / 2.0;
	static final double A = 0.417319242;
	static final double B = 0.590178532;
	static final float Af = 0.417319242f;
	static final float Bf = 0.590178532f;

	private static final int sinScale = 100;
	private static final float[] sin = new float[(90 * sinScale) + 2];
	static // build sine look-up table.
	{
		double toRadian = Math.PI / (180.0 * sinScale);
		for (int i = 0; i < sin.length; i++)
		{
			sin[i] = ((float)Math.sin(((double)i) * toRadian));
		}
		//System.out.println("Sin of 0 " + sin[0]);
		//System.out.println("Sin of 45 " + sin[(90*sinScale)/2]);
		//System.out.println("Sin of 90 " + sin[90*sinScale]);
	}

	static final public float sin(float a)
	{
		// Limit range if needed.
		if (a > 360)
			a %= 360;
		else if (a < 0)
		{
			a %= 360;
			a += 360;
		}

		int angle = (int) (a * sinScale);

		if (angle < (180 * sinScale) + 1)
		{
			if (angle < (90 * sinScale) + 1)
			{
				return sin[angle];
			}
			else
			{
				return sin[(180 * sinScale) - angle];
			}
		}
		else
		{
			if (angle < (270 * sinScale) + 1)
			{
				return -sin[angle - (180 * sinScale)];
			}
			else
			{
				return -sin[(360 * sinScale) - angle];
			}
		}
		//System.out.println("Sin of " + angle);
	}

	static final public float sinTerp(float a)
	{
		// Limit range if needed.
		if (a > 360)
			a %= 360;
		float angleScaled = a * sinScale;
		int angle = (int)angleScaled;
		float alpha = angleScaled - angle;
		//System.out.println(alpha);
		int index;
		if (angle < (180 * sinScale) + 1)
		{
			if (angle < (90 * sinScale) + 1)
			{
				index = angle;
			}
			else
			{
				index = (180 * sinScale) - angle;
			}
			// interp final angle
			return (1.0f - alpha) * sin[index] + alpha * sin[index + 1];
		}
		else
		{
			if (angle < (270 * sinScale) + 1)
			{
				index = angle - (180 * sinScale);
			}
			else
			{
				index = (360 * sinScale) - angle;
			}
			// interp final angle
			return - ((1.0f - alpha) * sin[index] + alpha * sin[index + 1]);
		}
		//System.out.println("Sin of " + angle);
	}

	static final public float cos(float a)
	{
		a += 90.0f;
		// Limit range if needed.
		if (a > 360)
			a %= 360;
		else if (a < 0)
		{
			a %= 360;
			a += 360;
		}
		int angle = (int) (a * sinScale);

		if (angle < (180 * sinScale) + 1)
		{
			if (angle < (90 * sinScale) + 1)
			{
				return sin[angle];
			}
			else
			{
				return sin[(180 * sinScale) - angle];
			}
		}
		else
		{
			if (angle < (270 * sinScale) + 1)
			{
				return -sin[angle - (180 * sinScale)];
			}
			else
			{
				return -sin[(360 * sinScale) - angle];
			}
		}
	}

	static final public double fastSqrt(double fp)
	{
		if (fp <= 0)
			return 0;
		int expo;
		double root;

		// split in to hi and lo bits
		long bitValue = Double.doubleToRawLongBits(fp);
		int lo = (int) (bitValue);
		int hi = (int) (bitValue >> 32);

		// pull out exponent
		expo = hi >> 20;
		expo -= 0x3fe;
		//System.out.println("expo2 " + expo);

		// clear exponent and set "normalized" bits
		hi &= 0x000fffff;
		hi += 0x3fe00000;

		// assemble back to double bits
		bitValue = ((long) (hi) << 32) + lo;

		fp = Double.longBitsToDouble(bitValue);
		//System.out.println("number " + fp);

		// find square for mantissa
		root = A + B * fp;

		root = 0.5 * (fp / root + root);
		root = 0.5 * (fp / root + root);
		root = 0.5 * (fp / root + root);
		root = 0.5 * (fp / root + root);
		//root = 0.5 * (fp/root + root);
		fp = root;

		//short expoTmp = (short)(expo & 1);
		// find square for expo
		if ((expo & 1) != 0)
		{
			fp *= factor;
			++expo;
		}
		if (expo < 0)
			expo = (short) (expo / 2);
		else
			expo = (short) ((expo + 1) / 2);

		//System.out.println("root " + fp);	
		expo += 0x3fe;

		// split back to hi and lo bits
		bitValue = Double.doubleToLongBits(fp);

		lo = (int) (bitValue);
		hi = (int) (bitValue >> 32);

		// put in exponent
		hi &= 0x000fffff;
		hi += expo << 20;

		// assemble back to double bits
		bitValue = ((long) (hi) << 32) + lo;

		//bitValue &= ((long)0x000fffff) << 32;
		//bitValue += (long)(expo) << 52 ;// | lo;
		fp = Double.longBitsToDouble(bitValue);
		//System.out.println("bitValue " + bitValue);	
		return fp;
	}

	static final public double fasterSqrt(double fp)
	{
		if (fp <= 0)
			return 0;
		int expo;
		double root;

		// split in to hi and lo bits
		long bitValue = Double.doubleToRawLongBits(fp);
		int lo = (int) (bitValue);
		int hi = (int) (bitValue >> 32);

		// pull out exponent
		expo = hi >> 20;
		expo -= 0x3fe;
		//System.out.println("expo2 " + expo);

		hi &= 0x000fffff;
		hi += 0x3fe00000;

		// assemble back to double bits
		//bitValue &= ((long)0x000fffff) << 32;
		//bitValue += ((long)0x3fe00000) << 32;// |lo

		//bitValue = (long)(hi) << 32 | lo;

		fp = Double.longBitsToDouble(((long) (hi) << 32) + lo);
		//System.out.println("number " + fp);

		// find square for decimal
		root = A + B * fp;

		root = 0.5 * (fp / root + root);
		root = 0.5 * (fp / root + root);
		//root = 0.5 * (fp/root + root);
		//root = 0.5 * (fp/root + root);
		//root = 0.5 * (fp/root + root);
		fp = root;

		//fp = Math.sqrt(fp);
		//System.out.println("square " + fp);

		if ((expo & 1) != 0)
		{
			fp *= factor;
			++expo;
		}
		//System.out.println("expo2 " + expo);

		if (expo < 0)
			expo = expo >> 1;
		else
			expo = (expo + 1) >> 1;

		//System.out.println("root " + fp);	
		expo += 0x3fe;

		// split in to hi and lo bits
		bitValue = Double.doubleToRawLongBits(fp);

		lo = (int) (bitValue);
		hi = (int) (bitValue >> 32);

		// put in exponent
		hi &= 0x000fffff;
		hi += expo << 20;

		// assemble back to double bits
		// three step here are merged in
		// the last line
		//bitValue = (long)(hi) << 32 | lo;
		//bitValue &= ((long)0x000fffff) << 32;
		//bitValue += (long)(expo) << 52 ;// | lo;

		//System.out.println("bitValue " + ((long)(hi) << 32) + lo);	
		return Double.longBitsToDouble(((long) (hi) << 32) + lo);
	}

	static final public float fastSqrtF(float fp)
	{
		if (fp <= 0)
			return 0;

		int expo;
		float root;

		int bitValue = Float.floatToRawIntBits(fp);

		// pull out exponent bits, put in expo
		expo = (bitValue >> 23);
		// subtract exponent bias
		expo -= 126;
		//expo -= 0x7E;
		//System.out.println("expo " + expo);

		// clear exponent in bitValue
		bitValue &= 0x7fffff;
		// sets expo bits to 127
		// which is really 1 with the bais of 126
		// effective making the number decimal only
		bitValue |= 0x3F800000; // ( 127 << 23 )
		// 1111111 0000000 00000000 00000000

		// turn bitValue back into decimal float for more processing
		fp = Float.intBitsToFloat(bitValue);

		// find square for decimal using Magic numbers
		root = Af + Bf * fp;

		// repeat for more accuracy, but slower function	
		root = 0.5f * (fp / root + root);
		root = 0.5f * (fp / root + root);
		root = 0.5f * (fp / root + root);
		//root = 0.5f * (fp/root + root);
		//root = 0.5f * (fp/root + root);
		//fp = root;

		if ((expo & 1) == 0)
		{
			//System.out.println("Odd expo");
			root *= factor;
		}
		expo++;
		expo >>= 1;
		expo += 126;

		// once again turn back to bits for recombining
		bitValue = Float.floatToRawIntBits(root);

		bitValue &= 0x7fffff;
		// put in exponent into bitValue
		//if ((expo & 0x01) != 0) bitValue |= 0x800000;

		bitValue += (expo << 23);

		// return final float from bit twisting	
		return Float.intBitsToFloat(bitValue);
	}

	static final public float fasterSqrtF(float fp)
	{
		if (fp <= 0)
			return 0;

		int expo;
		float root;
		int bitValue = Float.floatToRawIntBits(fp);

		// pull out exponent bits, put in expo
		expo = (bitValue >> 23);
		// subtract exponent bias
		expo -= 126;
		bitValue &= (0x7fffff); //+ 0x3f80000);
		bitValue |= 0x3F800000; // ( 128 << 23 ); 
		fp = Float.intBitsToFloat(bitValue);
		root = Af + Bf * fp;
		// repeat for more accuracy, but slower function	
		root = 0.5f * (fp / root + root);
		root = 0.5f * (fp / root + root);
		//root = 0.5f * (fp/root + root);
		//root = 0.5f * (fp/root + root);
		//root = 0.5f * (fp/root + root);
		if ((expo & 1) == 0)
		{
			root *= factor;
		}
		expo++;
		expo >>= 1;
		expo += 126;
		//expo = ((expo + 1)>>1)+126;

		bitValue = Float.floatToRawIntBits(root);
		bitValue &= 0x7fffff;
		bitValue += (expo << 23);
		// possible alternative for speed
		//bitValue = (bitValue & 0x7fffff) + (expo << 23);

		return Float.intBitsToFloat(bitValue);
	}

	static final public float fastInverseSqrt(float x)
	{
		float xhalf = 0.5f * x;
		int bitValue = Float.floatToRawIntBits(x);
		bitValue = 0x5f3759df - (bitValue >> 1);
		x = Float.intBitsToFloat(bitValue);
		// repeat for more accuracy, but slower function
		x = x * (1.5f - xhalf * x * x);
		//x = x*(1.5f - xhalf*x*x);
		//x = x*(1.5f - xhalf*x*x);
		//x = x*(1.5f - xhalf*x*x);
		//x = x*(1.5f - xhalf*x*x);
		return x;
	}

}
