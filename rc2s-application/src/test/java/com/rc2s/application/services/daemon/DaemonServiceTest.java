package com.rc2s.application.services.daemon;

import com.rc2s.common.vo.Size;
import org.junit.*;
import javax.naming.NamingException;

public class DaemonServiceTest
{
	private IDaemonService daemonService;

	public static boolean[][][] raw, expected;
	
	public final int sizeX				= 1;
	public final int sizeY				= 10;
	public final int sizeZ				= 100;
	public final boolean expectedState	= false;

	@BeforeClass
	public static void setUp()
	{
		raw = new boolean[][][] {
			{
				{true, false, false, false},
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false}
			},
			{
				{false, false, false, false},
				{true, false, false, false},
				{false, false, false, false},
				{false, false, false, false}
			},
			{
				{false, false, false, false},
				{false, false, false, false},
				{true, false, false, false},
				{false, false, false, false}
			},
			{
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false},
				{true, false, false, false}
			}
		};

		expected = new boolean[][][] {
			{
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, true}
			},
			{
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, true},
				{false, false, false, false}
			},

			{
				{false, false, false, false},
				{false, false, false, true},
				{false, false, false, false},
				{false, false, false, false}
			},
			{
				{false, false, false, true},
				{false, false, false, false},
				{false, false, false, false},
				{false, false, false, false}
			},
		};
	}

	@Before
	public void initContext() throws NamingException
	{
		daemonService = new DaemonService();
	}

	@After
	public void closeContext()
	{
		daemonService = null;
	}
	
	@Test
	public void generateBooleanArrayTest()
	{
		Size testSize = new Size(0, null, sizeX, sizeY, sizeZ, null, null);
		
		boolean[][][] stateStages = daemonService.generateBooleanArray(testSize, expectedState);
		
		for (boolean[][] stage : stateStages)
			for (boolean[] line : stage)
				for (int k = 0; k < line.length; k++)
					Assert.assertEquals(expectedState, line[k]);
	}

	@Test
	public void formatStatesArrayTest()
	{
		boolean[][][] states = daemonService.formatStatesArray(raw);

		for (int i = 0 ; i < states.length ; i++)
			for (int j = 0 ; j < states[i].length ; j++)
				for (int k = 0 ; k < states[i][j].length ; k++)
					Assert.assertEquals(expected[i][j][k], states[i][j][k]);
	}
}