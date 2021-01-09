package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.exception.InvalidDataIdException;
import com.lschaan.dataanalysisservice.exception.InvalidFileException;
import com.lschaan.dataanalysisservice.input.CustomerInput;
import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.input.ItemInput;
import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.dataanalysisservice.helper.Constants.CUSTOMER_ID;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_CONTENT_SEPARATOR;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_CUSTOMER_BUSINESS_AREA_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_CUSTOMER_CNPJ_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_CUSTOMER_NAME_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_DATA_ID_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_CONTENT_SEPARATOR;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_ID_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_LIST_CLOSER;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_LIST_OPENER;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_LIST_SEPARATOR;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_PRICE_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_ITEM_QUANTITY_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALESMAN_CPF_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALESMAN_NAME_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALESMAN_SALARY_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALE_ID_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALE_ITEM_LIST_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.LINE_SALE_SALESMAN_NAME_POSITION;
import static com.lschaan.dataanalysisservice.helper.Constants.SALESMAN_ID;
import static com.lschaan.dataanalysisservice.helper.Constants.SALE_ID;

@Service
public class DataAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisService.class);

    public List<DataInput> getDataFromContent(List<String> content) {
        logger.info("Iniciando conversão de linhas para DataInput.");
        return content.stream()
                .map(this::createDataByLine)
                .collect(Collectors.toList());
    }

    public DataInput createDataByLine(String line) {
        String[] lineArray = splitLine(line);
        String id = getIdFromLineArray(lineArray);

        try {
            return createDataInput(id, lineArray);
        } catch (Exception exception) {
            throw new InvalidFileException("Erro de processamento na linha: " + line, exception);
        }
    }

    private DataInput createDataInput(String id, String[] lineArray) {
        switch (id) {
            case SALESMAN_ID:
                return buildSalesman(lineArray);
            case CUSTOMER_ID:
                return buildCustomer(lineArray);
            case SALE_ID:
                return buildSale(lineArray);
            default:
                throw new InvalidDataIdException("ID inválido inserido. ID: " + id);
        }
    }

    private String[] splitLine(String line) {
        return line.split(LINE_CONTENT_SEPARATOR);
    }

    private String getIdFromLineArray(String[] lineArray) {
        return lineArray[LINE_DATA_ID_POSITION];
    }

    private SalesmanInput buildSalesman(String[] lineArray) {
        SalesmanInput salesman = new SalesmanInput();
        salesman.setCnpj(lineArray[LINE_SALESMAN_CPF_POSITION]);
        salesman.setName(lineArray[LINE_SALESMAN_NAME_POSITION]);
        salesman.setSalary(Double.parseDouble(lineArray[LINE_SALESMAN_SALARY_POSITION]));
        return salesman;
    }

    private CustomerInput buildCustomer(String[] lineArray) {
        CustomerInput customer = new CustomerInput();
        customer.setCpf(lineArray[LINE_CUSTOMER_CNPJ_POSITION]);
        customer.setName(lineArray[LINE_CUSTOMER_NAME_POSITION]);
        customer.setBusinessArea(lineArray[LINE_CUSTOMER_BUSINESS_AREA_POSITION]);
        return customer;
    }

    private SaleInput buildSale(String[] lineArray) {
        SaleInput sale = new SaleInput();
        sale.setId(lineArray[LINE_SALE_ID_POSITION]);
        sale.setSalesman(lineArray[LINE_SALE_SALESMAN_NAME_POSITION]);

        List<ItemInput> items = getItemsFromLineArray(lineArray);
        sale.setItems(items);

        return sale;
    }

    private List<ItemInput> getItemsFromLineArray(String[] lineArray) {
        String[] itemArray = extractItemArrayFromLineArray(lineArray);
        return Arrays.stream(itemArray)
                .map(this::buildItem)
                .collect(Collectors.toList());
    }

    private String[] extractItemArrayFromLineArray(String[] lineArray) {
        return lineArray[LINE_SALE_ITEM_LIST_POSITION]
                .replace(LINE_ITEM_LIST_OPENER, "")
                .replace(LINE_ITEM_LIST_CLOSER, "")
                .split(LINE_ITEM_LIST_SEPARATOR);
    }

    private ItemInput buildItem(String itemString) {
        String[] itemArray = itemString.split(LINE_ITEM_CONTENT_SEPARATOR);

        ItemInput item = new ItemInput();
        item.setId(itemArray[LINE_ITEM_ID_POSITION]);
        item.setQuantity(Long.valueOf(itemArray[LINE_ITEM_QUANTITY_POSITION]));
        item.setPrice(Double.valueOf(itemArray[LINE_ITEM_PRICE_POSITION]));

        return item;
    }

    public boolean isSale(DataInput input) {
        return input instanceof SaleInput;
    }

    public boolean isCustomer(DataInput input) {
        return input instanceof CustomerInput;
    }

    public boolean isSalesman(DataInput input) {
        return input instanceof SalesmanInput;
    }
}
