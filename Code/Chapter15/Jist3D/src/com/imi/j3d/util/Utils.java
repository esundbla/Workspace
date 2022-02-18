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
package com.imi.j3d.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureCubeMap;

import com.sun.j3d.utils.image.TextureLoader;

public final class Utils
{
	public static final int GENERATE_MIPMAP = TextureLoader.GENERATE_MIPMAP;

	static public boolean textureHasAlpha(Texture tex)
	{
		boolean alpha = false;
		int type = tex.getImage(0).getFormat();
		switch (type)
		{
			case ImageComponent.FORMAT_RGBA :
			case ImageComponent.FORMAT_RGB5_A1 :
			case ImageComponent.FORMAT_RGBA4 :
			case ImageComponent.FORMAT_LUM4_ALPHA4 :
			case ImageComponent.FORMAT_LUM8_ALPHA8 :
				alpha = true;
				break;
			default :
				break;
		}
		return alpha;
	}

	static public Texture loadTexture(String filename, int flags)
	{
		return loadTexture(filename, "RGB", flags);
	}

	static public Texture loadTexture(String filename, String type, int flags)
	{
		Texture out = null;
		Image img = null;
		//String type = "RGB";
		TextureLoader tl;
		FileInputStream F;
		try
		{
			if (filename.endsWith(".rgba")
				|| filename.endsWith(".rgb")
				|| filename.endsWith(".inta")
				|| filename.endsWith(".int")
				|| filename.endsWith(".sgi")
				|| filename.endsWith(".SGI")
				|| filename.endsWith(".RGBA")
				|| filename.endsWith(".RGB")
				|| filename.endsWith(".INTA")
				|| filename.endsWith(".INT"))
			{
				F = new FileInputStream(filename);
				RGBImageProducer rgb = new RGBImageProducer(F);
				img = Toolkit.getDefaultToolkit().createImage(rgb);
				type = rgb.getType();
				//System.out.println("Texture type " + type);

			}
			else
			{
				img = Toolkit.getDefaultToolkit().getImage(filename);
			}
			//System.out.println(img);
			//Component dummy = new Label();
			tl = new TextureLoader(img, type, flags, null);

			//System.out.println(tl);
			out = tl.getTexture();
		}
		catch (Exception e)
		{
			System.out.println("Failed to load " + filename);
			e.printStackTrace();
		}
		return out;
	}

	static public Texture loadTexture(String filename)
	{
		Texture out = null;
		BufferedImage bi = null;
		//String type = "RGB";
		//TextureLoader tl;
		bi = loadBufferedImage(filename);
		if (bi != null)
		{

			ImageComponent2D ic = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
			out = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, ic.getWidth(), ic.getHeight());
			out.setImage(0, ic);
			//System.out.println(tl);
			//out = tl.getTexture();
		}
		return out;
	}

	static public TextureCubeMap loadTextureCubeMap(
		String filename1,
		String filename2,
		String filename3,
		String filename4,
		String filename5,
		String filename6)
	{
		TextureCubeMap out = null;
		BufferedImage bi1, bi2, bi3, bi4, bi5, bi6 = null;
		//String type = "RGB";
		bi1 = loadBufferedImage(filename1);
		bi2 = loadBufferedImage(filename2);
		bi3 = loadBufferedImage(filename3);
		bi4 = loadBufferedImage(filename4);
		bi5 = loadBufferedImage(filename5);
		bi6 = loadBufferedImage(filename6);
		if (bi1 != null && bi2 != null && bi3 != null && bi4 != null && bi5 != null && bi6 != null)
		{

			ImageComponent2D ic1 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi1);
			ImageComponent2D ic2 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi2);
			ImageComponent2D ic3 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi3);
			ImageComponent2D ic4 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi4);
			ImageComponent2D ic5 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi5);
			ImageComponent2D ic6 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi6);
			out = new TextureCubeMap(Texture2D.BASE_LEVEL, Texture2D.RGB, ic1.getWidth(), 0);
			out.setImage(0, TextureCubeMap.POSITIVE_X, ic1);
			out.setImage(0, TextureCubeMap.NEGATIVE_X, ic2);
			out.setImage(0, TextureCubeMap.POSITIVE_Y, ic3);
			out.setImage(0, TextureCubeMap.NEGATIVE_Y, ic4);
			out.setImage(0, TextureCubeMap.POSITIVE_Z, ic5);
			out.setImage(0, TextureCubeMap.NEGATIVE_Z, ic6);
			//System.out.println(tl);
			//out = tl.getTexture();
		}
		return out;
	}

	static public BufferedImage loadBufferedImage(String filename)
	{
		BufferedImage bi = null;
		try
		{
			FileInputStream F;
			Image img = null;
			if (filename.endsWith(".rgba")
				|| filename.endsWith(".rgb")
				|| filename.endsWith(".inta")
				|| filename.endsWith(".int")
				|| filename.endsWith(".sgi")
				|| filename.endsWith(".SGI")
				|| filename.endsWith(".RGBA")
				|| filename.endsWith(".RGB")
				|| filename.endsWith(".INTA")
				|| filename.endsWith(".INT"))
			{
				F = new FileInputStream(filename);
				RGBImageProducer rgb = new RGBImageProducer(F);
				img = Toolkit.getDefaultToolkit().createImage(rgb);
				//type = rgb.getType();	     
				//System.out.println("Texture type " + type);

			}
			else
			{
				img = Toolkit.getDefaultToolkit().getImage(filename);
			}
			//System.out.println(img);
			//Component dummy = new Label();
			//tl = new TextureLoader( img, type, flags, null );
			Label dummy = new Label();
			try
			{
				MediaTracker tracker = new MediaTracker(dummy);
				tracker.addImage(img, 0);
				tracker.waitForID(0);
			}
			catch (Exception e)
			{
			}
			int width = img.getWidth(dummy);
			int height = img.getHeight(dummy);
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D biContext = bi.createGraphics();
			biContext.drawImage(img, 0, 0, null);
		}
		catch (Exception e)
		{
			System.out.println("Failed to load " + filename);
			e.printStackTrace();
		}
		return bi;
	}
}