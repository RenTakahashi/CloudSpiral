package memoworld.rest;

import memoworld.entities.Travelogue;
import memoworld.entities.Travelogues;
import memoworld.entities.Account;
import memoworld.entities.ErrorMessage;
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
        	for(int k = 1; k <= photomodel.count(); k++) {
        		result.photos.add(photomodel.findById(k));
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
					travelogue.likes.add(likemodel.findByLid(j));
				}
				for(int k = 1; k <= photomodel.count() ; k++) {
					travelogue.photos.add(photomodel.findById(k));
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
			
			for(int i = 1; i <= likemodel.count(); i++) {
				travelogue.likes.add(likemodel.findByLid(i));
			}
			
			for(int j = 1; j <= photomodel.count(); j++) {
				travelogue.photos.add(photomodel.findById(j));
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