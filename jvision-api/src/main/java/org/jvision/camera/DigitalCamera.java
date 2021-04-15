package org.jvision.camera;

import org.jeometry.geom2D.point.Point2D;
import org.jorigin.identification.Identified;
import org.jorigin.identification.Named;
import org.jorigin.property.HandleUserProperties;
import org.jvision.JVision;
import org.jvision.distortion.LensDistortion;
import org.jvision.sensor.camera.DigitalCameraSensor;

/**
 * A digital camera. This interface describe a digital camera and its characteristics (intrinsics parameters). 
 * The camera model specifies the transformation from 3D point expressed within the camera coordinate system to the pixel coordinates in an image.
 * <b>Coordinates reference systems</b><br>
 * The camera coordinate system is such as follows:
 * <ul>
 * <li> its origin is the camera projection center;
 * <li> the Z axis points towards the viewing direction;
 * <li> the X axis points to the right;
 * <li> the Y axis points down.
 * </ul>
 * The image coordinate system is a computer graphics standard and is described such as follows:
 * <ul>
 * <li>its origin is the top left image pixel;
 * <li>the X axis in the image coordinate system points to the right;
 * <li>the Y axis points down.
 * </ul>
 * <b>Intrinsic parameters</b><br>
 * Camera model relies on set of parameters that are determined during a calibration process. These parameter are:
 * <ul>
 * <li><i>f</i> be the camera {@link #getFocalLenth() focal length} expressed in pixel (px)
 * <li>(<i>c<sub>x</sub></i>, <i>c<sub>y</sub></i>) the camera {@link #getPrincipalPointOffset() principal point offset} (the offset of the projection center on an image from the image center) expressed in pixel (px);
 * <li><i>image<sub>w</sub></i> / <i>image<sub>h</sub></i> respectively the the width / height of an image produced by this camera expressed in pixel (px);
 * <li><i>frame<sub>w</sub></i> / <i>frame<sub>h</sub></i> respectively the the width / height of the camera sensor frame expressed in millimeters (mm);
 * <li><i>affinity</i> (also denoted <i>B<sub>1</sub></i>) the the differential scaling between the horizontal and vertical pixel spacings;
 * <li><i>skew</i> (also denoted non-orthogonality coefficient, or <i>B<sub>2</sub></i>) the nonorthogonality (axial skew) between the x and y axes.
 * </ul>
 * <b>Lens distortion</b><br>
 * As digital camera relies on optical lens systems, camera integrate a {@link LensDistortion lens distortion model} 
 * that enable to apply / correct lens distortion. 
 * The distortion model provides {@link LensDistortion#distort(Point2D) distort()} and {@link LensDistortion#undistort(Point2D) undistort()} methods in order to respectively apply / correct the lens distortion.<br><br>
 * <b>Geometric transformations</b><br>
 * The transformation from 3D point to pixel relies on the camera intrinsic parameters. Let (X, Y, Z) a 3D point expressed within the camera coordinate system, we have:
 * <table>
 * <caption>&nbsp;</caption>
 * <tr><td>(1)</td><td>  (x, y) = (X / Z, Y / Z) </td><td>Perspective correction.</td></tr>
 * <tr><td>(2)</td><td>(x', y') = distort(x, y)</td><td>Applying distortion.</td></tr>
 * <tr><td>(3)</td><td>  (u, v) = (image<sub>w</sub> * 0.5 + c<sub>x</sub> + x'f + x'B<sub>1</sub> + y'B<sub>2</sub>, image<sub>h</sub> * 0.5 + c<sub>y</sub> + y'f)</td><td>Retrieve pixel coordinates</td></tr>
 * <tr><td></td><td></td></tr>
 * </table>
 * <br>
 * 
 * Camera model description can be found within document <a href="http://info.asprs.org/publications/proceedings/Sacramento2012/files/Fraser.pdf">Automatic Camera Calibration In Close-range Photogrammetry</a> 
 * by Clive S. Fraser.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface DigitalCamera extends Named, Identified, HandleUserProperties {

	/**
	 * Get the model of the camera.
	 * @return the model of the camera.
	 * @see #setCameraModel(String)
	 */
	public abstract String getCameraModel();

	/**
	 * Set the model of the camera.
	 * @param model the model of the camera.
	 */
	public abstract void setCameraModel(String model);

	/**
	 * Get the manufacturer of the camera.
	 * @return the manufacturer of the camera.
	 * @see #setCameraManufacturer(String)
	 */
	public abstract String getCameraManufacturer();

	/**
	 * Set the manufacturer of the camera.
	 * @param manufacturer the manufacturer of the camera.
	 * @see #getCameraManufacturer()
	 */
	public abstract void setCameraManufacturer(String manufacturer);

	/**
	 * Get the serial number of the camera.
	 * @return the serial number of the camera.
	 * @see #setCameraSerialNumber(String)
	 */
	public abstract String getCameraSerialNumber();

	/**
	 * Set the serial number of the camera.
	 * @param serial the serial number of the camera.
	 * @see #getCameraSerialNumber()
	 */
	public abstract void setCameraSerialNumber(String serial);

	/**
	 * Get the {@link DigitalCameraSensor sensor} of the camera. A digital camera sensor is defined by a frame size, an image size and a pixel size.
	 * @return the sensor of the camera
	 * @see #setCameraSensor(DigitalCameraSensor)
	 */
	public abstract DigitalCameraSensor getCameraSensor();

	/**
	 * Set the {@link DigitalCameraSensor sensor} of the camera. A digital camera sensor is defined by a frame size, an image size and a pixel size.
	 * @param sensor the sensor of the camera
	 * @see #getCameraSensor()
	 */
	public abstract void setCameraSensor(DigitalCameraSensor sensor);

	/**
	 * Get the focal length of the camera. The focal length unit is the pixel
	 * @return the focal length in pixels of the camera (px).
	 * @see #setFocalLength(float)
	 * @see #getFocalLengthMetric()
	 */
	public abstract float getFocalLenth();

	/**
	 * Set the focal length of the camera. The focal length unit is the pixel.
	 * @param focal the focal length in pixels of the camera (px).
	 */
	public abstract void setFocalLength(float focal);

	/**
	 * Get the focal length of the camera. The focal length unit is millimeters (mm).
	 * @return the focal length of the camera. The focal length unit is millimeters (mm).
	 * @see #getFocalLenth()
	 */
	public abstract double getFocalLengthMetric();

	/**
	 * Get the camera principal point offset.
	 * @return the camera principal point offset.
	 * @see #setPrincipalPointOffset(Point2D)
	 */
	public abstract Point2D getPrincipalPointOffset();

	/**
	 * Set the camera principal point.
	 * @param point the camera principal point.
	 * @see #getPrincipalPointOffset()
	 */
	public abstract void setPrincipalPointOffset(Point2D point);

	/**
	 * Get the differential scaling between the horizontal and vertical pixel spacings (affinity). 
	 * Within some literature, this parameter is called <i>B<sub>1</sub></i>.
	 * @return the differential scaling between the horizontal and vertical pixel spacings.
	 * @see #setAffinity(double)
	 */
	public abstract double getAffinity();

	/**
	 * Set the differential scaling between the horizontal and vertical pixel spacings (affinity). 
	 * Within some literature, this parameter is called <i>B<sub>1</sub></i>.
	 * @param affinity the differential scaling between the horizontal and vertical pixel spacings.
	 */
	public abstract void setAffinity(double affinity);

	/**
	 * Set the nonorthogonality (axial skew) between the x and y axes.
	 * Within some literature, this parameter is called <i>B<sub>2</sub></i>.
	 * @return the nonorthogonality (axial skew) between the x and y axes.
	 * @see #setSkew(double)
	 */
	public abstract double getSkew();

	/**
	 * Set the nonorthogonality (axial skew) between the x and y axes.
	 * Within some literature, this parameter is called <i>B<sub>2</sub></i>.
	 * @param nonOrthogonality the nonorthogonality (axial skew) between the x and y axes.
	 * @see #getSkew()
	 */
	public abstract void setSkew(double nonOrthogonality);

	/**
	 * Get the {@link org.jvision.distortion.LensDistortion lens distortion} attached to this camera.
	 * @return the {@link LensDistortion lens distortion} attached to this camera.
	 * @see #setLensDistortion(LensDistortion)
	 * @see org.jvision.distortion.LensDistortion
	 */
	public abstract LensDistortion getLensDistortion();

	/**
	 * Set the {@link org.jvision.distortion.LensDistortion lens distortion} attached to this camera.
	 * @param distortion the {@link LensDistortion lens distortion} attached to this camera.
	 * @see #getLensDistortion()
	 * @see org.jvision.distortion.LensDistortion
	 */
	public abstract void setLensDistortion(LensDistortion distortion);

	/**
	 * Get the coordinate of the given image point (in pixel) within the camera coordinates system.<br>
	 * The camera coordinate system is such as follows:
	 * <ul>
	 * <li> its origin is the camera projection center;
	 * <li> the Z axis points towards the viewing direction;
	 * <li> the X axis points to the right;
	 * <li> the Y axis points down.
	 * </ul>
	 * <br>
	 * This method can be used in two ways.<br><br> 
	 * If the parameter <code>compenseDeformation</code> is set to <code>true</code>, for a given pixel \((u,\ v)\) the coordinates of the corresponding point \((x,\ y)\) within the camera coordinate system are obtained as follows:<br>
	 * <table>
	 * <caption>&nbsp;</caption>
	 * <tr><td>(1)</td><td>\(y' = \frac{v\ -\ image_{h}\times{}0.5\ -\ c_{y}}{f}\)</td></tr>
	 * <tr><td>   </td><td>\(x' = \frac{u\ -\ image_{w}\ \times\ 0.5\ -\ c_{x}\ -\ y'B_{2}}{f\ +\ B_{1}}\)</td></tr>
	 * <tr><td>(2)</td><td>\((x, y)\ =\ undistort(x', y')\)</td></tr>
	 * </table>
	 * <br>
	 * On the other hand, if the parameter <code>compenseDeformation</code> is set to <code>false</code>, for a given pixel \((u,\ v)\) the coordinates of the corresponding point \((x,\ y)\) within the camera coordinate system are obtained as follows:<br>
	 * <table>
	 * <caption>&nbsp;</caption>
	 * <tr><td>(1)</td><td>\(y = \frac{v\ -\ image_{h}\times{}0.5}{f}\)</td></tr>
	 * <tr><td>   </td><td>\(x = \frac{u\ -\ image_{w}\ \times\ 0.5\\ -\ y'B_{2}}{f\ +\ B_{1}}\)</td></tr>
	 * </table>
	 * <br>
	 * The first case is used if the input point come from an original produced image. The second case should be used if the input pint come from an already corrected image.
	 * @param pixel coordinates in pixel (px) of the point.
	 * @param compenseDeformation is <code>true</code> if this method has to take in account deformation (principal point offset and lens distortion) during the computation. 
	 * This is the case if the input point comes from the original image produced. This parameter has to be set to <code>false</code> if the input point comes from an already corrected images 
	 * (with no principal point offset and no distortion).
	 * @return the coordinates of the point within the camera coordinate system.
	 * @see #pointCameraToImage(Point2D, boolean)
	 */
	public Point2D pointImageToCamera(Point2D pixel, boolean compenseDeformation);

	/**
	 * Get the coordinate of the given image point (in camera coordinates system) within the image coordinates system.<br>
	 * The camera coordinate system is such as follows:
	 * 
	 * @param camera a point expressed within the camera coordinates system.
	 * @param applyDeformation is <code>true</code> if this method has to take in account deformation (principal point offset and lens distortion) during the computation. 
	 * This is the case if the output point has to be expressed on an original image (as its come from the camera). This parameter has to be set to <code>false</code> if the output point has to be expressed within corrected images 
	 * (with no principal point offset and no distortion).
	 * @return the coordinates of the point within the image coordinates system.
	 * @see #pointImageToCamera(Point2D, boolean)
	 */
	public Point2D pointCameraToImage(Point2D camera, boolean applyDeformation);

	/**
	 * Apply lens distortion to the given point expressed within image coordinates system (in pixel). 
	 * The returned point integrate the distortion and is also expressed within image coordinates system (in pixel). <br>
	 * This method aims to retrieve the position of the point in an original image from an undistorted one. 
	 * The distortion is applied by the underlying {@link #getLensDistortion() lens distortion}.
	 * @param point a point that is not affected by the distortion expressed within image coordinates system (in pixel). 
	 * @param ppOffsetCorrection specifies if the method has to process to the principal point offset correction. If <code>true</code>, 
	 * it is considered that the input <code>point</code> does not integrate principal point offset correction and so this method will make it. 
	 * If <code>false</code>, it is considered that the input point integrates the principal point offset correction and so this method does not make it. 
	 * In the two cases, the returned point as the same integration of the principal point offset than the input point.
	 * @return the input point affected by the distortion expressed within image coordinates system (in pixel). 
	 * @see #undistort(Point2D, boolean)
	 */
	public Point2D distort(Point2D point, boolean ppOffsetCorrection);

	/**
	 * Remove lens distortion from the given point expressed within image coordinates system (in pixel). 
	 * The returned point does no more integrate the distortion and is also expressed within image coordinates system (in pixel). <br>
	 * This method aims to retrieve the position of the point in an undistorted image from an distorted one. 
	 * The distortion is corrected by the the underlying {@link #getLensDistortion() lens distortion}.
	 * @param point a point that integrates the distortion expressed within image coordinates system (in pixel). 
	 * @param ppOffsetCorrection specifies if the method has to process to the principal point offset correction. If <code>true</code>, 
	 * it is considered that the input <code>point</code> does not integrate principal point offset correction and so this method will make it. 
	 * If <code>false</code>, it is considered that the input point integrates the principal point offset correction and so this method does not make it. 
	 * In the two cases, the returned point as the same integration of the principal point offset than the input point.
	 * @return the input point not affected by the distortion expressed within image coordinates system (in pixel). 
	 * @see #distort(Point2D, boolean)
	 */
	public Point2D undistort(Point2D point, boolean ppOffsetCorrection);
}
