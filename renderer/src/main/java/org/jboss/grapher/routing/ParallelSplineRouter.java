/**
 * User: alesj
 * Date: 2004.12.1
 * Time: 9:31:22
 */

package org.jboss.grapher.routing;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Paralled edge routing.
 * Taken from JGraphPad.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ParallelSplineRouter extends DefaultEdge.LoopRouting {
    protected GraphModel emptyModel = new DefaultGraphModel();

    /**
     * The distance between the control point and the middle line. A larger
     * number will lead to a more "bubbly" appearance of the bezier edges.
     */
    private double edgeSeparation = 25;

    /**
     * Returns the array of parallel edges.
     *
     * @param edge the edge view
     * @return cells
     */
    public Object[] getParallelEdges(EdgeView edge) {
        // FIXME: The model is stored in the cells only in the default
        // implementations. Otherwise we must use the real model here.
        return DefaultGraphModel.getEdgesBetween(emptyModel, edge.getSource()
                .getParentView().getCell(), edge.getTarget().getParentView()
                .getCell(), false);
    }

    @SuppressWarnings("unchecked")
    public List routeEdge(GraphLayoutCache cache, EdgeView edge) {
        List newPoints = new ArrayList();

        // Check presence of source/target nodes
        if ((null == edge.getSource()) || (null == edge.getTarget())
                || (null == edge.getSource().getParentView())
                || (null == edge.getTarget().getParentView())) {
            return null;
        }
        newPoints.add(edge.getSource());

        Object[] edges = getParallelEdges(edge);
        // Find the position of the current edge that we are currently routing
        if (edges == null)
            return null;
        int position = 0;
        for (int i = 0; i < edges.length; i++) {
            Object e = edges[i];
            if (e == edge.getCell()) {
                position = i;
            }
        }

        // If there is only 1 edge between the two vertices, we don't need this
        // special routing
        if (edges.length >= 2) {

            // Find the end point positions
            Point2D from = ((PortView) edge.getSource()).getLocation();
            Point2D to = ((PortView) edge.getTarget()).getLocation();

            if (from != null && to != null) {
                // calculate mid-point of the main edge
                double midX = Math.min(from.getX(), to.getX())
                        + Math.abs((from.getX() - to.getX()) / 2);
                double midY = Math.min(from.getY(), to.getY())
                        + Math.abs((from.getY() - to.getY()) / 2);

                // compute the normal slope. The normal of a slope is the
                // negative
                // inverse of the original slope.
                double m = (from.getY() - to.getY())
                        / (from.getX() - to.getX());
                double theta = Math.atan(-1 / m);

                // modify the location of the control point along the axis of
                // the
                // normal using the edge position
                double r = edgeSeparation * (Math.floor(position / 2) + 1);
                if ((position % 2) == 0) {
                    r = -r;
                }

                // convert polar coordinates to cartesian and translate axis to
                // the
                // mid-point
                double ex = r * Math.cos(theta) + midX;
                double ey = r * Math.sin(theta) + midY;
                Point2D controlPoint = new Point2D.Double(ex, ey);

                // add the control point to the points list
                newPoints.add(controlPoint);
            }
        }
        newPoints.add(edge.getTarget());
        return newPoints;
    }

    public int getEdgeStyle() {
        return GraphConstants.STYLE_SPLINE;
    }

    /**
     * @return Returns the edgeSeparation.
     */
    public double getEdgeSeparation() {
        return edgeSeparation;
    }

    /**
     * @param edgeSeparation The edgeSeparation to set.
     */
    public void setEdgeSeparation(double edgeSeparation) {
        this.edgeSeparation = edgeSeparation;
    }
}