package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.output.FileReportOutput;
import com.lschaan.dataanalysisservice.stub.DataInputStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileProcessorServiceTest {

    @Mock
    private DataAnalysisService dataAnalysisService;

    @Mock
    private FileService fileService;

    @Mock
    private ReportGeneratorService reportGeneratorService;

    @InjectMocks
    private FileProcessorService fileProcessorService;

    @Captor
    private ArgumentCaptor<File> fileArgumentCaptor;

    @Test
    public void shouldProcessFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");
        String outputFilename = "valid-file.done.dat";

        when(fileService.readFile(any(File.class))).thenReturn(Collections.emptyList());
        when(dataAnalysisService.getDataFromContent(any())).thenReturn(DataInputStub.createList());
        when(reportGeneratorService.generateReportFromData(any())).thenReturn(new FileReportOutput());
        when(reportGeneratorService.generateOutputContentFromReport(any(FileReportOutput.class))).thenReturn(Collections.emptyList());
        when(reportGeneratorService.getOutputFilename(any(FileReportOutput.class))).thenReturn(outputFilename);
        fileProcessorService.processFile(file);

        verify(fileService).readFile(fileArgumentCaptor.capture());
        verify(dataAnalysisService).getDataFromContent(any());
        verify(reportGeneratorService).generateOutputContentFromReport(any());
        verify(reportGeneratorService).generateReportFromData(any());
        verify(reportGeneratorService).getOutputFilename(any(FileReportOutput.class));
        verify(fileService).writeFile(eq(outputFilename), any());

        assertEquals(file.getName(), fileArgumentCaptor.getValue().getName());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExcpetionWhenWriteFileThrowsException() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");
        String outputFilename = "valid-file.done.dat";

        when(fileService.readFile(any(File.class))).thenReturn(Collections.emptyList());
        when(dataAnalysisService.getDataFromContent(anyList())).thenReturn(DataInputStub.createList());
        when(reportGeneratorService.generateReportFromData(anyList())).thenReturn(new FileReportOutput());
        when(reportGeneratorService.generateOutputContentFromReport(any(FileReportOutput.class))).thenReturn(Collections.emptyList());
        when(reportGeneratorService.getOutputFilename(any(FileReportOutput.class))).thenReturn(outputFilename);
        doThrow(new RuntimeException()).when(fileService).writeFile(anyString(), anyList());
        fileProcessorService.processFile(file);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExcpetionWhenGetDataFromContentThrowsException() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");

        when(fileService.readFile(any(File.class))).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException()).when(dataAnalysisService).getDataFromContent(anyList());
        fileProcessorService.processFile(file);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenReadFileThrowsException() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");

        doThrow(new RuntimeException()).when(fileService).readFile(any(File.class));
        fileProcessorService.processFile(file);
    }
}
