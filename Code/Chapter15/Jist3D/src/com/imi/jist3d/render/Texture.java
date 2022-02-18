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
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;

/*
 * Adapted from Texture support classes written by 
 * Gregory Pierce <gregorypierce@mac.com>
 * by Shawn Kendall
 */
public class Texture
{
	private String name;
	protected int width, height;
	protected int textureID = -1;

	protected BufferedImage bufferedImage;
	protected ByteBuffer imageByteBuffer = null;
	protected int target;
	protected int srcPixelFormat;
	protected int dstPixelFormat;
	protected int minFilter;
	protected int magFilter;
	protected boolean wrap;
	protected boolean mipmapped = false;
	public boolean hasAlpha = false;

	public Texture(
		String name,
		int target,
		int srcPixelFormat,
		int dstPixelFormat,
		int minFilter,
		int magFilter,
		boolean wrap,
		boolean mipmapped)
	{
		this.name = name;
		this.target = target;
		this.srcPixelFormat = srcPixelFormat;
		this.dstPixelFormat = dstPixelFormat;
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		this.wrap = wrap;
		this.mipmapped = mipmapped;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BufferedImage getBufferedImage()
	{
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage)
	{
		this.bufferedImage = bufferedImage;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getTextureID()
	{
		return textureID;
	}

	public void setTextureID(int textureID)
	{
		this.textureID = textureID;
	}

	public int getTarget()
	{
		return target;
	}

	public void setTarget(int target)
	{
		this.target = target;
	}

	public int getSrcPixelFormat()
	{
		return srcPixelFormat;
	}

	public void setSrcPixelFormat(int srcPixelFormat)
	{
		this.srcPixelFormat = srcPixelFormat;
	}

	public int getDstPixelFormat()
	{
		return dstPixelFormat;
	}

	public void setDstPixelFormat(int dstPixelFormat)
	{
		this.dstPixelFormat = dstPixelFormat;
	}

	public int getMinFilter()
	{
		return minFilter;
	}

	public void setMinFilter(int minFilter)
	{
		this.minFilter = minFilter;
	}

	public int getMagFilter()
	{
		return magFilter;
	}

	public void setMagFilter(int magFilter)
	{
		this.magFilter = magFilter;
	}

	public boolean isWrap()
	{
		return wrap;
	}

	public void setWrap(boolean wrap)
	{
		this.wrap = wrap;
	}

	public boolean isMipmapped()
	{
		return mipmapped;
	}

	public void setMipmapped(boolean mipmapped)
	{
		this.mipmapped = mipmapped;
	}

//TODO remove this call and JOGL dependancy?
	protected void bind(GL gl)
	{
		//if (textureID != -1)
		gl.glBindTexture(target, textureID);
	}
}
