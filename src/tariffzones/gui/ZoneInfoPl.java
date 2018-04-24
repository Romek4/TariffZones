package tariffzones.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tariffzones.basicobjects.Zone;

public class ZoneInfoPl extends JPanel {
	private Zone zone;
	
	private JLabel colorLabel;
	private JPanel infoPanel;
	
	public ZoneInfoPl(Zone zone) {
		super();
		this.zone = zone;
		initialize();
	}

	private void initialize() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLayout(new GridLayout(1, 2, 5, 0));
		this.add(getColorLabel());
		this.add(getInfoPanel());
	}
	
	private JLabel getColorLabel() {
		if (colorLabel == null) {
			colorLabel = new JLabel();
			colorLabel.setBackground(zone.getColor());
			colorLabel.setOpaque(true);
		}
		return colorLabel;
	}
	
	public JPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new JPanel(new GridLayout(2, 1));
			infoPanel.setBackground(Color.WHITE);
			
			infoPanel.add(new JLabel("e = " + String.format("%.3f", zone.getE())));
			
			JLabel infoLabel = new JLabel();
			int stopsInZone = zone.getStopsInZone().size();
			if (stopsInZone == 1) {
				infoLabel.setText(String.format("%d", stopsInZone) + " stop in zone");
			}
			else {
				infoLabel.setText(String.format("%d", stopsInZone) + " stops in zone");
			}
			
			infoPanel.add(infoLabel);
		}
		return infoPanel;
	}
	
}
