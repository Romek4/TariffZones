package tariffzones.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StopInfoPanel extends JInternalFrame{
	private JButton deleteBtn;
	private JButton editBtn;
	private JTextField stopNumberTf;
	private JTextField stopNameTf;
	private JTextField numberOfCustomersTf;
	
	public StopInfoPanel() {
		super();
		initializeComponents();
	}
	
	private void initializeComponents() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;

		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(new JLabel("Stop number"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(getStopNumberTf(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(new JLabel("Stop name"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getStopNameTf(), gbc);
	}
	
	public JTextField getStopNumberTf() {
		if (stopNumberTf == null) {
			stopNumberTf = new JTextField(50);
			stopNumberTf.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return stopNumberTf;
	}
	
	public JTextField getStopNameTf() {
		if (stopNameTf == null) {
			stopNameTf = new JTextField(50);
		}
		return stopNameTf;
	}

	private JButton getEditBtn() {
		if (editBtn == null) {
			editBtn = new JButton("Edit");
		}
		return editBtn;
	}

	private JButton getDeleteBtn() {
		if (deleteBtn == null) {
			deleteBtn = new JButton("Delete");
		}
		return deleteBtn;
	}
}
