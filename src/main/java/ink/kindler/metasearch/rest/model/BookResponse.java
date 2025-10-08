package ink.kindler.metasearch.rest.model;

public record BookResponse(Long id, String title, String author,
                           String coverImageUrl, String epubUrl, String koboUrl,
                           String mobiUrl, String azwUrl, String htmlUrl, String summary) {
}
