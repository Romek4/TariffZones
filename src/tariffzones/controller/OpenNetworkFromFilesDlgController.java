package tariffzones.controller;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

import tariffzones.gui.OpenNetworkFromFilesDlg;

public class OpenNetworkFromFilesDlgController {
	private OpenNetworkFromFilesDlg view;
	
	public OpenNetworkFromFilesDlgController() {
	}

	public void selectFile(JLabel label) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(view.getParent());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				label.setText(file.getAbsolutePath());
			}
		}
	}
	
	public void activate() {
		view.unregistryListeners();
		view.registryListeners();
	}
	
	public void setView(OpenNetworkFromFilesDlg openNetworkFromFilesDlg) {
		this.view = openNetworkFromFilesDlg;
	}
}
