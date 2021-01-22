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
 * VinUpdateError
 */

public class VinUpdateError {
  @JsonProperty("reason")
  private String reason;

  @JsonProperty("vinLabel")
  private String vinLabel;

  public VinUpdateError reason(String reason) {
    this.reason = reason;
    return this;
  }

   /**
   * Get reason
   * @return reason
  **/
  @ApiModelProperty(value = "")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public VinUpdateError vinLabel(String vinLabel) {
    this.vinLabel = vinLabel;
    return this;
  }

   /**
   * Get vinLabel
   * @return vinLabel
  **/
  @ApiModelProperty(value = "")
  public String getVinLabel() {
    return vinLabel;
  }

  public void setVinLabel(String vinLabel) {
    this.vinLabel = vinLabel;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VinUpdateError vinUpdateError = (VinUpdateError) o;
    return Objects.equals(this.reason, vinUpdateError.reason) &&
        Objects.equals(this.vinLabel, vinUpdateError.vinLabel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, vinLabel);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VinUpdateError {\n");
    
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    vinLabel: ").append(toIndentedString(vinLabel)).append("\n");
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

