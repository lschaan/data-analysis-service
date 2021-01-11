package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.stub.ChangedFileStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryMonitorServiceTest {

    @Mock
    private FileProcessorService fileProcessorService;
    @InjectMocks
    private DirectoryMonitorService directoryMonitorService;

    @Captor
    private ArgumentCaptor<File> fileArgumentCaptor;

    @Test
    public void shouldProcessFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");
        Set<ChangedFiles> changedSet = ChangedFileStub.createChangedFilesSet(file);

        directoryMonitorService.onChange(changedSet);
        verify(fileProcessorService).processFile(fileArgumentCaptor.capture());
        assertEquals(file.getName(), fileArgumentCaptor.getValue().getName());
    }

    @Test
    public void shouldProcessMultipleFiles() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:another-valid-file.dat");
        File file2 = ResourceUtils.getFile("classpath:valid-file.dat");
        Set<ChangedFiles> changedSet = ChangedFileStub.createChangedFilesSet(file);
        Set<ChangedFile> changedFileSet = ChangedFileStub.createChangedFileSet(file2);
        changedSet.add(new ChangedFiles(file.getParentFile(), changedFileSet));

        directoryMonitorService.onChange(changedSet);
        verify(fileProcessorService, times(2)).processFile(fileArgumentCaptor.capture());
    }

    @Test
    public void shouldNotProcessDeletedFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:valid-file.dat");
        Set<ChangedFile> changedFileSet = ChangedFileStub.createChangedFileSet(file, ChangedFile.Type.DELETE);
        Set<ChangedFiles> changedSet = ChangedFileStub.createChangedFilesSet(file.getParentFile(), changedFileSet);

        directoryMonitorService.onChange(changedSet);
        verify(fileProcessorService, never()).processFile(fileArgumentCaptor.capture());
    }

    @Test
    public void shouldNotProcessFileWithWrongSuffix() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:invalid-file.txt");
        Set<ChangedFiles> changedSet = ChangedFileStub.createChangedFilesSet(file);

        directoryMonitorService.onChange(changedSet);
        verify(fileProcessorService, never()).processFile(fileArgumentCaptor.capture());
    }

}
