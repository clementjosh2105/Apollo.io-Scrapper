package org.apollo.scrapper.bean.apollo.response.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApolloContacts {
  @JsonProperty("first_name")
  String fName;

  @JsonProperty("last_name")
  String lName;

  String name;
  String title;
  String email;

  @JsonProperty("organization_name")
  String organizationName;

  @JsonProperty("organization_id")
  String organizationId;

  @JsonProperty("linkedin_url")
  String linkedInURL;

  String industry;
  String state;
  String city;
  String country;

  @JsonProperty("created_at")
  Date createdAt;
}
