package voronoi;

// the voronoi diagram (a set of edges) for a set of points (sites)

import java.util.List;
import java.util.PriorityQueue;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import tariffzones.model.Stop;

public class Voronoi {
	
	private double borderMinX;
    private double borderMaxX;
    private double borderMinY;
    private double borderMaxY;
    
    public Edge westBorder;
    public Edge eastBorder;
    public Edge northBorder;
    public Edge southBorder;
    
    Rectangle2D boundingBox;
    
    public List <Site> sites;
	public List <Edge> edges; // edges on Voronoi diagram
	PriorityQueue<Event> events; // priority queue represents sweep line
	Parabola root; // binary search tree represents beach line
	
	double width = 1;
	double height = 1;
	
	double ycurr; // current y-coord of sweep line
	
	
	public Voronoi(List <Site> sites) {
		this.sites = sites;
		edges = new ArrayList<Edge>();
	}
	
	public Voronoi (List <Site> sites, double west, double east, double north, double south) {
		this.sites = sites;
		this.borderMinX = roundDouble(south-0.05);
		this.borderMaxX = roundDouble(north+0.05);
		this.borderMinY = roundDouble(west-0.05);
		this.borderMaxY = roundDouble(east+0.05);
		westBorder = new Edge(new Site(borderMinX, borderMinY), new Site(borderMaxX, borderMinY));
		eastBorder = new Edge(new Site(borderMinX, borderMaxY), new Site(borderMaxX, borderMaxY));
		northBorder = new Edge(new Site(borderMaxX, borderMinY), new Site(borderMaxX, borderMaxY));
		southBorder = new Edge(new Site(borderMinX, borderMinY), new Site(borderMinX, borderMaxY));
		edges = new ArrayList<Edge>();
		
//		boundingBox = new Rectangle2D.Double(borderMinX, borderMinY, borderMaxY-borderMinY, borderMaxX-borderMinX);
	}

	public List <Edge> generateVoronoi() {
		
		events = new PriorityQueue<Event>();
		for (Site s : sites) {
			events.add(new Event(s, Event.SITE_EVENT));
		}
		
		while (!events.isEmpty()) {
			Event e = events.remove();
			ycurr = e.p.y;
			if (e.getType() == Event.SITE_EVENT) {
				handleSite(e.p);
			}
			else {
				handleCircle(e);
			}
		}
		
		ycurr = width+height;
		
		endEdges(root); // close off any dangling edges
		
		// get rid of those crazy infinite lines
		for (Edge e: edges){

			if (e.neighbor != null) {
				e.start = e.neighbor.end;
				e.neighbor = null;
			}
			
			if ((!isInBorders(e.start) && isInBorders(e.end)) || (isInBorders(e.start) && !isInBorders(e.end))) {
				checkSiteBorders(e);
			}
			
			
			if (isInBorders(e.start)) {
				e.leftSite.addRegionSite(e.getStart());
				e.rightSite.addRegionSite(e.getStart());
			}
			
			if (isInBorders(e.end)) {
				e.leftSite.addRegionSite(e.getEnd());
				e.rightSite.addRegionSite(e.getEnd());
			}
			
//			e.leftSite.addRegionSite(e.getStart());
//			e.leftSite.addRegionSite(e.getEnd());
//			e.rightSite.addRegionSite(e.getStart());
//			e.rightSite.addRegionSite(e.getEnd());
		}
		

		edges.add(westBorder);
		edges.add(eastBorder);
		edges.add(northBorder);
		edges.add(southBorder);
		
		return edges;
	}

	// end all unfinished edges
	private void endEdges(Parabola p) {
		if (p.getType() == Parabola.IS_FOCUS) {
			p = null;
			return;
		}

		double x = getXofEdge(p);
		double y = p.getEdge().getSlope()*x+p.getEdge().getYint();

		p.getEdge().end = new Site (roundDouble(x), roundDouble(y));
		checkSiteBorders(p.getEdge());
		
		edges.add(p.getEdge());
		
		endEdges(p.getLeftChild());
		endEdges(p.getRightChild());
		
		p = null;
	}
	
	// processes site event
	private void handleSite(Site p) {
		// base case
		if (root == null) {
			root = new Parabola(p);
			return;
		}
		
		// find parabola on beach line right above p
		Parabola par = getParabolaByX(p.x);
		if (par.getEvent() != null) {
			events.remove(par.getEvent());
			par.event = null;
		}

		// create new dangling edge; bisects parabola focus and p
		Site start = new Site(p.x, getY(par.getSite(), p.x));
		Edge el = new Edge(start, par.getSite(), p);
		Edge er = new Edge(start, p, par.getSite());
		el.neighbor = er;
		er.neighbor = el;
		par.edge = el;
		par.type = Parabola.IS_VERTEX;
		
		// replace original parabola par with p0, p1, p2
		Parabola p0 = new Parabola (par.getSite());
		Parabola p1 = new Parabola (p);
		Parabola p2 = new Parabola (par.getSite());

		par.setLeftChild(p0);
		par.setRightChild(new Parabola());
		par.getRightChild().edge = er;
		par.getRightChild().setLeftChild(p1);
		par.getRightChild().setRightChild(p2);

		checkCircleEvent(p0);
		checkCircleEvent(p2);
	}
	
	// process circle event
	private void handleCircle(Event e) {
		
		// find p0, p1, p2 that generate this event from left to right
		Parabola p1 = e.arc;
		Parabola xl = Parabola.getLeftParent(p1);
		Parabola xr = Parabola.getRightParent(p1);
		Parabola p0 = Parabola.getLeftChild(xl);
		Parabola p2 = Parabola.getRightChild(xr);
		
		// remove associated events since the points will be altered
		if (p0.getEvent() != null) {
			events.remove(p0.getEvent());
			p0.event = null;
		}
		if (p2.getEvent() != null) {
			events.remove(p2.getEvent());
			p2.event = null;
		}
		
		Site site = new Site(e.p.x, getY(p1.getSite(), e.p.x)); // new vertex
		
		// end edges!
		xl.getEdge().end = site;
		xr.getEdge().end = site;
		
		checkSiteBorders(xl.getEdge());
		checkSiteBorders(xr.getEdge());
		
		edges.add(xl.getEdge());
		edges.add(xr.getEdge());

		// start new bisector (edge) from this vertex on which ever original edge is higher in tree
		Parabola higher = new Parabola();
		Parabola par = p1;
		while (par != root) {
			par = par.parent;
			if (par == xl) higher = xl;
			if (par == xr) higher = xr;
		}
		higher.setEdge(new Edge(site, p0.getSite(), p2.getSite()));
		
		// delete p1 and parent (boundary edge) from beach line
		Parabola gparent = p1.parent.parent;
		if (p1.parent.getLeftChild() == p1) {
			if(gparent.getLeftChild()  == p1.parent) gparent.setLeftChild(p1.parent.getRightChild());
			if(gparent.getRightChild() == p1.parent) gparent.setRightChild(p1.parent.getRightChild());
		}
		else {
			if(gparent.getLeftChild()  == p1.parent) gparent.setLeftChild(p1.parent.getLeftChild());
			if(gparent.getRightChild() == p1.parent) gparent.setRightChild(p1.parent.getLeftChild());
		}

		p1 = null;
		
		checkCircleEvent(p0);
		checkCircleEvent(p2);
	}
	
	// adds circle event if foci a, b, c lie on the same circle
	private void checkCircleEvent(Parabola b) {

		Parabola lp = Parabola.getLeftParent(b);
		Parabola rp = Parabola.getRightParent(b);

		if (lp == null || rp == null) return;
		
		Parabola a = Parabola.getLeftChild(lp);
		Parabola c = Parabola.getRightChild(rp);
	
		if (a == null || c == null || a.getSite() == c.getSite()) return;

		if (ccw(a.getSite(),b.getSite(),c.getSite()) != 1) return;
		
		// edges will intersect to form a vertex for a circle event
		Site start = getEdgeIntersection(lp.getEdge(), rp.getEdge());
		if (start == null) return;
		
		// compute radius
		double dx = b.getSite().x - start.x;
		double dy = b.getSite().y - start.y;
		double d = Math.sqrt((dx*dx) + (dy*dy));
		if (start.y + d < ycurr) return; // must be after sweep line

		Site ep = new Site(start.x, start.y + d);
		//System.out.println("added circle event "+ ep);

		// add circle event
		Event e = new Event (ep, Event.CIRCLE_EVENT);
		e.arc = b;
		b.setEvent(e);
		events.add(e);
	}

	private void checkSiteBorders(Edge e) {
		
		Site borderSite;
		if ((borderSite = intersects(e, westBorder)) != null) {
			if (isInBorders(e.start)) {
				e.end = borderSite;
			}
			else {
				e.start = borderSite;
			}
		}
		else if ((borderSite = intersects(e, eastBorder)) != null) {
			if (isInBorders(e.start)) {
				e.end = borderSite;
			}
			else {
				e.start = borderSite;
			}
		}
		else if ((borderSite = intersects(e, northBorder)) != null) {
			if (isInBorders(e.start)) {
				e.end = borderSite;
			}
			else {
				e.start = borderSite;
			}
		}
		else if ((borderSite = intersects(e, southBorder)) != null) {
			if (isInBorders(e.start)) {
				e.end = borderSite;
			}
			else {
				e.start = borderSite;
			}
		}
		
	}
	
	private boolean isInBorders(Site site) {
		if (site.x > borderMaxX || site.x < borderMinX || site.y > borderMaxY || site.y < borderMinY) {
			return false;
		}
		return true;
	}
	
	private Site intersects(Edge e1, Edge e2) {
        Site start1 = e1.start;
        Site end1 = e1.end;
        Site start2 = e2.start;
        Site end2 = e2.end;
        // First find Ax+By=C values for the two lines
        double A1 = end1.y - start1.y;
        double B1 = start1.x - end1.x;
        double C1 = A1 * start1.x + B1 * start1.y;

        double A2 = end2.y - start2.y;
        double B2 = start2.x - end2.x;
        double C2 = A2 * start2.x + B2 * start2.y;

        double det = (A1 * B2) - (A2 * B1);
        
        if (det != 0) {
            // Lines DO intersect somewhere, but do the line segments intersect?
            double x = roundDouble((B2 * C1 - B1 * C2) / det);
            double y = roundDouble((A1 * C2 - A2 * C1) / det);

            // Make sure that the intersection is within the bounding box of
            // both segments
            if ((x >= roundDouble(Math.min(start1.x, end1.x)) && x <= roundDouble(Math.max(start1.x, end1.x)))
                    && (y >= roundDouble(Math.min(start1.y, end1.y)) && y <= roundDouble(Math.max(start1.y, end1.y)))) {
                // We are within the bounding box of the first line segment,
                // so now check second line segment
                if ((x >= roundDouble(Math.min(start2.x, end2.x)) && x <= roundDouble(Math.max(start2.x, end2.x)))
                        && (y >= roundDouble(Math.min(start2.y, end2.y)) && y <= roundDouble(Math.max(start2.y, end2.y)))) {
                    // The line segments do intersect
                    return new Site(x, y);
                }
            }
        }
        
        // The lines do intersect, but the line segments do not
        return null;
    }

	// first thing we learned in this class :P
	public int ccw(Site a, Site b, Site c) {
        double area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if (area2 < 0) {
        	return -1;
        }
        else if (area2 > 0) {
        	return 1;
    	}
        else {
        	return  0;
    	}
    }

	// returns intersection of the lines of with vectors a and b
	private Site getEdgeIntersection(Edge a, Edge b) {

		if (b.getSlope() == a.getSlope() && b.getYint() != a.getYint()) return null;

		double x = (b.getYint() - a.getYint())/(a.getSlope() - b.getSlope());
		double y = a.getSlope()*x + a.getYint();

		return new Site(roundDouble(x), roundDouble(y));
	} 
	
	// returns current x-coordinate of an unfinished edge
	private double getXofEdge (Parabola par) {
		//find intersection of two parabolas
		
		Parabola left = Parabola.getLeftChild(par);
		Parabola right = Parabola.getRightChild(par);
		
		Site p = left.getSite();
		Site r = right.getSite();
		
		double dp = 2*(p.y - ycurr);
		double a1 = 1/dp;
		double b1 = -2*p.x/dp;
		double c1 = (p.x*p.x + p.y*p.y - ycurr*ycurr)/dp;
		
		double dp2 = 2*(r.y - ycurr);
		double a2 = 1/dp2;
		double b2 = -2*r.x/dp2;
		double c2 = (r.x*r.x + r.y*r.y - ycurr*ycurr)/dp2;
		
		double a = a1-a2;
		double b = b1-b2;
		double c = c1-c2;
		
		double disc = b*b - 4*a*c;
		double x1 = (-b + Math.sqrt(disc))/(2*a);
		double x2 = (-b - Math.sqrt(disc))/(2*a);
		
		double ry;
		if (p.y > r.y) ry = Math.max(x1, x2);
		else ry = Math.min(x1, x2);
		
		return ry;
	}
	
	// returns parabola above this x coordinate in the beach line
	private Parabola getParabolaByX (double xx) {
		Parabola par = root;
		double x = 0;
		while (par.getType() == Parabola.IS_VERTEX) {
			x = getXofEdge(par);
			if (x > xx) par = par.getLeftChild();
			else par = par.getRightChild();
		}
		return par;
	}
	
	// find corresponding y-coordinate to x on parabola with focus p
	private double getY(Site p, double x) {
		// determine equation for parabola around focus p
		double dp = 2*(p.y - ycurr);
		double a1 = 1/dp;
		double b1 = -2*p.x/dp;
		double c1 = (p.x*p.x + p.y*p.y - ycurr*ycurr)/dp;
		return roundDouble((a1*x*x + b1*x + c1));
	}
	
	private double roundDouble(double d) {
		return(double)Math.round(d * 100000000000d) / 100000000000d;
	}
	
}
