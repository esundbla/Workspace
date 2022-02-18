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
 
import java.util.Random;
/**
 * @author Shawn Kendall
 *
 */
public class MathTestsSquareRoot
{
	static final long randSeed = 111111; //System.currentTimeMillis();
	static final double randScale = 1000.0;
	static public long baseTimeCost = 0;
	static public long baseTimeCostF = 0;
	static final int loopCount = 50000;
	static double[] randDoubles = new double[loopCount];
	static double[] outDoubles = new double[loopCount];
	static float[] randFloats = new float[loopCount];
	static float[] outFloats = new float[loopCount];
	public static void main(String[] args)
	{
		long timeStart, timeDone1, delta;
		long standardTime, alt1Time, alt2Time;

		System.out.println("Creating random data...");
		Random random = new Random();
		random.setSeed(randSeed);
		for (int i = 0; i < loopCount; i++)
		{
			randDoubles[i] = random.nextDouble() * randScale;
			randFloats[i] = (float)randDoubles[i];
		}

		System.out.println("Performing warm-up for square roots test...");
		for (int j = 500; j > -1; j--)
		{
			if (j == 0)
				System.out.println("Square Root Tests");

			// find time cost of array retieves and assigns for double arrays
			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outDoubles[i] = randDoubles[i];
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			baseTimeCost = timeDone1 - timeStart;

			// find time cost of array retieves and assigns for float arrays
			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = randFloats[i];
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			baseTimeCostF = timeDone1 - timeStart;

			if (j == 0)
				System.out.println("Base Time Cost = " + baseTimeCost);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outDoubles[i] = Math.sqrt(randDoubles[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			standardTime = delta - baseTimeCost;
			if (j == 0)
				System.out.println(
					"Double Math.sqrt(double)              Elapsed time "
						+ standardTime
						+ " value = "
						+ outDoubles[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outDoubles[i] = MathUtils.fastSqrt(randDoubles[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt1Time = delta - baseTimeCost;
			if (j == 0)
				System.out.println(
					"Double MathUtils.fastSqrt(double)     Elapsed time "
						+ alt1Time
						+ " value = "
						+ outDoubles[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outDoubles[i] = MathUtils.fasterSqrt(randDoubles[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt2Time = delta - baseTimeCost;
			if (j == 0)
				System.out.println(
					"Double MathUtils.fasterSqrt(double)   Elapsed time "
						+ alt2Time
						+ " value = "
						+ outDoubles[0]);
			if (j == 0)
				System.out.println(
					"Perf Ratios: std Math = 1.0  fast 1 Math = "
						+ (double)alt1Time / standardTime);
			if (j == 0)
				System.out.println(
					"                             fast 2 Math = "
						+ (double)alt2Time / standardTime);

			if (j == 0)
				System.out.println();

			if (j == 0)
				System.out.println("Base Time Cost = " + baseTimeCostF);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = (float)Math.sqrt(randFloats[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			standardTime = delta - baseTimeCostF;
			if (j == 0)
				System.out.println(
					"Float (float)Math.sqrt(float)         Elapsed time "
						+ standardTime
						+ " value = "
						+ outFloats[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = MathUtils.fastSqrtF(randFloats[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt1Time = delta - baseTimeCostF;
			if (j == 0)
				System.out.println(
					"Float MathUtils.fastSqrtF(float)      Elapsed time "
						+ alt1Time
						+ " value = "
						+ outFloats[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = MathUtils.fasterSqrtF(randFloats[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt2Time = delta - baseTimeCostF;
			if (j == 0)
				System.out.println(
					"Float MathUtils.fasterSqrtF(float)    Elapsed time "
						+ alt2Time
						+ " value = "
						+ outFloats[0]);
			if (j == 0)
				System.out.println(
					"Perf Ratios: std Math = 1.0  fast 1 Math = "
						+ (double)alt1Time / standardTime);
			if (j == 0)
				System.out.println(
					"                             fast 2 Math = "
						+ (double)alt2Time / standardTime);

			if (j == 0)
				System.out.println();
			if (j == 0)
				System.out.println("Inverse Square Root Tests");

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outDoubles[i] = 1 / Math.sqrt(randDoubles[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			standardTime = delta - baseTimeCost;
			if (j == 0)
				System.out.println(
					"Double  1/Math.sqrt(double)            Elapsed time "
						+ standardTime
						+ " value = "
						+ outDoubles[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = 1.0f / (float)Math.sqrt(randFloats[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt1Time = delta - baseTimeCostF;
			if (j == 0)
				System.out.println(
					"Float  1/(float)Math.sqrt(float)       Elapsed time "
						+ alt1Time
						+ " value = "
						+ outFloats[0]);

			timeStart = sun.misc.Perf.getPerf().highResCounter();
			for (int i = 0; i < loopCount; i++)
			{
				outFloats[i] = MathUtils.fastInverseSqrt(randFloats[i]);
			}
			timeDone1 = sun.misc.Perf.getPerf().highResCounter();
			delta = timeDone1 - timeStart;
			alt2Time = delta - baseTimeCostF;
			if (j == 0)
				System.out.println(
					"Float MathUtils.fastInverseSqrt(float) Elapsed time "
						+ alt2Time
						+ " value = "
						+ outFloats[0]);
			if (j == 0)
				System.out.println(
					"Perf Ratios: std Math = 1.0  fast 1 Math = "
						+ (double)alt1Time / standardTime);
			if (j == 0)
				System.out.println(
					"                             fast 2 Math = "
						+ (double)alt2Time / standardTime);

			if (j == 0)
				System.out.println();
			Thread.yield();
		}
	}

}
