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

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JWindow;

public class GUIConstraints
{
	static GridBagConstraints c = new GridBagConstraints();
	static Insets insets = new Insets(0, 0, 0, 0);
	static GridBagLayout gridbag = new GridBagLayout();

	static public LayoutManager getLayout()
	{
		return gridbag;
	}

	static public void setConstraints(
		Container container,
		Component component,
		int grid_x,
		int grid_y,
		int grid_width,
		int grid_height,
		int fill,
		int anchor,
		double weight_x,
		double weight_y,
		int top,
		int left,
		int bottom,
		int right)
	{
		//        GridBagConstraints c = new GridBagConstraints();
		reset(c);
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.fill = fill;
		c.anchor = anchor;
		c.weightx = weight_x;
		c.weighty = weight_y;
		if (top + bottom + left + right > 0)
			c.insets = new Insets(top, left, bottom, right);

		((GridBagLayout)container.getLayout()).setConstraints(component, c);
	}

	static public void constrain(
		Container container,
		Component component,
		int grid_x,
		int grid_y,
		int grid_width,
		int grid_height,
		int fill,
		int anchor,
		double weight_x,
		double weight_y,
		int top,
		int left,
		int bottom,
		int right)
	{
		//        GridBagConstraints c = new GridBagConstraints();
		reset(c);
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.fill = fill;
		c.anchor = anchor;
		c.weightx = weight_x;
		c.weighty = weight_y;
		if (top + bottom + left + right > 0)
			c.insets = new Insets(top, left, bottom, right);

		if (container instanceof JFrame)
		{
			((GridBagLayout) ((JFrame)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
			((JFrame)container).getContentPane().add(component);
		}
		else if (container instanceof JWindow)
		{
			((GridBagLayout) ((JWindow)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
			((JWindow)container).getContentPane().add(component);
		}
		else
		{
			((GridBagLayout)container.getLayout()).setConstraints(component, c);
			container.add(component);
		}
	}

	static public void constrain(
		Container container,
		Component component,
		int grid_x,
		int grid_y,
		int grid_width,
		int grid_height)
	{
		constrain(
			container,
			component,
			grid_x,
			grid_y,
			grid_width,
			grid_height,
			GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST,
			0.0,
			0.0,
			0,
			0,
			0,
			0);
	}

	static public void constrain(
		Container container,
		Component component,
		int grid_x,
		int grid_y,
		int grid_width,
		int grid_height,
		int top,
		int left,
		int bottom,
		int right)
	{
		constrain(
			container,
			component,
			grid_x,
			grid_y,
			grid_width,
			grid_height,
			GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST,
			0.0,
			0.0,
			top,
			left,
			bottom,
			right);
	}

	static public void constrain(
		Container container,
		Component component,
		int grid_y,
		int grid_width,
		int grid_height,
		int fill,
		int anchor,
		double weight_x,
		double weight_y,
		int top,
		int left,
		int bottom,
		int right)
	{
		//        GridBagConstraints c = new GridBagConstraints();
		reset(c);
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.fill = fill;
		c.anchor = anchor;
		c.weightx = weight_x;
		c.weighty = weight_y;
		if (top + bottom + left + right > 0)
			c.insets = new Insets(top, left, bottom, right);

		if (container instanceof JFrame)
		{
			((GridBagLayout) ((JFrame)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
			((JFrame)container).getContentPane().add(component);
		}
		else if (container instanceof JWindow)
		{
			((GridBagLayout) ((JWindow)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
			((JWindow)container).getContentPane().add(component);
		}
		else
		{
			((GridBagLayout)container.getLayout()).setConstraints(component, c);
			container.add(component);
		}
	}
	static public void setConstraints(
		Container container,
		Component component,
		int grid_y,
		int grid_width,
		int grid_height,
		int fill,
		int anchor,
		double weight_x,
		double weight_y,
		int top,
		int left,
		int bottom,
		int right)
	{
		//        GridBagConstraints c = new GridBagConstraints();
		reset(c);
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.fill = fill;
		c.anchor = anchor;
		c.weightx = weight_x;
		c.weighty = weight_y;
		if (top + bottom + left + right > 0)
			c.insets = new Insets(top, left, bottom, right);

		if (container instanceof JFrame)
		{
			((GridBagLayout) ((JFrame)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
		}
		else if (container instanceof JWindow)
		{
			((GridBagLayout) ((JWindow)container).getContentPane().getLayout()).setConstraints(
				component,
				c);
			((JWindow)container).getContentPane().add(component);
		}
		else
		{
			((GridBagLayout)container.getLayout()).setConstraints(component, c);
		}
	}

	static public void reset(GridBagConstraints c)
	{
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = c.gridheight = 1;
		c.weightx = c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = insets;
		c.ipadx = 0;
		c.ipady = 0;
	}
}
