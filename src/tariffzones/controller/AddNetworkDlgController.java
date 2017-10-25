package tariffzones.controller;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import tariffzones.gui.AddNetworkDlg;

public class AddNetworkDlgController {
	private AddNetworkDlg view;
	
	public AddNetworkDlgController() {
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
	
	public void setView(AddNetworkDlg addNetworkDlg) {
		this.view = addNetworkDlg;
	}
}
