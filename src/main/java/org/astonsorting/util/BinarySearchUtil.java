package org.astonsorting.util;

import java.security.Key;
import java.util.Comparator;

import org.astonsorting.collection.CustomArrayList;

public class BinarySearchUtil {

    public static <T> int find(CustomArrayList<T> sortedList, T key, Comparator<T> comparator){
        if(sortedList == null)
            throw new IllegalArgumentException("Список не должен быть Null");
        int low = 0,
            high = sortedList.size() - 1;

        while(low <= high)
        {
            int mid = low + (high - low);
            T midValue = (T)sortedList.get(mid);
            int cmp = comparator.compare(midValue, key);
;

            if(cmp < 0)
                low = mid + 1;
            else if(cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }
}
