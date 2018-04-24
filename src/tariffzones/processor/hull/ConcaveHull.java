package tariffzones.processor.hull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import org.jxmapviewer.viewer.GeoPosition;

public class ConcaveHull {
	
	private Double euclideanDistance(GeoPosition a, GeoPosition b) {
        return Math.sqrt(Math.pow(a.getLatitude() - b.getLatitude(), 2) + Math.pow(a.getLongitude() - b.getLongitude(), 2));
    }

    private ArrayList<GeoPosition> kNearestNeighbors(ArrayList<GeoPosition> l, GeoPosition q, Integer k) {
        ArrayList<Pair<Double, GeoPosition>> nearestList = new ArrayList<>();
        for (GeoPosition o : l) {
            nearestList.add(new Pair<>(euclideanDistance(q, o), o));
        }

        Collections.sort(nearestList, new Comparator<Pair<Double, GeoPosition>>() {
            @Override
            public int compare(Pair<Double, GeoPosition> o1, Pair<Double, GeoPosition> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        ArrayList<GeoPosition> result = new ArrayList<>();

        for (int i = 0; i < Math.min(k, nearestList.size()); i++) {
            result.add(nearestList.get(i).getValue());
        }

        return result;
    }

    private GeoPosition findMinYPoint(ArrayList<GeoPosition> l) {
        Collections.sort(l, new Comparator<GeoPosition>() {
            @Override
            public int compare(GeoPosition o1, GeoPosition o2) {
            	if (o1.getLongitude() < o2.getLongitude()) {
					return -1;
				}
            	else if (o1.getLongitude() > o2.getLongitude()) {
					return 1;
				}
                return 0;
            }
        });
        return l.get(0);
    }

    private Double calculateAngle(GeoPosition o1, GeoPosition o2) {
        return Math.atan2(o2.getLongitude() - o1.getLongitude(), o2.getLatitude() - o1.getLatitude());
    }

    private Double angleDifference(Double a1, Double a2) {
        // calculate angle difference in clockwise directions as radians
        if ((a1 > 0 && a2 >= 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if ((a1 >= 0 && a2 > 0) && a1 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if ((a1 < 0 && a2 <= 0) && a1 < a2) {
            return 2 * Math.PI + a1 + Math.abs(a2);
        } else if ((a1 <= 0 && a2 < 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if (a1 <= 0 && 0 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if (a1 >= 0 && 0 >= a2) {
            return a1 + Math.abs(a2);
        } else {
            return 0.0;
        }
    }

    private ArrayList<GeoPosition> sortByAngle(ArrayList<GeoPosition> l, GeoPosition q, Double a) {
        // Sort by angle descending
        Collections.sort(l, new Comparator<GeoPosition>() {
            @Override
            public int compare(final GeoPosition o1, final GeoPosition o2) {
                Double a1 = angleDifference(a, calculateAngle(q, o1));
                Double a2 = angleDifference(a, calculateAngle(q, o2));
                return a2.compareTo(a1);
            }
        });
        return l;
    }

    private Boolean intersect(GeoPosition l1p1, GeoPosition l1p2, GeoPosition l2p1, GeoPosition l2p2) {
        // calculate part equations for line-line intersection
        Double a1 = l1p2.getLongitude() - l1p1.getLongitude();
        Double b1 = l1p1.getLatitude() - l1p2.getLatitude();
        Double c1 = a1 * l1p1.getLatitude() + b1 * l1p1.getLongitude();
        Double a2 = l2p2.getLongitude() - l2p1.getLongitude();
        Double b2 = l2p1.getLatitude() - l2p2.getLatitude();
        Double c2 = a2 * l2p1.getLatitude() + b2 * l2p1.getLongitude();
        // calculate the divisor
        Double tmp = (a1 * b2 - a2 * b1);

        // calculate intersection point x coordinate
        Double pX = (c1 * b2 - c2 * b1) / tmp;

        // check if intersection x coordinate lies in line line segment
        if ((pX > l1p1.getLatitude() && pX > l1p2.getLatitude()) || (pX > l2p1.getLatitude() && pX > l2p2.getLatitude())
                || (pX < l1p1.getLatitude() && pX < l1p2.getLatitude()) || (pX < l2p1.getLatitude() && pX < l2p2.getLatitude())) {
            return false;
        }

        // calculate intersection point y coordinate
        Double pY = (a1 * c2 - a2 * c1) / tmp;

        // check if intersection y coordinate lies in line line segment
        if ((pY > l1p1.getLongitude() && pY > l1p2.getLongitude()) || (pY > l2p1.getLongitude() && pY > l2p2.getLongitude())
                || (pY < l1p1.getLongitude() && pY < l1p2.getLongitude()) || (pY < l2p1.getLongitude() && pY < l2p2.getLongitude())) {
            return false;
        }

        return true;
    }

    private boolean pointInPolygon(GeoPosition p, ArrayList<GeoPosition> pp) {
        boolean result = false;
        for (int i = 0, j = pp.size() - 1; i < pp.size(); j = i++) {
            if ((pp.get(i).getLongitude() > p.getLongitude()) != (pp.get(j).getLongitude() > p.getLongitude()) &&
                    (p.getLatitude() < (pp.get(j).getLatitude() - pp.get(i).getLatitude()) * (p.getLongitude() - pp.get(i).getLongitude()) / (pp.get(j).getLongitude() - pp.get(i).getLongitude()) + pp.get(i).getLatitude())) {
                result = !result;
            }
        }
        return result;
    }

    public ConcaveHull() {

    }

    public ArrayList<GeoPosition> calculateConcaveHull(ArrayList<GeoPosition> pointArrayList, Integer k) {

        // the resulting concave hull
        ArrayList<GeoPosition> concaveHull = new ArrayList<>();

        // optional remove duplicates
        HashSet<GeoPosition> set = new HashSet<>(pointArrayList);
        ArrayList<GeoPosition> pointArraySet = new ArrayList<>(set);

        // k has to be greater than 3 to execute the algorithm
        Integer kk = Math.max(k, 3);

        // return Points if already Concave Hull
        if (pointArraySet.size() < 3) {
            return pointArraySet;
        }

        // make sure that k neighbors can be found
        kk = Math.min(kk, pointArraySet.size() - 1);

        // find first point and remove from point list
        GeoPosition firstPoint = findMinYPoint(pointArraySet);
        concaveHull.add(firstPoint);
        GeoPosition currentPoint = firstPoint;
        pointArraySet.remove(firstPoint);

        Double previousAngle = 0.0;
        Integer step = 2;

        while ((currentPoint != firstPoint || step == 2) && pointArraySet.size() > 0) {

            // after 3 steps add first point to dataset, otherwise hull cannot be closed
            if (step == 5) {
                pointArraySet.add(firstPoint);
            }

            // get k nearest neighbors of current point
            ArrayList<GeoPosition> kNearestPoints = kNearestNeighbors(pointArraySet, currentPoint, kk);

            // sort points by angle clockwise
            ArrayList<GeoPosition> clockwisePoints = sortByAngle(kNearestPoints, currentPoint, previousAngle);

            // check if clockwise angle nearest neighbors are candidates for concave hull
            Boolean its = true;
            int i = -1;
            while (its && i < clockwisePoints.size() - 1) {
                i++;

                int lastPoint = 0;
                if (clockwisePoints.get(i) == firstPoint) {
                    lastPoint = 1;
                }

                // check if possible new concave hull point intersects with others
                int j = 2;
                its = false;
                while (!its && j < concaveHull.size() - lastPoint) {
                    its = intersect(concaveHull.get(step - 2), clockwisePoints.get(i), concaveHull.get(step - 2 - j), concaveHull.get(step - 1 - j));
                    j++;
                }
            }

            // if there is no candidate increase k - try again
            if (its) {
                return calculateConcaveHull(pointArrayList, k + 1);
            }

            // add candidate to concave hull and remove from dataset
            currentPoint = clockwisePoints.get(i);
            concaveHull.add(currentPoint);
            pointArraySet.remove(currentPoint);

            // calculate last angle of the concave hull line
            previousAngle = calculateAngle(concaveHull.get(step - 1), concaveHull.get(step - 2));

            step++;

        }

        // Check if all points are contained in the concave hull
        Boolean insideCheck = true;
        int i = pointArraySet.size() - 1;

        while (insideCheck && i > 0) {
            insideCheck = pointInPolygon(pointArraySet.get(i), concaveHull);
            i--;
        }

        // if not all points inside -  try again
        if (!insideCheck) {
            return calculateConcaveHull(pointArrayList, k + 1);
        } else {
            return concaveHull;
        }

    }

}
