package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.input.DataInput;
import com.lschaan.dataanalysisservice.output.FileReportOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class FileProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessorService.class);
    private final FileService fileService;
    private final DataAnalysisService dataAnalysisService;
    private final ReportGeneratorService reportGeneratorService;

    public FileProcessorService(FileService fileService, DataAnalysisService dataAnalysisService, ReportGeneratorService reportGeneratorService) {
        this.fileService = fileService;
        this.dataAnalysisService = dataAnalysisService;
        this.reportGeneratorService = reportGeneratorService;
    }

    public void processFile(File file) {
        logger.info("Iniciando processamento para o arquivo {}", file.getName());
        FileReportOutput report = getReportInformationFromFile(file);
        writeOutputFileFromReport(report);
        logger.info("Processamento do arquivo {} finalizado.", file.getName());
    }

    private FileReportOutput getReportInformationFromFile(File file) {
        logger.info("Gerando informações de relatório a partir do conteúdo do arquivo {}", file.getName());
        List<DataInput> data = getDataFromFile(file);
        return buildFileReportOutput(file, data);
    }

    private FileReportOutput buildFileReportOutput(File file, List<DataInput> data) {
        FileReportOutput report = getReportInformationFromData(data);
        report.setOriginalFilename(file.getName());
        return report;
    }

    private List<DataInput> getDataFromFile(File file) {
        List<String> content = getFileContent(file);
        return getDataFromContent(content);
    }

    private List<String> getFileContent(File file) {
        return fileService.readFile(file);
    }

    private List<DataInput> getDataFromContent(List<String> content) {
        return dataAnalysisService.getDataFromContent(content);
    }

    private FileReportOutput getReportInformationFromData(List<DataInput> data) {
        return reportGeneratorService.generateReportFromData(data);
    }

    private void writeOutputFileFromReport(FileReportOutput report) {
        logger.info("Preparando informações para a escrita do relatório do arquivo {}", report.getOriginalFilename());
        List<String> outputContent = getOutputContentFromReport(report);
        String outputFilename = getOutputFilenameFromReport(report);

        writeOutputFile(outputFilename, outputContent);
    }

    private String getOutputFilenameFromReport(FileReportOutput report) {
        return reportGeneratorService.getOutputFilename(report);
    }

    private List<String> getOutputContentFromReport(FileReportOutput report) {
        return reportGeneratorService.generateOutputContentFromReport(report);
    }

    private void writeOutputFile(String filename, List<String> content) {
        fileService.writeFile(filename, content);
    }
}
