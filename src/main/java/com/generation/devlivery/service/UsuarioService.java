package com.generation.devlivery.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.generation.devlivery.model.Usuario;
import com.generation.devlivery.model.UsuarioLogin;
import com.generation.devlivery.repository.UsuarioRepository;
import com.generation.devlivery.security.JwtService;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
			return Optional.empty();
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(usuarioRepository.save(usuario));

	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = usuarioRepository.findByEmail(usuario.getEmail());
			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			return Optional.ofNullable(usuarioRepository.save(usuario));

		}
		return Optional.empty();

	}

	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(),
				usuarioLogin.get().getSenha());

		Authentication authentication = authenticationManager.authenticate(credenciais);

		if (authentication.isAuthenticated()) {

			Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());

			if (usuario.isPresent()) {

				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setEmail(usuario.get().getEmail());
				usuarioLogin.get().setEndereco(usuario.get().getEndereco());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getEmail()));
				usuarioLogin.get().setSenha("");

				return usuarioLogin;

			}

		}

		return Optional.empty();

	}
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	}

	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
	
	public Usuario salvarUsuario(Usuario usuario) {

	    return usuarioRepository.save(usuario);
	}

}