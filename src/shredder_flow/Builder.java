package shredder_flow;

import javax.swing.Timer;

import shredder_flow.logic.DraggablePolygon2DAdapter;
import shredder_flow.logic.FunctionGenerator;
import shredder_flow.logic.MeshModel;
import shredder_flow.logic.ParticleCreator;
import shredder_flow.logic.ParticleList;
import shredder_flow.logic.ParticleUpdater;
import shredder_flow.logic.TriangleList;
import shredder_flow.logic.TriangulationVertexList;
import shredder_flow.logic.VectorFieldGenerator;
import shredder_flow.view.FunctionGeneratorInvoker;
import shredder_flow.view.MeshPlugin;
import shredder_flow.view.ParticleAdderPanel;
import shredder_flow.view.ParticlePlugin;
import shredder_flow.view.ParticleUpdateInvoker;
import shredder_flow.view.TriangulationInvoker;
import shredder_flow.view.VectorFieldGeneratorInvoker;
import de.jtem.discreteCurves.SubdividedPolygonPlugin;
import de.jtem.java2dx.plugin.Java2DViewer;

public class Builder {

	/**
	 * Completely wires up the logic and view of this project.
	 * 
	 * @param viewer
	 *            Viewer in which everything will be visualized.
	 */
	public void buildAndRegister(Java2DViewer viewer) {
		int UPDATES_PER_SECOND = 60;
		
		Autoredrawer autoredrawer = new Autoredrawer();
		Timer redrawTimer = new Timer(1000/UPDATES_PER_SECOND, autoredrawer);

		ParticleList particles = new ParticleList();
		TriangleList triangles = new TriangleList();
		TriangulationVertexList vertices = new TriangulationVertexList();

		SubdividedPolygonPlugin subdividedPolygonPlugin = new SubdividedPolygonPlugin();
		viewer.registerPlugin(subdividedPolygonPlugin);
		DraggablePolygon2DAdapter polygon2DAdapter = new DraggablePolygon2DAdapter(
				subdividedPolygonPlugin);
		setMeshRelatedPlugins(viewer, polygon2DAdapter, triangles, vertices, autoredrawer);
		
		setParticleUpdaterPlugin(viewer, UPDATES_PER_SECOND, particles, triangles, autoredrawer);
		redrawTimer.start();
	}

	private void setParticleUpdaterPlugin(Java2DViewer viewer,
			int UPDATES_PER_SECOND, ParticleList particles, TriangleList triangles, Autoredrawer autoredrawer) {

		ParticlePlugin particlePlugin = new ParticlePlugin(new ParticleCreator(
				particles, triangles), particles);
		viewer.registerPlugin(particlePlugin);
		autoredrawer.setParticleDrawer(particlePlugin);
		
		ParticleAdderPanel particleAdder = new ParticleAdderPanel(new ParticleCreator(
				particles, triangles), particlePlugin);
		viewer.registerPlugin(particleAdder);
		
		Timer updateTimer = new Timer(0, null);
		ParticleUpdater particleUpdater = new ParticleUpdater(particles,
				UPDATES_PER_SECOND, updateTimer);
		ParticleUpdateInvoker particleUpdateInvoker = new ParticleUpdateInvoker(
				particleUpdater);
		viewer.registerPlugin(particleUpdateInvoker);
		
	}

	private void setVectorFieldGeneratorPlugin(Java2DViewer viewer,
			TriangleList triangles, MeshPlugin vectorDrawer) {
		VectorFieldGeneratorInvoker vectorFieldGeneratorInvoker = new VectorFieldGeneratorInvoker(
				new VectorFieldGenerator(triangles), vectorDrawer);
		viewer.registerPlugin(vectorFieldGeneratorInvoker);
	}

	private void setFunctionGeneratorPlugin(Java2DViewer viewer,
			TriangulationVertexList vertices, MeshPlugin valueDrawer) {
		FunctionGeneratorInvoker functionGeneratorInvoker = new FunctionGeneratorInvoker(
				new FunctionGenerator(vertices), valueDrawer);
		viewer.registerPlugin(functionGeneratorInvoker);
	}

	private void setMeshRelatedPlugins(Java2DViewer viewer,
			DraggablePolygon2DAdapter polygon2DAdapter, TriangleList triangles,
			TriangulationVertexList vertices, Autoredrawer autoredrawer) {
		MeshModel meshModel = new MeshModel(triangles, vertices);

		MeshPlugin meshPlugin = new MeshPlugin(meshModel);
		viewer.registerPlugin(meshPlugin);
		autoredrawer.setMeshDrawer(meshPlugin);

		TriangulationInvoker triangulationInvoker = new TriangulationInvoker(
				meshModel, polygon2DAdapter);
		viewer.registerPlugin(triangulationInvoker);

		setFunctionGeneratorPlugin(viewer, vertices, meshPlugin);
		setVectorFieldGeneratorPlugin(viewer, triangles, meshPlugin);
	}

	public static void main(String[] args) {
		Java2DViewer viewer = new Java2DViewer();
		Builder builder = new Builder();
		builder.buildAndRegister(viewer);
		viewer.startup();
	}

}