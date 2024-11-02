package ru.study.chapter_03._05_rxjava_reactivestreams;


@Service
public class RxFileService implements FileService {
      /*
      Принимает Publisher как основной тип для взаимодействия между компонентами
       */
    @Override
    public void writeTo(
            String file,
            Publisher<String> content
    ) {

        AsyncFileSubscriber rxSubscriber =
                new AsyncFileSubscriber(file);
          /*
          Подписка на контент
          RxReactiveStreams прео бразует экземпляр Subscriber из RxJava
           */
        content.subscribe(RxReactiveStreams.toSubscriber(rxSubscriber));
    }
}
