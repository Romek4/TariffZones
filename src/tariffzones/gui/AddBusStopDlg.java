package tariffzones.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddBusStopDlg extends JPanel {
	
	private JTextField stopNumberTf;
	private JTextField stopNameTf;
	private JTextField numberOfCustomersTf;
	
	public AddBusStopDlg() {
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
		gbc.insets = new Insets(5, 5, 5, 5);
		
		JLabel stopNumberLb = new JLabel("Stop number:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(stopNumberLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.add(getStopNumberTf(), gbc);
		
		JLabel stopNameLb = new JLabel("Stop name:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(stopNameLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.add(getStopNameTf(), gbc);
		
		JLabel numberOfCustomersLb = new JLabel("Number of customers:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(numberOfCustomersLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(getNumberOfCustomersTf(), gbc);
	}
	
	public JTextField getStopNumberTf() {
		if (stopNumberTf == null) {
			stopNumberTf = new JTextField();
			stopNumberTf.setPreferredSize(new Dimension(100, 19));
			stopNumberTf.setMinimumSize(new Dimension(100, 19));
		}
		return stopNumberTf;
	}
	
	public JTextField getStopNameTf() {
		if (stopNameTf == null) {
			stopNameTf = new JTextField();
			stopNameTf.setPreferredSize(new Dimension(100, 19));
			stopNameTf.setMinimumSize(new Dimension(100, 19));
		}
		return stopNameTf;
	}
	
	public JTextField getNumberOfCustomersTf() {
		if (numberOfCustomersTf == null) {
			numberOfCustomersTf = new JTextField();
			numberOfCustomersTf.setPreferredSize(new Dimension(100, 19));
			numberOfCustomersTf.setMinimumSize(new Dimension(100, 19));
		}
		return numberOfCustomersTf;
	}
	
	public static void main(String[] args) {
		AddBusStopDlg dlg = new AddBusStopDlg();
		dlg.showDlg();
	}
}
