package com.pkb.common.base;

import static com.pkb.common.base.StreamUtils.pull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class StreamUtilsTest {

    @Test
    public void testPull() {
        // GIVEN
        Stream<Integer> intStream = IntStream.range(0, 25).boxed();
        Iterator<Integer> iterator = intStream.iterator();
        // WHEN
        @NotNull List<Integer> out1 = pull(iterator, 10);
        @NotNull List<Object> out2 = pull(iterator, 10);
        @NotNull List<Integer> out3 = pull(iterator, 10);
        @NotNull List<Integer> out4 = StreamUtils.pull(iterator, 10);

        // THEN
        Assert.assertArrayEquals(IntStream.range(0,10).boxed().toArray(), out1.toArray(new Integer[0]));
        Assert.assertArrayEquals(IntStream.range(10,20).boxed().toArray(), out2.toArray(new Integer[0]));
        Assert.assertArrayEquals(IntStream.range(20,25).boxed().toArray(), out3.toArray(new Integer[0]));
        Assert.assertTrue(out4.isEmpty());
    }

}
