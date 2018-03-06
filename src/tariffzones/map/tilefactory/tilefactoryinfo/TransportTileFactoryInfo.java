package tariffzones.map.tilefactory.tilefactoryinfo;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class TransportTileFactoryInfo extends TileFactoryInfo {
	private static final int max = 19;

	/**
	 * Default constructor
	 */	
	public TransportTileFactoryInfo()
	{
		this("Transport", "http://a.tile2.opencyclemap.org/transport");
	}

	public TransportTileFactoryInfo(String name, String baseURL)
	{
		super(name, 
				1, max - 2, max, 
				256, true, true, 					// tile size is 256 and x/y orientation is normal
				baseURL,
				"x", "y", "z");						// 5/15/10.png
	}

	@Override
	public String getTileUrl(int x, int y, int zoom)
	{
		zoom = max - zoom;
		String url = this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
		return url;
	}

}
