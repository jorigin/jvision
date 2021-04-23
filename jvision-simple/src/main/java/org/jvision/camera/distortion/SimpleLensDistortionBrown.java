package org.jvision.camera.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Vector;
import org.jvision.JVision;

/**
 * A class dedicated to the handling of lens distortion within the Brown model. 
 * A complete description of the model can be found in <a href="https://eserv.asprs.org/PERS/1966journal/may/1966_may_444-462.pdf">Decentering distortion of lenses</a>
 * <br><br>
 * The distortion is used to compute distorted / undistorted points for a given camera. Let (x, y) be the projection of a 3D point onto an image. 
 * A new point (x', y') that integrate distortion (a distorted point) can be obtained a follows:<br>
 * <br>
 * r = sqrt(x<sup>2</sup> + y<sup>2</sup>)<br>
 * x' = x(1 + K<sub>1</sub>r<sup>2</sup> + K<sub>2</sub>r<sup>4</sup> + K<sub>3</sub>r<sup>6</sup> + K<sub>4</sub>r<sup>8</sup>) + (P<sub>2</sub>(r<sup>2</sup>+2x<sup>2</sup>) + 2P<sub>1</sub>xy)(1 + P<sub>3</sub>r<sup>2</sup> + P<sub>4</sub>r<sup>4</sup>)<br>  
 * y' = y(1 + K<sub>1</sub>r<sup>2</sup> + K<sub>2</sub>r<sup>4</sup> + K<sub>3</sub>r<sup>6</sup> + K<sub>4</sub>r<sup>8</sup>) + (P<sub>1</sub>(r<sup>2</sup>+2y<sup>2</sup>) + 2P<sub>2</sub>xy)(1 + P<sub>3</sub>r<sup>2</sup> + P<sub>4</sub>r<sup>4</sup>)<br> 
 * 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleLensDistortionBrown implements LensDistortionBrown{

	/**
	 * The distortion convention.
	 */
	private String distortionConvention = "brown";

	/**
	 * The distortion components.
	 */
	private int distortionComponents        = LensDistortion.TYPE_NO_DISTORTION;

	/** The K1 coefficient.*/ private double k1 = 0.0d;
	/** The K2 coefficient.*/ private double k2 = 0.0d;
	/** The K3 coefficient.*/ private double k3 = 0.0d;
	/** The K4 coefficient.*/ private double k4 = 0.0d;

	/** The P1 coefficient.*/ private double p1 = 0.0d;
	/** The P2 coefficient.*/ private double p2 = 0.0d;
	/** The P3 coefficient.*/ private double p3 = 0.0d;
	/** The P4 coefficient.*/ private double p4 = 0.0d;

	/**
	 * The number of iterations for undistortion computation.
	 */
	private int iterationMax = 5;

	/**
	 * Create a new Brown based lens distortion with no distortion.
	 */
	public SimpleLensDistortionBrown() {	
	}

	/**
	 * Create a new distortion representation based on Brown formalization with radial coefficients (k1, k2, k3, k4) 
	 * and tangential coefficients (p1, p2, p3, p4).
	 * @param k1 the first radial distortion coefficient
	 * @param k2 the second radial distortion coefficient
	 * @param k3 the third radial distortion coefficient
	 * @param k4 the fourth radial distortion coefficient
	 * @param p1 the first tangential distortion coefficient
	 * @param p2 the second tangential distortion coefficient
	 * @param p3 the third tangential distortion coefficient
	 * @param p4 the fourth tangential distortion coefficient
	 */
	public SimpleLensDistortionBrown(double k1, double k2, double k3, double k4, double p1, double p2, double p3, double p4){
		this();
		setDistortionCoefficients(k1, k2, k3, k4, p1, p2, p3, p4);
	}

	/**
	 * Create a new distortion representation based on Brown formalization from the given {@link Vector vector}. Possible vector values are:
	 * <ul>
	 *   <li>(k1, k2, k3, k4, p1, p2, p3, p4) (8 dimensions)
	 *   <li>(k1, k2, k3, p1, p2) (5 dimensions)
	 *   <li>(k1, k2, k3) (3 dimensions)
	 *   <li> null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 */
	public SimpleLensDistortionBrown(Vector coefficients) {
		this();
		setDistortionCoefficients(coefficients);
	}

	/**
	 * Create a new distortion representation based on Brown formalization from the given <code>float</code> array. Possible vector values are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 *   <li> null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 */
	public SimpleLensDistortionBrown(float[] coefficients) {
		this();
		setDistortionCoefficients(coefficients);
	}

	/**
	 * Create a new distortion representation based on Brown formalization from the given <code>float</code> array. Possible vector values are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 *   <li> null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 */
	public SimpleLensDistortionBrown(double[] coefficients) {
		this();
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
	public Point2D distort(Point2D input){

		return distort(input, JeometryFactory.createPoint2D(0.0d,0.0d));
	}

	@Override
	public Point2D distort(Point2D input, Point2D distorted) {
		if (distorted != null) {
			double x = input.getX();
			double y = input.getY();

			double xp = x;
			double yp = y;

			if (distortionComponents != LensDistortion.TYPE_NO_DISTORTION){

				// Compute the r factors.
				double r2 = input.getX()*input.getX() + input.getY()*input.getY();
				double r4 = r2 * r2;
				double r6 = r2 * r4;
				double r8 = r4 * r4;

				// Apply radial distortion
				if ((distortionComponents & LensDistortion.TYPE_RADIAL) != 0){
					// xp = x(1 + K1r2 + K2r4 + K3r6 + K4r8)
					xp = x * (1+k1*r2 + k2*r4 + k3*r6 + k4*r8);

					// yp = y(1 + K1r2 + K2r4 + K3r6 + K4r8)
					yp = y * (1+k1*r2 + k2*r4 + k3*r6 + k4*r8);
				}

				// Apply tangential distortion
				if ((distortionComponents & LensDistortion.TYPE_TANGENTIAL) != 0){

					// xp = xp + (P2(r2+2x2) + 2P1xy)(1 + P3r2 + P4r4) 
					xp = xp + (p2*(r2+2*x*x) + 2*p1*x*y)*(1 + p3*r2 + p4*r4); 

					// yp = yp + (P1(r2+2y2) + 2P2xy)(1 + P3r2 + P4r4) 
					yp = yp + (p1 * (r2+2*y*y) + 2*p2*x*y)*(1 + p3*r2 + p4*r4); 
				}

			}

			distorted.setX(xp);
			distorted.setY(yp);
		}
		return distorted;
	}

	@Override
	public Point2D undistort(Point2D input){

		return undistort(input, JeometryFactory.createPoint2D(0.0d, 0.0d));
	}

	@Override
	public Point2D undistort(Point2D input, Point2D corrected) {
		if (corrected != null) {
			double x = input.getX();
			double y = input.getY();

			double xu = x;
			double yu = y;

			if (distortionComponents != LensDistortion.TYPE_NO_DISTORTION){

				for(int i = 0; i < iterationMax; i++){

					// Compute the r factors.
					double r2 = input.getX()*input.getX() + input.getY()*input.getY();
					double r4 = r2 * r2;
					double r6 = r2 * r4;
					double r8 = r4 * r4;

					// Correct tangential distortion
					if ((distortionComponents & LensDistortion.TYPE_TANGENTIAL) != 0){
						xu = x - ((p2*(r2+2*x*x) + 2*p1*x*y)*(1 + p3*r2 + p4*r4));
						yu = y - ((p1 * (r2+2*y*y) + 2*p2*x*y)*(1 + p3*r2 + p4*r4));
					}

					// Correct radial distortion
					if ((distortionComponents & LensDistortion.TYPE_RADIAL) != 0){
						xu = xu / (1+k1*r2 + k2*r4 + k3*r6 + k4*r8);
						yu = yu / (1+k1*r2 + k2*r4 + k3*r6 + k4*r8);
					}
				}
			}

			corrected.setX(xu);
			corrected.setY(yu);
		}
		return corrected;
	}

	/**
	 * Get the distortion coefficients as the {@link Vector vector} (k<sub>1</sub>, k2, k3, k4, p1, p2, p3, p4) (8 dimensions).
	 * @return the distortion coefficients
	 */
	@Override
	public Vector getDistortionCoefficients() {
		Vector v = getDistortionCoefficients(JeometryFactory.createVector(8));
		return v;
	}

	/**
	 * Get the distortion coefficients as a {@link Vector vector} by filling the given <code>coefficients</code>. 
	 * Possible output vectors are:
	 * <ul>
	 *   <li>(k1, k2, k3, k4, p1, p2, p3, p4) (8 dimensions)
	 *   <li>(k1, k2, k3, p1, p2) (5 dimensions)
	 *   <li>(k1, k2, k3) (3 dimensions)
	 * </ul>
	 * @param coefficients the output vector that has to store distortion coefficients
	 * @return a reference on the given vector, for chaining purposes
	 * @throws IllegalArgumentException if the given vector does match the distortion implementation requirement
	 */
	@Override
	public Vector getDistortionCoefficients(Vector coefficients) throws IllegalArgumentException{
		if (coefficients != null) {

			if (coefficients.getDimension() == 8) {
				coefficients.setValue(0, k1);
				coefficients.setValue(1, k2);
				coefficients.setValue(2, k3);
				coefficients.setValue(3, k4);
				coefficients.setValue(4, p1);
				coefficients.setValue(5, p2);
				coefficients.setValue(6, p3);
				coefficients.setValue(7, p4);
			} else if (coefficients.getDimension() == 5) {
				coefficients.setValue(0, k1);
				coefficients.setValue(1, k2);
				coefficients.setValue(2, k3);
				coefficients.setValue(3, p1);
				coefficients.setValue(4, p2);
			} else if (coefficients.getDimension() == 3) {
				coefficients.setValue(0, k1);
				coefficients.setValue(1, k2);
				coefficients.setValue(2, k3);
			} else {
				throw new IllegalArgumentException("Invalid vector dimension "+coefficients.getDimension()+", expected ones are 8, 5 or 3.");
			}
		}

		return coefficients;
	}

	/**
	 * Set the coefficients of the distortion from the given {@link Vector vector}. Possible vector values are:
	 * <ul>
	 *   <li>(k1, k2, k3, k4, p1, p2, p3, p4) (8 dimensions)
	 *   <li>(k1, k2, k3, p1, p2) (5 dimensions)
	 *   <li>(k1, k2, k3) (3 dimensions)
	 *   <li> null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @see #setDistortionCoefficients(float[])
	 * @see #setDistortionCoefficients(double[])
	 */
	@Override
	public void setDistortionCoefficients(Vector coefficients) {
		if ((coefficients != null) && (coefficients.getDimension() > 0)) {
			if (coefficients.getDimension() == 8) {
				setDistortionCoefficients(coefficients.getValue(0), coefficients.getValue(1), coefficients.getValue(2), coefficients.getValue(3), 
						coefficients.getValue(4), coefficients.getValue(5), coefficients.getValue(6), coefficients.getValue(7));				
			} else if (coefficients.getDimension() == 5) {
				setDistortionCoefficients(coefficients.getValue(0), coefficients.getValue(1), coefficients.getValue(2), 0.0d, 
						coefficients.getValue(3), coefficients.getValue(4),                   0.0d, 0.0d);		
			} else if (coefficients.getDimension() == 3) {
				setDistortionCoefficients(coefficients.getValue(0), coefficients.getValue(1), coefficients.getValue(2), 0.0d, 
						0.0d,                   0.0d,                   0.0d, 0.0d);	
			} else {
				throw new IllegalArgumentException("Incorrect input vector dimension "+coefficients.getDimension()+", expected values are 8, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d, 0.0d, 0.0d);
		}
	}

	/**
	 * Get the distortion coefficients as the <code>float</code> array [k1, k2, k3, k4, p1, p2, p3, p4] (length 8). 
	 * @return the distortion coefficients
	 */
	@Override
	public float[] getDistortionCoefficientsFloat() {
		return getDistortionCoefficientsFloat(new float[8]);
	}

	/**
	 * Get the distortion coefficients as a <code>float</code> array by filling the given <code>coefficients</code>. 
	 * Possible vector values are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 * </ul>
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	@Override
	public float[] getDistortionCoefficientsFloat(float[] coefficients) throws IllegalArgumentException{
		if (coefficients != null) {
			if (coefficients.length == 8) {
				coefficients[0] = (float) k1;
				coefficients[1] = (float) k2;
				coefficients[2] = (float) k3;
				coefficients[3] = (float) k4;
				coefficients[4] = (float) p1;
				coefficients[5] = (float) p2;
				coefficients[6] = (float) p3;
				coefficients[7] = (float) p4;
			} else if (coefficients.length == 5) {
				coefficients[0] = (float) k1;
				coefficients[1] = (float) k2;
				coefficients[2] = (float) k3;
				coefficients[3] = (float) p1;
				coefficients[4] = (float) p2;
			} else if (coefficients.length == 3) {
				coefficients[0] = (float) k1;
				coefficients[1] = (float) k2;
				coefficients[2] = (float) k3;
			} else {
				throw new IllegalArgumentException("Invalid array length "+coefficients.length+", expected ones are 8, 5 or 3.");
			}
		}

		return coefficients;
	}

	/**
	 * Set the coefficients of the distortion from the given <code>float</code> array. Possible arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 *   <li> null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 */
	@Override
	public void setDistortionCoefficients(float[] coefficients) {
		if ((coefficients != null) && (coefficients.length > 0)) {
			if (coefficients.length == 8) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3], 
						coefficients[4], coefficients[5], coefficients[6], coefficients[7]);				
			} else if (coefficients.length == 5) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], 0.0d, 
						coefficients[3], coefficients[4],          0.0d, 0.0d);		
			} else if (coefficients.length == 3) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], 0.0d, 
						0.0d,          0.0d,          0.0d, 0.0d);	
			} else {
				throw new IllegalArgumentException("Incorrect input array length "+coefficients.length+", expected values are 8, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d, 0.0d, 0.0d);
		}
	}

	/**
	 * Get the distortion coefficients as a <code>double</code> array [k1, k2, k3, k4, p1, p2, p3, p4] (length 8). 
	 * @return the distortion coefficients
	 */
	@Override
	public double[] getDistortionCoefficientsDouble() {
		return getDistortionCoefficientsDouble(new double[8]);
	}

	/**
	 * Get the distortion coefficients as a <code>double</code> array by filling the given <code>coefficients</code>. 
	 * Possible vector values are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 * </ul>
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	@Override
	public double[] getDistortionCoefficientsDouble(double[] coefficients) throws IllegalArgumentException{
		if (coefficients != null) {
			if (coefficients.length == 8) {
				coefficients[0] = k1;
				coefficients[1] = k2;
				coefficients[2] = k3;
				coefficients[3] = k4;
				coefficients[4] = p1;
				coefficients[5] = p2;
				coefficients[6] = p3;
				coefficients[7] = p4;
			} else if (coefficients.length == 5) {
				coefficients[0] = k1;
				coefficients[1] = k2;
				coefficients[2] = k3;
				coefficients[3] = p1;
				coefficients[4] = p2;
			} else if (coefficients.length == 3) {
				coefficients[0] = k1;
				coefficients[1] = k2;
				coefficients[2] = k3;
			} else {
				throw new IllegalArgumentException("Invalid array length "+coefficients.length+", expected ones are 8, 5 or 3.");
			}
		}

		return coefficients;
	}

	/**
	 * Set the coefficients of the distortion from the given <code>float</code> array. Possible arrays are:
	 * <ul>
	 *   <li>[k1, k2, k3, k4, p1, p2, p3, p4] (8 values)
	 *   <li>[k1, k2, k3, p1, p2] (5 values)
	 *   <li>[k1, k2, k3] (3 values)
	 *   <li> null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 */
	@Override
	public void setDistortionCoefficients(double[] coefficients) {
		if ((coefficients != null) && (coefficients.length > 0)) {
			if (coefficients.length == 8) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3], 
						coefficients[4], coefficients[5], coefficients[6], coefficients[7]);				
			} else if (coefficients.length == 5) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], 0.0d, 
						coefficients[3], coefficients[4],          0.0d, 0.0d);		
			} else if (coefficients.length == 3) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[2], 0.0d, 
						0.0d,          0.0d,          0.0d, 0.0d);	
			} else {
				throw new IllegalArgumentException("Incorrect input array length "+coefficients.length+", expected values are 8, 5 or 3.");
			}
		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 
					0.0d, 0.0d, 0.0d, 0.0d);
		}
	}

	@Override
	public double getK1() {
		return k1;
	}

	@Override
	public double getK2() {
		return k2;
	}

	@Override
	public double getK3() {
		return k3;
	}

	@Override
	public double getK4() {
		return k4;
	}

	@Override
	public double getP1() {
		return p1;
	}

	@Override
	public double getP2() {
		return p2;
	}

	@Override
	public double getP3() {
		return p3;
	}

	@Override
	public double getP4() {
		return p4;
	}

	@Override
	public int getUndistortIterationMax(){
		return iterationMax;
	}

	@Override
	public void setUndistortIterationMax(int max){

	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double p1, double p2, double p3, double p4) {
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
		this.k4 = k4;

		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;

		distortionComponents = LensDistortion.TYPE_NO_DISTORTION;

		if ((k1 != 0.0d)||(k2 != 0.0d)||(k3 != 0.0d)||(k4 == 0.0d)){
			distortionComponents |= LensDistortion.TYPE_RADIAL_SIMPLE;
		}

		if ((p1 != 0.0d)||(p2 != 0.0d)||(p3 != 0.0d)||(p4 == 0.0d)){
			distortionComponents |= LensDistortion.TYPE_TANGENTIAL;
		}
	}
}



