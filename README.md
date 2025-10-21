# 🔎 Metasearch  

[![Docker Pulls](https://img.shields.io/docker/pulls/kasramp/metasearch.svg)](https://hub.docker.com/repository/docker/kasramp/metasearch/general)
[![Build Status](https://img.shields.io/github/actions/workflow/status/kindlerz/metasearch/ci.yml?branch=main)](https://github.com/kindlerz/metasearch/actions)
[![License](https://img.shields.io/github/license/kindlerz/metasearch)](LICENSE)

> 📚 *Search public-domain libraries with a single API.*

Metasearch is a search engine built for the [Kindler](https://github.com/kindlerz/kindler) ecosystem. It aggregates and unifies results from multiple public-domain ebook sources—like **Project Gutenberg**, **Gutenberg Australia**, and **Standard Ebooks** through one clean REST API.  

Developed with **Java Spring Boot** and **MySQL**, Metasearch powers Kindler’s public-domain search while being fully **Dockerized**, **Swarm-ready**, and auto-deployable via CI/CD.  

🐳 **Docker Hub:** [kasramp/metasearch](https://hub.docker.com/repository/docker/kasramp/metasearch/general)  
💻 **Org:** [github.com/kindlerz](https://github.com/kindlerz)


## Development

Just clone the repository and import it in your IntelliJ. 

## Run (locally)

Via Gradle:

```bash
$ ./gradlew bootRun
```

It automatically runs the `docker-compose.yml` file that spins up a MySQL container.

**Note:** for Standard Ebooks, you need API key.

## Run tests

```bash
$ ./gradlew clean test
```

## API calls

Once the project is up and running you can send GET requests to:

- Search by a provider: `http://localhost:8080/v1/books/search?q=`
- Get book details: `http://localhost:8080/v1/books/10`

Search example:

```bash
$ curl 'http://localhost:8080/v1/books/search?q=moby&provider=STANDARD_EBOOKS'
```

```
[
   {
      "id":58,
      "title":"Moby Dick",
      "author":"Herman Melville",
      "coverImageUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/downloads/cover-thumbnail.jpg"
   }
]
```

Get book details example:

```bash
$ curl 'http://localhost:8080/v1/books/58'
```

```
{
   "id":58,
   "title":"Moby Dick",
   "author":"Herman Melville",
   "coverImageUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/downloads/cover-thumbnail.jpg",
   "epubUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/downloads/herman-melville_moby-dick.epub?source=feed",
   "koboUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/downloads/herman-melville_moby-dick.kepub.epub?source=feed",
   "mobiUrl":null,
   "azwUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/downloads/herman-melville_moby-dick.azw3?source=feed",
   "htmlUrl":"https://standardebooks.org/ebooks/herman-melville/moby-dick/text/single-page",
   "summary":"“Call me Ishmael” says Moby Dick’s protagonist, and with this famous first line launches one of the acclaimed great American novels. Part adventure story, part quest for vengeance, part biological textbook, and part whaling manual, Moby Dick was first published in 1851. The story follows Ishmael as he abandons his humdrum life on shore for an adventure on the waves. Finding the whaler Pequod at harbour in Nantucket, he signs up for a three year term without meeting the Captain of the ship, a mysterious figure called Ahab. It’s only well into the voyage that Ahab’s thirst for vengeance against the eponymous white whale Moby Dick—and the consequences—become clear. The novel is semi-autobiographical: Herman Melville had had his own experience of whaling, having spent a year and a half aboard a whaling ship and further years traveling the world in the early 1840s. Herman used the knowledge gained from his experiences and wide reading on the subject to furnish Moby Dick with an almost encyclopaedic quality. The literary style varies widely, veering from soliloquies and staged scenes to dream sequences to comprehensive lists of ships’ provisions, but everything serves to further detail the world that’s being painted. Presented here is the New York edition, which was published later than the London edition and reverted numerous changes the original publishers had made, as well as including the initially omitted epilogue."
}
```

## DB dump and clean up (in local)

```bash
$ docker exec [CONTAINER_ID] mysqldump -uroot -psecret --no-create-info --skip-add-drop-table --skip-comments --complete-insert metasearch book book_summary > book_tables_dump.sql
$ grep '^INSERT INTO' book_tables_dump.sql > clean_seed.sql
```
