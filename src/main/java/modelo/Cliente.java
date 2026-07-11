package modelo;

public record Cliente(int id, String nome, String email, String fone) {
    @Override
    public String toString() {
        return String.format("ID: %-3d | Nome: %-20s | E-mail: %-20s | Telefone: %s",
                id, nome, email, fone);
    }
}
