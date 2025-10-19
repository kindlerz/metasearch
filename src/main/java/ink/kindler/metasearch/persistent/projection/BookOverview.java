package ink.kindler.metasearch.persistent.projection;

public interface BookOverview {
  Long getId();
  String getTitle();
  String getAuthor();
  String getCoverImageUrl();
  String getGoogleCoverImageUrl();
}
