package org.jvision.sensor;

import org.jvision.JVision;

/**
 * A sensor represents an identified device that can produce data along modalities. 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 */
public interface Sensor {

	/**
	 * Get the identifier of the sensor.
	 * @return the identifier of the sensor
	 * @see #getIdentifier()
	 */
	public String getIdentifier();
	
	/**
	 * Set the identifier of the sensor.
	 * @param identifier the identifier of the sensor
	 * @see #getIdentifier()
	 */
	public void setIdentifier(String identifier);
	
	/**
	 * Get the type of the sensor.
	 * @return the type of the sensor
	 * @see #setSensorType(String)
	 */
	public String getSensorType();
	
	/**
	 * Set the type of the sensor.
	 * @param type the type of the sensor.
	 * @see #getSensorType()
	 */
	public void setSensorType(String type);
	
	/**
	 * Get the sensor model.
	 * @return the sensor model
	 * @see #setSensorModel(String)
	 */
	public String getSensorModel();
	
	/**
	 * Set the sensor model.
	 * @param model the sensor model
	 * @see #getSensorModel()
	 */
	public void setSensorModel(String model);
	
	/**
	 * Get the serial number of the sensor.
	 * @return the serial number of the sensor.
	 * @see #setSensorSerialNumber(String)
	 */
	public String getSensorSerialNumber();
	
	/**
	 * Set the serial number of the sensor.
	 * @param serialNumber the serial number of the sensor
	 * @see #getSensorSerialNumber()
	 */
	public void setSensorSerialNumber(String serialNumber);
	
	/**
	 * Get the sensor manufacturer.
	 * @return the sensor manufacturer
	 * @see #setSensorManufacturer(String)
	 */
	public String getSensorManufacturer();
	
	/**
	 * Set the sensor manufacturer.
	 * @param manufacturer the sensor manufacturer
	 * @see #getSensorManufacturer()
	 */
	public void setSensorManufacturer(String manufacturer);
}
