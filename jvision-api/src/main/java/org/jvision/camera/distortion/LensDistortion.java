package org.jvision.camera.distortion;

import org.jeometry.geom2D.point.Point2D;
import org.jvision.JVision;

/**
 * An interface that enables to group photographic lens distortion models.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 */
public interface LensDistortion {

	/**
	 * A flag that indicates that the lens has no distortion distortion.
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_NO_DISTORTION   = 0;

	/**
	 * A flag that indicates that the radial distortion has a simple component (at least one of <i>K<sub>1</sub></i>, <i>K<sub>2</sub></i> or <i>K<sub>3</sub></i> is not equals to <i>0</i>.)
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_RADIAL_SIMPLE   = 1;

	/**
	 * A flag that indicates that the radial distortion has a rational component (at least one of <i>K<sub>4</sub></i>, <i>K<sub>5</sub></i> or <i>K<sub>6</sub></i> is not equals to <i>0</i>.)
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_RADIAL_RATIONAL = 2;

	/**
	 * A flag that indicates that the distortion has a radial component (that can be simple, radial or both).
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_RADIAL = TYPE_RADIAL_SIMPLE | TYPE_RADIAL_RATIONAL;

	/**
	 * A flag that indicates that the distortion has a tangential component (at least one of <i>P<sub>1</sub></i> or <i>P<sub>2</sub></i> is not equals to <i>0</i>.)
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_TANGENTIAL      = 4;

	/**
	 * A flag that indicates that the distortion has a prism component (at least one of <i>S<sub>1</sub></i>, <i>S<sub>2</sub></i>, <i>S<sub>3</sub></i> or <i>S<sub>4</sub></i> is not equals to <i>0</i>.)
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_PRISM           = 8;

	/**
	 * A flag that indicates that the distortion has a tilt component (at least one of <i>T<sub>x</sub></i>, <i>T<sub>y</sub></i> is not equals to <i>0</i>.)
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 */
	public static final int TYPE_TILT            = 16;

	/**
	 * Get the convention used by this lens distortion representation.
	 * @return the convention used by this lens distortion representation.
	 */
	public String getDistortionConvention();	

	/**
	 * Get the components of the distortion. The returned value is a logical combination of {@link #TYPE_RADIAL_SIMPLE}, {@link #TYPE_RADIAL_RATIONAL}, {@link #TYPE_TANGENTIAL}, {@link #TYPE_PRISM} or {@link #TYPE_TILT}.
	 * @return the components of the distortion
	 */
	public int getDistortionComponents();

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
