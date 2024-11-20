package org.apollo.scrapper.bean.apollo.response.contacts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApolloContactsMapperBean {
    private String name ;
    private String fName ;
    private String lName ;
    private String organizationName ;
    private String title ;
    private String industry ;
    private String email ;
    private String linkedIn ;
    private String country ;
    private String state ;
    private String city ;
}
