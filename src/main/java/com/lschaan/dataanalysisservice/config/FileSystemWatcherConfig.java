package com.lschaan.dataanalysisservice.config;

import com.lschaan.dataanalysisservice.helper.FileHelper;
import com.lschaan.dataanalysisservice.service.DirectoryMonitorService;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;

@Configuration
public class FileSystemWatcherConfig {

    @Bean
    public FileSystemWatcher fileSystemWatcher(DirectoryMonitorService directoryMonitorService) {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher();
        fileSystemWatcher.addListener(directoryMonitorService);
        fileSystemWatcher.addSourceDirectory(directoryToMonitor());
        fileSystemWatcher.start();
        return fileSystemWatcher;
    }

    private File directoryToMonitor() {
        return new File(FileHelper.getInputDirectory());
    }

}