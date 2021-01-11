package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.exception.InvalidFileException;
import com.lschaan.dataanalysisservice.input.CustomerInput;
import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.input.ItemInput;
import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DataAnalysisServiceTest {

    @InjectMocks
    private DataAnalysisService dataAnalysisService;

    @Test
    public void shouldReturnListWithCustomer() {
        List<String> content = Collections.singletonList("002ç2345675434544345çJose da SilvaçRural");
        List<DataInput> data = dataAnalysisService.getDataFromContent(content);

        assertEquals(1, data.size());
        assertTrue(data.get(0) instanceof CustomerInput);

        CustomerInput customer = (CustomerInput) data.get(0);
        assertEquals("2345675434544345", customer.getCpf());
        assertEquals("Jose da Silva", customer.getName());
        assertEquals("Rural", customer.getBusinessArea());
    }

    @Test
    public void shouldReturnListWithSalesman() {
        List<String> content = Collections.singletonList("001ç3245678865434çRenatoç40000.99");
        List<DataInput> data = dataAnalysisService.getDataFromContent(content);

        assertEquals(1, data.size());
        assertTrue(data.get(0) instanceof SalesmanInput);

        SalesmanInput salesman = (SalesmanInput) data.get(0);
        assertEquals("3245678865434", salesman.getCnpj());
        assertEquals("Renato", salesman.getName());
        assertEquals(40000.99, salesman.getSalary());
    }

    @Test
    public void shouldReturnListWithSale() {
        List<String> content = Collections.singletonList("003ç11ç[1-11-100,2-30-2.50,3-40-3.10]çRoberto");
        List<DataInput> data = dataAnalysisService.getDataFromContent(content);

        assertEquals(1, data.size());
        assertTrue(data.get(0) instanceof SaleInput);

        SaleInput sale = (SaleInput) data.get(0);
        assertEquals("11", sale.getId());
        assertEquals("Roberto", sale.getSalesman());

        List<ItemInput> items = sale.getItems();
        assertEquals(3, items.size());
        assertEquals(1299.0, sale.getTotalValue());
    }

    @Test(expected = InvalidFileException.class)
    public void shouldThrowExceptionWhenAnyLineContainsInvalidID() {
        List<String> content = Arrays.asList("001ç3245678865434çRenatoç40000", "005ç000000000çINVALIDç0.0", "002ç2345675434544345çJose da SilvaçRural");
        dataAnalysisService.getDataFromContent(content);
    }

    @Test(expected = InvalidFileException.class)
    public void shouldThrowExceptionWhenAnyLineFailsToConvert() {
        List<String> content = Arrays.asList("001ç3245678865434çRenatoç40000", "001ç000000000INVALIDç0.0", "002ç2345675434544345çJose da SilvaçRural");
        dataAnalysisService.getDataFromContent(content);
    }

}
