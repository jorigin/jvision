package org.jvision.camera.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Matrix;
import org.jeometry.math.Vector;
import org.jvision.JVision;

/**
 * A class dedicated to the handling of lens distortion within the OpenCV convention. 
 * A complete description of the convention can be found in <a href="https://docs.opencv.org/master/d9/d0c/group__calib3d.html">Camera calibration With OpenCV </a>
 * <br>
 * The distortion is used to compute distorted / undistorted points for a given camera. Let (x, y) be the projection of a 3D point onto an image.<br> 
 * <img src="doc-files/opencv_pinhole_camera_model.png" alt="OPENCV Pinehole camera model">
 * <br>
 * A new point (x'', y'') that integrate distortion (a distorted point) can be obtained a follows:<br>
 * 
 *  <table>
 *  <caption>&nbsp;</caption>
 *  <tr><td>   </td><td> </td><td>  </td><td>       </td><td>1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup></td><td> </td><td>                </td><td> </td><td>                                              </td><td> </td><td></td><td>                 </td><td>                                    </td></tr>
 *  <tr><td>x''</td><td>=</td><td>x'</td><td>&times;</td><td><hr></td><td>+</td><td>2p<sub>1</sub>x'y'</td><td>+</td><td>p<sub>2</sub>(r<sup>2</sup>+2x'<sup>2</sup>)</td><td>+</td><td>s<sub>1</sub>r<sup>2</sup></td><td>+</td><td>s<sub>2</sub>r<sup>4</sup></td></tr>
 *  <tr><td>   </td><td> </td><td>  </td><td>       </td><td>1 + k<sub>4</sub>r<sup>2</sup> + k<sub>5</sub>r<sup>4</sup> + k<sub>6</sub>r<sup>6</sup></td><td> </td><td>                </td><td> </td><td>                                              </td><td> </td><td>                          </td><td> </td><td>                          </td></tr>
 *  </table>
 *  <br>and<br>
 *  <table>
 *  <caption>&nbsp;</caption>
 *  <tr><td>   </td><td> </td><td>  </td><td>       </td><td>1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup></td><td> </td><td>                </td><td> </td><td>                                              </td><td> </td><td>                          </td><td> </td><td>                          </td></tr>
 *  <tr><td>y''</td><td>=</td><td>y'</td><td>&times;</td><td><hr></td><td>+</td><td>p<sub>1</sub>(r<sup>2</sup>+2y'<sup>2</sup>)</td><td>+</td><td>2p<sub>2</sub>x'y'</td><td>+</td><td>s<sub>3</sub>r<sup>2</sup></td><td>+</td><td>s<sub>4</sub>r<sup>4</sup></td></tr>
 *  <tr><td>   </td><td> </td><td>  </td><td>       </td><td>1 + k<sub>4</sub>r<sup>2</sup> + k<sub>5</sub>r<sup>4</sup> + k<sub>6</sub>r<sup>6</sup></td><td> </td><td>                </td><td> </td><td>                                              </td><td> </td><td>                          </td><td> </td><td>                          </td></tr>
 *  </table>
 * 
 * with:<br>
 * <ul>
 * <li>(x', y') is a 3D point within camera referential that is not affected by the distortion.
 * <li>(x'', y'') is a point within camera referential that is affected by the distortion.
 * <li>r<sup>2</sup> = x'<sup>2</sup> + y'<sup>2</sup>.
 * <li>k<sub>1</sub>, ..., k<sub>6</sub> are the radial distortion coefficients.
 * <li>p<sub>1</sub>, p<sub>2</sub> are the tangential distortion coefficients.
 * <li>s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub> are the thin prism distortion coefficients.
 * </ul>
 * 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleLensDistortionOpenCV implements LensDistortionOpenCV {

	/**
	 * The distortion convention.
	 */
	private String distortionConvention = "opencv";

	/**
	 * The distortion components.
	 */
	private int distortionComponents        = LensDistortion.TYPE_NO_DISTORTION;

	/**
	 * The numerical limit.
	 */
	private double epsilon       = 0.000001;

	/**
	 * The max number of iterations for undistort computation.
	 */
	private double iterationsMax = 50;

	// Radial simple coefficients
	/** The K1 radial parameter.*/ private double k1 = 0.0d;
	/** The K2 radial parameter.*/ private double k2 = 0.0d;
	/** The K3 radial parameter.*/ private double k3 = 0.0d;

	// Radial rationnal coefficients
	/** The K4 radial rational parameter.*/ private double k4 = 0.0d;
	/** The K5 radial rational parameter.*/ private double k5 = 0.0d;
	/** The K6 radial rational parameter.*/ private double k6 = 0.0d;

	// Tangential coefficients
	/** The P1 tangential parameter.*/ private double p1 = 0.0d;
	/** The P2 tangential parameter.*/ private double p2 = 0.0d;

	// Prism coefficient
	/** The S1 prism parameter.*/ private double s1 = 0.0d;
	/** The S2 prism parameter.*/ private double s2 = 0.0d;
	/** The S3 prism parameter.*/ private double s3 = 0.0d;
	/** The S4 prism parameter.*/ private double s4 = 0.0d;

	/** The tx tilt parameter.*/ private double tx = 0.0d;
	/** The ty tils parameter.*/ private double ty = 0.0d;

	/**
	 * Create a new OpenCV lens distortion representation with no distortion.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 */
	public SimpleLensDistortionOpenCV() {
		
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 * @param tx the x tilt distortion coefficient
	 * @param ty the y tilt distortion coefficient
	 */
	public SimpleLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2, double s1, double s2, double s3, double s4, double tx, double ty) {
		this();
		setDistortionCoefficients(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4, tx, ty);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 */
	public SimpleLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2, double s1, double s2, double s3, double s4) {
		this();
		setDistortionCoefficients(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 */
	public SimpleLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2) {
		this();
		setDistortionCoefficients(k1, k2, k3, k4, k5, k6, p1, p2);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 */
	public SimpleLensDistortionOpenCV(double k1, double k2, double k3, double p1, double p2) {
		this();
		setDistortionCoefficients(k1, k2, k3, p1, p2);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted vectors are:
	 * <ul>
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>) (14 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>) (12 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>) (8 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>) (5 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>) (4 dimensions)
	 * <li>null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public SimpleLensDistortionOpenCV(Vector coefficients) {
		this();
		setDistortionCoefficients(coefficients);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted arrays are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * <li>null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public SimpleLensDistortionOpenCV(float[] coefficients) {
		this();
		setDistortionCoefficients(coefficients);
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted arrays are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * <li>null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public SimpleLensDistortionOpenCV(double[] coefficients) {
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
	public Point2D distort(Point2D input, Point2D distorted) {
		if (distorted != null) {
			double r2 = input.getX()*input.getX() + input.getY()+input.getY();
			double r4 = r2*r2;
			double r6 = r2*r4;

			// Radial distortion
			distorted.setX(input.getX()*((1+ k1*r2 + k2*r4 + k3*r6)/(1+ k4*r2 + k5*r4 + k6*r6)));
			distorted.setY(input.getY()*((1+ k1*r2 + k2*r4 + k3*r6)/(1+ k4*r2 + k5*r4 + k6*r6)));

			// Tangential distortion
			distorted.setX(distorted.getX() + 2*p1*input.getX()*input.getY() + p2*(r2 + 2*input.getX()*input.getX())); 
			distorted.setY(distorted.getY() +  p1*(r2 + 2*input.getY()*input.getY())+2*p2*input.getX()*input.getY()); 

			// Prism distortion
			distorted.setX(distorted.getX() +  s1*r2 + s2*r4);
			distorted.setY(distorted.getY() +  s3*r2 + s4*r4);
		}
		return distorted;
	}

	@Override
	public Point2D distort(Point2D input) {    
		return distort(input, JeometryFactory.createPoint2D(0.0d, 0.0d));
	}

	@Override
	public Point2D undistort(Point2D input) {
		return undistort(input, JeometryFactory.createPoint2D(input.getX(), input.getY()));
	}


	@Override
	public Point2D undistort(Point2D input, Point2D corrected) {

		//TODO Implements undistort(Point2D, Point2D) for all parameters

		if (corrected != null) {
			double x = input.getX();
			double y = input.getY();

			double x0 = x;
			double y0 = x;

			double error = 0;

			for( int j = 0; ; j++ ){
				if ( j >= iterationsMax)
					break;
				if (error < epsilon)
					break;

				double r2     = x*x + y*y;

				double icdist = (1 + ((k6*r2 + k5)*r2 + k4)*r2)/(1 + ((k3*r2 + k2)*r2 + k1)*r2);

				double deltaX = 2*p1*x*y + p2*(r2 + 2*x*x)+ s1*r2+s2*r2*r2;

				double deltaY = p2*(r2 + 2*y*y) + 2*p2*x*y+ s3*r2+s4*r2*r2;

				x = (x0 - deltaX)*icdist;
				y = (y0 - deltaY)*icdist;


				if ((k4 != 0.0d) && (k5 != 0.0d) && (k6 != 0.0d)) {
					/*
  	  	      double r4, r6, a1, a2, a3, cdist, icdist2;

  	  	      double xd, yd, xd0, yd0;

  	  	      double[] vecTilt;

  	  	      r2 = x*x + y*y;
  	  	      r4 = r2*r2;
  	  	      r6 = r4*r2;
  	  	      a1 = 2*x*y;
  	  	      a2 = r2 + 2*x*x;
  	  	      a3 = r2 + 2*y*y;
  	  	      cdist = 1 + k1*r2 + k2*r4 + k3*r6;
  	  	      icdist2 = 1./(1 + k4*r2 + k5*r4 + k6*r6);
  	  	      xd0 = x*cdist*icdist2 + p1*a1 + p2*a2 + s1*r2+s2*r4;
  	  	      yd0 = y*cdist*icdist2 + p1*a3 + p2*a1 + s3*r2+s4*r4;

  	  	      vecTilt = matTilt*cv::Vec3d(xd0, yd0, 1);
  	  	      invProj = vecTilt(2) ? 1./vecTilt(2) : 1;
  	  	      xd = invProj * vecTilt(0);
  	  	      yd = invProj * vecTilt(1);

  	  	      double x_proj = xd*fx + cx;
  	  	      double y_proj = yd*fy + cy;

  	  	      error = Math.sqrt( Math.pow(x_proj - u, 2) + Math.pow(y_proj - v, 2) );
					 */
				}
			}

			corrected.setX(x);
			corrected.setY(y);
		}

		return corrected;
	}

	@Override
	public Vector getDistortionCoefficients() {
		return getDistortionCoefficients(JeometryFactory.createVector(14));
	}

	@Override
	public Vector getDistortionCoefficients(Vector coefficients) throws IllegalArgumentException{
		if (coefficients != null) {
			if (coefficients.getDimension() == 14) {
				coefficients.setValue( 0, k1);
				coefficients.setValue( 1, k2);
				coefficients.setValue( 2, p1);
				coefficients.setValue( 3, p2);
				coefficients.setValue( 4, k3);
				coefficients.setValue( 5, k4);
				coefficients.setValue( 6, k5);
				coefficients.setValue( 7, k6);
				coefficients.setValue( 8, s1);
				coefficients.setValue( 9, s2);
				coefficients.setValue(10, s3);
				coefficients.setValue(11, s4);
				coefficients.setValue(12, tx);
				coefficients.setValue(13, ty);
			} else if (coefficients.getDimension() == 12) {
				coefficients.setValue( 0, k1);
				coefficients.setValue( 1, k2);
				coefficients.setValue( 2, p1);
				coefficients.setValue( 3, p2);
				coefficients.setValue( 4, k3);
				coefficients.setValue( 5, k4);
				coefficients.setValue( 6, k5);
				coefficients.setValue( 7, k6);
				coefficients.setValue( 8, s1);
				coefficients.setValue( 9, s2);
				coefficients.setValue(10, s3);
				coefficients.setValue(11, s4);
			} else if (coefficients.getDimension() == 8) {
				coefficients.setValue( 0, k1);
				coefficients.setValue( 1, k2);
				coefficients.setValue( 2, p1);
				coefficients.setValue( 3, p2);
				coefficients.setValue( 4, k3);
				coefficients.setValue( 5, k4);
				coefficients.setValue( 6, k5);
				coefficients.setValue( 7, k6);
			} else if (coefficients.getDimension() == 5) {
				coefficients.setValue( 0, k1);
				coefficients.setValue( 1, k2);
				coefficients.setValue( 2, p1);
				coefficients.setValue( 3, p2);
				coefficients.setValue( 4, k3);
			} else if (coefficients.getDimension() == 4) {
				coefficients.setValue( 0, k1);
				coefficients.setValue( 1, k2);
				coefficients.setValue( 2, p1);
				coefficients.setValue( 3, p2);
			} else {
				throw new IllegalArgumentException("Incorrect vector dimension "+coefficients.getDimension()+", expected values are 14, 12, 8, 5 or 4.");
			}
		}

		return coefficients;
	}

	@Override
	public void setDistortionCoefficients(Vector coefficients) throws IllegalArgumentException{
		if ((coefficients != null) && (coefficients.getDimension() > 0)) {

			if (coefficients.getDimension() == 14) {
				setDistortionCoefficients(coefficients.getValue(0),  coefficients.getValue(1), coefficients.getValue(4),           // K1, K2, K3 
						coefficients.getValue(5),  coefficients.getValue(6), coefficients.getValue(7),                           // K4, K5, K6
						coefficients.getValue(2),  coefficients.getValue(3),                                                   // P1, P2 
						coefficients.getValue(8),  coefficients.getValue(9), coefficients.getValue(10), coefficients.getValue(11), // S1, S2, S3, S4 
						coefficients.getValue(12), coefficients.getValue(13));                                                 // TX, TY
			} else if (coefficients.getDimension() == 12) {
				setDistortionCoefficients(coefficients.getValue(0),  coefficients.getValue(1), coefficients.getValue(4),           // K1, K2, K3 
						coefficients.getValue(5),  coefficients.getValue(6), coefficients.getValue(7),                           // K4, K5, K6
						coefficients.getValue(2),  coefficients.getValue(3),                                                   // P1, P2 
						coefficients.getValue(8),  coefficients.getValue(9), coefficients.getValue(10), coefficients.getValue(11));// S1, S2, S3, S4
			} else if (coefficients.getDimension() == 8) {
				setDistortionCoefficients(coefficients.getValue(0),  coefficients.getValue(1), coefficients.getValue(4),           // K1, K2, K3 
						coefficients.getValue(5),  coefficients.getValue(6), coefficients.getValue(7),                           // K4, K5, K6
						coefficients.getValue(2),  coefficients.getValue(3));                                                  // P1, P2
			} else if (coefficients.getDimension() == 5) {
				setDistortionCoefficients(coefficients.getValue(0),  coefficients.getValue(1), coefficients.getValue(4),           // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                                                                 // K4, K5, K6
						coefficients.getValue(2),  coefficients.getValue(3));                                                  // P1, P2
			} else if (coefficients.getDimension() == 4) {
				setDistortionCoefficients(coefficients.getValue(0),  coefficients.getValue(1), 0.0d,                             // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                                                                 // K4, K5, K6
						coefficients.getValue(2),  coefficients.getValue(3));                                                  // P1, P2
			} else {
				throw new IllegalArgumentException("Incorrect vector dimension "+coefficients.getDimension()+", expected values are 14, 12, 8, 5 or 4.");
			}

		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
		}
	}

	@Override
	public float[] getDistortionCoefficientsFloat() {
		return getDistortionCoefficientsFloat(new float[14]);
	}

	@Override
	public float[] getDistortionCoefficientsFloat(float[] coefficients) throws IllegalArgumentException{
		if (coefficients != null) {
			if (coefficients.length == 14) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
				coefficients[8]  = (float) this.s1;
				coefficients[9]  = (float) this.s2;
				coefficients[10] = (float) this.s3;
				coefficients[11] = (float) this.s4;
				coefficients[12] = (float) this.tx;
				coefficients[13] = (float) this.ty;
			} else if (coefficients.length == 12) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
				coefficients[8]  = (float) this.s1;
				coefficients[9]  = (float) this.s2;
				coefficients[10] = (float) this.s3;
				coefficients[11] = (float) this.s4;
			} else if (coefficients.length == 8) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else if (coefficients.length == 5) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else if (coefficients.length == 4) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else {
				throw new IllegalArgumentException("Incorrect array length "+coefficients.length+", expected lengthes are 14, 12, 8, 5 or 4.");
			}
		}

		return coefficients;
	}

	@Override
	public void setDistortionCoefficients(float[] coefficients) throws IllegalArgumentException{
		if ((coefficients != null) && (coefficients.length > 0)) {

			if (coefficients.length == 14) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[4],    // K1, K2, K3 
						coefficients[5],  coefficients[6], coefficients[7],                   // K4, K5, K6
						coefficients[2],  coefficients[3],                                  // P1, P2 
						coefficients[8],  coefficients[9], coefficients[10], coefficients[11],  // S1, S2, S3, S4 
						coefficients[12], coefficients[13]);                                // TX, TY
			} else if (coefficients.length == 12) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[4],    // K1, K2, K3 
						coefficients[5],  coefficients[6], coefficients[7],                   // K4, K5, K6
						coefficients[2],  coefficients[3],                                  // P1, P2 
						coefficients[8],  coefficients[9], coefficients[10], coefficients[11]); // S1, S2, S3, S4
			} else if (coefficients.length == 8) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[4],    // K1, K2, K3 
						coefficients[5],  coefficients[6], coefficients[7],                   // K4, K5, K6
						coefficients[2],  coefficients[3]);                                 // P1, P2
			} else if (coefficients.length == 5) {
				setDistortionCoefficients(coefficients[0], coefficients[1], coefficients[4],    // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                              // K4, K5, K6
						coefficients[2],  coefficients[3]);                                 // P1, P2
			} else if (coefficients.length == 4) {
				setDistortionCoefficients(coefficients[0], coefficients[1], 0.0d,             // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                              // K4, K5, K6
						coefficients[2],  coefficients[3]);                                 // P1, P2
			} else {
				throw new IllegalArgumentException("Incorrect array length "+coefficients.length+", expected lengthes are 14, 12, 8, 5 or 4.");
			}

		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
		}
	}

	@Override
	public double[] getDistortionCoefficientsDouble() {
		return getDistortionCoefficientsDouble(new double[14]);
	}

	@Override
	public double[] getDistortionCoefficientsDouble(double[] coefficients) throws IllegalArgumentException{
		if (coefficients != null) {
			if (coefficients.length == 14) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
				coefficients[8]  = (float) this.s1;
				coefficients[9]  = (float) this.s2;
				coefficients[10] = (float) this.s3;
				coefficients[11] = (float) this.s4;
				coefficients[12] = (float) this.tx;
				coefficients[13] = (float) this.ty;
			} else if (coefficients.length == 12) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
				coefficients[8]  = (float) this.s1;
				coefficients[9]  = (float) this.s2;
				coefficients[10] = (float) this.s3;
				coefficients[11] = (float) this.s4;
			} else if (coefficients.length == 8) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[5]  = (float) this.k4;
				coefficients[6]  = (float) this.k5;
				coefficients[7]  = (float) this.k6;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else if (coefficients.length == 5) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[4]  = (float) this.k3;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else if (coefficients.length == 4) {
				coefficients[0]  = (float) this.k1;
				coefficients[1]  = (float) this.k2;
				coefficients[2]  = (float) this.p1;
				coefficients[3]  = (float) this.p2;
			} else {
				throw new IllegalArgumentException("Incorrect array length "+coefficients.length+", expected lengthes are 14, 12, 8, 5 or 4.");
			}
		}

		return coefficients;
	}

	@Override
	public void setDistortionCoefficients(double[] parameters) throws IllegalArgumentException{
		if ((parameters != null) && (parameters.length > 0)) {

			if (parameters.length == 14) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[4],    // K1, K2, K3 
						parameters[5],  parameters[6], parameters[7],                   // K4, K5, K6
						parameters[2],  parameters[3],                                  // P1, P2 
						parameters[8],  parameters[9], parameters[10], parameters[11],  // S1, S2, S3, S4 
						parameters[12], parameters[13]);                                // TX, TY
			} else if (parameters.length == 12) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[4],    // K1, K2, K3 
						parameters[5],  parameters[6], parameters[7],                   // K4, K5, K6
						parameters[2],  parameters[3],                                  // P1, P2 
						parameters[8],  parameters[9], parameters[10], parameters[11]); // S1, S2, S3, S4
			} else if (parameters.length == 8) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[4],    // K1, K2, K3 
						parameters[5],  parameters[6], parameters[7],                   // K4, K5, K6
						parameters[2],  parameters[3]);                                 // P1, P2
			} else if (parameters.length == 5) {
				setDistortionCoefficients(parameters[0], parameters[1], parameters[4],    // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                              // K4, K5, K6
						parameters[2],  parameters[3]);                                 // P1, P2
			} else if (parameters.length == 4) {
				setDistortionCoefficients(parameters[0], parameters[1], 0.0d,             // K1, K2, K3 
						0.0d,  0.0d, 0.0d,                                              // K4, K5, K6
						parameters[2],  parameters[3]);                                 // P1, P2
			} else {
				throw new IllegalArgumentException("Incorrect array length "+parameters.length+", expected lengthes are 14, 12, 8, 5 or 4.");
			}

		} else {
			setDistortionCoefficients(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
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
	public double getK5() {
		return k5;
	}

	@Override
	public void setK5(double k5) {
		this.k5 = k5;
	}

	@Override
	public double getK6() {
		return k6;
	}

	@Override
	public void setK6(double k6) {
		this.k6 = k6;
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
	public double getS1() {
		return s1;
	}

	@Override
	public void setS1(double s1) {
		this.s1 = s1;
	}

	@Override
	public double getS2() {
		return s2;
	}

	@Override
	public void setS2(double s2) {
		this.s2 = s2;
	}

	@Override
	public double getS3() {
		return s3;
	}

	@Override
	public void setS3(double s3) {
		this.s3 = s3;
	}

	@Override
	public double getS4() {
		return s4;
	}

	@Override
	public void setS4(double s4) {
		this.s4 = s4;
	}

	@Override
	public double getTx() {
		return tx;
	}

	@Override
	public void setTx(double tx) {
		this.tx = tx;
	}

	@Override
	public double getTy() {
		return ty;
	}

	@Override
	public void setTy(double ty) {
		this.ty = ty;
	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2, 
			double s1, double s2, double s3, double s4,
			double tx, double ty) {
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
		this.k4 = k4;
		this.k5 = k5;
		this.k6 = k6;
		this.p1 = p1;
		this.p2 = p2;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
		this.tx = tx;
		this.ty = ty;

		if ((k1 != 0) || (k2 != 0) || (k3 != 0)) {
			this.distortionComponents |= LensDistortion.TYPE_RADIAL_SIMPLE;
		}

		if ((k4 != 0) || (k5 != 0) || (k6 != 0)) {
			this.distortionComponents |= LensDistortion.TYPE_RADIAL_RATIONAL;
		}

		if ((s1 != 0) || (s2 != 0) || (s3 != 0) || (s4 != 0)) {
			this.distortionComponents |= LensDistortion.TYPE_PRISM;
		}

		if ((tx != 0) || (ty != 0)) {
			this.distortionComponents |= LensDistortion.TYPE_TILT;
		}
	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2, 
			double s1, double s2, double s3, double s4) {
		setDistortionCoefficients(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4, 0.0d, 0.0d);
	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2) {
		setDistortionCoefficients(k1, k2, k3, k4, k5, k6, p1, p2, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
	}

	@Override
	public void setDistortionCoefficients(double k1, double k2, double k3, double p1, double p2) {
		setDistortionCoefficients(k1, k2, k3, 0.0d, 0.0d, 0.0d, p1, p2, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
	}
	
	/**
	 * Compute the tilt distortion
	 * @param tauX the tauX parameter
	 * @param tauY the tauY parameter
	 * @param matTilt the tilt matrix
	 * @param dMatTiltdTauX the tauX matrix
	 * @param dMatTiltdTauY tauY matrix
	 * @param invMatTilt the inversed tilt matrix
	 */
	private void computeTiltProjectionMatrix(double tauX, double tauY, Matrix matTilt, 
			Matrix dMatTiltdTauX, Matrix dMatTiltdTauY,
			Matrix invMatTilt){

		/*
		 * This method is a Java implementation of the 
		 * 
		 *  template <typename FLOAT> void computeTiltProjectionMatrix(FLOAT tauX, FLOAT tauY, Matx<FLOAT, 3, 3>* matTilt = 0,
		 *                                                             Matx<FLOAT, 3, 3>* dMatTiltdTauX = 0,
		 *                                                             Matx<FLOAT, 3, 3>* dMatTiltdTauY = 0,
		 *                                                             Matx<FLOAT, 3, 3>* invMatTilt = 0)
		 *                                                             
		 * from distortion_model.hpp (module imgproc/detail)                                                          
		 */
		double cTauX = Math.cos(tauX);
		double sTauX = Math.sin(tauX);
		double cTauY = Math.cos(tauY);
		double sTauY = Math.sin(tauY);

		Matrix matRotX  = JeometryFactory.createMatrix(new double[][] {{1,       0,     0},
			{ 0,  cTauX, sTauX},
			{ 0, -sTauX, cTauX }});

		Matrix matRotY  = JeometryFactory.createMatrix(new double[][]{{ cTauY, 0 ,-sTauY },
			{ 0    , 1 ,     0 },
			{ sTauY, 0 , cTauY }});

		Matrix matRotXY = matRotY.multiply(matRotX);

		Matrix matProjZ = JeometryFactory.createMatrix(new double[][]{{ matRotXY.getValue(2, 2),                       0, -matRotXY.getValue(0, 2) },
			{                       0, matRotXY.getValue(2, 2), -matRotXY.getValue(1, 2) },
			{                       0,                       0,                       1  }});

		if (matTilt != null){

			// Matrix for trapezoidal distortion of tilted image sensor
			// matTilt = matProjZ * matRotXY
			matProjZ.multiply(matRotXY, matTilt);

		}

		if (dMatTiltdTauX != null){
			// Derivative with respect to tauX
			Matrix dMatRotXYdTauX = JeometryFactory.createMatrix(new double[][] {{0, -sTauY * -cTauX, -sTauY * -sTauX},
				{0,          -sTauX,           cTauX},
				{0,  cTauY * -cTauX,  cTauY * -sTauX}});

			Matrix dMatProjZdTauX = JeometryFactory.createMatrix(new double[][]{{dMatRotXYdTauX.getValue(2,2),                             0, -dMatRotXYdTauX.getValue(0, 2)}, 
				{                           0, dMatRotXYdTauX.getValue(2, 2), -dMatRotXYdTauX.getValue(1, 2)},
				{                           0,                             0,                             0 }});


			// dMatTiltdTauX = (matProjZ * dMatRotXYdTauX) + (dMatProjZdTauX * matRotXY)
			matProjZ.multiply(dMatRotXYdTauX, dMatTiltdTauX);
			dMatTiltdTauX.add(dMatProjZdTauX.multiply(matRotXY));

		}

		if (dMatTiltdTauY != null){
			// Derivative with respect to tauY
			Matrix dMatRotXYdTauY = JeometryFactory.createMatrix( new double[][]{{-sTauY, -cTauY * -sTauX, -cTauY * cTauX}, 
				{     0,               0,              0}, 
				{ cTauY, -sTauY * -sTauX, -sTauY * cTauX}});

			Matrix dMatProjZdTauY = JeometryFactory.createMatrix( new double[][]{{dMatRotXYdTauY.getValue(2, 2),                             0, -dMatRotXYdTauY.getValue(0, 2)}, 
				{                            0, dMatRotXYdTauY.getValue(2, 2), -dMatRotXYdTauY.getValue(1, 2)},
				{                            0,                             0,                             0 }});

			// dMatTiltdTauY = (matProjZ * dMatRotXYdTauY) + (dMatProjZdTauY * matRotXY)
			matProjZ.multiply(dMatRotXYdTauY, dMatTiltdTauY);
			dMatTiltdTauY.add(dMatProjZdTauY.multiply(matRotXY));
		}

		if (invMatTilt != null){
			double inv = 1.0d / matRotXY.getValue(2, 2);
			Matrix invMatProjZ = JeometryFactory.createMatrix(new double[][]{{ inv,   0, inv*matRotXY.getValue(0, 2) },
				{   0, inv, inv*matRotXY.getValue(1, 2) },
				{   0,   0,                           1 }});

			matRotXY.transpose().multiply(invMatProjZ, invMatTilt);
		}
	}
}
