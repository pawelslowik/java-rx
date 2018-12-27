package pl.com.psl.java.rx.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FluxExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxExample.class);

    public static void main(String[] args) throws Exception {
        new FluxExample().handleErrors();
    }

    private void justNumber() {
        Flux.just(0, 1, 2, 3)
                .subscribe(n -> LOGGER.info(String.valueOf(n)));
    }

    private void rangeNumbers() {
        Flux.range(0,3)
                .map(String::valueOf)
                .subscribe(LOGGER::info);
    }

    private void fromArray() {
        Flux.fromArray(new Integer[] {0, 1, 2, 3})
                .map(String::valueOf)
                .subscribe(LOGGER::info);
    }

    private void fromIterable() {
        Flux.fromIterable(Arrays.asList(0, 1, 2, 3))
                .map(String::valueOf)
                .subscribe(LOGGER::info);
    }

    private void fromStream() {
        Flux.fromStream(Stream.of(0, 1, 2, 3))
                .map(String::valueOf)
                .subscribe(LOGGER::info);
    }

    private void interval() throws InterruptedException {
        //interval publishes values in a separate thread pool!
        Flux.interval(Duration.ofSeconds(1))
                .map(String::valueOf)
                .subscribe(LOGGER::info);

        Thread.sleep(4000);
    }

    private void generate() {
        // generates infinite number of values
        Flux<String> generatedNumbers = Flux.generate(() -> 0, (number, sink) -> {
            sink.next(String.valueOf(number));
            return number + 1;
        });

        generatedNumbers
                .take(4)
                .subscribe(LOGGER::info);
    }

    private void flatMap() {
        //transforms every element from original flux into a new publisher and then merges all of them into a single flux
        Flux.range(0, 4)
                .doOnNext(n -> LOGGER.info("Published " + n))
                .flatMap(n -> Flux.just("A", "B").map(l -> l + String.valueOf(n)))
                .subscribe(LOGGER::info);
    }

    private void concatWith() throws InterruptedException {
        Flux.range(0, 4)
                .delayElements(Duration.ofSeconds(1))
                .concatWith(Flux.just(4, 5))
                .map(String::valueOf)
                .subscribe(LOGGER::info);

        Thread.sleep(5000);
    }

    private void mergeWith() throws InterruptedException {
        Flux.range(0, 4)
                .delayElements(Duration.ofSeconds(1))
                .mergeWith(Flux.just(4, 5))
                .map(String::valueOf)
                .subscribe(LOGGER::info);

        Thread.sleep(5000);
    }

    private void zipWith() throws InterruptedException {
        Flux<LocalTime> secondInterval = Flux.interval(Duration.ofSeconds(1))
                .map(tick -> LocalTime.now());

        Flux.range(0, 4)
                .zipWith(secondInterval)
                .map(tuple -> "Value " + tuple.getT1() + " published at " + tuple.getT2())
                .subscribe(LOGGER::info);

        Thread.sleep(4000);
    }

    private void filter() {
        Flux.range(0, 4)
                .take(3)
                .filter(n -> n % 2 == 0)
                .skip(1)
                .map(String::valueOf)
                .subscribe(LOGGER::info);
    }

    private void peekAndHandle() {
        Flux.range(0, 4)
                .map(n -> {
                    if (n == 2) {
                        throw new IllegalArgumentException("Bad element!");
                    }
                    return n;
                })
                .onErrorContinue((e, n) -> LOGGER.warn("Continuing on error '{}' on element {}", e.getMessage(), n))
                .doOnNext(n -> LOGGER.info("Peeking next element:" + n))
                .doOnComplete(() -> LOGGER.info("Peeking completion"))
                .doOnError(e -> LOGGER.error("Peeking error '{}'", e.getMessage()))
                .subscribe(
                        n -> LOGGER.info("Handling next element:" + n),
                        e -> LOGGER.error("Handling error '{}'", e.getMessage()),
                        () -> LOGGER.info("Handling completion")
                );
    }

    private void handleErrors() {
        AtomicInteger errorCounter = new AtomicInteger(0);
        int allowedErrors = 1;

        Flux<Integer> fallback = Flux.range(0, 4)
                .doOnNext(n -> LOGGER.info("Publishing fallback element {}", n))
                .map(n -> {
                   if (n == 2) {
                       throw new IllegalArgumentException("Bad element in fallback!");
                   }
                   return n;
                })
                .doOnError(e -> LOGGER.error("Error: '{}', returning default value: 10", e.getMessage()))
                .onErrorReturn(10);

        Flux.range(0, 4)
                .doOnNext(n -> LOGGER.info("Publishing element {}", n))
                .map(n -> {
                    throw new IllegalArgumentException("Bad element:" + n);
                })
                .doOnError(e -> LOGGER.error("Received error: {}", e.getMessage()))
                .onErrorContinue(p -> errorCounter.get() < allowedErrors,(e, n) -> {
                    errorCounter.addAndGet(1);
                    LOGGER.warn("Continuing on error '{}' on element {}", e.getMessage(), n);
                })
                .onErrorResume(e -> {
                    LOGGER.info("Exceeded allowed errors ({}), using fallback sequence on error", allowedErrors);
                    return fallback;
                })
                .map(String::valueOf)
                .subscribe(n -> LOGGER.info("Subscriber received element {}", n));
    }
}
