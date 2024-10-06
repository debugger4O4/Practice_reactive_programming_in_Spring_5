package ru.study.chapter_01.spring_futures;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.study.chapter_01.commons.ExamplesCollection;

@RestController
@RequestMapping("api/v2/resource/b")
public class AsyncServiceTwo {

    @GetMapping
    public ExamplesCollection process() throws InterruptedException {
        Thread.sleep(1000);

        ExamplesCollection ec = new ExamplesCollection();
        ec.setValue("test");

        return ec;
    }
}
