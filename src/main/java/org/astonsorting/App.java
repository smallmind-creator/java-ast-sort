package org.astonsorting;

import org.astonsorting.model.Book;
import org.astonsorting.util.BinarySearchUtil;
import org.astonsorting.util.DataLoader;
import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.service.SortingService;
import org.astonsorting.service.strategy.MergeSortStrategy;
import org.astonsorting.service.CollectionCounterService;
import org.astonsorting.service.strategy.EvenOddSortStrategy;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {
    private final CustomArrayList<Book> bookList;
    private final Scanner scanner;
    private final DataLoader dataLoader;
    private final SortingService<Book> sortingService;
    private final CollectionCounterService counterService;

    public App() {
        this.bookList = new CustomArrayList<>();
        this.scanner = new Scanner(System.in);
        this.dataLoader = new DataLoader();
        this.sortingService = new SortingService<>();
        this.counterService = new CollectionCounterService();
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
        sortingService.shutdown();
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
        System.out.println("\n=== Загрузка Книг ===");
        System.out.println("1. Загрузить из файла");
        System.out.println("2. Сгенерировать случайные");
        System.out.println("3. Ввести книгу вручную");
        System.out.print("Ваш выбор: ");
        String choice = scanner.nextLine();

         switch (choice) {
                case "1": 
                    System.out.print("Введите путь: ");
                    CustomArrayList<Book> loadedBooks = dataLoader.loadBooksFromCsvFile(scanner.nextLine());
                    bookList.addAll(loadedBooks);
                    System.out.println("Загружено " + loadedBooks.size() + " книг.");
                    break;
                case "2": 
                    System.out.print("Введите количество книг: ");
                    for (Book book : dataLoader.generateRandomBooks(Integer.parseInt(scanner.nextLine()))) {
                        bookList.add(book);
                    }
                    break;
                case "3": 
                    bookList.add(dataLoader.loadBookFromConsole(scanner));
                    break;
                default: System.out.println("Пожалуйста, выберите пункт от 1 до 3."); break;
            }

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
        System.out.println("\nОтсортировать текущий список");
        System.out.println("Статус данных: [" + bookList.size() + " книг]");

        if (bookList.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, сначала загрузите данные");
            return;
        }

        // Выбор стратегии
        System.out.println("Выберите тип сортировки:");
        System.out.println("1. Обычная: MergeSort");
        System.out.println("2. Кастомная: чётные годы сортируются, нечётные остаются на местах");
        System.out.print("Ваш выбор: ");
        String strategyChoice = scanner.nextLine().trim();

        // Тут выбор поля сортировки (компаратора)
        System.out.println("Как отсортировать?");
        System.out.println("1. По названию");
        System.out.println("2. По автору");
        System.out.println("3. По году");
        System.out.print("Ваш выбор: ");

        String fieldChoice = scanner.nextLine().trim();

        Comparator<Book> comparator;
        switch (fieldChoice) {
            case "1" -> // По названию, если содержит числа - сортируем по числу (из-за реализованной генерации книг и авторов с просто добавленной нумерацией). Если чисел нет, то книга уходит в конец списка - сортируем мх между собой по алфавиту
                    comparator = Comparator
                            .comparingInt((Book b) -> {
                                String title = b.getTitle() == null ? "" : b.getTitle();
                                String digits = title.replaceAll("\\D+", ""); // вытаскиваем число из названия
                                return digits.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(digits);
                            })
                            .thenComparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER) // если числа равны или цифр нет — алфавит по названию
                            .thenComparingInt(Book::getPublicationYear);                  // окончательный тай-брейкер

            case "2" -> // По автору, в алфавит порядке с тай-брейкерами
                    comparator = Comparator
                            .comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER)
                            .thenComparingInt(Book::getPublicationYear);

            case "3" -> // По году, по возрастанию года + тай-брейкеры
                    comparator = Comparator
                            .comparingInt(Book::getPublicationYear)
                            .thenComparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);

            default -> {
                System.out.println("Пожалуйста, выберите пункт от 1 до 3");
                handleSortData();
                return;
            }
        }

        // Запуск нужной стратегии
        switch (strategyChoice) {
            case "2" -> {
                // Cортируем только чётные publicationYear
                sortingService.sort(
                        bookList,
                        new EvenOddSortStrategy<Book>(Book::getPublicationYear),
                        comparator
                );
            }
            case "1" -> {
                // Обычная стратегия
                sortingService.sort(
                        bookList,
                        new MergeSortStrategy<>(),
                        comparator
                );
            }
            default -> {
                System.out.println("Пожалуйста, выберите пункт 1 или 2");
                handleSortData();
                return;
            }
        }

        System.out.println("Готово. Текущий список:");
        for (Book b : bookList) {
            System.out.println(b);
        }
    }


    private void handleFindElement() {
        System.out.println("\n=== Найти книгу ===");
        System.out.print("Введите название книги: ");
        Book book = new Book.Builder()
                            .setTitle(scanner.nextLine())
                            .build();
        Comparator bookComparator = Comparator.comparing(Book::getTitle);
        sortingService.sort(bookList, new MergeSortStrategy<>(), bookComparator);
        int  index = BinarySearchUtil.find(bookList, book, bookComparator);
        if (index == -1)
            System.out.print("Книги нет в списке");
        else{
            book = bookList.get(index);
            System.out.println(book);
        }
    }

    private void handleCountOccurrences() {
        if (bookList.isEmpty()) {
            System.out.println("Список книг пуст. Сначала загрузите данные.");
            return;
        }

        System.out.println("\n=== Подсчет вхождений книги ===");
        System.out.println("Введите данные для искомой книги:");

        System.out.print("Введите название: ");
        String title = scanner.nextLine();

        System.out.print("Введите автора: ");
        String author = scanner.nextLine();

        System.out.print("Введите год публикации: ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: год должен быть числом. Операция отменена.");
            return;
        }

        Book bookToFind = new Book.Builder()
                .setTitle(title)
                .setAuthor(author)
                .setPublicationYear(year)
                .build();

        System.out.println("Запускаем подсчет для: " + bookToFind + "...");
        int occurrences = counterService.countOccurrences(bookList, bookToFind);

        if (occurrences >= 0) {
            System.out.println("Результат: данная книга встречается в коллекции " + occurrences + " раз(а).");
        } else {
            System.out.println("Произошла ошибка в процессе подсчета.");
        }
    }

    private void handleWriteToFile() {
        // Здесь будет вызываться сервис для записи данных в файл.
    }
}