package org.jvision;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

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
  public static final long BUILD     = 201907091400L;
  
  /**
   * The version number
   */
  public static final String version = "1.0.0";

  /**
   * Initialize the JOrigin common package.
   */
  public static final void init(){

    // Logging initialization.
    logger = Logger.getLogger("org.jorigin.Common");
    
    Formatter formatter = new Formatter(){

      private String lineSeparator = System.getProperty("line.separator");
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
      
      @Override
      public String format(LogRecord record) {
        if (record.getThrown() == null){
          return "("+sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] "+record.getMessage()+lineSeparator;
        } else {
          String str = "("+sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] caused by "+record.getThrown().getMessage()+lineSeparator;
          
          StackTraceElement[] elements = record.getThrown().getStackTrace();
          for(int i = 0; i < elements.length; i++){
            str += "("+sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] at "+elements[i]+lineSeparator;
          }
          return "("+sdf.format(new Date(record.getMillis()))+") "+record.getLevel()+" ["+record.getSourceClassName()+"] ["+record.getSourceMethodName()+"] "+record.getMessage()+lineSeparator+str;
        }
      }
      
    };
    
    StreamHandler handler = new StreamHandler(System.out, formatter){
      @Override
      public void publish(LogRecord record) {
        super.publish(record);  
        flush();
      }
    };

    handler.setFilter(new Filter(){

      @Override
      public boolean isLoggable(LogRecord record) {
        return true;
      }
      
    });

    try {
      handler.setEncoding("UTF-8");
    } catch (Exception ex) {
      try {
        handler.setEncoding(null);
      } catch (Exception ex2) {
        // doing a setEncoding with null should always work.
        // assert false;
      }
    }
    
    String property = System.getProperty("java.util.logging.level");
    Level level = Level.CONFIG;
    if (property != null){
      if (property.equalsIgnoreCase("OFF")){
        level = Level.OFF;
      } else if (property.equalsIgnoreCase("SEVERE")){
        level = Level.SEVERE;
      } else if (property.equalsIgnoreCase("WARNING")){
        level = Level.WARNING;
      } else if (property.equalsIgnoreCase("INFO")){
        level = Level.INFO;
      } else if (property.equalsIgnoreCase("CONFIG")){
        level = Level.CONFIG;
      } else if (property.equalsIgnoreCase("FINE")){
        level = Level.FINE;
      } else if (property.equalsIgnoreCase("FINER")){
        level = Level.FINER;
      } else if (property.equalsIgnoreCase("FINEST")){
        level = Level.FINEST;
      } else if (property.equalsIgnoreCase("ALL")){
        level = Level.ALL;
      }
    }
    
    handler.setLevel(level);
    logger.setLevel(level);
    
    logger.addHandler(handler);
    logger.setUseParentHandlers(false);
  }
  
  /**
   * Set the {@link java.util.logging.Logger logger} to use for reporting.
   * @param logger the {@link java.util.logging.Logger logger} to use for reporting.
   */
  public static void setLogger(Logger logger){
    JVision.logger = logger;
  }
}