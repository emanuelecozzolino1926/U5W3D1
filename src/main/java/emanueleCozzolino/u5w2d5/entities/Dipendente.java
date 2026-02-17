package emanueleCozzolino.u5w2d5.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emanueleCozzolino.u5w2d5.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"password", "accountNonExpired", "accountNonLocked", "authorities", "credentialsNonExpired", "enabled"})
public class Dipendente implements UserDetails {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private UUID id;

	private String username;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String immagineProfilo;
	@Enumerated(EnumType.STRING)
	private Role role;

	public Dipendente(String username, String nome, String cognome, String email, String password) {
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.role = Role.USER;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	public String getUsername() {
		return this.username;
	}
}
