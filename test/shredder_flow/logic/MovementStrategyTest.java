package shredder_flow.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MovementStrategyTest {

	private Vertex vertex0;
	private Vertex vertex1;
	private Vertex vertex2;
	private Vertex vertex3;
	private TriangleList triangles;
	private Triangle leftTriangle;
	private Triangle rightTriangle;
	
	@Before
	public void setUp() throws Exception {
		vertex0 = new Vertex(-1, 0);
		vertex1 = new Vertex(0, -1);
		vertex2 = new Vertex(0, 1);
		vertex3 = new Vertex(1, 0);
		leftTriangle = createTriangle(vertex0, vertex1, vertex2);
		rightTriangle = createTriangle(vertex1, vertex2, vertex3);
		triangles = new TriangleList();
		triangles.add(leftTriangle);
		triangles.add(rightTriangle);
	}

	@Test
	public void testParticleUpdateOneStepInSingleTriangle() throws Exception {
		TriangulationVertexList verticesForBigTriangle = new TriangulationVertexList();
		verticesForBigTriangle.add(new Vertex(10000, 0));
		verticesForBigTriangle.add(new Vertex(0, 10000));
		verticesForBigTriangle.add(new Vertex(-10000, -10000));
		Triangle bigTriangle = new Triangle(verticesForBigTriangle);
		TriangleList bigTriangleList = new TriangleList();
		bigTriangleList.add(bigTriangle);
		ParticleList particleListForBigTriangle = new ParticleList();
		ParticleCreator creatorForBigTriangle = new ParticleCreator(
				particleListForBigTriangle, bigTriangleList);
		creatorForBigTriangle.addParticle(0, 0);
		Particle particle = particleListForBigTriangle.get(0);

		bigTriangle.getFieldVector().set(800.324, 123.42);
		particle.update(1);
		assertPositionEquals(particle, 800.324, 123.42);
	}

	@Test
	public void testParticleMoveOverTwoTrianglesWithoutTouchingEdge()
			throws Exception {
		leftTriangle.getFieldVector().set(1, 0);
		rightTriangle.getFieldVector().set(1.0 / 2, 0);

		Particle particle = new Particle(-1.0 / 2, 0, leftTriangle,
				new MovementStrategy(triangles));

		updateThreeTimesAndAssert(leftTriangle, particle);
		updateAndAssert(rightTriangle, particle, 1.0 / 2, 1.0 / 4, 0);
	}

	@Test
	public void testParticleMoveOverTwoTrianglesWithCollidingFieldVectors()
			throws Exception {
		leftTriangle.getFieldVector().set(1, 0);
		rightTriangle.getFieldVector().set(-1, 0);

		Particle particle = new Particle(-1.0 / 2, 0, leftTriangle,
				new MovementStrategy(triangles));

		updateThreeTimesAndAssert(leftTriangle, particle);
		updateAndAssert(rightTriangle, particle, 1.0 / 2, 0, 0);

		for (int i = 0; i < 1000; i++) {
			particle.update(1);
			assertPositionEquals(particle, 0, 0);
		}
		assertPositionEquals(particle, 0, 0);
	}

	private void assertPositionEquals(Particle particle, double expectedX,
			double expectedY) {
		assertEquals(expectedX, particle.getX(), 0);
		assertEquals(expectedY, particle.getY(), 0);
	}

	private void updateThreeTimesAndAssert(Triangle leftTriangle,
			Particle particle) {
		updateAndAssert(leftTriangle, particle, 1.0 / 4, -1.0 / 4, 0);
		updateAndAssert(leftTriangle, particle, 0, -1.0 / 4, 0);
		updateAndAssert(leftTriangle, particle, 1.0 / 4, 0, 0);
	}

	private void updateAndAssert(Triangle leftTriangle, Particle particle,
			double deltaT, double expectedX, int expectedY) {
		particle.update(deltaT);
		assertPositionEquals(particle, expectedX, expectedY);
		assertEquals(leftTriangle, particle.getTriangle());
	}

	private Triangle createTriangle(Vertex vertex0, Vertex vertex1,
			Vertex vertex2) {
		TriangulationVertexList verticesForLeftTriangle = new TriangulationVertexList();
		verticesForLeftTriangle.add(vertex0);
		verticesForLeftTriangle.add(vertex1);
		verticesForLeftTriangle.add(vertex2);
		Triangle leftTriangle = new Triangle(verticesForLeftTriangle);
		return leftTriangle;
	}
}
