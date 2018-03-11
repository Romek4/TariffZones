package tariffzones.model;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import tariffzones.gui.ZoneInfoPl;
import tariffzones.tariffzonesprocessor.greedy.Zone;

public class ZoneCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	        boolean isSelected, boolean hasFocus, int row, int column) {
		
		Zone zone = (Zone) value;
		
		ZoneInfoPl zoneInfoPl = new ZoneInfoPl(zone);
		
		if (isSelected) {
			zoneInfoPl.setBackground(table.getSelectionBackground());
	    } else {
	    	zoneInfoPl.setBackground(table.getBackground());
	    }
		
	    return zoneInfoPl;
	}

}
