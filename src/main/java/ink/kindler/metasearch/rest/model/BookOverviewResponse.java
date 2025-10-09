package ink.kindler.metasearch.rest.model;

public record BookOverviewResponse(Long id, String title, String author,
                                   String coverImageUrl) {
}
