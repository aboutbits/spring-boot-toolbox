package it.aboutbits.springboot.toolbox.exception;

/**
 * Use this to create a class or enum that defines the exception messages.
 * This way, the messages can be referenced by a constant rather than using plain strings.
 * <p>
 * Example:
 * {@snippet :
 *
 * @Getter
 * @Accessors(fluent = true)
 * @RequiredArgsConstructor enum MyExceptionMessages implements ExceptionMessageDefinition {
 * SHARED_ERROR_GENERAL("shared.error.general");
 * <p>
 * private final String code;
 * }
 *}
 */
public interface ExceptionMessageDefinition {
    String code();
}
