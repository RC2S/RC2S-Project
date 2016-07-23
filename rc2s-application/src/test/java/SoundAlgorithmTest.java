import com.rc2s.application.services.streaming.CallbackAdapter;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundAlgorithmTest {
	
	private final char LIGHT_MODE	= 'L';
	private final char STAGE_MODE	= 'S';
	private final char CUBE_MODE	= 'C';
	
	private final int[] EMPTY_DIMENSIONS	= new int[] { 0, 0, 0, 0 };
	private final int[] CHOSEN_DIMENSIONS	= new int[] { 10, 10, 10, 1 };
	
	private final static List<Integer> DATALINE	= new ArrayList<>();
	private final boolean ABSOLUTE				= true;
	private final boolean NOT_ABSOLUTE			= false;
	private final static int UNIQUE_VALUE		= -1;
	
	private static CallbackAdapter callback;
	
	/**
	 * BEGIN callback manipulation
	 */
	private static void instanciateCallbackAdapter(int[] dimensions, char ledMode)
	{
		callback = new CallbackAdapter(null, 4, dimensions, ledMode);
	}
	
	private static void emptyCallbackAdapter()
	{
		callback = null;
	}
	/**
	 * END callback manipulation
	 */
	
	
	/**
	 * BEGIN Data preparation for line variation
	 */
	private static void prepareOneData()
	{
		DATALINE.add(UNIQUE_VALUE);
	}
	
	private static void prepareRandomData()
	{
		for (int val = 100; val > -1; val--)
			DATALINE.add((int) (Math.random() * 100));
	}
	/**
	 * END Data preparation for line variation
	 */
	
	
	@After
	public void tearDown()
	{
		emptyCallbackAdapter();
	}
	
	
	/**
	 * BEGIN getLineVariation() tests
	 */
	@Test
	public void getLineVariationEmptyDatas()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, LIGHT_MODE);
		
		int result = callback.getLineVariation(DATALINE, NOT_ABSOLUTE);
		
		assertEquals(0, result);
	}
	
	@Test
	public void getLineVariationOneData()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, LIGHT_MODE);
		
		prepareOneData();
		
		int notAbsResult	= callback.getLineVariation(DATALINE, NOT_ABSOLUTE);
		int absResult		= callback.getLineVariation(DATALINE, ABSOLUTE);
		
		assertEquals(UNIQUE_VALUE, notAbsResult);
		assertEquals(Math.abs(UNIQUE_VALUE), absResult);
	}
	
	@Test
	public void getLineVariationRandomData()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, LIGHT_MODE);
		
		prepareRandomData();
		
		int absResult		= callback.getLineVariation(DATALINE, ABSOLUTE);
		
		assertTrue(absResult > 0);
	}
	/**
	 * END getLineVariation() tests
	 */
	
	
	/**
	 * BEGIN getLighteningSize() tests
	 */
	@Test
	public void getLighteningSizeWithLightModeTest()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, LIGHT_MODE);
		
		int result = callback.getLighteningSize();
		
		assertEquals(1, result);
	}
	
	@Test
	public void getLighteningSizeWithStageModeEmptyDimensionsTest()
	{
		instanciateCallbackAdapter(EMPTY_DIMENSIONS, STAGE_MODE);
		
		int result = callback.getLighteningSize();
		
		// Result shall be 4 * 4 = 16
		assertEquals(16, result);
	}
	
	@Test
	public void getLighteningSizeWithStageModeWithDimensionsTest()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, STAGE_MODE);
		
		int result = callback.getLighteningSize();
		
		// Result shall be 10 * 10 = 100
		assertEquals(100, result);
	}
	
	@Test
	public void getLighteningSizeWithCubeModeEmptyDimensionsTest()
	{
		instanciateCallbackAdapter(EMPTY_DIMENSIONS, CUBE_MODE);
		
		int result = callback.getLighteningSize();
		
		// Result shall be 4 * 4 * 4 = 64
		assertEquals(64, result);
	}
	
	@Test
	public void getLighteningSizeWithCubeModeWithDimensionsTest()
	{
		instanciateCallbackAdapter(CHOSEN_DIMENSIONS, CUBE_MODE);
		
		int result = callback.getLighteningSize();
		
		// Result shall be 10 * 10 * 10 = 1000
		assertEquals(1000, result);
	}
	/**
	 * END getLighteningSize() tests
	 */
}
