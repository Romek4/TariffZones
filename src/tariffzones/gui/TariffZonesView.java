package tariffzones.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultRowSorter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.AbstractTileFactory;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import tariffzones.controller.TariffZonesController;
import tariffzones.map.MapViewer;
import tariffzones.model.Network;
import tariffzones.model.StopTableModel;
import tariffzones.model.WayTableModel;

public class TariffZonesView extends JFrame {
	
	private JPanel toolBoxPanel, tablePanel;
	private JLabel infoLabel;
	
	private JXMapViewer mapViewer;
	private JTable stopTable, wayTable;
	private JComboBox citiesCb;
	private JComboBox stopCb;
	private JComboBox wayCb;
	
	private JComboBox tileServersCb;
	
	private ButtonPl buttonPl;
	
	private JButton loadBtn;
	private JButton connectToDBBtn;
	private JButton openNetworkFromFilesBtn;
	private JButton addNetworkBtn, removeNetoworkBtn;
	private JButton addStopBtn, removeFromFrstTblBtn;
	private JButton addToScndTblBtn, removeFromScndTblBtn;
	
	private JMenuItem stopEditMenuItem;  
	private JMenuItem stopDeleteMenuItem;
	
	private JMenuItem wayEditMenuItem;  
	private JMenuItem wayDeleteMenuItem;
	
	private JMenuItem deleteFromStopTableMenuItem;
	private JMenuItem deleteFromWayTableMenuItem;
	
	private JMenuItem exportStopsToCSVMenuItem;
	private JMenuItem exportWaysToCSVMenuItem;
	
	private JPopupMenu stopMapPopupMenu;
	private JPopupMenu wayMapPopupMenu;
	private JPopupMenu stopTablePopupMenu;
	private JPopupMenu wayTablePopupMenu;
	
	private MapToolboxPl mapToolboxPl;
	
	private TableRowSorter<TableModel> sorter;
	
	private JTextField stopFilterTf;
	private JTextField wayFilterTf;
	
	private boolean makeAPoint = false;
	private boolean makeAWay = false;
	private Point startPoint = null;
	private Point endPoint = null;
	private TariffZonesController tariffZonesController;
	
	private ListSelectionListener listSelectionListener;
	private MouseListener mapViewerMouseListener;
	private MouseMotionListener mapViewerMouseMotionListener;
	private ActionListener actionListener;
	
	private static final String DEFAULT_INFO_MSG = "Use wheel for zoom. Drag left mouse button to move.";
	private static final String MAKEPOINT_INFO_MSG = "Use left mouse button to point on the map.";
	private static final String MAKEWAY_INFO_MSG = "Use left mouse button to select two stops the new way be between.";
	
	public TariffZonesView() {
		tariffZonesController = new TariffZonesController();
		tariffZonesController.setView(this);
		
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Tariff Zones Problem Solver");
		
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		
		initializeComponents();
		tariffZonesController.activate();
		
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					makeAPoint = false;
					makeAWay = false;
					startPoint = null;
					endPoint = null;
					getInfoLabel().setText(DEFAULT_INFO_MSG);
				}
			}
		});
	}

	private void initializeComponents() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu exportMenu = new JMenu("Export");
		
		exportMenu.add(getExportStopsToCSVMenuItem());
		exportMenu.add(getExportWaysToCSVMenuItem());
		
		fileMenu.add(exportMenu);
		menuBar.add(fileMenu);
		
//		JMenuItem importMenuItem = new JMenuItem("Import");
//		JMenuItem helpMenuItem = new JMenuItem("Help");
		
//		importMenuItem.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//TODO:
//				tariffZonesController.readBusStops("d:\\Diplomov� pr�ca\\TariffZones\\DP data\\MHD ZA\\zastavky.txt");
//				
//			}
//		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.weighty = 0.03;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
//		menuBar.add(fileMenuItem, gbc);
//		menuBar.add(importMenuItem, gbc);
//		menuBar.add(helpMenuItem, gbc);
//		
//		GridBagConstraints menuBarConstraints = new GridBagConstraints();
//		menuBarConstraints.gridx = 0;
//		menuBarConstraints.gridy = 0;
//		menuBarConstraints.gridheight = 1;
//		menuBarConstraints.fill = GridBagConstraints.BOTH;
//		this.add(menuBar, menuBarConstraints);
		
		this.add(menuBar, gbc);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints leftPanelgbc = new GridBagConstraints();
		leftPanelgbc.gridx = 0;
		leftPanelgbc.gridy = 1;
//		leftPanelgbc.gridwidth = 1;
//		leftPanelgbc.gridheight = 5;
		leftPanelgbc.weightx = 0.1;
		leftPanelgbc.weighty = 1;
		leftPanelgbc.fill = GridBagConstraints.BOTH;		
		
		//ToolBoxPanel - upperleft
		GridBagConstraints toolBoxPanelConstraints = new GridBagConstraints();
		toolBoxPanelConstraints.gridx = 0;
		toolBoxPanelConstraints.gridy = 0;
		toolBoxPanelConstraints.weightx = 1;
		toolBoxPanelConstraints.weighty = 0.05;
		toolBoxPanelConstraints.fill = GridBagConstraints.BOTH;
		leftPanel.add(getToolBoxPanel(), toolBoxPanelConstraints);
		
		//TablePanel - lowerleft
		GridBagConstraints tablePanelConstraints = new GridBagConstraints();
		tablePanelConstraints.gridx = 0;
		tablePanelConstraints.gridy = 1;
		tablePanelConstraints.weightx = 1;
		tablePanelConstraints.weighty = 0.95;
		tablePanelConstraints.fill = GridBagConstraints.BOTH;
		leftPanel.add(getTablePanel(), tablePanelConstraints);
		
		this.add(leftPanel, leftPanelgbc);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints rightPanelgbc = new GridBagConstraints();
		rightPanelgbc.gridx = 2;
		rightPanelgbc.gridy = 1;
		rightPanelgbc.weightx = 0.9;
		rightPanelgbc.weighty = 1;
		rightPanelgbc.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints mapViewerConstraints = new GridBagConstraints();
		mapViewerConstraints.gridx = 0;
		mapViewerConstraints.gridy = 0;
		mapViewerConstraints.weighty = 0.9;
		mapViewerConstraints.weightx = 0.9;
		mapViewerConstraints.fill = GridBagConstraints.BOTH;
		rightPanel.add(getMapViewer(), mapViewerConstraints);
	
		getMapToolboxPl().setBounds(10, 10, 250, 50);
		getMapViewer().add(getMapToolboxPl());
		
		getTileServersCb().setBounds(300, 10, 200, 50);
		getMapViewer().add(getTileServersCb());
		
		//InfoPanel - bottom
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.LIGHT_GRAY);
		Dimension dimension = new Dimension(100, 25);
		infoPanel.setMinimumSize(dimension);
		infoPanel.setMaximumSize(dimension);
		infoPanel.setPreferredSize(dimension);
		infoPanel.add(getInfoLabel());
		GridBagConstraints infoPanelConstraints = new GridBagConstraints();
		infoPanelConstraints.gridx = 0;
		infoPanelConstraints.gridy = 1;
//		infoPanelConstraints.gridwidth = 4;
//		infoPanelConstraints.gridheight = 1;
		infoPanelConstraints.fill = GridBagConstraints.BOTH;
		rightPanel.add(infoPanel, infoPanelConstraints);
		
		this.add(rightPanel, rightPanelgbc);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, rightPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.05);
		GridBagConstraints splitPaneGbc = new GridBagConstraints();
		splitPaneGbc.gridx = 1;
		splitPaneGbc.gridy = 1;
		splitPaneGbc.weightx = 1;
		splitPaneGbc.weighty = 1;
		splitPaneGbc.fill = GridBagConstraints.BOTH;
		this.add(splitPane, splitPaneGbc);
		
		this.validate();
		this.pack();
	}

	private JPanel getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());
	
			JPanel upperTablePanel = new JPanel(new GridBagLayout());
			
			GridBagConstraints stopComboBoxConstraints = new GridBagConstraints();
			stopComboBoxConstraints.gridx = 0;
			stopComboBoxConstraints.gridy = 0;
			stopComboBoxConstraints.weightx = 0.5;
			stopComboBoxConstraints.fill = GridBagConstraints.BOTH;
			upperTablePanel.add(getStopCb(), stopComboBoxConstraints);
			
			JLabel stopFilterLb = new JLabel("Filter: ");
			stopFilterLb.setHorizontalAlignment(JLabel.CENTER);
//			filterLb.setSize(new Dimension(16, 16));
//			Image img;
//			try {
//				img = ImageIO.read(new FileInputStream("resources/images/filterIcon.png"));
//				filterLb.setIcon(new ImageIcon(img.getScaledInstance(filterLb.getWidth(), filterLb.getHeight(), Image.SCALE_SMOOTH)));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
			GridBagConstraints stopFilterLbConstraints = new GridBagConstraints();
			stopFilterLbConstraints.gridx = 1;
			stopFilterLbConstraints.gridy = 0;
			stopFilterLbConstraints.weightx = 0.05;
			stopFilterLbConstraints.fill = GridBagConstraints.BOTH;
			upperTablePanel.add(stopFilterLb, stopFilterLbConstraints);
			
			GridBagConstraints stopFilterTfConstraints = new GridBagConstraints();
			stopFilterTfConstraints.gridx = 2;
			stopFilterTfConstraints.gridy = 0;
			stopFilterTfConstraints.weightx = 0.5;
			stopFilterTfConstraints.fill = GridBagConstraints.BOTH;
			upperTablePanel.add(getStopFilterTf(), stopFilterTfConstraints);
			
			GridBagConstraints stopTableConstraints = new GridBagConstraints();
			stopTableConstraints.gridx = 0;
			stopTableConstraints.gridy = 1;
			stopTableConstraints.gridwidth = 3;
			stopTableConstraints.weightx = 1;
			stopTableConstraints.weighty = 0.5;
			stopTableConstraints.fill = GridBagConstraints.BOTH;
			upperTablePanel.add(new JScrollPane(getStopTable()), stopTableConstraints);
			
			JPanel lowerTablePanel = new JPanel(new GridBagLayout());
			
			GridBagConstraints wayComboBoxConstraints = new GridBagConstraints();
			wayComboBoxConstraints.gridx = 0;
			wayComboBoxConstraints.gridy = 0;
			wayComboBoxConstraints.weightx = 1;
			wayComboBoxConstraints.fill = GridBagConstraints.BOTH;
			lowerTablePanel.add(getWayCb(), wayComboBoxConstraints);
			
			JLabel wayFilterLb = new JLabel("Filter: ");
			wayFilterLb.setHorizontalAlignment(JLabel.CENTER);
			
			GridBagConstraints wayFilterLbConstraints = new GridBagConstraints();
			wayFilterLbConstraints.gridx = 1;
			wayFilterLbConstraints.gridy = 0;
			wayFilterLbConstraints.weightx = 0.05;
			wayFilterLbConstraints.fill = GridBagConstraints.BOTH;
			lowerTablePanel.add(wayFilterLb, wayFilterLbConstraints);
			
			GridBagConstraints wayFilterTfConstraints = new GridBagConstraints();
			wayFilterTfConstraints.gridx = 2;
			wayFilterTfConstraints.gridy = 0;
			wayFilterTfConstraints.weightx = 0.5;
			wayFilterTfConstraints.fill = GridBagConstraints.BOTH;
			lowerTablePanel.add(getWayFilterTf(), wayFilterTfConstraints);
			
			GridBagConstraints wayTableConstraints = new GridBagConstraints();
			wayTableConstraints.gridx = 0;
			wayTableConstraints.gridy = 2;
			wayTableConstraints.gridwidth = 3;
			wayTableConstraints.weightx = 1;
			wayTableConstraints.weighty = 0.5;
			wayTableConstraints.fill = GridBagConstraints.BOTH;
			lowerTablePanel.add(new JScrollPane(getWayTable()), wayTableConstraints);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperTablePanel, lowerTablePanel);
			splitPane.setOneTouchExpandable(true);
			splitPane.setResizeWeight(0.5);
			
			GridBagConstraints splitPaneGbc = new GridBagConstraints();
			splitPaneGbc.gridx = 0;
			splitPaneGbc.gridy = 1;
			splitPaneGbc.weightx = 1;
			splitPaneGbc.weighty = 1;
			splitPaneGbc.fill = GridBagConstraints.BOTH;
			tablePanel.add(splitPane, splitPaneGbc);
		}
		return tablePanel;
	}

	private JPanel getToolBoxPanel() {
		if (toolBoxPanel == null) {
			toolBoxPanel = new JPanel();
			toolBoxPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints citiesComboBoxConstraints = new GridBagConstraints();
			citiesComboBoxConstraints.gridx = 0;
			citiesComboBoxConstraints.gridy = 0;
			citiesComboBoxConstraints.weightx = 0.5;
			citiesComboBoxConstraints.weighty = 0.3;
			citiesComboBoxConstraints.fill = GridBagConstraints.BOTH;
			toolBoxPanel.add(getCitiesCb(), citiesComboBoxConstraints);
			
			GridBagConstraints connectToDBButtonConstraints = new GridBagConstraints();
			connectToDBButtonConstraints.gridx = 1;
			connectToDBButtonConstraints.gridy = 0;
			connectToDBButtonConstraints.weightx = 0.5;
			connectToDBButtonConstraints.weighty = 0.3;
			connectToDBButtonConstraints.fill = GridBagConstraints.BOTH;
			toolBoxPanel.add(getConnectToDBBtn(), connectToDBButtonConstraints);
			
			GridBagConstraints loadButtonConstraints = new GridBagConstraints();
			loadButtonConstraints.gridx = 0;
			loadButtonConstraints.gridy = 1;
			loadButtonConstraints.gridwidth = 2;
			loadButtonConstraints.weightx = 0.5;
			loadButtonConstraints.weighty = 0.4;
			loadButtonConstraints.fill = GridBagConstraints.BOTH;
			toolBoxPanel.add(getLoadBtn(), loadButtonConstraints);
			
			GridBagConstraints removeNetworkBtnConstraints = new GridBagConstraints();
			removeNetworkBtnConstraints.gridx = 0;
			removeNetworkBtnConstraints.gridy = 2;
			removeNetworkBtnConstraints.gridwidth = 2;
			removeNetworkBtnConstraints.weightx = 0.5;
			removeNetworkBtnConstraints.weighty = 0.3;
			removeNetworkBtnConstraints.fill = GridBagConstraints.BOTH;
			toolBoxPanel.add(getRemoveNetworkBtn(), removeNetworkBtnConstraints);
			
			GridBagConstraints addNetworkBtnConstraints = new GridBagConstraints();
			addNetworkBtnConstraints.gridx = 2;
			addNetworkBtnConstraints.gridy = 0;
			addNetworkBtnConstraints.gridheight = 3;
			addNetworkBtnConstraints.weightx = 0.5;
			addNetworkBtnConstraints.weighty = 1;
			addNetworkBtnConstraints.fill = GridBagConstraints.BOTH;
			toolBoxPanel.add(getOpenNetworkFromFilesBtn(), addNetworkBtnConstraints);
		}
		return toolBoxPanel;
	}

	public void clearMapViewer() {
//		getMapViewer().getMapMarkerList().clear();
//		getMapViewer().getMapPolygonList().clear();
	}
	
	private JButton getOpenNetworkFromFilesBtn() {
		if (openNetworkFromFilesBtn == null) {
			openNetworkFromFilesBtn = new JButton();
			openNetworkFromFilesBtn.setSize(new Dimension(48, 48));
			openNetworkFromFilesBtn.setContentAreaFilled(true);
			try {
				Image img = ImageIO.read(new FileInputStream("resources/images/openIcon.png"));
				openNetworkFromFilesBtn.setIcon(new ImageIcon(img.getScaledInstance(openNetworkFromFilesBtn.getWidth(), openNetworkFromFilesBtn.getHeight(), Image.SCALE_SMOOTH)));
				openNetworkFromFilesBtn.setToolTipText("Read network from files");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return openNetworkFromFilesBtn;
	}
	
	private JButton getRemoveNetworkBtn() {
		if (removeNetoworkBtn == null) {
			removeNetoworkBtn = new JButton();
			removeNetoworkBtn.setSize(new Dimension(24, 24));
			removeNetoworkBtn.setContentAreaFilled(true);
			try {
				Image img = ImageIO.read(new FileInputStream("resources/images/removeIcon.png"));
				removeNetoworkBtn.setIcon(new ImageIcon(img.getScaledInstance(removeNetoworkBtn.getWidth(), removeNetoworkBtn.getHeight(), Image.SCALE_SMOOTH)));
				removeNetoworkBtn.setToolTipText("Delete network");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return removeNetoworkBtn;
	}
	
	public JButton getConnectToDBBtn() {
		if (connectToDBBtn == null) {
			try {
				connectToDBBtn = new JButton();
				connectToDBBtn.setSize(new Dimension(24, 24));
				connectToDBBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(new FileInputStream("resources/images/databaseConnectionIcon.png"));
				connectToDBBtn.setIcon(new ImageIcon(img.getScaledInstance(connectToDBBtn.getWidth(), connectToDBBtn.getHeight(), Image.SCALE_SMOOTH)));
				connectToDBBtn.setToolTipText("Connect to database");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return connectToDBBtn;
	}
	
	private JButton getSolveBtn() {
		return getMapToolboxPl().getSolveBtn();
	}
	
	private JButton getAddStopBtn() {
		return getMapToolboxPl().getPointBtn();
	}
	
	private JButton getAddWayBtn() {
		return getMapToolboxPl().getWayBtn();
	}
	
	private JButton getSaveBtn() {
		return getMapToolboxPl().getSaveBtn();
	}

	public JComboBox getWayCb() {
		if (wayCb == null) {
			wayCb = new JComboBox<String>();
			wayCb.addItem("Ways");
			wayCb.addItem("Stops");
		}
		return wayCb;
	}
	
	public JComboBox getStopCb() {
		if (stopCb == null) {
			stopCb = new JComboBox<String>();
			stopCb.addItem("Stops");
			stopCb.addItem("Ways");
		}
		return stopCb;
	}
	
	public JComboBox getTileServersCb() {
		if (tileServersCb == null) {
			tileServersCb = new JComboBox();
			tileServersCb.setFont(new Font(tileServersCb.getFont().getName(), Font.BOLD, 14));
	        tileServersCb.setMaximumRowCount(3);
		}
		return tileServersCb;
	}
	
	public MapToolboxPl getMapToolboxPl() {
		if (mapToolboxPl == null) {
			mapToolboxPl = new MapToolboxPl();
		}
		return mapToolboxPl;
	}
	
	public JXMapViewer getMapViewer() {
		if (mapViewer == null) {
			mapViewer = new MapViewer();

			// Create a TileFactoryInfo for OpenStreetMap
			TileFactoryInfo info = new OSMTileFactoryInfo();
			DefaultTileFactory tileFactory = new DefaultTileFactory(info);
			mapViewer.setTileFactory(tileFactory);
			
			// Use 8 threads in parallel to load the tiles
			tileFactory.setThreadPoolSize(8);

			// Set the focus
			GeoPosition slovakia = new GeoPosition(48.8, 19.2);

			mapViewer.setZoom(15);
			mapViewer.setAddressLocation(slovakia);
		}
		return mapViewer;
	}
	
	private MouseListener getMouseListener() {
		if (mapViewerMouseListener == null) {
			mapViewerMouseListener = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getSource().equals(getMapViewer())) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							if (makeAPoint) {
								getController().addBusStop(e.getPoint());
								makeAPoint = false;
							}
							else if (makeAWay) {
								//pick first stop
								if (startPoint == null) {
									getInfoLabel().setText("Click on begining stop of new way");
									if (getController().checkMousePositionForStop(e.getPoint()) != null) {
										startPoint = e.getPoint();
										getInfoLabel().setText("Pick second stop");
									}
									return;
								}
								//pick second stop
								if (endPoint == null) {
									getInfoLabel().setText("Click on ending stop of new way");
									if (getController().checkMousePositionForStop(e.getPoint()) != null) {
										endPoint = e.getPoint();
										getInfoLabel().setText(DEFAULT_INFO_MSG);
									}
								}
								
								if (startPoint != null && endPoint != null) {
									getController().addWay(startPoint, endPoint);
								}
								else { return; }
								
								startPoint = null;
								endPoint = null;
								makeAWay = false;
								
							}
							
							//TODO: witch table?
							int stopIndex = getController().getClickedStopIndex(e.getPoint());
							int wayIndex = getController().getClickedWayIndex(e.getPoint());
							if (stopIndex >= 0) {
								if (getStopCb().getSelectedItem().toString().equals("Stops")) {
									int tableIndex = getStopTable().convertRowIndexToView(stopIndex);
									getStopTable().setRowSelectionInterval(tableIndex, tableIndex);
								}
								else {
									//second table will be Stops
									int tableIndex = getWayTable().convertRowIndexToView(stopIndex);
									getWayTable().setRowSelectionInterval(tableIndex, tableIndex);
								}
							}
							else if (wayIndex >= 0) {
								if (getStopCb().getSelectedItem().toString().equals("Ways")) {
									int tableIndex = getStopTable().convertRowIndexToView(wayIndex);
									getStopTable().setRowSelectionInterval(tableIndex, tableIndex);
								}
								else {
									//second table will be Ways
									int tableIndex = getWayTable().convertRowIndexToView(wayIndex);
									getWayTable().setRowSelectionInterval(tableIndex, tableIndex);
								}
							}
							else {
								getStopTable().clearSelection();
								getWayTable().clearSelection();
								getController().resetSelected();
							}
						}
						else if (SwingUtilities.isRightMouseButton(e)) {
							if (!getController().checkForStopAndShowPopup(e.getPoint())) {
								getController().checkForWayAndShowPopup(e.getPoint());
							}
						}
					}
					else if (e.getSource().equals(getStopTable())) {
						if (SwingUtilities.isRightMouseButton(e)) {
							getStopTablePopupMenu().show(getStopTable(), e.getX(), e.getY());
						}
					}
					else if (e.getSource().equals(getWayTable())) {
						if (SwingUtilities.isRightMouseButton(e)) {
							getWayTablePopupMenu().show(getWayTable(), e.getX(), e.getY());
						}
					}
				}
			};
		}
		return mapViewerMouseListener;
	}
	
	private MouseMotionListener getMapViewerMousemotionListener() {
		if (mapViewerMouseMotionListener == null) {
			mapViewerMouseMotionListener = new MouseAdapter() {
				
				@Override
				public void mouseMoved(MouseEvent e) {
					if (!getController().checkForStopAndShowTooltip(e.getPoint())) {
						getController().checkForWayAndShowTooltip(e.getPoint());
					}
				}
			};
		}
		return mapViewerMouseMotionListener;
	}
	
	private ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//getConnectToDBBtn
					if (e.getSource().equals(getConnectToDBBtn())) {
						if (getController().connectToDB()) {
							List networks = getController().getNetworks();
							if (networks != null) {
								getCitiesCb().setModel((new DefaultComboBoxModel(networks.toArray())));
							}
						}
					}
					//getLoadBtn
					else if (e.getSource().equals(getLoadBtn())) {
						if (getController().isUnsavedChanges()) {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "There are some unsaved changes! Do you want to save changes before reloading of network?", "Stop delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								if (getController().saveChangesToDatabase()) {
									getController().clearUnsaved();
									getController().updateBtns();
									JOptionPane.showMessageDialog(getContentPane(), "Data saved.", "Save Data To Database", JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
						
						clearMapViewer();
						Object selectedItem = getCitiesCb().getSelectedItem();
						if (selectedItem != null) {
							getController().addStopsInNetworkToMap(((Network)selectedItem).getNetworkName());
							getController().addWaysBetweenStopsInCityToMap(((Network)selectedItem).getNetworkName());
						}
					}
					//first combobox
					else if (e.getSource().equals(getStopCb())) {
						if (getStopCb().getSelectedItem().toString().equals("Stops")) {
							getController().fillTableWithStops(getStopTable());
							getController().resetSelected();
							if (getWayCb().getSelectedItem().toString().equals("Stops")) {
								getStopTable().setModel(getWayTable().getModel());
								getWayCb().setSelectedItem("Ways");
							}
						}
						else {
							getController().fillTableWithWays(getStopTable());
							getController().resetSelected();
							if (getWayCb().getSelectedItem().toString().equals("Ways")) {
								getWayCb().setSelectedItem("Stops");
							}
						}
					}
					//second combobox
					else if (e.getSource().equals(getWayCb())) {
						if (getWayCb().getSelectedItem().toString().equals("Stops")) {
							getController().fillTableWithStops(getWayTable());
							getController().resetSelected();
							if (getStopCb().getSelectedItem().toString().equals("Stops")) {
								getStopCb().setSelectedItem("Ways");
							}
						}
						else {
							getController().fillTableWithWays(getWayTable());
							getController().resetSelected();
							if (getStopCb().getSelectedItem().toString().equals("Ways")) {
								getStopCb().setSelectedItem("Stops");
							}
						}
					}
					else if (e.getSource().equals(getStopFilterTf())) {
						sorter = new TableRowSorter<TableModel>(getStopTable().getModel());
						getStopTable().setRowSorter(sorter);
						sorter.setRowFilter(RowFilter.regexFilter(getStopFilterTf().getText()));
					}
					else if (e.getSource().equals(getWayFilterTf())) {
						sorter = new TableRowSorter<TableModel>(getWayTable().getModel());
						getWayTable().setRowSorter(sorter);
						sorter.setRowFilter(RowFilter.regexFilter(getWayFilterTf().getText()));
					}
					else if (e.getSource().equals(getTileServersCb())) {
						TileFactory tileFactory = (TileFactory) getTileServersCb().getSelectedItem();
						((AbstractTileFactory) tileFactory).setThreadPoolSize(8);
						getMapViewer().setTileFactory(tileFactory);
						
					}
					else if(e.getSource().equals(getSolveBtn())) {
						getController().solveTariffZonesProblem();
					}
					//getAddStopBtn
					else if (e.getSource().equals(getAddStopBtn())) {
							getInfoLabel().setText(MAKEPOINT_INFO_MSG);
							makeAPoint = true;
					}
					//getAddWayBtn
					else if (e.getSource().equals(getAddWayBtn())) {
							getInfoLabel().setText(MAKEWAY_INFO_MSG);
							makeAWay = true;
					}
					else if(e.getSource().equals(getSaveBtn())) {
						if (getController().saveChangesToDatabase()) {
							getController().clearUnsaved();
							getController().updateBtns();
							JOptionPane.showMessageDialog(getContentPane(), "Data saved.", "Save Data To Database", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if (e.getSource().equals(getOpenNetworkFromFilesBtn())) {
						try {
							getController().openNetworkFromFiles();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(getRootPane(), e1.toString(), "Read network from files", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if (e.getSource().equals(getRemoveNetworkBtn())) {
						if (getCitiesCb().getSelectedItem() != null) {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete this network?", "Network Delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteNetwork(getCitiesCb().getSelectedItem().toString());
							}
						}
					}
					else if (e.getSource().equals(getExportStopsToCSVMenuItem())) {
						getController().exportStops();
					}
					else if (e.getSource().equals(getExportWaysToCSVMenuItem())) {
						getController().exportWays();
					}
					else if (e.getSource().equals(getStopEditMenuItem())) {
						getController().editStop(getController().getLastPickedStop());
					}
					else if (e.getSource().equals(getStopDeleteMenuItem())) {
						int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete this stop?", "Stop Delete", JOptionPane.YES_NO_OPTION);
						if (check == 0) {
							getController().deleteStop(getController().getLastPickedStop());
							getController().resetLastPickedStop();
						}
					}
					else if (e.getSource().equals(getWayEditMenuItem())) {
						getController().editWay(getController().getLastPickedWay());
					}
					else if (e.getSource().equals(getWayDeleteMenuItem())) {
						int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete this way?", "Way Delete", JOptionPane.YES_NO_OPTION);
						if (check == 0) {
							getController().deleteWay(getController().getLastPickedWay());
							getController().resetLastPickedWay();
						}
					}
					else if (e.getSource().equals(getDeleteFromStopTableMenuItem())) {
						if (getStopTable().getSelectedRowCount() == 0) {
							return;
						}
						
						int [] selectedRows = convertRowIndexes(getStopTable(), getStopTable().getSelectedRows());
						
						if (getStopCb().getSelectedItem().toString().equals("Stops")) {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete selected stop/s?", "Stop Delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteStops(((StopTableModel)getStopTable().getModel()).getStopsAt(selectedRows));
							}
						}
						else {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete selected way/s?", "Way Delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteWays(((WayTableModel)getStopTable().getModel()).getWaysAt(getStopTable().getSelectedRows()));
							}
						}
					}
					else if (e.getSource().equals(getDeleteFromWayTableMenuItem())) {
						if (getWayTable().getSelectedRowCount() == 0) {
							return;
						}
					
						int [] selectedRows = convertRowIndexes(getWayTable(), getWayTable().getSelectedRows());
						
						if (getWayCb().getSelectedItem().toString().equals("Stops")) {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete selected stop/s?", "Stop Delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteStops(((StopTableModel)getWayTable().getModel()).getStopsAt(selectedRows));
							}
						}
						else {
							int check = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to delete selected way/s?", "Way Delete", JOptionPane.YES_NO_OPTION);
							if (check == 0) {
								getController().deleteWays(((WayTableModel)getWayTable().getModel()).getWaysAt(selectedRows));
							}
						}
					}
				}
			};
		}
		
		return actionListener;
	}
	
	public ListSelectionListener getListSelectionListener() {
		if (listSelectionListener == null) {
			listSelectionListener = new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getSource().equals(getStopTable().getSelectionModel())) {
 						int [] selectedRows = convertRowIndexes(getStopTable(), getStopTable().getSelectedRows());
						
						if (getStopCb().getSelectedItem().toString().equals("Stops")) {
							getController().highligthStops(((StopTableModel)getStopTable().getModel()).getStopsAt(selectedRows), Color.RED);
						}
						else {
							getController().highligthWays(((WayTableModel)getStopTable().getModel()).getWaysAt(selectedRows), Color.RED);
						}
					}
					else if (e.getSource().equals(getWayTable().getSelectionModel())) {
						int [] selectedRows = convertRowIndexes(getWayTable(), getWayTable().getSelectedRows());
						
						if (getWayCb().getSelectedItem().toString().equals("Stops")) {
							getController().highligthStops(((StopTableModel)getWayTable().getModel()).getStopsAt(selectedRows), Color.RED);
						}
						else {
							getController().highligthWays(((WayTableModel)getWayTable().getModel()).getWaysAt(selectedRows), Color.RED);
						}
					}
				}
			};
		}
		return listSelectionListener;
	}
	
	private int[] convertRowIndexes(JTable table, int[] selectedRows) {
		selectedRows = table.getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedRows[i] = table.convertRowIndexToModel(selectedRows[i]);
		}
		
		return selectedRows;
	}
	
	public void registryListeners() {
		getMapViewer().addMouseListener(getMouseListener());
		getMapViewer().addMouseMotionListener(getMapViewerMousemotionListener());
		getLoadBtn().addActionListener(getActionListener());
		getStopCb().addActionListener(getActionListener());
		getWayCb().addActionListener(getActionListener());
		getStopFilterTf().addActionListener(getActionListener());
		getWayFilterTf().addActionListener(getActionListener());
		getTileServersCb().addActionListener(getActionListener());
		getSolveBtn().addActionListener(getActionListener());
		getAddStopBtn().addActionListener(getActionListener());
		getAddWayBtn().addActionListener(getActionListener());
		getSaveBtn().addActionListener(getActionListener());
		getConnectToDBBtn().addActionListener(getActionListener());
		getOpenNetworkFromFilesBtn().addActionListener(getActionListener());
		getRemoveNetworkBtn().addActionListener(getActionListener());
		getExportStopsToCSVMenuItem().addActionListener(getActionListener());
		getExportWaysToCSVMenuItem().addActionListener(getActionListener());
		getStopEditMenuItem().addActionListener(getActionListener());
		getStopDeleteMenuItem().addActionListener(getActionListener());
		getWayEditMenuItem().addActionListener(getActionListener());
		getWayDeleteMenuItem().addActionListener(getActionListener());
		getDeleteFromStopTableMenuItem().addActionListener(getActionListener());
		getDeleteFromWayTableMenuItem().addActionListener(getActionListener());
		getStopTable().getSelectionModel().addListSelectionListener(getListSelectionListener());
		getWayTable().getSelectionModel().addListSelectionListener(getListSelectionListener());
		getStopTable().addMouseListener(getMouseListener());
		getWayTable().addMouseListener(getMouseListener());
	}
	
	public void unregistryListeners() {
		getMapViewer().removeMouseListener(getMouseListener());
		getMapViewer().removeMouseMotionListener(getMapViewerMousemotionListener());
		getLoadBtn().removeActionListener(getActionListener());
		getStopCb().removeActionListener(getActionListener());
		getWayCb().removeActionListener(getActionListener());
		getStopFilterTf().removeActionListener(getActionListener());
		getWayFilterTf().removeActionListener(getActionListener());
		getTileServersCb().removeActionListener(getActionListener());
		getSolveBtn().removeActionListener(getActionListener());
		getAddStopBtn().removeActionListener(getActionListener());
		getAddWayBtn().removeActionListener(getActionListener());
		getSaveBtn().removeActionListener(getActionListener());
		getConnectToDBBtn().removeActionListener(getActionListener());
		getOpenNetworkFromFilesBtn().removeActionListener(getActionListener());
		getRemoveNetworkBtn().removeActionListener(getActionListener());
		getExportStopsToCSVMenuItem().removeActionListener(getActionListener());
		getExportWaysToCSVMenuItem().removeActionListener(getActionListener());
		getStopEditMenuItem().removeActionListener(getActionListener());
		getStopDeleteMenuItem().removeActionListener(getActionListener());
		getWayEditMenuItem().removeActionListener(getActionListener());
		getWayDeleteMenuItem().removeActionListener(getActionListener());
		getDeleteFromStopTableMenuItem().removeActionListener(getActionListener());
		getDeleteFromStopTableMenuItem().removeActionListener(getActionListener());
		getStopTable().getSelectionModel().removeListSelectionListener(getListSelectionListener());
		getWayTable().getSelectionModel().removeListSelectionListener(getListSelectionListener());
		getStopTable().removeMouseListener(getMouseListener());
		getWayTable().removeMouseListener(getMouseListener());
	}
	
	public JPopupMenu getStopTablePopupMenu() {
		if (stopTablePopupMenu == null) {
			stopTablePopupMenu = new JPopupMenu("Manipulate");
			stopTablePopupMenu.add(getDeleteFromStopTableMenuItem());
		}
		
		return stopTablePopupMenu;
	}
	
	public JPopupMenu getWayTablePopupMenu() {
		if (wayTablePopupMenu == null) {
			wayTablePopupMenu = new JPopupMenu("Manipulate");
			wayTablePopupMenu.add(getDeleteFromWayTableMenuItem());
		}
		
		return wayTablePopupMenu;
	}
	
	public JPopupMenu getStopMapPopupMenu() {
		if (stopMapPopupMenu == null) {
			stopMapPopupMenu = new JPopupMenu("Manipulate");
			stopMapPopupMenu.add(getStopEditMenuItem());
			stopMapPopupMenu.add(getStopDeleteMenuItem());
		}
		
		return stopMapPopupMenu;
	}
	
	public JPopupMenu getWayMapPopupMenu() {
		if (wayMapPopupMenu == null) {
			wayMapPopupMenu = new JPopupMenu("Manipulate");
			wayMapPopupMenu.add(getWayEditMenuItem());
			wayMapPopupMenu.add(getWayDeleteMenuItem());
		}
		
		return wayMapPopupMenu;
	}
	
	private JMenuItem getExportStopsToCSVMenuItem() {
		if (exportStopsToCSVMenuItem == null) {
			exportStopsToCSVMenuItem = new JMenuItem("Export Stops To CSV");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/exportIcon.png"));
				exportStopsToCSVMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return exportStopsToCSVMenuItem;
	}
	
	private JMenuItem getExportWaysToCSVMenuItem() {
		if (exportWaysToCSVMenuItem == null) {
			exportWaysToCSVMenuItem = new JMenuItem("Export Ways To CSV");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/exportIcon.png"));
				exportWaysToCSVMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return exportWaysToCSVMenuItem;
	}
	
	private JMenuItem getStopEditMenuItem() {
		if (stopEditMenuItem == null) {
			stopEditMenuItem = new JMenuItem("Edit");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/editIcon.png"));
				stopEditMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stopEditMenuItem;
	}
	
	private JMenuItem getStopDeleteMenuItem() {
		if (stopDeleteMenuItem == null) {
			stopDeleteMenuItem = new JMenuItem("Delete");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/removeIcon.png"));
				stopDeleteMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stopDeleteMenuItem;
	}
	
	private JMenuItem getWayEditMenuItem() {
		if (wayEditMenuItem == null) {
			wayEditMenuItem = new JMenuItem("Edit");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/editIcon.png"));
				wayEditMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wayEditMenuItem;
	}
	
	private JMenuItem getWayDeleteMenuItem() {
		if (wayDeleteMenuItem == null) {
			wayDeleteMenuItem = new JMenuItem("Delete");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/removeIcon.png"));
				wayDeleteMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wayDeleteMenuItem;
	}
	
	private JMenuItem getDeleteFromStopTableMenuItem() {
		if (deleteFromStopTableMenuItem == null) {
			deleteFromStopTableMenuItem = new JMenuItem("Delete Selected");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/removeIcon.png"));
				deleteFromStopTableMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return deleteFromStopTableMenuItem;
	}
	
	private JMenuItem getDeleteFromWayTableMenuItem() {
		if (deleteFromWayTableMenuItem == null) {
			deleteFromWayTableMenuItem = new JMenuItem("Delete Selected");
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/removeIcon.png"));
				deleteFromWayTableMenuItem.setIcon(new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return deleteFromWayTableMenuItem;
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
			infoLabel.setBackground(Color.LIGHT_GRAY);
			infoLabel.setHorizontalTextPosition(JLabel.CENTER);
		}
		return infoLabel;
	}
	
	public JTextField getStopFilterTf() {
		if (stopFilterTf ==null) {
			stopFilterTf = new JTextField();
		}
		return stopFilterTf;
	}
	
	public JTextField getWayFilterTf() {
		if (wayFilterTf ==null) {
			wayFilterTf = new JTextField();
		}
		return wayFilterTf;
	}
	
	public JTable getStopTable() {
		if (stopTable == null) {
			stopTable = new JTable();
			stopTable.setAutoCreateRowSorter(true);
		}
		return stopTable;
	}
	
	public JTable getWayTable() {
		if (wayTable == null) {
			wayTable = new JTable();
			wayTable.setAutoCreateRowSorter(true);
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