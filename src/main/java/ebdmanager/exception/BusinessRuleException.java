package ebdmanager.exception;

/**
 * Exceção lançada quando ocorre uma violação de regra de negócio.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
