# Bingo 90 Ticket Generator

A Java application that generates valid Bingo 90 tickets following standard UK rules. Each strip consists of 6 tickets containing all numbers from 1 to 90 distributed according to official regulations.


## Pre-requisites

- Java 21
- Maven

## Running the application

1. Clone the sources code
```bash
git clone [repository-url]
```

2. In the application folder, compile the code:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn exec:java -Dexec.mainClass=com.mrq.bingo.Runner -Dexec.args="<noOfStrips>"
```
Replace `<noOfStrips>` with the number of strips you want to generate (e.g., "1")

## Testing the application

All tests can be found in the `BingoStripGeneratorTest` class. They can be run using:
```bash
mvn test
```

The tests verify:
- Correct number of tickets per strip
- Valid number distribution in rows and columns
- Proper number ranges per column
- Ascending order within columns
- No duplicate numbers across the strip
- Performance requirements

## Further Information

This implementation ensures:
- All numbers from 1-90 appear exactly once in a strip
- Each ticket row contains exactly 5 numbers
- Each column contains 1-3 numbers
- Column ranges are strictly enforced
- Numbers are sorted within columns
- Efficient generation even for large batch requests
- 100 % test coverage

## Output Format

The generated tickets are displayed in a clear, formatted output showing:
- Column ranges (1-9, 10-19, etc.)
- Numbers in their correct positions
- Empty cells marked appropriately
- Color-coded output for better readability

## Performance

The generator is optimized to handle bulk generation efficiently, meeting the requirement of generating 10,000 strips in less than 1 second on standard hardware.