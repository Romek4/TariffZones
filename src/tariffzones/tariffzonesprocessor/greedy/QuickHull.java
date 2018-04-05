package tariffzones.tariffzonesprocessor.greedy;
import java.util.ArrayList;

import org.jxmapviewer.viewer.GeoPosition;

public class QuickHull {
     
    public int pointLocation(GeoPosition A, GeoPosition B, GeoPosition P) {

    	double cp1 = (B.getLatitude()-A.getLatitude())*(P.getLongitude()-A.getLongitude()) - (B.getLongitude()-A.getLongitude())*(P.getLatitude()-A.getLatitude());
	    return (cp1>0) ? -1 : 1; 
                         
    }
     
 
    public double distance(GeoPosition A, GeoPosition B, GeoPosition C) {
    double ABx = B.getLatitude()-A.getLatitude();
    double ABy = B.getLongitude()-A.getLongitude();
    double num = ABx*(A.getLongitude()-C.getLongitude()-ABy*(A.getLatitude()-C.getLatitude()));
    if (num < 0) 
        num = -num;
    return num;
     
    }
     
    public ArrayList<GeoPosition> quickHull(ArrayList<GeoPosition> points) {
    ArrayList<GeoPosition> convexHull = new ArrayList<GeoPosition>();
    if (points.size() < 3) return (ArrayList)points.clone();
 
    int minPoint = -1, maxPoint = -1;
    double minX = Double.MAX_VALUE;
    double maxX = Double.MIN_VALUE;
    for (int i = 0; i < points.size(); i++) {
      if (points.get(i).getLatitude() < minX) {
        minX = points.get(i).getLatitude();
        minPoint = i;
      } 
      if (points.get(i).getLatitude() > maxX) {
        maxX = points.get(i).getLatitude();
        maxPoint = i;       
      }
    }
     
    GeoPosition A = points.get(minPoint);
    GeoPosition B = points.get(maxPoint);
    convexHull.add(A);
    convexHull.add(B);
    points.remove(A);
    points.remove(B);
     
    ArrayList<GeoPosition> leftSet = new ArrayList<GeoPosition>();
    ArrayList<GeoPosition> rightSet = new ArrayList<GeoPosition>();
     
    for (int i = 0; i < points.size(); i++) {
    	GeoPosition p = points.get(i);
      if (pointLocation(A,B,p) == -1)
        leftSet.add(p);
      else
        rightSet.add(p);
    }
     
    hullSet(A,B,rightSet,convexHull);
    hullSet(B,A,leftSet,convexHull);
     
    return convexHull;
  }
   
  public void hullSet(GeoPosition A, GeoPosition B, ArrayList<GeoPosition> set, ArrayList<GeoPosition> hull) {
    int insertPosition = hull.indexOf(B);
    if (set.size() == 0) return;
    if (set.size() == 1) {
    	GeoPosition p = set.get(0);
      set.remove(p);
      hull.add(insertPosition,p);
      return;
    }
     
    double dist = Double.MIN_VALUE;
    int furthestPoint = -1;
    for (int i = 0; i < set.size(); i++) {
    	GeoPosition p = set.get(i);
         double distance  = distance(A,B,p);
         if (distance > dist) {
             dist = distance;
             furthestPoint = i;
         }
    }
     
    if (furthestPoint == -1) {
		return;
	}
    
    GeoPosition P = set.get(furthestPoint);
    set.remove(furthestPoint);
    hull.add(insertPosition,P);
     
    ArrayList<GeoPosition> leftSetAP = new ArrayList<GeoPosition>();
    for (int i = 0; i < set.size(); i++) {
    	GeoPosition M = set.get(i);
         if (pointLocation(A,P,M)==1) { 
             leftSetAP.add(M);
         }
    }
     
    ArrayList<GeoPosition> leftSetPB = new ArrayList<GeoPosition>();
    for (int i = 0; i < set.size(); i++) {
    	GeoPosition M = set.get(i);
         if (pointLocation(P,B,M)==1) { 
             leftSetPB.add(M);
         }
    }
     
    hullSet(A,P,leftSetAP,hull);
    hullSet(P,B,leftSetPB,hull);
  }
}
