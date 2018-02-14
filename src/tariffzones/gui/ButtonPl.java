package tariffzones.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPl extends JPanel {
	private JButton solveBtn;
	private JButton exportCSVBtn;
	
	public ButtonPl() {
		super();
		initialize();
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(new Dimension(40, 100));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(getSolveBtn(), gbc);
	}

	private JButton getSolveBtn() {
		if (solveBtn == null) {
			solveBtn = new JButton("Solve");
		}
		return solveBtn;
	}
	
	private JButton getExportCSVBtn() {
		if (exportCSVBtn == null) {
			exportCSVBtn = new JButton("Export");
		}
		return exportCSVBtn;
	}
}
