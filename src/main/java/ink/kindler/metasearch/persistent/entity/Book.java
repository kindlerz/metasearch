package ink.kindler.metasearch.persistent.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "book")
@Entity
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  private BookSummary summary;

  @Column(name = "cover_image_url")
  private String coverImageUrl;

  @Column(name = "epub_url")
  private String epubUrl;

  @Column(name = "kobo_url")
  private String koboUrl;

  @Column(name = "mobi_url")
  private String mobiUrl;

  @Column(name = "azw_url")
  private String azwUrl;

  @Column(name = "html_url")
  private String htmlUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Provider provider;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant created;

  @Column(nullable = false)
  @UpdateTimestamp
  private Instant updated;

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public BookSummary getSummary() {
    return summary;
  }

  public void setSummary(BookSummary summary) {
    this.summary = summary;
  }

  public String getCoverImageUrl() {
    return coverImageUrl;
  }

  public void setCoverImageUrl(String coverImageUrl) {
    this.coverImageUrl = coverImageUrl;
  }

  public String getEpubUrl() {
    return epubUrl;
  }

  public void setEpubUrl(String epubUrl) {
    this.epubUrl = epubUrl;
  }

  public String getKoboUrl() {
    return koboUrl;
  }

  public void setKoboUrl(String koboUrl) {
    this.koboUrl = koboUrl;
  }

  public String getMobiUrl() {
    return mobiUrl;
  }

  public void setMobiUrl(String mobiUrl) {
    this.mobiUrl = mobiUrl;
  }

  public String getAzwUrl() {
    return azwUrl;
  }

  public void setAzwUrl(String azwUrl) {
    this.azwUrl = azwUrl;
  }

  public String getHtmlUrl() {
    return htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public Instant getCreated() {
    return created;
  }

  public Instant getUpdated() {
    return updated;
  }
}
