
package memoworld.rest;

import memoworld.entities.Account;
import memoworld.entities.ErrorMessage;
import memoworld.model.AccountModel;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountRest {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response postAccount(
			@FormParam("pass") String pass
			,@FormParam("name") String name
			,@FormParam("uid") String uid
			) {
	if(pass == null || pass.trim().equals(""))
		return errorMessage(400, "empty password");
	if(name == null || name.trim().equals(""))
		return errorMessage(400, "empty name");
	if(pass.length() < 5)
		return errorMessage(400, "too short password");
		try (AccountModel model = new AccountModel()) {
			Account account = model.register(new Account(pass,name,uid));
			return Response.status(201).entity(account).build();
		}
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccounts() {
		try (AccountModel model = new AccountModel()) {
			return Response.ok(model.getAccounts()).build();
		}
	}
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{aid}")
	public Response getAccount(
			@PathParam("aid") String idString) {
	try (AccountModel model = new AccountModel()) {
		int aid = toInteger(idString);
		if (aid <= 0)
			return errorMessage(400, "Bad request");
		Account account = model.findById(aid);
		if(account == null)
			return errorMessage(404, "Not found");
			return Response.status(200).entity(account).build();
	
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{aid}")
	public Response deleteAccounts(
			@PathParam("aid") String idString) {
	try (AccountModel model = new AccountModel()){
	int aid = toInteger(idString);
	if(aid <= 0)
		return errorMessage(400, "Bad request");
	if (!model.deleteAccounts(aid))
		return errorMessage(400, "Bad request");
		return Response.status(200).build();
		
		}
	}
	
	public Response errorMessage(int statusCode, String message) {
		return Response.status(statusCode).entity(new ErrorMessage(message)).build();
		}
	
	private int toInteger(String string) {
		try {
			return Integer.parseInt(string);
		}catch(NumberFormatException e){
			return -1;
		}
	}
}










