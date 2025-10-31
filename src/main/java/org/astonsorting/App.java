package org.astonsorting;

import org.astonsorting.model.Book;
import org.astonsorting.util.BinarySearchUtil;
import org.astonsorting.util.DataLoader;
import org.astonsorting.util.DataWriter;
import org.astonsorting.collection.CustomArrayList;
import org.astonsorting.service.SortingService;
import org.astonsorting.service.strategy.MergeSortStrategy;
import org.astonsorting.service.strategy.ParallelMergeSortStrategy;
import org.astonsorting.service.CollectionCounterService;
import org.astonsorting.service.strategy.EvenOddSortStrategy;
import org.astonsorting.service.strategy.Sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {
    private final CustomArrayList<Book> bookList;
    private final Scanner scanner;
    private final DataLoader dataLoader;
    private final SortingService<Book> sortingService;
    private final DataWriter dataWriter;
    private final CollectionCounterService counterService;

    public App() {
        this.bookList = new CustomArrayList<>();
        this.scanner = new Scanner(System.in);
        this.dataLoader = new DataLoader();
        this.sortingService = new SortingService<>();
        this.dataWriter = new DataWriter();
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
        System.out.println("\n=== Загрузить Книги ===");
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
        System.out.println("\n=== Отсортировать текущий список ===");
        System.out.println("Статус данных: [" + bookList.size() + " книг]");

        // добавлен выбор сортировки (обычная и чёт/нечёт)
        System.out.println("Выберите тип сортировки:");
        System.out.println("1. Обычная (MergeSort)");
        System.out.println("2. Кастомная: чётные годы сортируются, нечётные остаются на месте");
        System.out.print("Ваш выбор: ");
        String sortType = scanner.nextLine();

        if ("2".equals(sortType)) {
            // кастомная сортировка, где чётные сортируем, а нечётные остаются на месте
            System.out.println("1. По названию");
            System.out.println("2. По автору");
            System.out.println("3. По году");
            System.out.print("Ваш выбор: ");
            String fieldForCustom = scanner.nextLine();

            Comparator<Book> customComparator;
            switch (fieldForCustom) {
                case "1" -> customComparator = Comparator.comparing(Book::getTitle);
                case "2" -> customComparator = Comparator.comparing(Book::getAuthor);
                case "3" -> customComparator = Comparator.comparingInt(Book::getPublicationYear);
                default -> {
                    System.out.println("Пожалуйста, выберите пункт от 1 до 3.");
                    return;
                }
            }
            // тут сортируются только чётные годы переданным компаратором
            sortingService.sort(bookList, new EvenOddSortStrategy<Book>(Book::getPublicationYear), customComparator);
            return; // выходим
        }

        // Merge сортировка
        System.out.println("1. По названию");
        System.out.println("2. По автору");
        System.out.println("3. По году");
        System.out.print("Ваш выбор: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                sortingService.sort(bookList, new ParallelMergeSortStrategy<>(), Comparator.comparing(Book::getTitle));
                break;
            case "2":
                sortingService.sort(bookList, new ParallelMergeSortStrategy<>(), Comparator.comparing(Book::getAuthor));
                break;
            case "3":
                sortingService.sort(bookList, new ParallelMergeSortStrategy<>(), Comparator.comparing(Book::getPublicationYear));
                break;
            default:
                System.out.println("Пожалуйста, выберите пункт от 1 до 3.");
                handleSortData();
                break;
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
        System.out.println("\n=== Записать книги в файлы ===");
        System.out.print("Введите путь к файлу: ");
        dataWriter.writeListToFile(scanner.nextLine(), bookList);
    }
}