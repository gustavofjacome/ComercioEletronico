package modelo;

public record Categoria(int id, String descricao) {
    @Override
    public String toString() {
        return String.format("ID: %-3d | Descrição: %s", id, descricao);
    }
}
