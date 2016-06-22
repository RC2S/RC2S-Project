package com.rc2s.application.services.streaming;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

@Stateless
public class StreamingService implements IStreamingService
{
	@Override
	public void streamMusic()
	{
		String music = "/media/Data/Datas/Music/Caravan_Palace/LoneDigger.mp3";
		
		//firstTest(music);
		
		trueTest(music);
	}
	
	private void trueTest(String music)
	{
		System.out.println("------------------------------");
		System.out.println("*** Running true test ***");
		
		File				soundFile;
		InputStream			ins;
		BufferedInputStream	buff;
		AudioInputStream	ais;
		SourceDataLine		line;
		AudioFormat			fmt;
		 
		try
		{
			// Preparation
            soundFile = new File(music);
			System.out.println("111");
			ins = new FileInputStream(soundFile);
			System.out.println("222");
			buff = new BufferedInputStream(ins);
			System.out.println("333");
			ais	= AudioSystem.getAudioInputStream(buff);
			System.out.println("444");
			byte[] bytes = new byte[(int) soundFile.length()];
			ais.read(bytes);
			
			fmt	= ais.getFormat();
			
			DataLine.Info info	= new DataLine.Info(SourceDataLine.class, fmt);
			line				= (SourceDataLine) AudioSystem.getLine(info);
            line.open(fmt);
			line.start();
			
			float[] samples = new float[100000];
			//int retrievedSamples = 0;
			
			// Real thing
			for (int blen = 0; (blen = ais.read(bytes)) > -1;) {
				int slen;
				slen = SimpleAudioConversion.unpack(bytes, samples, blen, fmt);

				// Do something with samples

				blen = SimpleAudioConversion.pack(samples, bytes, slen, fmt);
				line.write(bytes, 0, blen);
			}
        }
		catch (LineUnavailableException e)
		{
            e.printStackTrace();
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
		
		
	}
	
	private void firstTest(String music)
	{
		System.out.println("------------------------------");
		System.out.println("*** Running first test ***");
	
		File f = new File(music);
		int size = (int)f.length();
		byte[] soundByteArray = new byte[size];

		try
		{
			FileInputStream fis = new FileInputStream(f);
			
			fis.read(soundByteArray);
			
			// Test data
			StringBuilder sb = new StringBuilder();
			int mini = 10000;
			int maxi = -10000;
			int total = 0;
			int count = 0;
			
			// Coordinates arraylist
			List<Coordinates3D> coordinatesBufferlist = new ArrayList<>();
			
			// Buffer length wanted for coordinates retrieving
			final short BUFFER_LENGTH = 300;
			short currentLength = 0;
			
			// Creating byte buffer
			byte[] byteBuffer = new byte[BUFFER_LENGTH];
			
			for (byte data : soundByteArray)
			{
				if (currentLength < BUFFER_LENGTH)
				{
					byteBuffer[currentLength] = data;
					currentLength++;
				}
				else
				{
					coordinatesBufferlist.addAll(mapBufferByteArrayToCoordinates3D(byteBuffer));
					currentLength = 0;
				}
				
				// Test
				count++;
				total += data;
				
				if (mini > data)
					mini = data;
				if (maxi < data)
					maxi = data;
				
				sb.append(data).append(", ");
			}
			
			System.out.println("Count : " + count);
			System.out.println("Total : " + total);
			System.out.println("Mini  : " + mini);
			System.out.println("Maxi  : " + maxi);
			System.out.println(sb.substring(0, 1000));
			// End Test
			
			/* Classical 3D mapping
			System.out.println("\nPrinting Coordinates");
			for (int index = 300; index < 800; index++)
				System.out.println(coordinatesBufferlist.get(index));
			*/
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * CoordinateMapper3D v0.1
	 * [byte, byte, byte] -> coordinate(x, y, z)
	 *
	 */
	private ArrayList<Coordinates3D> mapBufferByteArrayToCoordinates3D(byte[] soundBytes)
	{
		ArrayList<Coordinates3D> mappedSoundData = new ArrayList<>();
		
		for (int i = 0; i < soundBytes.length; i += 3)
		{
			mappedSoundData.add(
				new Coordinates3D(
					convertByteToPosition3D(soundBytes[i]),
					convertByteToPosition3D(soundBytes[i+1]),
					convertByteToPosition3D(soundBytes[i+2])
				)
			);
		}
		
		return mappedSoundData;
	}
	
	private byte convertByteToPosition3D(byte entry)
	{
		return (byte) ((entry / 64) + 2);
	}
	
	private class Coordinates3D
	{
		private final byte row;
		private final byte column;
		private final byte depth;

		public Coordinates3D(byte row, byte column, byte depth)
		{
			this.row	= row;
			this.column = column;
			this.depth	= depth;
		}

		public byte getRow()
		{
			return row;
		}

		public byte getColumn()
		{
			return column;
		}

		public byte getDepth()
		{
			return depth;
		}
		
		@Override
		public String toString()
		{
			return "Coord('" + row + "', '" + column + "', '" + depth + "')"; 
		}
	}
	/* END CoordinateMapper3D */
}



