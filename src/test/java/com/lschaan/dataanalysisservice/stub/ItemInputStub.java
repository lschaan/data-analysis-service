package com.lschaan.dataanalysisservice.stub;

import com.lschaan.dataanalysisservice.input.ItemInput;
import java.util.ArrayList;
import java.util.List;

public class ItemInputStub {

    public static List<ItemInput> createItems() {
        List<ItemInput> items = new ArrayList<>();
        items.add(createItem("01", 15L, 1.0));
        items.add(createItem("02", 5L, 2.0));
        return items;
    }

    public static ItemInput createItem() {
        return createItem("01", 10L, 10.0);
    }

    public static ItemInput createItem(String id, Long quantity, double price) {
        return new ItemInput(id, quantity, price);
    }
}
