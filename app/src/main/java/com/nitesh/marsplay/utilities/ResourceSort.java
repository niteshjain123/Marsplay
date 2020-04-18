package com.nitesh.marsplay.utilities;

import com.nitesh.marsplay.models.Resource;

import java.util.Comparator;

public class ResourceSort implements Comparator<Resource> {

    public static final String SORT_ORDER_ASC = "ASC";
    public static final String SORT_ORDER_DESC = "DESC";

    private String sortOrder;

    public ResourceSort(String sortOrder) {
        this.sortOrder = sortOrder;
    }


    @Override
    public int compare(Resource firstResource, Resource secondResource) {
        if (sortOrder.equals(SORT_ORDER_ASC)) {
            if (firstResource.getTimeStamp() == secondResource.getTimeStamp())
                return 0;
            else if (firstResource.getTimeStamp() > secondResource.getTimeStamp())
                return 1;
            else
                return -1;
        } else {
            if (firstResource.getTimeStamp() == secondResource.getTimeStamp())
                return 0;
            else if (firstResource.getTimeStamp() < secondResource.getTimeStamp())
                return 1;
            else
                return -1;
        }
    }
}
