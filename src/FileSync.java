import subclasses.CreateCopyVisitor;
import subclasses.DeleteVisitor;

import java.io.IOException;
import java.nio.file.*;

public class FileSync {

    private Path src;
    private Path dst;

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Please use format FileSync SourceDir DestinationDir");
        }
        new FileSync().run(args[0], args[1]);
    }

    private void run(String source, String destination) {
        System.out.println("Start synchronization " + source + " -> " + destination);
        setSourceRoot(source);
        setDestinationRoot(destination);
        try {
            Files.walkFileTree(dst, new DeleteVisitor(src, dst));
            Files.walkFileTree(src, new CreateCopyVisitor(src, dst));
        } catch (IOException e) {
            throw new RuntimeException("Synchronization Failed");
        }
        System.out.println("Synchronization successfully completed");
    }

    private void setSourceRoot(String dir) {
        if (dir == null || dir.length() == 0) {
            throw new IllegalArgumentException("Source directory isn't specified");
        }
        src = Paths.get(dir);
        if (!Files.exists(src) || !Files.isDirectory(src)) {
            throw new IllegalArgumentException("Directory " + dir + " doesn't exist");
        }
    }

    private void setDestinationRoot(String dir) {
        if (dir == null || dir.length() == 0) {
            throw new IllegalArgumentException("Destination directory isn't specified");
        }
        dst = Paths.get(dir);
        if (Files.exists(dst)) {
            if (!Files.isDirectory(dst)) {
                throw new IllegalArgumentException(dir +" isn't a directory");
            }
        } else {
            try {
                Files.createDirectory(dst);
                System.out.println("Create " + dst);
            } catch (IOException e) {
                throw new RuntimeException("Can't create " + dir);
            }
        }
    }
}
