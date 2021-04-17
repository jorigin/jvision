package org.jvision.camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeometry.geom2D.point.Point2D;
import org.jvision.JVision;
import org.jvision.camera.distortion.LensDistortion;
import org.jvision.sensor.camera.DigitalCameraSensor;

/**
 * Default implementation of a {@link DigitalCamera}. 
 * For a complete description of the digital camera model, please refers to {@link DigitalCamera DigitialCamera interface} documentation.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class SimpleDigitalCamera implements DigitalCamera{

	private static int CAMERA_NUMBER = 1;
	
	private String name               = "";

	private int identifier            = CAMERA_NUMBER;
	
	private String model              = "";

	private String manufacturer       = "";

	private String serial             = "";

	private DigitalCameraSensor sensor = null;
	
	private float focalLength         = 1.0f;
	private double focalLengthMetric  = 0.0d; 

	private Point2D principalPointOffset = null;

	/**
	 * The affinity.
	 */
	private double affinity = 0.0d;

	/**
	 * The non-orthogonality (skew).
	 */
	private double skew = 0.0d;

	private LensDistortion distortion = null;

	private Map<String, Object> userProperties;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getIdentification() {
		return identifier;
	}

	@Override
	public void setIdentification(int indentifier) {
		this.identifier = indentifier;
	}

	@Override
	public Object getUserProperty(String propertyName) {
		if (userProperties != null) {
			return userProperties.get(propertyName);
		}
		return null;
	}

	@Override
	public Object setUserProperty(String name, Object value) {
		if (userProperties == null) {
			userProperties = new HashMap<String, Object>();
		}
		return userProperties.put(name, value);
	}

	@Override
	public List<String> getUserPropertyNames() {
		if (userProperties != null) {
			
			if ((userProperties.keySet() != null) && (userProperties.keySet().size() > 0)) {
				List<String> names = new ArrayList<String>(userProperties.keySet().size());
				
				for(String key : userProperties.keySet()) {
					names.add(key);
				}
				
				return names;	
			} else {
				return new ArrayList<String>();
			}
		}
		return null;
	}

	@Override
	public void clearUserProperties() {
		if (userProperties != null) {
			userProperties.clear();
		}
	}
	
	@Override
	public String getCameraModel() {
		return model;
	}

	@Override
	public void setCameraModel(String model) {
		this.model = model;
	}

	@Override
	public String getCameraManufacturer() {
		return manufacturer;
	}

	@Override
	public void setCameraManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Override
	public String getCameraSerialNumber() {
		return serial;
	}

	@Override
	public void setCameraSerialNumber(String serial) {
		this.serial = serial; 
	}

	@Override
	public float getFocalLenth() {
		return focalLength;
	}

	@Override
	public void setFocalLength(float focal) {
		this.focalLength = focal;
	}

	@Override
	public double getFocalLengthMetric() {
		return focalLengthMetric;
	}

	@Override
	public Point2D getPrincipalPointOffset() {
		return principalPointOffset;
	}

	@Override
	public void setPrincipalPointOffset(Point2D point) {
		this.principalPointOffset = point;
	}

	@Override
	public LensDistortion getLensDistortion() {
		return distortion;
	}

	@Override
	public void setLensDistortion(LensDistortion distortion) {
		this.distortion = distortion;
	}

	@Override
	public double getAffinity() {
		return affinity;
	}

	@Override
	public void setAffinity(double affinity) {
		this.affinity = affinity;
	}

	@Override
	public double getSkew() {
		return skew;
	}

	@Override
	public void setSkew(double skew) {
		this.skew = skew;
	}

	@Override
	public Point2D pointImageToCamera(Point2D pixel, boolean compenseDeformation) {
		// TODO implements SimpleDigitalCamera.pointImageToCamera(Point2D, boolean)
/*
		double x = 0.0d;
		double y = 0.0d;

		y = (pixel.getY() - getImageSize().getHeight()*0.5d - this.getPrincipalPointOffset().getX())/getFocalLenth();
*/
		return null;
	}

	@Override
	public Point2D pointCameraToImage(Point2D camera, boolean applyDeformation) {
		// TODO implements SimpleDigitalCamera.pointCameraToImage(Point2D, boolean)
		return null;
	}

	@Override
	public Point2D distort(Point2D point, boolean ppOffsetCorrection) {
		// TODO implements SimpleDigitalCamera.distort(Point2D point, boolean ppOffsetCorrection)
		return null;
	}

	@Override
	public Point2D undistort(Point2D point, boolean ppOffsetCorrection) {
		// TODO implements SimpleDigitalCamera.undistort(Point2D, boolean)
		return null;
	}

	/**
	 * Create a new digital camera with given parameters.
	 * @param sensor the {@link DigitalCameraSensor camera sensor}
	 * @param focalLength the focal length in pixels (px) of the camera
	 * @param principalPointOffset the offset of the principal point of the camera
	 * @param affinity the the differential scaling between the horizontal and vertical pixel spacings
	 * @param skew the non-orthogonality (skew coefficient)
	 * @param distortion the lens distortion that affect the camera
	 */
	public SimpleDigitalCamera(DigitalCameraSensor sensor, float focalLength, Point2D principalPointOffset, double affinity, double skew, LensDistortion distortion){
        this.sensor = sensor;

		this.focalLength = focalLength;
		this.principalPointOffset = principalPointOffset;

		this.affinity = affinity;
		this.skew = skew;

		this.distortion = distortion;

		name = "DigitalCamera_"+CAMERA_NUMBER;

		checkCamera();
	}

	/**
	 * Check and update camera internal data. This method is called when a camera parameter is changed. 
	 * If some parameter is not consistent (null or 0 image size, frame size, 0 or less focal length...), 
	 * an {@link IllegalArgumentException IllegalArgumentException} is raised.
	 */
	public void checkCamera(){
		//TODO Implements check camera
/*
		// Check the validity of the parameters.
		if (imageSize == null){
			if (imageSize.getWidth() <= 0){
				throw new IllegalArgumentException("Image width of the camera should be greater than 0px.");
			} else if (imageSize.getHeight() <= 0){
				throw new IllegalArgumentException("Image height of the camera should be greater than 0px.");
			}
		} else {
			throw new IllegalArgumentException("Image size of the camera should not be null.");
		}

		if (frameSize == null){
			if (frameSize.getWidth() <= 0){
				throw new IllegalArgumentException("Sensor frame width of the camera should be greater than 0mm.");
			} else if (frameSize.getHeight() <= 0){
				throw new IllegalArgumentException("Sensor frame height of the camera should be greater than 0mm.");
			}
		} else {
			throw new IllegalArgumentException("Image size of the camera should not be null.");
		}

		if (focalLength <= 0){
			throw new IllegalArgumentException("IThe focal length of the camera should be grater than 0mm.");
		}

		// Update internal data
		pixelWidth        = frameSize.getWidth() / imageSize.getWidth(); 
		pixelHeight       = frameSize.getHeight() / imageSize.getHeight();

		focalLengthMetric = focalLength * pixelWidth;
*/
	}

	@Override
	public DigitalCameraSensor getCameraSensor() {
		return sensor;
	}

	@Override
	public void setCameraSensor(DigitalCameraSensor sensor) {
		this.sensor = sensor;
	}
}
