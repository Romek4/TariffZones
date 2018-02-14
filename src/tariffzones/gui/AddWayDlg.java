package tariffzones.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddWayDlg extends JPanel {
	private JTextField wayLengthTf;
	private JTextField wayTimeLengthTf;
	private JLabel iconLabel;
	
	public AddWayDlg() {
		super();
		initialize();
	}
	
	public void showDlg() {
//		this.setModal(true);
		this.setVisible(true);
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
		
		JLabel wayLengthLb = new JLabel("Way length(km):");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(wayLengthLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getWayLengthTf(), gbc);
		
		JLabel wayTimeLengthLb = new JLabel("Way time length(min):");
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(wayTimeLengthLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(getWayTimeLengthTf(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
		}
		return iconLabel;
	}
	
	public JTextField getWayLengthTf() {
		if (wayLengthTf == null) {
			wayLengthTf = new JTextField();
			wayLengthTf.setPreferredSize(new Dimension(100, 20));
			wayLengthTf.setMinimumSize(new Dimension(100, 20));
		}
		return wayLengthTf;
	}
	
	public JTextField getWayTimeLengthTf() {
		if (wayTimeLengthTf == null) {
			wayTimeLengthTf = new JTextField();
			wayTimeLengthTf.setPreferredSize(new Dimension(100, 20));
			wayTimeLengthTf.setMinimumSize(new Dimension(100, 20));
		}
		return wayTimeLengthTf;
	}
	
	public void setIconLabelImg(Image img) {
		getIconLabel().setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
	}
	
	public void setIconLabelText(String text) {
		getIconLabel().setText(text);
	}
	
	public static void main(String[] args) {
		AddWayDlg dlg = new AddWayDlg();
		dlg.showDlg();
	}
}
