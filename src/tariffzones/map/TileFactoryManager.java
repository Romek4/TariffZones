package tariffzones.map;

import java.util.List;

import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class TileFactoryManager {
	private List<TileFactory> tileFactories;
	
	public TileFactoryManager(List<TileFactory> tileFactories) {
		this.tileFactories = tileFactories;
	}
	
	public List<TileFactory> getTileFactoies() {
		return tileFactories;
	}
	
	public void setTileFactories(List<TileFactory> tileFactories) {
		this.tileFactories = tileFactories;
	}
}
