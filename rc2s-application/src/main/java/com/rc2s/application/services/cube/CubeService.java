package com.rc2s.application.services.cube;

import com.rc2s.common.vo.Cube;
import com.rc2s.dao.cube.ICubeDAO;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CubeService implements ICubeService
{
	@EJB
    private ICubeDAO cubeDAO;
    
    @Override
    public List<Cube> getCubes()
    {
        return cubeDAO.getCubes();
    }
	
	@Override
	public boolean getStatus(Cube c)
	{
		try
		{
			boolean win = System.getProperty("os.name").toLowerCase().contains("win");

			ProcessBuilder processBuilder = new ProcessBuilder("ping", win ? "-n" : "-c", "1", c.getIp());
			Process proc = processBuilder.start();

			int code = proc.waitFor();
			return (code == 0);
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
