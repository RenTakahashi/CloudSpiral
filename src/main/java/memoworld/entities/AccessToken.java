package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@XmlRootElement
public class AccessToken {
    public static final String TOKEN_TYPE = "Bearer";
    private static final int TOKEN_LENGTH = 64;

    @XmlElement(name = "access_token")
    private String accessToken;

    @XmlElement(name = "token_type")
    private String tokenType = TOKEN_TYPE;

    @XmlElement(name = "expires_at")
    private Date expiresAt;

    public AccessToken() {
        setAccessToken(generateAccessToken());

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        setExpiresAt(calendar.getTime());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * アクセストークンが有効期限切れかを返す
     *
     * @return アクセストークンの有効期限が切れているならtrue
     */
    public boolean isExpired() {
        return expiresAt.before(new Date());
    }

    /**
     * アクセストークンを生成する
     *
     * @return アクセストークン
     */
    private String generateAccessToken() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] tokenBytes = new byte[TOKEN_LENGTH];
            random.nextBytes(tokenBytes);
            return Base64.getEncoder().encodeToString(tokenBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
