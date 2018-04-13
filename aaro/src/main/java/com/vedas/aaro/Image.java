package com.vedas.aaro;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {

 public static void main(String[] args) {
  try {
   File f = new File("/Users/apple/Desktop/ram.jpg");
   BufferedImage image = ImageIO.read(f);
   int height = image.getHeight();
   int width = image.getWidth();
   System.out.println("Height : " + height);
   System.out.println("Width : " + width);
  } catch (IOException ioe) {
   ioe.printStackTrace();
  }
 }
}
