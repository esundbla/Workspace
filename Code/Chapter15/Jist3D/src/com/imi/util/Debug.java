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
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.Date;

public class Debug
{
	static boolean on = true;
	static String errorLog = "";
	static String prefix = "Cosmic--[ ";
	static String suffix = " ]";
	public static void setState(boolean state)
	{
		on = state;
	}

	public static void println(int out)
	{
		if (on)
			System.out.println(prefix + out + suffix);
	}
	public static void println(float out)
	{
		if (on)
			System.out.println(prefix + out + suffix);
	}
	public static void println(double out)
	{
		if (on)
			System.out.println(prefix + out + suffix);
	}
	public static void println(String out)
	{
		if (on)
			System.out.println(prefix + out + suffix);
	}
	public static void print(String out)
	{
		if (on)
			System.out.print(prefix + out + suffix);
	}
	public static void println(Object out)
	{
		if (on)
			System.out.println(prefix + out + suffix);
	}
	public static void print(Object out)
	{
		if (on)
			System.out.print(prefix + out + suffix);
	}

	public static void logErrorln(String out)
	{
		Date date = new Date();
		errorLog += "|"
			+ DateFormat.getTimeInstance(DateFormat.MEDIUM).format(date)
			+ "| "
			+ out
			+ "\n";
		Toolkit.getDefaultToolkit().beep();
		//System.out.println( "<[1m" + out + "[0>" );
	}

	public static void logError(String out)
	{
		Date date = new Date();
		errorLog += "|" + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(date) + "| " + out;
		Toolkit.getDefaultToolkit().beep();
		//System.out.print( "<[1m" + out + "[0>" );
	}

	public static void dumpErrorLog()
	{
		Toolkit.getDefaultToolkit().beep();
		//System.out.print( "<[1m" );
		System.out.println("===================================");
		System.out.println("Error Log Dump");
		System.out.println("===================================");
		System.out.print(errorLog);
		System.out.println("===================================");
		//System.out.print( "[0>" );
	}
}
