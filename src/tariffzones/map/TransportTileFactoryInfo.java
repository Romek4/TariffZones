package tariffzones.map;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class TransportTileFactoryInfo extends TileFactoryInfo implements ImageTextComboBoxItem {
	private static final int max = 19;

	private ImageIcon icon;
	
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
	
	@Override
	public ImageIcon getImageIcon() {
		if (icon == null) {
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/exportIcon.png"));
				icon = new ImageIcon(img.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			} catch (IOException e) {
				e.printStackTrace();
				icon = null;
			}
		}
		return icon;
	}
	
	@Override
	public String getText() {
		return this.getName();
	}
}
