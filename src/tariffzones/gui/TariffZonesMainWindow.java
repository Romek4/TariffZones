package tariffzones.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import tariffzones.controller.TariffZonesController;
import tariffzones.model.sql.processor.DataImporter;

public class TariffZonesMainWindow extends JFrame {
	
	private JPanel toolBoxPanel, tablePanel;
	private JLabel infoLabel;
	private JMapViewer mapViewer;
	private JTable stopTable, wayTable;
	
	private boolean makeAPointButtonPressed = false;
	private TariffZonesController tariffZonesController;
	
	private static final String DEFAULT_INFO_MSG = "Use double click left mouse button for zoom in. Drag right mouse button to move.";
	private static final String MAKEPOINT_INFO_MSG = "Use left mouse button to point on the map.";
	
	public TariffZonesMainWindow() {
		tariffZonesController = new TariffZonesController();
		
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Tariff Zones Problem Solver");
		initializeComponents();
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
		toolBoxPanelConstraints.gridwidth = 1;
		toolBoxPanelConstraints.gridheight = 2;
		toolBoxPanelConstraints.weightx = 0.2;
		toolBoxPanelConstraints.insets = new Insets(1, 1, 1, 1);
		toolBoxPanelConstraints.fill = GridBagConstraints.BOTH;
		this.add(toolBoxPanel, toolBoxPanelConstraints);
		initializeToolBoxPanel();
		
		//MapMarkerDotPanel - lowerleft
		initializeTablePanel();
		
		//InfoPanel - bottom
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.LIGHT_GRAY);
		infoLabel = new JLabel(DEFAULT_INFO_MSG);
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
		mapViewer = new JMapViewer();
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
					String stopNumber = JOptionPane.showInputDialog(mapViewer, "Set number for this stop");
					if (stopNumber == null) {
						makeAPointButtonPressed = false;
						infoLabel.setText(DEFAULT_INFO_MSG);
						return;
					}
					String stopName = JOptionPane.showInputDialog(mapViewer, "Set name for this stop");
					if (stopName == null) {
						makeAPointButtonPressed = false;
						infoLabel.setText(DEFAULT_INFO_MSG);
						return;
					}
					tariffZonesController.addBusStop(Integer.parseInt(stopNumber), stopName, lat, lon);
					mapViewer.addMapMarker(new MapMarkerDot(stopName, new Coordinate(lat, lon)));;
					makeAPointButtonPressed = false;
					infoLabel.setText(DEFAULT_INFO_MSG);
				}
			}
		});
		GridBagConstraints mapViewerConstraints = new GridBagConstraints();
		mapViewerConstraints.gridx = 2;
		mapViewerConstraints.gridy = 1;
		mapViewerConstraints.gridwidth = 3;
		mapViewerConstraints.gridheight = 4;
		mapViewerConstraints.weightx = 0.8;
		mapViewerConstraints.weighty = 1;
		mapViewerConstraints.fill = GridBagConstraints.BOTH;
		this.add(mapViewer, mapViewerConstraints);
		
		this.validate();
		this.pack();
	}

	private void initializeTablePanel() {
		tablePanel = new JPanel();
		tablePanel.setBackground(Color.DARK_GRAY);
		tablePanel.setLayout(new GridBagLayout());
		
		JComboBox<String> stopComboBox = new JComboBox<String>();
		stopComboBox.addItem("Bus stops");
		stopComboBox.addItem("Ways");
		GridBagConstraints stopComboBoxConstraints = new GridBagConstraints();
		stopComboBoxConstraints.gridx = 0;
		stopComboBoxConstraints.gridy = 1;
		stopComboBoxConstraints.weightx = 1;
		stopComboBoxConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(stopComboBox, stopComboBoxConstraints);
		
		stopTable = new JTable();
		GridBagConstraints stopTableConstraints = new GridBagConstraints();
		stopTableConstraints.gridx = 0;
		stopTableConstraints.gridy = 2;
		stopTableConstraints.weighty = 0.5;
		stopTableConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(new JScrollPane(stopTable), stopTableConstraints);
		
		JComboBox<String> wayComboBox = new JComboBox<String>();
		wayComboBox.addItem("Ways");
		wayComboBox.addItem("Bus stops");
		GridBagConstraints wayComboBoxConstraints = new GridBagConstraints();
		wayComboBoxConstraints.gridx = 0;
		wayComboBoxConstraints.gridy = 3;
		wayComboBoxConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(wayComboBox, wayComboBoxConstraints);
		
		wayTable = new JTable();
		GridBagConstraints wayTableConstraints = new GridBagConstraints();
		wayTableConstraints.gridx = 0;
		wayTableConstraints.gridy = 4;
		wayTableConstraints.weighty = 0.5;
		wayTableConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(new JScrollPane(wayTable), wayTableConstraints);
		
		GridBagConstraints tablePanelConstraints = new GridBagConstraints();
		tablePanelConstraints.gridx = 0;
		tablePanelConstraints.gridy = 3;
		tablePanelConstraints.weightx = 0.2;
		tablePanelConstraints.weighty = 0.5;
		tablePanelConstraints.fill = GridBagConstraints.BOTH;
		tablePanelConstraints.insets = new Insets(0, 1, 1, 1);
		this.add(tablePanel, tablePanelConstraints);
	}

	private void initializeToolBoxPanel() {
		toolBoxPanel.setBackground(Color.LIGHT_GRAY);
		toolBoxPanel.setLayout(new GridBagLayout());
		
		JComboBox citiesComboBox = new JComboBox<>();
		tariffZonesController.getCityNames(citiesComboBox);
		GridBagConstraints citiesComboBoxConstraints = new GridBagConstraints();
		citiesComboBoxConstraints.gridx = 0;
		citiesComboBoxConstraints.gridy = 0;
		citiesComboBoxConstraints.weightx = 0.7;
		citiesComboBoxConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(citiesComboBox, citiesComboBoxConstraints);
		
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tariffZonesController.addStopsInCityToMap(mapViewer, stopTable, wayTable, citiesComboBox.getSelectedItem().toString());
			}
		});
		GridBagConstraints loadButtonConstraints = new GridBagConstraints();
		loadButtonConstraints.gridx = 1;
		loadButtonConstraints.gridy = 0;
		loadButtonConstraints.weightx = 0.3;
		loadButtonConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(loadButton, loadButtonConstraints);
		
		JLabel toolBoxPanelNameLabel = new JLabel("ToolBox");
		GridBagConstraints toolBoxPanelNameLabelConstraints = new GridBagConstraints();
		toolBoxPanelNameLabelConstraints.gridx = 0;
		toolBoxPanelNameLabelConstraints.gridy = 1;
		toolBoxPanelNameLabelConstraints.gridwidth = 2;
		toolBoxPanelNameLabelConstraints.gridheight = 1;
		toolBoxPanelNameLabelConstraints.insets = new Insets(4, 4, 4, 4);
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
				infoLabel.setText(MAKEPOINT_INFO_MSG);
				makeAPointButtonPressed = true;
			}
		});
		GridBagConstraints mapMakerDotBtnConstraints = new GridBagConstraints();
		mapMakerDotBtnConstraints.gridx = 0;
		mapMakerDotBtnConstraints.gridy = 2;
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