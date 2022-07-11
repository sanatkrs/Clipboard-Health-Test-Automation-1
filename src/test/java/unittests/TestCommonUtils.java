package unittests;

import common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Slf4j
public class TestCommonUtils {

    @Test
    public void testGetMessageFromException_null() {
        Throwable e = null;
        String msg = CommonUtils.getMsgAndCauseFromException(e);

        Assert.assertEquals(msg, "N/A - Exception was null");
    }

    @Test
    public void testGetMessageFromException_with_cause() {
        Throwable e = new IOException("IOEception Msg").initCause(new IOException("IO Cause"));
        String msg = CommonUtils.getMsgAndCauseFromException(e);

        Assert.assertEquals(msg, "IOEception Msg\n Caused by: IO Cause");
    }

    @Test
    public void testGetMessageFromException() {
        Throwable e = new IOException("IOEception Msg");
        String msg = CommonUtils.getMsgAndCauseFromException(e);

        Assert.assertEquals(msg, "IOEception Msg");
    }

}
