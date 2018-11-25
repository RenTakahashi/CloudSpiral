package memoworld.rest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import memoworld.entities.Account;
import memoworld.entities.ErrorMessage;
import memoworld.entities.Location;
import memoworld.entities.Photo;
import memoworld.model.AccessTokenModel;
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
import java.util.Base64;

@Path("/photos")
public class PhotoRest {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response postPhoto(@Context HttpHeaders headers, Photo photo) {
        try {
            // 写真をデコードして画像として正しいかをチェック
            PhotoModel model = new PhotoModel();
            byte[] base64 = Base64.getDecoder().decode(photo.getRaw());
            toBufferedImage(base64);

            // 投稿者情報の取得
            String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
            AccessTokenModel accessTokenModel = new AccessTokenModel();
            Account account = accessTokenModel.getAccount(token);
            photo.setAuthor(account.getName());

            // exifから情報取得
            boolean isDefaultLocation = photo.getLocation() == null;
            boolean isDefaultDate = photo.getDate() == null;
            if (isDefaultLocation || isDefaultDate) {
                Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(base64));
                boolean reverseLatitude = false;
                boolean reverseLongitude = false;
                Location location = new Location();
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("GPS Latitude") && isDefaultLocation) {
                            // 緯度を60進から10進に直して取得
                            String[] s = tag.getDescription().split("[°'\"]");
                            location.setLatitude(Double.parseDouble(s[0]) +
                                    Double.parseDouble(s[1]) / 60 +
                                    Double.parseDouble(s[2]) / 3600);
                        } else if (tag.getTagName().equals("GPS Latitude Ref")) {
                            // 緯度を反転させるかどうかの取得
                            reverseLatitude = tag.getDescription().equals("S");
                        } else if (tag.getTagName().equals("GPS Longitude") && isDefaultLocation) {
                            // 経度を60進から10進に直して取得
                            String[] s = tag.getDescription().split("[°'\"]");
                            location.setLongitude(Double.parseDouble(s[0]) +
                                    Double.parseDouble(s[1]) / 60 +
                                    Double.parseDouble(s[2]) / 3600);
                        } else if (tag.getTagName().equals("GPS Longitude Ref")) {
                            // 経度を反転させるかどうかの取得
                            reverseLongitude = tag.getDescription().equals("W");
                        } else if (tag.getTagName().equals("Date/Time") && isDefaultDate) {
                            // 日付の取得・更新
                            photo.setDate(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(tag.getDescription()));
                        }
                    }
                }
                // 位置情報を写真から取得したデータを使うなら更新
                if (isDefaultLocation) {
                    photo.setLocation(location);
                    if (reverseLatitude)
                        photo.getLocation().setLatitude(-photo.getLocation().getLatitude());
                    if (reverseLongitude)
                        photo.getLocation().setLongitude(-photo.getLocation().getLongitude());
                }
            }

            // レスポンスには画像データを入れない
            Photo result = model.register(photo);
            result.setRaw(null);
            return Response.status(201)
                    .entity(result)
                    .build();
        } catch (IOException | IllegalArgumentException e) {
            // 画像バイナリが正しくなかった場合
            return errorMessage(400, "bad image binary");
        } catch (ImageProcessingException | ParseException e) {
            // EXIFメタデータが読み込めなかった場合
            return errorMessage(400, "invalid image metadata");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("{id}")
    public Response getPhoto(@PathParam("id") String idString) {
        try (PhotoModel model = new PhotoModel()) {
            int id = toInteger(idString);
            if (id <= 0)
                return errorMessage(400, "Bad request");
            Photo photo = model.findById(id);
            if (photo == null)
                return errorMessage(404, "Not found");

            // 画像データは返さないのでnullにする
            photo.setRaw(null);
            return Response.status(200)
                    .entity(photo)
                    .build();
        } catch (Exception e) {
            return errorMessage(500, "server internal error");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("{id}/raw")
    public Response getRawImage(@PathParam("id") String idString) {
        try (PhotoModel model = new PhotoModel()) {
            int id = toInteger(idString);
            if (id <= 0)
                return errorMessage(400, "Bad request");
            Photo photo = model.findById(id);
            if (photo == null)
                return errorMessage(404, "Not found");

            // 画像以外のデータは返さないのでnullにする
            photo.setDate(null);
            photo.setDescription(null);
            photo.setLocation(null);
            photo.setAuthor(null);
            photo.setRaw_uri(null);
            return Response.status(200)
                    .entity(photo)
                    .build();
        } catch (Exception e) {
            return errorMessage(500, "server internal error");
        }
    }

    private Response errorMessage(int statusCode, String message) {
        return Response.status(statusCode)
                .entity(new ErrorMessage(message))
                .build();
    }

    /**
     * バイト列からバッファイメージに変換する
     * @param imageBinary 画像のバイナリ
     * @return バッファイメージ
     * @throws IOException 変換不可能なバイナリの場合の例外
     */
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

    private int toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
