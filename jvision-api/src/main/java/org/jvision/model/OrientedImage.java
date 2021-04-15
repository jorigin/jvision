package org.jvision.model;

import org.jeometry.geom2D.point.Point2DContainer;
import org.jeometry.geom3D.transform.Transform3D;
import org.jorigin.identification.Identified;
import org.jorigin.identification.Named;
import org.jorigin.property.HandleUserProperties;
import org.jvision.JVision;
import org.jvision.camera.DigitalCamera;

/**
 * An oriented image is an image provided by a {@link DigitalCamera camera} that can be located within a 3D space using a {@link Transform3D 3D transformation}. 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface OrientedImage extends Named, Identified, HandleUserProperties {

	/**
	 * Get the {@link DigitalCamera camera} that has produced the image.
	 * @return the {@link DigitalCamera camera} that has produced the image.
	 * @see #setCamera(DigitalCamera)
	 */
	public DigitalCamera getCamera();
	
	/**
	 * Set the {@link DigitalCamera camera} that has produced the image.
	 * @param camera the {@link DigitalCamera camera} that has produced the image.
	 * @see #getCamera()
	 */
	public void setCamera(DigitalCamera camera);
	
	/**
	 * Get the {@link Transform3D 3D transformation} that enables to locate the image.
	 * @return the {@link Transform3D 3D transformation} that enables to locate the image.
	 * @see #setTransformation(Transform3D)
	 */
	public Transform3D getTransformation();
	
	/**
	 * Set the {@link Transform3D 3D transformation} that enables to locate the image.
	 * @param transformation the {@link Transform3D 3D transformation} that enables to locate the image.
	 * @see #getTransformation()
	 */
	public void setTransformation(Transform3D transformation);
	
	/**
	 * Get the observation that are attached to this image.
	 * @return the observation that are attached to this image
	 */
	public Point2DContainer getObservations();
}
