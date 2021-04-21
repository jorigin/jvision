package org.jvision;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;


/**
 * This class contains static information about the JOrigin Vision package. The logging system of the whole package is handled 
 * by this class and is it possible to change the internal logger with method {@link #setLogger(Logger)}.
 * @author Julien Seinturier - JOrigin - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jvision">https://github.com/jorigin/jvision</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class JVision{
  
  /**
   * The {@link java.util.logging.Logger logger} used for reporting.
   */
  public static Logger logger = null;
  
  static {
    init();
  }

  /**
   * The build version.
   */
  public static final long BUILD     = 202104211400L;
  
  /**
   * The version number
   */
  public static final String version = "1.0.0";

  /**
   * Initialize the JOrigin common package.
   */
  public static final void init(){

    // Logging initialization.
    logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
  }
  
  /**
   * Set the {@link java.util.logging.Logger logger} to use for reporting.
   * @param logger the {@link java.util.logging.Logger logger} to use for reporting.
   */
  public static void setLogger(Logger logger){
    JVision.logger = logger;
  }
}