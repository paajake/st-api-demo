package dev.paajake.url_shortener.url;

import dev.paajake.url_shortener.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Data
public class Url {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@URL(message = "A valid url must be entered")
	@NotBlank(message = "A destination full URL is mandatory")
	private String fullUrl;

	@Column(unique = true)
	@Size(min = 2, message = "Number of characters used for short URL path MUST exceed 2.")
	@Size(max = 150, message = "Number of characters used for short URL path can NOT exceed 150.")
	private String shortUrlPath;
	private Long clicks = 0L;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ManyToOne
	private User user;
}
