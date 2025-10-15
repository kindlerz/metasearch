package ink.kindler.metasearch.integration.service;

import ink.kindler.metasearch.persistent.entity.Book;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;

import java.util.stream.Stream;

public interface CsvService {

  Stream<CSVRecord> streamCsvQuietly(Resource indexCsv);

  Book convertCsvRecord(CSVRecord csvRecord);
}
