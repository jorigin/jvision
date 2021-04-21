package org.jvision.camera.distortion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jvision.JVision;
import org.jvision.factory.JVisionFactory;

/**
 * A test dedicated to {@link LensDistortionBrown Brown representation of lens distortion}.
 * <br>
 * <b>Usage:</b>
 * <p>
 * Create a test class that extends this one and add the method:<br><br>
 * <code>
 * {@literal @}BeforeClass<br>
 * public static void initClass() {<br>
 * <br>
 * &nbsp;&nbsp;// The implementation classes for provided objets. <br>
 * &nbsp;&nbsp;// set to null if no check has to be done<br>
 * &nbsp;&nbsp;lensDistortionImplementationClass = [the lens distortion objects class];<br>
 * <br>
 * &nbsp;&nbsp;JVisionFactory.setCameraBuilder([a builder that provide suitable classes]);<br>
 * }<br>
 * </code>
 * <br>
 * If the object provided by the factory are not from the same classes as the declared ones, tests will fail.
 * </p>

 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 *
 */
public class LensDistortionBrownTest{

  static final float[] LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_FLOAT = new float[] {-0.1047f, 0.25263f, -0.0385946f, 0.0f, 0.000508056f, -0.0027698f, 0.0f, 0.0f};
  
  static final double[] LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_DOUBLE = new double[] {-0.1047d, 0.25263d, -0.0385946d, 0.0f, 0.000508056d, -0.0027698d, 0.0d, 0.0d};
  
  static final Vector LENS_DISTORTION_BROWN_COEFFICIENTS_VECTOR = JeometryFactory.createVector(LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_DOUBLE);
  
  static List<Point2D> observedPoints;
  
  static LensDistortionBrown distortion;
  
  static final double NUMERIC_PRECISION = 0.001d;
  
  static final int STABILITY_ITERATION_COUNT = 1;
  
  static Class<?> lensDistortionImplementationClass = null;
  
  // Static initialization
  {
	  initData();
  }
  
  /**
   * Initialize the data.
   */
  private static void initData() {

	    observedPoints = new ArrayList<Point2D>(4);
	    
	    double step = 0.001;
	    
	    observedPoints.add(JeometryFactory.createPoint2D(0.0d, 0.0d));
	    for(double i = step; i <= 0.5; i = i + step){
	      observedPoints.add(JeometryFactory.createPoint2D(   i,    i));
	      observedPoints.add(JeometryFactory.createPoint2D(  -i,    i));
	      observedPoints.add(JeometryFactory.createPoint2D(   i,   -i));
	      observedPoints.add(JeometryFactory.createPoint2D(  -i,   -i));
	      observedPoints.add(JeometryFactory.createPoint2D(   i, 0.0d));
	      observedPoints.add(JeometryFactory.createPoint2D(  -i, 0.0d));
	      observedPoints.add(JeometryFactory.createPoint2D(0.0d,    i));
	      observedPoints.add(JeometryFactory.createPoint2D(0.0d,   -i));
	    }
	    
	    distortion = JVisionFactory.createLensDistortionBrown(-0.1047d, 0.25263d, -0.0385946d, 0.0d, 0.000508056d, -0.0027698d, 0.0d, 0.0d);
  }
  
  /**
   * Initialize the tests
   */
  @BeforeAll
  public static void initClass(){
	  fail("Method public static void initClass() has to be overriden.");
  }
  
  /**
   * Test the distortion center invariability.
   */
  @Test
  public void testDistortionCenterInvariability(){
	  
    Point2D reference = JeometryFactory.createPoint2D(0.0d, 0.0d);
    Point2D corrected;
    
    corrected = distortion.undistort(reference);
    
    assertEquals(reference.getX(), corrected.getX(), NUMERIC_PRECISION, "Undistort is not invariant for reference point ("+reference.getX()+", "+reference.getY()+"): ("+corrected.getX()+", "+corrected.getY()+").");
    assertEquals(reference.getY(), corrected.getY(), NUMERIC_PRECISION, "Undistort is not invariant for reference point ("+reference.getX()+", "+reference.getY()+"): ("+corrected.getX()+", "+corrected.getY()+").");
  }
  
  /**
   * Testing distortion stability
   */
  @Test
  public void testDistortionStability(){
	  
    Iterator<Point2D> iter = observedPoints.iterator();
    Point2D reference;
    Point2D corrected = JeometryFactory.createPoint2D(Double.NaN, Double.NaN);
    Point2D distorted;
    while(iter.hasNext()){
      reference = iter.next();
      distorted = reference;

      for(int i = 0; i < STABILITY_ITERATION_COUNT; i++){
        
        // Test undistort / distort
        corrected = distortion.undistort(distorted);
        distorted = distortion.distort(corrected);
      }
   
      assertEquals(reference.getX(), distorted.getX(), NUMERIC_PRECISION, "Distort / Undistort is not stable for reference point ("+reference.getX()+", "+reference.getY()+"): ("+distorted.getX()+", "+distorted.getY()+").");
      assertEquals(reference.getY(), distorted.getY(), NUMERIC_PRECISION, "Distort / Undistort is not stable for reference point ("+reference.getX()+", "+reference.getY()+"): ("+distorted.getX()+", "+distorted.getY()+").");
    }
  }
}



