package common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

@Slf4j
public class FileUtil {

	// Prevent instantiation
	private FileUtil() {
	}

	public static File getFile(String filePath) throws IOException {
		validateFilePath(filePath);
		return getFileAsResource(filePath);
	}

	public static void validateFilePath(String filePath) throws FileNotFoundException {
		if (StringUtils.isBlank(filePath)) {
			throw new FileNotFoundException("File path was null/empty...");
		}

		try {
			File file = getFileWithoutClasspath(filePath);
			if (!fileIsPresent(file))
				throw new FileNotFoundException();
		} catch (IOException e) {
			File file = getFileAsResource(filePath);
			if (!fileIsPresent(file))
				throw new FileNotFoundException("File does not exist? " + filePath);
		}
	}

	public static File getFileAsResource(String filePath) {
		File potentialFile = getFileWithoutClasspath(filePath);
		if (potentialFile != null)
			return potentialFile;

		try {
			return generateFileFromClassPathFile(filePath);
		} catch (Exception e) {
			log.debug("Attempted to get {} but encountered {}", filePath, CommonUtils.getMsgAndCauseFromException(e));
		}

		// Is there a more graceful fallback for this?
		return null;
	}

	public static File getFileWithoutClasspath(String filePath) {
		if (fileExistsInMainResources(filePath))
			return getFileFromMainResources(filePath);

		if (fileExistsInTestResources(filePath))
			return getFileFromTestResources(filePath);

		File potentialFile = new File(filePath);
		if (potentialFile.exists())
			return potentialFile;

		return null;
	}

	public static boolean fileExistsInMainResources(String filePath) {
		String fullPath = Constants.STACK_MAIN_RESOURCE_DIR + filePath;
		return new File(fullPath).exists();
	}

	public static File getFileFromMainResources(String filePath) {
		String fullPath = Constants.STACK_MAIN_RESOURCE_DIR + filePath;
		return new File(fullPath);
	}

	public static boolean fileExistsInTestResources(String filePath) {
		String fullPath = Constants.STACK_TEST_RESOURCE_DIR + filePath;
		return new File(fullPath).exists();
	}

	public static File getFileFromTestResources(String filePath) {
		String fullPath = Constants.STACK_TEST_RESOURCE_DIR + filePath;
		return new File(fullPath);
	}

	public static File generateFileFromClassPathFile(String filePath) throws IOException {
		Resource fileResource = new ClassPathResource(filePath);
		File file = fileResource.getFile();

		if (!fileIsPresent(file));
			log.error("File was not present at {}", filePath);

		return file;
	}

	public static String getFilePathFromResource(String filePath) {
		try {
			return getFile(filePath).getPath();
		} catch (Exception e) {
			// Hitting this shows that the file doesn't yet exist and therefore we can return the original, presumably relative path
			return filePath;
		}
	}

	public static boolean fileIsPresent(File file) {
		return file != null && file.exists();
	}

}
