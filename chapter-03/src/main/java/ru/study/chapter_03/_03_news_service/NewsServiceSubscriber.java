package ru.study.chapter_03._03_news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import ru.study.chapter_03._03_news_service.dto.NewsLetter;

import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Требования Reactive Streams в действии
 */
public class NewsServiceSubscriber implements Subscriber<NewsLetter> {

    final Queue<NewsLetter> mailbox = new ConcurrentLinkedQueue<>();
    final AtomicInteger remaining = new AtomicInteger();

    // Количество новостей, которое пользователь готов принять единовременно или в ближайший год
    final int take;

    Subscription subscription;

    public NewsServiceSubscriber(int take) {
        this.take = take;
        this.remaining.set(take);
    }

    public void onSubscribe(Subscription s) {
        if (subscription == null) {

            // Сохранение полученного экземпляра
            subscription = s;

            // Запрос на сервер с количеством сообщений, которое готов принять пользователь
            subscription.request(take);
        } else {
            s.cancel();
        }
    }

    public void onNext(NewsLetter newsLetter) {
        Objects.requireNonNull(newsLetter);

        // Поместить новое сообщение в очередь
        mailbox.offer(newsLetter);
    }

    // Вызывается сразу после отмены подписки
    public void onError(Throwable t) {
        Objects.requireNonNull(t);

        if (t instanceof ResubscribableErrorLetter) {
            subscription = null;
            ((ResubscribableErrorLetter) t).resubscribe(this);
        }
    }

    // Вызывается сразу после отмены подписки
    public void onComplete() {
        subscription = null;
    }

    /*
     Имитация действий пользователя, таких как открытие или закрытие почтового ящика, проверка новых сообщений,
     извлечение писем и маркировка их как прочитанных или просто закрытие почтового ящика, если нет новых сообщений
     Optional для того, чтобы показать, что почтовый ящик может быть пустым
     */
    public Optional<NewsLetter> eventuallyReadDigest() {

        // Получить последние непрочитанные новости
        NewsLetter letter = mailbox.poll();
        if (letter != null) {
            // decrementAndGet() - уменьшение счетчика непрочитанных новостей, запрошенных и полученных от службы
            if (remaining.decrementAndGet() == 0) {
                subscription.request(take);
                remaining.set(take);
            }

            /*
             Если получены все запрошенные новости, то дополнительно посылаем запрос на получение новой порции данных
             и переустанавливаем счетчик новых сообщений
             */
            return Optional.of(letter);
        }

        // Непрочитанных новостей нет
        return Optional.empty();
    }
}