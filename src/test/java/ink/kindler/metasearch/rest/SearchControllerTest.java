package ink.kindler.metasearch.rest;

import ink.kindler.metasearch.persistent.entity.Book;
import ink.kindler.metasearch.persistent.entity.BookSummary;
import ink.kindler.metasearch.persistent.entity.Provider;
import ink.kindler.metasearch.persistent.projection.BookOverview;
import ink.kindler.metasearch.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {
  private static final String QUERY_HUNDRED_FIVE_CHARACTERS = "cbrw18ZgJJ@XM+9kq7FneXe345dfgrdw4tD%+2o=UeJ#8ZnR2a$n3y4FNv9&Y9q@8HR$!BNWfS&*zb0MgMyh2G+ODo+RGwmn5W3gWh4@G";
  private static final String BOOK_BY_ID_RESPONSE = """
      {
         "id":null,
         "title":"The Adventures of Tom Sawyer",
         "author":"Mark Twain",
         "coverImageUrl":"https://coverimageurl.com",
         "epubUrl":"https://epuburl.com",
         "koboUrl":"https://kobourl.com",
         "mobiUrl":"https://mobiurl.com",
         "azwUrl":"https://azwurl.com",
         "htmlUrl":"https://htmlurl.com",
         "summary":"Test summary"
      }
      """;

  private static final String BOOK_SEARCH_RESPONSE = """
      [
         {
            "id":1,
            "title":"The Adventures of Tom Sawyer",
            "author":"Mark Twain",
            "coverImageUrl":"https://coverimageurl.com"
         },
         {
            "id":2,
            "title":"The Mark of Zorro",
            "author":"Johnston McCulley",
            "coverImageUrl":"https://coverimageurl.com"
         }
      ]
      """;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SearchService searchService;

  @Test
  void shouldSearchBookByQuery() throws Exception {
    when(searchService.search(Provider.STANDARD_EBOOKS, "Mark")).thenReturn(List.of(
        stubBookOverview(1L, "The Adventures of Tom Sawyer", "Mark Twain"),
        stubBookOverview(2L, "The Mark of Zorro", "Johnston McCulley")
    ));

    mockMvc.perform(get("/v1/books/search")
            .queryParam("q", "Mark")
            .queryParam("provider", "STANDARD_EBOOK"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(BOOK_SEARCH_RESPONSE));
  }

  @Test
  void shouldReturnBadRequestIfQuerySizeExceedsOneHundredCharacters() throws Exception {
    mockMvc.perform(get("/v1/books/search")
            .queryParam("q", QUERY_HUNDRED_FIVE_CHARACTERS)
            .queryParam("provider", "STANDARD_EBOOK"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldSearchBookById() throws Exception {
    when(searchService.findBookById(1L)).thenReturn(Optional.of(stubBook()));

    mockMvc.perform(get("/v1/books/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().json(BOOK_BY_ID_RESPONSE));
  }

  @Test
  void shouldReturnNotFoundWhenBookByIdDoesNotExist() throws Exception {
    when(searchService.findBookById(1L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/v1/books/2"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }


  private Book stubBook() {
    var book = new Book();
    book.setTitle("The Adventures of Tom Sawyer");
    book.setAuthor("Mark Twain");
    book.setSummary(stubBookSummary(book));
    book.setCoverImageUrl("https://coverimageurl.com");
    book.setEpubUrl("https://epuburl.com");
    book.setKoboUrl("https://kobourl.com");
    book.setMobiUrl("https://mobiurl.com");
    book.setAzwUrl("https://azwurl.com");
    book.setHtmlUrl("https://htmlurl.com");
    book.setProvider(Provider.STANDARD_EBOOKS);
    return book;
  }

  private BookSummary stubBookSummary(Book book) {
    var bookSummary = new BookSummary();
    bookSummary.setSummary("Test summary");
    bookSummary.setBook(book);
    return bookSummary;
  }

  private BookOverview stubBookOverview(Long id, String title, String author) {
    return new BookOverview() {
      @Override
      public Long getId() {
        return id;
      }

      @Override
      public String getTitle() {
        return title;
      }

      @Override
      public String getAuthor() {
        return author;
      }

      @Override
      public String getCoverImageUrl() {
        return "https://coverimageurl.com";
      }
    };
  }
}