package com.g3bookstore.util;

import com.g3bookstore.entities.Ad;
import com.g3bookstore.entities.Book;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.model.UploadedFile;

/**
 * This class ensure that the image will go inside the 
 * resource final with the right size.
 * 
 * @author Denis Lebedev
 */
public class UploadImage {
        private static final Logger log = Logger.getLogger(UploadImage.class.getName());

    
    private final int SMALLW = 175;
    private final int SMALLH = 263;
    private final int BIGW = 332;
    private final int BIGH = 499;
    private final int ADW = 1400;
    private final int ADH = 375;
    
    /**
     * Insert a new image inside the target that respect the size of an
     * ad with a unique name base on the date and time.
     * 
     * @param upload
     * @param ad
     * @throws IOException 
     */
    public void addAdBanner(UploadedFile upload, Ad ad) throws IOException {
        String fileN = upload.getFileName();
        String ext = fileN.substring(fileN.indexOf(".") + 1);

        log.log(Level.INFO, "Extension:{0}", ext);
        log.log(Level.INFO, "FILE NAME:{0}", fileN);
        
        //Use date and time to ensure uniqueness of the file name
        fileN = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssSSS")) + "." + ext;
        log.log(Level.INFO, "UNIQUE file name:{0}", fileN);
        
        String pathAd = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/icons");

        BufferedImage image = ImageIO.read(upload.getInputstream());
        BufferedImage adImg = this.imageToBufferedImageConverter(image.getScaledInstance(ADW, ADH, Image.SCALE_DEFAULT));

        File pathS = new File(pathAd + "/" + fileN);

        ad.setImage(fileN);
        try {
            ImageIO.write(adImg, ext, pathS);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Issue adding images:{0}", e.getMessage());
        }
    }
    
    
    /**
     * Insert the new image to the resource with the specific size in order
     * to keep the website consistent. 
     * 
     * @param upload
     * @param book
     * @throws IOException 
     */
    public void addBookCover(UploadedFile upload, Book book) throws IOException {
        String fileN = upload.getFileName();
        String ext = fileN.substring(fileN.indexOf(".") + 1);

        log.log(Level.INFO, "Extension:{0}", ext);
        log.log(Level.INFO, "FILE NAME:{0}", fileN);
        //The file name will be the isbn to ensure that the file name is unique
        fileN = book.getIsbn() + "." + ext;
        log.log(Level.INFO, "UNIQUE file name:{0}", fileN);

        String pathSmallC = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/smallcovers");
        String pathBigC = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/bigcovers");

        BufferedImage image = ImageIO.read(upload.getInputstream());
        BufferedImage smallImg = this.imageToBufferedImageConverter(image.getScaledInstance(SMALLW, SMALLH, Image.SCALE_DEFAULT));
        BufferedImage bigImg = this.imageToBufferedImageConverter(image.getScaledInstance(BIGW, BIGH, Image.SCALE_DEFAULT));

        File pathS = new File(pathSmallC + "/" + fileN);
        File pathB = new File(pathBigC + "/" + fileN);

        book.setImage(fileN);
        try {
            ImageIO.write(smallImg, ext, pathS);
            ImageIO.write(bigImg, ext, pathB);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Issue adding images:{0}", e.getMessage());
        }
    }
    
    /**
     * Convert an Image object to a BufferedImage allowing to write it to the
     * disk.
     *
     * @param img
     * @return
     */
    private BufferedImage imageToBufferedImageConverter(Image img) {
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.SCALE_DEFAULT);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
}
