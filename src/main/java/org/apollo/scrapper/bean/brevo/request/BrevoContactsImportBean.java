package org.apollo.scrapper.bean.brevo.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrevoContactsImportBean {
  private String fileBody;
  private List<Integer> listIds;
  @Builder.Default private boolean updateEnabled = true;
}
