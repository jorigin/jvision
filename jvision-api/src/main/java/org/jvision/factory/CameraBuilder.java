package org.jvision.factory;

import org.jeometry.math.Vector;
import org.jvision.JVision;
import org.jvision.camera.DigitalCamera;
import org.jvision.camera.distortion.LensDistortionBrown;
import org.jvision.camera.distortion.LensDistortionMetashape;
import org.jvision.camera.distortion.LensDistortionOpenCV;
import org.jvision.camera.distortion.LensDistortion;

/**
 * An interface that describe a builder for camera related classes. Classes that implements this interface are able to produce implementation classes such as {@link DigitalCamera}, {@link LensDistortion}, ...
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public interface CameraBuilder {

	/**
	 * Create a new distortion representation based on {@link LensDistortionBrown Brown formalization} with no distortion.
	 * @return the lens distortion
	 */
	public LensDistortionBrown createLensDistortionBrown(); 
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionBrown Brown formalization} with radial coefficients (k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>) 
	 * and tangential coefficients (p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>).
	 * @param k1 the first radial distortion coefficient
	 * @param k2 the second radial distortion coefficient
	 * @param k3 the third radial distortion coefficient
	 * @param k4 the fourth radial distortion coefficient
	 * @param p1 the first tangential distortion coefficient
	 * @param p2 the second tangential distortion coefficient
	 * @param p3 the third tangential distortion coefficient
	 * @param p4 the fourth tangential distortion coefficient
	 * @return a new Brown based lens distortion with radial coefficients (k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>) and tangential (decentering) coefficients (p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>)
	 */
	public LensDistortionBrown createLensDistortionBrown(double k1, double k2, double k3, double k4, double p1, double p2, double p3, double p4);
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionBrown Brown formalization} with given coefficients. Possible values are:
	 * <ul>
	 * <li>(k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>) (8 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, p<sub>1</sub>, p<sub>2</sub>) (5 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>) (3 dimensions)
	 * <li> null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new Brown lens distortion representation
	 * @throws IllegalArgumentException if the input vector does not match a distortion configuration
	 */
	public LensDistortionBrown createLensDistortionBrown(Vector coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionBrown Brown formalization} with given coefficients. Possible values are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>] (8 values)
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, p<sub>1</sub>, p<sub>2</sub>] (5 values)
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>] (3 values)
	 * <li> null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new Brown lens distortion representation
	 * @throws IllegalArgumentException if the input array does not match a distortion configuration
	 */
	public LensDistortionBrown createLensDistortionBrown(float[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new Brown based lens distortion with given coefficients. Possible values are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>] (8 values)
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, p<sub>1</sub>, p<sub>2</sub>] (5 values)
	 * <li>[k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>] (3 values)
	 * <li> null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new Brown lens distortion representation
	 * @throws IllegalArgumentException if the input array does not match a distortion configuration
	 */
	public LensDistortionBrown createLensDistortionBrown(double[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with no distortion.
	 * @return the lens distortion
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(); 
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 * @param tx the x tilt distortion coefficient
	 * @param ty the y tilt distortion coefficient
	 * @return a new OpenCV lens distortion representation
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2, 
			                                               double s1, double s2, double s3, double s4,
			                                               double tx, double ty);
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * With this creation method, the tilt distortion coefficients are set to <code>0</code> (no distortion).
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @param s1 the first thin prism distortion coefficient
	 * @param s2 the second thin prism distortion coefficient
	 * @param s3 the third thin prism distortion coefficient
	 * @param s4 the fourth thin prism distortion coefficient
	 * @return a new OpenCV lens distortion representation
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2, 
			                                               double s1, double s2, double s3, double s4);
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * With this creation method, the thin prism and tilt distortions coefficients are set to <code>0</code> (no distortion).
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the first radial distortion rational coefficient
	 * @param k5 the second radial distortion rational coefficient
	 * @param k6 the third radial distortion rational coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @return a new OpenCV lens distortion representation
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2);
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * With this creation method, the radial rational, thin prism and tilt distortions coefficients are set to <code>0</code> (no distortion).
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @return a new OpenCV lens distortion representation
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double p1, double p2);
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted vectors are:
	 * <ul>
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>) (14 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>) (12 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>) (8 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>) (5 dimensions)
	 * <li>(k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>) (4 dimensions)
	 * <li>null or empty vector for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new OpenCV lens distortion representation
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(Vector coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted arrays are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * <li>null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new OpenCV lens distortion representation
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(float[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with given coefficients.<br><br>
	 * An OpenCV distortion is made of 4 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> (simple) and k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub> (rational).
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * <li>Thin prism distortion defined by coefficients s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub> and s<sub>4</sub>
	 * <li>Tilt distortion defined by coefficients &tau;x and &tau;y.
	 * </ul>
	 * Accepted arrays are:
	 * <ul>
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>] (length 12)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>] (length 8)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>] (length 5)
	 * <li>[k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>] (length 4)
	 * <li>null or empty array for no distortion
	 * </ul>
	 * @param coefficients the distortion coefficients
	 * @return a new OpenCV lens distortion representation
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionOpenCV createLensDistortionOpenCV(double[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with no distortion.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * @return the lens distortion
	 */
	public LensDistortionMetashape createLensDistortionMetashape(); 
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * @param k1 the first radial distortion simple coefficient
	 * @param k2 the second radial distortion simple coefficient
	 * @param k3 the third radial distortion simple coefficient
	 * @param k4 the fourth radial distortion simple coefficient
	 * @param p1 the first tangential (decentering) distortion coefficient
	 * @param p2 the second tangential (decentering) distortion coefficient
	 * @return a lens distortion
	 */
	public LensDistortionMetashape createLensDistortionMetashape(double k1, double k2, double k3, double k4, double p1, double p2); 
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted vector is (k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>) (6 dimensions).
	 * @param coefficients the distortion coefficients
	 * @return a lens distortion
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionMetashape createLensDistortionMetashape(Vector coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted array is [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>] (length 6).
	 * @param coefficients the distortion coefficients
	 * @return a lens distortion
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionMetashape createLensDistortionMetashape(float[] coefficients) throws IllegalArgumentException;
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with given coefficients.<br><br>
	 * An Agisoft Metashape (c) distortion is made of 2 components:
	 * <ul>
	 * <li>Radial distortion defined by coefficients k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub> and k<sub>4</sub>.
	 * <li>Tangential (decentering) distortion defined by coefficients p<sub>1</sub> and p<sub>2</sub>.
	 * </ul>
	 * Accepted array is [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>,  p<sub>1</sub>, p<sub>2</sub>] (length 6).
	 * @param coefficients the distortion coefficients
	 * @return a lens distortion
	 * @throws IllegalArgumentException  if the input array does not match a distortion configuration
	 */
	public LensDistortionMetashape createLensDistortionMetashape(double[] coefficients) throws IllegalArgumentException;
	
}
