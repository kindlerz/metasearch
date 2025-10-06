package ink.kindler.metasearch.integration.service.model;

public record StandardEbooksBook(
   String title, String author,
   String coverImage, String summary,
   String epub, String kobo, String azw, String html
) {}
