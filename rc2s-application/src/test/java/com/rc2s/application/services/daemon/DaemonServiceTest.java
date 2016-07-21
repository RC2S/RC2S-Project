package com.rc2s.application.services.daemon;

import org.junit.*;
import javax.naming.NamingException;

public class DaemonServiceTest
{
	private IDaemonService daemonService;

	public static boolean[][][] raw, expected;

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
				{true, false, false, false}
			},
			{
				{false, false, false, false},
				{false, false, false, false},
				{true, false, false, false},
				{false, false, false, false}
			},

			{
				{false, false, false, false},
				{true, false, false, false},
				{false, false, false, false},
				{false, false, false, false}
			},
			{
				{true, false, false, false},
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
	public void formatStatesArray()
	{
		boolean[][][] states = daemonService.formatStatesArray(raw);

		for(int i = 0 ; i < states.length ; i++)
		{
			for(int j = 0 ; j < states[i].length ; j++)
			{
				for(int k = 0 ; k < states[i][j].length ; k++)
				{
					Assert.assertEquals(states[i][j][k], expected[i][j][k]);
				}
			}
		}
	}
}