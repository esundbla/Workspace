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

import net.java.games.jogl.GL;
import net.java.games.jogl.GLU;
import java.io.IOException;
import java.util.*;
/*
 * Adapted from Texture support classes written by 
 * Gregory Pierce <gregorypierce@mac.com>
 * by Shawn Kendall
 */
public class TextureManager
{
	private Map textures = new HashMap();
	private static TextureManager instance;

	protected TextureManager()
	{
	}

	public static synchronized TextureManager getInstance()
	{
		if (instance == null)
		{
			instance = new TextureManager();
		}

		return instance;
	}

	public void bindTexture(String name, GL gl)
	{
		((Texture)textures.get(name)).bind(gl);
	}

	public void manageTexture(Texture texture)
	{
		System.out.println("Managing texture [" + texture.getName() + "]");
		textures.put(texture.getName(), texture);
	}

	public Texture createTexture(
		String name,
		String resourceName,
		int target,
		int srcPixelFormat,
		int dstPixelFormat,
		int minFilter,
		int magFilter,
		boolean wrap,
		boolean mipmapped,
		GL gl, GLU glu)
		throws IOException, TextureFormatException
	{
		return TextureFactory.getFactory(gl).createTexture(
			name,
			resourceName,
			target,
			srcPixelFormat,
			dstPixelFormat,
			minFilter,
			magFilter,
			wrap,
			mipmapped, glu);
	}

	public Texture createManagedTexture(
		String name,
		String resourceName,
		int target,
		int srcPixelFormat,
		int dstPixelFormat,
		int minFilter,
		int magFilter,
		boolean wrap,
		boolean mipmapped,
		GL gl, GLU glu)
		throws IOException, TextureFormatException
	{
		Texture texture =
			TextureFactory.getFactory(gl).createTexture(
				name,
				resourceName,
				target,
				srcPixelFormat,
				dstPixelFormat,
				minFilter,
				magFilter,
				wrap,
				mipmapped, glu);
		System.out.println("Texture " + texture);
		manageTexture(texture);

		return texture;
	}

	public Texture createManagedTexture(
		javax.media.j3d.Texture textureIn,
		GL gl, GLU glu)
		throws IOException, TextureFormatException
	{
		Texture texture = (Texture)textures.get(textureIn.toString());
		//System.out.println("Texture " + texture);
		if (texture == null)
		{
			texture =
				TextureFactory.getFactory(gl).createTexture(
					textureIn.toString(),
					textureIn, glu);
			System.out.println(" Created new Texture " + texture);
			textures.put(textureIn.toString(), texture);
		}
		if (texture == null)
			System.out.println(" Created new Texture ERROR");
		return texture;
	}

	public Texture createManagedTextureUnbound(
		javax.media.j3d.Texture textureIn)
		throws IOException, TextureFormatException
	{
		Texture texture = (Texture)textures.get(textureIn.toString());
		//System.out.println("Texture " + texture);
		if (texture == null)
		{
			texture =
				TextureFactory.createTextureUnbound(
					textureIn.toString(),
					textureIn);
			System.out.println(" Created new Texture " + texture);
			textures.put(textureIn.toString(), texture);
		}
		if (texture == null)
			System.out.println(" Created new Texture ERROR");
		return texture;
	}
}