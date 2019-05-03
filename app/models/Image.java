package models;

import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * User: pair
 * Date: 8/21/14
 * Time: 11:22 AM
 */
@Entity
public class Image extends Model {
    private Blob image;
    private String blobId;

    public Image() {
    }

    public Image(Blob image) {
        this.image = image;
        this.blobId = image.getUUID();
    }

    public Blob getImage() {
        return image;
    }

    public String getBlobId() {
        return blobId;
    }

    public static Image findByIdWithException(Long id) throws Exception {
        if(id == null)
        {
            throw new Exception("Image not found!");
        }
        Image model = Image.findById(id);
        if(model == null)
        {
            throw new Exception("Image not found!");
        }
        return model;
    }

    private static String getEntityName() {
        return "Image";
    }
}
