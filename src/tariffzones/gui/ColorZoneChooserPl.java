package tariffzones.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import tariffzones.basicobjects.ColorEditor;
import tariffzones.basicobjects.ColorRenderer;
import tariffzones.basicobjects.ColorTableModel;

public class ColorZoneChooserPl extends JPanel {
	private JComboBox<Integer> numberOfZonesCb;
	private JScrollPane colorForZonesTableScrollPane;
	private JTable colorForZonesTable;
	private JCheckBox countODMatrixChb;
	private JCheckBox usePricesChb;
	private JRadioButton useTimeLengthRb;
	private JRadioButton useDistanceRb;
	private ButtonGroup radioBtnGrp;
	private JButton odMatrixBtn;
	private JButton pricesBtn;
	private JLabel odMatrixLb;
	private JLabel pricesLb;
	private JCheckBox useODMatrixChb;

	private JRadioButton algWithNoPricesRb;
	private JRadioButton algWithPricesRb;
	private ButtonGroup algRadioBtnGrp;
	
	private JLabel f1Lb;
	private JLabel f2Lb;
	private JTextField f1Tf;
	private JTextField f2Tf;
	
	private JButton e1Btn;
	private JButton e2Btn;
	private JButton e3Btn;
	
	private int eSelected = 1;
	
	public ColorZoneChooserPl() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		
		JLabel zoneNumberLb = new JLabel("Set number of zones:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(zoneNumberLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(getNumberOfZonesCb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		this.add(getColorForZonesTableScrollPane(), gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(getAlgWithNoPricesRb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(getAlgWithPricesRb(), gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(getF1Lb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(getF1Tf(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(getF2Lb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.add(getF2Tf(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		this.add(getPricesLb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		this.add(getPricesBtn(), gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		this.add(getE1Btn(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		this.add(getE2Btn(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		this.add(getE3Btn(), gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 9;
		this.add(getUseDistanceRb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 9;
		this.add(getUseTimeLengthRb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		this.add(getUseODMatrixChb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		this.add(getCountODMatrixChb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		this.add(getODMatrixLb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 12;
		this.add(getODMatrixBtn(), gbc);
	}

	public JComboBox getNumberOfZonesCb() {
		if (numberOfZonesCb == null) {
			numberOfZonesCb = new JComboBox<>();
			numberOfZonesCb.addItem(0);
			for (int i = 2; i < 100; i++) {
				numberOfZonesCb.addItem(new Integer(i));
			}
			
			numberOfZonesCb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (getNumberOfZonesCb().getSelectedIndex() > 0) {
						
						int numberOfZones = (int) getNumberOfZonesCb().getSelectedItem();
						ColorTableModel tableModel = (ColorTableModel)getColorForZonesTable().getModel();
						Object[][] data = null;
						
						if (tableModel.getRowCount() < numberOfZones) {
							data = copyDataFrom(tableModel.getData(), numberOfZones, tableModel.getRowCount(), 2);
							for (int i = tableModel.getRowCount(); i < numberOfZones; i++) {
								data [i][0] = "Zone " + (i+1);
								data [i][1] = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
							}
						}
						else {
							data = copyDataFrom(tableModel.getData(), numberOfZones, numberOfZones, 2);
						}
						
						tableModel.setData(data);
					}
				}
			});
		}
		return numberOfZonesCb;
	}

	private Object[][] copyDataFrom(Object[][] dataToCopyFrom, int totalRows, int rowsToCopy, int columnsToCopy) {
		Object[][] data = new Object[totalRows][columnsToCopy];
		
		if (dataToCopyFrom.length > 0) {
			for (int i = 0; i < rowsToCopy; i++) {
				for (int j = 0; j < columnsToCopy; j++) {
					data[i][j] = dataToCopyFrom[i][j];
				}
			}
		}
		
		return data;
	}
	
	public ArrayList<Color> getSelectedColors() {
		ArrayList<Color> colorList = new ArrayList<>();
		ColorTableModel model = (ColorTableModel) getColorForZonesTable().getModel();
		for (int i = 0; i < model.getData().length; i++) {
			colorList.add((Color) model.getData()[i][1]);
		}
		
		return colorList;
	}
	
	public JCheckBox getUseODMatrixChb() {
		if (useODMatrixChb == null) {
			useODMatrixChb = new JCheckBox("Use OD-matrix");
			useODMatrixChb.setSelected(false);
			useODMatrixChb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					if (useODMatrixChb.isSelected()) {
						getCountODMatrixChb().setEnabled(true);
						getODMatrixLb().setEnabled(true);
						getODMatrixBtn().setEnabled(true);
					}
					else {
						getCountODMatrixChb().setEnabled(false);
						getODMatrixLb().setEnabled(false);
						getODMatrixBtn().setEnabled(false);
					}
				}
			});
		}
		return useODMatrixChb;
	}
	
	public JCheckBox getUsePricesChb() {
		if (usePricesChb == null) {
			usePricesChb = new JCheckBox("Use prices");
			usePricesChb.setSelected(false);
			usePricesChb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (usePricesChb.isSelected()) {
						getPricesBtn().setEnabled(true);
						getPricesLb().setEnabled(true);
					}
					else {
						getPricesBtn().setEnabled(false);
						getPricesLb().setEnabled(false);
					}
				}
			});
		}
		return usePricesChb;
	}

	private JScrollPane getColorForZonesTableScrollPane() {
		if (colorForZonesTableScrollPane == null) {
			colorForZonesTableScrollPane = new JScrollPane(getColorForZonesTable());
			colorForZonesTableScrollPane.setPreferredSize(new Dimension(350, 120));
		}
		return colorForZonesTableScrollPane;
	}
	
	public JTable getColorForZonesTable() {
		if (colorForZonesTable == null) {
			colorForZonesTable = new JTable(new ColorTableModel(new String[]{"Zone", "Color"}));
			colorForZonesTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
			colorForZonesTable.setDefaultEditor(Color.class, new ColorEditor());
		}
		return colorForZonesTable;
	}
	
	public JCheckBox getCountODMatrixChb() {
		if (countODMatrixChb == null) {
			countODMatrixChb = new JCheckBox("Use gravity model count OD matrix");
			countODMatrixChb.setSelected(false);
			countODMatrixChb.setEnabled(false);
			
			countODMatrixChb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (countODMatrixChb.isSelected()) {
						getODMatrixBtn().setEnabled(false);
						getODMatrixLb().setEnabled(false);
					}
					else {
						getODMatrixBtn().setEnabled(true);
						getODMatrixLb().setEnabled(true);
					}
				}
			});
		}
		return countODMatrixChb;
	}
	
	public JLabel getODMatrixLb() {
		if (odMatrixLb == null) {
			odMatrixLb = new JLabel("OD matrix file not yet selected");
			odMatrixLb.setMinimumSize(new Dimension(100, 19));
			odMatrixLb.setPreferredSize(new Dimension(100, 19));
			odMatrixLb.setMaximumSize(new Dimension(100, 19));
			odMatrixLb.setEnabled(false);
		}
		return odMatrixLb;
	}
	
	public JLabel getPricesLb() {
		if (pricesLb == null) {
			pricesLb = new JLabel("Prices file not yet selected");
			pricesLb.setMinimumSize(new Dimension(100, 19));
			pricesLb.setPreferredSize(new Dimension(100, 19));
			pricesLb.setMaximumSize(new Dimension(100, 19));
			pricesLb.setEnabled(false);
		}
		return pricesLb;
	}
	
	private JButton getODMatrixBtn() {
		if (odMatrixBtn == null) {
			try {
				odMatrixBtn = new JButton();
				odMatrixBtn.setSize(new Dimension(24, 24));
				odMatrixBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(new FileInputStream("resources/images/browseFoldersIcon.png"));
				odMatrixBtn.setIcon(new ImageIcon(img.getScaledInstance(odMatrixBtn.getWidth(), odMatrixBtn.getHeight(), Image.SCALE_SMOOTH)));
				odMatrixBtn.setToolTipText("Select OD matrix file");
				odMatrixBtn.setEnabled(false);
				
				odMatrixBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser();
						int returnVal = fileChooser.showOpenDialog(odMatrixBtn.getParent());
						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							if (file != null) {
								getODMatrixLb().setText(file.getAbsolutePath());
							}
						}
						
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return odMatrixBtn;
	}
	
	private JButton getPricesBtn() {
		if (pricesBtn == null) {
			try {
				pricesBtn = new JButton();
				pricesBtn.setSize(new Dimension(24, 24));
				pricesBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(new FileInputStream("resources/images/browseFoldersIcon.png"));
				pricesBtn.setIcon(new ImageIcon(img.getScaledInstance(pricesBtn.getWidth(), pricesBtn.getHeight(), Image.SCALE_SMOOTH)));
				pricesBtn.setToolTipText("Select prices file");
				pricesBtn.setEnabled(false);
				
				pricesBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser();
						int returnVal = fileChooser.showOpenDialog(pricesBtn.getParent());
						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							if (file != null) {
								getPricesLb().setText(file.getAbsolutePath());
							}
						}
						
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pricesBtn;
	}
	
	public JLabel getF1Lb() {
		if (f1Lb == null) {
			f1Lb = new JLabel("f1");
			f1Lb.setMinimumSize(new Dimension(100, 19));
			f1Lb.setPreferredSize(new Dimension(100, 19));
			f1Lb.setMaximumSize(new Dimension(100, 19));
			f1Lb.setEnabled(false);
		}
		return f1Lb;
	}
	
	public JLabel getF2Lb() {
		if (f2Lb == null) {
			f2Lb = new JLabel("f2");
			f2Lb.setMinimumSize(new Dimension(100, 19));
			f2Lb.setPreferredSize(new Dimension(100, 19));
			f2Lb.setMaximumSize(new Dimension(100, 19));
			f2Lb.setEnabled(false);
		}
		return f2Lb;
	}
	
	public JTextField getF1Tf() {
		if (f1Tf == null) {
			f1Tf = new JTextField();
			f1Tf.setMinimumSize(new Dimension(100, 19));
			f1Tf.setPreferredSize(new Dimension(100, 19));
			f1Tf.setMaximumSize(new Dimension(100, 19));
			f1Tf.setEnabled(false);
		}
		return f1Tf;
	}
	
	public JTextField getF2Tf() {
		if (f2Tf == null) {
			f2Tf = new JTextField();
			f2Tf.setMinimumSize(new Dimension(100, 19));
			f2Tf.setPreferredSize(new Dimension(100, 19));
			f2Tf.setMaximumSize(new Dimension(100, 19));
			f2Tf.setEnabled(false);
		}
		return f2Tf;
	}
	
	private JButton getE1Btn() {
		if (e1Btn == null) {
			try {
				e1Btn = new JButton();
				e1Btn.setSize(new Dimension(48, 48));
				e1Btn.setBackground(Color.WHITE);
				Image img = ImageIO.read(new FileInputStream("resources/images/e1.jpg"));
				e1Btn.setIcon(new ImageIcon(img.getScaledInstance(e1Btn.getWidth(), e1Btn.getHeight(), Image.SCALE_SMOOTH)));
				e1Btn.setBorder(new LineBorder(Color.BLUE, 2));
				
				e1Btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						e1Btn.setBorder(new LineBorder(Color.BLUE, 2));
						e2Btn.setBorder(new LineBorder(Color.WHITE, 2));
						e3Btn.setBorder(new LineBorder(Color.WHITE, 2));
						eSelected = 1;
						getUseDistanceRb().setEnabled(false);
						getUseTimeLengthRb().setEnabled(false);
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return e1Btn;
	}
	
	private JButton getE2Btn() {
		if (e2Btn == null) {
			try {
				e2Btn = new JButton();
				e2Btn.setSize(new Dimension(116, 48));
				e2Btn.setBackground(Color.WHITE);
				Image img = ImageIO.read(new FileInputStream("resources/images/e2.jpg"));
				e2Btn.setIcon(new ImageIcon(img.getScaledInstance(e2Btn.getWidth(), e2Btn.getHeight(), Image.SCALE_SMOOTH)));
				e2Btn.setBorder(new LineBorder(Color.WHITE, 2));
				
				e2Btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						e1Btn.setBorder(new LineBorder(Color.WHITE, 2));
						e2Btn.setBorder(new LineBorder(Color.BLUE, 2));
						e3Btn.setBorder(new LineBorder(Color.WHITE, 2));
						eSelected = 2;
						getUseDistanceRb().setEnabled(true);
						getUseTimeLengthRb().setEnabled(true);
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return e2Btn;
	}
	
	private JButton getE3Btn() {
		if (e3Btn == null) {
			try {
				e3Btn = new JButton();
				e3Btn.setSize(new Dimension(116, 48));
				e3Btn.setBackground(Color.WHITE);
				Image img = ImageIO.read(new FileInputStream("resources/images/e3.jpg"));
				e3Btn.setIcon(new ImageIcon(img.getScaledInstance(e3Btn.getWidth(), e3Btn.getHeight(), Image.SCALE_SMOOTH)));
				e3Btn.setBorder(new LineBorder(Color.WHITE, 2));
				
				e3Btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						e1Btn.setBorder(new LineBorder(Color.WHITE, 2));
						e2Btn.setBorder(new LineBorder(Color.WHITE, 2));
						e3Btn.setBorder(new LineBorder(Color.BLUE, 2));
						eSelected = 3;
						getUseDistanceRb().setEnabled(true);
						getUseTimeLengthRb().setEnabled(true);
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return e3Btn;
	}
	
	public JRadioButton getUseTimeLengthRb() {
		if (useTimeLengthRb == null) {
			useTimeLengthRb = new JRadioButton("Use time length");
			useTimeLengthRb.setSelected(false);
			useTimeLengthRb.setEnabled(false);
			getRadioBtnGrp().add(useTimeLengthRb);
		}
		return useTimeLengthRb;
	}
	
	public JRadioButton getUseDistanceRb() {
		if (useDistanceRb == null) {
			useDistanceRb = new JRadioButton("Use distance(km)");
			useDistanceRb.setSelected(true);
			useDistanceRb.setEnabled(false);
			getRadioBtnGrp().add(useDistanceRb);
		}
		return useDistanceRb;
	}
	
	private ButtonGroup getRadioBtnGrp() {
		if (radioBtnGrp == null) {
			radioBtnGrp = new ButtonGroup();
		}
		return radioBtnGrp;
	}
	
	public JRadioButton getAlgWithNoPricesRb() {
		if (algWithNoPricesRb == null) {
			algWithNoPricesRb = new JRadioButton("with no prices");
			algWithNoPricesRb.setSelected(true);
			getAlgRadioBtnGrp().add(algWithNoPricesRb);
		}
		return algWithNoPricesRb;
	}
	
	public JRadioButton getAlgWithPricesRb() {
		if (algWithPricesRb == null) {
			algWithPricesRb = new JRadioButton("with prices");
			algWithPricesRb.setSelected(true);
			getAlgRadioBtnGrp().add(algWithPricesRb);
			
			algWithPricesRb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (algWithPricesRb.isSelected()) {
						getF1Lb().setEnabled(true);
						getF2Lb().setEnabled(true);
						getF1Tf().setEnabled(true);
						getF2Tf().setEnabled(true);
						getPricesLb().setEnabled(true);
						getPricesBtn().setEnabled(true);
						getUseODMatrixChb().setEnabled(true);
					}
					else {
						getF1Lb().setEnabled(false);
						getF2Lb().setEnabled(false);
						getF1Tf().setEnabled(false);
						getF2Tf().setEnabled(false);
						getPricesLb().setEnabled(false);
						getPricesBtn().setEnabled(false);
						getUseODMatrixChb().setEnabled(false);
					}
				}
			});
		}
		return algWithPricesRb;
	}
	
	private ButtonGroup getAlgRadioBtnGrp() {
		if (algRadioBtnGrp == null) {
			algRadioBtnGrp = new ButtonGroup();
		}
		return algRadioBtnGrp;
	}
	
	public int getESelected() {
		return eSelected;
	}
}
