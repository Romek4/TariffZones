package tariffzones.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tariffzones.tariffzonesprocessor.greedy.Zone;

public class ZoneInfoPl extends JPanel {
	private Zone zone;
	
	private JLabel colorLabel;
	private JLabel infoLabel;
	
	public ZoneInfoPl(Zone zone) {
		super();
		this.zone = zone;
		initialize();
	}

	private void initialize() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.3;
		gbc.weighty = 0.5;
				
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		this.add(getColorLabel(), gbc);
		
		gbc.weightx = 0.7;
		gbc.gridheight = 1;
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(new JLabel("e = " + String.format("%.3f", zone.getE())), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getInfoLabel(), gbc);
	}
	
	private JLabel getColorLabel() {
		if (colorLabel == null) {
			colorLabel = new JLabel();
			colorLabel.setBackground(zone.getColor());
			colorLabel.setOpaque(true);
		}
		return colorLabel;
	}
	
	private JLabel getInfoLabel() {
		if (infoLabel == null) {
			infoLabel = new JLabel();
			
			int stopsInZone = zone.getStopsInZone().size();
			if (stopsInZone == 1) {
				infoLabel.setText(stopsInZone + " stop in zone");
			}
			else {
				infoLabel.setText(stopsInZone + " stops in zone");
			}
		}
		return infoLabel;
	}
	
}
