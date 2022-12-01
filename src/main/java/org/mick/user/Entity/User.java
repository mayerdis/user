package org.mick.user.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.mick.user.Dto.UserDto;
import org.mick.user.Validation.ValidPassword;
import org.mick.user.controller.UserController;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(generator = "uuidUsers")
    @GenericGenerator(name = "uuidUsers", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "email", length = 120, unique = true, nullable = false)
    @ApiModelProperty(notes = "Correo Electronico",name="email",required=true,value="mayerdis@gmail.com", position = 2)
    private String email;

    @NotNull(message = "Nombre del Usuario es Requerido")
    @Column(name = "name", nullable = false, length = 60)
    @ApiModelProperty(notes = "Nombre del Usuario",name="name",required=true,value="Michael Ayerdis", position = 1)
    private String name;

    @NotNull(message = "La Contraseña es Requerida.")
    @NotEmpty(message = "La Contraseña es Requerida")
    @ValidPassword(message = "Contraseña invalida")
    @Column(name = "password", nullable = false, length = 16)
    @ApiModelProperty(notes = "Contraseña del Usuario",name="password",required=true,value="Hunt$?dfgd545er2", position = 3)
    private String password;

    @JsonIgnore
    @CreationTimestamp
    @PastOrPresent
    @Column(name = "created")
    private Timestamp created;

    @JsonIgnore
    @UpdateTimestamp
    @PastOrPresent
    @Column(name = "modified")
    private Timestamp modified;

    @JsonIgnore
    @UpdateTimestamp
    @PastOrPresent
    @Column(name = "lastLogin")
    private Timestamp lastLogin;

    @JsonIgnore
    @Column(name = "token", length = 513)
    private String token;

    @JsonIgnore
    @Column(name = "isActive")
    private Boolean isActive;

    @JsonIgnore
    @Column(name = "loginFailed")
    private Integer loginFailed;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    List<Phone> phones;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", created=" + created +
                ", lastLogin=" + lastLogin +
                ", token='" + token + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
