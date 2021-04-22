package org.jvision.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.jeometry.factory.JeometryFactory;
import org.jeometry.math.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jvision.JVision;
import org.jvision.camera.distortion.LensDistortionBrown;
import org.jvision.camera.distortion.LensDistortionMetashape;
import org.jvision.camera.distortion.LensDistortionOpenCV;

/**
 * A test suite dedicated to {@link JVisionFactory}.<br>
 * <p>
 * In order to conform to these tests, an implementation has to define a test class that extends from this one 
 * and that implements the method:<br><br>
 * <code>
 * {@literal @}BeforeAll<br>
 * public static void initClass() {<br>
 * &nbsp;&nbsp;// Specifying the JVision implementation to use<br>
 * &nbsp;&nbsp;JVisionFactory.loadImplementation("<b>implementation</b>");<br>
 * <br>
 * &nbsp;&nbsp;// Specifying implementation classes for conformity check (Optional)<br>
 * &nbsp;&nbsp;lensDistortionBrownImplementationClass = &lt;Lens Distortion Brown implementation class&gt;;<br>
 * &nbsp;&nbsp;lensDistortionOpenCVImplementationClass = &lt;Lens Distortion OpenCV implementation class&gt;;<br>
 * &nbsp;&nbsp;lensDistortionMetashapeImplementationClass = &lt;Lens Distortion Agisoft Metashape(c) implementation class&gt;;<br>
 * <br>
 * }<br>
 * </code>
 * Implementation classes can be omitted. If they are specified, test will check the conformity of a produced instances to their specified implementation classes. 
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class JVisionFactoryCameraBuilderTest {

	/**
	 * The Brown distortion coefficients as a float array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>]
	 */
	public static final float[] LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_FLOAT = new float[] {-0.1047f, 0.25263f, -0.0385946f, 0.0f, 0.000508056f, -0.0027698f, 0.0f, 0.0f};

	/**
	 * The Brown distortion coefficients as a double array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>, p<sub>3</sub>, p<sub>4</sub>]
	 */
	public static final double[] LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_DOUBLE = new double[] {-0.1047d, 0.25263d, -0.0385946d, 0.0f, 0.000508056d, -0.0027698d, 0.0d, 0.0d};

	/**
	 * The Brown distortion coefficients as a float array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 */
	public static final float[] LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14 = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f};

	/**
	 * The Brown distortion coefficients as a double array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub> , p<sub>1</sub>, p<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, k<sub>5</sub>, k<sub>6</sub>, s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, s<sub>4</sub>, &tau;<sub>x</sub>, &tau;<sub>y</sub>] (length 14)
	 */
	public static final double[] LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14 = new double[] {1.0d, 2.0d, 3.0d, 4.0d, 5.0d, 6.0d, 7.0d, 8.0d, 9.0d, 10.0d, 11.0d, 12.0d, 13.0d, 14.0d};

	/**
	 * The Brown distortion coefficients as a float array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>] (length 6)
	 */
	public static final float[] LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_FLOAT6 = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};

	/**
	 * The Brown distortion coefficients as a double array.<br>
	 * <br>
	 * [k<sub>1</sub>, k<sub>2</sub>, k<sub>3</sub>, k<sub>4</sub>, p<sub>1</sub>, p<sub>2</sub>] (length 6)
	 */
	public static final double[] LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6 = new double[] {1.0d, 2.0d, 3.0d, 4.0d, 5.0d, 6.0d};

	
	/**
	 * The Brown distortion implementation class
	 */
	static Class<?> lensDistortionBrownImplementationClass = null;

	/**
	 * The OpenCV distortion implementation class
	 */
	static Class<?> lensDistortionOpenCVImplementationClass = null;

	/**
	 * The Agisoft Metashape (c) distortion implementation class
	 */
	static Class<?> lensDistortionMetashapeImplementationClass = null;

	/**
	 * Initialize the tests
	 */
	@BeforeAll
	public static void initClass(){
		fail("Method public static void initClass() has to be overriden.");
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionBrown()}.
	 */
	@Test
	public void createLensDistortionBrownTest()  {

		LensDistortionBrown distortion = JVisionFactory.createLensDistortionBrown();

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionBrownImplementationClass != null) {
			assertEquals(lensDistortionBrownImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionBrownImplementationClass.getSimpleName());
		}	

		assertEquals(0.0d, distortion.getK1(), "Invalid k1 parameter, expected "+0.0d+" but got "+distortion.getK1());
		assertEquals(0.0d, distortion.getK2(), "Invalid k2 parameter, expected "+0.0d+" but got "+distortion.getK2());
		assertEquals(0.0d, distortion.getK3(), "Invalid k3 parameter, expected "+0.0d+" but got "+distortion.getK3());
		assertEquals(0.0d, distortion.getK4(), "Invalid k4 parameter, expected "+0.0d+" but got "+distortion.getK4());
		assertEquals(0.0d, distortion.getP1(), "Invalid p1 parameter, expected "+0.0d+" but got "+distortion.getP1());
		assertEquals(0.0d, distortion.getP2(), "Invalid p2 parameter, expected "+0.0d+" but got "+distortion.getP2());
		assertEquals(0.0d, distortion.getP3(), "Invalid p3 parameter, expected "+0.0d+" but got "+distortion.getP3());
		assertEquals(0.0d, distortion.getP4(), "Invalid p4 parameter, expected "+0.0d+" but got "+distortion.getP4());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionBrown(double, double, double, double, double, double, double, double)}
	 */
	@Test
	public void createLensDistortionBrownParametersTest() {

		float[] a = LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_FLOAT;
		LensDistortionBrown distortion = JVisionFactory.createLensDistortionBrown(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionBrownImplementationClass != null) {
			assertEquals(lensDistortionBrownImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionBrownImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
		assertEquals(a[6], distortion.getP3(), "Invalid p3 parameter, expected "+a[6]+" but got "+distortion.getP3());
		assertEquals(a[7], distortion.getP4(), "Invalid p4 parameter, expected "+a[7]+" but got "+distortion.getP4());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionBrown(Vector)}
	 */
	@Test
	public void createLensDistortionBrownVectorTest() {
		double[] a = LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_DOUBLE;
		Vector v = JeometryFactory.createVector(a);

		LensDistortionBrown distortion = JVisionFactory.createLensDistortionBrown(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionBrownImplementationClass != null) {
			assertEquals(lensDistortionBrownImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionBrownImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
		assertEquals(a[6], distortion.getP3(), "Invalid p3 parameter, expected "+a[6]+" but got "+distortion.getP3());
		assertEquals(a[7], distortion.getP4(), "Invalid p4 parameter, expected "+a[7]+" but got "+distortion.getP4());
	}

	/**
	 * Test method for {@link JVisionFactory#createLensDistortionBrown(float[])}
	 */
	@Test
	public void createLensDistortionBrownArrayFloatTest() {
		float[] a = LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_FLOAT;

		LensDistortionBrown distortion = JVisionFactory.createLensDistortionBrown(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionBrownImplementationClass != null) {
			assertEquals(lensDistortionBrownImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionBrownImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
		assertEquals(a[6], distortion.getP3(), "Invalid p3 parameter, expected "+a[6]+" but got "+distortion.getP3());
		assertEquals(a[7], distortion.getP4(), "Invalid p4 parameter, expected "+a[7]+" but got "+distortion.getP4());
	}

	/**
	 * Test method for {@link JVisionFactory#createLensDistortionBrown(double[])}
	 */
	@Test
	public void createLensDistortionBrownArrayDoubleTest() {
		double[] a = LENS_DISTORTION_BROWN_COEFFICIENTS_ARRAY_DOUBLE;

		LensDistortionBrown distortion = JVisionFactory.createLensDistortionBrown(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionBrownImplementationClass != null) {
			assertEquals(lensDistortionBrownImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionBrownImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
		assertEquals(a[6], distortion.getP3(), "Invalid p3 parameter, expected "+a[6]+" but got "+distortion.getP3());
		assertEquals(a[7], distortion.getP4(), "Invalid p4 parameter, expected "+a[7]+" but got "+distortion.getP4());
	}

	/**
	 * Test method {@link JVisionFactory#createLensDistortionOpenCV()}
	 */
	@Test
	public void createLensDistortionOpenCV() {

		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV();

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(0.0d, distortion.getK1(), "Invalid k1 parameter, expected "+0.0d+" but got "+distortion.getK1());
		assertEquals(0.0d, distortion.getK2(), "Invalid k2 parameter, expected "+0.0d+" but got "+distortion.getK2());
		assertEquals(0.0d, distortion.getK3(), "Invalid k3 parameter, expected "+0.0d+" but got "+distortion.getK3());
		assertEquals(0.0d, distortion.getK4(), "Invalid k4 parameter, expected "+0.0d+" but got "+distortion.getK4());
		assertEquals(0.0d, distortion.getK5(), "Invalid k5 parameter, expected "+0.0d+" but got "+distortion.getK5());
		assertEquals(0.0d, distortion.getK6(), "Invalid k6 parameter, expected "+0.0d+" but got "+distortion.getK6());
		assertEquals(0.0d, distortion.getP1(), "Invalid p1 parameter, expected "+0.0d+" but got "+distortion.getP1());
		assertEquals(0.0d, distortion.getP2(), "Invalid p2 parameter, expected "+0.0d+" but got "+distortion.getP2());
		assertEquals(0.0d, distortion.getS1(), "Invalid s1 parameter, expected "+0.0d+" but got "+distortion.getS1());
		assertEquals(0.0d, distortion.getS2(), "Invalid s2 parameter, expected "+0.0d+" but got "+distortion.getS2());
		assertEquals(0.0d, distortion.getS3(), "Invalid s3 parameter, expected "+0.0d+" but got "+distortion.getS3());
		assertEquals(0.0d, distortion.getS4(), "Invalid s4 parameter, expected "+0.0d+" but got "+distortion.getS4());
		assertEquals(0.0d, distortion.getTx(), "Invalid tx parameter, expected "+0.0d+" but got "+distortion.getTx());
		assertEquals(0.0d, distortion.getTy(), "Invalid ty parameter, expected "+0.0d+" but got "+distortion.getTy());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double, double, double, double, double, double, double, double, double, double, double, double, double, double)}
	 */
	@Test
	public void createLensDistortionOpenCVParameters14Test() {

		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a[0], a[1], a[4], a[5], a[6], a[7], a[2], a[3], a[8], a[9], a[10], a[11], a[12], a[13]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
		assertEquals(a[12], distortion.getTx(), "Invalid s3 parameter, expected "+a[12]+" but got "+distortion.getTx());
		assertEquals(a[13], distortion.getTy(), "Invalid s4 parameter, expected "+a[13]+" but got "+distortion.getTy());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 */
	@Test
	public void createLensDistortionOpenCVParameters12Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a[0], a[1], a[4], a[5], a[6], a[7], a[2], a[3], a[8], a[9], a[10], a[11]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double, double, double, double, double, double, double, double)}
	 */
	@Test
	public void createLensDistortionOpenCVParameters8Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a[0], a[1], a[4], a[5], a[6], a[7], a[2], a[3]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double, double, double, double, double)}
	 */
	@Test
	public void createLensDistortionOpenCVParameters5() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a[0], a[1], a[4], a[2], a[3]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(Vector)} with a dimension 14 vector.
	 */
	@Test
	public void createLensDistortionOpenCVVector14Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		Vector v = JeometryFactory.createVector(a);
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
		assertEquals(a[12], distortion.getTx(), "Invalid s3 parameter, expected "+a[12]+" but got "+distortion.getTx());
		assertEquals(a[13], distortion.getTy(), "Invalid s4 parameter, expected "+a[13]+" but got "+distortion.getTy());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(Vector)} with a dimension 12 vector.
	 */
	@Test
	public void createLensDistortionOpenCVVector12Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		Vector v = JeometryFactory.createVector(12);
		for(int i = 0; i < 12; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(Vector)} with a dimension 8 vector.
	 */
	@Test
	public void createLensDistortionOpenCVVector8Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		Vector v = JeometryFactory.createVector(8);
		for(int i = 0; i < 8; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(Vector)} with a dimension 5 vector.
	 */
	@Test
	public void createLensDistortionOpenCVVector5Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		Vector v = JeometryFactory.createVector(5);
		for(int i = 0; i < 5; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(Vector)} with a dimension 4 vector.
	 */
	@Test
	public void createLensDistortionOpenCVVector4Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;

		Vector v = JeometryFactory.createVector(4);
		for(int i = 0; i < 4; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(float[])} with an array of length 14.
	 */
	@Test
	public void createLensDistortionOpenCVArrayFloat14Test() {
		float[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14;
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
		assertEquals(a[12], distortion.getTx(), "Invalid s3 parameter, expected "+a[12]+" but got "+distortion.getTx());
		assertEquals(a[13], distortion.getTy(), "Invalid s4 parameter, expected "+a[13]+" but got "+distortion.getTy());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(float[])} with an array of length 12.
	 */
	@Test
	public void createLensDistortionOpenCVArrayFloat12Test() {
		float[] a = new float[12];
		
		for(int i = 0; i < 12; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(float[])} with an array of length 8.
	 */
	@Test
	public void createLensDistortionOpenCVArrayFloat8Test() {
		float[] a = new float[8];
		
		for(int i = 0; i < 8; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(float[])} with an array of length 5.
	 */
	@Test
	public void createLensDistortionOpenCVArrayFloat5Test() {
		float[] a = new float[5];
		
		for(int i = 0; i < 5; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(float[])} with an array of length 4.
	 */
	@Test
	public void createLensDistortionOpenCVArrayFloat4Test() {
		float[] a = new float[4];
		
		for(int i = 0; i < 4; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_FLOAT14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 14.
	 */
	@Test
	public void createLensDistortionOpenCVArrayDouble14Test() {
		double[] a = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14;
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
		assertEquals(a[12], distortion.getTx(), "Invalid s3 parameter, expected "+a[12]+" but got "+distortion.getTx());
		assertEquals(a[13], distortion.getTy(), "Invalid s4 parameter, expected "+a[13]+" but got "+distortion.getTy());
	}	

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 12.
	 */
	@Test
	public void createLensDistortionOpenCVArrayDouble12Test() {
		double[] a = new double[12];
		
		for(int i = 0; i < 12; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
		assertEquals(a[8], distortion.getS1(), "Invalid s1 parameter, expected "+a[8]+" but got "+distortion.getS1());
		assertEquals(a[9], distortion.getS2(), "Invalid s2 parameter, expected "+a[9]+" but got "+distortion.getS2());
		assertEquals(a[10], distortion.getS3(), "Invalid s3 parameter, expected "+a[10]+" but got "+distortion.getS3());
		assertEquals(a[11], distortion.getS4(), "Invalid s4 parameter, expected "+a[11]+" but got "+distortion.getS4());
	}	
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 8.
	 */
	@Test
	public void createLensDistortionOpenCVArrayDouble8Test() {
		double[] a = new double[8];
		
		for(int i = 0; i < 8; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[5], distortion.getK4(), "Invalid k4 parameter, expected "+a[5]+" but got "+distortion.getK4());
		assertEquals(a[6], distortion.getK5(), "Invalid k5 parameter, expected "+a[6]+" but got "+distortion.getK5());
		assertEquals(a[7], distortion.getK6(), "Invalid k6 parameter, expected "+a[7]+" but got "+distortion.getK6());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}	
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 5.
	 */
	@Test
	public void createLensDistortionOpenCVArrayDouble5Test() {
		double[] a = new double[5];
		
		for(int i = 0; i < 5; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[4], distortion.getK3(), "Invalid k3 parameter, expected "+a[4]+" but got "+distortion.getK3());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 4.
	 */
	@Test
	public void createLensDistortionOpenCVArrayDouble4Test() {
		double[] a = new double[4];
		
		for(int i = 0; i < 4; i++) {
			a[i] = LENS_DISTORTION_OPENCV_COEFFICIENTS_ARRAY_DOUBLE14[i];
		}
		
		LensDistortionOpenCV distortion = JVisionFactory.createLensDistortionOpenCV(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionOpenCVImplementationClass != null) {
			assertEquals(lensDistortionOpenCVImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getP1(), "Invalid p1 parameter, expected "+a[2]+" but got "+distortion.getP1());
		assertEquals(a[3], distortion.getP2(), "Invalid p2 parameter, expected "+a[3]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionOpenCV(double[])} with an array of length 4.
	 */
	@Test
	public void createLensDistortionMetashapeTest() {
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape();

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionMetashapeImplementationClass.getSimpleName());
		}	

		assertEquals(0.0d, distortion.getK1(), "Invalid k1 parameter, expected "+0.0d+" but got "+distortion.getK1());
		assertEquals(0.0d, distortion.getK2(), "Invalid k2 parameter, expected "+0.0d+" but got "+distortion.getK2());
		assertEquals(0.0d, distortion.getK3(), "Invalid k3 parameter, expected "+0.0d+" but got "+distortion.getK3());
		assertEquals(0.0d, distortion.getK4(), "Invalid k4 parameter, expected "+0.0d+" but got "+distortion.getK4());
		assertEquals(0.0d, distortion.getP1(), "Invalid p1 parameter, expected "+0.0d+" but got "+distortion.getP1());
		assertEquals(0.0d, distortion.getP2(), "Invalid p2 parameter, expected "+0.0d+" but got "+distortion.getP2());
	}	

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(double, double, double, double, double, double)}
	 */
	@Test
	public void createLensDistortionMetashapeParametersTest() {
		double[] a = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6;

		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a[0], a[1], a[2], a[3], a[4], a[5]);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionMetashapeImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(Vector)} with 6 dimension vector.
	 */
	@Test
	public void createLensDistortionMetashapeVector6Test() {
		double[] a = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6;

		Vector v = JeometryFactory.createVector(6);
		for(int i = 0; i < 6; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionMetashapeImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(Vector)} with 5 dimension vector.
	 */
	@Test
	public void createLensDistortionMetashapeVector5Test() {
		double[] a = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6;

		Vector v = JeometryFactory.createVector(5);
		for(int i = 0; i < 5; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionMetashapeImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getP1(), "Invalid p1 parameter, expected "+a[3]+" but got "+distortion.getP1());
		assertEquals(a[4], distortion.getP2(), "Invalid p2 parameter, expected "+a[4]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(Vector)} with 3 dimension vector.
	 */
	@Test
	public void createLensDistortionMetashapeVector3Test() {
		double[] a = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6;

		Vector v = JeometryFactory.createVector(3);
		for(int i = 0; i < 3; i++) {
			v.setValue(i, a[i]);
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(v);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionMetashapeImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(float[])} with 6 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayFloat6Test() {
		float[] a = new float[6];
		
		for(int i = 0; i < 6; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_FLOAT6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(float[])} with 5 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayFloat5Test() {
		float[] a = new float[5];
		
		for(int i = 0; i < 5; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_FLOAT6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getP1(), "Invalid p1 parameter, expected "+a[3]+" but got "+distortion.getP1());
		assertEquals(a[4], distortion.getP2(), "Invalid p2 parameter, expected "+a[4]+" but got "+distortion.getP2());
	}

	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(float[])} with 3 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayFloat3Test() {
		float[] a = new float[3];
		
		for(int i = 0; i < 3; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_FLOAT6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(double[])} with 6 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayDouble6Test() {
		double[] a = new double[6];
		
		for(int i = 0; i < 6; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getK4(), "Invalid k4 parameter, expected "+a[3]+" but got "+distortion.getK4());
		assertEquals(a[4], distortion.getP1(), "Invalid p1 parameter, expected "+a[4]+" but got "+distortion.getP1());
		assertEquals(a[5], distortion.getP2(), "Invalid p2 parameter, expected "+a[5]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(double[])} with 5 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayDouble5Test() {
		double[] a = new double[5];
		
		for(int i = 0; i < 5; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
		assertEquals(a[3], distortion.getP1(), "Invalid p1 parameter, expected "+a[3]+" but got "+distortion.getP1());
		assertEquals(a[4], distortion.getP2(), "Invalid p2 parameter, expected "+a[4]+" but got "+distortion.getP2());
	}
	
	/**
	 * Test for method {@link JVisionFactory#createLensDistortionMetashape(double[])} with 3 length array
	 */
	@Test
	public void createLensDistortionMetashapeArrayDouble3Test() {
		double[] a = new double[3];
		
		for(int i = 0; i < 3; i++) {
			a[i] = LENS_DISTORTION_METASHAPE_COEFFICIENTS_ARRAY_DOUBLE6[i];
		}
		
		LensDistortionMetashape distortion = JVisionFactory.createLensDistortionMetashape(a);

		assertNotNull(distortion, "Cannot instantiate distortion.");

		if (lensDistortionMetashapeImplementationClass != null) {
			assertEquals(lensDistortionMetashapeImplementationClass, distortion.getClass(), "Invalid class, got "+distortion.getClass().getSimpleName()+" but exptected "+lensDistortionOpenCVImplementationClass.getSimpleName());
		}	

		assertEquals(a[0], distortion.getK1(), "Invalid k1 parameter, expected "+a[0]+" but got "+distortion.getK1());
		assertEquals(a[1], distortion.getK2(), "Invalid k2 parameter, expected "+a[1]+" but got "+distortion.getK2());
		assertEquals(a[2], distortion.getK3(), "Invalid k3 parameter, expected "+a[2]+" but got "+distortion.getK3());
	}
}
