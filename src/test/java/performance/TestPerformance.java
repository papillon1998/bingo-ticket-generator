package performance;


import com.mrq.bingo.generator.TicketGenerator;
import com.mrq.bingo.model.Strip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestPerformance {

    @Test
    @DisplayName("Performance Test - Generate 10,000 Strips")
    void shouldGenerateTenThousandStripsInReasonableTime() {
        TicketGenerator generator = new TicketGenerator();

        // Measure the time taken to generate 10,000 strips
        long startTime = System.nanoTime();
        List<Strip> strips = generator.generateStrips(10_000);
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds
        long durationInMillis = (endTime - startTime) / 1_000_000;

        // Output the result
        System.out.println("Time taken to generate 10,000 strips: " + durationInMillis + " ms");

        // Assertions to ensure correctness
        assertThat(strips).hasSize(10_000);
        strips.forEach(strip -> assertThat(strip.getTickets()).hasSize(Strip.TICKETS_PER_STRIP));

        // Performance expectation: should complete within a reasonable time frame (e.g., under 2 seconds)
        assertThat(durationInMillis).isLessThan(1000); // Adjust this limit based on your performance benchmarks
    }
}
