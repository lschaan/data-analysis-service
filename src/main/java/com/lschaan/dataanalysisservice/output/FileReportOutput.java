package com.lschaan.dataanalysisservice.output;

import com.lschaan.dataanalysisservice.input.SaleInput;
import com.lschaan.dataanalysisservice.input.SalesmanInput;

public class FileReportOutput {

    private String originalFilename;
    private Long customerAmount;
    private Long salesmanAmount;
    private SaleInput bestSale;
    private SalesmanInput worstSalesman;

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Long getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(Long customerAmount) {
        this.customerAmount = customerAmount;
    }

    public Long getSalesmanAmount() {
        return salesmanAmount;
    }

    public void setSalesmanAmount(Long salesmanAmount) {
        this.salesmanAmount = salesmanAmount;
    }

    public SaleInput getBestSale() {
        return bestSale;
    }

    public void setBestSale(SaleInput bestSale) {
        this.bestSale = bestSale;
    }

    public SalesmanInput getWorstSalesman() {
        return worstSalesman;
    }

    public void setWorstSalesman(SalesmanInput worstSalesman) {
        this.worstSalesman = worstSalesman;
    }
}
