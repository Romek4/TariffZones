package tariffzones.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectionDlg extends JPanel {
	
	private JTextField usernameTf;
	private JPasswordField passwordPf;
	private JTextField dbUrlTf;
	private JLabel iconLabel;
	private JCheckBox rememberChb;
	
	public ConnectionDlg() {
		super();
		initializeComponents();
	}
	
	private void initializeComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 15, 5);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.2;
		gbc.weighty = 0.5;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getIconLabel(), gbc);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		
		JLabel dbUrlLb = new JLabel("Database URL:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(dbUrlLb, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		this.add(getDBUrlTf(), gbc);
		
		JLabel usernameLb = new JLabel("Username:");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		this.add(usernameLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.8;
		this.add(getUsernameTf(), gbc);
		
		JLabel passwordLb = new JLabel("Password:");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.2;
		this.add(passwordLb, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0.8;
		this.add(getPasswordTf(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		this.add(getRememberChb(), gbc);
	}
	
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel("Login");
			iconLabel.setFont(new Font(iconLabel.getFont().getName(), Font.BOLD, 16));
			Image img;
			try {
				img = ImageIO.read(new FileInputStream("resources/images/loginImg.png"));
				iconLabel.setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return iconLabel;
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
	
	public JCheckBox getRememberChb() {
		if (rememberChb == null) {
			rememberChb = new JCheckBox("Remember url and username");
			rememberChb.setSelected(true);
		}
		return rememberChb;
	}
	
}
