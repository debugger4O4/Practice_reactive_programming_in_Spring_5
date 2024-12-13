interface MessagesSource {
    String INPUT = "messages";

    @Input(INPUT) // Очередь, из которой должны извлекаться сообщения.
    SubscribableChannel input();
}