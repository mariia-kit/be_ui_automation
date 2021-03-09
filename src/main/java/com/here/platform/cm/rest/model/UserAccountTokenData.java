/*
 * HERE Consent Management API v1
 * HERE Consent Management REST API. More details can be found here: https://confluence.in.here.com/display/OLP/Neutral+Server+Consent+Management
 *
 * OpenAPI spec version: 3.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.here.platform.cm.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;


/**
 * UserAccountTokenData
 */

public class UserAccountTokenData {
  @JsonProperty("cm_token")
  private String cmToken;

  public UserAccountTokenData cmToken(String cmToken) {
    this.cmToken = cmToken;
    return this;
  }

   /**
   * CM token
   * @return cmToken
  **/
  @ApiModelProperty(example = "413a0bf6-891e-3648-bf46-72eb399781c0", value = "CM token")
  public String getCmToken() {
    return cmToken;
  }

  public void setCmToken(String cmToken) {
    this.cmToken = cmToken;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccountTokenData userAccountTokenData = (UserAccountTokenData) o;
    return Objects.equals(this.cmToken, userAccountTokenData.cmToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cmToken);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccountTokenData {\n");
    
    sb.append("    cmToken: ").append(toIndentedString(cmToken)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
