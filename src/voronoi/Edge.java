package voronoi;

// an edge on the Voronoi diagram
public class Edge {

	Site start;
	Site end;
	Site leftSite;
	Site rightSite;
	Site direction; // edge is really a vector normal to left and right points
	
	Edge neighbor; // the same edge, but pointing in the opposite direction
	
	private double slope;
	private double yint;
	
	public Edge(Site start, Site end) {
		this.start = start;
		this.end = end;
		slope = (end.x - start.x)/(start.y - end.y);
		yint = (start.y + end.y)/2 - getSlope()*((end.x + start.x)/2);
	}
	
	public Edge (Site start, Site left, Site right) {
		this.start = start;
		leftSite = left;
		rightSite = right;
		direction = new Site(right.getY() - left.getY(), - (right.getX() - left.getX()));
		end = null;		
		slope = (right.x - left.x)/(left.y - right.y);
		yint = (left.y + right.y)/2 - getSlope()*((right.x + left.x)/2);
	}
	
	protected double getSlope() {
		return slope;
	}
	
	protected double getYint() {
		return yint;
	}
	
	public Site getStart() {
		return start;
	}
	
	public Site getEnd() {
		return end;
	}
	
	public String toString() {
		return start + ", " + end;
	}
}
