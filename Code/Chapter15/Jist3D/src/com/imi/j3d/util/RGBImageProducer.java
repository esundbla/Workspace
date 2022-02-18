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

import java.awt.color.ColorSpace;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class RGBImageProducer extends Object implements ImageProducer
{
	private short storage;
	private short BPC;
	private int dimension, xsize, ysize, zsize;
	private int pixmin, pixmax;
	private int colormap;
	private byte Red[] = null, Green[] = null, Blue[] = null, Alpha[] = null;
	private ImageConsumer theConsumer = null;
	private Hashtable Properties = null;
	private String type;
	static private int bufferSize = 1024 * 16;

	public RGBImageProducer(String Filename) throws IOException
	{
		BufferedInputStream Input = new BufferedInputStream(new FileInputStream(Filename), bufferSize);
		Initialize(Input, (Hashtable) null);
		Input.close();
	}

	public RGBImageProducer(String Filename, Hashtable props) throws IOException
	{
		BufferedInputStream Input = new BufferedInputStream(new FileInputStream(Filename), bufferSize);
		Initialize(Input, props);
		Input.close();
	}

	public RGBImageProducer(InputStream Input) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(Input, bufferSize);
		Initialize(bis, (Hashtable) null);
	}

	public RGBImageProducer(InputStream Input, Hashtable props) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(Input, bufferSize);
		Initialize(bis, props);
	}

	private void Initialize(InputStream Input, Hashtable props) throws IOException
	{
		Properties = props;
		DataInputStream data = new DataInputStream(Input);
		if (data.readShort() != 474)
		{
			throw new IOException("Not IRIX RGB format");
		}
		storage = data.readByte();
		if ((storage > 1) || (storage < 0))
		{
			throw new IOException("Not IRIX RGB format");
		}
		BPC = data.readByte();
		if ((BPC > 2) || (BPC < 1))
		{
			throw new IOException("Not IRIX RGB format");
		}
		dimension = data.readUnsignedShort();
		if ((dimension > 3) || (dimension < 1))
		{
			throw new IOException("Not IRIX RGB format");
		}
		xsize = data.readUnsignedShort();
		ysize = data.readUnsignedShort();
		bufferSize = ysize * xsize;
		if (dimension == 1)
		{
			ysize = 1;
		}
		zsize = data.readUnsignedShort();
		//System.out.println("zsize " + zsize );
		//System.out.println("dimension " + dimension );

		//		if( (zsize < 1) || ((dimension == 1)&&(zsize != 1 )) ||
		//				 ((dimension == 2)&&(zsize != 1 )) )
		if ((zsize < 1) || ((dimension == 1) && (zsize != 1)))
		{
			throw new IOException("Not IRIX RGB format");
		}
		setType(zsize);
		//System.out.println("zsize " + zsize );
		pixmin = data.readInt();
		pixmax = data.readInt();
		data.skipBytes(84);
		colormap = data.readInt();
		switch (colormap)
		{
			case 0 :
			case 1 :
				break;
			case 2 :
			case 3 :
				throw new IOException("Obsolete IRIX RGB format");
			default :
				throw new IOException("Not IRIX RGB format");
		}
		data.skipBytes(404);
		switch (storage)
		{
			case 0 :
				byte ImgData[] = new byte[ysize * xsize * zsize * BPC];
				Input.read(ImgData);
				fillColorBuffers(new DataInputStream(new ByteArrayInputStream(ImgData)));
				break;
			case 1 :
				readRLEdata(data);
				break;
			default :
				throw new IOException("Not IRIX RGB format");
		}
	}

	public String getType()
	{
		return type;
	}

	private void setType(int dim)
	{
		//System.out.println("TEXTURE DIMENSIONS " + dim);
		switch (dim)
		{
			case 1 :
				type = "INTENSITY";
				break;
			case 2 :
				type = "LUMINANCE_ALPHA";
				break;
			case 3 :
				type = "RGB";
				break;
			case 4 :
				type = "RGBA";
				break;
		}
	}

	private final void readRLEdata(DataInputStream data) throws IOException
	{
		int size = zsize * ysize;
		int starttab[] = new int[ysize * xsize];
		int lengthtab[] = new int[ysize * xsize];

		ByteArrayOutputStream RestOfFile = new ByteArrayOutputStream();
		byte tableData[] = new byte[1024];
		while (true)
		{
			int bytes_read = 0;

			bytes_read = data.read(tableData);
			if (bytes_read == -1)
				break;
			RestOfFile.write(tableData, 0, bytes_read);
		}
		byte RLEdata[] = RestOfFile.toByteArray();
		DataInputStream tableStream = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(RLEdata), bufferSize));
		for (int i = 0; i < size; i++)
		{
			starttab[i] = tableStream.readInt();
		}
		for (int i = 0; i < size; i++)
		{
			lengthtab[i] = tableStream.readInt();
		}
		ByteArrayOutputStream ImgData = new ByteArrayOutputStream();
		for (int i = 0; i < zsize; i++)
		{
			int lo = i * ysize;
			for (int j = 0; j < ysize; j++)
			{
				int rleoffset = starttab[lo + j] - 512;
				int rlelength = lengthtab[j + lo];
				if (BPC == 2)
					throw new IOException("RLE 2byte RGB unsupported");
				while (true)
				{
					int count;
					int tmp = rleoffset;

					count = 0x7f & RLEdata[rleoffset++];
					if (0 == count)
					{
						break;
					}
					if (0 != (0x80 & RLEdata[tmp]))
					{
						while (0 < count--)
						{
							ImgData.write(RLEdata[rleoffset++]);
						}
					}
					else
					{
						byte pixel = RLEdata[rleoffset];
						rleoffset++;
						while (0 < count--)
						{
							ImgData.write(pixel);
						}
					}
				}
			}
		}
		fillColorBuffers(new DataInputStream(new ByteArrayInputStream(ImgData.toByteArray())));
	}

	private final void fillColorBuffers(DataInputStream data) throws IOException
	{
		switch (zsize)
		{
			case 1 :
				Red = new byte[ysize * xsize];
				readColor(Red, data);
				break;
			case 2 :
				Red = new byte[ysize * xsize];
				Alpha = new byte[ysize * xsize];
				readColor(Red, data);
				readColor(Alpha, data);
				break;
			case 3 :
				Red = new byte[ysize * xsize];
				Green = new byte[ysize * xsize];
				Blue = new byte[ysize * xsize];
				readColor(Red, data);
				readColor(Green, data);
				readColor(Blue, data);
				break;
			case 4 :
				Red = new byte[ysize * xsize];
				Green = new byte[ysize * xsize];
				Blue = new byte[ysize * xsize];
				Alpha = new byte[ysize * xsize];
				readColor(Red, data);
				readColor(Green, data);
				readColor(Blue, data);
				readColor(Alpha, data);
				break;
			default :
				throw new IOException("Not IRIX RGB format");
		}
	}

	private final void readColor(byte color[], DataInputStream data) throws IOException
	{
		int lo, i, j;
		if (BPC == 1)
		{
			for (i = ysize - 1; i >= 0; --i)
			{
				lo = i * xsize;
				data.read(color, lo, xsize);
				/*
				for( j=0; j<xsize; j++ )
				{		    
					color[lo+j] = data.readByte();
				}
				*/
			}
		}
		else if (BPC == 2)
		{
			for (i = ysize - 1; i >= 0; --i)
			{
				lo = i * xsize;
				for (j = 0; j < xsize; j++)
				{
					color[lo + j] = (byte)downSize(data.readUnsignedShort());
				}
			}
		}
	}

	static private final int downSize(int org)
	{
		return (int) ((((float)org) / 0xffff) * 0xff);
	}

	private synchronized void readImg() throws IOException
	{
		int tmp = 0;

		if (theConsumer != null)
		{
			theConsumer.setDimensions(xsize, ysize);
		}
		if (theConsumer != null)
		{
			theConsumer.setProperties(Properties);
		}
		if (theConsumer != null)
		{
			theConsumer.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS | ImageConsumer.SINGLEFRAME);
		}
		switch (zsize)
		{
			case 1 :
				{
					DirectColorModel CModel = new DirectColorModel(8, 0xff, 0xff, 0xff);
					if (theConsumer != null)
					{
						theConsumer.setColorModel(CModel);
					}
					if (theConsumer != null)
						theConsumer.setPixels(0, 0, xsize, ysize, CModel, Red, 0, xsize);
				}
				break;
			case 2 :
				{
					int pix[] = new int[xsize];
					ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);

					//			    DirectColorModel CModel = new DirectColorModel( cs, 32,
					//					     0xff, 0xff, 0xff, 0xff000000, false, DataBuffer.TYPE_INT );
					DirectColorModel CModel = new DirectColorModel(32, 0xff, 0xff, 0xff, 0xff000000);
					if (theConsumer != null)
					{
						theConsumer.setColorModel(CModel);
					}
					for (int k = 0; k < ysize; k++)
					{
						int size = k * xsize;
						for (int j = 0; j < xsize; j++)
						{
							int pos = size + j;

							pix[j] = (Red[pos] & 0xff) | ((Alpha[pos] << 24) & 0xff000000);
						}
						if (theConsumer != null)
							theConsumer.setPixels(0, k, xsize, 1, CModel, pix, 0, xsize);
					}
				}
				break;
			case 3 :
				{
					int pix[] = new int[xsize];
					DirectColorModel CModel = new DirectColorModel(24, 0xff, 0xff00, 0xff0000);
					if (theConsumer != null)
					{
						theConsumer.setColorModel(CModel);
					}
					for (int k = 0; k < ysize; k++)
					{
						int size = k * xsize;
						for (int j = 0; j < xsize; j++)
						{
							int pos = size + j;

							pix[j] = ((Red[pos]) & 0xff) | ((Green[pos] << 8) & 0xff00) | ((Blue[pos] << 16) & 0xff0000);
						}
						if (theConsumer != null)
							theConsumer.setPixels(0, k, xsize, 1, CModel, pix, 0, xsize);
					}
				}
				break;
			case 4 :
				{
					int pix[] = new int[xsize];
					DirectColorModel CModel = new DirectColorModel(32, 0xff, 0xff00, 0xff0000, 0xff000000);
					if (theConsumer != null)
					{
						theConsumer.setColorModel(CModel);
					}
					for (int k = 0; k < ysize; k++)
					{
						int size = k * xsize;
						for (int j = 0; j < xsize; j++)
						{
							int pos = size + j;

							pix[j] = (Red[pos] & 0xff) | ((Green[pos] << 8) & 0xff00) | ((Blue[pos] << 16) & 0xff0000) | ((Alpha[pos] << 24) & 0xff000000);
						}
						if (theConsumer != null)
							theConsumer.setPixels(0, k, xsize, 1, CModel, pix, 0, xsize);
					}
				}
				break;
		}
		if (theConsumer != null)
		{
			theConsumer.imageComplete(ImageConsumer.STATICIMAGEDONE);
		}
	}

	public synchronized void addConsumer(ImageConsumer ic)
	{
		startProduction(ic);
	}

	public synchronized boolean isConsumer(ImageConsumer ic)
	{
		return (ic == theConsumer);
	}

	public synchronized void removeConsumer(ImageConsumer ic)
	{
		if (isConsumer(ic))
		{
			theConsumer = null;
		}
	}

	public void requestTopDownLeftRightResend(ImageConsumer ic)
	{
		startProduction(ic);
	}

	public synchronized void startProduction(ImageConsumer ic)
	{
		theConsumer = ic;
		try
		{
			readImg();
		}
		catch (Exception e)
		{
			if (theConsumer != null)
			{
				theConsumer.imageComplete(ImageConsumer.IMAGEERROR);
			}
		}
	}
	static public void main(String args[])
	{
		System.out.println("Hello");
		String filename = "in";
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-FILE"))
			{
				filename = args[i + 1];
				i++;
			}
		}
		while (true)
		{
			java.awt.Image img = null;
			FileInputStream F;
			try
			{
				if (filename.endsWith(".rgba") || filename.endsWith(".rgb") || filename.endsWith(".inta") || filename.endsWith(".int") || filename.endsWith(".sgi") || filename.endsWith(".SGI") || filename.endsWith(".RGBA") || filename.endsWith(".RGB") || filename.endsWith(".INTA") || filename.endsWith(".INT"))
				{
					F = new FileInputStream(filename);
					RGBImageProducer rgb = new RGBImageProducer(F);
					img = java.awt.Toolkit.getDefaultToolkit().createImage(rgb);
				}
				else
				{
					System.out.println("Error: bad file type");
				}
				System.out.println("Read " + filename);
			}
			catch (Exception e)
			{
				System.out.println("Failed to load " + filename);
				e.printStackTrace();
			}
		}
	}
}
