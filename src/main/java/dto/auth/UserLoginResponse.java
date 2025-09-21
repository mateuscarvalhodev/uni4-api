package dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginResponse {
  @JsonProperty("access_token")
  public String accessToken;

  @JsonProperty("refresh_token")
  public String refreshToken;

  @JsonProperty("expires_in")
  public int expiresIn;

  @JsonProperty("refresh_expires_in")
  public int refreshExpiresIn;

  @JsonProperty("token_type")
  public String tokenType;

  @JsonProperty("scope")
  public String scope;

}
