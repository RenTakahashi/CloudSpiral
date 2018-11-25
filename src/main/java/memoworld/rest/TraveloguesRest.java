package memoworld.rest;

import memoworld.entities.*;
import memoworld.model.AccessTokenModel;
import memoworld.model.LikeModel;
import memoworld.model.PhotoModel;
import memoworld.model.TravelogueModel;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/travelogues")
public class TraveloguesRest {
	private static final String AUTHENTICATION_SCHEME = "Bearer";
	
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTravelogue(@Context HttpHeaders headers,Travelogue travelogue){
        
        try (TravelogueModel model = new TravelogueModel()) {
        	 // 投稿者情報の取得
            String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
            AccessTokenModel accessTokenModel = new AccessTokenModel();
            Account account = accessTokenModel.getAccount(token);
            travelogue.setAuthor(account.getName());

        	
        	travelogue.getTitle();
        	travelogue.getDate();
        	travelogue.getPhotos_id();
        	Travelogue result =	model.register(travelogue);
        	PhotoModel photomodel = new PhotoModel();
        	for(int id : result.getPhotos_id()) {
				Photo photo = photomodel.findById(id);
				if (photo == null){
					return errorMessage(404, "Not found photo");
				}
				result.photos.add(photo);
			}
        	result.setPhotos_id(null);
        	return Response.status(201)
                    .entity(result)
                    .build();
        }
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTraveloges() {
		try (TravelogueModel model = new TravelogueModel()) {
			Travelogue travelogue = new Travelogue();
			LikeModel likemodel = new LikeModel();
			Travelogues travelogues = new Travelogues();
			PhotoModel photomodel = new PhotoModel();
			
			for(int i = 1; i <=  model.count() ; i++) {
					travelogue = model.findById(i);
				for(int j = 1; j <= likemodel.count(); j++) {
					Like like = likemodel.findByLid(j);
					if (like.getTraveloguesid() == travelogue.getId())
						travelogue.likes.add(like);
				}
				for(int id : travelogue.getPhotos_id()) {
					Photo photo = photomodel.findById(id);
					if (photo == null){
						return errorMessage(404, "Not found photo");
					}
					travelogue.photos.add(photo);
				}
				travelogues.travelogues.add(travelogue);	
			}
				photomodel.close();
				likemodel.close();
				return Response.status(200)
						.entity(travelogues)
						.build();
		}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getTravelogue(
    		@PathParam("id")String idString) {
		try (TravelogueModel model = new TravelogueModel()) {
			int id = toInteger(idString);
			if(id < 0)
				return errorMessage(400,"Bad request");
			
			Travelogue travelogue = model.findById(id);
			if(travelogue == null)
				return errorMessage(404, "Not found");
			LikeModel likemodel = new LikeModel();
			PhotoModel photomodel = new PhotoModel();

			for(int j = 1; j <= likemodel.count(); j++) {
				Like like = likemodel.findByLid(j);
				if (like.getTraveloguesid() == travelogue.getId())
					travelogue.likes.add(like);
			}

			for(int photo_id : travelogue.getPhotos_id()) {
				Photo photo = photomodel.findById(photo_id);
				if (photo == null){
					return errorMessage(404, "Not found photo");
				}
				travelogue.photos.add(photo);
			}
			likemodel.close();
			photomodel.close();
		
			return Response.status(200)
					.entity(travelogue)
					.build();
		}catch(Exception e){
			return errorMessage(500, "server internal error");
		}	
    }
    
    

    public Response errorMessage(int statusCode,
                                 String message) {
        return Response.status(statusCode)
                .entity(new ErrorMessage(message))
                .build();

    }

    private int toInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
