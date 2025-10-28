package org.astonsorting.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.model.Book;

public class DataWriter {
     public void writeListToFile(String path, CustomArrayList<Book> books){
        Stream<Book> stream = StreamSupport.stream(books.spliterator(), false);
        stream.map(book -> book.getTitle()+","+book.getAuthor()+","+book.getPublicationYear())
                .forEach(bookLine -> {
                    try{
                        Files.write(Paths.get(path), (bookLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                    }
                    catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                });
     }
}
