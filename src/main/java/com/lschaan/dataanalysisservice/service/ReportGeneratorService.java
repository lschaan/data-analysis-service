package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;
import com.lschaan.dataanalysisservice.output.FileReportOutput;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lschaan.dataanalysisservice.helper.Constants.INPUT_FILE_SUFFIX;
import static com.lschaan.dataanalysisservice.helper.Constants.OUTPUT_FILE_SUFFIX;

@Service
public class ReportGeneratorService {

    private final DataAnalysisService dataAnalysisService;

    public ReportGeneratorService(DataAnalysisService dataAnalysisService) {
        this.dataAnalysisService = dataAnalysisService;
    }

    public FileReportOutput generateReportFromData(List<DataInput> data) {
        FileReportOutput report = new FileReportOutput();

        Long customerAmount = countCustomers(data);
        report.setCustomerAmount(customerAmount);

        Long salesmanAmount = countSalesmen(data);
        report.setSalesmanAmount(salesmanAmount);

        SaleInput bestSale = getBestSale(data);
        report.setBestSale(bestSale);

        SalesmanInput worstSalesman = getWorstSalesman(data);
        report.setWorstSalesman(worstSalesman);

        return report;
    }

    private Long countCustomers(List<DataInput> data) {
        return data.stream()
                .filter(dataAnalysisService::isCustomer)
                .count();
    }

    private Long countSalesmen(List<DataInput> data) {
        return data.stream()
                .filter(dataAnalysisService::isSalesman)
                .count();
    }

    private SaleInput getBestSale(List<DataInput> data) {
        SaleInput bestSale = null;
        List<SaleInput> sales = getSales(data);

        for (SaleInput sale : sales) {
            double saleValue = sale.getTotalValue();

            if (bestSale == null || saleValue > bestSale.getTotalValue()) {
                bestSale = sale;
            }
        }

        return bestSale;
    }

    private List<SaleInput> getSales(List<DataInput> data) {
        return data.stream()
                .filter(dataAnalysisService::isSale)
                .map(dataInput -> (SaleInput) dataInput)
                .collect(Collectors.toList());
    }

    private SalesmanInput getWorstSalesman(List<DataInput> data) {
        List<SalesmanInput> salesmen = getSalesmen(data);
        List<SaleInput> sales = getSales(data);

        SalesmanInput worstSalesman = null;
        double worstSalesmanTotal = 0.0;

        for (SalesmanInput salesman : salesmen) {
            double salesmanTotal = getSalesmanTotal(sales, salesman);

            if (worstSalesman == null || salesmanTotal < worstSalesmanTotal) {
                worstSalesmanTotal = salesmanTotal;
                worstSalesman = salesman;
            }
        }

        return worstSalesman;
    }

    private List<SalesmanInput> getSalesmen(List<DataInput> data) {
        return data.stream()
                .filter(dataAnalysisService::isSalesman)
                .map(dataInput -> (SalesmanInput) dataInput)
                .collect(Collectors.toList());
    }

    private double getSalesmanTotal(List<SaleInput> sales, SalesmanInput salesman) {
        return sales.stream()
                .filter(sale -> sale.isFromSalesman(salesman.getName()))
                .mapToDouble(SaleInput::getTotalValue)
                .sum();
    }

    public List<String> generateOutputContentFromReport(FileReportOutput report) {
        List<String> outputContent = new ArrayList<>();

        outputContent.add("---Análise do arquivo " + report.getOriginalFilename() + "---");
        outputContent.add("Total de clientes: " + report.getCustomerAmount() + ".");
        outputContent.add("Total de vendedores: " + report.getSalesmanAmount() + ".");
        outputContent.add("ID da melhor venda: " + report.getBestSale().getId() + ".");
        outputContent.add("Pior vendedor encontrado: " + report.getWorstSalesman().getName() + ".");

        return outputContent;
    }

    public String getOutputFilename(FileReportOutput report) {
        return report.getOriginalFilename().replace(INPUT_FILE_SUFFIX, OUTPUT_FILE_SUFFIX);
    }
}
