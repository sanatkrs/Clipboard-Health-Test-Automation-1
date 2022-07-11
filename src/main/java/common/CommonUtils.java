package common;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
public class CommonUtils {

	// Disable instantiation
	private CommonUtils() {}

	public static String getMsgAndCauseFromException(Throwable e) {
		if (e == null)
			return "N/A - Exception was null";

		String msg = e.getMessage();

		if (e.getCause() != null)
			msg += "\n Caused by: " + e.getCause().getMessage();

		return msg;
	}

	public static void failWithMessage(String message) {
		log.error(message);
		Assert.fail(message);
	}

	public static void logTimeToNowIfGreaterThanHalfSec(long start, String loggerFormattableMsg) {
		logTimeToNowIfGreaterThanXSec(0.5, start, loggerFormattableMsg);
	}

	public static void logTimeToNowIfGreaterThanXSec(double seconds, long start, String loggerFormattableMsg) {
		double totalTime = TemporalUtils.getTotalTimeToNowAsDecimalInSeconds(start);

		if (totalTime > seconds)
			log.warn(loggerFormattableMsg, totalTime);
	}

}
