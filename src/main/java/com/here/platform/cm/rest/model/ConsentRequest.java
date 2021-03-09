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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * ConsentRequest
 */

public class ConsentRequest {
  @JsonProperty("additionalLinks")
  private List<AdditionalLink> additionalLinks = null;

  @JsonProperty("consentRequestId")
  private String consentRequestId;

  @JsonProperty("consumerId")
  private String consumerId;

  @JsonProperty("containerId")
  private String containerId;

  @JsonProperty("privacyPolicy")
  private String privacyPolicy;

  @JsonProperty("providerId")
  private String providerId;

  @JsonProperty("purpose")
  private String purpose;

  @JsonProperty("title")
  private String title;

  public ConsentRequest additionalLinks(List<AdditionalLink> additionalLinks) {
    this.additionalLinks = additionalLinks;
    return this;
  }

  public ConsentRequest addAdditionalLinksItem(AdditionalLink additionalLinksItem) {
    if (this.additionalLinks == null) {
      this.additionalLinks = new ArrayList<>();
    }
    this.additionalLinks.add(additionalLinksItem);
    return this;
  }

   /**
   * Additional links provided by consumer for consent request
   * @return additionalLinks
  **/
  @ApiModelProperty(value = "Additional links provided by consumer for consent request")
  public List<AdditionalLink> getAdditionalLinks() {
    return additionalLinks;
  }

  public void setAdditionalLinks(List<AdditionalLink> additionalLinks) {
    this.additionalLinks = additionalLinks;
  }

  public ConsentRequest consentRequestId(String consentRequestId) {
    this.consentRequestId = consentRequestId;
    return this;
  }

   /**
   * Unique consent request identifier generated during consent request creation
   * @return consentRequestId
  **/
  @ApiModelProperty(value = "Unique consent request identifier generated during consent request creation")
  public String getConsentRequestId() {
    return consentRequestId;
  }

  public void setConsentRequestId(String consentRequestId) {
    this.consentRequestId = consentRequestId;
  }

  public ConsentRequest consumerId(String consumerId) {
    this.consumerId = consumerId;
    return this;
  }

   /**
   * Consumer ID on market place
   * @return consumerId
  **/
  @ApiModelProperty(example = "olp-consumer-example", value = "Consumer ID on market place")
  public String getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(String consumerId) {
    this.consumerId = consumerId;
  }

  public ConsentRequest containerId(String containerId) {
    this.containerId = containerId;
    return this;
  }

   /**
   * Container id as it was listed at Neutral Server
   * @return containerId
  **/
  @ApiModelProperty(value = "Container id as it was listed at Neutral Server")
  public String getContainerId() {
    return containerId;
  }

  public void setContainerId(String containerId) {
    this.containerId = containerId;
  }

  public ConsentRequest privacyPolicy(String privacyPolicy) {
    this.privacyPolicy = privacyPolicy;
    return this;
  }

   /**
   * Link to privacy policy provided by consumer for consent request
   * @return privacyPolicy
  **/
  @ApiModelProperty(value = "Link to privacy policy provided by consumer for consent request")
  public String getPrivacyPolicy() {
    return privacyPolicy;
  }

  public void setPrivacyPolicy(String privacyPolicy) {
    this.privacyPolicy = privacyPolicy;
  }

  public ConsentRequest providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }

   /**
   * Provider ID, OEM identifier
   * @return providerId
  **/
  @ApiModelProperty(example = "olp-here-provider", value = "Provider ID, OEM identifier")
  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public ConsentRequest purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

   /**
   * Description provided by consumer for consent request
   * @return purpose
  **/
  @ApiModelProperty(value = "Description provided by consumer for consent request")
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public ConsentRequest title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Title provided by consumer for consent request
   * @return title
  **/
  @ApiModelProperty(value = "Title provided by consumer for consent request")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsentRequest consentRequest = (ConsentRequest) o;
    return Objects.equals(this.additionalLinks, consentRequest.additionalLinks) &&
        Objects.equals(this.consentRequestId, consentRequest.consentRequestId) &&
        Objects.equals(this.consumerId, consentRequest.consumerId) &&
        Objects.equals(this.containerId, consentRequest.containerId) &&
        Objects.equals(this.privacyPolicy, consentRequest.privacyPolicy) &&
        Objects.equals(this.providerId, consentRequest.providerId) &&
        Objects.equals(this.purpose, consentRequest.purpose) &&
        Objects.equals(this.title, consentRequest.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(additionalLinks, consentRequestId, consumerId, containerId, privacyPolicy, providerId, purpose, title);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsentRequest {\n");
    
    sb.append("    additionalLinks: ").append(toIndentedString(additionalLinks)).append("\n");
    sb.append("    consentRequestId: ").append(toIndentedString(consentRequestId)).append("\n");
    sb.append("    consumerId: ").append(toIndentedString(consumerId)).append("\n");
    sb.append("    containerId: ").append(toIndentedString(containerId)).append("\n");
    sb.append("    privacyPolicy: ").append(toIndentedString(privacyPolicy)).append("\n");
    sb.append("    providerId: ").append(toIndentedString(providerId)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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
