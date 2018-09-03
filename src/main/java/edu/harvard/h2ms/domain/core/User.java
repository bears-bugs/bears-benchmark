package edu.harvard.h2ms.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	@JsonIgnore
    @Transient
    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Properties */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column
    private String firstName;

    @Column
    private String middleName;

    @NotNull
    @Column
    private String lastName;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @Column
    private String notificationFrequency;

    @NotNull
    @Column
    private String type;

    @NotNull
    @Column
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "reset_token")
    private String resetToken;



    public User(String firstName, String middleName, String lastName, String email, String password, String type) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        setEmail(email);
        this.type = type;
        setPassword(password);
    }

    public User() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        return authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
    	if(email != null)
    		this.email = email.toLowerCase();
    }

    public String getNotificationFrequency() {
        return notificationFrequency;
    }

    public void setNotificationFrequency(String notificationFrequency) {
        this.notificationFrequency = notificationFrequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // This would be better as a Bean but I couldn't figure out why @Autowired
        // wasn't doing the right thing in an entity.
        if (password != null)
        	this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean value) {
    	this.enabled = value;
    }

    public Date getLastLogin() {
    	return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
    	this.lastLogin = lastLogin;
    }

    public Date getCreatedOn() {
    	return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
    	this.createdOn = createdOn;
    }

    public String getResetToken() {
    	return resetToken;
    }

    public void setResetToken(String resetToken) {
    	this.resetToken = resetToken;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [id=")
                .append(id)
                .append(", firstName=")
                .append(firstName)
                .append(", middleName=")
                .append(middleName)
                .append(", lastName=")
                .append(lastName)
                .append(", email=")
                .append(email)
                .append(", type")
                .append(type)
                .append(", notificationFrequency=")
                .append(notificationFrequency);
        return builder.toString();
    }
}
