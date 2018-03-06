package tariffzones.gui;

import java.awt.Color;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddStopDlg extends JPanel {
	
	private JTextField stopNumberTf;
	private JTextField stopNameTf;
	private JTextField numberOfCustomersTf;
	private JLabel iconLabel;
	
	public AddStopDlg() {
		super();
		initialize();
	}

	private void initialize() {
//		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 15, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getIconLabel(), gbc);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		
		JLabel stopNumberLb = new JLabel("Stop number:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(stopNumberLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getStopNumberTf(), gbc);
		
		JLabel stopNameLb = new JLabel("Stop name:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(stopNameLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(getStopNameTf(), gbc);
		
		JLabel numberOfCustomersLb = new JLabel("Number of customers:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(numberOfCustomersLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(getNumberOfCustomersTf(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
		}
		return iconLabel;
	}
	
	public JTextField getStopNumberTf() {
		if (stopNumberTf == null) {
			stopNumberTf = new JTextField();
			stopNumberTf.setPreferredSize(new Dimension(100, 20));
			stopNumberTf.setMinimumSize(new Dimension(100, 20));
		}
		return stopNumberTf;
	}
	
	public JTextField getStopNameTf() {
		if (stopNameTf == null) {
			stopNameTf = new JTextField();
			stopNameTf.setPreferredSize(new Dimension(100, 20));
			stopNameTf.setMinimumSize(new Dimension(100, 20));
		}
		return stopNameTf;
	}
	
	public JTextField getNumberOfCustomersTf() {
		if (numberOfCustomersTf == null) {
			numberOfCustomersTf = new JTextField();
			numberOfCustomersTf.setPreferredSize(new Dimension(100, 20));
			numberOfCustomersTf.setMinimumSize(new Dimension(100, 20));
		}
		return numberOfCustomersTf;
	}
	
	public void setIconLabelImg(Image img) {
		getIconLabel().setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
	}
	
	public void setIconLabelText(String text) {
		getIconLabel().setText(text);
	}
}
