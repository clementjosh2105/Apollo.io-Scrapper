package org.apollo.scrapper.importer;

import org.apollo.scrapper.bean.apollo.response.list.ApolloSavedList;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Importer {
  void importApolloList(ApolloSavedList apolloSavedList) throws URISyntaxException, IOException;
}
