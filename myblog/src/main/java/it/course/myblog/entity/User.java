package it.course.myblog.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="USER")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NaturalId
	@Email
	@NotBlank
	@Size(min=6, max=120)
	@Column(length=120, nullable = false)
	private String email;
	
	@NotBlank
	@Size(min=4, max=15)
	@Column(length=15, nullable = false)
	private String username;
	
	@NotBlank
	@Column(length=100, nullable = false)
	@Size(min=5, max=100)
	private String password;
	
	@Column(name="IS_ENABLED", nullable=false, columnDefinition="TINYINT(1) DEFAULT 1")
	private Boolean enabled;
	
	@Column(name="CREATED_AT", 
			updatable=false, insertable=false, 
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@Column(name="UPDATED_AT",
			updatable=true, insertable=false, 
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date updatedAt;
	

}