package org.jvision.factory;

import org.jeometry.math.Vector;
import org.jvision.JVision;
import org.jvision.camera.distortion.LensDistortionBrown;
import org.jvision.camera.distortion.LensDistortionMetashape;
import org.jvision.camera.distortion.LensDistortionOpenCV;
import org.jvision.camera.distortion.SimpleLensDistortionBrown;
import org.jvision.camera.distortion.SimpleLensDistortionMetashape;
import org.jvision.camera.distortion.SimpleLensDistortionOpenCV;

/**
 * An implementation of {@link CameraBuilder camera builder} that provides instances from the simple implementation.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 *
 */
public class SimpleCameraBuilder implements CameraBuilder {

	@Override
	public LensDistortionBrown createLensDistortionBrown() {
		return new SimpleLensDistortionBrown();
	}

	@Override
	public LensDistortionBrown createLensDistortionBrown(double k1, double k2, double k3, double k4, 
			                                             double p1, double p2, double p3, double p4) {
		return new SimpleLensDistortionBrown(k1, k2, k3, k4, p1, p2, p3, p4);
	}

	@Override
	public LensDistortionBrown createLensDistortionBrown(Vector parameters) throws IllegalArgumentException {
		return new SimpleLensDistortionBrown(parameters);
	}

	@Override
	public LensDistortionBrown createLensDistortionBrown(float[] parameters) throws IllegalArgumentException {
		return new SimpleLensDistortionBrown(parameters);
	}

	@Override
	public LensDistortionBrown createLensDistortionBrown(double[] parameters) throws IllegalArgumentException {
		return new SimpleLensDistortionBrown(parameters);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV() {
		return new SimpleLensDistortionOpenCV();
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2, double s1, double s2, double s3, double s4, double tx, double ty) {
		return new SimpleLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4, tx, ty);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2, double s1, double s2, double s3, double s4) {
		return new SimpleLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, double k4, double k5,
			double k6, double p1, double p2) {
		return new SimpleLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, double p1, double p2) {
		return new SimpleLensDistortionOpenCV(k1, k2, k3, p1, p2);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(Vector coefficients) throws IllegalArgumentException {
		return new SimpleLensDistortionOpenCV(coefficients);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(float[] coefficients) throws IllegalArgumentException {
	    return new SimpleLensDistortionOpenCV(coefficients);
	}

	@Override
	public LensDistortionOpenCV createLensDistortionOpenCV(double[] coefficients) throws IllegalArgumentException {
		return new SimpleLensDistortionOpenCV(coefficients);
	}

	@Override
	public LensDistortionMetashape createLensDistortionMetashape() {
		return new SimpleLensDistortionMetashape();
	}

	@Override
	public LensDistortionMetashape createLensDistortionMetashape(double k1, double k2, double k3, double k4, double p1,
			double p2) {
		return new SimpleLensDistortionMetashape(k1, k2, k3, k4, p1, p2);
	}

	@Override
	public LensDistortionMetashape createLensDistortionMetashape(Vector coefficients) throws IllegalArgumentException {
		return new SimpleLensDistortionMetashape(coefficients);
	}

	@Override
	public LensDistortionMetashape createLensDistortionMetashape(float[] coefficients) throws IllegalArgumentException {
		return new SimpleLensDistortionMetashape(coefficients);
	}

	@Override
	public LensDistortionMetashape createLensDistortionMetashape(double[] coefficients)
			throws IllegalArgumentException {
		return new SimpleLensDistortionMetashape(coefficients);
	}

}
