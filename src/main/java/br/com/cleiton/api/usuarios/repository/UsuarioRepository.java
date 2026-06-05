package br.com.cleiton.api.usuarios.repository;

import br.com.cleiton.api.usuarios.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UsuarioRepository {

    HashMap<Long, Usuario> banco = new HashMap<>();
    AtomicLong id = new AtomicLong(1);

    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(id.getAndIncrement());
            banco.put(usuario.getId(), usuario);

            return banco.get(usuario.getId());
        }

        banco.put(usuario.getId(), usuario);

        return banco.get(usuario.getId());
    }

    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(banco.get(id));
    }

    public Optional<Usuario> findByEmail(String email) {

        return banco.values()
                .stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst();
    }

    public void deleteById(Long id) {
        banco.remove(id);
    }

    public boolean existsByEmail(String email) {
        return banco.values()
                .stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .anyMatch(usuario -> true);
    }

}
