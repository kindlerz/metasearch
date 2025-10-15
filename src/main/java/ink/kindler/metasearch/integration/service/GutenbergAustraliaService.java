package ink.kindler.metasearch.integration.service;

import org.springframework.core.io.Resource;

public interface GutenbergAustraliaService {

  boolean shouldSkipIndexing();

  void refreshIndex(Resource indexCsv);
}
