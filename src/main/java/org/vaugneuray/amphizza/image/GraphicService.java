package org.vaugneuray.amphizza.image;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Objects;
import javax.imageio.ImageIO;

@Service
public class GraphicService {

    public BufferedImage textToBufferedImage(String text) throws IOException {
        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = 450;
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        return img;

    }

    public BufferedImage mergeImages(BufferedImage... images) {
        BufferedImage image = merge2Image(images[0], images[1]);
        for (int i = 2; i < images.length; i++) {
            image = merge2Image(image, images[i]);
        }
        return image;
    }
    public BufferedImage merge2Image(BufferedImage top, BufferedImage bottom) {

// create the new image, canvas size is the max. of both image sizes
        int w = top.getWidth();

// paint both images, preserving the alpha channels
        final var scaleFactor = (float) top.getWidth() / (float) bottom.getWidth();
        final var scaledBottomImg = scaleImg(bottom, scaleFactor);

        int h = (int) (top.getHeight() + bottom.getHeight() * scaleFactor) + 25;
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        g.drawImage(top, 0, 0, null);

        g.drawImage(scaledBottomImg, 0, top.getHeight() + 25, null);

        g.dispose();

// Save as new image
        return combined;
    }

    public BufferedImage getLogoImage() throws IOException {
        return ImageIO.read(new ClassPathResource("img/logo-amphiesta.png").getInputStream());
    }

    private BufferedImage scaleImg(BufferedImage before, float scaleFactor) {
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);
        return after;
    }
}