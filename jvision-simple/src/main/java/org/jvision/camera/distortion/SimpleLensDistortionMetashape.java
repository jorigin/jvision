package org.jvision.camera.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Vector;
import org.jvision.JVision;

/**
 * A class dedicated to the handling of lens distortion within the Agisoft Metashape(c) convention. 
 * <br>
 * The distortion is used to compute distorted / undistorted points for a given camera. Let (X, Y, Z) be a 3D point expressed within the camera referential.<br> 
 * <br>
 * A new point (u, v) that integrate distortion (a distorted point) can be obtained a follows:<br>
 * 
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>x</td><td>=</td><td>X&nbsp;/&nbsp;Z</td></tr>
 * <tr><td>y</td><td>=</td><td>Y&nbsp;/&nbsp;Z</td></tr>
 * </table>
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>x'</td><td>=</td><td>x</td><td>(1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup> + k<sub>4</sub>r<sup>8</sup>)</td><td>+</td><td>(P<sub>1</sub>(r<sup>2</sup>+2x<sup>2</sup>) + 2P<sub>2</sub>xy)</td></tr>
 * </table>
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>y'</td><td>=</td><td>x</td><td>(1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup> + k<sub>4</sub>r<sup>8</sup>)</td><td>+</td><td>(P<sub>2</sub>(r<sup>2</sup>+2y<sup>2</sup>) + 2P<sub>1</sub>xy)</td></tr>
 * </table>
 * <br>
 * with:<br>
 * <ul>
 * <li>(x', y') is a point within camera referential that is affected by the distortion.
 * <li>r<sup>2</sup> = x<sup>2</sup> + y<sup>2</sup>.
 * <li>k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub> are the radial distortion coefficients.
 * <li>p<sub>1</sub>, p<sub>2</sub> are the tangential distortion coefficients.
 * </ul>
 * We got then:<br>
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>u = w &times; 0.5 + c<sub>x</sub> + x'f + x'B<sub>1</sub> + y'B<sub>2</sub></td><tr>
 * <tr><td>v = h &times; 0.5 + c<sub>y</sub> + y'f</td></tr> 
 * </table>
 * <br>
 * with:<br>
 * <ul>
 * <li>(u, v) is a point within the image referential that is affected by the distortion expressed in pixels (px).
 * <li>w is the digital image width in pixels (px).
 * <li>h is the digital image height in pixels (px).
 * <li>(c<sub>x</sub>, c<sub>y</sub>) is the principal point offset expressed in pixels (px).
 * <li>f is the camera focal length in pixels (px).
 * <li>B<sub>1</sub> is the affinity coefficient.
 * <li>B<sub>2</sub> is the non-orthogonality (skew) coefficient.
 * </ul>
 * As the parameters B<sub>1</sub> and B<sub>2</sub> are not part of the lens distortion, the last equation is not taken in account within this class computation.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleLensDistortionMetashape implements LensDistortionMetashape {

	/**
	 * The distortion convention.
	 */
	private String distortionConvention = "agisoft-matashape";

	/**
	 * The distortion components.
	 */
	private int distortionComponents        = LensDistortion.TYPE_NO_DISTORTION;

	/** The K1 radial parameter.*/ private double k1 = 0.0d;
	/** The K2 radial parameter.*/ private double k2 = 0.0d;
	/** The K3 radial parameter.*/ private double k3 = 0.0d;
	/** The K4 radial parameter.*/ private double k4 = 0.0d;

	/** The P1 tangential parameter.*/ private double p1 = 0.0d;
	/** The P2 tangential parameter.*/ private double p2 = 0.0d;

	/**
	 * Construct a new Agisoft Metashape lens distortion representation with no distortion. 
	 * <br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 */
	public SimpleLensDistortionMetashape() {
	}
	
	/**
	 * Construct a new Agisoft Metashape lens distortion. <br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * @param k1 the first radial distortion parameter 
	 * @param k2 the second radial distortion parameter 
	 * @param k3 the third radial distortion parameter 
	 * @param k4 the fourth radial distortion parameter 
	 * @param p1 the first tangential distortion parameter 
	 * @param p2 the second tangential distortion parameter 
	 */
	public SimpleLensDistortionMetashape(double k1, double k2, double k3, double k4, 
			double p1, double p2) {

		this();
		
		setK1(k1);
		setK2(k2);
		setK3(k3);
		setK4(k4);

		setP1(p1);
		setP2(p2);
	}

	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted vector is (k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>) (6 dimensions).
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public SimpleLensDistortionMetashape(Vector coefficients) throws IllegalArgumentException{
		this();
		setDistortionCoefficients(coefficients);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted array is [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>] (length 6).
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public SimpleLensDistortionMetashape(float[] coefficients) throws IllegalArgumentException{
		setDistortionCoefficients(coefficients);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted array is [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>] (length 6).
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
    public SimpleLensDistortionMetashape(double[] coefficients) throws IllegalArgumentException{
    	setDistortionCoefficients(coefficients);
	}
	
	@Override
	public String getDistortionConvention() {
		return distortionConvention;
	}

	@Override
	public int getDistortionComponents() {
		return distortionComponents;
	}

	@Override
	public Point2D distort(Point2D input) {
		return distort(input, JeometryFactory.createPoint2D());
	}

	@Override
	public Point2D distort(Point2D input, Point2D distorted) {

		if (distorted != null) {

			if (input != null) {
				double r2 = input.getX()*input.getX()+input.getY()*input.getY();
				double r4 = r2*r2; 
				double r6 = r4*r2; 
				double r8 = r4*r4;

				double xp = input.getX()*(1 + k1*r2 + k2*r4 + k3*r6 + k4*r8) + (p1*(r2+2*input.getX()*input.getX()) + 2*p2*input.getX()*input.getY());

				double yp = input.getY()*(1 + k1*r2 + k2*r4 + k3*r6 + k4*r8) + (p1*(r2+2*input.getY()*input.getY()) + 2*p1*input.getX()*input.getY()); 

				distorted.setX(xp);
				distorted.setY(yp);

			} else {
				distorted.setX(Double.NaN);
				distorted.setY(Double.NaN);
			}
		}

		return distorted;
	}

	@Override
	public Point2D undistort(Point2D input) {
		return undistort(input, JeometryFactory.createPoint2D());
	}

	@Override
	public Point2D undistort(Point2D input, Point2D corrected) {

		if (corrected != null) {

			if (input != null) {
				// TODO Implements Point2D undistort(Point2D input, Point2D corrected)
			} else {
				corrected.setX(Double.NaN);
				corrected.setY(Double.NaN);
			}
		}

		return corrected;
	}

	/**
	 * Get the distortion parameters as the {@link Vector vector} (k1, k2, k3, k4, p1, p2) (6 dimensions). 
	 * @return the distortion parameters
	 */
	@Override
	public Vector getDistortionCoefficients() {
		Vector v = getDistortionCoefficients(JeometryFactory.createVector(6));
		return v;
	}

	/**
	 * Get the distortion parameters as a {@link Vector vector} by filling the given <code>parameters</code>. 
	 * Possible output vectors are:
	 * <ul>
	 *   <li>(k1, k2, k3, k4, p1, p2) (6 dimensions)
	 *   <li>(k1, k2, k3, p1, p2) (5 dimensions)
	 *   <li>(k1, k2, k3) (3 dimensions)
	 * </ul>
	 * @param parameters the output vector that has to store distortion parameters
	 * @return a reference on the given vector, for chaining purposes
	 * @throws IllegalArgumentException if the given vector does match the distortion implementation requirement
	 */
	@Override
	public Vector getDistortionCoefficients(Vector parameters) throws IllegalArgumentException{
		if (parameters != null) {

			if (parameters.getDimension() == 6) {
				parameters.setValue(0, k1);
				parameters.setValue(1, k2);
				parameters.setValue(2, k3);
				parameters.setValue(3, k4);
				parameters.setValue(4, p1);
				parameters.setValue(5, p2);
			} else if (parameters.getDimension() == 5) {
				parameters.setValue(0, k1);
				parameters.setValue(1, k2);
				parameters.setValue(2, k3);
				parameters.setValue(3, p1);
				parameters.setValue(4, p2);
			} else if (parameters.getDimension() == 3) {
				parameters.setValue(0, k1);
				parameters.setValue(1, k2);
				parameters.setValue(2, k3);
			} else {
				throw new IllegalArgumentException("Invalid vector dimension "+parameters.getDimension()+", expected ones are 6, 5 or 3.");
			}
		}

		return parameters;
	}

	/**
	 * Set the distortion parameters from the given {@link Vector vector}. 
	 * Possible input vectors are:
	 * <ul>
	 *   <li>(k1, k2, k3, k4, p1, p2) (6 dimensions)
	 *   <li>(k1, k2, k3, p1, p2) (5 dimensions)
	 *   <li>(k1, k2, k3) (3 dimensions)
	 *   <li>null or empty vector for no distortion
	 * </ul>
	 * @param parameters the distortion parameters
	 * @throws IllegalArgumentException if the input vector does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(Vector parameters) throws IllegalArgumentException{
		if ((parameters != null) && (parameters.getDimension() > 0)) {
			if (parameters.getDimension() == 6) {
				setDistortionCoefficients(parameters.getValue(0), parameters.getValue(1), parameters.getValue(2), parameters.getValue(3), 
						parameters.getValue(4), parameters.getValue(5));				
			} else if (parameters.getDimension() == 5) {
				setDistortionCoefficients(parameters.getValue(0), parameters.getValue(1), parameters.getValue(2), 0.0d, 
						parameters.getValue(3), parameters.getValue(4));		
			} else if (parameters.getDimension() == 3) {
				setDistortionCoefficients(parameters.getValue(0), parameters.getValue(1), parameters.getValue(2), 0.0d, 
						0.0d,                   0.0d);	
			} else {
				throw new IllegalArgumentException("Incorrect input vector dimension "+parameters.getDimension()+", expected values are 6, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d);	
		}
	}

	/**
	 * Get the distortion parameters as the <code>float</code> array [k1, k2, k3, k4, p1, p2] (length 6). 
	 * @return the distortion parameters
	 */
	@Override
	public float[] getDistortionCoefficientsFloat() {
		return getDistortionCoefficientsFloat(new float[6]);
	}

	/**
	 * Get the distortion parameters as a <code>float</code> array by filling the given <code>parameters</code>. 
	 * Possible output arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2] (length 6)
	 *   <li>[k1, k2, k3, p1, p2] (length 5)
	 *   <li>[k1, k2, k3] (length 3)
	 * </ul>
	 * @param parameters the output array that has to store distortion parameters
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	@Override
	public float[] getDistortionCoefficientsFloat(float[] parameters) throws IllegalArgumentException{
		if (parameters != null) {
			if (parameters.length == 6) {
				parameters[0] = (float) k1;
				parameters[1] = (float) k2;
				parameters[2] = (float) k3;
				parameters[3] = (float) k4;
				parameters[4] = (float) p1;
				parameters[5] = (float) p2;
			} else if (parameters.length == 5) {
				parameters[0] = (float) k1;
				parameters[1] = (float) k2;
				parameters[2] = (float) k3;
				parameters[3] = (float) p1;
				parameters[4] = (float) p2;
			} else if (parameters.length == 3) {
				parameters[0] = (float) k1;
				parameters[1] = (float) k2;
				parameters[2] = (float) k3;
			} else {
				throw new IllegalArgumentException("Invalid array length "+parameters.length+", expected ones are 6, 5 or 3.");
			}
		}

		return parameters;
	}

	/**
	 * Set the distortion parameters from the given <code>float</code> array. 
	 * Possible input arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2] (length 6)
	 *   <li>[k1, k2, k3, p1, p2] (length 5)
	 *   <li>[k1, k2, k3] (length 3)
	 *   <li>null or empty vector for no distortion
	 * </ul>
	 * @param parameters the distortion parameters
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(float[] parameters) throws IllegalArgumentException{
		if ((parameters != null) && (parameters.length > 0)) {
			if (parameters.length == 6) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], parameters[3], 
						parameters[4], parameters[5]);				
			} else if (parameters.length == 5) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], 0.0d, 
						parameters[3], parameters[4]);		
			} else if (parameters.length == 3) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], 0.0d, 
						0.0d,          0.0d);	
			} else {
				throw new IllegalArgumentException("Invalid array length "+parameters.length+", expected values are 6, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d);	
		}
	}

	/**
	 * Get the distortion parameters as the <code>double</code> array [k1, k2, k3, k4, p1, p2] (length 6).
	 * @return the distortion parameters
	 */
	@Override
	public double[] getDistortionCoefficientsDouble() {
		return getDistortionCoefficientsDouble(new double[6]);
	}

	/**
	 * Get the distortion parameters as a <code>double</code> array by filling the given <code>parameters</code>. 
	 * Possible output arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2] (length 6)
	 *   <li>[k1, k2, k3, p1, p2] (length 5)
	 *   <li>[k1, k2, k3] (length 3)
	 * </ul>
	 * @param parameters the output array that has to store distortion parameters
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	public double[] getDistortionCoefficientsDouble(double[] parameters) throws IllegalArgumentException {
		if (parameters != null) {
			if (parameters.length == 6) {
				parameters[0] = k1;
				parameters[1] = k2;
				parameters[2] = k3;
				parameters[3] = k4;
				parameters[4] = p1;
				parameters[5] = p2;
			} else if (parameters.length == 5) {
				parameters[0] = k1;
				parameters[1] = k2;
				parameters[2] = k3;
				parameters[3] = p1;
				parameters[4] = p2;
			} else if (parameters.length == 3) {
				parameters[0] = k1;
				parameters[1] = k2;
				parameters[2] = k3;
			} else {
				throw new IllegalArgumentException("Invalid array length "+parameters.length+", expected ones are 6, 5 or 3.");
			}
		}

		return parameters;
	}

	/**
	 * Set the distortion parameters from the given <code>double</code> array. 
	 * Possible input arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2] (length 6)
	 *   <li>[k1, k2, k3, p1, p2] (length 5)
	 *   <li>[k1, k2, k3] (length 3)
	 *   <li>null or empty vector for no distortion
	 * </ul>
	 * @param parameters the distortion parameters
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(double[] parameters) throws IllegalArgumentException {
		if ((parameters != null) && (parameters.length > 0)) {
			if (parameters.length == 6) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], parameters[3], 
						parameters[4], parameters[5]);				
			} else if (parameters.length == 5) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], 0.0d, 
						parameters[3], parameters[4]);		
			} else if (parameters.length == 3) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[2], 0.0d, 
						0.0d,          0.0d);	
			} else {
				throw new IllegalArgumentException("Invalid array length "+parameters.length+", expected values are 6, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d);	
		}
	}

	@Override
	public double getK1() {
		return k1;
	}

	@Override
	public void setK1(double k1) {
		this.k1 = k1;
	}

	@Override
	public double getK2() {
		return k2;
	}

	@Override
	public void setK2(double k2) {
		this.k2 = k2;
	}

	@Override
	public double getK3() {
		return k3;
	}

	@Override
	public void setK3(double k3) {
		this.k3 = k3;
	}

	@Override
	public double getK4() {
		return k4;
	}

	@Override
	public void setK4(double k4) {
		this.k4 = k4;
	}

	@Override
	public double getP1() {
		return p1;
	}

	@Override
	public void setP1(double p1) {
		this.p1 = p1;
	}

	@Override
	public double getP2() {
		return p2;
	}

	@Override
	public void setP2(double p2) {
		this.p2 = p2;
	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double p1, double p2) {
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
		this.k4 = k4;

		this.p1 = p1;
		this.p2 = p2;

		distortionComponents = LensDistortion.TYPE_NO_DISTORTION;

		if ((k1 != 0.0d)||(k2 != 0.0d)||(k3 != 0.0d)||(k4 == 0.0d)){
			distortionComponents |= LensDistortion.TYPE_RADIAL_SIMPLE;
		}

		if ((p1 != 0.0d)||(p2 != 0.0d)){
			distortionComponents |= LensDistortion.TYPE_TANGENTIAL;
		}
	}
}