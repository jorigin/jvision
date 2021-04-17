package org.jvision.camera.distortion;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.geom2D.point.Point2D;
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
public class SimpleBrownDistortion implements BrownDistortion{

	/**
	 * The distortion convention.
	 */
	private String distortionConvention = "brown";

	/**
	 * The distortion components.
	 */
	private int distortionComponents        = LensDistortion.TYPE_NO_DISTORTION;

	/** The K1 parameter.*/ private double k1;
	/** The K2 parameter.*/ private double k2;
	/** The K3 parameter.*/ private double k3;
	/** The K4 parameter.*/ private double k4;

	/** The P1 parameter.*/ private double p1;
	/** The P2 parameter.*/ private double p2;
	/** The P3 parameter.*/ private double p3;
	/** The P4 parameter.*/ private double p4;

	/**
	 * The number of iterations for undistortion computation.
	 */
	private int iterationMax = 5;

	/**
	 * Create a new Brown based lens distortion with radial parameters (k1, k2, k3, k4) 
	 * and tangential parameters (p1, p2, p3, p4).
	 * @param convention the convention of the lens distortion
	 * @param k1 the first radial distortion parameter
	 * @param k2 the second radial distortion parameter
	 * @param k3 the third radial distortion parameter
	 * @param k4 the fourth radial distortion parameter
	 * @param p1 the first tangential distortion parameter
	 * @param p2 the second tangential distortion parameter
	 * @param p3 the third tangential distortion parameter
	 * @param p4 the fourth tangential distortion parameter
	 */
	public SimpleBrownDistortion(String convention, double k1, double k2, double k3, double k4, double p1, double p2, double p3, double p4){
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


	@Override
	public String getDistortionConvention() {
		return distortionConvention;
	}

	@Override
	public int getDistortionComponents() {
		return distortionComponents;
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
					if ((distortionComponents & LensDistortion.TYPE_TANGENTIAL) != 0){
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

}



