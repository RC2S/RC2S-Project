package com.rc2s.application.services.streaming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.Stateless;

@Stateless
public class StreamingService implements IStreamingService
{
	@Override
	public void streamMusic()
	{
		System.out.println("IN");
		String music = "/media/Data/Datas/Music/Caravan_Palace/LoneDigger.mp3";
	
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
			ArrayList<Coordinates3D> coordinatesBufferlist = new ArrayList<>();
			
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



