package org.jvision.camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeometry.geom2D.point.Point2D;
import org.jvision.JVision;
import org.jvision.distortion.LensDistortion;
import org.jvision.sensor.camera.DigitalCameraSensor;

/**
 * A digital camera that describe an Agisoft (c) camera.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version}
 * @since 1.0.0
 */
public class AgisoftMetashapeCamera implements DigitalCamera{

	private static int CAMERA_NUMBER = 1;
	
	private String name = "AgisoftCamera";
	
	private int identifier            = CAMERA_NUMBER;
	
	private String model              = "";

	private String manufacturer       = "";

	private String serial             = "";

	private DigitalCameraSensor sensor = null;

	private float focalLength         = 1.0f;
	private double focalLengthMetric  = 0.0d; 

	private Point2D principalPointOffset = null;
	
	private double affinity = 0.0d;

	private double skew = 0.0d;

	private LensDistortion distortion = null;

	private Map<String, Object> userProperties;
	
	/**
	 * Create a new digital camera that represents an Agisoft(c) camera. Such a camera is used within Metashape(c) or AgiLens(c) softwares.
	 */
    public AgisoftMetashapeCamera() {
    	
    }
	
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

	@Override
	public DigitalCameraSensor getCameraSensor() {
		return sensor;
	}

	@Override
	public void setCameraSensor(DigitalCameraSensor sensor) {
		this.sensor = sensor;
	}
}
