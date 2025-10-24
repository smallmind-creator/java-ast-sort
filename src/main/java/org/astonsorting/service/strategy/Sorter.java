package org.astonsorting.service.strategy;

import org.astonsorting.collection.CustomArrayList;
import java.util.Comparator;

public interface Sorter<T> {
    CustomArrayList<T> sort (CustomArrayList<T> list, Comparator<T> comparator);
}
