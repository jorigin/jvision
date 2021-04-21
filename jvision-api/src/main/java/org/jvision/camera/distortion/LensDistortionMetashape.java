package org.jvision.camera.distortion;

import org.jvision.JVision;

/**
 * An interface that class describes the lens distortion within the Agisoft Metashape(c) convention. 
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
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface LensDistortionMetashape extends LensDistortion {

	/**
	 * Get the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @return the first radial distortion coefficient <i>K<sub>1</sub></i>
	 * @see #setK1(double)
	 * @see #getK2()
	 * @see #getK3()
	 * @see #getK4()
	 */
	public double getK1();

	/**
	 * Set the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @param k1 the first radial distortion coefficient <i>K<sub>1</sub></i>
	 * @see #getK1()
	 * @see #setK2(double)
	 * @see #setK3(double)
	 * @see #setK4(double)
	 */
	public void setK1(double k1);

	/**
	 * Get the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @return the second radial distortion coefficient <i>K<sub>2</sub></i>
	 * @see #setK2(double)
	 * @see #getK1()
	 * @see #getK3()
	 * @see #getK4()
	 */
	public double getK2();

	/**
	 * Set the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @param k2 the second radial distortion coefficient <i>K<sub>2</sub></i>
	 * @see #getK2()
	 * @see #setK1(double)
	 * @see #setK3(double)
	 * @see #setK4(double)
	 */
	public void setK2(double k2);

	/**
	 * Get the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @return the third radial distortion coefficient <i>K<sub>3</sub></i>
	 * @see #setK3(double)
	 * @see #getK1()
	 * @see #getK2()
	 * @see #getK4()
	 */
	public double getK3();

	/**
	 * Set the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @param k3 the third radial distortion coefficient <i>K<sub>3</sub></i>
	 * @see #getK3()
	 * @see #setK1(double)
	 * @see #setK2(double)
	 * @see #setK4(double)
	 */
	public void setK3(double k3);

	/**
	 * Get the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @return the fourth radial distortion coefficient <i>K<sub>4</sub></i>
	 * @see #setK4(double)
	 * @see #getK1()
	 * @see #getK2()
	 * @see #getK3()
	 */
	public double getK4();

	/**
	 * Set the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @param k4 the third radial distortion coefficient <i>K<sub>4</sub></i>
	 * @see #getK4()
	 * @see #setK1(double)
	 * @see #setK2(double)
	 * @see #setK3(double)
	 */
	public void setK4(double k4);

	/**
	 * Get the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @return the first tangential distortion coefficient <i>P<sub>1</sub></i>
	 * @see #setP1(double)
	 * @see #getP2()
	 */
	public double getP1();

	/**
	 * Set the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @param p1 the first tangential distortion coefficient <i>P<sub>1</sub></i>
	 * @see #getP1()
	 * @see #setP2(double)
	 */
	public void setP1(double p1);

	/**
	 * Get the second tangential distortion coefficient <i>P<sub>2</sub></i>.
	 * @return the second tangential distortion coefficient <i>P<sub>2</sub></i>
	 * @see #setP2(double)
	 * @see #getP1()
	 */
	public double getP2();

	/**
	 * Set the second tangential distortion coefficient <i>P<sub>2</sub></i>.
	 * @param p2 the second tangential distortion coefficient <i>P<sub>2</sub></i>
	 * @see #getP2()
	 * @see #setP1(double)
	 */
	public void setP2(double p2);
	
	/**
	 * Set the distortion coefficients.
	 * @param k1 the first radial distortion coefficient
	 * @param k2 the second radial distortion coefficient
	 * @param k3 the third radial distortion coefficient
	 * @param k4 the fourth radial distortion coefficient
	 * @param p1 the first tangential distortion coefficient
	 * @param p2 the second tangential distortion coefficient
	 */
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double p1, double p2);
}
