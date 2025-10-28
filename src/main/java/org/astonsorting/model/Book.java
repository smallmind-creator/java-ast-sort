package org.astonsorting.model;
import java.util.Objects;

public class Book implements Comparable<Book> {
    private final String title;
    private final String author;
    private final int publicationYear;

    private Book(Builder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.publicationYear = builder.publicationYear;
    }

    public static class Builder {
        private String title;
        private String author;
        private int publicationYear;

        public Builder setTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;

            return this;
        }

        public Builder setPublicationYear(int year) {
            this.publicationYear = year;

            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getPublicationYear() {
        return this.publicationYear;
    }

    @Override
    public int compareTo(Book book) {
        return this.title.compareTo(book.title);
    }

    @Override
    public String toString() {
        return ("Название: " + " [" + this.title + "], " +
                "Автор: " + " [" + this.author + "], " +
                "Год: " + " [" + this.publicationYear + "]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publicationYear);
    }
}
