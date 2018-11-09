
package memoworld.rest;

import memoworld.entities.Account;
import memoworld.entities.ErrorMessage;
import memoworld.model.AccountModel;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountRest {
	int largecount = 0;
	int smallcount = 0;
	int othercount = 0;

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response postAccount(
			@FormParam("pass") String pass
			,@FormParam("name") String name
			,@FormParam("aid") String aid
			) {
	if(pass == null || pass.trim().equals(""))
		return errorMessage(400, "empty password");
	if(name == null || name.trim().equals(""))
		return errorMessage(400, "empty name");
	if(pass.length() < 5) {
		return errorMessage(400, "too short password");
		}
	int passlen = pass.length();
	for (int m = 0; m < passlen; m++) {
		char firstchar = pass.charAt(m);
		if(Character.isUpperCase(firstchar) == true) {
			largecount++;
		}else if(Character.isLowerCase(firstchar)){
			smallcount++;
		}else {
			othercount++;
		}
	}
//	int password1 = largecount + othercount;
//	int password2 = smallcount + othercount;
	if (largecount == 0||smallcount == 0) {
		return errorMessage(400, "パスワードに大文字と小文字の両方を含めてください。");
	}
		try (AccountModel model = new AccountModel()) {
			Account account = model.register(new Account(pass,name,aid));
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
	@Path("{uid}")
	public Response getAccount(
			@PathParam("uid") String idString) {
	try (AccountModel model = new AccountModel()) {
		int uid = toInteger(idString);
		if (uid <= 0) {
			return errorMessage(400, "Bad request");
			}
		Account account = model.findById(uid);
		if(account == null) {
			return errorMessage(404, "Not found");
			}
		return Response.status(200).entity(account).build();
	
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{uid}")
	public Response deleteAccounts(
			@PathParam("uid") String idString) {
	try (AccountModel model = new AccountModel()){
	int uid = toInteger(idString);
	if(uid <= 0) {
		return errorMessage(400, "Bad request");
	}
	if (!model.deleteAccounts(uid)) {
		return errorMessage(400, "Bad request");
	}
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










