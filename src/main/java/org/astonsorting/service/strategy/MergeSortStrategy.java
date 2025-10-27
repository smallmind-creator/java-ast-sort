package org.astonsorting.service.strategy;

//import org.astonsorting.service.strategy.Sorter;
import org.astonsorting.collection.CustomArrayList;
import java.util.Comparator;

/**
 * Реализация стратегии сортировки слиянием.
 * Для сортировки объектов, реализующих Comparable:
 * Comparator.naturalOrder()
 * (b1, b2) -> b1.compareTo(b2)
 * Comparator.comparing(Book::getTitle)
 * Comparator.comparing(Book::getAuthor)
 * Comparator.comparing(Book::getPublicationYear)
 */
public class MergeSortStrategy<T> implements Sorter<T>{
    @Override
    public CustomArrayList<T> sort(CustomArrayList<T> list, Comparator<T> comparator) {
        if(list == null) return null;

        if(list.size() < 2) return list;

        CustomArrayList<T> leftSideList = new CustomArrayList<>();
        for(int index = 0; index < (list.size() / 2); index++) {
            leftSideList.add(list.get(index));
        }

        CustomArrayList<T> rightSideList = new CustomArrayList<>();
        for(int index = (list.size() / 2); index < list.size(); index++) {
            rightSideList.add(list.get(index));
        }

        leftSideList = sort(leftSideList, comparator);
        rightSideList = sort(rightSideList, comparator);

        return mergeSortList(leftSideList, rightSideList, comparator);
    }

/**
* Объединение частей коллекции list.
* Применение comparator для сравнения пары классов Book из leftSideList и rightSideList соответственно.
*/
    private CustomArrayList<T> mergeSortList(CustomArrayList<T> leftSideList, CustomArrayList<T> rightSideList,
                                             Comparator<T> comparator) {
        CustomArrayList<T> resList = new CustomArrayList<>();

        int posLeft = 0;
        int posRight = 0;

        while(posLeft < leftSideList.size() && posRight < rightSideList.size()) {
            if(comparator.compare(leftSideList.get(posLeft), rightSideList.get(posRight)) <= 0) {
                resList.add(leftSideList.get(posLeft));
                posLeft++;
            }
            else {
                resList.add(rightSideList.get(posRight));
                posRight++;
            }
        }

        while(posLeft < leftSideList.size()) {
            resList.add(leftSideList.get(posLeft));
            posLeft++;
        }

        while(posRight < rightSideList.size()) {
            resList.add(rightSideList.get(posRight));
            posRight++;
        }

        return resList;
    }
}
