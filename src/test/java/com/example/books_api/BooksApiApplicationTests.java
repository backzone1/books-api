package com.example.books_api;

import com.example.books_api.model.Book;
import com.example.books_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BooksApiApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BookRepository bookRepository;

	@LocalServerPort
	private int port;

	private String baseUrl;
	@BeforeEach
	void setUp() {
//		bookRepository.deleteAll();
		baseUrl = "http://localhost:" + port + "/books";
	}

	@Test
	void shouldCreateBookSuccessfully() {
		String payload = """
        {
          "title": "Valid Book",
          "author": "Tester",
          "publishedDate": "2568-01-31"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<Book> response = restTemplate.postForEntity(baseUrl, request, Book.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isNotNull();
		assertThat(response.getBody().getTitle()).isEqualTo("Valid Book");
		assertThat(response.getBody().getAuthor()).isEqualTo("Tester");
		assertThat(response.getBody().getPublishedDate()).isEqualTo("2568-01-31");
	}

	@Test
	void shouldFailWhenDateFormatInvalid() {
		String payload = """
        {
          "title": "Book",
          "author": "Tester",
          "publishedDate": "invalid-date"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Invalid input format");
	}

	@Test
	void shouldFailWhenTitleMissing() {
		String payload = """
        {
          "author": "Tester",
          "publishedDateBuddhist": "2568-01-31"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Title must not be empty");
	}

	@Test
	void shouldFailWhenTitleIsBlank() {
		String payload = """
        {
          "title": "",
          "author": "Tester",
          "publishedDateBuddhist": "2568-01-31"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Title must not be empty");
	}

	@Test
	void shouldFailWhenAuthorMissing() {
		String payload = """
        {
          "title": "Book",
          "publishedDateBuddhist": "2568-01-31"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Author must not be empty");
	}

	@Test
	void shouldFailWhenAuthorIsBlank() {
		String payload = """
        {
          "title": "Test Book",
          "author": "",
          "publishedDateBuddhist": "2568-01-31"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Author must not be empty");
	}

	@Test
	void shouldFailWhenPublishedDateMissing() {
		String payload = """
        {
          "title": "Book",
          "author": "Tester"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Published date must not be null");
	}

	@Test
	void shouldFailWhenPublishedDateIsBlank() {
		String payload = """
        {
          "title": "Book",
          "author": "Tester",
          "publishedDate": ""
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("must not be null");
	}

	@Test
	void shouldFailWhenPublishedYearIsTooOld() {
		String payload = """
        {
          "title": "Ancient Book",
          "author": "Tester",
          "publishedDate": "0999-01-01"
        }
        """;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Published year must be valid");
	}

	@Test
	void shouldFailWhenPublishedDateIsInFuture() {
		int futureYear = LocalDate.now().getYear() + 544;

		String payload = String.format("""
        {
          "title": "Future Book",
          "author": "Tester",
          "publishedDate": "%d-01-01"
        }
        """, futureYear);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Published year must be valid");
	}

	@Test
	void shouldGetBooksByAuthor() {
		Book book = new Book();
		book.setTitle("Sample Get Book");
		book.setAuthor("Tester");
		book.setPublishedDate(LocalDate.of(2024, 1, 1));
		bookRepository.save(book);

		String url = baseUrl + "?author=Tester";

		ResponseEntity<Book[]> response = restTemplate.getForEntity(url, Book[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isGreaterThan(0);
		assertThat(response.getBody()[0].getAuthor()).isEqualTo("Tester");
	}

	@Test
	void shouldReturnEmptyWhenAuthorNotFound() {
		String url = baseUrl + "?author=Unknown";

		ResponseEntity<Book[]> response = restTemplate.getForEntity(url, Book[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().length).isEqualTo(0);
	}

}
