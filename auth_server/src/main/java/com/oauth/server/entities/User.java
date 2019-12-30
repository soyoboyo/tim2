package com.oauth.server.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "\"user\"")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Integer id;

	@NonNull
	@Column(unique = true)
	private String email;

	@NonNull
	private String password;

	@NonNull
	@OneToOne(cascade = CascadeType.MERGE,
			fetch = FetchType.EAGER)
	@JoinColumn(name = "users_principal")
	private Principal principal;

}
