package org.ifsul.games4todos.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        store(file, null, null);
    }

    @Override
    public void store(MultipartFile file, String subdirectory, String rename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path root = this.rootLocation.toAbsolutePath().normalize();

            Path targetDir = (subdirectory == null || subdirectory.isBlank())
                    ? root
                    : root.resolve(subdirectory).normalize();

            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String extension = (StringUtils.hasText(originalFilename) && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : null;

            String filename = (rename == null || subdirectory.isBlank())
                    ? originalFilename
                    : rename + extension;
            Path destinationFile = targetDir.resolve(filename).normalize();

            // cannot store files outside storage root
            if (!destinationFile.startsWith(root)) {
                throw new StorageException(
                        "Cannot store file outside storage directory.");
            }

            Files.createDirectories(targetDir);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation)
                    .filter(Files::isRegularFile)
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        Path root = rootLocation.toAbsolutePath().normalize();
        Path file = root.resolve(filename).normalize();
        if (!file.startsWith(root)) {
            throw new StorageException("Invalid path: " + filename);
        }
        return file;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public Resource loadAsResourceNoExtension(String directory, String filename) {
        Path dir = this.rootLocation.toAbsolutePath().normalize().resolve(directory);
        String pattern = filename + ".*";

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern)) {
            for (Path entry : stream) {
                Resource resource = new UrlResource(entry.toUri());

                if (resource.exists() || resource.isReadable())
                    return resource;
            }
        }
        catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }

        return null;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void createDirectory(String directoryName){
        Path root = rootLocation.toAbsolutePath().normalize();
        Path subdirectory = root.resolve(directoryName).normalize();

        if (!subdirectory.startsWith(root)) {
            throw new StorageException("Cannot create directory outside storage root.");
        }

        try {
            Files.createDirectories(subdirectory);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}