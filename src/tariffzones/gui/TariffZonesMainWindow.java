package tariffzones.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import tariffzones.controller.TariffZonesController;

public class TariffZonesMainWindow extends JFrame {
	
	private JPanel toolBoxPanel, mapMarkerDotPanel;
	private JLabel infoLabel;
	
	private boolean makeAPointButtonPressed = false;
	private TariffZonesController tariffZonesController;
	
	public TariffZonesMainWindow() {
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Tariff Zones Problem Solver");
		initializeComponents();
		
		tariffZonesController = new TariffZonesController();
	}

	private void initializeComponents() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem fileMenuItem = new JMenuItem("File");
		JMenuItem importMenuItem = new JMenuItem("Import");
		JMenuItem helpMenuItem = new JMenuItem("Help");
		menuBar.add(fileMenuItem);
		menuBar.add(importMenuItem);
		menuBar.add(helpMenuItem);
		GridBagConstraints menuBarConstraints = new GridBagConstraints();
		menuBarConstraints.gridx = 0;
		menuBarConstraints.gridy = 0;
		menuBarConstraints.gridwidth = 5;
		menuBarConstraints.gridheight = 1;
		menuBarConstraints.fill = GridBagConstraints.HORIZONTAL;
		menuBarConstraints.anchor = GridBagConstraints.PAGE_START;
		this.add(menuBar, menuBarConstraints);
		
		//ToolBoxPanel - upperleft
		toolBoxPanel = new JPanel();
		GridBagConstraints toolBoxPanelConstraints = new GridBagConstraints();
		toolBoxPanelConstraints.gridx = 0;
		toolBoxPanelConstraints.gridy = 1;
		toolBoxPanelConstraints.gridwidth = 2;
		toolBoxPanelConstraints.gridheight = 2;
		toolBoxPanelConstraints.weightx = 0.05;
		toolBoxPanelConstraints.insets = new Insets(1, 1, 1, 1);
		toolBoxPanelConstraints.fill = GridBagConstraints.BOTH;
		this.add(toolBoxPanel, toolBoxPanelConstraints);
		initializeToolBoxPanel();
		
		//MapMarkerDotPanel - lowerleft
		initializeMapMarkerDotPanel();
		
		//InfoPanel - bottom
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.LIGHT_GRAY);
		infoLabel = new JLabel("Use double click left mouse button for zoom in. Drag right mouse button to move.");
		infoPanel.add(infoLabel);
		GridBagConstraints infoPanelConstraints = new GridBagConstraints();
		infoPanelConstraints.gridx = 0;
		infoPanelConstraints.gridy = 5;
		infoPanelConstraints.gridwidth = 5;
		infoPanelConstraints.gridheight = 1;
		infoPanelConstraints.weighty = 0.03;
		infoPanelConstraints.insets = new Insets(0, 1, 1, 1);
		infoPanelConstraints.fill = GridBagConstraints.BOTH;
		this.add(infoPanel, infoPanelConstraints);

		//JMapViewer
		JMapViewer mapViewer = new JMapViewer();
		mapViewer.setLayout(new BorderLayout());
		JLabel latLonLabel = new JLabel("");
		latLonLabel.setHorizontalAlignment(JLabel.RIGHT);
		mapViewer.add(latLonLabel, BorderLayout.SOUTH);
		mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				latLonLabel.setText("[" + Double.toString(mapViewer.getPosition(e.getX(), e.getY()).getLat()) + ", "
										+ Double.toString(mapViewer.getPosition(e.getX(), e.getY()).getLon()) + "]");
			}
		});
		mapViewer.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (makeAPointButtonPressed) {
					double lat = mapViewer.getPosition(e.getX(), e.getY()).getLat();
					double lon = mapViewer.getPosition(e.getX(), e.getY()).getLon();
					String pointName = JOptionPane.showInputDialog(mapViewer, "Set name for this point");
					tariffZonesController.addBusStop(pointName, lat, lon);
					mapViewer.addMapMarker(new MapMarkerDot(pointName, new Coordinate(lat, lon)));;
					makeAPointButtonPressed = false;
					infoLabel.setText("Use double click left mouse button for zoom in. Drag right mouse button to move.");
				}
			}
		});
		GridBagConstraints mapViewerConstraints = new GridBagConstraints();
		mapViewerConstraints.gridx = 2;
		mapViewerConstraints.gridy = 1;
		mapViewerConstraints.gridwidth = 3;
		mapViewerConstraints.gridheight = 4;
		mapViewerConstraints.weightx = 1;
		mapViewerConstraints.weighty = 1;
		mapViewerConstraints.fill = GridBagConstraints.BOTH;
		this.add(mapViewer, mapViewerConstraints);
		
		this.validate();
		this.pack();
	}

	private void initializeMapMarkerDotPanel() {
		mapMarkerDotPanel = new JPanel();
		//mapMarkerDotPanel.setBackground(Color.DARK_GRAY);
		mapMarkerDotPanel.setLayout(new GridBagLayout());
		JLabel mapMarkerDotPanelNameLabel = new JLabel("MapMarkerDots");
		GridBagConstraints mapMarkerDotPanelNameLabelConstraints = new GridBagConstraints();
		mapMarkerDotPanelNameLabelConstraints.gridx = 0;
		mapMarkerDotPanelNameLabelConstraints.gridy = 0;
		mapMarkerDotPanelNameLabelConstraints.gridwidth = 2;
		mapMarkerDotPanelNameLabelConstraints.gridheight = 1;
		mapMarkerDotPanel.add(mapMarkerDotPanelNameLabel, mapMarkerDotPanelNameLabelConstraints);
		
//		JList mapMarkerDotlist = new JList(new String[] {"Roman", "Martin", "Rado"});
//		GridBagConstraints mapMarkerDotlistConstraints = new GridBagConstraints();
//		mapMarkerDotlistConstraints.gridx = 0;
//		mapMarkerDotlistConstraints.gridy = 1;
//		mapMarkerDotlistConstraints.gridwidth = 2;
//		mapMarkerDotlistConstraints.gridheight = 1;
//		mapMarkerDotlistConstraints.weightx = 1;
//		mapMarkerDotPanel.add(mapMarkerDotlist, mapMarkerDotlistConstraints);
		
		GridBagConstraints mapMarkerDotPanelConstraints = new GridBagConstraints();
		mapMarkerDotPanelConstraints.gridx = 0;
		mapMarkerDotPanelConstraints.gridy = 3;
		mapMarkerDotPanelConstraints.gridwidth = 2;
		mapMarkerDotPanelConstraints.gridheight = 2;
		//mapMarkerDotPanelConstraints.weighty = 0.5;
		mapMarkerDotPanelConstraints.insets = new Insets(0, 1, 1, 1);
		mapMarkerDotPanelConstraints.fill = GridBagConstraints.BOTH;
		this.add(mapMarkerDotPanel, mapMarkerDotPanelConstraints);
	}

	private void initializeToolBoxPanel() {
		toolBoxPanel.setBackground(Color.LIGHT_GRAY);
		toolBoxPanel.setLayout(new GridBagLayout());
		
		JLabel toolBoxPanelNameLabel = new JLabel("ToolBox");
		GridBagConstraints toolBoxPanelNameLabelConstraints = new GridBagConstraints();
		toolBoxPanelNameLabelConstraints.gridx = 0;
		toolBoxPanelNameLabelConstraints.gridy = 0;
		toolBoxPanelNameLabelConstraints.gridwidth = 2;
		toolBoxPanelNameLabelConstraints.gridheight = 1;
		//toolBoxPanelNameLabelConstraints.weighty = 0.5;
		//toolBoxPanelNameLabelConstraints.anchor = GridBagConstraints.PAGE_START;
		toolBoxPanel.add(toolBoxPanelNameLabel, toolBoxPanelNameLabelConstraints);
		
//		JLabel circleIcon = new JLabel();
//		circleIcon.setIcon(new ImageIcon("orange-circle-png-3.png"));
//		GridBagConstraints circleIconConstraints = new GridBagConstraints();
//		circleIconConstraints.gridx = 0;
//		circleIconConstraints.gridy = 1;
//		circleIconConstraints.gridwidth = 1;
//		circleIconConstraints.gridheight = 1;
//		toolBoxPanel.add(circleIcon, circleIconConstraints);
		
		JButton mapMakerDotBtn = new JButton("Make a point");
		//mapMakerDotBtn.setIcon(new ImageIcon("orange-circle-png-3.png"));
		mapMakerDotBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				infoLabel.setText("Use left mouse button to point on map.");
				makeAPointButtonPressed = true;
			}
		});
		GridBagConstraints mapMakerDotBtnConstraints = new GridBagConstraints();
		mapMakerDotBtnConstraints.gridx = 1;
		mapMakerDotBtnConstraints.gridy = 1;
		mapMakerDotBtnConstraints.gridwidth = 2;
		mapMakerDotBtnConstraints.gridheight = 1;
		mapMakerDotBtnConstraints.weightx = 1;
		mapMakerDotBtnConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(mapMakerDotBtn, mapMakerDotBtnConstraints);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TariffZonesMainWindow().setVisible(true);
            }
        });
	}

}
