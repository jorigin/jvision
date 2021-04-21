package org.jvision.camera.distortion;

import org.junit.jupiter.api.BeforeAll;
import org.jvision.JVision;
import org.jvision.factory.JVisionFactory;

/**
 * A class for testing simple Brown representation of a lens distortion.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleLensDistortionBrownTest extends LensDistortionBrownTest {

	  /**
	   * Initialize the tests
	   */
	  @BeforeAll
	  public static void initClass(){
		  JVisionFactory.loadImplementation("simple");
	  }
}
