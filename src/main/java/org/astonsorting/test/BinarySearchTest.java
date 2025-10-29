package org.astonsorting.test;

import java.util.Comparator;

import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.model.Book;
import org.astonsorting.util.BinarySearchUtil;

public class BinarySearchTest {
    public static void main(String[] args) {
        CustomArrayList<Book> customArrayList = new CustomArrayList<>();

        customArrayList.add(new Book.Builder().setTitle("A").setAuthor("A").setPublicationYear(2001).build());
        customArrayList.add(new Book.Builder().setTitle("B").setAuthor("B").setPublicationYear(2002).build());
        customArrayList.add(new Book.Builder().setTitle("C").setAuthor("C").setPublicationYear(2003).build());
        customArrayList.add(new Book.Builder().setTitle("D").setAuthor("D").setPublicationYear(2004).build());
        customArrayList.add(new Book.Builder().setTitle("E").setAuthor("E").setPublicationYear(2005).build());

        Book input = new Book.Builder().setTitle("E").build(),
             expected = new Book.Builder().setTitle("E").setAuthor("E").setPublicationYear(2005).build();

        if(expected.equals(customArrayList.get(BinarySearchUtil.find(customArrayList, input, Comparator.comparing(Book::getTitle)))))
            System.out.println("SUCCESS");
        else
            System.out.println("FAILURE");
    }

}
