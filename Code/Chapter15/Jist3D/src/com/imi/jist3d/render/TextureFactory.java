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
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.nio.*;
import java.awt.*;
import com.imi.j3d.util.RGBImageProducer;
import java.awt.color.ColorSpace;
import java.util.*;

/*
 * Adapted from Texture support classes written by 
 * Gregory Pierce <gregorypierce@mac.com>
 * by Shawn Kendall
 * 
 * Not entirely tested
 * Some methods may fail in there usage
 *
 */
public class TextureFactory
{
	private static TextureFactory instance;

	//private GLU      glu;
	private GL gl;
	static private ColorModel glAlphaColorModel;
	static private ColorModel glColorModel;

	// uncomment to see textures loading
	// plus more elsewhere
//	static Frame frame = null;
//	static Canvas canvas = null;

	protected TextureFactory(GL gl)
	{
		this.gl = gl;
		//this.glu = glu;
		glAlphaColorModel =
			new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] { 8, 8, 8, 8 },
				true,
				false,
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);

		glColorModel =
			new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] { 8, 8, 8, 0 },
				false,
				false,
				ComponentColorModel.OPAQUE,
				DataBuffer.TYPE_BYTE);
		// uncomment to see textures loading
//		frame = new Frame();
//		canvas = new Canvas();
//		frame.add(canvas);
//		frame.setSize(512,512);
//		frame.show();
	}

	public static synchronized TextureFactory getFactory(GL gl)
	{
		if (instance == null)
		{
			instance = new TextureFactory(gl);
		}
		return instance;
	}

	static protected BufferedImage loadImage(String filename)
		throws IOException
	{
		System.out.println("Loading resource [" + filename + "]");
		Image img = null;
		String type = "RGB";
		BufferedImage bufferedImage = null;
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
				//System.out.println("img " + img);
				if (img != null)
				{
					//System.out.println("Using custom texture loader.... - no trilinear mip");
					Label dummy = new Label();
					try
					{
						MediaTracker tracker = new MediaTracker(dummy);
						tracker.addImage(img, 0);
						tracker.waitForID(0);
						//System.out.println("tracker " + img);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					int width = img.getWidth(dummy);
					int height = img.getHeight(dummy);

					int bufferedType = BufferedImage.TYPE_INT_RGB;
					if (type.equals("INTENSITY"))
					{
						bufferedType = BufferedImage.TYPE_BYTE_GRAY;
					}
					else if (type.equals("LUMINANCE_ALPHA"))
					{
						bufferedType = BufferedImage.TYPE_INT_ARGB;
					}
					else if (type.equals("RGBA"))
					{
						bufferedType = BufferedImage.TYPE_INT_ARGB;
					}
					bufferedImage =
						new BufferedImage(width, height, bufferedType);
					//System.out.println("bufferedImage " + bufferedImage);
					Graphics2D biContext = bufferedImage.createGraphics();
					//System.out.println("Graphics2D " + biContext);
					biContext.drawImage(img, 0, 0, null);
				}
			}
			else
			{
				bufferedImage = ImageIO.read(new File(filename));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Flip Image
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op =
			new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);

		return bufferedImage;
		//return  ImageIO.read( getClass().getClassLoader().getResourceAsStream( resourceName ));
	}

	static private ByteBuffer convertImageData(
		BufferedImage bufferedImage,
		boolean alpha,
		Texture texture)
		throws IOException
	{
		ByteBuffer imageBuffer = null;
		WritableRaster raster;
		BufferedImage texImage;

		int texWidth = 2;
		int texHeight = 2;

		while (texWidth < bufferedImage.getWidth())
		{
			texWidth *= 2;
		}
		while (texHeight < bufferedImage.getHeight())
		{
			texHeight *= 2;
		}

		texture.setHeight(texHeight);
		texture.setWidth(texWidth);

		if (alpha)
		{
			System.out.println("Alpha image ");
			raster =
				Raster.createInterleavedRaster(
					DataBuffer.TYPE_BYTE,
					texWidth,
					texHeight,
					4,
					null);
			texImage =
				new BufferedImage(
					glAlphaColorModel,
					raster,
					true,
					new Hashtable());
		}
		else
		{
			raster =
				Raster.createInterleavedRaster(
					DataBuffer.TYPE_BYTE,
					texWidth,
					texHeight,
					3,
					null);
			texImage =
				new BufferedImage(glColorModel, raster, false, new Hashtable());
		}

		texImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
		byte[] data = null;
		
		//uncomment to see textures loading
//		canvas.getGraphics().drawImage(bufferedImage,0,0,null);

		if (alpha)
		{
			data =
				((DataBufferByte)texImage.getRaster().getDataBuffer())
					.getData();
		}
		else
		{
			data =
				((DataBufferByte)texImage.getRaster().getDataBuffer())
					.getData();
		}

		imageBuffer = ByteBuffer.allocateDirect((int)(data.length * 1.3f));
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);

		return imageBuffer;
	}

	static protected ByteBuffer convertImageDataOld(BufferedImage bufferedImage)
		throws TextureFormatException
	{
		ByteBuffer imageBuffer = null;
		//System.out.println("convertImageData " + bufferedImage);
		switch (bufferedImage.getType())
		{
			case BufferedImage.TYPE_3BYTE_BGR :
			case BufferedImage.TYPE_CUSTOM :
				{
					byte[] data =
						((DataBufferByte)bufferedImage
							.getRaster()
							.getDataBuffer())
							.getData();
					imageBuffer = ByteBuffer.allocateDirect((int)(data.length * 1.3f));
					imageBuffer.order(ByteOrder.nativeOrder());
					imageBuffer.put(data, 0, data.length);
					break;
				}

			case BufferedImage.TYPE_INT_BGR :
				{
					//System.out.println("BufferedImage.TYPE_INT_BGR");
					int[] data =
						((DataBufferInt)bufferedImage
							.getRaster()
							.getDataBuffer())
							.getData();
					imageBuffer = ByteBuffer.allocateDirect(data.length * 4);
					imageBuffer.order(ByteOrder.nativeOrder());
					imageBuffer.asIntBuffer().put(data, 0, data.length);
					break;
				}
			case BufferedImage.TYPE_INT_RGB :
				{
					//System.out.println("BufferedImage.TYPE_INT_RGB");
					int[] data =
						((DataBufferInt)bufferedImage
							.getRaster()
							.getDataBuffer())
							.getData();
					imageBuffer = ByteBuffer.allocateDirect(data.length * 4);
					imageBuffer.order(ByteOrder.nativeOrder());
					//imageBuffer.order(ByteOrder.LITTLE_ENDIAN);					
					imageBuffer.asIntBuffer().put(data, 0, data.length);
					break;
				}

			default :
				throw new TextureFormatException(
					"Unsupported image type " + bufferedImage.getType());
		}

		return imageBuffer;
	}

	protected int createTextureID()
	{
		int[] tmp = new int[1];
		gl.glGenTextures(1, tmp);
		//System.out.println("creatTexture ID = " + tmp[0]);
		return tmp[0];
	}

	static public Texture createTextureUnbound(
		String name,
		String resourceName)
		throws IOException, TextureFormatException
	{
		//System.out.println("Creating texture [" + name + "],[" + resourceName + "]");
		int target = GL.GL_TEXTURE_2D;
		int dstPixelFormat = GL.GL_RGB;
		int srcPixelFormat = GL.GL_RGB;
		int minFilter = GL.GL_LINEAR;
		int magFilter = GL.GL_LINEAR;
		boolean wrap = true;
		boolean mipmapped = false;
		Texture texture =
			new Texture(
				name,
				target,
				srcPixelFormat,
				dstPixelFormat,
				minFilter,
				magFilter,
				wrap,
				mipmapped);

		// load the buffered image for this resource - save a copy to we can draw into it later
		BufferedImage bufferedImage = loadImage(resourceName);
		//System.out.println("BufferedImage bufferedImage " + bufferedImage);
		texture.setBufferedImage(bufferedImage);

		// convert that image into a byte buffer of texture data
		texture.imageByteBuffer = convertImageData(bufferedImage, false, texture);
		return texture;
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
		boolean mipmapped, GLU glu)
		throws IOException, TextureFormatException
	{
		//System.out.println("Creating texture [" + name + "],[" + resourceName + "]");
		Texture texture =
			new Texture(
				name,
				target,
				srcPixelFormat,
				dstPixelFormat,
				minFilter,
				magFilter,
				wrap,
				mipmapped);

		// create the texture ID for this texture
		int textureID = createTextureID();
		texture.setTextureID(textureID);

		// bind this texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);

		// load the buffered image for this resource - save a copy to we can draw into it later
		BufferedImage bufferedImage = loadImage(resourceName);
		//System.out.println("BufferedImage bufferedImage " + bufferedImage);
		texture.setBufferedImage(bufferedImage);

		// convert that image into a byte buffer of texture data
		texture.imageByteBuffer = convertImageData(bufferedImage, false, texture);

		// set up the texture wrapping mode depending on whether or not
		// this texture is specified for wrapping or not
		int wrapMode = wrap ? GL.GL_REPEAT : GL.GL_CLAMP;

		if (target == GL.GL_TEXTURE_2D)
		{
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
		}

		// create either a series of mipmaps of a single texture image based on what's loaded
		//
		//if (mipmapped)
		if(true)
		{
			
			glu.gluBuild2DMipmaps( target,
					  dstPixelFormat,
					  bufferedImage.getWidth(),
					  bufferedImage.getHeight(),
					  srcPixelFormat,
					  GL.GL_UNSIGNED_BYTE,
			texture.imageByteBuffer );
			
		}
		else
		{
			gl.glTexImage2D(
				target,
				0,
				dstPixelFormat,
				bufferedImage.getWidth(),
				bufferedImage.getHeight(),
				0,
				srcPixelFormat,
				GL.GL_UNSIGNED_BYTE,
				texture.imageByteBuffer);
		}
		return texture;
	}

	// utility method for converting from J3D to Cosmic
	public Texture createTexture(
		String name,
		javax.media.j3d.Texture textureIn, GLU glu)
		throws IOException, TextureFormatException
	{
		//   logger.debug("Creating texture [" + name + "],[" + resourceName + "]");
		int target = GL.GL_TEXTURE_2D;
		int dstPixelFormat = GL.GL_RGB;
		int srcPixelFormat = GL.GL_RGB;
		int minFilter = GL.GL_LINEAR;
		int magFilter = GL.GL_LINEAR;
		boolean wrap = true;
		boolean mipmapped = false;
		Texture texture =
			new Texture(
				name,
				target,
				dstPixelFormat,
				srcPixelFormat,
				minFilter,
				magFilter,
				wrap,
				mipmapped);

		// create the texture ID for this texture
		//
		int textureID = createTextureID();
		texture.setTextureID(textureID);
		//System.out.println("Creating texture [" + name + "],[" + textureIn + "] = " + textureID);

		// bind this texture
		//
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);

		// load the buffered image for this resource - save a copy to we can draw into it later
		//
		BufferedImage bufferedImage =
			((javax.media.j3d.ImageComponent2D)textureIn.getImage(0))
				.getImage();
		//System.out.println("BufferedImage bufferedImage " + bufferedImage);

		// Flip Image
		//

		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op =
			new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);

		texture.setBufferedImage(bufferedImage);

		// convert that image into a byte buffer of texture data
		//

		texture.hasAlpha = (textureIn.getFormat() == textureIn.RGBA);
		texture.imageByteBuffer =
			convertImageData(bufferedImage, texture.hasAlpha, texture);

		// set up the texture wrapping mode depending on whether or not
		// this texture is specified for wrapping or not
		//
		int wrapMode = wrap ? GL.GL_REPEAT : GL.GL_CLAMP;

		if (target == GL.GL_TEXTURE_2D)
		{
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
		}

		// create either a series of mipmaps of a single texture image based on what's loaded
		//
		//if (mipmapped)
			if (texture.hasAlpha)
			{
				gl.glTexImage2D(
					target,
					0,
					GL.GL_RGBA,
					bufferedImage.getWidth(),
					bufferedImage.getHeight(),
					0,
					GL.GL_RGBA,
					GL.GL_UNSIGNED_BYTE,
					texture.imageByteBuffer);
				System.out.println("Made alpha texture...");
			}
			else
			{
				if (true)
				{
					glu.gluBuild2DMipmaps(
						target,
						dstPixelFormat,
						bufferedImage.getWidth(),
						bufferedImage.getHeight(),
						srcPixelFormat,
						GL.GL_UNSIGNED_BYTE,
						texture.imageByteBuffer);
				}
				else
				{
					gl.glTexImage2D(
						target,
						0,
						dstPixelFormat,
						bufferedImage.getWidth(),
						bufferedImage.getHeight(),
						0,
						srcPixelFormat,
						GL.GL_UNSIGNED_BYTE,
						texture.imageByteBuffer);
				}
			}

		return texture;
	}
	static public Texture createTextureUnbound(
		String name,
		javax.media.j3d.Texture textureIn)
		throws IOException, TextureFormatException
	{
		//   logger.debug("Creating texture [" + name + "],[" + resourceName + "]");
		int target = GL.GL_TEXTURE_2D;
		int dstPixelFormat = GL.GL_RGB;
		int srcPixelFormat = GL.GL_RGB;
		int minFilter = GL.GL_LINEAR;
		int magFilter = GL.GL_LINEAR;
		boolean wrap = true;
		boolean mipmapped = false;
		Texture texture =
			new Texture(
				name,
				target,
				dstPixelFormat,
				srcPixelFormat,
				minFilter,
				magFilter,
				wrap,
				mipmapped);

		// create the texture ID for this texture
		//
		//int textureID = createTextureID();
		//texture.setTextureID(textureID);
		//System.out.println(
		//	"Creating texture [" + name + "],[" + textureIn + "] = " + textureID);

		// bind this texture
		//
		//gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);

		// load the buffered image for this resource - save a copy to we can draw into it later
		//
		BufferedImage bufferedImage =
			((javax.media.j3d.ImageComponent2D)textureIn.getImage(0))
				.getImage();
		//System.out.println("BufferedImage bufferedImage " + bufferedImage);

		// Flip Image
		//

		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op =
			new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);

		texture.setBufferedImage(bufferedImage);

		// convert that image into a byte buffer of texture data
		//

		texture.hasAlpha = (textureIn.getFormat() == textureIn.RGBA);
		//texture.imageByteBuffer = convertImageData(bufferedImage);

		// set up the texture wrapping mode depending on whether or not
		// this texture is specified for wrapping or not
		//
		/*
		int wrapMode = wrap ? GL.GL_REPEAT : GL.GL_CLAMP;
		
		if (target == GL.GL_TEXTURE_2D)
		{
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, wrapMode);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
			gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
		}
		*/
		// create either a series of mipmaps of a single texture image based on what's loaded
		//
		/*
		if (mipmapped)
		{
			glu.gluBuild2DMipmaps( target,
					  dstPixelFormat,
					  bufferedImage.getWidth(),
					  bufferedImage.getHeight(),
					  srcPixelFormat,
					  GL.GL_UNSIGNED_BYTE,
					  textureBuffer );
		}
		else
		{
			if (texture.hasAlpha)
			{
				gl.glTexImage2D(
					target,
					0,
				GL.GL_RGBA,
					bufferedImage.getWidth(),
					bufferedImage.getHeight(),
					0,
				GL.GL_RGBA,
					GL.GL_UNSIGNED_BYTE,
					textureBuffer);
					System.out.println("Made alpha texture...");
			}
			else
			{
				gl.glTexImage2D(
					target,
					0,
					dstPixelFormat,
					bufferedImage.getWidth(),
					bufferedImage.getHeight(),
					0,
					srcPixelFormat,
					GL.GL_UNSIGNED_BYTE,
					textureBuffer);
			}
		}
		*/
		return texture;
	}
	
	public void bindTexture(Texture texture, GLU glu)
		throws IOException, TextureFormatException
	{
		if ( texture == null) return;
		if (texture.textureID != -1)
		{
			System.out.println("Texture already bound ");
			return;
		}
		// create the texture ID for this texture
		//
		int textureID = createTextureID();
		texture.setTextureID(textureID);
		System.out.println("Binding texture " + texture + " ID " + textureID);

		// bind this texture
		//
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);

		// convert that image into a byte buffer of texture data
		if (texture.imageByteBuffer == null)
			texture.imageByteBuffer =
				convertImageData(
					texture.bufferedImage,
					texture.hasAlpha,
					texture);

		// set up the texture wrapping mode depending on whether or not
		// this texture is specified for wrapping or not
		//

		int wrapMode = texture.wrap ? GL.GL_REPEAT : GL.GL_CLAMP;

		if (texture.target == GL.GL_TEXTURE_2D)
		{
			gl.glTexParameteri(texture.target, GL.GL_TEXTURE_WRAP_S, wrapMode);
			gl.glTexParameteri(texture.target, GL.GL_TEXTURE_WRAP_T, wrapMode);
			gl.glTexParameteri(
				texture.target,
				GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_LINEAR_MIPMAP_LINEAR);
				//texture.minFilter);
			
			gl.glTexParameteri(
				texture.target,
				GL.GL_TEXTURE_MAG_FILTER,
				texture.magFilter);
		}

		// create either a series of mipmaps of a single texture image based on what's loaded
		//
		//if (texture.mipmapped)
			if (texture.hasAlpha)
			{
				gl.glTexParameteri(
				texture.target,
				GL.GL_TEXTURE_MIN_FILTER,
				texture.minFilter);
			gl.glTexImage2D(
					texture.target,
					0,
					GL.GL_RGBA,
					texture.bufferedImage.getWidth(),
					texture.bufferedImage.getHeight(),
					0,
					GL.GL_RGBA,
					GL.GL_UNSIGNED_BYTE,
					texture.imageByteBuffer);
				System.out.println("Made alpha texture...");
			}
			else
			{
				if (true)
				{

					glu.gluBuild2DMipmaps(
						texture.target,
						texture.dstPixelFormat,
						texture.bufferedImage.getWidth(),
						texture.bufferedImage.getHeight(),
						texture.srcPixelFormat,
						GL.GL_UNSIGNED_BYTE,
						texture.imageByteBuffer);

				}
				else
				{
					gl.glTexImage2D(
						texture.target,
						0,
						texture.dstPixelFormat,
						texture.bufferedImage.getWidth(),
						texture.bufferedImage.getHeight(),
						0,
						texture.srcPixelFormat,
						GL.GL_UNSIGNED_BYTE,
						texture.imageByteBuffer);
				}
			}
	}

	public void updateTexture(Texture texture, BufferedImage bufferedImage, GLU glu)
		throws TextureFormatException
	{
		// bind this texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureID());

		ByteBuffer textureBuffer = convertImageDataOld(bufferedImage);

		// create either a series of mipmaps of a single texture image based on what's loaded
//		if (texture.isMipmapped())
		if (true)
		{
			
			  glu.gluBuild2DMipmaps( texture.getTarget(),
					  texture.getDstPixelFormat(),
					  bufferedImage.getWidth(),
					  bufferedImage.getHeight(),
					  texture.getSrcPixelFormat(),
					  GL.GL_UNSIGNED_BYTE,
					  textureBuffer );
		}
		else
		{
			gl.glTexImage2D(
				texture.getTarget(),
				0,
				texture.getDstPixelFormat(),
				bufferedImage.getWidth(),
				bufferedImage.getHeight(),
				0,
				texture.getSrcPixelFormat(),
				GL.GL_UNSIGNED_BYTE,
				textureBuffer);
		}
	}
}