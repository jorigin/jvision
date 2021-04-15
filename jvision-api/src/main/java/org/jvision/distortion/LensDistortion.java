package org.jvision.distortion;

import org.jeometry.geom2D.point.Point2D;
import org.jvision.JVision;

/**
 * An interface that enables to group photographic lens distortion models.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface LensDistortion {
  
  /**
   * Get the convention used by this lens distortion representation.
   * @return the convention used by this lens distortion representation.
   */
  public String getConvention();	

  /**
   * Distort the point given in parameter. The point have to be expressed within camera coordinates. 
   * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
   * @param input the input point within camera coordinates. 
   * @return the distorted point.
   * @see #undistort(Point2D)
   */
  public Point2D distort(Point2D input);
  
  /**
   * Distort the <code>input</code> point and store the result within <code>output</code> point. 
   * The point have to be expressed within camera coordinates. 
   * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
   * @param input the input point within camera coordinates. 
   * @param distorted the output point within camera coordinates. 
   * @return the distorted point (same reference as <code>output</code> if its not <code>null</code>).
   * @see #undistort(Point2D, Point2D)
   */
  public Point2D distort(Point2D input, Point2D distorted);
  
  /**
   * Undistort the point given in parameter. The point have to be expressed within image coordinates.
   * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
   * @param input the input point within image coordinates. 
   * @return the undistorted point. 
   * @see #distort(Point2D)
   */
  public Point2D undistort(Point2D input);
  
  /**
   * Undistort the <code>input</code> point and store the result within <code>output</code> point. 
   * The point have to be expressed within image coordinates.
   * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
   * @param input the input point within image coordinates. 
   * @param corrected the output point within camera coordinates. 
   * @return the undistorted point (same reference as <code>output</code> if its not <code>null</code>).
   * @see #distort(Point2D, Point2D)
   */
  public Point2D undistort(Point2D input, Point2D corrected);
}
