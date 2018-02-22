package tariffzones.gui;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectionDlg extends JPanel {
	
	private JTextField usernameTf;
	private JPasswordField passwordPf;
	private JTextField dbUrlTf;
	
	public ConnectionDlg() {
		super();
		initializeComponents();
	}
	
	private void initializeComponents() {
		
	}

	public JTextField getUsernameTf() {
		if (usernameTf == null) {
			usernameTf = new JTextField();
		}
		return usernameTf;
	}
	
	public JPasswordField getPasswordTf() {
		if (passwordPf == null) {
			passwordPf = new JPasswordField();
		}
		return passwordPf;
	}
	
	public JTextField getDBUrlTf() {
		if (dbUrlTf == null) {
			dbUrlTf = new JTextField();
		}
		return dbUrlTf;
	}
	
}
