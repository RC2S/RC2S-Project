package com.rc2s.application.services.streaming;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import java.util.ArrayList;
import java.util.List;

public class CallbackAdapter extends DefaultAudioCallbackAdapter
{
	private final StreamingService streamingService;

	// Check if onPlay time data changed
	private static boolean timeChanged;
	
	// Keep current time retrieved in onPlay
	private static long currentTime;
	
	// Array containing the coordinates of the points to lighten
	private int[][] positionsToLighten;
	private int lighteningIndex;
	
	// Type of algorithm wanted
	private final AlgoEffectEnum algoEffect;
	
	// Sync dimensions
	private static int syncWidth;
	private static int syncHeight;
	private static int syncDepth;
	
	// Lists used to compute audio algorithm
	private final List<Integer> lineMaxAnalysis;
	private final List<Integer> lineMinAnalysis;
	private final List<Integer> lineAvgAnalysis;
	
    private final Logger log = LogManager.getLogger(CallbackAdapter.class);
    
    public CallbackAdapter(StreamingService streamingService, int blockSize, int[] syncDimensions)
    {
        super(blockSize);
		this.streamingService = streamingService;
		
		currentTime = 0;
		
		setDimensions(syncDimensions);
		
		algoEffect = getAlgoEffect((int) (Math.random() * 6));
		
		positionsToLighten = new int[algoEffect.getSize()][3];
		lighteningIndex = 0;
		
		lineMinAnalysis = new ArrayList<>();
		lineMaxAnalysis = new ArrayList<>();
		lineAvgAnalysis = new ArrayList<>();
    }

	public void setDimensions(int[] syncDimensions)
	{
		syncWidth	= syncDimensions[0];
		syncHeight	= syncDimensions[1];
		syncDepth	= syncDimensions[2];
	}
	
	private AlgoEffectEnum getAlgoEffect(int value)
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

    @Override
    protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] bytes, int sampleCount, long pts)
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
    public void flush(DirectAudioPlayer mediaPlayer, long pts)
    {
		super.flush(mediaPlayer, pts);
		log.info("flush()");
    }
    
    @Override
    public void drain(DirectAudioPlayer mediaPlayer)
    {
		super.drain(mediaPlayer);
		log.info("drain()");
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
			// Gets coordinates proposal
			prepareCoordinates();
			// Sends coordinates proposal
			sendCoordinates();
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
	
	/**
	 * Adds a tuple(x, y, z) which represents the coordinates of
	 * a LED to lighten in the global LED-to-lighten list
	 * 
	 * @param pos_x
	 * @param pos_y
	 * @param pos_z 
	 */
	private void addPositionsToLighteningList(int pos_x, int pos_y, int pos_z)
	{
		positionsToLighten[lighteningIndex][0] = pos_x;
		positionsToLighten[lighteningIndex][1] = pos_y;
		positionsToLighten[lighteningIndex][2] = pos_z;
		lighteningIndex++;

		log.info(pos_x + ", " + pos_y + ", " + pos_z);
	}
	
	private void prepareCoordinates()
	{
		int abs_x = (Math.abs(getLineVariation(lineMaxAnalysis, true)) % syncWidth);
		int abs_y = (Math.abs(getLineVariation(lineMinAnalysis, true)) % syncDepth);
		int abs_z = (Math.abs(getLineVariation(lineAvgAnalysis, true)) % syncHeight);
		
		addPositionsToLighteningList(abs_x, abs_y, abs_z);
		
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

	private void produceClassicCoordinate()
	{
		int cla_x = (Math.abs(getLineVariation(lineMaxAnalysis, false)) % syncWidth);
		int cla_y = (Math.abs(getLineVariation(lineMinAnalysis, false)) % syncDepth);
		int cla_z = (Math.abs(getLineVariation(lineAvgAnalysis, false)) % syncHeight);

		addPositionsToLighteningList(cla_x, cla_y, cla_z);
	}
	
	private int getMirrorCoordinateValue(int origin, int dimension)
	{
		return (-1 * origin) + dimension - 1;
	}

	private void produceWidthMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int wid_x = getMirrorCoordinateValue(abs_x, syncWidth);
		
		addPositionsToLighteningList(wid_x, abs_y, abs_z);
	}
	
	private void produceDepthMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int dep_y = getMirrorCoordinateValue(abs_y, syncDepth);
		
		addPositionsToLighteningList(abs_x, dep_y, abs_z);
	}

	private void produceHeightMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		int hei_z = getMirrorCoordinateValue(abs_z, syncHeight);
		
		addPositionsToLighteningList(abs_x, abs_y, hei_z);
	}
	
	private void produceTripleMirrorCoordinate(int abs_x, int abs_y, int abs_z)
	{
		produceWidthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceDepthMirrorCoordinate(abs_x, abs_y, abs_z);
		produceHeightMirrorCoordinate(abs_x, abs_y, abs_z);
	}
	
	private void produceEightifyCoordinates(int abs_x, int abs_y, int abs_z)
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
	
	private void sendCoordinates()
	{
		streamingService.processCoordinates(positionsToLighten);

		positionsToLighten = new int[algoEffect.getSize()][3];
		lighteningIndex = 0;
		
		lineMaxAnalysis.clear();
		lineMinAnalysis.clear();
		lineAvgAnalysis.clear();
	}
}
