package teddie.service;

public record TestCase(
        String name,
        String displayName,
        String input,
        String expectedOutput,
        boolean expectError
) {
}
