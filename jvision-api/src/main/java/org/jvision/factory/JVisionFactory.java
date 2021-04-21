package org.jvision.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.jeometry.math.Vector;
import org.jvision.JVision;
import org.jvision.camera.distortion.LensDistortionBrown;
import org.jvision.camera.distortion.LensDistortionMetashape;
import org.jvision.camera.distortion.LensDistortionOpenCV;

/**
 * A factory dedicated to the creation of JVision classes implementations.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class JVisionFactory {

	/**
	 * A system property that enables to set the global implementation to use.
	 * The value of this property is used to locate the initialization class for the given implementation.<br><br>
	 * 
	 * For example, if the Java application is launched with <code>-D{@value #JVISION_IMPLEMENTATION_PROPERTY}=val</code>, JVision will:
	 * <ul>
	 * <li>Load the class <code>org.jvision.<b>val</b>.{@value #JVISION_IMPLEMENTATION_INIT_CLASS}</code> (where <code>val</code> value is lower cased)
	 * <li>Call the static method <code>void initJVisionImplementation()</code> that have to provide the registering of the builders
	 * </ul>
	 * <br>
	 * By default, JVision use the <code>simple</code> implementation that will provide simple implementations of all the API interfaces..
	 */
	public static final String JVISION_IMPLEMENTATION_PROPERTY = "org.jvision.implementation";

	/**
	 * The Initialization class of a JVision implementation. 
	 */
	public static final String JVISION_IMPLEMENTATION_INIT_CLASS  = "JVisionImplementation";

	/**
	 * The static initialization method contained within the JVision {@link #JVISION_IMPLEMENTATION_INIT_CLASS implementation initialization class}
	 */
	public static final String JVISION_IMPLEMENTATION_INIT_METHOD = "initJVisionImplementation";

	/**
	 * The default implementation that is used.
	 */
	public static final String JVISION_DEFAULT_IMPLEMENTATION = "simple";

	/**
	 * The default camera builder.
	 */
	private static CameraBuilder defaultCameraBuilder = null;
	
	//Call static initialization.
	static {init();}

	/**
	 * Load and attach the given <code>implementation</code>. 
	 * <br><br>
	 * In order to be loaded, an implementation has to provide an <code>org.jvision.&lt;implementation&gt;.JVisionImplementation</code> class 
	 * that contains a method <code>public static void initJVisionImplementation()</code> that attach the implementation to the factory.<br><br>
	 * For example, the <code>simple</code> implementation provides a class <code>org.jvision.simple.JVisionImplementation</code> that contains the method <code>public static void initJVisionImplementation()</code>.
	 * @param implementation the implementation to use
	 * @return <code>true</code> if the implementation is successfully loaded and attached and <code>false</code> otherwise.
	 */
	public static boolean loadImplementation(String implementation) {
		boolean loaded = false;

		if (implementation != null) {
			// Invoke the initialization methid
			String initClassName = "org.jvision."+implementation.toLowerCase()+"."+JVISION_IMPLEMENTATION_INIT_CLASS;
			Class<?> initClass = null;
			Method initMethod  = null;

			try {
				initClass = Class.forName(initClassName);

				initMethod = initClass.getMethod(JVISION_IMPLEMENTATION_INIT_METHOD);

				initMethod.invoke(null);

				loaded = true;

				JVision.logger.log(Level.CONFIG, "Using "+implementation+" implementation.");			
			} catch (ClassNotFoundException e) {
				JVision.logger.log(Level.SEVERE, "Cannot find \""+implementation+"\" implementation initialization class "+initClassName+": "+e.getMessage(), e);
				initMethod = null;
			} catch (NoSuchMethodException e) {
				JVision.logger.log(Level.SEVERE, "Cannot find \""+implementation+"\" implementation initialization method public static "+JVISION_IMPLEMENTATION_INIT_METHOD+"(): "+e.getMessage(), e);
			} catch (SecurityException e) {
				JVision.logger.log(Level.SEVERE, "Cannot access \""+implementation+"\" implementation initialization class "+initClassName+": "+e.getMessage(), e);
			} catch (IllegalAccessException e) {
				JVision.logger.log(Level.SEVERE, "Cannot access \""+implementation+"\" implementation initialization class "+initClassName+": "+e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				JVision.logger.log(Level.SEVERE, "Cannot execute \""+implementation+"\" implementation initialization method public static "+JVISION_IMPLEMENTATION_INIT_METHOD+"(): "+e.getMessage(), e);
			} catch (InvocationTargetException e) {
				JVision.logger.log(Level.SEVERE, "Cannot invoke \""+implementation+"\" implementation initialization method public static "+JVISION_IMPLEMENTATION_INIT_METHOD+"(): "+e.getMessage(), e);
			} catch (Throwable t) {
				JVision.logger.log(Level.SEVERE, "Cannot use \""+implementation+"\" implementation: "+t.getMessage(), t);
			}
		}



		return loaded;
	}

	/**
	 * Static initialization.
	 */
	private static void init(){

		boolean initialized = false;

		// Check if a specific implementation is set
		String implementation = System.getProperty(JVISION_IMPLEMENTATION_PROPERTY);

		if (implementation != null) {
			JVision.logger.log(Level.INFO, "Using JVision implementation \""+implementation+"\" specified with "+JVISION_IMPLEMENTATION_PROPERTY+" property.");

			initialized = loadImplementation(implementation);

			if (!initialized) {
				JVision.logger.log(Level.SEVERE, "JVision implementation \""+implementation+"\" cannot be loaded.");
				JVision.logger.log(Level.INFO, "Using \""+JVISION_DEFAULT_IMPLEMENTATION+"\" implementation as default.");

				initialized = loadImplementation(JVISION_DEFAULT_IMPLEMENTATION);
			}

		} else {
			JVision.logger.log(Level.INFO, "No JVision implementation specified with "+JVISION_IMPLEMENTATION_PROPERTY+" property.");
			JVision.logger.log(Level.INFO, "Using \""+JVISION_DEFAULT_IMPLEMENTATION+"\" implementation as default.");

			initialized = loadImplementation(JVISION_DEFAULT_IMPLEMENTATION);
		}

		// No implementation is available
		if (!initialized) {
			JVision.logger.log(Level.SEVERE, "No JVision implementation is available, adding jvision-simple.jar to classpath may solve the problem.");
		}
	}

	/**
	 * Get the default {@link CameraBuilder camera builder}.
	 * @return the default {@link CameraBuilder camera builder}
	 * @see #setDefaultCameraBuilder(CameraBuilder)
	 */
	public static CameraBuilder getDefaultCameraBuilder() {
		return defaultCameraBuilder;
	}
	
	/**
	 * Set the default {@link CameraBuilder camera builder}.
	 * @param builder the camera builder to use as default
	 * @see #getDefaultCameraBuilder()
	 */
	public static void setDefaultCameraBuilder(CameraBuilder builder) {
		defaultCameraBuilder = builder;
	}
	
	// Camera builder related methods start
	/**
	 * Create a new distortion representation based on {@link LensDistortionBrown Brown formalization} with no distortion.
	 * @return the lens distortion
	 */
	public static LensDistortionBrown createLensDistortionBrown() {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionBrown();
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionBrown createLensDistortionBrown(double k1, double k2, double k3, double k4, double p1, double p2, double p3, double p4) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionBrown(k1, k2, k3, k4, p1, p2, p3, p4);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionBrown createLensDistortionBrown(Vector coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionBrown(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionBrown createLensDistortionBrown(float[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionBrown(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionBrown createLensDistortionBrown(double[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionBrown(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionOpenCV OpenCV formalization} with no distortion.
	 * @return the lens distortion
	 */
	public static LensDistortionOpenCV createLensDistortionOpenCV() {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV();
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2, 
			                                               double s1, double s2, double s3, double s4,
			                                               double tx, double ty) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4, tx, ty);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2, 
			                                               double s1, double s2, double s3, double s4) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2, s1, s2, s3, s4);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double k4, double k5, double k6, 
			                                               double p1, double p2) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(k1, k2, k3, k4, k5, k6, p1, p2);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(double k1, double k2, double k3, 
			                                               double p1, double p2) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(k1, k2, k3, p1, p2);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(Vector coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(float[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionOpenCV createLensDistortionOpenCV(double[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionOpenCV(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
	/**
	 * Create a new distortion representation based on {@link LensDistortionMetashape Agisoft Metashape(c) formalization} with no distortion.
	 * @return the lens distortion
	 */
	public static LensDistortionMetashape createLensDistortionMetashape() {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionMetashape();
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionMetashape createLensDistortionMetashape(double k1, double k2, double k3, double k4, double p1, double p2) {
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionMetashape(k1, k2, k3, k4, p1, p2);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionMetashape createLensDistortionMetashape(Vector coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionMetashape(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionMetashape createLensDistortionMetashape(float[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionMetashape(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	
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
	public static LensDistortionMetashape createLensDistortionMetashape(double[] coefficients) throws IllegalArgumentException{
		if (defaultCameraBuilder != null) {
			return defaultCameraBuilder.createLensDistortionMetashape(coefficients);
		} else {
			throw new IllegalStateException("No camera builder available.");
		}
	}
	// Camera builder related methods end
	
}
