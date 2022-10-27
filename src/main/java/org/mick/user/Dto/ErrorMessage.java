package org.mick.user.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {

    @ApiModelProperty(notes = "Codigo",name="code",  position = 1)
    String code;

    @ApiModelProperty(notes = "Mensaje",name="message",  position = 2)
    String message;

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
