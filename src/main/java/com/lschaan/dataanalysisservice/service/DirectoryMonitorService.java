package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.exception.InvalidFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Set;

import static com.lschaan.dataanalysisservice.helper.Constants.INPUT_FILE_SUFFIX;

@Service
public class DirectoryMonitorService implements FileChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryMonitorService.class);
    private final FileProcessorService fileProcessorService;

    public DirectoryMonitorService(FileProcessorService fileProcessorService) {
        this.fileProcessorService = fileProcessorService;
    }

    public void processFile(File file) {
        try {
            validateFile(file);
            fileProcessorService.processFile(file);
        } catch (Exception exception) {
            logger.error("Não foi possível realizar o processamento do arquivo {}.", file.getName(), exception);
        }
    }

    private void validateFile(File file) {
        validateExists(file);
        validateFilename(file);
    }

    private void validateExists(File file) {
        if (!file.exists()) {
            throw new InvalidFileException("Arquivo não encontrado!");
        }
    }

    private void validateFilename(File file) {
        logger.info("Validando nome do arquivo {}", file.getAbsolutePath());
        String filename = file.getName();
        if (!filename.endsWith(INPUT_FILE_SUFFIX)) {
            throw new InvalidFileException("Arquivo não é válido para processamento!");
        }
    }

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        changeSet.stream()
                .flatMap(changedFile -> changedFile.getFiles().stream())
                .filter(changedFile -> !(changedFile.getType() == ChangedFile.Type.DELETE))
                .map(ChangedFile::getFile)
                .forEach(this::processFile);
    }
}
