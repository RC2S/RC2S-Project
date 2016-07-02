package com.rc2s.application.services.streaming;

import java.io.BufferedOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

public class CallbackAdapter extends DefaultAudioCallbackAdapter
{
	private static long currentTime;
	private static StringBuilder algoBuilder;
	
	private final int positionsPerSecond;
	
    private final Logger log = LogManager.getLogger(CallbackAdapter.class);
    
    private BufferedOutputStream out = null;
    
    public CallbackAdapter(int blockSize)
    {
        super(blockSize);
        out = new BufferedOutputStream(System.out);
		
		currentTime = 0;
		algoBuilder = new StringBuilder();
		positionsPerSecond = 40;
    }

    @Override
    protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] bytes, int sampleCount, long pts)
    {
		try
		{
			System.out.println("Time : " + mediaPlayer.getTime() + "\n"); // +261/262 every 10 turn --> 1sec ~ 40 turns
			
			// Time changed, send retrieved datas
			if (mediaPlayer.getTime() != currentTime)
			{
				System.out.println("TIME DIFFERENT !");
				currentTime = mediaPlayer.getTime();
			}

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
			algoBuilder.append(getLineWord(sampleLine, sampleCount));

			//System.out.println("Play		: " + timesOnPlay); // 2288 for 60.000L
			//timesOnPlay++;
			
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

	// Transform 4 bytes that represent a sample to
	// a representation (0 -> 28) used in a line
	private byte getSampleDef(byte[] sampleBytes)
	{
		byte result = 0;
		
		// Add a value between 0 and 7
		for (byte smpb : sampleBytes)
			result += (smpb >> 5) +0b0100;
		// 0 <= final result <= 28
		
		return result;
	}
	
	// Use a line of sampleCount bytes transformed to N coordinates
	// In order to graph a sound representation
	// 1 line is about 26ms which means 10 lines is 260ms ~
	// 10 lines should be represented by at least 10 positions
	// So one line should give at least 1 position
	// Can be changed via positionsPerSecond attribute
	private String getLineWord(byte[] sampleLine, int sampleCount)
	{	
		// Line possesses values between 0 and 28
		
		int max = 0, min = 255;
		int average = 0;
		
		System.out.println("******* In getlineWord() ********");
		
		StringBuilder sb = new StringBuilder();
		for (byte data : sampleLine)
		{
			// Compute
			average += data;
			if (data > max)
				max = data;
			if (data < min)
				min = data;
			
			sb.append(data);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		System.out.println("Line found	: " + sb.toString());
		System.out.println("Max			: " + max);
		System.out.println("Min			: " + min);
		System.out.println("Average		: " + (average / sampleCount));
		System.out.println("******* Out getLineWord() *********");
		
		System.out.println(".");
		System.out.println(".");
		System.out.println(".");
		
		return sb.toString();
	}
}
