package com.lschaan.dataanalysisservice.stub;

import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ChangedFileStub {

    public static Set<ChangedFile> createChangedFileSet(File file) {
        return createChangedFileSet(file, ChangedFile.Type.ADD);
    }

    public static Set<ChangedFile> createChangedFileSet(File file, ChangedFile.Type type) {
        Set<ChangedFile> changedFileSet = new HashSet<>();
        ChangedFile changedFile = new ChangedFile(file.getParentFile(), file, type);
        changedFileSet.add(changedFile);
        return changedFileSet;
    }

    public static Set<ChangedFiles> createChangedFilesSet(File file) {
        Set<ChangedFile> changedFileSet = createChangedFileSet(file);
        return createChangedFilesSet(file.getParentFile(), changedFileSet);
    }

    public static Set<ChangedFiles> createChangedFilesSet(File directory, Set<ChangedFile> changedFile) {
        Set<ChangedFiles> changedFilesSet = new HashSet<>();
        ChangedFiles changedFiles = new ChangedFiles(directory, changedFile);
        changedFilesSet.add(changedFiles);
        return changedFilesSet;
    }
}
