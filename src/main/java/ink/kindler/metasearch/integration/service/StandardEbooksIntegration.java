package ink.kindler.metasearch.integration.service;

import ink.kindler.metasearch.integration.service.model.StandardEbooksBook;

import java.util.List;

public interface StandardEbooksIntegration {

  List<StandardEbooksBook> retrieveAllEbooksFromFeed();
}
