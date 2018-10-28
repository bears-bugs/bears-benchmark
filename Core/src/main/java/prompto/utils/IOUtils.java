package prompto.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public abstract class IOUtils {

	public static void deleteFilesRecursively(File root, boolean deleteRoot) throws IOException {
		Path rootPath = root.toPath();
		   Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   Files.delete(file);
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				   if(deleteRoot || !dir.equals(rootPath))
					   Files.delete(dir);
				   return FileVisitResult.CONTINUE;
			   }

		   });
	}
	
	public static byte[] readStreamFully(InputStream stream) throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		for(;;) {
			int read = stream.read(buffer);
			if(read<0)
				break;
			data.write(buffer, 0, read);
		}
		data.flush();
		return data.toByteArray();
	}
	
	public static String readFileToString(File file) throws IOException {
		char[] chars = new char[2048];
		StringBuilder sb = new StringBuilder();
		try(Reader reader = new FileReader(file)) {
			int read = reader.read(chars);
			while(read>=0) {
				sb.append(chars, 0, read);
				read = reader.read(chars);
			}
		}
		return sb.toString();
	}

}
