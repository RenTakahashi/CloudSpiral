package memoworld.rest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import memoworld.entities.ErrorMessage;
import memoworld.entities.Photo;
import memoworld.model.PhotoModel;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("/photos")
public class PhotoRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPhoto(@Context HttpHeaders headers, Photo photo) {
        try {
            // TODO: トークンの認証
            String auth = headers.getHeaderString("WWW-Authenticate");

            // 写真をDBに登録して登録内容を返す
            PhotoModel model = new PhotoModel();
            byte[] base64 = Base64.getDecoder().decode(photo.getRawImage());

            // exifから情報取得
            boolean isDefaultLocation = photo.getLocation().getLatitude() == -360;
            boolean isDefaultDate = photo.getDate() == null;
            if (isDefaultLocation || isDefaultDate) {
                Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(base64));
                boolean reverseLatitude = false;
                boolean reverseLongitude = false;
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("GPS Latitude") && isDefaultLocation) {
                            photo.getLocation().setLatitude(Double.parseDouble(tag.getDescription()
                                    .replace("' ", "")
                                    .replace(".", "")
                                    .replace("\"", "")
                                    .replace("° ", ".")));
                        } else if (tag.getTagName().equals("GPS Latitude Ref") && isDefaultLocation) {
                            reverseLatitude = tag.getDescription().equals("N");
                        } else if (tag.getTagName().equals("GPS Longitude") && isDefaultLocation) {
                            photo.getLocation().setLongitude(Double.parseDouble(tag.getDescription()
                                    .replace("' ", "")
                                    .replace(".", "")
                                    .replace("\"", "")
                                    .replace("° ", ".")));
                        } else if (tag.getTagName().equals("GPS Longitude Ref") && isDefaultLocation) {
                            reverseLatitude = tag.getDescription().equals("W");
                        } else if (tag.getTagName().equals("Date/Time") && isDefaultDate) {
                            photo.setDate(new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").parse(tag.getDescription()));
                        }
                    }
                }
                if (reverseLatitude)
                    photo.getLocation().setLatitude(-photo.getLocation().getLatitude());
                if (reverseLongitude)
                    photo.getLocation().setLongitude(-photo.getLocation().getLongitude());
            }

            return Response.status(201)
                    .header("Content-Type", "application/json")
                    .entity(model.register(photo))
                    .build();

        } catch (IOException | IllegalArgumentException e) {
            // 画像バイナリが正しくなかった場合
            HashMap<String, Object> h = new HashMap<>();
            h.put("Content-Type", "application/json");
            return errorMessage(400, "bad image binary", h);
        } catch (ImageProcessingException | ParseException e) {
            // EXIFメタデータが読み込めなかった場合
            HashMap<String, Object> h = new HashMap<>();
            h.put("Content-Type", "application/json");
            return errorMessage(400, "invalid image metadata", h);
        }
    }

    private Response errorMessage(int statusCode,
                                  String message,
                                  HashMap<String, Object> headers) {
        Response.ResponseBuilder response = Response.status(statusCode);
        for (Map.Entry<String, Object> header : headers.entrySet()) {
            response = response.header(header.getKey(), header.getValue());
        }
        return response.entity(new ErrorMessage(message))
                .build();

    }

    private static BufferedImage toBufferedImage(byte[] imageBinary) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBinary));
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage bufImage = new BufferedImage(img.getWidth(), height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int c = img.getRGB(x, y);
                int r = c >> 16 & 0xff;
                int g = c >> 8 & 0xff;
                int b = c & 0xff;
                int rgb = 0xff000000 | r << 16 | g << 8 | b;
                bufImage.setRGB(x, y, rgb);
            }
        }
        return bufImage;
    }
}