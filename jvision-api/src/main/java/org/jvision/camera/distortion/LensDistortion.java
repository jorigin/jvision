package org.jvision.camera.distortion;

import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Vector;
import org.jvision.JVision;

/**
 * An interface that enables to group photographic lens distortion models.<br><br>
 * In geometric optics, distortion is a deviation from rectilinear projection; a projection in which straight lines in a scene remain straight in an image. It is a form of optical aberration.
 * Various models and convention enable to represents distortion. Moreover, distortion can be made of different components such as:
 * <ul>
 * <li>Radial distortion that is the symmetric distortion caused by the lens due to imperfections in curvature when the lens was ground.
 * <li>Tangential (or decentering) distortion that is the non-symmetric distortion due to the misalignment of the lens elements when the camera is assembled.
 * <li>Thin prism distortion that arises from slight tilt of lens or image sensor array
 * <li>Tilt distortion that is a a perspective distortion caused by image sensor that may be tilted in order to focus an oblique plane in front of the camera (Scheimpflug principle)
 * </ul>
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
	 * A flag that indicates that the radial distortion has a simple component.
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_RADIAL_SIMPLE   = 1;

	/**
	 * A flag that indicates that the radial distortion has a rational component.
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
	 * A flag that indicates that the distortion has a tangential (decentering) component.
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_PRISM
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_TANGENTIAL      = 4;

	/**
	 * A flag that indicates that the distortion has a prism component.
	 * @see #TYPE_NO_DISTORTION
	 * @see #TYPE_RADIAL_SIMPLE
	 * @see #TYPE_RADIAL_RATIONAL
	 * @see #TYPE_TANGENTIAL
	 * @see #TYPE_TILT
	 */
	public static final int TYPE_PRISM           = 8;

	/**
	 * A flag that indicates that the distortion has a tilt component.
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
	 * Distort the given point. The point have to be expressed within camera coordinates. 
	 * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param undistorted the input point within camera coordinates. 
	 * @return the distorted point.
	 * @see #undistort(Point2D)
	 */
	public Point2D distort(Point2D undistorted);

	/**
	 * Distort the <code>input</code> point and store the result within <code>output</code> point. 
	 * The point have to be expressed within camera coordinates. 
	 * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param undistorted the input point within camera coordinates. 
	 * @param distorted the output point within camera coordinates. 
	 * @return the distorted point (same reference as <code>output</code> if its not <code>null</code>).
	 * @see #undistort(Point2D, Point2D)
	 */
	public Point2D distort(Point2D undistorted, Point2D distorted);

	/**
	 * Undistort the given point. The point have to be expressed within image coordinates.
	 * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param distorted the input point within image coordinates. 
	 * @return the undistorted point. 
	 * @see #distort(Point2D)
	 */
	public Point2D undistort(Point2D distorted);

	/**
	 * Undistort the <code>input</code> point and store the result within <code>output</code> point. 
	 * The point have to be expressed within image coordinates.
	 * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param distorted the input point within image coordinates. 
	 * @param undistorted the output point within camera coordinates. 
	 * @return the undistorted point (same reference as <code>output</code> if its not <code>null</code>).
	 * @see #distort(Point2D, Point2D)
	 */
	public Point2D undistort(Point2D distorted, Point2D undistorted);
	
	/**
	 * Get the distortion coefficients as a {@link Vector vector}. 
	 * The dimension of the vector and the ordering of its components are defined by the underlying lens distortion implementation.
	 * @return the distortion coefficients
	 */
	public Vector getDistortionCoefficients();
	
	/**
	 * Get the distortion coefficients as a {@link Vector vector} by filling the given <code>coefficients</code>. 
	 * The dimension of the given vector has to fit the underlying lens distortion implementation.
	 * @param coefficients the output vector that has to store distortion coefficients
	 * @return a reference on the given vector, for chaining purposes
	 * @throws IllegalArgumentException if the given vector does match the distortion implementation requirement
	 */
	public Vector getDistortionCoefficients(Vector coefficients) throws IllegalArgumentException;
	
	/**
	 * Set the distortion coefficients from the given {@link Vector vector}. 
	 * The dimension of the vector and the ordering of its components are defined by the underlying lens distortion implementation.
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input vector does match the distortion implementation requirement
	 */
	public void setDistortionCoefficients(Vector coefficients) throws IllegalArgumentException;
	
	/**
	 * Get the distortion coefficients as a <code>float</code> array. 
	 * The dimension of the array and the ordering of its values are defined by the underlying lens distortion implementation.
	 * @return the distortion coefficients
	 */
	public float[] getDistortionCoefficientsFloat();
	
	/**
	 * Get the distortion coefficients as a <code>float</code> array by filling the given <code>coefficients</code>. 
	 * The dimension of the given array has to fit the underlying lens distortion implementation.
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	public float[] getDistortionCoefficientsFloat(float[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Set the distortion coefficients from the given <code>float</code> array. 
	 * The dimension of the array and the ordering of its values are defined by the underlying lens distortion implementation.
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	public void setDistortionCoefficients(float[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Get the distortion coefficients as a <code>double</code> array. 
	 * The dimension of the array and the ordering of its values are defined by the underlying lens distortion implementation.
	 * @return the distortion coefficients
	 */
	public double[] getDistortionCoefficientsDouble();
	
	/**
	 * Get the distortion coefficients as a <code>double</code> array by filling the given <code>coefficients</code>. 
	 * The dimension of the given array has to fit the underlying lens distortion implementation.
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	public double[] getDistortionCoefficientsDouble(double[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Set the distortion coefficients from the given <code>double</code> array. 
	 * The dimension of the array and the ordering of its values are defined by the underlying lens distortion implementation.
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	public void setDistortionCoefficients(double[] coefficients) throws IllegalArgumentException;
}
