package org.jvision.factory;

import org.junit.jupiter.api.BeforeAll;
import org.jvision.JVision;
import org.jvision.camera.distortion.SimpleLensDistortionBrown;
import org.jvision.camera.distortion.SimpleLensDistortionMetashape;
import org.jvision.camera.distortion.SimpleLensDistortionOpenCV;

/**
 * A test suite for {@link JVisionFactory} with the <code>simple</code> implementation.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleJVisionFactoryTest extends JVisionFactoryTest {

	/**
	 * Initialize the tests
	 */
	@BeforeAll
	public static void initClass(){
    
		JVisionFactory.loadImplementation("simple");
		
		lensDistortionBrownImplementationClass = SimpleLensDistortionBrown.class;
		
		lensDistortionOpenCVImplementationClass = SimpleLensDistortionOpenCV.class;

        lensDistortionMetashapeImplementationClass = SimpleLensDistortionMetashape.class;
	}

}
