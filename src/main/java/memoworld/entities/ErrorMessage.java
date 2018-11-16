package memoworld.entities;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 @XmlRootElement
public class ErrorMessage {
    @XmlElement(name="message")
    private String message;
    public ErrorMessage(){
    }
    public ErrorMessage(String message){
        this.setMessage(message);
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
} 