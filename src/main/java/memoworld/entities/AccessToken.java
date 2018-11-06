package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
public class AccessToken {
    public static final String TOKEN_TYPE = "Bearer";

    @XmlElement(name = "access_token")
    private String accessToken;

    @XmlElement(name = "token_type")
    private String tokenType = TOKEN_TYPE;

    @XmlElement(name = "expires_at")
    private Date expiresAt;

    @XmlElement(name = "refresh_token")
    private String refreshToken;

    public AccessToken() {
    }

    public AccessToken(Account account) {
        setAccessToken(generateAccessToken(account));
        setRefreshToken(generateRefreshToken(account));

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    private String encodeByBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    /**
     * アクセストークンを生成する
     *
     * @param account アカウント
     * @return アクセストークン
     */
    private String generateAccessToken(Account account) {
        return encodeByBase64(new Random().nextLong() + account.getUserId() + account.getUserName());
    }

    /**
     * 再発行トークンを生成する
     *
     * @param account アカウント
     * @return 再発行トークン
     */
    private String generateRefreshToken(Account account) {
        return encodeByBase64(new Random().nextLong() + account.getUserName() + account.getUserId());
    }
}
