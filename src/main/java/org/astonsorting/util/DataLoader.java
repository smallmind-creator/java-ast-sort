package org.astonsorting.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.model.Book;

public class DataLoader {

    public CustomArrayList<Book> loadBooksFromCsvFile(String path){
        CustomArrayList<Book> books = new CustomArrayList<>();

        try(Stream<String> lines = Files.lines(Paths.get(path))){
            lines
                .filter(line -> !line.trim().isEmpty())
                .map(line -> line.split(","))
                .forEach(fields -> {
                    try{
                        if(fields.length != 3)
                            throw new IOException("Неверное количество полей в строке: " + String.join(",", fields));

                        books.add(new Book.Builder()
                                        .setTitle(fields[0].trim())
                                        .setAuthor(fields[1].trim())
                                        .setPublicationYear(Integer.parseInt(fields[2].trim()))
                                        .build());
                    }
                    catch(NumberFormatException nfe){
                        System.out.println("Год не является числом в строке: "+ String.join(",", fields));
                    }
                    catch(IOException ioe){
                        System.out.println(ioe.getMessage());
                    }
                });
        }
        catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }

        return books;
    }

    public CustomArrayList<Book> generateRandomBooks(int count){
        CustomArrayList<Book> books = new CustomArrayList<>();
        
        AtomicInteger counter = new AtomicInteger(1);

        Stream.generate(() -> counter.getAndIncrement())
                .limit(count)
                .forEach(number -> {
                    Random random = new Random();

                    books.add(new Book.Builder()
                    .setTitle("Книга #"+number)
                    .setAuthor("Автор #"+number)
                    .setPublicationYear(random.nextInt(1700, 2025))
                    .build());
                });
        
        return books;
    }

    
    public Book loadBookFromConsole(Scanner scanner){

        System.out.print("Введите название: ");
        String title = scanner.nextLine();
        System.out.print("Введите имя автора: ");
        String author = scanner.nextLine();
        int publicationYear;
        //задалбливаем юзера пока не введет год в виде числа
        while(true){
            try{
                System.out.print("Введите год издания: ");
                publicationYear = Integer.parseInt(scanner.nextLine());
                break;
            }
            catch(NumberFormatException nfe){
                System.out.println("Год не является числом");
            }
        }

        return new Book.Builder()
        .setTitle(title)
        .setAuthor(author)
        .setPublicationYear(publicationYear)
        .build();
    }
}
