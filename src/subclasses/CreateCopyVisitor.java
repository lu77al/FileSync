package subclasses;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CreateCopyVisitor implements FileVisitor<Path> {
    private Path src;
    private Path dst;

    public CreateCopyVisitor(Path source, Path destination) {
        this.src = source;
        this.dst = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path destDirPath = dst.resolve(src.relativize(dir));
        if (!Files.exists(destDirPath)) {
            Files.createDirectory(destDirPath);
            System.out.println("Create " + destDirPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path destFilePath = dst.resolve(src.relativize(file));
        boolean copyFile = !Files.exists(destFilePath);
        if (!copyFile) {
            copyFile = Files.size(file) != Files.size(destFilePath);
        }
        if (copyFile) {
            Files.copy(file, destFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copy " + file + " -> " + destFilePath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}

