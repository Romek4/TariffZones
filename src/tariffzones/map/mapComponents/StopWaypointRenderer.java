package tariffzones.map.mapComponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import tariffzones.model.Stop;

public class StopWaypointRenderer implements WaypointRenderer<Stop> {

	private BufferedImage img = null;
	private Color color; 
	private int i = 0;
	
	public StopWaypointRenderer() {
		color = Color.BLACK; //default
	}
	
	public StopWaypointRenderer(Color color) {
		this.color = color;
	}
	
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, Stop waypoint) {

		Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
		if (img == null) {
			Ellipse2D.Double ellipse = new Double();
			ellipse.x = point.getX()-5;
			ellipse.y = point.getY()-5;
			ellipse.width = 10;
			ellipse.height = 10;
			g.setColor(color);
			g.fill(ellipse);
			g.draw(ellipse);
		}
		else {
			int x = (int)point.getX() -img.getWidth() / 2;
			int y = (int)point.getY() -img.getHeight();
			g.drawImage(img, x, y, null);
		}
		
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public void setImg(String filePath) {
		try {
			img = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

}
