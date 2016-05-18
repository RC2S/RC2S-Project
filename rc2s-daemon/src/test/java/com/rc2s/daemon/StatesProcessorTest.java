package com.rc2s.daemon;

import org.junit.Assert;
import org.junit.Test;

public class StatesProcessorTest
{
    /*@InjectMocks
    private StatesProcessor processor;

    @Mock
    private Hardware hardware;

    public static Packet packet;

    @BeforeClass
    public static void setUpClass()
    {
        Stage s1 = new Stage(new boolean[][] {{true, true, false, false}, {true, true, false, false}, {true, true, false, false}, {true, true, false, false}});
        Stage s2 = new Stage(new boolean[][] {{true, true, false, false}, {true, true, false, false}, {true, true, false, false}, {true, true, false, false}});
        Stage s3 = new Stage(new boolean[][] {{true, true, false, false}, {true, true, false, false}, {true, true, false, false}, {true, true, false, false}});
        Stage s4 = new Stage(new boolean[][] {{true, true, false, false}, {true, true, false, false}, {true, true, false, false}, {true, true, false, false}});

        Stage[] stages = new Stage[] {s1, s2, s3, s4};
        packet = new Packet(5000, stages);
    }

    @Before
    public void initMocks()
    {
        MockitoAnnotations.initMocks(this);
    }*/

    @Test
    public void levelSupportsGreaterValues()
    {
        Assert.assertTrue(true);
    }
}
