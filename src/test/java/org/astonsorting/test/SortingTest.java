package org.astonsorting.test;

import java.util.Comparator;

import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.model.Book;
import org.astonsorting.service.SortingService;
import org.astonsorting.service.strategy.MergeSortStrategy;

public class SortingTest {
    public static void main(String[] args) {
        CustomArrayList<Book> input = new CustomArrayList<>();
        CustomArrayList<Book> expected = new CustomArrayList<>();
        SortingService<Book> sortingService = new SortingService<>();

        input.add(new Book.Builder().setTitle("E").setAuthor("E").setPublicationYear(2005).build());
        input.add(new Book.Builder().setTitle("B").setAuthor("B").setPublicationYear(2002).build());
        input.add(new Book.Builder().setTitle("D").setAuthor("D").setPublicationYear(2004).build());
        input.add(new Book.Builder().setTitle("A").setAuthor("A").setPublicationYear(2001).build());
        input.add(new Book.Builder().setTitle("C").setAuthor("C").setPublicationYear(2003).build());

        expected.add(new Book.Builder().setTitle("A").setAuthor("A").setPublicationYear(2001).build());
        expected.add(new Book.Builder().setTitle("B").setAuthor("B").setPublicationYear(2002).build());
        expected.add(new Book.Builder().setTitle("C").setAuthor("C").setPublicationYear(2003).build());
        expected.add(new Book.Builder().setTitle("D").setAuthor("D").setPublicationYear(2004).build());
        expected.add(new Book.Builder().setTitle("E").setAuthor("E").setPublicationYear(2005).build());

        sortingService.sort(input, new MergeSortStrategy<>(), Comparator.comparing(Book::getTitle));
        // sortingService.sort(input, new MergeSortStrategy<>(), Comparator.comparing(Book::getAuthor));
        // sortingService.sort(input, new MergeSortStrategy<>(), Comparator.comparing(Book::getPublicationYear));


        // System.out.println(input);
        // System.out.println(expected);

        if(input.equals(expected))
            System.out.println("SUCCESS");
        else
            System.out.println("FAILURE");
    }
}
