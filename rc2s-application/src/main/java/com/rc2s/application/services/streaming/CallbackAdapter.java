package com.rc2s.application.services.streaming;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

public class CallbackAdapter extends DefaultAudioCallbackAdapter
{
	private static boolean timeChanged;
	private static boolean timeTests;
	private static long currentTime;
	
	private static int cubeWidth;
	private static int cubeDepth;
	private static int cubeHeight;
	
	private final List<Integer> lineMinAnalysis;
	private final List<Integer> lineMaxAnalysis;
	private final List<Integer> lineAvgAnalysis;
	
    private final Logger log = LogManager.getLogger(CallbackAdapter.class);
    
    private BufferedOutputStream out = null;
    
    public CallbackAdapter(int blockSize)
    {
        super(blockSize);
        out = new BufferedOutputStream(System.out);
		
		timeTests	= false;
		currentTime = 0;
		
		cubeWidth	= 8;
		cubeDepth	= 8;
		cubeHeight	= 8;
		
		lineMinAnalysis = new ArrayList<>();
		lineMaxAnalysis = new ArrayList<>();
		lineAvgAnalysis = new ArrayList<>();
    }

    @Override
    protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] bytes, int sampleCount, long pts)
    {
		try
		{
			// Notice time changed to send datas when adding last line
			if (mediaPlayer.getTime() != currentTime)
			{
				timeChanged = true;
				
				if (timeTests)
					System.out.println("NEW TIME : " + mediaPlayer.getTime()); // +261/262 every 10 turn --> 1sec ~ 40 turns
			
				currentTime = mediaPlayer.getTime();
			}
			else
				timeChanged = false;

			final int BYTES_LEN = bytes.length;
			final int SAMPLE_LEN = BYTES_LEN / sampleCount;
			int b_index;

			byte[] sampleLine = new byte[sampleCount];
			int s_index = 0;

			// There are sampleCount samples in a line
			// A line is 1 turn ~= (260/10)ms					
			for (b_index = 0; b_index < BYTES_LEN; b_index += SAMPLE_LEN)
			{
				byte[] sampleBytes = new byte[4];

				// Get sample bytes
				for (int loc = 0; loc < SAMPLE_LEN; loc++)
					sampleBytes[loc] = bytes[b_index + loc];

				// Add sample computing to line
				sampleLine[s_index] = getSampleDef(sampleBytes);
				s_index++;
			}

			// Add line to algo buffer
			addLineWord(sampleLine, sampleCount);
			
			//out.write(bytes);
			out.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    
    @Override
    public void flush(DirectAudioPlayer mediaPlayer, long pts)
    {
		try
		{
			out.flush();
			log.info("flush()");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    
    @Override
    public void drain(DirectAudioPlayer mediaPlayer)
    {
		try
		{
			out.close();
			log.info("drain()");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    }

	/**
	 * Transform the 4 bytes that represent the sample
	 * to a 0 -> 28 representation used in a computeable line
	 * 
	 * Value is divided by 32 and added 4
	 * 
	 * @param sampleBytes
	 * @return 
	 */
	private byte getSampleDef(byte[] sampleBytes)
	{
		byte result = 0;
		
		for (byte smpb : sampleBytes)
			result += (smpb >> 5) + 0b0100;
		
		return result;
	}
	
	/**
	 * Use a line of sampleCount bytes transformed to N coordinates
	 * In order to graph a sound representation
	 * 1 line is about 26ms which means 10 lines is 260ms ~
	 * 10 lines should be represented by at least 10 positions
	 * So one line should give at least 1 position
	 * Should be changeable via positionsPerSecond attribute
	 * 
	 * @param sampleLine
	 * @param sampleCount 
	 */
	private void addLineWord(byte[] sampleLine, int sampleCount)
	{	
		if (timeChanged)
		{
			if (timeTests)
				doTimeTests();
			
			// Get coordinates proposal
			// @arg1 : with classics
			// @arg2 : depth mirror (y)
			// @arg3 : width mirror (x)
			// @arg4 : height mirror (z)
			// @arg5 : triple mirror (x, y, z -> +3 coords)
			// @arg6 : eightify (y, z, y, x, y, z, y -> +7 coords)
			proposeCoordinates(true, false, false, false, false, false);
			
			lineMaxAnalysis.clear();
			lineMinAnalysis.clear();
			lineAvgAnalysis.clear();
		}
		
		// Compute that new data line
		computeAudioDataLine(sampleLine, sampleCount);	
	}
	
	/**
	 * Gets analysable datas from audio data line
	 * The line contains samples values taken from the
	 * getSampleDef() function
	 * 
	 * @param sampleLine
	 * @param sampleCount 
	 */
	private void computeAudioDataLine(byte[] sampleLine, int sampleCount)
	{
		int max = 0;
		int min = 255;
		int average = 0;
		
		for (byte data : sampleLine)
		{
			// Compute
			average += data;
			
			if (data > max)
				max = data;
			
			if (data < min)
				min = data;
		}
		lineMaxAnalysis.add(max);
		lineMinAnalysis.add(min);
		lineAvgAnalysis.add(average / sampleCount);
	}
	
	/**
	 * Shall return the variation in an integer list
	 * If absolute is set to true, take absolute variation
	 * Otherwise take signed value
	 * 
	 * @param datas
	 * @param absolute
	 * @return Overall variation
	 */
	private int getLineVariation(List<Integer> datas, boolean absolute) // Let's keep absolute in case we want to generate 2 points and not 1
	{
		int variation = 0;
		int prev = datas.get(0);
		
		for (int index = 1; index < datas.size(); index++)
		{
			if (absolute)
				variation += Math.abs(datas.get(index) - prev);
			else
				variation += datas.get(index) - prev;
			
			prev = datas.get(index);
		}
		
		return variation;
	}
	
	private void proposeCoordinates(boolean withClassic, boolean depthMirror,
			boolean widthMirror, boolean heightMirror, boolean tripleMirror,
			boolean eightify)
	{
		int abs_x = (Math.abs(getLineVariation(lineMaxAnalysis, true)) % cubeWidth);
		int abs_y = (Math.abs(getLineVariation(lineMinAnalysis, true)) % cubeDepth);
		int abs_z = (Math.abs(getLineVariation(lineAvgAnalysis, true)) % cubeHeight);
		
		System.out.println(
			"Coord proposal : ABS(" + abs_x + ", " + abs_y + ", " + abs_z + ")"
		);
		
		if (withClassic)
			produceClassicCoordinate();
		if (widthMirror)
			produceWidthMirrorCoordinate(abs_x, abs_y, abs_z);
		if (depthMirror)
			produceDepthMirrorCoordinate(abs_x, abs_y, abs_z);
		if (heightMirror)
			produceHeightMirrorCoordinate(abs_x, abs_y, abs_z);
		if (tripleMirror)
			produceTripleMirrorCoordinate(abs_x, abs_y, abs_z);
		if (eightify)
			produceEightifyCoordinates(abs_x, abs_y, abs_z);
		
		System.out.println("--------------------");
	}

	private void produceClassicCoordinate()
	{
		int cla_x = (Math.abs(getLineVariation(lineMaxAnalysis, false)) % cubeWidth);
		int cla_y = (Math.abs(getLineVariation(lineMinAnalysis, false)) % cubeDepth);
		int cla_z = (Math.abs(getLineVariation(lineAvgAnalysis, false)) % cubeHeight);

		System.out.println(
			"Coord proposal : CLA(" + cla_x + ", " + cla_y + ", " + cla_z + ")"
		);
		System.out.println(".");
	}

	private void produceWidthMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int wid_x = getMirrorCoordinateValue(abs_x, cubeWidth);
		
		System.out.println(
			"Coord proposal : WID(" + wid_x + ", " + abs_y + ", " + abs_z + ")"
		);
	}
	
	private void produceDepthMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int dep_y = getMirrorCoordinateValue(abs_y, cubeDepth);
		
		System.out.println(
			"Coord proposal : DEP(" + abs_x + ", " + dep_y + ", " + abs_z + ")"
		);
	}

	private void produceHeightMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int hei_z = getMirrorCoordinateValue(abs_z, cubeHeight);
		
		System.out.println(
			"Coord proposal : HEI(" + abs_x + ", " + abs_y + ", " + hei_z + ")"
		);
	}
	
	private void produceTripleMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		produceWidthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceDepthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceHeightMirrorCoordinate(abs_x, abs_y, abs_z);
		System.out.println(".");
	}
	
	private void produceEightifyCoordinates(int abs_x, int abs_y, int abs_z)
	{
		int new_x = abs_x,
			new_y = abs_y,
			new_z = abs_z;
		
		/* Scheme shall be m(Y(Z(Y(X(Y(Z(Y))))))) */
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, cubeDepth);
		// Z
		produceHeightMirrorCoordinate(new_x, new_y, new_z);
		new_z = getMirrorCoordinateValue(new_z, cubeHeight);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, cubeDepth);
		// X
		produceWidthMirrorCoordinate(new_x, new_y, new_z);
		new_x = getMirrorCoordinateValue(new_x, cubeWidth);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, cubeDepth);
		// Z
		produceHeightMirrorCoordinate(new_x, new_y, new_z);
		new_z = getMirrorCoordinateValue(new_z, cubeHeight);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		System.out.println(".");
	}
	
	private int getMirrorCoordinateValue(int origin, int dimension)
	{
		return (-1 * origin) + dimension - 1;
	}
	
	private void doTimeTests()
	{
		System.out.println("***** IN Line analysis *****");
			
		System.out.println("Max list	: " + lineMaxAnalysis.toString());
		System.out.println("Max total	: " + lineMaxAnalysis.stream().mapToInt(Integer::intValue).sum());
		System.out.println("Max var		: " + getLineVariation(lineMaxAnalysis, false));
		System.out.println("Max var abs	: " + getLineVariation(lineMaxAnalysis, true));

		System.out.println("Min list	: " + lineMinAnalysis.toString());
		System.out.println("Min total	: " + lineMinAnalysis.stream().mapToInt(Integer::intValue).sum());
		System.out.println("Min var		: " + getLineVariation(lineMinAnalysis, false));
		System.out.println("Min var abs	: " + getLineVariation(lineMinAnalysis, true));

		System.out.println("Avg list	: " + lineAvgAnalysis.toString());
		System.out.println("Avg total	: " + lineAvgAnalysis.stream().mapToInt(Integer::intValue).sum());
		System.out.println("Avg var		: " + getLineVariation(lineAvgAnalysis, false));
		System.out.println("Avg var abs	: " + getLineVariation(lineAvgAnalysis, true));

		System.out.println("***** OUT Line analysis *****");
		//System.out.println(".");
		//System.out.println(".");
	}
}
