package com.rc2s.application.services.streaming;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * CallbackAdapter
 * 
 * Used for sound algorithm computing
 * 
 * @author RC2S
 */
public class CallbackAdapter extends DefaultAudioCallbackAdapter
{
	private final StreamingService streamingService;

	// Check if onPlay time data changed
	private static boolean timeChanged;
	
	// Keep current time retrieved in onPlay
	private static long currentTime;
	
	// (L)ight/(S)tage/(C)ube - what to light
	private final char lightening;
	private final int algoNeededSize;
	
	// Array containing the coordinates of the points to lighten
	private int[][] positionsToLighten;
	private int lighteningIndex;
	
	// Type of algorithm wanted
	private final AlgoEffectEnum algoEffect;
	
	// Sync dimensions & Cubes number
	private static int syncWidth;
	private static int syncHeight;
	private static int syncDepth;
	private static int numCubes;
	
	// Lists used to compute audio algorithm
	private final List<Integer> lineMaxAnalysis;
	private final List<Integer> lineMinAnalysis;
	private final List<Integer> lineAvgAnalysis;
	
    private final Logger log = LogManager.getLogger(CallbackAdapter.class);
    
    public CallbackAdapter(final StreamingService streamingService, final int blockSize, final int[] syncDimensions)
    {
        super(blockSize);
		this.streamingService = streamingService;
		
		currentTime = 0;
		
		setDimensions(syncDimensions);
		
		algoEffect = getAlgoEffect((int) (Math.random() * 6));
		
		lightening = 'S';
		
		// If lightening set to 'Light', we should take algoEffect
		// in consideration to guess lightening size
		algoNeededSize = (lightening == 'L' ? algoEffect.getSize() : 1);
		
		// Initialize our lightening coordinates array
		positionsToLighten = new int[algoNeededSize * getLighteningSize(lightening)][3];
		lighteningIndex = 0;
		
		// Lists for sound computing
		lineMinAnalysis = new ArrayList<>();
		lineMaxAnalysis = new ArrayList<>();
		lineAvgAnalysis = new ArrayList<>();
    }

	/**
	 * setDimensions
	 * 
	 * Sets the synchronisation list dimensions
	 * Also gets the number of cubes
	 * 
	 * @param syncDimensions 
	 */
	public final void setDimensions(final int[] syncDimensions)
	{
		syncWidth	= syncDimensions[0];
		if (syncWidth == 0)
			syncWidth = 4;
		
		syncHeight	= syncDimensions[1];
		if (syncHeight == 0)
			syncHeight = 4;
		
		syncDepth	= syncDimensions[2];
		if (syncDepth == 0)
			syncDepth = 4;
		
		numCubes	= syncDimensions[3];
		if (numCubes == 0)
			numCubes = 1;
	}

	/**
	 * getLighteningSize
	 * 
	 * Get the expected number of LEDs lightened for 1 data
	 * e.g if size is 4x4x4
	 * 'C'ube will light 64 leds
	 * 'S'tage will light 16 leds
	 * 'L'ight will light 1 led
	 * 
	 * @param lightening
	 * @return int factor of LEDs lightening 
	 */
	private int getLighteningSize(final char lightening)
	{
		switch (lightening)
		{
			case 'C':
				return (syncDepth * syncHeight * syncWidth) / this.numCubes;
				
			case 'S':
				return (syncDepth * syncWidth) / this.numCubes;
				
			case 'L':
			default:
				return 1;
		}
	}
	
	/**
	 * getAlgoEffect
	 * 
	 * Choose the algo effect following random value
	 * 
	 * @param value
	 * @return AlgoEffectEnum the algo effect asked
	 */
	private AlgoEffectEnum getAlgoEffect(final int value)
	{
		switch (value)
		{
			case 0:
				return AlgoEffectEnum.CLASSIC;
				
			case 1:
				return AlgoEffectEnum.MIRROR_WIDTH;
				
			case 2:
				return AlgoEffectEnum.MIRROR_DEPTH;
				
			case 3:
				return AlgoEffectEnum.MIRROR_HEIGHT;
				
			case 4:
				return AlgoEffectEnum.MIRROR_TRIPLE;
				
			case 5:
				return AlgoEffectEnum.EIGHTIFY;
				
			default:
				return AlgoEffectEnum.CLASSIC;
		}
	}

	/**
	 * onPlay
	 * 
	 * Method retrieving byte data during music play
	 * Computes the byte array then extracts data from it 
	 * 
	 * @param mediaPlayer
	 * @param bytes
	 * @param sampleCount
	 * @param pts 
	 */
    @Override
    protected void onPlay(final DirectAudioPlayer mediaPlayer, final byte[] bytes, final int sampleCount, final long pts)
    {
		// Notice time changed to send datas when adding last line
		if (mediaPlayer.getTime() != currentTime)
		{
			timeChanged = true;
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
    }
    
    @Override
    public void flush(final DirectAudioPlayer mediaPlayer, final long pts)
    {
		super.flush(mediaPlayer, pts);
		log.info("flush()");
    }
    
    @Override
    public void drain(final DirectAudioPlayer mediaPlayer)
    {
		super.drain(mediaPlayer);
		log.info("drain()");
    }

	/**
	 * getSampleDef(sampleBytes)
	 * 
	 * Transform the 4 bytes that represent the sample
	 * to a 0 -> 28 representation used in a computeable line
	 * 
	 * Value is divided by 32 and added 4
	 * 
	 * @param sampleBytes
	 * @return 
	 */
	private byte getSampleDef(final byte[] sampleBytes)
	{
		byte result = 0;
		
		for (byte smpb : sampleBytes)
			result += (smpb >> 5) + 0b0100;
		
		return result;
	}
	
	/**
	 * addLineWord(sampleLine, sampleCount)
	 * 
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
	private void addLineWord(final byte[] sampleLine, final int sampleCount)
	{	
		if (timeChanged)
		{
			// Gets coordinates proposal
			prepareCoordinates();
			// Sends coordinates proposal
			sendCoordinates();
		}
		
		// Compute that new data line
		computeAudioDataLine(sampleLine, sampleCount);	
	}
	
	/**
	 * computeAudioDataLine(sampleLine, sampleCount)
	 * 
	 * Gets analysable datas from audio data line
	 * The line contains samples values taken from the
	 * getSampleDef() function
	 * 
	 * @param sampleLine
	 * @param sampleCount 
	 */
	private void computeAudioDataLine(final byte[] sampleLine, final int sampleCount)
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
	 * getLineVariation(datas, absolute)
	 * 
	 * Shall return the variation in an integer list
	 * If absolute is set to true, take absolute variation
	 * Otherwise take signed value
	 * 
	 * @param datas
	 * @param absolute
	 * @return Overall variation
	 */
	private int getLineVariation(final List<Integer> datas, final boolean absolute) // Let's keep absolute in case we want to generate 2 points and not 1
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
	
	/**
	 * addPositionsToLighteningList(x, y, z)
	 * 
	 * Adds one or several tuples(x, y, z) which represents
	 * the coordinates of a LED to lighten in the global
	 * LED-to-lighten list, following the 'lightening' asked
	 * (Light/Stage/Cube)
	 * 
	 * @param pos_x
	 * @param pos_y
	 * @param pos_z 
	 */
	private void addPositionsToLighteningList(final int pos_x, final int pos_y, final int pos_z)
	{
		switch (this.lightening)
		{
			case 'C':
				addCubePositions(pos_x, pos_y, false, pos_z);
				break;
				
			case 'S':
				addCubePositions(pos_x, pos_y, true, pos_z);
				break;
				
			case 'L':
			default:				
				addLighteningPosition(pos_x, pos_y, pos_z);
				break;
		}
	}
	
	/**
	 * addLighteningPosition(x, y, z)
	 * 
	 * Adds a tuple(x, y, z) which represents the coordinates of
	 * a LED to lighten in the global LED-to-lighten list, following
	 * the 'lightening' asked (Light/Stage/Cube)
	 * 
	 * @param pos_x
	 * @param pos_y
	 * @param pos_z 
	 */
	private void addLighteningPosition(final int pos_x, final int pos_y, final int pos_z)
	{
		positionsToLighten[lighteningIndex][0] = pos_x;
		positionsToLighten[lighteningIndex][1] = pos_y;
		positionsToLighten[lighteningIndex][2] = pos_z;
		lighteningIndex++;
	}
	
	/**
	 * addGroupPositionsToLighteningList(firstXLight, x_size, pos_y, isStaged, pos_z)
	 * 
	 * Adds all the position on the given range
	 * X range is given by firstXLight && x_size
	 * Y range is given by pos_y && isStaged (whole cube or only stage)
	 * Z range is always full range
	 * 
	 * @param firstXLight
	 * @param x_size
	 * @param pos_y
	 * @param isStaged
	 * @param pos_z 
	 */
	private void addGroupPositionsToLighteningList(int firstXLight, int x_size, final int pos_y, boolean isStaged, final int pos_z)
	{
		int i, j, k;
		
		int xThreshold = firstXLight + x_size;
		
		int y_begin = 0;
		int y_end = syncHeight;
		
		if (isStaged)
		{
			y_begin = pos_y;
			y_end = pos_y + 1;
		}
		
		for (i = firstXLight; i < xThreshold; i++)
			for (j = y_begin; j < y_end; j++)
				for (k = 0; k < syncDepth; k++)
					addLighteningPosition(i, j, k);
	}
	
	/**
	 * addCubePositions(x, y, isStaged, z)
	 * 
	 * Used for Stage mode and Cube mode
	 * From an initial LED coordinate, get the
	 * coordinates for all the stage or the cube
	 * 
	 * @param pos_x
	 * @param pos_y
	 * @param isStaged
	 * @param pos_z 
	 */
	private void addCubePositions(final int pos_x, final int pos_y, boolean isStaged, final int pos_z)
	{
		// More than 1 cube
		if (numCubes != 1)
		{
			// Size in x = whole width / number of cubes
			int x_size = syncWidth / numCubes;
			
			// Cube number to light
			int lightCube = pos_x / x_size;
			
			// Cube first x axis light
			int firstXLight = lightCube * x_size;
			
			// Now we shall light with firstXLight <= x <= firstXLight + x_size - 1
			addGroupPositionsToLighteningList(firstXLight, x_size, pos_y, isStaged, pos_z);
		}
		// Only one cube - Add everything on all ranges
		else
			addGroupPositionsToLighteningList(0, syncHeight, pos_y, isStaged, pos_z);
	}
	
	/**
	 * prepareCoordinates()
	 * 
	 * Get adapted coordinates to light from chosen effect
	 */
	private void prepareCoordinates()
	{
		int abs_x = (Math.abs(getLineVariation(lineMaxAnalysis, true)) % syncWidth);
		int abs_y = (Math.abs(getLineVariation(lineMinAnalysis, true)) % syncDepth);
		int abs_z = (Math.abs(getLineVariation(lineAvgAnalysis, true)) % syncHeight);
		
		addPositionsToLighteningList(abs_x, abs_y, abs_z);
		
		if (lightening == 'L')
		{
			switch (this.algoEffect)
			{
				case CLASSIC:
					produceClassicCoordinate();
					break;

				case MIRROR_WIDTH:
					produceWidthMirrorCoordinate(abs_x, abs_y, abs_z);
					break;

				case MIRROR_DEPTH:
					produceDepthMirrorCoordinate(abs_x, abs_y, abs_z);
					break;

				case MIRROR_HEIGHT:
					produceHeightMirrorCoordinate(abs_x, abs_y, abs_z);
					break;

				case MIRROR_TRIPLE:
					produceTripleMirrorCoordinate(abs_x, abs_y, abs_z);
					break;

				case EIGHTIFY:
					produceEightifyCoordinates(abs_x, abs_y, abs_z);
					break;
			}
		}
	}

	/**
	 * produceClassicCoordinate()
	 * 
	 * A 'classic' coordinates is computed on non-absolute wave analysis basis
	 */
	private void produceClassicCoordinate()
	{
		int cla_x = (Math.abs(getLineVariation(lineMaxAnalysis, false)) % syncWidth);
		int cla_y = (Math.abs(getLineVariation(lineMinAnalysis, false)) % syncDepth);
		int cla_z = (Math.abs(getLineVariation(lineAvgAnalysis, false)) % syncHeight);

		addPositionsToLighteningList(cla_x, cla_y, cla_z);
	}
	
	/**
	 * getMirrorCoordinateValue(origin, dimension)
	 * 
	 * Get the mirror coordinate value on a specified range
	 * e.g if range is 8
	 * 0 - 7
	 * 1 - 6
	 * 2 - 5
	 * 3 - 4
	 * 
	 * @param origin
	 * @param dimension
	 * @return int mirror value on this range
	 */
	private int getMirrorCoordinateValue(final int origin, final int dimension)
	{
		return (-1 * origin) + dimension - 1;
	}

	private void produceWidthMirrorCoordinate(final int abs_x, final int abs_y, final int abs_z)
	{
		int wid_x = getMirrorCoordinateValue(abs_x, syncWidth);
		
		addPositionsToLighteningList(wid_x, abs_y, abs_z);
	}
	
	private void produceDepthMirrorCoordinate(final int abs_x, final int abs_y, final int abs_z)
	{
		int dep_y = getMirrorCoordinateValue(abs_y, syncDepth);
		
		addPositionsToLighteningList(abs_x, dep_y, abs_z);
	}

	private void produceHeightMirrorCoordinate(final int abs_x, final int abs_y, final int abs_z)
	{
		int hei_z = getMirrorCoordinateValue(abs_z, syncHeight);
		
		addPositionsToLighteningList(abs_x, abs_y, hei_z);
	}
	
	/**
	 * produceTripleMirrorCoordinate(x, y, z)
	 * 
	 * Add the mirror coordinate on every axis
	 * to the existing coordinate
	 * 
	 * e.g if input is (0, 0, 0) and dimension 6
	 * we add (5, 0, 0), (0, 5, 0), (0, 0, 5) 
	 * 
	 * @param abs_x
	 * @param abs_y
	 * @param abs_z 
	 */
	private void produceTripleMirrorCoordinate(final int abs_x, final int abs_y, final int abs_z)
	{
		produceWidthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceDepthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceHeightMirrorCoordinate(abs_x, abs_y, abs_z);
	}
	
	/**
	 * produceEightifyCoordinates
	 * 
	 * Add all mirror coordinates on whole cube
	 * to the existing coordinate
	 * 
	 * e.g if input is (0, 0, 0) and dimension 4
	 * we add (0, 0, 4), (0, 4, 0), (0, 4, 4),
	 * (4, 0, 0), (4, 0, 4), (4, 4, 0), (4, 4, 4)
	 * 
	 * @param abs_x
	 * @param abs_y
	 * @param abs_z 
	 */
	private void produceEightifyCoordinates(final int abs_x, final int abs_y, final int abs_z)
	{
		int new_x = abs_x,
			new_y = abs_y,
			new_z = abs_z;
		
		/* Scheme shall be m(Y(Z(Y(X(Y(Z(Y))))))) */
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, syncDepth);
		// Z
		produceHeightMirrorCoordinate(new_x, new_y, new_z);
		new_z = getMirrorCoordinateValue(new_z, syncHeight);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, syncDepth);
		// X
		produceWidthMirrorCoordinate(new_x, new_y, new_z);
		new_x = getMirrorCoordinateValue(new_x, syncWidth);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
		new_y = getMirrorCoordinateValue(new_y, syncDepth);
		// Z
		produceHeightMirrorCoordinate(new_x, new_y, new_z);
		new_z = getMirrorCoordinateValue(new_z, syncHeight);
		// Y
		produceDepthMirrorCoordinate(new_x, new_y, new_z);
	}
	
	/**
	 * sendCoordinates()
	 * 
	 * Send retrieved coordinates during a certain time period
	 * in order to light LED on the cube and prepare space for
	 * the incoming datas.
	 */
	private void sendCoordinates()
	{
		streamingService.processCoordinates(positionsToLighten);

		positionsToLighten = new int[algoNeededSize * getLighteningSize(lightening)][3];
		lighteningIndex = 0;
		
		lineMaxAnalysis.clear();
		lineMinAnalysis.clear();
		lineAvgAnalysis.clear();
	}
}
