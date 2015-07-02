package shredder_flow.logic;

import java.util.Random;

public class FunctionGenerator {

	private TriangulationVertexList vertices;

	public FunctionGenerator(TriangulationVertexList vertexList) {
		this.vertices = vertexList;
	}

	public void generate() {
		// TODO: implement
		// The task is to provide different generate()
		// methods that can be called from the FunctionGeneratorInvoker, e.g.
		// generateRandomValues(), generateNormOfVertex(),
		// generateblablub()
		// ...
	}

	public void generateRandomFunction(double rangeMin, double rangeMax) {
		Random r = new Random();
		for (Vertex vertex : vertices) {
			double randomValue = rangeMin + (rangeMax - rangeMin)
					* r.nextDouble();
			vertex.setFunctionValue(randomValue);
		}

	}
}
