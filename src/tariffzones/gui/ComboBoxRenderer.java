package tariffzones.gui;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import tariffzones.map.ImageTextComboBoxItem;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer {
	
	private List<ImageTextComboBoxItem> items;

    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {

    	//Get the selected index. (The index param isn't
        //always valid, so just use the value.)
        int selectedIndex = ((Integer)value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (items != null) {
        	//Set the icon and text.  If icon was null, say so.
            ImageIcon icon = items.get(selectedIndex).getImageIcon();
            String text = items.get(selectedIndex).getText();
            if (icon != null) {
            	setIcon(icon);
            }
            
            setText(text);
            setFont(list.getFont());
		}

        return this;
    }
    
    public void setItems(List<ImageTextComboBoxItem> items) {
    	this.items = items;
    }
}
