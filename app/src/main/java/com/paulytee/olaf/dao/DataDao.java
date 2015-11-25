package com.paulytee.olaf.dao;

import com.google.gson.annotations.SerializedName;
import com.paulytee.olaf.data.Flashcard;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.List;

/**
 * Created by Pauly T on 11/19/2015.
 */
public class DataDao {
    public static List<Flashcard> getFlashcards(Integer level) throws Exception {
        String user = "paulytee", pass, db = "olaf";

        // create the http connection
        HttpClient httpClient = new StdHttpClient.Builder()
                .url("https://" + user + ".cloudant.com")
                .username(user)
//                .password(pass)
                .build();

        // initialize couch instance and connector
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector dbc = new StdCouchDbConnector(db, dbInstance);

        // create a new "repo" that allows for built-in CRUD functionality
        CrudRepository repo = new CrudRepository(dbc);

        // create a document for use in this example
        Flashcard fc = new Flashcard();
        fc.setLevel(level);

        // add the doc to the database
//        System.out.println("Adding new document to database...");
//        repo.add(fc);

        // neat method that returns a boolean on whether the id exists in the database
//        if(repo.contains(id))
//            System.out.println("Successfully wrote document!");
//        else
//            System.out.println("FAILED to write document to database!");

        // read the document back based on id
        System.out.println("Reading document back from database...");
        return repo.getByLevel(level);
//        System.out.println("Revision of the document is: " + fc.getRevision());

        // update the doc and re-commit
//        System.out.println("Updating document with new value...");
//        fc.setAge(50);
//        repo.update(fc);
//        System.out.println("Wrote updated document to database!");

        // delete the document
//        System.out.println("Deleting document from the database...");
//        repo.remove(fc);
//        System.out.println("Successfully deleted the document!");
    }

    // Our custon "repo" class which extends the Ektorp base class.  The CouchDbRepositorySupport<T> class
    // contains built in CRUD methods.  The Ektorp library as a whole contains more functionality than what
    // is represented in this example.
    public static class CrudRepository extends CouchDbRepositorySupport<Flashcard> {
        public CrudRepository(CouchDbConnector dbc) {
            super(Flashcard.class, dbc, true);
        }

        public List<Flashcard> getByLevel(Integer level) {
            return queryView("cardsByLevel", level);
        }
    }

    public class FlashcardResponse
    {
        @SerializedName("flashcards")
        List<Flashcard> flashcards;
    }

}
