package tariffzones.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OpenNetworkFromFilesDlg extends JPanel {
	private JLabel iconLabel;
	private JLabel stopsFileLb;
	private JLabel waysFileLb;
	private JButton stopsBtn;
	private JButton waysBtn;
	
	private ActionListener actionListener;

	public OpenNetworkFromFilesDlg() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getIconLabel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(new JLabel("Choose file for stops input: "), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		this.add(getStopsFileLb(), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		this.add(getStopsBtn(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(new JLabel("Choose file for ways input: "), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		this.add(getWayFileLb(), gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		this.add(getWaysBtn(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
		}
		return iconLabel;
	}
	
	public JLabel getStopsFileLb() {
		if (stopsFileLb == null) {
			stopsFileLb = new JLabel("Not selected");
			stopsFileLb.setMinimumSize(new Dimension(100, 19));
			stopsFileLb.setPreferredSize(new Dimension(100, 19));
			stopsFileLb.setMaximumSize(new Dimension(100, 19));
		}
		return stopsFileLb;
	}
	
	public JLabel getWayFileLb() {
		if (waysFileLb == null) {
			waysFileLb = new JLabel("Not selected");
			waysFileLb.setMinimumSize(new Dimension(100, 19));
			waysFileLb.setPreferredSize(new Dimension(100, 19));
			waysFileLb.setMaximumSize(new Dimension(100, 19));
		}
		return waysFileLb;
	}

	private JButton getWaysBtn() {
		if (waysBtn == null) {
			try {
				waysBtn = new JButton();
				waysBtn.setSize(new Dimension(24, 24));
				waysBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(new FileInputStream("resources/images/browseFoldersIcon.png"));
				waysBtn.setIcon(new ImageIcon(img.getScaledInstance(waysBtn.getWidth(), waysBtn.getHeight(), Image.SCALE_SMOOTH)));
				waysBtn.setToolTipText("Solve tariff zones problem for current network");
				
				waysBtn.addActionListener(getActionListener());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return waysBtn;
	}

	private JButton getStopsBtn() {
		if (stopsBtn == null) {
			try {
				stopsBtn = new JButton();
				stopsBtn.setSize(new Dimension(24, 24));
				stopsBtn.setContentAreaFilled(false);
				Image img = ImageIO.read(new FileInputStream("resources/images/browseFoldersIcon.png"));
				stopsBtn.setIcon(new ImageIcon(img.getScaledInstance(stopsBtn.getWidth(), stopsBtn.getHeight(), Image.SCALE_SMOOTH)));
				stopsBtn.setToolTipText("Solve tariff zones problem for current network");
				
				stopsBtn.addActionListener(getActionListener());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stopsBtn;
	}
	
	private ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(getStopsBtn())) {
						selectFile(getStopsFileLb());
					}
					else if (e.getSource().equals(getWaysBtn())) {
						selectFile(getWayFileLb());
					}
				}
			};
		}
		return actionListener;
	}
	
	private void selectFile(JLabel label) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(this.getParent());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				label.setText(file.getAbsolutePath());
			}
		}
	}
	
	public void setIconLabelImg(Image img) {
		getIconLabel().setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
	}
	
	public void setIconLabelText(String text) {
		getIconLabel().setText(text);
	}
}
