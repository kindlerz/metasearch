# Public domain libraries metasearch

Search public domain libraries with a single API!


## DB dump and clean up (in local)

```bash
$ docker exec [CONTAINER_ID] mysqldump -uroot -psecret --no-create-info --skip-add-drop-table --skip-comments --complete-insert metasearch book book_summary > book_tables_dump.sql
$ grep '^INSERT INTO' book_tables_dump.sql > clean_seed.sql
```