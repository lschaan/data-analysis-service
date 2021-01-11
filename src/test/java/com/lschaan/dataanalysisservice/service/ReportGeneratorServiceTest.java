package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;
import com.lschaan.dataanalysisservice.output.FileReportOutput;
import com.lschaan.dataanalysisservice.stub.DataInputStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ReportGeneratorServiceTest {

    @InjectMocks
    private ReportGeneratorService reportGeneratorService;

    @Test
    public void shouldGenerateFileReport() {
        List<DataInput> data = DataInputStub.createList();
        FileReportOutput output = reportGeneratorService.generateReportFromData(data);

        SaleInput bestSale = output.getBestSale();
        assertEquals(100.0, bestSale.getTotalValue());
        assertEquals("02", bestSale.getId());
        assertEquals(1, bestSale.getItems().size());

        SalesmanInput worstSalesman = output.getWorstSalesman();
        assertEquals("salesman", worstSalesman.getName());
        assertEquals("000111222333444", worstSalesman.getCnpj());

        assertEquals(2L, output.getCustomerAmount());
        assertEquals(2L, output.getSalesmanAmount());
    }

    @Test
    public void shouldGetOutputFilename() {
        FileReportOutput report = new FileReportOutput();
        report.setOriginalFilename("filename.dat");

        String outputFilename = reportGeneratorService.getOutputFilename(report);
        assertEquals("filename.done.dat", outputFilename);
    }

    @Test
    public void shouldGenerateContent() {
        FileReportOutput report = new FileReportOutput();
        report.setBestSale(DataInputStub.createSale());
        report.setCustomerAmount(2L);
        report.setSalesmanAmount(3L);
        report.setWorstSalesman(DataInputStub.createSalesman());

        List<String> content = reportGeneratorService.generateOutputContentFromReport(report);
        assertTrue(content.contains("Total de clientes: 2."));
        assertTrue(content.contains("Total de vendedores: 3."));
        assertTrue(content.contains("ID da melhor venda: 01."));
        assertTrue(content.contains("Pior vendedor encontrado: salesman."));
    }
}
