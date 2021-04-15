package org.jvision.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
import org.jeometry.math.Matrix;
import org.jvision.JVision;

/**
 * A class dedicated to the handling of lens distortion within the OpenCV convention. 
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
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class OpenCVDistortion implements LensDistortion {

  /**
   * A flag that indicates that the radial distortion has a simple component (at least one of <i>K<sub>1</sub></i>, <i>K<sub>2</sub></i> or <i>K<sub>3</sub></i> is not equals to <i>0</i>.)
   * @see #RADIAL_RATIONAL
   * @see #TANGENTIAL
   * @see #PRISM
   * @see #TILT
   */
  public static final int RADIAL_SIMPLE   = 1;
  
  /**
   * A flag that indicates that the distortion has a rational component (at least one of <i>K<sub>4</sub></i>, <i>K<sub>5</sub></i> or <i>K<sub>6</sub></i> is not equals to <i>0</i>.)
   * @see #RADIAL_SIMPLE
   * @see #TANGENTIAL
   * @see #PRISM
   * @see #TILT
   */
  public static final int RADIAL_RATIONAL = 2;

  /**
   * A flag that indicates that the distortion has a tangential component (at least one of <i>P<sub>1</sub></i> or <i>P<sub>2</sub></i> is not equals to <i>0</i>.)
   * @see #RADIAL_SIMPLE
   * @see #RADIAL_RATIONAL
   * @see #PRISM
   * @see #TILT
   */
  public static final int TANGENTIAL      = 4;
  
  /**
   * A flag that indicates that the distortion has a prism component (at least one of <i>S<sub>1</sub></i>, <i>S<sub>2</sub></i>, <i>S<sub>3</sub></i> or <i>S<sub>4</sub></i> is not equals to <i>0</i>.)
   * @see #RADIAL_SIMPLE
   * @see #RADIAL_RATIONAL
   * @see #TANGENTIAL
   * @see #TILT
   */
  public static final int PRISM           = 8;

  /**
   * A flag that indicates that the distortion has a tilt component (at least one of <i>T<sub>x</sub></i>, <i>T<sub>y</sub></i> is not equals to <i>0</i>.)
   * @see #RADIAL_SIMPLE
   * @see #RADIAL_RATIONAL
   * @see #TANGENTIAL
   * @see #PRISM
   */
  public static final int TILT            = 16;
  
  private String convention = "opencv";
  
  private double epsilon       = 0.000001;
  
  private double iterationsMax = 50;
  
  // Radial simple coefficients
  private double k1 = 0.0d;
  private double k2 = 0.0d;
  private double k3 = 0.0d;
  
  // Radial rationnal coefficients
  private double k4 = 0.0d;
  private double k5 = 0.0d;
  private double k6 = 0.0d;
  
  // Tangential coefficients
  private double p1 = 0.0d;
  private double p2 = 0.0d;
  
  // Prism coefficient
  private double s1 = 0.0d;
  private double s2 = 0.0d;
  private double s3 = 0.0d;
  private double s4 = 0.0d;
  
  private double tx = 0.0d;
  private double ty = 0.0d;
  
  @Override
  public String getConvention() {
	  return convention;
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
  
  /**
   * Get the first radial distortion parameter <i>K<sub>1</sub></i>.
   * @return the first radial distortion parameter <i>K<sub>1</sub></i>.
   * @see #setK1(double)
   * @see #getK2()
   * @see #getK3()
   */
  public double getK1() {
    return k1;
  }

  /**
   * Set the first radial distortion parameter <i>K<sub>1</sub></i>.
   * @param k1 the first radial distortion parameter <i>K<sub>1</sub></i>.
   * @see #getK1()
   * @see #setK2(double)
   * @see #setK3(double)
   */
  public void setK1(double k1) {
    this.k1 = k1;
  }

  /**
   * Get the second radial distortion parameter <i>K<sub>2</sub></i>.
   * @return the second radial distortion parameter <i>K<sub>2</sub></i>.
   * @see #setK2(double)
   * @see #getK1()
   * @see #getK3()
   */
  public double getK2() {
    return k2;
  }

  /**
   * Set the second radial distortion parameter <i>K<sub>2</sub></i>.
   * @param k2 the second radial distortion parameter <i>K<sub>2</sub></i>.
   * @see #getK2()
   * @see #setK1(double)
   * @see #setK3(double)
   */
  public void setK2(double k2) {
    this.k2 = k2;
  }

  /**
   * Get the third radial distortion parameter <i>K<sub>3</sub></i>.
   * @return the third radial distortion parameter <i>K<sub>3</sub></i>.
   * @see #setK3(double)
   * @see #getK1()
   * @see #getK2()
   */
  public double getK3() {
    return k3;
  }

  /**
   * Set the third radial distortion parameter <i>K<sub>3</sub></i>.
   * @param k3 the third radial distortion parameter <i>K<sub>3</sub></i>.
   * @see #getK3()
   * @see #setK1(double)
   * @see #setK2(double)
   */
  public void setK3(double k3) {
    this.k3 = k3;
  }

  /**
   * Get the fourth radial distortion parameter <i>K<sub>4</sub></i>.
   * @return the fourth radial distortion parameter <i>K<sub>4</sub></i>.
   * @see #setK4(double)
   * @see #getK5()
   * @see #getK6()
   */
  public double getK4() {
    return k4;
  }

  /**
   * Set the fourth radial distortion parameter <i>K<sub>4</sub></i>.
   * @param k4 the fourth radial distortion parameter <i>K<sub>4</sub></i>.
   * @see #getK4()
   * @see #setK5(double)
   * @see #setK6(double)
   */
  public void setK4(double k4) {
    this.k4 = k4;
  }

  /**
   * Get the fifth radial distortion parameter <i>K<sub>5</sub></i>.
   * @return the fourth radial distortion parameter <i>K<sub>5</sub></i>.
   * @see #setK5(double)
   * @see #getK4()
   * @see #getK6()
   */
  public double getK5() {
    return k5;
  }

  /**
   * Set the fifth radial distortion parameter <i>K<sub>5</sub></i>.
   * @param k5 the fourth radial distortion parameter <i>K<sub>5</sub></i>.
   * @see #getK5()
   * @see #setK4(double)
   * @see #setK6(double)
   */
  public void setK5(double k5) {
    this.k5 = k5;
  }

  /**
   * Get the sixth radial distortion parameter <i>K<sub>6</sub></i>.
   * @return the fourth radial distortion parameter <i>K<sub>6</sub></i>.
   * @see #setK6(double)
   * @see #getK4()
   * @see #getK5()
   */
  public double getK6() {
    return k6;
  }

  /**
   * Set the sixth radial distortion parameter <i>K<sub>6</sub></i>.
   * @param k6 the fourth radial distortion parameter <i>K<sub>6</sub></i>.
   * @see #getK6()
   * @see #setK4(double)
   * @see #setK5(double)
   */
  public void setK6(double k6) {
    this.k6 = k6;
  }

  /**
   * Get the first tangential distortion parameter <i>P<sub>1</sub></i>.
   * @return the first tangential distortion parameter <i>P<sub>1</sub></i>.
   * @see #setP1(double)
   * @see #getP2()
   */
  public double getP1() {
    return p1;
  }

  /**
   * Set the first tangential distortion parameter <i>P<sub>1</sub></i>.
   * @param p1 the first tangential distortion parameter <i>P<sub>1</sub></i>.
   * @see #getP1()
   * @see #setP2(double)
   */
  public void setP1(double p1) {
    this.p1 = p1;
  }

  /**
   * Get the second tangential distortion parameter <i>P<sub>2</sub></i>.
   * @return the second tangential distortion parameter <i>P<sub>2</sub></i>.
   * @see #setP2(double)
   * @see #getP1()
   */
  public double getP2() {
    return p2;
  }

  /**
   * Set the second tangential distortion parameter <i>P<sub>2</sub></i>.
   * @param p2 the second tangential distortion parameter <i>P<sub>2</sub></i>.
   * @see #getP2()
   * @see #setP1(double)
   */
  public void setP2(double p2) {
    this.p2 = p2;
  }

  /**
   * Get the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
   * @return the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
   * @see #setS1(double)
   * @see #getS2()
   * @see #getS3()
   * @see #getS4()
   */
  public double getS1() {
    return s1;
  }

  /**
   * Set the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
   * @param s1 the first thin prism distortion coefficient <i>S<sub>1</sub></i>.
   * @see #getS1()
   * @see #setS2(double)
   * @see #setS3(double)
   * @see #setS4(double)
   */
  public void setS1(double s1) {
    this.s1 = s1;
  }

  /**
   * Get the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
   * @return the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
   * @see #setS2(double)
   * @see #getS1()
   * @see #getS3()
   * @see #getS4()
   */
  public double getS2() {
    return s2;
  }

  /**
   * Set the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
   * @param s2 the second thin prism distortion coefficient <i>S<sub>2</sub></i>.
   * @see #getS2()
   * @see #setS1(double)
   * @see #setS3(double)
   * @see #setS4(double)
   */
  public void setS2(double s2) {
    this.s2 = s2;
  }

  /**
   * Get the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
   * @return the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
   * @see #setS3(double)
   * @see #getS1()
   * @see #getS2()
   * @see #getS4()
   */
  public double getS3() {
    return s3;
  }

  /**
   * Set the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
   * @param s3 the third thin prism distortion coefficient <i>S<sub>3</sub></i>.
   * @see #getS3()
   * @see #setS1(double)
   * @see #setS2(double)
   * @see #setS4(double)
   */
  public void setS3(double s3) {
    this.s3 = s3;
  }

  /**
   * Get the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
   * @return the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
   * @see #setS4(double)
   * @see #getS1()
   * @see #getS2()
   * @see #getS3()
   */
  public double getS4() {
    return s4;
  }

  /**
   * Set the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
   * @param s4 the fourth thin prism distortion coefficient <i>S<sub>4</sub></i>.
   * @see #getS4()
   * @see #setS1(double)
   * @see #setS2(double)
   * @see #setS3(double)
   */
  public void setS4(double s4) {
    this.s4 = s4;
  }

  /**
   * Get the x axis tilt angular parameter <i>T<sub>x</sub></i>.
   * @return the x axis tilt angular parameter <i>T<sub>x</sub></i>.
   * @see #setTx(double)
   * @see #getTy()
   */
  public double getTx() {
    return tx;
  }

  /**
   * Set the x axis tilt angular parameter <i>T<sub>x</sub></i>.
   * @param tx the x axis tilt angular parameter <i>T<sub>x</sub></i>.
   * @see #getTx()
   * @see #setTy(double)
   */
  public void setTx(double tx) {
    this.tx = tx;
  }

  /**
   * Get the y axis tilt angular parameter <i>T<sub>y</sub></i>.
   * @return the y axis tilt angular parameter <i>T<sub>y</sub></i>.
   * @see #setTy(double)
   * @see #getTx()
   */
  public double getTy() {
    return ty;
  }

  /**
   * Set the y axis tilt angular parameter <i>T<sub>y</sub></i>.
   * @param ty the y axis tilt angular parameter <i>T<sub>y</sub></i>.
   * @see #getTy()
   * @see #setTx(double)
   */
  public void setTy(double ty) {
    this.ty = ty;
  }
  
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
