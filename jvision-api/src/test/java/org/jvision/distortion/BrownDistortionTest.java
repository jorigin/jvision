package org.jvision.distortion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jvision.JVision;

/**
 * A test dedicated to {@link BrownDistortion BrownDistortion} class.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 *
 */
public class BrownDistortionTest{

  static List<Point2D> observedPoints;
  
  static BrownDistortion distortion;
  
  static final double NUMERIC_PRECISION = 0.001d;
  
  static final int STABILITY_ITERATION_COUNT = 1;
  
  /**
   * Initialize the tests
   */
  @BeforeAll
  public static void initTest(){
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
      observedPoints.add(JeometryFactory.createPoint2D(0.0d, i));
      observedPoints.add(JeometryFactory.createPoint2D(0.0d, -i));
    }

    distortion = new BrownDistortion("standard", -0.1047, 0.25263, -0.0385946, 0.0, 0.000508056, -0.0027698, 0.0d, 0.0d);
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
    
    System.out.println("Testing distortion stability.");
    
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



