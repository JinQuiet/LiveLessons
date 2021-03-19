package utils;

import datamodels.TripResponse;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Define a collector that converts a stream of TripResponses into
 * a Flux that emits the cheapest priced trips(s).
 */
public class CheapestPriceCollector
             implements Collector<TripResponse,
                                  List<TripResponse>,
                                  Flux<TripResponse>> {
    /**
     * The minimum value seen by the collector.
     */
    Double mMin = Double.MAX_VALUE;

    /**
     * A function that creates and returns a new mutable result
     * container that will hold all the TripResponses in the stream.
     *
     * @return a function which returns a new, mutable result container
     */
    @Override
    public Supplier<List<TripResponse>> supplier() {
        return ArrayList::new;
    }

    /**
     * A function that folds a TripResponse into the mutable result
     * container.
     *
     * @return a function which folds a value into a mutable result container
     */
    @Override
    public BiConsumer<List<TripResponse>, TripResponse> accumulator() {
        return (lowestPrices, tripResponse) -> {
            if (tripResponse.getPrice() < mMin) {
                lowestPrices.clear();
                lowestPrices.add(tripResponse);
                mMin = tripResponse.getPrice();
            } else if (tripResponse.getPrice().equals(mMin)) {
                lowestPrices.add(tripResponse);
            }
        };
    }

    /**
     * A function that accepts two partial results and merges them.
     * The combiner function may fold state from one argument into the
     * other and return that, or may return a new result container.
     *
     * @return a function which combines two partial results into a combined
     * result
     */
    @Override
    public BinaryOperator<List<TripResponse>> combiner() {
        return (one, another) -> {
            one.addAll(another);
            return one;
        };
    }

    /**
     * Perform the final transformation from the intermediate
     * accumulation type {@code A} to the final result type {@code R}.
     *
     * @return a function which transforms the intermediate result (a
     * List<TripResponse>) to the final result (a Flux<TripResponse)
     */
    @Override
    public Function<List<TripResponse>, Flux<TripResponse>> finisher() {
        return Flux::fromIterable;
    }

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics}
     * indicating the characteristics of this Collector.
     *
     * @return An emptySet()
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    /**
     * This static factory method creates a new CheapestFlightCollector.
     *
     * @return A new CheapestFlightCollector()
     */
    public static Collector<TripResponse, List<TripResponse>, Flux<TripResponse>>
        toFlux() {
        return new CheapestPriceCollector();
    }
}

