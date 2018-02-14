package tariffzones.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tariffzones.controller.OpenNetworkFromFilesDlgController;

public class OpenNetworkFromFilesDlg extends JPanel {
	private JLabel iconLabel;
	private JLabel stopsFileLb;
	private JLabel waysFileLb;
	private JButton stopsBtn;
	private JButton waysBtn;
	
	private ActionListener actionListener;
	private OpenNetworkFromFilesDlgController controller;

	public OpenNetworkFromFilesDlg() {
		super();
		initialize();
		getController().setView(this);
		getController().activate();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getIconLabel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(new JLabel("Choose file for stops input: "), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		this.add(getStopsFileLb(), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		this.add(getStopsBtn(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(new JLabel("Choose file for ways input: "), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		this.add(getWayFileLb(), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		this.add(getWaysBtn(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
		}
		return iconLabel;
	}
	
	public JLabel getStopsFileLb() {
		if (stopsFileLb == null) {
			stopsFileLb = new JLabel("Not selected");
			stopsFileLb.setMinimumSize(new Dimension(100, 19));
			stopsFileLb.setPreferredSize(new Dimension(100, 19));
			stopsFileLb.setMaximumSize(new Dimension(100, 19));
		}
		return stopsFileLb;
	}
	
	public JLabel getWayFileLb() {
		if (waysFileLb == null) {
			waysFileLb = new JLabel("Not selected");
			waysFileLb.setMinimumSize(new Dimension(100, 19));
			waysFileLb.setPreferredSize(new Dimension(100, 19));
			waysFileLb.setMaximumSize(new Dimension(100, 19));
		}
		return waysFileLb;
	}

	private JButton getWaysBtn() {
		if (waysBtn == null) {
			waysBtn = new JButton("Browse");
		}
		return waysBtn;
	}

	private JButton getStopsBtn() {
		if (stopsBtn == null) {
			stopsBtn = new JButton("Browse");
		}
		return stopsBtn;
	}

	public void registryListeners() {
		getStopsBtn().addActionListener(getActionListener());
		getWaysBtn().addActionListener(getActionListener());
	}
	
	public void unregistryListeners() {
		getStopsBtn().removeActionListener(getActionListener());
		getWaysBtn().removeActionListener(getActionListener());
	}
	
	private ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(getStopsBtn())) {
						getController().selectFile(getStopsFileLb());
					}
					else if (e.getSource().equals(getWaysBtn())) {
						getController().selectFile(getWayFileLb());
					}
				}
			};
		}
		return actionListener;
	}
	
	private OpenNetworkFromFilesDlgController getController() {
		if (controller == null) {
			controller = new OpenNetworkFromFilesDlgController();
		}
		return controller;
	}
	
	public void setIconLabelImg(Image img) {
		getIconLabel().setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
	}
	
	public void setIconLabelText(String text) {
		getIconLabel().setText(text);
	}
	
	public void showDlg() {
//		this.setModal(true);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		OpenNetworkFromFilesDlg dlg = new OpenNetworkFromFilesDlg();
		dlg.showDlg();
	}
}
