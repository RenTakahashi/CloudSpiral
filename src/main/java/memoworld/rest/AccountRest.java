
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
			@FormParam("password") String password
			,@FormParam("name") String name
			,@FormParam("user_id") String user_id
			) {
		if(password == null || password.trim().equals(""))
			return errorMessage(400, "empty password");
		if(password.length() < 5) 
			return errorMessage(400, "too short password");
		if(password.equals(password.toLowerCase())|| password.equals(password.toUpperCase()))
			return errorMessage(400, "パスワードに大文字と小文字を含めてください");
		if(name == null || name.trim().equals(""))
			return errorMessage(400, "empty name");
		try (AccountModel model = new AccountModel()) {
//			Account d = model.findById(user_id);
//			System.out.println(d.getUser_id());
//			System.out.println(user_id);
//			if(user_id.equals(d.getUser_id())) {
//				return errorMessage(400, "すでにアカウントが存在します。");				
//			}
			Account account = model.register(new Account(password,name,user_id));
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
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAccounts(
			@PathParam("user_id") String user_id
			){
		
		try (AccountModel model = new AccountModel()){
			if(user_id.length() <= 0) {
				return errorMessage(400, "Bad request");
			}
			if (!model.deleteAccounts(user_id)) {
				return errorMessage(400, "Bad request");
			}
		return Response.status(200).build();
		}
	}
		public Response errorMessage(int statusCode, String message) {
		return Response.status(statusCode).entity(new ErrorMessage(message)).build();
	}
}