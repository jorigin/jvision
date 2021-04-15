package org.jvision.sensor.camera;

import org.jeometry.geom2D.point.Point2D;
import org.jvision.sensor.Sensor;

/**
 * A digital camera {@link Sensor sensor}. Such a sensor is made of a rectangular frame that can produce digital images. 
 * @author Julien Seinturier - COMEX S.A. - http://www.seinturier.fr
 */
public interface DigitalCameraSensor extends Sensor {

	/**
	 * Get the width in millimeters (mm) of the sensor frame.
	 * @return the width in millimeters (mm) of the sensor frame.
	 * @see #getFrameHeight()
	 */
	public float getFrameWidth();

	/**
	 * Get the height in millimeters (mm) of the sensor frame.
	 * @return the height in millimeters (mm) of the sensor frame.
	 * @see #getFrameWidth()
	 */
	public float getFrameHeight();

	/**
	 * Get the width in pixels (px) of the images produced by the sensor.
	 * @return the width in pixels (px) of the images produced by the sensor.
	 * @see #getImageHeight()
	 */
	public int getImageWidth();

	/**
	 * Get the height in pixels (px) of the images produced by the sensor.
	 * @return the height in pixels (px) of the images produced by the sensor.
	 * @see #getImageWidth()
	 */
	public int getImageHeight();
	
	/**
	 * Get the width in microns (&mu;m) of an image pixel (ie the width of the cell on the sensor frame that represents a pixel).
	 * @return the width in microns (&mu;m) of an image pixel (ie the width of the cell on the sensor frame that represents a pixel).
	 * @see #getPixelHeight()
	 */
	public float getPixelWidth();
	
	/**
	 * Get the height in microns (&mu;m) of an image pixel (ie the height of the cell on the sensor frame that represents a pixel).
	 * @return the height in microns (&mu;m) of an image pixel (ie the height of the cell on the sensor frame that represents a pixel).
	 * @see #getPixelWidth()
	 */
	public float getPixelHeight();
	
	/**
	 * Set the sensor image and frame sizes. The pixel size is computed from the input values. 
	 * @param frameWidth the width in millimeters (mm) of the sensor frame.
	 * @param frameHeight the width in millimeters (mm) of the sensor height.
	 * @param imageWidth the width in pixels (px) of the images produced by the sensor.
	 * @param imageHeight the height in pixels (px) of the images produced by the sensor.
	 * @see #setSensorSize(float, float, float, float)
	 * @see #setSensorSize(int, int, float, float)
	 */
	public void setSensorSize(float frameWidth, float frameHeight, int imageWidth, int imageHeight);
	
	/**
	 * Set the sensor frame and pixel sizes. The image size is computed from the input values. 
	 * @param frameWidth the width in microns (&mu;m) of the sensor frame.
	 * @param frameHeight the width in microns (&mu;m) of the sensor height.
	 * @param pixelWidth the width in microns (&mu;m) of an image pixel (ie the width of the cell on the sensor frame that represents a pixel).
	 * @param pixelHeight the height in microns (&mu;m) of an image pixel (ie the height of the cell on the sensor frame that represents a pixel).
	 * @see #setSensorSize(float, float, int, int)
	 * @see #setSensorSize(int, int, float, float)
	 */
	public void setSensorSize(float frameWidth, float frameHeight, float pixelWidth, float pixelHeight);
	
	/**
	 * Set the sensor image and pixel sizes. The frame size is computed from the input values. 
	 * @param imageWidth the width in pixels (px) of the images produced by the sensor.
	 * @param imageHeight the height in pixels (px) of the images produced by the sensor.
	 * @param pixelWidth the width in microns (&mu;m) of an image pixel (ie the width of the cell on the sensor frame that represents a pixel).
	 * @param pixelHeight  the height in microns (&mu;m) of an image pixel (ie the height of the cell on the sensor frame that represents a pixel).
	 * @see #setSensorSize(float, float, int, int)
	 * @see #setSensorSize(float, float, float, float)
	 */
	public void setSensorSize(int imageWidth, int imageHeight, float pixelWidth, float pixelHeight);
		
	/**
	 * Set the sensor frame, image and pixel sizes. The frame size is computed from the input values. 
	 * @param frameWidth the width in millimeters (mm) of the sensor frame.
	 * @param frameHeight the width in millimeters (mm) of the sensor height.
	 * @param imageWidth the width in pixels (px) of the images produced by the sensor.
	 * @param imageHeight the height in pixels (px) of the images produced by the sensor.
	 * @param pixelWidth the width in microns (&mu;m) of an image pixel (ie the width of the cell on the sensor frame that represents a pixel).
	 * @param pixelHeight the height in microns (&mu;m) of an image pixel (ie the height of the cell on the sensor frame that represents a pixel).
	 */
	public void setSensorSize(float frameWidth, float frameHeight, int imageWidth, int imageHeight, float pixelWidth, float pixelHeight);
	
	/**
	 * Get the coordinate of the given image point (in pixel) on the camera sensor frame (in millimeters).<br>
	 * This method do not take in account any correction (distortion, principal point offset, ...).
	 * @param pixel coordinates in pixel (px) of the point.
	 * @return the coordinates in millimeters (mm) of the given point on the camera sensor frame.
	 * @see #pointFrameToImage(Point2D)
	 */
	public Point2D pointImageToFrame(Point2D pixel);

	/**
	 * Get the coordinate of the given point on the camera sensor frame (in millimeters) on an image (in pixels).<br>
	 * This method do not take in account any correction (distortion, principal point offset, ...).
	 * @param point the coordinates in millimeters (mm) of the given point on the camera sensor frame.
	 * @return the coordinates in pixel (px) of the point on an image produces by the camera.
	 */
	public Point2D pointFrameToImage(Point2D point);

}
