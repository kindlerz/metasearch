package ink.kindler.metasearch.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "book_summary")
public class BookSummary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String summary;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false, unique = true)
  private Book book;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant created;

  @Column(nullable = false)
  @UpdateTimestamp
  private Instant updated;

  public Long getId() {
    return id;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Instant getCreated() {
    return created;
  }

  public Instant getUpdated() {
    return updated;
  }
}
