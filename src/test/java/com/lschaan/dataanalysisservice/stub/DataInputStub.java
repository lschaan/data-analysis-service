package com.lschaan.dataanalysisservice.stub;

import com.lschaan.dataanalysisservice.input.CustomerInput;
import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.input.ItemInput;
import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataInputStub {

    public static List<DataInput> createList() {
        List<DataInput> data = new ArrayList<>();
        data.add(createSale());
        data.add(createSale("salesman2", "02", Collections.singletonList(ItemInputStub.createItem())));
        data.add(createCustomer());
        data.add(createCustomer());
        data.add(createSalesman());
        data.add(createSalesman("salesman2"));
        return data;
    }

    public static CustomerInput createCustomer() {
        CustomerInput customer = new CustomerInput();
        customer.setName("customer");
        customer.setCpf("000111222333");
        customer.setBusinessArea("business area");
        return customer;
    }

    public static SalesmanInput createSalesman() {
        return createSalesman("salesman", 10.0, "000111222333444");
    }

    public static SalesmanInput createSalesman(String name) {
        return createSalesman(name, 10.0, "444333222111000");
    }

    public static SalesmanInput createSalesman(String name, Double salary, String cnpj) {
        SalesmanInput salesman = new SalesmanInput();
        salesman.setName(name);
        salesman.setSalary(salary);
        salesman.setCnpj(cnpj);
        return salesman;
    }

    public static SaleInput createSale() {
        return createSale("salesman", "01", ItemInputStub.createItems());
    }

    public static SaleInput createSale(String salesmanName, String id, List<ItemInput> items) {
        SaleInput sale = new SaleInput();
        sale.setSalesman(salesmanName);
        sale.setId(id);
        sale.setItems(items);
        return sale;
    }


}
