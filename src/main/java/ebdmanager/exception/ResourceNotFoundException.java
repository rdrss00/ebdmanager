package ebdmanager.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado no sistema.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
