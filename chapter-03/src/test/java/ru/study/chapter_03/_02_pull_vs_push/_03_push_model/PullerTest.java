package ru.study.chapter_03._02_pull_vs_push._03_push_model;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

public class PullerTest {

    @Test
    public void pullTest() throws InterruptedException {
        CountDownLatch l = new CountDownLatch(1);
        Puller puller = new Puller();

        puller.list(10)
                .collect(ArrayList::new, ArrayList::add)
                .subscribe(al -> {
                    MatcherAssert.assertThat(al, Matchers.allOf(
                            Matchers.hasSize(10),
                            Matchers.contains(
                                    Matchers.hasProperty("id", Matchers.equalTo("2")),
                                    Matchers.hasProperty("id", Matchers.equalTo("4")),
                                    Matchers.hasProperty("id", Matchers.equalTo("6")),
                                    Matchers.hasProperty("id", Matchers.equalTo("8")),
                                    Matchers.hasProperty("id", Matchers.equalTo("10")),
                                    Matchers.hasProperty("id", Matchers.equalTo("12")),
                                    Matchers.hasProperty("id", Matchers.equalTo("14")),
                                    Matchers.hasProperty("id", Matchers.equalTo("16")),
                                    Matchers.hasProperty("id", Matchers.equalTo("18")),
                                    Matchers.hasProperty("id", Matchers.equalTo("20"))
                            )
                    ));
                    l.countDown();
                });

        l.await();
    }
}
