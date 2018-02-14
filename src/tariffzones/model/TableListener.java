package tariffzones.model;

import java.util.EventListener;

public interface TableListener extends EventListener {
	
	void rowSelected(TableEvent e);
	
}
