package org.astonsorting.test;

import org.astonsorting.model.Book;

public class BuilderTest {
    public static void main(String[] args) {

        Book input = new Book.Builder().setTitle("A").setAuthor("B").setPublicationYear(2001).build();

        if(input.getTitle() == "A" && input.getAuthor() == "B" && input.getPublicationYear() == 2001)
            System.out.println("SUCCESS");
        else
            System.out.println("FAILURE");
    }
}
