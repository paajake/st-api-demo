package dev.paajake.url_shortener.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.paajake.url_shortener.url.Url;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)

	@Email(message = "A valid email must be entered")
	@NotBlank(message = "Unique Email is Mandatory")
	private String email;

	@NotBlank(message = "A unique username is mandatory")
	@Column(unique = true, nullable = false)
	private String username;

	@NotBlank(message = "A password is mandatory")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String imageUrl;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "user")
	Set<Url> urls;
}
