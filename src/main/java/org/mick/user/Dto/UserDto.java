package org.mick.user.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @ApiModelProperty(notes = "Identificador Unico",name="id", position = 1)
    private String id;

    @ApiModelProperty(notes = "Fecha de Creacion",name="created", position = 4)
    private String created;

    @ApiModelProperty(notes = "Fecha Ultima Actualizacion",name="modified",  position = 5)
    private String modified;

    @ApiModelProperty(notes = "Fecha Ultimo Login con Exito",name="lastLogin",  position = 3)
    private String lastLogin;

    @ApiModelProperty(notes = "Token Generado",name="token", position = 2)
    private String token;

    @ApiModelProperty(notes = "Estado",name="isActive",  position = 6)
    private Boolean isActive;

    public UserDto() {
    }

    public UserDto(String id, String created, String modified, String lastLogin, String token, Boolean isActive) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

}
