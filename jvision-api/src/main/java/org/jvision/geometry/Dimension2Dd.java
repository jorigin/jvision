package org.jvision.geometry;

import java.awt.geom.Dimension2D;

import org.jvision.JVision;

/**
 * An implementation of {@link java.awt.geom.Dimension2D Dimension2D} that enables to be constructed with double values.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value JVision#version} b{@value JVision#BUILD}
 * @since 1.0.0
 *
 */
public class Dimension2Dd extends Dimension2D{

  double width;
  
  double height;
  
  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public void setSize(double width, double height) {
    this.width  = width;
    this.height = height;
  }

  /**
   * Returns a string representation of the values of this
   * <code>Dimension2Dd</code> object's <code>height</code> and
   * <code>width</code> fields. This method is intended to be used only
   * for debugging purposes, and the content and format of the returned
   * string may vary between implementations. The returned string may be
   * empty but may not be <code>null</code>.
   *
   * @return  a string representation of this <code>Dimension</code>
   *          object
   */
  public String toString() {
      return getClass().getName() + "[width=" + width + ",height=" + height + "]";
  }
  
  /**
   * Construct a new dimension with given width and height.
   * @param width the width value.
   * @param height the height value.
   */
  public Dimension2Dd(double width, double height){
    this.width  = width;
    this.height = height;
  }
}
