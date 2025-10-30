package org.astonsorting.service.strategy;

import org.astonsorting.collection.CustomArrayList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//Стратегия, которая сортирует элемегты только с чётным publicationYear (элементы с нечётным годом остаются на месте)
// Сравнение чётных выполняется переданным компаратором
// Возвращается новый CustomArrayList<T>

public class EvenOddSortStrategy<T> implements Sorter<T> {

    //Извлечение года публикации из элемента T
    @FunctionalInterface
    public interface PublicationYearExtractor<T> {
        int getPublicationYear(T item);
    }

    private final PublicationYearExtractor<T> yearExtractor;

    public EvenOddSortStrategy(PublicationYearExtractor<T> yearExtractor) {
        this.yearExtractor = Objects.requireNonNull(yearExtractor, "yearExtractor must not be null");
    }

    @Override
    public CustomArrayList<T> sort(CustomArrayList<T> list, Comparator<T> comparator) {
        if (list == null) throw new IllegalArgumentException("list must not be null");
        if (comparator == null) throw new IllegalArgumentException("comparator must not be null");

        int n = list.size();
        // возвращает копию как есть, если 0 или 1 элемент
        if (n < 2) {
            CustomArrayList<T> copy = new CustomArrayList<>();
            for (int i = 0; i < n; i++) copy.add(list.get(i));
            return copy;
        }

        // отмечаем позиции чётных и собираем чётные элементы
        boolean[] evenPos = new boolean[n];
        CustomArrayList<T> evens = new CustomArrayList<>();
        for (int i = 0; i < n; i++) {
            T item = list.get(i);
            boolean isEven = (yearExtractor.getPublicationYear(item) & 1) == 0; // чётный год?
            evenPos[i] = isEven;
            if (isEven) evens.add(item);
        }

        // Если чётных < 2, то перестановок не будет, т.е. вернём копию исходного списка
        if (evens.size() < 2) {
            CustomArrayList<T> copy = new CustomArrayList<>();
            for (int i = 0; i < n; i++) copy.add(list.get(i));
            return copy;
        }

        // сортируем только чётные по компаратору
        evens = new MergeSortStrategy<T>().sort(evens, comparator);

        // сбор нового списка
        // на местах evenPos[i] = true разместили отсортированные чётные по порядку
        //на остальных позициях оставили нечётные элементы
        CustomArrayList<T> result = new CustomArrayList<>();
        int k = 0; // индекс в списке чётных
        for (int i = 0; i < n; i++) {
            if (evenPos[i]) {
                result.add(evens.get(k++));
            } else {
                result.add(list.get(i));
            }
        }

        return result;
    }
}
