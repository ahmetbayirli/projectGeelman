package helpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ahmet.bayirli on 22.8.2015.
 */
public class FileHelper
{
    public static boolean isImageFile(File file)
    {
        try {
            BufferedImage image= ImageIO.read(file);
            if (image == null) {
                return false;
            }
        } catch(IOException ex) {
            return false;
        }
        return true;
    }
}
