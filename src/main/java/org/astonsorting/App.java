package org.astonsorting;

import org.astonsorting.model.Book;
import org.astonsorting.collection.CustomArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {


    private final CustomArrayList<Book> bookList;
    private final Scanner scanner;

    public App() {
        this.bookList = new CustomArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App application = new App();
        application.run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": handleLoadData(); break;
                case "2": handleViewData(); break;
                case "3": handleSortData(); break;
                case "4": handleFindElement(); break;
                case "5": handleCountOccurrences(); break;
                case "6": handleWriteToFile(); break;
                case "7": running = false; break;
                default: System.out.println("Пожалуйста, выберите пункт от 1 до 7."); break;
            }
            if (running) {
                pressEnterToContinue();
            }
        }
        System.out.println("Завершение работы приложения.");
        scanner.close();
    }

    private void pressEnterToContinue() {
        System.out.println("\nНажмите Enter, чтобы продолжить...");
        scanner.nextLine();
    }

    private void displayMainMenu() {
        System.out.println("\n=== Приложение для Сортировки Книг ===");
        System.out.println("Статус данных: [" + bookList.size() + " книг]");
        System.out.println("1. Загрузить данные");
        System.out.println("2. Показать текущий список");
        System.out.println("3. Отсортировать текущий список");
        System.out.println("4. Найти книгу"); // Бинарный поиск
        System.out.println("5. Подсчитать вхождения элемента");
        System.out.println("6. Записать результат в файл");
        System.out.println("7. Выход");
        System.out.print("Ваш выбор: ");
    }


    private void handleLoadData() {
        // В будущем здесь будет вызываться логика из класса DataLoader.
    }

    private void handleViewData() {
        if (bookList.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, сначала загрузите данные.");
        } else {
            for (Book book : bookList) {
                System.out.println(book);
            }
        }
    }

    private void handleSortData() {
        // В будущем здесь будет вызываться метод SortingService.sort(bookList).
    }

    private void handleFindElement() {
        // В будущем здесь будет вызываться метод из утилиты бинарного поиска.
    }

    private void handleCountOccurrences() {
        // Здесь будет вызываться многопоточный сервис подсчета элементов.
    }

    private void handleWriteToFile() {
        // Здесь будет вызываться сервис для записи данных в файл.
    }
}