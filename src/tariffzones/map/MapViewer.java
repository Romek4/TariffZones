package tariffzones.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.BorderUIResource;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.MapClickListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

public class MapViewer extends JXMapViewer {
	private GeoPosition lastClickedGeoPosition;
	private JLabel latLonLabel;
	private List<Painter> painters;
	private JToolTip tooltip;
	
	
	public MapViewer() {
		super();
		initializeListeners();
		this.setLayout(null);
		
		tooltip = new JToolTip();
		this.add(tooltip);
		
		this.add(getLatLonLabel());
		
		this.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println(getHeight());
				getLatLonLabel().setBounds(getWidth()-255, getHeight()-20, 250, 25);
			}
		});
	}

	/**
	 * Initialize listeners for basic map operations.
	 */
	private void initializeListeners() {
		MapClickListener mapClickListener = new MapClickListener(this) {
			@Override
			public void mapClicked(GeoPosition position) {
				lastClickedGeoPosition = position;
			}
		};
		this.addMouseListener(mapClickListener);
		
		MouseInputListener mouseInputListener = new PanMouseInputListener(this) {
			@Override
			public void mousePressed(MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					super.mousePressed(evt);
				}
			}
			@Override
			public void mouseMoved(MouseEvent evt) {
				super.mouseMoved(evt);
				GeoPosition position = getLastMouseGeoPosition(evt.getPoint());
				getLatLonLabel().setText(position.getLatitude() + ", " + position.getLongitude());
			}
		};
		
		this.addMouseListener(mouseInputListener);
		this.addMouseMotionListener(mouseInputListener);
		this.addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));
	}
	
	public GeoPosition getLastClickedGeoPosition() { 
		return lastClickedGeoPosition;
	}
	
	private GeoPosition getLastMouseGeoPosition(Point point) {
		return this.convertPointToGeoPosition(point);
	}
	
	public JLabel getLatLonLabel() {
		if (latLonLabel == null) {
			latLonLabel = new JLabel("");
			latLonLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return latLonLabel;
	}
	
	public JToolTip getToolTip() {
		return tooltip;
	}
}
