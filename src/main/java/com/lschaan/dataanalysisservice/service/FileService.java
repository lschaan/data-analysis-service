package com.lschaan.dataanalysisservice.service;

import com.lschaan.dataanalysisservice.helper.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public List<String> readFile(File file) {
        logger.info("Iniciando leitura de conteúdo do arquivo {}", file.getName());
        List<String> content = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file.getAbsolutePath());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();

            while (line != null) {
                content.add(line);
                line = bufferedReader.readLine();
            }

        } catch (IOException ioException) {
            logger.error("Erro na leitura do arquivo {}", file.getName(), ioException);
            throw new UncheckedIOException("Erro na leitura do arquivo {} " + file.getName(), ioException);
        }

        logger.info("Leitura do arquivo {} realizada com sucesso. Conteúdo tem {} linhas.", file.getName(), content.size());
        return content;
    }

    public void writeFile(String filename, List<String> content) {
        File outputDirectory = createOutputDirectory();
        String outputFilename = buildOutputFilename(outputDirectory, filename);
        logger.info("Tentando escrever arquivo {}", outputFilename);

        try (FileWriter fileWriter = new FileWriter(outputFilename);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String line : content) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        } catch (IOException ioe) {
            throw new UncheckedIOException("Erro na escrita do arquivo {} " + filename, ioe);
        }
    }

    private File createOutputDirectory() {
        File outputDirectory = new File(FileHelper.getOutputDirectory());

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        return outputDirectory;
    }

    private String buildOutputFilename(File outputDirectory, String filename) {
        return outputDirectory.getPath() + "/" + filename;
    }
}
