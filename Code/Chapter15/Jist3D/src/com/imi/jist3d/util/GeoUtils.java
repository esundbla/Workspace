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
package com.imi.jist3d.util;

import vecmath.*;
/**
 * @author Shawn Kendall
 *
 */
public class GeoUtils
{
	static public float[] compactAndIndexify(
		Triangle[] triangle,
		int[] faceIndices)
	{
		int count = 0;
		boolean found = false;
		float[] vertsBuffer = new float[triangle.length * 3 * 3];
		System.out.println("triangle.length " + triangle.length);
		/*
		for (int i = 0; i < face.length; i++)
		{
			System.out.println("Face " + i + " p1.x " + face[i].p1.x);
			System.out.println("Face " + i + " p1.y " + face[i].p1.y);
			System.out.println("Face " + i + " p1.z " + face[i].p1.z);
			System.out.println("Face " + i + " p2.x " + face[i].p2.x);
			System.out.println("Face " + i + " p2.y " + face[i].p2.y);
			System.out.println("Face " + i + " p2.z " + face[i].p2.z);
			System.out.println("Face " + i + " p3.x " + face[i].p3.x);
			System.out.println("Face " + i + " p3.y " + face[i].p3.y);
			System.out.println("Face " + i + " p3.z " + face[i].p3.z);
		}
		*/
		for (int i = 0; i < triangle.length; i++)
		{
			// search for p1
			found = false;
			for (int j = 0; j < count; j++)
			{
				//System.out.println("i j " + i + " " + j);
				if (triangle[i].p1.x == vertsBuffer[j * 3]
					&& triangle[i].p1.y == vertsBuffer[j * 3 + 1]
					&& triangle[i].p1.z == vertsBuffer[j * 3 + 2])
				{
					//face[i].p1i = j;
					faceIndices[i * 3] = j;
					found = true;
					//System.out.println("Found face " + i + " p1 match at " + j);
					break;
				}
			}
			if (!found)
			{
				//System.out.println("Add face " + i + " p1 " + count);
				vertsBuffer[count * 3 + 0] = triangle[i].p1.x;
				vertsBuffer[count * 3 + 1] = triangle[i].p1.y;
				vertsBuffer[count * 3 + 2] = triangle[i].p1.z;
				//face[i].p1i = count;
				faceIndices[i * 3] = count;
				count++;
			}
			// search for p2
			found = false;
			for (int j = 0; j < count; j++)
			{
				if (triangle[i].p2.x == vertsBuffer[j * 3]
					&& triangle[i].p2.y == vertsBuffer[j * 3 + 1]
					&& triangle[i].p2.z == vertsBuffer[j * 3 + 2])
				{
					//face[i].p2i = j;
					faceIndices[i * 3 + 1] = j;
					found = true;
					//System.out.println("Found face " + i + " p2 match at " + j);
					break;
				}
			}
			if (!found)
			{
				//System.out.println("Add face " + i + " p2 " + count);
				vertsBuffer[count * 3 + 0] = triangle[i].p2.x;
				vertsBuffer[count * 3 + 1] = triangle[i].p2.y;
				vertsBuffer[count * 3 + 2] = triangle[i].p2.z;
				//face[i].p2i = count;
				faceIndices[i * 3 + 1] = count;
				count++;
			}
			// search for p3
			found = false;
			for (int j = 0; j < count; j++)
			{
				if (triangle[i].p3.x == vertsBuffer[j * 3]
					&& triangle[i].p3.y == vertsBuffer[j * 3 + 1]
					&& triangle[i].p3.z == vertsBuffer[j * 3 + 2])
				{
					//face[i].p3i = j;
					faceIndices[i * 3 + 2] = j;
					found = true;
					//System.out.println("Found face " + i + " p3 match at " + j);
					break;
				}
			}
			if (!found)
			{
				//System.out.println("Add face " + i + " p3 " + count);
				vertsBuffer[count * 3 + 0] = triangle[i].p3.x;
				vertsBuffer[count * 3 + 1] = triangle[i].p3.y;
				vertsBuffer[count * 3 + 2] = triangle[i].p3.z;
				//face[i].p3i = count;
				faceIndices[i * 3 + 2] = count;
				count++;
			}

		}
		System.out.println("Compact Count " + count);

		float[] vertsCompact = new float[count * 3];
		for (int i = 0; i < count * 3; i++)
		{
			vertsCompact[i] = vertsBuffer[i];
			//System.out.println("vertsCompact[i] " + vertsCompact[i]);
		}
		return vertsCompact;
	}

	static public float[] generateTextureCoordsSphere(float[] verts)
	{
		int count = (verts.length / 3) * 2;
		float[] texCoords = new float[count];
		//tu = asin(Nx)/PI + 0.5 
		//tv = asin(Ny)/PI + 0.5 
		for (int i = 0; i < verts.length / 3; i++)
		{
			//texCoords[i*2+1] = ( verts[i*3+1] + 1.0f )/2.0f;
			texCoords[i * 2 + 1] =
				1.0f - (float) ((Math.acos(verts[i * 3 + 1]) / (Math.PI)));
			//if ( verts[i*3] == 0.0f )
			//	texCoords[i*2] = 0.0f;
			//else
			if (verts[i * 3] < 0)
			{
				//texCoords[i*2] = (float)(Math.asin(verts[i*3+2])/(Math.PI) + 0.5);
				//texCoords[i*2] = (float)(verts[i*3+2]/2 + 0.5);
				texCoords[i * 2] =
					- (float)
						((Math.atan(verts[i * 3 + 2] / verts[i * 3])
							/ (Math.PI))
							- 0.5)
						/ 2.0f;
			}
			else
			{
				//texCoords[i*2] = 1.0f - (float)(Math.asin(verts[i*3+2])/(Math.PI) + 0.5);
				//texCoords[i*2] = 1.0f - (float)(verts[i*3+2]/2 + 0.5);
				texCoords[i * 2] =
					- (float)
						((Math.atan(verts[i * 3 + 2] / verts[i * 3])
							/ (Math.PI))
							- 0.5)
						/ 2.0f
						+ 0.5f;
				//texCoords[i*2+1] = 0.0f;
			}
		}
		return texCoords;
	}

	static public Triangle[] makeSphereTriangles(int iterations)
	{
		if (iterations > 7)
			return null;
		//int iterations = 5;
		Triangle[] f = new Triangle[(int)Math.pow(4, iterations) * 8];
		//Face[] f = new Face[8];
		int i, it;
		for (i = 0; i < f.length; i++)
			f[i] = new Triangle();
		Vector3f[] p = new Vector3f[6];
		p[0] = new Vector3f(0, 1, 0);
		p[1] = new Vector3f(0, -1, 0);
		p[2] = new Vector3f(0, 0, 1);
		p[3] = new Vector3f(1, 0, 0);
		p[4] = new Vector3f(0, 0, -1);
		p[5] = new Vector3f(-1, 0, 0);

		Vector3f pa = new Vector3f();
		Vector3f pb = new Vector3f();
		Vector3f pc = new Vector3f();
		int nt = 0, ntold;

		/* Create the level 0 object */
		/*
		double a;
		a = 1 / Math.sqrt(2.0);
		for (i=0;i<6;i++) {
			  p[i].x *= a;
			  p[i].y *= a;
		}
		*/
		f[0].p1.set(p[0]);
		f[0].p2.set(p[3]);
		f[0].p3.set(p[4]);
		f[1].p1.set(p[0]);
		f[1].p2.set(p[4]);
		f[1].p3.set(p[5]);
		f[2].p1.set(p[0]);
		f[2].p2.set(p[5]);
		f[2].p3.set(p[2]);
		f[3].p1.set(p[0]);
		f[3].p2.set(p[2]);
		f[3].p3.set(p[3]);
		f[4].p1.set(p[1]);
		f[4].p2.set(p[4]);
		f[4].p3.set(p[3]);
		f[5].p1.set(p[1]);
		f[5].p2.set(p[5]);
		f[5].p3.set(p[4]);
		f[6].p1.set(p[1]);
		f[6].p2.set(p[2]);
		f[6].p3.set(p[5]);
		f[7].p1.set(p[1]);
		f[7].p2.set(p[3]);
		f[7].p3.set(p[2]);
		nt = 8;

		if (iterations < 1)
			return (f);

		/* Bisect each edge and move to the surface of a unit sphere */

		for (it = 0; it < iterations; it++)
		{
			ntold = nt;
			for (i = 0; i < ntold; i++)
			{
				pa.x = (f[i].p1.x + f[i].p2.x) / 2;
				pa.y = (f[i].p1.y + f[i].p2.y) / 2;
				pa.z = (f[i].p1.z + f[i].p2.z) / 2;
				pb.x = (f[i].p2.x + f[i].p3.x) / 2;
				pb.y = (f[i].p2.y + f[i].p3.y) / 2;
				pb.z = (f[i].p2.z + f[i].p3.z) / 2;
				pc.x = (f[i].p3.x + f[i].p1.x) / 2;
				pc.y = (f[i].p3.y + f[i].p1.y) / 2;
				pc.z = (f[i].p3.z + f[i].p1.z) / 2;
				pa.normalize();
				pb.normalize();
				pc.normalize();
				f[nt].p1.set(f[i].p1);
				f[nt].p2.set(pa);
				f[nt].p3.set(pc);
				nt++;
				f[nt].p1.set(pa);
				f[nt].p2.set(f[i].p2);
				f[nt].p3.set(pb);
				nt++;
				f[nt].p1.set(pb);
				f[nt].p2.set(f[i].p3);
				f[nt].p3.set(pc);
				nt++;
				f[i].p1.set(pa);
				f[i].p2.set(pb);
				f[i].p3.set(pc);
			}
		}
		return (f);
	}

	//no good faces. 
	static public Triangle[] makeCubeTriangles()
	{

		Vector3f[] verts = new Vector3f[8];
		verts[0] = new Vector3f(0.5f, 0.5f, 0.5f);
		verts[1] = new Vector3f(-0.5f, 0.5f, 0.5f);
		verts[2] = new Vector3f(-0.5f, -0.5f, 0.5f);
		verts[3] = new Vector3f(0.5f, -0.5f, 0.5f);
		verts[4] = new Vector3f(0.5f, 0.5f, -0.5f);
		verts[5] = new Vector3f(-0.5f, 0.5f, -0.5f);
		verts[6] = new Vector3f(-0.5f, -0.5f, -0.5f);
		verts[7] = new Vector3f(0.5f, -0.5f, -0.5f);

		Triangle[] faces = new Triangle[12];
		int i, it;

		for (i = 0; i < faces.length; i++)
			faces[i] = new Triangle();

		//top	
		faces[0].p1.set(verts[0]);
		faces[0].p2.set(verts[4]);
		faces[0].p3.set(verts[5]);

		faces[1].p1.set(verts[0]);
		faces[1].p2.set(verts[5]);
		faces[1].p3.set(verts[1]);

		//bottom	
		faces[2].p1.set(verts[3]);
		faces[2].p2.set(verts[6]);
		faces[2].p3.set(verts[7]);

		faces[3].p1.set(verts[3]);
		faces[3].p2.set(verts[2]);
		faces[3].p3.set(verts[6]);

		//front	
		faces[4].p1.set(verts[0]);
		faces[4].p2.set(verts[1]);
		faces[4].p3.set(verts[2]);

		faces[5].p1.set(verts[0]);
		faces[5].p2.set(verts[2]);
		faces[5].p3.set(verts[3]);

		//back	
		faces[6].p1.set(verts[4]);
		faces[6].p2.set(verts[6]);
		faces[6].p3.set(verts[5]);

		faces[7].p1.set(verts[4]);
		faces[7].p2.set(verts[7]);
		faces[7].p3.set(verts[6]);

		//left	
		faces[8].p1.set(verts[1]);
		faces[8].p2.set(verts[5]);
		faces[8].p3.set(verts[6]);

		faces[9].p1.set(verts[1]);
		faces[9].p2.set(verts[6]);
		faces[9].p3.set(verts[2]);

		//right	
		faces[10].p1.set(verts[0]);
		faces[10].p2.set(verts[7]);
		faces[10].p3.set(verts[4]);

		faces[11].p1.set(verts[0]);
		faces[11].p2.set(verts[3]);
		faces[11].p3.set(verts[7]);

		return faces;
	}
}