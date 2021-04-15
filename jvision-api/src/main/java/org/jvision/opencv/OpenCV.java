package org.jvision.opencv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import org.jvision.JVision;


/**
 * This class enable to check the availability of OpenCV wraps.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 */
public class OpenCV {
  
  private static boolean available = false;
  
  private static String libraryNameWin     = "opencv_java310";
  private static String libraryNameLinux   = "libopencv_java310";
  
  {init();}
  
  private static void init(){
    
    String osArch    = System.getProperty("os.arch");    // Operating system architecture
    String osName    = System.getProperty("os.name");    // Operating system name


    if (osName != null){
      if (osName.toUpperCase().contains("WINDOWS")){
        
        if (osArch != null){
          
          if (osArch.toUpperCase().contains("64")){

            String resourcePath = "natives/win64/"+libraryNameWin+".dll";
            available = deployLibrary(resourcePath, libraryNameWin, ".dll");

          } else if (osArch.toUpperCase().contains("86")){
            
            String resourcePath = "natives/win32/"+libraryNameWin+".dll";
            available = deployLibrary(resourcePath, libraryNameWin, ".dll");

          } else {
            JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (unknonwn Window system architecture "+osArch);
            available = false;
          }
        } else {
          JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (no system architecture os.arch available");
          available = false;
        } 
  
      } else  if (osName.toUpperCase().indexOf("NIS") >= 0 || osName.toUpperCase().indexOf("NUX") >= 0 || osName.toUpperCase().indexOf("AIX") > 0 ) {

        if (osArch != null){
          if (osArch.toUpperCase().contains("AMD64")){
            String resourcePath = "natives/linux64/"+libraryNameLinux+".so";
            available = deployLibrary(resourcePath, libraryNameLinux, ".so");
          } else if (osArch.toUpperCase().contains("X86")){
            String resourcePath = "natives/linux32/"+libraryNameLinux+".so";
            available = deployLibrary(resourcePath, libraryNameLinux, ".so");
          } else {
            JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (unknonwn Linux/Unix system architecture "+osArch);
            available = false;
          }
        } else {
          JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (no system architecture os.arch available");
          available = false;
        }
      } else {
        JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (no system architecture os.arch available");
        available = false;
      } 
    } else {
      JVision.logger.log(Level.WARNING, "Cannot load OpenCV libraries (no system name os.name available");
      available = false;
    }
  }

  /**
   * Check if the openCV native library is available and accessible from Java.
   * @return <code>true</code> if the openCV native library is available or <code>false</code> otherwise.
   */
  public static boolean isAvailable(){

    if (!available){
      init();
    }
    
    return available;
  }
  
  private static boolean deployLibrary(String libraryPath, String libraryName, String libraryExtension){
    
    boolean deployed = false;

    if (libraryPath != null){
      
      if ((libraryName != null) && (libraryExtension != null)){
        
        // Create a temporary file for the library deployment.
        File tmpLib = null;
        try {
          tmpLib = File.createTempFile(libraryName, libraryExtension);
        } catch (Exception e) {
          JVision.logger.log(Level.SEVERE, "Cannot create temporary deployed file for "+libraryName+libraryExtension+": "+e.getMessage(), e);
          tmpLib = null;
        } 
        
        // Copy the library to the deployed file
        if (tmpLib != null){
          
          tmpLib.deleteOnExit();
          
          InputStream in = null;
          try {
            in = OpenCV.class.getClassLoader().getResourceAsStream(libraryPath);
            
            if (in != null){
              Files.copy(in, tmpLib.toPath(), StandardCopyOption.REPLACE_EXISTING);
              deployed = true;
            } else {
              JVision.logger.log(Level.SEVERE, "Cannot open "+libraryPath+" library.");
              deployed = false;
            }

          } catch (IOException e) {
            JVision.logger.log(Level.SEVERE, "Cannot copy "+libraryPath+" to destination "+tmpLib.getPath()+": "+e.getMessage(), e);
            deployed = false;
          } finally{
            try {
              if (in != null){
                in.close();
              }
            } catch (IOException e) {
              JVision.logger.log(Level.WARNING, "Cannot close input stream to "+libraryPath);
            }
          }
        } else {
          JVision.logger.log(Level.WARNING, "No temporary file available for library deploying "+libraryPath+".");
          deployed = false;
        }
        
        // Load the deployed library
        if (deployed){
          try {
            System.load(tmpLib.toString());
            JVision.logger.log(Level.CONFIG, "Library "+libraryName+" deployed and loaded from "+tmpLib.toString());
          } catch (Exception e) {
            JVision.logger.log(Level.SEVERE, "Cannot load library "+tmpLib.toString()+": "+e.getMessage(), e);
            deployed = false;
          }
        }
        
      } else {
        JVision.logger.log(Level.WARNING, "Cannot deploy "+libraryPath+" to null deploy path.");
        deployed = false;
      }
      
    } else {
      JVision.logger.log(Level.WARNING, "Cannot deploy null library");
      deployed = false;
    }
    
    return deployed;
  }
}
