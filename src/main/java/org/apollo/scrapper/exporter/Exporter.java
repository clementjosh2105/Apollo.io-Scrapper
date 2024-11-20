package org.apollo.scrapper.exporter;

import org.apollo.scrapper.bean.apollo.response.list.ApolloSavedList;

public interface Exporter {

  void export(ApolloSavedList apolloSavedList);
}
