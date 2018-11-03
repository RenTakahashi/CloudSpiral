package memoworld.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorMessageTest {

    @Test
    public void Test() {
        String message = "foo";
        ErrorMessage em1 = new ErrorMessage();
        em1.setMessage(message);
        ErrorMessage em2 = new ErrorMessage(message);
        assertEquals(em1.getMessage(), em2.getMessage());
        assertEquals(message, em2.getMessage());
    }
}