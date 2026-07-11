package exception;

public class VendaItemNaoEncontradoException extends RuntimeException {
    public VendaItemNaoEncontradoException(String message) {
        super(message);
    }
}
