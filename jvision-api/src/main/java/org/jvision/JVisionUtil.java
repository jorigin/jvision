package org.jvision;

import org.jvision.geometry.Dimension2Dd;

/**
 * This class involves various utilities related to computer vision.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 *
 */
public class JVisionUtil {

  /**
   * Get the dimension of a sensor frame in millimeters (mm) 
   * from a dimension expressed as a fraction of an inch (1/3, 1/4, ...) and an aspect ratio. 
   * If the ratio is not known, standard ratio can be used such as 4/3 for compact cameras / video recorders 
   * or 3/2 for professional cameras. Please be warned that the aspect ratio has to be given in double format (4.0d/3.0d).
   * @param format the format expressed as a fraction of an inch.
   * @param ratio the ratio of the width / height of the sensor (3/2, 4/3, ...).
   * @return the dimension in millimeters of the sensor.
   */
  public static Dimension2Dd getSensorDimension(double format, double ratio){
    
    // Correct diagonal size
    double diagonal = format * 0.63 * 25.4;
    
    double width  = Math.sqrt((diagonal * diagonal ) / (1 + 1/(ratio*ratio)));
    double height = width / ratio;
    
    return new Dimension2Dd(width, height);
  }
}
