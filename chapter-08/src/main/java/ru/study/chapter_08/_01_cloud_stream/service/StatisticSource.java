interface StatisticSource {
    String INPUT = "statistic";

    @Input(INPUT)
    SubscribableChannel input();
}