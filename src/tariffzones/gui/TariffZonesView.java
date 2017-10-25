package tariffzones.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import tariffzones.controller.TariffZonesController;
import tariffzones.model.MyCoordinate;
import tariffzones.model.TableEvent;
import tariffzones.model.TableListener;
import tariffzones.model.TableModel;
import tariffzones.model.sql.processor.DataImporter;

public class TariffZonesView extends JFrame {
	
	private JPanel toolBoxPanel, tablePanel;
	private JLabel infoLabel;
	private JLabel latLonLabel;
	private JMapViewer mapViewer;
	private JTable stopTable, wayTable;
	private JComboBox citiesCb;
	private JComboBox stopCb;
	private JComboBox wayCb;
	
	private JButton loadBtn;
	private JButton addNetworkBtn, removeNetoworkBtn;
	private JButton addToFrstTblBtn, removeFromFrstTblBtn;
	private JButton addToScndTblBtn, removeFromScndTblBtn;
	
	private boolean makeAPoint = false;
	private TariffZonesController tariffZonesController;
	
	private ListSelectionListener listSelectionListener;
	private MouseListener mapViewerMouseListener;
	private MouseMotionListener mapViewerMouseMotionListener;
	private ActionListener actionListener;
	private TableListener firstTableListener;
	
	private static final String DEFAULT_INFO_MSG = "Use double click left mouse button for zoom in. Drag right mouse button to move.";
	private static final String MAKEPOINT_INFO_MSG = "Use left mouse button to point on the map.";
	
	public TariffZonesView() {
		tariffZonesController = new TariffZonesController();
		tariffZonesController.setView(this);
		
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Tariff Zones Problem Solver");
		initializeComponents();
		
		tariffZonesController.activate();
	}

	private void initializeComponents() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem fileMenuItem = new JMenuItem("File");
		JMenuItem importMenuItem = new JMenuItem("Import");
		JMenuItem helpMenuItem = new JMenuItem("Help");
		
//		importMenuItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//TODO:
//				tariffZonesController.readBusStops("d:\\Diplomová práca\\TariffZones\\DP data\\MHD ZA\\zastavky.txt");
//				
//			}
//		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		menuBar.add(fileMenuItem, gbc);
		menuBar.add(importMenuItem, gbc);
		menuBar.add(helpMenuItem, gbc);
		
		GridBagConstraints menuBarConstraints = new GridBagConstraints();
		menuBarConstraints.gridx = 0;
		menuBarConstraints.gridy = 0;
		menuBarConstraints.fill = GridBagConstraints.BOTH;
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
		infoPanel.add(getInfoLabel());
		GridBagConstraints infoPanelConstraints = new GridBagConstraints();
		infoPanelConstraints.gridx = 1;
		infoPanelConstraints.gridy = 6;
		infoPanelConstraints.gridwidth = 5;
		infoPanelConstraints.gridheight = 1;
		infoPanelConstraints.insets = new Insets(0, 1, 1, 1);
		infoPanelConstraints.fill = GridBagConstraints.BOTH;
		this.add(infoPanel, infoPanelConstraints);

		GridBagConstraints mapViewerConstraints = new GridBagConstraints();
		mapViewerConstraints.gridx = 2;
		mapViewerConstraints.gridy = 1;
		mapViewerConstraints.gridheight = 4;
		mapViewerConstraints.weightx = 0.7;
		mapViewerConstraints.weighty = 1;
		mapViewerConstraints.fill = GridBagConstraints.BOTH;
		this.add(getMapViewer(), mapViewerConstraints);
		
		this.validate();
		this.pack();
	}

	private void initializeTablePanel() {
		tablePanel = new JPanel();
		tablePanel.setBackground(Color.DARK_GRAY);
		tablePanel.setLayout(new GridBagLayout());

		GridBagConstraints stopComboBoxConstraints = new GridBagConstraints();
		stopComboBoxConstraints.gridx = 0;
		stopComboBoxConstraints.gridy = 1;
		stopComboBoxConstraints.gridwidth = 3;
		stopComboBoxConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getStopCb(), stopComboBoxConstraints);
		
		GridBagConstraints stopTableConstraints = new GridBagConstraints();
		stopTableConstraints.gridx = 0;
		stopTableConstraints.gridy = 2;
		stopTableConstraints.weighty = 0.5;
		stopTableConstraints.gridwidth = 3;
		stopTableConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(new JScrollPane(getStopTable()), stopTableConstraints);
		
		GridBagConstraints addToFrstTblBtnConstraints = new GridBagConstraints();
		addToFrstTblBtnConstraints.gridx = 0;
		addToFrstTblBtnConstraints.gridy = 3;
		addToFrstTblBtnConstraints.weightx = 0.5;
		addToFrstTblBtnConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getAddToFrstTblBtn(), addToFrstTblBtnConstraints);
		
		GridBagConstraints removeFromFrstTblBtnConstraints = new GridBagConstraints();
		removeFromFrstTblBtnConstraints.gridx = 1;
		removeFromFrstTblBtnConstraints.gridy = 3;
		removeFromFrstTblBtnConstraints.weightx = 0.5;
		removeFromFrstTblBtnConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getRemoveFromFrstTblBtn(), removeFromFrstTblBtnConstraints);
		
		GridBagConstraints wayComboBoxConstraints = new GridBagConstraints();
		wayComboBoxConstraints.gridx = 0;
		wayComboBoxConstraints.gridy = 4;
		wayComboBoxConstraints.gridwidth = 3;
		wayComboBoxConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getWayCb(), wayComboBoxConstraints);
		
		GridBagConstraints wayTableConstraints = new GridBagConstraints();
		wayTableConstraints.gridx = 0;
		wayTableConstraints.gridy = 5;
		wayTableConstraints.weighty = 0.5;
		wayTableConstraints.gridwidth = 3;
		wayTableConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(new JScrollPane(getWayTable()), wayTableConstraints);
		
		GridBagConstraints addToScndTblBtnConstraints = new GridBagConstraints();
		addToScndTblBtnConstraints.gridx = 0;
		addToScndTblBtnConstraints.gridy = 6;
		addToScndTblBtnConstraints.weightx = 0.5;
		addToScndTblBtnConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getAddToScndTblBtn(), addToScndTblBtnConstraints);
		
		GridBagConstraints removeFromScndTblBtnConstraints = new GridBagConstraints();
		removeFromScndTblBtnConstraints.gridx = 1;
		removeFromScndTblBtnConstraints.gridy = 6;
		removeFromScndTblBtnConstraints.weightx = 0.5;
		removeFromScndTblBtnConstraints.fill = GridBagConstraints.BOTH;
		tablePanel.add(getRemoveFromScndTblBtn(), removeFromScndTblBtnConstraints);
		
		GridBagConstraints tablePanelConstraints = new GridBagConstraints();
		tablePanelConstraints.gridx = 0;
		tablePanelConstraints.gridy = 4;
		tablePanelConstraints.weightx = 0.2;
		tablePanelConstraints.fill = GridBagConstraints.BOTH;
		tablePanelConstraints.insets = new Insets(0, 1, 1, 1);
		this.add(tablePanel, tablePanelConstraints);
	}

	private void initializeToolBoxPanel() {
		toolBoxPanel.setBackground(Color.LIGHT_GRAY);
		toolBoxPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints citiesComboBoxConstraints = new GridBagConstraints();
		citiesComboBoxConstraints.gridx = 0;
		citiesComboBoxConstraints.gridy = 0;
		citiesComboBoxConstraints.weightx = 0.7;
		citiesComboBoxConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(getCitiesCb(), citiesComboBoxConstraints);
		
		GridBagConstraints loadButtonConstraints = new GridBagConstraints();
		loadButtonConstraints.gridx = 1;
		loadButtonConstraints.gridy = 0;
		loadButtonConstraints.weightx = 0.3;
		loadButtonConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(getLoadBtn(), loadButtonConstraints);
		
		GridBagConstraints addNetworkBtnConstraints = new GridBagConstraints();
		addNetworkBtnConstraints.gridx = 0;
		addNetworkBtnConstraints.gridy = 1;
		addNetworkBtnConstraints.weightx = 0.5;
		addNetworkBtnConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(getAddNetworkBtn(), addNetworkBtnConstraints);
		
		GridBagConstraints removeNetworkBtnConstraints = new GridBagConstraints();
		removeNetworkBtnConstraints.gridx = 1;
		removeNetworkBtnConstraints.gridy = 1;
		removeNetworkBtnConstraints.weightx = 0.5;
		removeNetworkBtnConstraints.fill = GridBagConstraints.BOTH;
		toolBoxPanel.add(getRemoveNetworkBtn(), removeNetworkBtnConstraints);
		
//		JLabel toolBoxPanelNameLabel = new JLabel("ToolBox");
//		GridBagConstraints toolBoxPanelNameLabelConstraints = new GridBagConstraints();
//		toolBoxPanelNameLabelConstraints.gridx = 0;
//		toolBoxPanelNameLabelConstraints.gridy = 1;
//		toolBoxPanelNameLabelConstraints.gridwidth = 2;
//		toolBoxPanelNameLabelConstraints.gridheight = 1;
//		toolBoxPanelNameLabelConstraints.insets = new Insets(4, 4, 4, 4);
//		//toolBoxPanelNameLabelConstraints.weighty = 0.5;
//		//toolBoxPanelNameLabelConstraints.anchor = GridBagConstraints.PAGE_START;
//		toolBoxPanel.add(toolBoxPanelNameLabel, toolBoxPanelNameLabelConstraints);
		
//		JLabel circleIcon = new JLabel();
//		circleIcon.setIcon(new ImageIcon("orange-circle-png-3.png"));
//		GridBagConstraints circleIconConstraints = new GridBagConstraints();
//		circleIconConstraints.gridx = 0;
//		circleIconConstraints.gridy = 1;
//		circleIconConstraints.gridwidth = 1;
//		circleIconConstraints.gridheight = 1;
//		toolBoxPanel.add(circleIcon, circleIconConstraints);
		
//		JButton mapMakerDotBtn = new JButton("Make a point");
//		//mapMakerDotBtn.setIcon(new ImageIcon("orange-circle-png-3.png"));
//		mapMakerDotBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				getInfoLabel().setText(MAKEPOINT_INFO_MSG);
//				makeAPointButtonPressed = true;
//			}
//		});
//		GridBagConstraints mapMakerDotBtnConstraints = new GridBagConstraints();
//		mapMakerDotBtnConstraints.gridx = 0;
//		mapMakerDotBtnConstraints.gridy = 2;
//		mapMakerDotBtnConstraints.gridwidth = 2;
//		mapMakerDotBtnConstraints.gridheight = 1;
//		mapMakerDotBtnConstraints.weightx = 1;
//		mapMakerDotBtnConstraints.fill = GridBagConstraints.BOTH;
//		toolBoxPanel.add(mapMakerDotBtn, mapMakerDotBtnConstraints);
	}

	public void clearMapViewer() {
		getMapViewer().getMapMarkerList().clear();
		getMapViewer().getMapPolygonList().clear();
	}
	
	private JButton getAddNetworkBtn() {
		if (addNetworkBtn == null) {
			addNetworkBtn = new JButton("Add new");
		}
		return addNetworkBtn;
	}
	
	private JButton getRemoveNetworkBtn() {
		if (removeNetoworkBtn == null) {
			removeNetoworkBtn = new JButton("Delete");
		}
		return removeNetoworkBtn;
	}
	
	private JButton getAddToFrstTblBtn() {
		if (addToFrstTblBtn == null) {
			addToFrstTblBtn = new JButton("Add new");
			addToFrstTblBtn.setEnabled(false);
		}
		return addToFrstTblBtn;
	}
	
	private JButton getRemoveFromFrstTblBtn() {
		if (removeFromFrstTblBtn == null) {
			removeFromFrstTblBtn = new JButton("Delete");
			removeFromFrstTblBtn.setEnabled(false);
		}
		return removeFromFrstTblBtn;
	}
	
	private JButton getAddToScndTblBtn() {
		if (addToScndTblBtn == null) {
			addToScndTblBtn = new JButton("Add new");
			addToScndTblBtn.setEnabled(false);
		}
		return addToScndTblBtn;
	}
	
	private JButton getRemoveFromScndTblBtn() {
		if (removeFromScndTblBtn == null) {
			removeFromScndTblBtn = new JButton("Delete");
			removeFromScndTblBtn.setEnabled(false);
		}
		return removeFromScndTblBtn;
	}

	public JComboBox getWayCb() {
		if (wayCb == null) {
			wayCb = new JComboBox<String>();
			wayCb.addItem("Ways");
			wayCb.addItem("Bus stops");
		}
		return wayCb;
	}
	
	public JComboBox getStopCb() {
		if (stopCb == null) {
			stopCb = new JComboBox<String>();
			stopCb.addItem("Bus stops");
			stopCb.addItem("Ways");
		}
		return stopCb;
	}
	
	public JMapViewer getMapViewer() {
		if (mapViewer == null) {
			mapViewer = new JMapViewer();
			mapViewer.setLayout(new BorderLayout());
			mapViewer.add(getLatLonLabel(), BorderLayout.SOUTH);
		}
		return mapViewer;
	}
	
	private MouseMotionListener getMapViewerMouseMotionListener() {
		if (mapViewerMouseMotionListener == null) {
			mapViewerMouseMotionListener = new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					latLonLabel.setText("[" + Double.toString(mapViewer.getPosition(e.getX(), e.getY()).getLat()) + ", "
											+ Double.toString(mapViewer.getPosition(e.getX(), e.getY()).getLon()) + "]");
				}
			};
		}
		return mapViewerMouseMotionListener;
	}
	
	private MouseListener getMapViewerMouseListener() {
		if (mapViewerMouseListener == null) {
			mapViewerMouseListener = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					if (makeAPoint) {
						getController().addBusStop(e.getX(), e.getY());
						makeAPoint = false;
					}
				}
			};
		}
		return mapViewerMouseListener;
	}
	
	private ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//LoadBtn
					if (e.getSource().equals(getLoadBtn())) {
						clearMapViewer();
						Object selectedItem = getCitiesCb().getSelectedItem();
						if (selectedItem != null) {
							getController().addStopsInNetworkToMap(selectedItem.toString());
							updateBtns();
						}
					}
					//first combobox
					else if (e.getSource().equals(getStopCb())) {
						if (getStopCb().getSelectedItem().toString().equals("Bus stops")) {
							getController().fillTableWithStops(getStopTable(), getController().getLoadedNetworkName()); //TODO: not to load it from db again - maybe its not that bad		
						}
						else {
							getController().fillTableWithWays(getStopTable(), getController().getLoadedNetworkName());
						}
					}
					//second combobox
					else if (e.getSource().equals(getWayCb())) {
						if (getWayCb().getSelectedItem().toString().equals("Bus stops")) {
							getController().fillTableWithStops(getWayTable(), getController().getLoadedNetworkName());
						}
						else {
							getController().fillTableWithWays(getWayTable(), getController().getLoadedNetworkName());
						}
					}
					//getAddToFrstTblBtn
					else if (e.getSource().equals(getAddToFrstTblBtn())) {
						if (getStopCb().getSelectedItem().toString().equals("Bus stops")) {
							getInfoLabel().setText(MAKEPOINT_INFO_MSG);
							makeAPoint = true;
						}
						else {
							//TODO: add way
						}
					}
					//getRemoveFromFrstTblBtn
					else if (e.getSource().equals(getRemoveFromFrstTblBtn())) {
						if (getStopCb().getSelectedItem().toString().equals("Bus stops")) {
							int stopNumber = (int) getStopTable().getValueAt(getStopTable().getSelectedRow(), 0);
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete this stop?", "Stop delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteBusStop(getController().getLoadedNetworkName(), stopNumber);
							}
						}
						else {
							//TODO: delete way
						}
					}
					//getAddToScndTblBtn
					else if (e.getSource().equals(getAddToScndTblBtn())) {
						if (getWayCb().getSelectedItem().toString().equals("Bus stops")) {
							getInfoLabel().setText(MAKEPOINT_INFO_MSG);
							makeAPoint = true;
						}
						else {
							//TODO: add way
						}
					}
					//getRemoveFromScndTblBtn
					else if (e.getSource().equals(getRemoveFromScndTblBtn())) {
						if (getWayCb().getSelectedItem().toString().equals("Bus stops")) {
							//TODO: delete bus stop
						}
						else {
							//TODO: delete way
						}
					}
					else if (e.getSource().equals(getAddNetworkBtn())) {
						getController().addNetwork();
					}
					else if (e.getSource().equals(getRemoveNetworkBtn())) {
						if (getCitiesCb().getSelectedItem() != null) {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete this network?", "Network delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteNetwork(getCitiesCb().getSelectedItem().toString());
							}
						}
					}
				}
			};
		}
		
		return actionListener;
	}
	
	private void updateBtns() {
		if (getController().getLoadedNetworkName() != null) {
			getAddToFrstTblBtn().setEnabled(true);
			getAddToScndTblBtn().setEnabled(true);
			getRemoveFromFrstTblBtn().setEnabled(true);
			getRemoveFromScndTblBtn().setEnabled(true);
		}
	}
	
	public TableListener getFirstTableListener() {
		if (firstTableListener == null) {
			firstTableListener = new TableListener() {
				
				@Override
				public void rowSelected(TableEvent e) {
					System.out.println("row selected");
					
				}
			};
		}
		return firstTableListener;
	}
	
	public ListSelectionListener getListSelectionListener() {
		if (listSelectionListener == null) {
			listSelectionListener = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getSource().equals(getStopTable().getSelectionModel())) {
						ArrayList<Integer> stopNumbers = new ArrayList<>();
						for (int i = 0; i < getStopTable().getSelectedRows().length; i++) {
							stopNumbers.add((Integer) getStopTable().getValueAt(getStopTable().getSelectedRows()[i], 0));
						}
						getController().highligthBusStop(stopNumbers);
					}
				}
			};
		}
		return listSelectionListener;
	}
	
	public void registryListeners() {
		getMapViewer().addMouseMotionListener(getMapViewerMouseMotionListener());
		getMapViewer().addMouseListener(getMapViewerMouseListener());
		getLoadBtn().addActionListener(getActionListener());
		getStopCb().addActionListener(getActionListener());
		getWayCb().addActionListener(getActionListener());
		getAddToFrstTblBtn().addActionListener(getActionListener());
		getRemoveFromFrstTblBtn().addActionListener(getActionListener());
		getAddNetworkBtn().addActionListener(getActionListener());
		getRemoveNetworkBtn().addActionListener(getActionListener());
		getStopTable().getSelectionModel().addListSelectionListener(getListSelectionListener());
	}
	
	public void unregistryListeners() {
		getMapViewer().removeMouseMotionListener(getMapViewerMouseMotionListener());
		getMapViewer().removeMouseListener(getMapViewerMouseListener());
		getLoadBtn().removeActionListener(getActionListener());
		getStopCb().removeActionListener(getActionListener());
		getWayCb().removeActionListener(getActionListener());
		getAddToFrstTblBtn().removeActionListener(getActionListener());
		getRemoveFromFrstTblBtn().removeActionListener(getActionListener());
		getAddNetworkBtn().removeActionListener(getActionListener());
		getRemoveNetworkBtn().removeActionListener(getActionListener());
		getStopTable().getSelectionModel().removeListSelectionListener(getListSelectionListener());
	}

	private JButton getLoadBtn() {
		if (loadBtn == null) {
			loadBtn = new JButton("Load");
		}
		return loadBtn;
	}

	public JLabel getInfoLabel() {
		if (infoLabel == null) {
			infoLabel = new JLabel(DEFAULT_INFO_MSG);
		}
		return infoLabel;
	}
	
	public JLabel getLatLonLabel() {
		if (latLonLabel == null) {
			latLonLabel = new JLabel("");
			latLonLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return latLonLabel;
	}
	
	public JTable getStopTable() {
		if (stopTable == null) {
			stopTable = new JTable();
		}
		return stopTable;
	}
	
	public JTable getWayTable() {
		if (wayTable == null) {
			wayTable = new JTable();
		}
		return wayTable;
	}
	
	public JComboBox getCitiesCb() {
		if (citiesCb == null) {
			citiesCb = new JComboBox<>();
		}
		return citiesCb;
	}
	
	public TariffZonesController getController() {
		if (tariffZonesController == null) {
			tariffZonesController = new TariffZonesController();
		}
		return tariffZonesController;
	}
	
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TariffZonesView().setVisible(true);
            }
        });
	}
}