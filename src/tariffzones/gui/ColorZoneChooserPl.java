package tariffzones.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import tariffzones.model.ColorEditor;
import tariffzones.model.ColorRenderer;
import tariffzones.model.ColorTableModel;

public class ColorZoneChooserPl extends JPanel {
	private JComboBox<Integer> numberOfZonesCb;
	private JCheckBox useRandomColorsChb;
	private JScrollPane colorForZonesTableScrollPane;
	private JTable colorForZonesTable;
	private JCheckBox useNumberOfHabitantsChb;
	private JCheckBox useOnlyNumberOfHabitantsChb;
	private JRadioButton useTimeLengthRb;
	private JRadioButton useDistanceRb;
	private ButtonGroup radioBtnGrp;
	
	public ColorZoneChooserPl() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.BOTH;
		
		JLabel zoneNumberLb = new JLabel("Set number of zones:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(zoneNumberLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(getNumberOfZonesCb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
//		this.add(getUseRandomColorsChb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		this.add(getColorForZonesTableScrollPane(), gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(getUseNumberOfHabitantsChb(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		this.add(getUseDistanceRb(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.add(getUseTimeLengthRb(), gbc);
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
	
	private JCheckBox getUseRandomColorsChb() {
		if (useRandomColorsChb == null) {
			useRandomColorsChb = new JCheckBox("Use random colors");
			useRandomColorsChb.setSelected(false);
		}
		return useRandomColorsChb;
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
	
	public JCheckBox getUseNumberOfHabitantsChb() {
		if (useNumberOfHabitantsChb == null) {
			useNumberOfHabitantsChb = new JCheckBox("Include number of habitants");
			useNumberOfHabitantsChb.setSelected(false);
		}
		return useNumberOfHabitantsChb;
	}
	
	public JCheckBox getUseOnlyNumberOfHabitantsChb() {
		if (useOnlyNumberOfHabitantsChb == null) {
			useOnlyNumberOfHabitantsChb = new JCheckBox("Include only number of habitants");
			useOnlyNumberOfHabitantsChb.setSelected(false);
			useOnlyNumberOfHabitantsChb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (useOnlyNumberOfHabitantsChb.isSelected()) {
						getUseNumberOfHabitantsChb().setSelected(false);
						getUseDistanceRb().setEnabled(false);
						getUseTimeLengthRb().setEnabled(false);
					}
					else {
						getUseNumberOfHabitantsChb().setSelected(true);
						getUseDistanceRb().setEnabled(true);
						getUseTimeLengthRb().setEnabled(true);
					}
				}
			});
		}
		return useOnlyNumberOfHabitantsChb;
	}
	
	public JRadioButton getUseTimeLengthRb() {
		if (useTimeLengthRb == null) {
			useTimeLengthRb = new JRadioButton("Include time length");
			useTimeLengthRb.setSelected(false);
			getRadioBtnGrp().add(useTimeLengthRb);
		}
		return useTimeLengthRb;
	}
	
	public JRadioButton getUseDistanceRb() {
		if (useDistanceRb == null) {
			useDistanceRb = new JRadioButton("Include distance");
			useDistanceRb.setSelected(true);
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
}
