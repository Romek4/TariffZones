package voronoi;

import java.util.HashSet;
import java.util.Set;

// a point in 2D, sorted by y-coordinate
public class Site implements Comparable <Site>{
	
	Set<Site> regionSites; //to remember vertices associated with site - just for non-vertex sites - used for region painting
	
	double x;
	double y;
	
	public Site (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public int compareTo (Site other) {
		if (this.y == other.y) {
			if (this.x == other.x) return 0;
			else if (this.x > other.x) return 1;
			else return -1;
		}
		else if (this.y > other.y) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	public Set<Site> getRegionSites() {
		if (regionSites == null) {
			regionSites = new HashSet<>();
		}
		
		return regionSites;
	}
	
	public void addRegionSite(Site site) {
		getRegionSites().add(site);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

}
