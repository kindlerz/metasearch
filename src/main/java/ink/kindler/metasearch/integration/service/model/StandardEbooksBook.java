package ink.kindler.metasearch.integration.service.model;

import java.time.Instant;

public record StandardEbooksBook(
   String title, String author,
   String coverImage, String summary,
   String epub, String kobo, String azw, String html,
   Instant updatedAt
) {}
