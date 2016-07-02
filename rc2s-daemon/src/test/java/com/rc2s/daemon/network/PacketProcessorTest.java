package com.rc2s.daemon.network;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PacketProcessorTest
{
	private final static long DURATION = 0L;
	private final static int CUBE_SIZE = 2;

	private static byte[] testBytes;

	@BeforeClass
	public static void setUp()
	{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream())
		{
			DataOutputStream dos = new DataOutputStream(bos);

			dos.writeLong(DURATION);
			dos.writeInt(CUBE_SIZE);
			dos.writeInt(CUBE_SIZE);
			dos.writeInt(CUBE_SIZE);

			dos.writeBoolean(true);
			dos.writeBoolean(true);
			dos.writeBoolean(false);
			dos.writeBoolean(false);
			dos.writeBoolean(false);
			dos.writeBoolean(false);
			dos.writeBoolean(true);
			dos.writeBoolean(true);

			dos.flush();
			testBytes = bos.toByteArray();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void forgePacketTest() throws IOException
	{
		PacketProcessor packetProcessor = new PacketProcessor(null, null, null);
		Packet packet = packetProcessor.forgePacket(testBytes);

		assertEquals(DURATION, packet.getDuration());
		assertEquals(CUBE_SIZE, packet.getStages().length);

		assertArrayEquals(new boolean[] {true, true}, packet.getStages()[0].getStates()[0]);
		assertArrayEquals(new boolean[] {false, false}, packet.getStages()[0].getStates()[1]);
		assertArrayEquals(new boolean[] {false, false}, packet.getStages()[1].getStates()[0]);
		assertArrayEquals(new boolean[] {true, true}, packet.getStages()[1].getStates()[1]);
	}
}
