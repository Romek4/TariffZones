package tariffzones.map;

import org.jxmapviewer.viewer.AbstractTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class DefaultTileFactory extends AbstractTileFactory {

	public DefaultTileFactory(TileFactoryInfo info) {
		super(info);
	}

	public String toString() {
		return this.getInfo().getName();
	}
	
}
