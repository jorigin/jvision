package org.jvision.camera.distortion;

import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Vector;
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
 *   <li>(x', y') is a 3D point within camera referential that is not affected by the distortion.
 *   <li>(x'', y'') is a point within camera referential that is affected by the distortion.
 *   <li>r<sup>2</sup> = x'<sup>2</sup> + y'<sup>2</sup>.
 *   <li>k<sub>1</sub>, ..., k<sub>6</sub> are the radial distortion coefficients.
 *   <li>p<sub>1</sub>, p<sub>2</sub> are the tangential distortion coefficients.
 *   <li>s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub> are the thin prism distortion coefficients.
 * </ul>
 * 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface LensDistortionOpenCV extends LensDistortion {

	/**
	 * Distort the given point (<i>x'</i>,&nbsp;<i>y'</i>) expressed wit focal normalized coordinates such as:<br>
	 *  <table>
     *  <caption>&nbsp;</caption>
     *  <tr><td>                                               </td><td> </td><td> </td><td style="text-align: center;">x&nbsp;-&nbsp;c<sub>x</sub></td><td>       </td><td style="text-align: center;">y&nbsp;-&nbsp;c<sub>y</sub></td><td></td></tr>
     *  <tr><td>(<i>x'</i>,&nbsp;<i>y'</i>)</td><td>=</td><td>(</td><td>          <hr>             </td><td>,&nbsp;</td><td>          <hr>             </td><td>)</td></tr>
     *  <tr><td>                                               </td><td> </td><td> </td><td style="text-align: center;">f<sub>x</sub>              </td><td>       </td><td style="text-align: center;">f<sub>y</sub>              </td><td></td></tr>
     *  </table>
     *  where:<br>
     *  <ul>
     *  <li>(<i>x</i>,&nbsp;<i>y</i>) are the coordinates of the point on the undistorted image in pixels (px)
     *  <li>(<i>c<sub>x</sub></i>,&nbsp;<i>c<sub>y</sub></i>) are the coordinates of the projection center on the image in pixels (px)
     *  <li><i>f<sub>x</sub></i>, <i>f<sub>y</sub></i> are respectively the focal lengths along <i>X</i> and <i>Y</i> axis in pixels (px)
     *  </ul>
     * 
     *  The distorted point (x'', y'') is such that:<br>
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
     * <li>(x', y') is the undistorted point expressed within focal normalized coordinates.
     * <li>(x'', y'') is a point expressed within focal normalized coordinates that is affected by the distortion.
     * <li>r<sup>2</sup> = x'<sup>2</sup> + y'<sup>2</sup>.
     * <li>k<sub>1</sub>, ..., k<sub>6</sub> are the radial distortion coefficients.
     * <li>p<sub>1</sub>, p<sub>2</sub> are the tangential distortion coefficients.
     * <li>s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub> are the thin prism distortion coefficients.
     * </ul>
     *  
	 * If the <code>undistorted</code> point is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param undistorted the input point within focal normalized coordinates. 
	 * @return the distorted point.
	 * @see #undistort(Point2D)
	 */
	@Override
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
	@Override
	public Point2D distort(Point2D undistorted, Point2D distorted);

	/**
	 * Undistort the given point. The point have to be expressed within image coordinates.
	 * If the input is <code>null</code>, coordinates of the result are set to {@link Double#NaN}.
	 * @param distorted the input point within image coordinates. 
	 * @return the undistorted point. 
	 * @see #distort(Point2D)
	 */
	@Override
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
	@Override
	public Point2D undistort(Point2D distorted, Point2D undistorted);
	
	/**
	 * Get the distortion parameters as the {@link Vector vector} 
	 * (k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>) (14 dimensions). 
	 * @return the distortion parameters
	 */
	@Override
	public Vector getDistortionCoefficients();

	/**
	 * Get the distortion parameters as a {@link Vector vector} by filling the given <code>parameters</code>. 
	 * Possible output vectors are:
	 * <ul>
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>) (14 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>) (12 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>) (8 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>) (5 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>) (4 dimensions)
	 * </ul>
	 * @param coefficients the output vector that has to store distortion parameters
	 * @return a reference on the given vector, for chaining purposes
	 * @throws IllegalArgumentException if the given vector does match the distortion implementation requirement
	 */
	@Override
	public Vector getDistortionCoefficients(Vector coefficients) throws IllegalArgumentException;

	/**
	 * Set the distortion coefficients from the given {@link Vector vector}. 
	 * Possible vectors are:
	 * <ul>
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>) (14 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>) (12 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>) (8 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>) (5 dimensions)
	 *   <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>) (4 dimensions)
	 * <li>null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input vector does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(Vector coefficients) throws IllegalArgumentException;

	/**
	 * Get the distortion coefficients as a <code>float</code> array. 
	 * Possible output arrays are:
	 * <ul>
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * </ul>
	 * @return the distortion coefficients
	 */
	@Override
	public float[] getDistortionCoefficientsFloat();

	/**
	 * Get the distortion parameters as a <code>float</code> array by filling the given <code>coefficients</code>. 
	 * Possible output arrays are:
	 * <ul>
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * </ul>
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	@Override
	public float[] getDistortionCoefficientsFloat(float[] coefficients) throws IllegalArgumentException;

	/**
	 * Set the distortion coefficients from the given <code>float</code> array. 
	 * Possible arrays are:
	 * <ul>
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 *   <li>null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(float[] coefficients) throws IllegalArgumentException;

	/**
	 * Get the distortion coefficients as a <code>double</code> array [k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14).
	 * @return the distortion coefficients
	 */
	@Override
	public double[] getDistortionCoefficientsDouble();

	/**
	 * Get the distortion parameters as a <code>double</code> array by filling the given <code>coefficients</code>. 
	 * Possible output arrays are:
	 * <ul>
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * </ul>
	 * @param coefficients the output array that has to store distortion coefficients
	 * @return a reference on the given array, for chaining purposes
	 * @throws IllegalArgumentException if the given array does match the distortion implementation requirement
	 */
	@Override
	public double[] getDistortionCoefficientsDouble(double[] coefficients) throws IllegalArgumentException;

	/**
	 * Set the distortion coefficients from the given <code>double</code> array. 
	 * Possible arrays are:
	 * <ul>
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 *   <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 *   <li>null or empty array for no distortion
     * </ul>
	 * @param coefficients the distortion coefficients
	 * @throws IllegalArgumentException if the input array does match the distortion implementation requirement
	 */
	@Override
	public void setDistortionCoefficients(double[] coefficients) throws IllegalArgumentException;

	/**
	 * Get the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @return the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @see #setK1(double)
	 * @see #getK2()
	 * @see #getK3()
	 */
	public double getK1();

	/**
	 * Set the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @param k1 the first radial distortion coefficient <i>K<sub>1</sub></i>.
	 * @see #getK1()
	 * @see #setK2(double)
	 * @see #setK3(double)
	 */
	public void setK1(double k1);

	/**
	 * Get the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @return the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @see #setK2(double)
	 * @see #getK1()
	 * @see #getK3()
	 */
	public double getK2();
	/**
	 * Set the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @param k2 the second radial distortion coefficient <i>K<sub>2</sub></i>.
	 * @see #getK2()
	 * @see #setK1(double)
	 * @see #setK3(double)
	 */
	public void setK2(double k2);

	/**
	 * Get the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @return the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @see #setK3(double)
	 * @see #getK1()
	 * @see #getK2()
	 */
	public double getK3();

	/**
	 * Set the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @param k3 the third radial distortion coefficient <i>K<sub>3</sub></i>.
	 * @see #getK3()
	 * @see #setK1(double)
	 * @see #setK2(double)
	 */
	public void setK3(double k3);

	/**
	 * Get the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @return the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @see #setK4(double)
	 * @see #getK5()
	 * @see #getK6()
	 */
	public double getK4();

	/**
	 * Set the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @param k4 the fourth radial distortion coefficient <i>K<sub>4</sub></i>.
	 * @see #getK4()
	 * @see #setK5(double)
	 * @see #setK6(double)
	 */
	public void setK4(double k4);

	/**
	 * Get the fifth radial distortion coefficient <i>K<sub>5</sub></i>.
	 * @return the fourth radial distortion coefficient <i>K<sub>5</sub></i>.
	 * @see #setK5(double)
	 * @see #getK4()
	 * @see #getK6()
	 */
	public double getK5();

	/**
	 * Set the fifth radial distortion coefficient <i>K<sub>5</sub></i>.
	 * @param k5 the fourth radial distortion coefficient <i>K<sub>5</sub></i>.
	 * @see #getK5()
	 * @see #setK4(double)
	 * @see #setK6(double)
	 */
	public void setK5(double k5);

	/**
	 * Get the sixth radial distortion coefficient <i>K<sub>6</sub></i>.
	 * @return the fourth radial distortion coefficient <i>K<sub>6</sub></i>.
	 * @see #setK6(double)
	 * @see #getK4()
	 * @see #getK5()
	 */
	public double getK6();

	/**
	 * Set the sixth radial distortion coefficient <i>K<sub>6</sub></i>.
	 * @param k6 the fourth radial distortion coefficient <i>K<sub>6</sub></i>.
	 * @see #getK6()
	 * @see #setK4(double)
	 * @see #setK5(double)
	 */
	public void setK6(double k6);

	/**
	 * Get the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @return the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @see #setP1(double)
	 * @see #getP2()
	 */
	public double getP1();

	/**
	 * Set the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @param p1 the first tangential distortion coefficient <i>P<sub>1</sub></i>.
	 * @see #getP1()
	 * @see #setP2(double)
	 */
	public void setP1(double p1);

	/**
	 * Get the second tangential distortion coefficient <i>P<sub>2</sub></i>.
	 * @return the second tangential distortion coefficient <i>P<sub>2</sub></i>.
	 * @see #setP2(double)
	 * @see #getP1()
	 */
	public double getP2();

	/**
	 * Set the second tangential distortion coefficient <i>P<sub>2</sub></i>.
	 * @param p2 the second tangential distortion coefficient <i>P<sub>2</sub></i>.
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
	 * Get the x axis tilt angular coefficient <i>T<sub>x</sub></i>.
	 * @return the x axis tilt angular coefficient <i>T<sub>x</sub></i>.
	 * @see #setTx(double)
	 * @see #getTy()
	 */
	public double getTx();

	/**
	 * Set the x axis tilt angular coefficient <i>T<sub>x</sub></i>.
	 * @param tx the x axis tilt angular coefficient <i>T<sub>x</sub></i>.
	 * @see #getTx()
	 * @see #setTy(double)
	 */
	public void setTx(double tx);

	/**
	 * Get the y axis tilt angular coefficient <i>T<sub>y</sub></i>.
	 * @return the y axis tilt angular coefficient <i>T<sub>y</sub></i>.
	 * @see #setTy(double)
	 * @see #getTx()
	 */
	public double getTy();

	/**
	 * Set the y axis tilt angular coefficient <i>T<sub>y</sub></i>.
	 * @param ty the y axis tilt angular coefficient <i>T<sub>y</sub></i>.
	 * @see #getTy()
	 * @see #setTx(double)
	 */
	public void setTy(double ty);

	/**
	 * Set the distortion coefficients.
	 * @param k1 the first radial coefficient
	 * @param k2 the second radial coefficient
	 * @param k3 the third radial coefficient
	 * @param k4 the first radial rational coefficient
	 * @param k5 the second radial rational coefficient
	 * @param k6 the third radial rational coefficient
	 * @param p1 the first tangential coefficient
	 * @param p2 the second tangential coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 * @param tx the x tilt distortion coefficient
	 * @param ty the y tilt distortion coefficient
	 */
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2, 
			double s1, double s2, double s3, double s4,
			double tx, double ty);

	/**
	 * Set the radial, tangential and thin prism distortion coefficients. 
	 * The tilt distortion coefficients &tau;<sub>x</sub> and &tau;<sub>x</sub> are set to 0.
	 * @param k1 the first radial coefficient
	 * @param k2 the second radial coefficient
	 * @param k3 the third radial coefficient
	 * @param k4 the first radial rational coefficient
	 * @param k5 the second radial rational coefficient
	 * @param k6 the third radial rational coefficient
	 * @param p1 the first tangential coefficient
	 * @param p2 the second tangential coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 */
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2, 
			double s1, double s2, double s3, double s4);

	/**
	 * Set the radial and tangential distortion coefficients. 
	 * The thin prism and the tilt distortion coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub> and &tau;<sub>x</sub> are set to 0.
	 * @param k1 the first radial coefficient
	 * @param k2 the second radial coefficient
	 * @param k3 the third radial coefficient
	 * @param k4 the first radial rational coefficient
	 * @param k5 the second radial rational coefficient
	 * @param k6 the third radial rational coefficient
	 * @param p1 the first tangential coefficient
	 * @param p2 the second tangential coefficient
	 */
	public void setDistortionCoefficients(double k1, double k2, double k3, double k4, double k5, double k6,
			double p1, double p2);

	/**
	 * Set the radial and tangential distortion coefficients. 
	 * The radial rational, thin prism and the tilt distortion coefficients k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub> and &tau;<sub>x</sub> are set to 0.
	 * @param k1 the first radial coefficient
	 * @param k2 the second radial coefficient
	 * @param k3 the third radial coefficient
	 * @param p1 the first tangential coefficient
	 * @param p2 the second tangential coefficient
	 */
	public void setDistortionCoefficients(double k1, double k2, double k3, double p1, double p2);
}
