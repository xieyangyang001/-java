package api.util;

import api.canstants.FileErrorCodeCanstant;
import api.canstants.PathFileCanstant;
import net.bytebuddy.implementation.bytecode.Throw;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 提高图片识别率
 * Created by XieYangYang on 2018/11/22.
 */
public class PictVerification {
    Logger logger = Logger.getLogger(PictVerification.class);

    public BufferedImage readPicToBufferedImage(String path) throws IOException {
        File picFile = new File(path);
        if (!picFile.isFile()) {
            logger.error("文件路径：" + path + "," + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND.getCode() + "," + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND.getDesc());
            throw new IOException(path + ":" + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND.getDesc());
        }
        BufferedImage bufferedImage = ImageIO.read(picFile);
        return bufferedImage;
    }

    public BufferedImage twoValued(BufferedImage bufferedImage) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        BufferedImage newBuffImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i, j);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                if (r * 0.299 + g * 0.578 + b * 0.114 >= 192) {
                    //浅色 ...
                    newBuffImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
                } else {
                    //深色 ...
                    newBuffImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
                }
            }
        }
        return newBuffImage;
    }


    //像素以内的干扰线全部干掉可以使原本的验证码变细
    /*public BufferedImage removeSmallLine(BufferedImage bufferedImage) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        BufferedImage newBuffImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i, j);
                Color color = new Color(rgb);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                if (r == 0 && b == 0 && g == 0) {
                    if (i == 0 && j == 0) {


                    } else if (i == 0 && j == height - 1) {

                    } else if (i == width - 1 && j == 0) {

                    } else if (i == width - 1 && j == height - 1) {

                    } else {

                    }
                }

            }
        }
        return newBuffImage;
    }*/


    /**
     * 获取某像素的颜色
     *
     * @param bufferedImage
     * @param x             横坐标
     * @param y             纵坐标
     * @return
     */
    public int getPixColor(BufferedImage bufferedImage, int x, int y) {
        int rgb = bufferedImage.getRGB(x, y);
        Color color = new Color(rgb);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if (r == 0 && g == 0 && b == 0) {
            return 1;
        }
        return 0;
    }


    /**
     * @param newBuffImage
     * @param newImgFile
     */
    public void createImg(BufferedImage newBuffImage, String newImgFile) {
        File file = new File(newImgFile);
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ImageIO.write(newBuffImage, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String picToCharacter() {
        BufferedImage buffImage = null;
        try {
            buffImage = readPicToBufferedImage(PathFileCanstant.picVerificationOldPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage buffNewImage = twoValued(buffImage);
        createImg(buffNewImage, PathFileCanstant.picVerificationNewPath);
        File imageFile = new File(PathFileCanstant.picVerificationNewPath);
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources(PathFileCanstant.tessData);
        instance.setLanguage("eng");
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        String character = null;
        try {
            character = instance.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return character;
    }
}
