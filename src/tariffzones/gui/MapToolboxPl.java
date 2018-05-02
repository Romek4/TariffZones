package tariffzones.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class MapToolboxPl extends JPanel {
	private JButton solveBtn;
	private JButton pointBtn;
	private JButton wayBtn;
	private JButton saveBtn;
	private JButton openNetworkFromFilesBtn;
	
	public MapToolboxPl() {
		super();
		initializePanel();
	}

	private void initializePanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 1;
		
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		this.add(getOpenNetworkFromFilesBtn(), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(getSolveBtn(), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		this.add(getPointBtn(), gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		this.add(getWayBtn(), gbc);
		
		gbc.gridx = 4;
		gbc.gridy = 0;
//		this.add(getSaveBtn(), gbc);
	}
	
	public JButton getOpenNetworkFromFilesBtn() {
		if (openNetworkFromFilesBtn == null) {
			openNetworkFromFilesBtn = new JButton();
			openNetworkFromFilesBtn.setSize(new Dimension(32, 32));
			openNetworkFromFilesBtn.setContentAreaFilled(false);
			try {
				Image img = ImageIO.read(ClassLoader.class.getResourceAsStream("/openIcon.png"));
				openNetworkFromFilesBtn.setIcon(new ImageIcon(img.getScaledInstance(openNetworkFromFilesBtn.getWidth(), openNetworkFromFilesBtn.getHeight(), Image.SCALE_SMOOTH)));
				openNetworkFromFilesBtn.setToolTipText("Read network from files");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return openNetworkFromFilesBtn;
	}

	public JButton getSolveBtn() {
		if (solveBtn == null) {
			try {
				solveBtn = new JButton();
				solveBtn.setSize(new Dimension(32, 32));
				solveBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(ClassLoader.class.getResourceAsStream("/solve.png"));
				solveBtn.setIcon(new ImageIcon(img.getScaledInstance(solveBtn.getWidth(), solveBtn.getHeight(), Image.SCALE_SMOOTH)));
				solveBtn.setToolTipText("Solve tariff zones problem for current network");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return solveBtn;
	}
	
	public JButton getPointBtn() {
		if (pointBtn == null) {
			try {
				pointBtn = new JButton();
				pointBtn.setSize(new Dimension(32, 32));
				pointBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(ClassLoader.class.getResourceAsStream("/busStopBtnImg.png"));
				pointBtn.setIcon(new ImageIcon(img.getScaledInstance(pointBtn.getWidth(), pointBtn.getHeight(), Image.SCALE_SMOOTH)));
				pointBtn.setToolTipText("Add new stop to current network");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pointBtn;
	}
	
	public JButton getWayBtn() {
		if (wayBtn == null) {
			try {
				wayBtn = new JButton();
				wayBtn.setSize(new Dimension(32, 32));
				wayBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(ClassLoader.class.getResourceAsStream("/wayBtnImg.png"));
				wayBtn.setIcon(new ImageIcon(img.getScaledInstance(wayBtn.getWidth(), wayBtn.getHeight(), Image.SCALE_SMOOTH)));
				wayBtn.setToolTipText("Add new way to current network");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wayBtn;
	}
	
	public JButton getSaveBtn() {
		if (saveBtn == null) {
			try {
				saveBtn = new JButton();
				saveBtn.setSize(new Dimension(32, 32));
				saveBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(ClassLoader.class.getResourceAsStream("/saveToDBImg.png"));
				saveBtn.setIcon(new ImageIcon(img.getScaledInstance(saveBtn.getWidth(), saveBtn.getHeight(), Image.SCALE_SMOOTH)));
				saveBtn.setToolTipText("Save changes to database");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return saveBtn;
	}

	public void enableBtns(boolean enabled) {
		getSolveBtn().setEnabled(enabled);
		getPointBtn().setEnabled(enabled);
		getWayBtn().setEnabled(enabled);
		getSaveBtn().setEnabled(enabled);
	}
}
