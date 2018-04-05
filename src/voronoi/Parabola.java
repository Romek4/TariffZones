package voronoi;

// represents the beach line
// can either be a site that is the center of a parabola
// or can be a vertex that bisects two sites
public class Parabola {
	
	public static int IS_FOCUS = 0;
	public static int IS_VERTEX = 1;
	
	int type;
	Site site; // if is focus
	Edge edge; // if is vertex
	Event event; // a parabola with a focus can disappear in a circle event
	
	Parabola parent;
	Parabola leftChild;
	Parabola rightChild;
	
	public Parabola () {
		type = IS_VERTEX;
	}
	
	public Parabola (Site site) {
		this.site = site;
		type = IS_FOCUS;
	}

	public void setLeftChild (Parabola p) {
		leftChild = p;
		p.parent = this;
	}

	public void setRightChild (Parabola p) {
		rightChild = p;
		p.parent = this;
	}
	
	// returns the closest left site (focus of parabola) 
	public static Parabola getLeft(Parabola p) {
		return getLeftChild(getLeftParent(p));
	}
	
	// returns closest right site (focus of parabola)
	public static Parabola getRight(Parabola p) {
		return getRightChild(getRightParent(p));
	}
	
	// returns the closest parent on the left
	public static Parabola getLeftParent(Parabola p) {
		Parabola parent = p.getParent();
		if (parent == null) {
			return null;
		}
		
		Parabola last = p;
		while (parent.getLeftChild().equals(last)) {
			if(parent.getParent() == null) {
				return null;
			}
			last = parent;
			parent = parent.getParent();
		}
		return parent;
	}
	
	// returns the closest parent on the right
	public static Parabola getRightParent(Parabola p) {
		Parabola parent = p.getParent();
		if (parent == null){
			return null;
		}
		
		Parabola last = p;
		while (parent.getRightChild().equals(last)) {
			if(parent.getParent() == null) {
				return null;
			}
			last = parent;
			parent = parent.getParent();
		}
		return parent;
	}
	
	// returns closest site (focus of another parabola) to the left
	public static Parabola getLeftChild(Parabola p) {
		if (p == null) {
			return null;
		}
		
		Parabola child = p.getLeftChild();
		while(child.type == IS_VERTEX) {
			child = child.getRightChild();
		}
		return child;
	}
	
	// returns closest site (focus of another parabola) to the right
	public static Parabola getRightChild(Parabola p) {
		if (p == null) {
			return null;
		}
		
		Parabola child = p.getRightChild();
		while(child.type == IS_VERTEX) {
			child = child.getLeftChild();
		}
		return child;	
	}
	
	public Parabola getParent() {
		return parent;
	}
	
	public Parabola getLeftChild() {
		return leftChild;
	}
	
	public Parabola getRightChild() {
		return rightChild;
	}
	
	public int getType() {
		return type;
	}
	
	public Site getSite() {
		return site;
	}
	
	public Edge getEdge() {
		return edge;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
