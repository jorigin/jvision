package org.jvision.camera.distortion;

import org.jvision.JVision;

/**
 * An interface that describes the lens distortion within the OpenCV convention. 
 * A complete description of the convention can be found in <a href="https://docs.opencv.org/3.4/d9/d0c/group__calib3d.html">Camera calibration With OpenCV </a>
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
public interface OpenCVDistortion extends LensDistortion {

	  /**
	   * Get the first radial distortion parameter <i>K<sub>1</sub></i>.
	   * @return the first radial distortion parameter <i>K<sub>1</sub></i>.
	   * @see #setK1(double)
	   * @see #getK2()
	   * @see #getK3()
	   */
	  public double getK1();

	  /**
	   * Set the first radial distortion parameter <i>K<sub>1</sub></i>.
	   * @param k1 the first radial distortion parameter <i>K<sub>1</sub></i>.
	   * @see #getK1()
	   * @see #setK2(double)
	   * @see #setK3(double)
	   */
	  public void setK1(double k1);

	  /**
	   * Get the second radial distortion parameter <i>K<sub>2</sub></i>.
	   * @return the second radial distortion parameter <i>K<sub>2</sub></i>.
	   * @see #setK2(double)
	   * @see #getK1()
	   * @see #getK3()
	   */
	  public double getK2();
	  /**
	   * Set the second radial distortion parameter <i>K<sub>2</sub></i>.
	   * @param k2 the second radial distortion parameter <i>K<sub>2</sub></i>.
	   * @see #getK2()
	   * @see #setK1(double)
	   * @see #setK3(double)
	   */
	  public void setK2(double k2);

	  /**
	   * Get the third radial distortion parameter <i>K<sub>3</sub></i>.
	   * @return the third radial distortion parameter <i>K<sub>3</sub></i>.
	   * @see #setK3(double)
	   * @see #getK1()
	   * @see #getK2()
	   */
	  public double getK3();

	  /**
	   * Set the third radial distortion parameter <i>K<sub>3</sub></i>.
	   * @param k3 the third radial distortion parameter <i>K<sub>3</sub></i>.
	   * @see #getK3()
	   * @see #setK1(double)
	   * @see #setK2(double)
	   */
	  public void setK3(double k3);

	  /**
	   * Get the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	   * @return the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	   * @see #setK4(double)
	   * @see #getK5()
	   * @see #getK6()
	   */
	  public double getK4();

	  /**
	   * Set the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	   * @param k4 the fourth radial distortion parameter <i>K<sub>4</sub></i>.
	   * @see #getK4()
	   * @see #setK5(double)
	   * @see #setK6(double)
	   */
	  public void setK4(double k4);

	  /**
	   * Get the fifth radial distortion parameter <i>K<sub>5</sub></i>.
	   * @return the fourth radial distortion parameter <i>K<sub>5</sub></i>.
	   * @see #setK5(double)
	   * @see #getK4()
	   * @see #getK6()
	   */
	  public double getK5();

	  /**
	   * Set the fifth radial distortion parameter <i>K<sub>5</sub></i>.
	   * @param k5 the fourth radial distortion parameter <i>K<sub>5</sub></i>.
	   * @see #getK5()
	   * @see #setK4(double)
	   * @see #setK6(double)
	   */
	  public void setK5(double k5);

	  /**
	   * Get the sixth radial distortion parameter <i>K<sub>6</sub></i>.
	   * @return the fourth radial distortion parameter <i>K<sub>6</sub></i>.
	   * @see #setK6(double)
	   * @see #getK4()
	   * @see #getK5()
	   */
	  public double getK6();

	  /**
	   * Set the sixth radial distortion parameter <i>K<sub>6</sub></i>.
	   * @param k6 the fourth radial distortion parameter <i>K<sub>6</sub></i>.
	   * @see #getK6()
	   * @see #setK4(double)
	   * @see #setK5(double)
	   */
	  public void setK6(double k6);

	  /**
	   * Get the first tangential distortion parameter <i>P<sub>1</sub></i>.
	   * @return the first tangential distortion parameter <i>P<sub>1</sub></i>.
	   * @see #setP1(double)
	   * @see #getP2()
	   */
	  public double getP1();

	  /**
	   * Set the first tangential distortion parameter <i>P<sub>1</sub></i>.
	   * @param p1 the first tangential distortion parameter <i>P<sub>1</sub></i>.
	   * @see #getP1()
	   * @see #setP2(double)
	   */
	  public void setP1(double p1);

	  /**
	   * Get the second tangential distortion parameter <i>P<sub>2</sub></i>.
	   * @return the second tangential distortion parameter <i>P<sub>2</sub></i>.
	   * @see #setP2(double)
	   * @see #getP1()
	   */
	  public double getP2();

	  /**
	   * Set the second tangential distortion parameter <i>P<sub>2</sub></i>.
	   * @param p2 the second tangential distortion parameter <i>P<sub>2</sub></i>.
	   * @see #getP2()
	   * @see #setP1(double)
	   */
	  public void setP2(double p2);

	  /**
	   * Get the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
	   * @return the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
	   * @see #setS1(double)
	   * @see #getS2()
	   * @see #getS3()
	   * @see #getS4()
	   */
	  public double getS1();

	  /**
	   * Set the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
	   * @param s1 the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
	   * @see #getS1()
	   * @see #setS2(double)
	   * @see #setS3(double)
	   * @see #setS4(double)
	   */
	  public void setS1(double s1);

	  /**
	   * Get the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
	   * @return the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
	   * @see #setS2(double)
	   * @see #getS1()
	   * @see #getS3()
	   * @see #getS4()
	   */
	  public double getS2();

	  /**
	   * Set the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
	   * @param s2 the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
	   * @see #getS2()
	   * @see #setS1(double)
	   * @see #setS3(double)
	   * @see #setS4(double)
	   */
	  public void setS2(double s2);

	  /**
	   * Get the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
	   * @return the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
	   * @see #setS3(double)
	   * @see #getS1()
	   * @see #getS2()
	   * @see #getS4()
	   */
	  public double getS3();

	  /**
	   * Set the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
	   * @param s3 the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
	   * @see #getS3()
	   * @see #setS1(double)
	   * @see #setS2(double)
	   * @see #setS4(double)
	   */
	  public void setS3(double s3);

	  /**
	   * Get the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
	   * @return the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
	   * @see #setS4(double)
	   * @see #getS1()
	   * @see #getS2()
	   * @see #getS3()
	   */
	  public double getS4();

	  /**
	   * Set the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
	   * @param s4 the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
	   * @see #getS4()
	   * @see #setS1(double)
	   * @see #setS2(double)
	   * @see #setS3(double)
	   */
	  public void setS4(double s4);

	  /**
	   * Get the x axis tilt angular parameter <i>T<sub>x</sub></i>.
	   * @return the x axis tilt angular parameter <i>T<sub>x</sub></i>.
	   * @see #setTx(double)
	   * @see #getTy()
	   */
	  public double getTx();

	  /**
	   * Set the x axis tilt angular parameter <i>T<sub>x</sub></i>.
	   * @param tx the x axis tilt angular parameter <i>T<sub>x</sub></i>.
	   * @see #getTx()
	   * @see #setTy(double)
	   */
	  public void setTx(double tx);

	  /**
	   * Get the y axis tilt angular parameter <i>T<sub>y</sub></i>.
	   * @return the y axis tilt angular parameter <i>T<sub>y</sub></i>.
	   * @see #setTy(double)
	   * @see #getTx()
	   */
	  public double getTy();

	  /**
	   * Set the y axis tilt angular parameter <i>T<sub>y</sub></i>.
	   * @param ty the y axis tilt angular parameter <i>T<sub>y</sub></i>.
	   * @see #getTy()
	   * @see #setTx(double)
	   */
	  public void setTy(double ty);
}
