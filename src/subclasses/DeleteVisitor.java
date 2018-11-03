package subclasses;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteVisitor  implements FileVisitor<Path> {
    private Path src;
    private Path dst;

    public DeleteVisitor(Path source, Path destination) {
        this.src = source;
        this.dst = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        delete(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        delete(dir);
        return FileVisitResult.CONTINUE;
    }

    private void delete(Path file) throws IOException {
        Path srcFilePath = src.resolve(dst.relativize(file));
        if (!Files.exists(srcFilePath)) {
            Files.delete(file);
            System.out.println("Delete " + file);
        }
    }
}
