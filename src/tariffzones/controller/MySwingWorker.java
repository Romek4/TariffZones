package tariffzones.controller;

import java.util.ArrayList;

import javax.swing.SwingWorker;

import tariffzones.basicobjects.Zone;

public class MySwingWorker extends SwingWorker<ArrayList<Zone>, Integer> {

	private SolverParameters params;
	private TariffZonesProblemSolver solver;
	
	@Override
	protected ArrayList<Zone> doInBackground() throws Exception {
		try {
			return solver.run();
		} catch(Exception e) {
			
		}
		return null;
	}

}
