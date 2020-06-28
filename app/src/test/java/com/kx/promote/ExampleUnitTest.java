package com.kx.promote;

import android.util.Log;

import com.kx.promote.utils.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    final static String TAG = "okhttpTest";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}