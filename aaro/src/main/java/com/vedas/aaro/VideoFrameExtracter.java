package com.vedas.aaro;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.RgbToBgr;
import org.jcodec.scale.Transform;
import org.jcodec.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class VideoFrameExtracter {
  public File createThumbnailFromVideo(File file, int frameNumber) throws IOException, JCodecException {
      Picture frame = FrameGrab.getFrameFromFile(file, frameNumber);
     // File tempFile = File.createTempFile("thumb_" + frameNumber + file.getName().replaceAll("(.+)\\..+", "$1"), ".png");
      File tempFile = new File("C:\\images\\" + file.getName().replaceAll("(.+)\\..+", "$1"+".png"));
      
      //Create the file
      if (file.createNewFile()){
      System.out.println("File is created!");
      }else{
      System.out.println("File already exists.");
      }
      ImageIO.write(toBufferedImage8Bit(frame), "png", tempFile);
      return tempFile;
  }
  // this method is from Jcodec AWTUtils.java.
  private BufferedImage toBufferedImage8Bit(Picture src) {
      if (src.getColor() != ColorSpace.RGB) {
          Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
          if (transform == null) {
              throw new IllegalArgumentException("Unsupported input colorspace: " + src.getColor());
          }
          Picture out = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB);
          transform.transform(src, out);
          new RgbToBgr().transform(out, out);
          src = out;
      }
      BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
              BufferedImage.TYPE_3BYTE_BGR);
      if (src.getCrop() == null)
          toBufferedImage8Bit2(src, dst);
      else
          toBufferedImageCropped8Bit(src, dst);
      return dst;
  }
  // this method is from Jcodec AWTUtils.java.
  private void toBufferedImage8Bit2(Picture src, BufferedImage dst) {
      byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
      byte[] srcData = src.getPlaneData(0);
      for (int i = 0; i < data.length; i++) {
          data[i] = (byte) (srcData[i] + 128);
      }
  }
  // this method is from Jcodec AWTUtils.java.
  private static void toBufferedImageCropped8Bit(Picture src, BufferedImage dst) {
      byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
      byte[] srcData = src.getPlaneData(0);
      int dstStride = dst.getWidth() * 3;
      int srcStride = src.getWidth() * 3;
      for (int line = 0, srcOff = 0, dstOff = 0; line < dst.getHeight(); line++) {
          for (int id = dstOff, is = srcOff; id < dstOff + dstStride; id += 3, is += 3) {
              data[id] = (byte) (srcData[is] + 128);
              data[id + 1] = (byte) (srcData[is + 1] + 128);
              data[id + 2] = (byte) (srcData[is + 2] + 128);
          }
          srcOff += srcStride;
          dstOff += dstStride;
      }
  }
  
  /*
  public static void main(String[] args) {
      VideoFrameExtracter videoFrameExtracter = new VideoFrameExtracter();
      File file = Paths.get("C:/Users/wave/Desktop/test.mp4").toFile();
      try {
          File imageFrame = videoFrameExtracter.createThumbnailFromVideo(file, 2);
          System.out.println("input file name : " + file.getAbsolutePath());
          System.out.println("output video frame file name  : " + imageFrame.getAbsolutePath());
      } catch (IOException | JCodecException e) {
          System.out.println("error occurred while extracting image : " + e.getMessage());
      }
  }
  */
  
}
