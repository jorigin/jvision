package org.jvision.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
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
 * <tr><td>x'</td><td>=</td><td>x</td><td>(1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup> + k<sub>4</sub>r<sup>8</sup>)</td><td>+</td><td>(P<sub>1</sub>(r<sup>2</sup>+2x<sup>2</sup>) + 2P<sub>2</sub>xy)(1 + P<sub>3</sub>r<sup>2</sup> + P<sub>4</sub>r<sup>4</sup>)</td></tr>
 * </table>
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>y'</td><td>=</td><td>x</td><td>(1 + k<sub>1</sub>r<sup>2</sup> + k<sub>2</sub>r<sup>4</sup> + k<sub>3</sub>r<sup>6</sup> + k<sub>4</sub>r<sup>8</sup>)</td><td>+</td><td>(P<sub>2</sub>(r<sup>2</sup>+2y<sup>2</sup>) + 2P<sub>1</sub>xy)(1 + P<sub>3</sub>r<sup>2</sup> + P<sub>4</sub>r<sup>4</sup>)</td></tr>
 * </table>
 * <br>
 * with:<br>
 * <ul>
 * <li>(x', y') is a point within camera referential that is affected by the distortion.
 * <li>r<sup>2</sup> = x<sup>2</sup> + y<sup>2</sup>.
 * <li>k<sub>1</sub>, ..., k<sub>4</sub> are the radial distortion coefficients.
 * <li>p<sub>1</sub>, ..., p<sub>4</sub> are the tangential distortion coefficients.
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
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class AgisoftMetashapeDistortion implements LensDistortion {

	private String convention = "agisoft_metashape";

	private double k1 = 0.0d;
	private double k2 = 0.0d;
	private double k3 = 0.0d;
	private double k4 = 0.0d;

	private double p1 = 0.0d;
	private double p2 = 0.0d;
	private double p3 = 0.0d;
	private double p4 = 0.0d;	
	
	@Override
	public String getConvention() {
		return convention;
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
				
				double xp = input.getX()*(1 + k1*r2 + k2*r4 + k3*r6 + k4*r8) + (p1*(r2+2*input.getX()*input.getX()) + 2*p2*input.getX()*input.getY())*(1 + p3*r2 + p4*r4);

				double yp = input.getY()*(1 + k1*r2 + k2*r4 + k3*r6 + k4*r8) + (p1*(r2+2*input.getY()*input.getY()) + 2*p1*input.getX()*input.getY())*(1 + p3*r2 + p4*r4); 

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
	 * Construct a new Agisoft Metashape lens distortion. 
	 * @param k1 the first radial distortion parameter 
	 * @param k2 the second radial distortion parameter 
	 * @param k3 the third radial distortion parameter 
	 * @param k4 the fourth radial distortion parameter 
	 * @param p1 the first tangential distortion parameter 
	 * @param p2 the second tangential distortion parameter 
	 * @param p3 the third tangential distortion parameter 
	 * @param p4 the fourth tangential distortion parameter 
	 */
	public AgisoftMetashapeDistortion(double k1, double k2, double k3, double k4, 
			                          double p1, double p2, double p3, double p4) {

		setK1(k1);
		setK2(k2);
		setK3(k3);
		setK4(k4);
		
		setP1(p1);
		setP2(p2);
		setP3(p3);
		setP4(p4);
	}

	/**
	 * Get the first radial distortion parameter <i>K<sub>1</sub></i>.
	 * @return the first radial distortion parameter <i>K<sub>1</sub></i>
	 * @see #setK1(double)
	 * @see #getK2()
	 * @see #getK3()
	 * @see #getK4()
	 */
	public double getK1() {
		return k1;
	}

	/**
	 * Set the first radial distortion parameter <i>K<sub>1</sub></i>.
	 * @param k1 the first radial distortion parameter <i>K<sub>1</sub></i>
	 * @see #getK1()
	 * @see #setK2(double)
	 * @see #setK3(double)
	 * @see #setK4(double)
	 */
	public void setK1(double k1) {
		this.k1 = k1;
	}

	/**
	 * Get the second radial distortion parameter <i>K<sub>2</sub></i>.
	 * @return the second radial distortion parameter <i>K<sub>2</sub></i>
	 * @see #setK2(double)
	 * @see #getK1()
	 * @see #getK3()
	 * @see #getK4()
	 */
	public double getK2() {
		return k2;
	}

	/**
	 * Set the second radial distortion parameter <i>K<sub>2</sub></i>.
	 * @param k2 the second radial distortion parameter <i>K<sub>2</sub></i>
	 * @see #getK2()
	 * @see #setK1(double)
	 * @see #setK3(double)
	 * @see #setK4(double)
	 */
	public void setK2(double k2) {
		this.k2 = k2;
	}

	/**
	 * Get the third radial distortion parameter <i>K<sub>3</sub></i>.
	 * @return the third radial distortion parameter <i>K<sub>3</sub></i>
	 * @see #setK3(double)
	 * @see #getK1()
	 * @see #getK2()
	 * @see #getK4()
	 */
	public double getK3() {
		return k3;
	}

	/**
	 * Set the third radial distortion parameter <i>K<sub>3</sub></i>.
	 * @param k3 the third radial distortion parameter <i>K<sub>3</sub></i>
	 * @see #getK3()
	 * @see #setK1(double)
	 * @see #setK2(double)
	 * @see #setK4(double)
	 */
	public void setK3(double k3) {
		this.k3 = k3;
	}

	/**
	 * Get the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	 * @return the fourth radial distortion parameter <i>K<sub>4</sub></i>
	 * @see #setK4(double)
	 * @see #getK1()
	 * @see #getK2()
	 * @see #getK3()
	 */
	public double getK4() {
		return k4;
	}

	/**
	 * Set the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	 * @param k4 the third radial distortion parameter <i>K<sub>4</sub></i>
	 * @see #getK4()
	 * @see #setK1(double)
	 * @see #setK2(double)
	 * @see #setK3(double)
	 */
	public void setK4(double k4) {
		this.k4 = k4;
	}

	/**
	 * Get the first tangential distortion parameter <i>P<sub>1</sub></i>.
	 * @return the first tangential distortion parameter <i>P<sub>1</sub></i>
	 * @see #setP1(double)
	 * @see #getP2()
	 * @see #getP3()
	 * @see #getP4()
	 */
	public double getP1() {
		return p1;
	}

	/**
	 * Set the first tangential distortion parameter <i>P<sub>1</sub></i>.
	 * @param p1 the first tangential distortion parameter <i>P<sub>1</sub></i>
	 * @see #getP1()
	 * @see #setP2(double)
	 * @see #setP3(double)
	 * @see #setP4(double)
	 */
	public void setP1(double p1) {
		this.p1 = p1;
	}

	/**
	 * Get the second tangential distortion parameter <i>P<sub>2</sub></i>.
	 * @return the second tangential distortion parameter <i>P<sub>2</sub></i>
	 * @see #setP2(double)
	 * @see #getP1()
	 * @see #getP3()
	 * @see #getP4()
	 */
	public double getP2() {
		return p2;
	}

	/**
	 * Set the second tangential distortion parameter <i>P<sub>2</sub></i>.
	 * @param p2 the second tangential distortion parameter <i>P<sub>2</sub></i>
	 * @see #getP2()
	 * @see #setP1(double)
	 * @see #setP3(double)
	 * @see #setP4(double)
	 */
	public void setP2(double p2) {
		this.p2 = p2;
	}

	/**
	 * Get the third tangential distortion parameter <i>P<sub>3</sub></i>.
	 * @return the third tangential distortion parameter <i>P<sub>3</sub></i>
	 * @see #setP3(double)
	 * @see #getP1()
	 * @see #getP2()
	 * @see #getP4()
	 */
	public double getP3() {
		return p3;
	}

	/**
	 * Set the third tangential distortion parameter <i>P<sub>3</sub></i>.
	 * @param p3 the third tangential distortion parameter <i>P<sub>3</sub></i>
	 * @see #getP3()
	 * @see #setP1(double)
	 * @see #setP2(double)
	 * @see #setP4(double)
	 */
	public void setP3(double p3) {
		this.p3 = p3;
	}

	/**
	 * Get the fourth tangential distortion parameter <i>P<sub>4</sub></i>.
	 * @return the fourth tangential distortion parameter <i>P<sub>4</sub></i>
	 * @see #setP4(double)
	 * @see #getP1()
	 * @see #getP2()
	 * @see #getP3()
	 */
	public double getP4() {
		return p4;
	}

	/**
	 * Set the fourth tangential distortion parameter <i>P<sub>4</sub></i>.
	 * @param p4 the fourth tangential distortion parameter <i>P<sub>4</sub></i>
	 * @see #getP4()
	 * @see #setP1(double)
	 * @see #setP2(double)
	 * @see #setP3(double)
	 */
	public void setP4(double p4) {
		this.p4 = p4;
	}
}
