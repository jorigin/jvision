package org.jvision.simple;

import org.jvision.JVision;
import org.jvision.factory.CameraBuilder;
import org.jvision.factory.JVisionFactory;
import org.jvision.factory.SimpleCameraBuilder;

/**
 * The JVision implementation class. This class is needed in order to register the implementation to the {@link JVisionFactory JVision factory}.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @version 1.0.0
 */
public class JVisionImplementation {

	/**
	 * The default camera builder.
	 */
	private static CameraBuilder cameraBuilder = new SimpleCameraBuilder();
	
	/**
	 * The implementation initialization method. This method can be invoked by the {@link JVisionFactory JVision factory}. 
	 */
	public static void initJVisionImplementation() {
		JVisionFactory.setDefaultCameraBuilder(cameraBuilder);
	}
}
