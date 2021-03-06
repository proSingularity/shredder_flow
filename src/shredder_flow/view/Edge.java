package shredder_flow.view;

import shredder_flow.logic.Vertex;
import de.jtem.java2dx.Line2DDouble;

/**
 * Extention of awt class. Therefore this is for use in the view.
 */
class Edge extends Line2DDouble {

	private static final long serialVersionUID = 1L;

	public Edge(Vertex vertex1, Vertex vertex2) {
		super(vertex1.getX(), vertex1.getY(), vertex2.getX(), vertex2.getY());
	}
}
