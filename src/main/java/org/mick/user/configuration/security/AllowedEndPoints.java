package org.mick.user.configuration.security;

import lombok.Getter;
import lombok.Setter;

public enum AllowedEndPoints {
    DB_CONSOLE("/h2-console/**"),
    SWAGGER_RESOURCES("/swagger-resources/**"),
    SWAGGER_UI("/swagger-ui/**"),
    SWAGGER_DOCS("/v2/api-docs"),
    API_SIGN_UP("/api/v1/user"),
    LOGIN ("/api/v1/login"),
    ERROR("/error");

    @Getter
    @Setter
    private String urls;
    AllowedEndPoints(String urls) {
        this.urls = urls;
    }
}
