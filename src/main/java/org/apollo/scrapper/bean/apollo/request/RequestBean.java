package org.apollo.scrapper.bean.apollo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBean {
    private String finderTableLayoutId;
    private List<String> contactLabelIds;
    private List<String> prospectedByCurrentTeam;
    private int page;
    private String displayMode;
    private int perPage;
    private List<String> openFactorNames;
    private int numFetchResult;
    private String context;
    private boolean showSuggestions;
    private String uiFinderRandomSeed;
    private String cacheKey;
}
