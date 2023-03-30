import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class CombinePictures {


    public String combineTwoPictures(String firstImage, String secondImage, String buildFolder, String coinPareName) throws Exception {
        BufferedImage img1 = ImageIO.read( new File( secondImage ) );
        BufferedImage img2 = ImageIO.read( new File( firstImage ) );
        BufferedImage joinedImg = joinBufferedImage(img1, img2);
        String pathname = buildFolder + coinPareName + ".png";
        ImageIO.write(joinedImg, "png", new File(pathname));
        return pathname;
    }

    public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
        int offset = 2;
//        int width = img1.getWidth() + img2.getWidth() + offset;
//        int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;

        int height = img1.getHeight() + img2.getHeight() + offset;
        int width = Math.max(img1.getWidth(), img2.getWidth()) + offset;

        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight() + offset);
        g2.dispose();
        return newImage;
    }

}
