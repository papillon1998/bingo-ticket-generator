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

        long startTime = System.nanoTime();
        List<Strip> strips = generator.generateStrips(10_000);
        long endTime = System.nanoTime();

        long durationInMillis = (endTime - startTime) / 1_000_000;

        System.out.println("Time taken to generate 10,000 strips: " + durationInMillis + " ms");

        assertThat(strips).hasSize(10_000);
        strips.forEach(strip -> assertThat(strip.getTickets()).hasSize(Strip.TICKETS_PER_STRIP));

        assertThat(durationInMillis).isLessThan(1000);
    }
}
