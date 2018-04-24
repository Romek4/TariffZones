package tariffzones.processor.voronoi;

// an event is either a site or circle event for the sweep line to process
public class Event implements Comparable <Event>{
	
	// a site event is when the point is a site
	public static int SITE_EVENT = 0;
	
	// a circle event is when the point is a vertex of the voronoi diagram/parabolas
	public static int CIRCLE_EVENT = 1;
	
	Site p;
	int type;
	Parabola arc; // only if circle event
	
	public Event (Site p, int type) {
		this.p = p;
		this.type = type;
		arc = null;
	}
	
	public int compareTo(Event other) {
		return this.p.compareTo(other.p);
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

}
