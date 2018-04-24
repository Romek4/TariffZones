package tariffzones.basicobjects;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import tariffzones.gui.ZoneInfoPl;

public class ZoneCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	        boolean isSelected, boolean hasFocus, int row, int column) {
		
		Zone zone = (Zone) value;
		
		ZoneInfoPl zoneInfoPl = new ZoneInfoPl(zone);
		
		if (isSelected) {
			zoneInfoPl.setBackground(table.getSelectionBackground());
			zoneInfoPl.getInfoPanel().setBackground(table.getSelectionBackground());
	    } else {
	    	zoneInfoPl.setBackground(table.getBackground());
	    	zoneInfoPl.getInfoPanel().setBackground(table.getBackground());
	    }
		
	    return zoneInfoPl;
	}

}
