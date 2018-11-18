package memoworld.entities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AccountTest {

	@Test
	public void Test() {
		Account a = new Account();
		String password = "1234aA";	
		String name = "Test Taro";	
		String user_id = "111111";	
			
		a.setName(name);
		a.setPassword(password);
		a.setUser_id(user_id);
		assertEquals(password, a.getPassword());
		assertEquals(name, a.getName());
		assertEquals(user_id, a.getUser_id());
	}
}