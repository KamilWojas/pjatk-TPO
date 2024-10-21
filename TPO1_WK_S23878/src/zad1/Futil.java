package zad1;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        Path dir = Paths.get(dirName);
        Path resultFile = Paths.get(resultFileName);
        Charset inputCharset = Charset.forName("Cp1250");
        Charset outputCharset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, outputCharset, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (Files.isRegularFile(file)) {
                        try (FileChannel channel = FileChannel.open(file)) {
                            ByteBuffer buffer = ByteBuffer.allocate(4096);
                            CharBuffer charBuffer = CharBuffer.allocate(4096);
                            CharsetDecoder decoder = inputCharset.newDecoder();
                            while (channel.read(buffer) > 0) {
                                buffer.flip();
                                decoder.decode(buffer, charBuffer, false);
                                charBuffer.flip();
                                writer.write(charBuffer.toString());
                                charBuffer.clear();
                                buffer.clear();
                            }
                            decoder.decode(buffer, charBuffer, true);
                            charBuffer.flip();
                            writer.write(charBuffer.toString());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
