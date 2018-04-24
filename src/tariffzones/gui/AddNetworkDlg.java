package tariffzones.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddNetworkDlg extends JPanel {

	private JLabel iconLabel;
	private JTextField networkNameTf;
	private JLabel stopsFileLb;
	private JLabel waysFileLb;
	private JButton stopsBtn;
	private JButton waysBtn;
	private JCheckBox dbImportChb;
	private JComboBox countryCb;
	private JComboBox networkTypeCb;
	
	private List countryIds;
	private List networkTypes;
	
//	private ActionListener actionListener;
	
//	private OpenNetworkFromFilesDlgController controller;
	

	public AddNetworkDlg(List networkTypes, List countryIds) {
		super();
		this.networkTypes = networkTypes;
		this.countryIds = countryIds;
		initialize();
//		getController().setView(this);
//		getController().activate();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 15, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getIconLabel(), gbc);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(new JLabel("Network name:"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getNetworkNameTf(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(new JLabel("Network type:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(getNetworkTypeCb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(new JLabel("Country:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(getCountryCb(), gbc);
		
//		gbc.gridx = 0;
//		gbc.gridy = 3;
//		this.add(new JLabel("Choose file for stops input:"), gbc);
//		
//		gbc.gridx = 1;
//		gbc.gridy = 3;
//		this.add(getStopsFileLb(), gbc);
//		
//		gbc.gridx = 2;
//		gbc.gridy = 3;
//		this.add(getStopsBtn(), gbc);
//		
//		gbc.gridx = 0;
//		gbc.gridy = 4;
//		this.add(new JLabel("Choose file for ways input:"), gbc);
//		
//		gbc.gridx = 1;
//		gbc.gridy = 4;
//		this.add(getWayFileLb(), gbc);
//		
//		gbc.gridx = 2;
//		gbc.gridy = 4;
//		this.add(getWaysBtn(), gbc);
//		
//		gbc.gridx = 0;
//		gbc.gridy = 5;
//		this.add(getDBImportChb(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel("Add Network");
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/networkIcon.png"));
				iconLabel.setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return iconLabel;
	}

	public JTextField getNetworkNameTf() {
		if (networkNameTf == null) {
			networkNameTf = new JTextField();
			networkNameTf.setPreferredSize(new Dimension(100, 19));
			networkNameTf.setMinimumSize(new Dimension(100, 19));
		}
		return networkNameTf;
	}
	
	public JComboBox getCountryCb() {
		if (countryCb == null) {
			countryCb = new JComboBox<>();
			if (countryIds != null) {
				countryCb.setModel((new DefaultComboBoxModel(countryIds.toArray())));
			}
		}
		return countryCb;
	}
	public JComboBox getNetworkTypeCb() {
		if (networkTypeCb == null) {
			networkTypeCb = new JComboBox<>();
			if (networkTypes != null) {
				networkTypeCb.setModel((new DefaultComboBoxModel(networkTypes.toArray())));
			}
		}
		return networkTypeCb;
	}
	
	public List getCountryIds() {
		return countryIds;
	}

//	public JLabel getStopsFileLb() {
//		if (stopsFileLb == null) {
//			stopsFileLb = new JLabel("Not selected");
//			stopsFileLb.setMinimumSize(new Dimension(100, 19));
//			stopsFileLb.setPreferredSize(new Dimension(100, 19));
//			stopsFileLb.setMaximumSize(new Dimension(100, 19));
//		}
//		return stopsFileLb;
//	}
//	
//	public JLabel getWayFileLb() {
//		if (waysFileLb == null) {
//			waysFileLb = new JLabel("Not selected");
//			waysFileLb.setMinimumSize(new Dimension(100, 19));
//			waysFileLb.setPreferredSize(new Dimension(100, 19));
//			waysFileLb.setMaximumSize(new Dimension(100, 19));
//		}
//		return waysFileLb;
//	}
//
//	public JCheckBox getDBImportChb() {
//		if (dbImportChb == null) {
//			dbImportChb = new JCheckBox("Import data to database also");
//			dbImportChb.setSelected(true);
//		}
//		return dbImportChb;
//	}
//
//	private JButton getWaysBtn() {
//		if (waysBtn == null) {
//			waysBtn = new JButton("Browse");
//		}
//		return waysBtn;
//	}
//
//	private JButton getStopsBtn() {
//		if (stopsBtn == null) {
//			stopsBtn = new JButton("Browse");
//		}
//		return stopsBtn;
//	}
//	
//	public void registryListeners() {
//		getStopsBtn().addActionListener(getActionListener());
//		getWaysBtn().addActionListener(getActionListener());
//	}
//	
//	public void unregistryListeners() {
//		getStopsBtn().removeActionListener(getActionListener());
//		getWaysBtn().removeActionListener(getActionListener());
//	}
//	
//	private ActionListener getActionListener() {
//		if (actionListener == null) {
//			actionListener = new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					if (e.getSource().equals(getStopsBtn())) {
//						getController().selectFile(getStopsFileLb());
//					}
//					else if (e.getSource().equals(getWaysBtn())) {
//						getController().selectFile(getWayFileLb());
//					}
//				}
//			};
//		}
//		return actionListener;
//	}
//	
//	private OpenNetworkFromFilesDlgController getController() {
//		if (controller == null) {
//			controller = new OpenNetworkFromFilesDlgController();
//		}
//		return controller;
//	}
}
