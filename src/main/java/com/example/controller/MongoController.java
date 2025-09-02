package com.example.controller;

import com.mongodb.client.MongoClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MongoController {

  private final MongoClient mongoClient;

  public MongoController(final MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  /**
   * Get database list
   *
   * @return A list of all database names on the MongoDB server.
   */
  @GetMapping("/databases")
  public List<String> listDatabases() {
    return StreamSupport.stream(mongoClient.listDatabaseNames().spliterator(), false)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a set of collection names for the specified database.
   *
   * @param dbName The name of the database from which to list collections.
   * @return A set containing the names of all collections in the specified database.
   */
  @GetMapping("/databases/{dbName}/collections")
  public Set<String> listCollections(@PathVariable String dbName) {

    // Create a new MongoTemplate instance for the specific database.
    MongoTemplate specificMongoTemplate = getMongoTemplateForDb(dbName);
    return specificMongoTemplate.getCollectionNames();
  }

  /**
   * Executes a query on the specified collection within the given database.
   *
   * @param dbName The name of the database where the collection is located.
   * @param collectionName The name of the collection to query.
   * @param query The query in JSON format to be executed against the collection.
   * @return A list of documents that match the query, limited to 200 results.
   */
  @PostMapping("/databases/{dbName}/{collectionName}/query")
  public List<Document> executeQuery(
      @PathVariable String dbName, @PathVariable String collectionName, @RequestBody String query) {
    MongoTemplate specificMongoTemplate = new MongoTemplate(mongoClient, dbName);
    BasicQuery basicQuery = new BasicQuery(query);
    // Limit results to avoid overwhelming the client.
    basicQuery.limit(200);
    return specificMongoTemplate.find(basicQuery, Document.class, collectionName);
  }

  private MongoTemplate getMongoTemplateForDb(String dbName) {
    return new MongoTemplate(mongoClient, dbName);
  }
}
