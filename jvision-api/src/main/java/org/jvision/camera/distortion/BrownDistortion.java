package org.jvision.camera.distortion;

import org.jvision.JVision;

/**
 * An interface that describes the lens distortion within the Brown model. 
 * A complete description of the model can be found in <a href="http://www.close-range.com/docs/Decentering_Distortion_of_Lenses_Brown_1966_may_444-462.pdf">Decentering distortion of lenses</a>
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
public interface BrownDistortion extends LensDistortion{

	  /**
	   * Get the first radial distortion parameter.
	   * @return the first radial distortion parameter.
	   * @see #getK2()
	   * @see #getK3()
	   * @see #getK4()
	   */
	  public double getK1();

	  /**
	   * Get the second radial distortion parameter.
	   * @return the second radial distortion parameter.
	   * @see #getK1()
	   * @see #getK3()
	   * @see #getK4()
	   */
	  public double getK2();

	  /**
	   * Get the third radial distortion parameter.
	   * @return the third radial distortion parameter.
	   * @see #getK1()
	   * @see #getK2()
	   * @see #getK4()
	   */
	  public double getK3();

	  /**
	   * Get the fourth radial distortion parameter.
	   * @return the fourth radial distortion parameter.
	   * @see #getK1()
	   * @see #getK2()
	   * @see #getK3()
	   */
	  public double getK4();

	  /**
	   * Get the first tangential distortion parameter.
	   * @return the first tangential distortion parameter.
	   * @see #getP2()
	   * @see #getP3()
	   * @see #getP4()
	   */
	  public double getP1();

	  /**
	   * Get the second tangential distortion parameter.
	   * @return the second tangential distortion parameter.
	   * @see #getP1()
	   * @see #getP3()
	   * @see #getP4()
	   */
	  public double getP2();

	  /**
	   * Get the third tangential distortion parameter.
	   * @return the third tangential distortion parameter.
	   * @see #getP1()
	   * @see #getP2()
	   * @see #getP4()
	   */
	  public double getP3();

	  /**
	   * Get the fourth tangential distortion parameter.
	   * @return the fourth tangential distortion parameter.
	   * @see #getP1()
	   * @see #getP2()
	   * @see #getP3()
	   */
	  public double getP4();
	  
	  /**
	   * Get the maximum number of iterations to process when computing undistortion of a point (default is <code>5</code>).
	   * @return the maximum number of iterations to process when computing undistortion of a point.
	   * @see #setUndistortIterationMax(int)
	   */
	  public int getUndistortIterationMax();
	  
	  /**
	   * Set the maximum number of iterations to process when computing undistortion of a point (default is <code>5</code>).
	   * @param max the maximum number of iterations to process when computing undistortion of a point.
	   * @see #getUndistortIterationMax()
	   */
	  public void setUndistortIterationMax(int max);
	  
}
