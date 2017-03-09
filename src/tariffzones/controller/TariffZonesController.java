package tariffzones.controller;

import tariffzones.model.Coordinate;
import tariffzones.model.TariffZonesModel;

public class TariffZonesController {
	private TariffZonesModel model;
	
	public TariffZonesController() {
		model = new TariffZonesModel();
	}
	
	public boolean addBusStop(String stopName, double latitude, double longitude) {
		if (!model.addStop(stopName, new Coordinate(latitude, longitude))) {
			return false;
		}
		return true;
	}

	
}
