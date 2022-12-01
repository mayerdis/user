package org.mick.user.controller;

import io.swagger.annotations.*;
import org.mick.user.Dto.ErrorMessage;
import org.mick.user.Dto.UserDto;
import org.mick.user.Entity.User;
import org.mick.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/")
@Api(value = "UserController"
        , tags = "users"
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Crea un nuevo Usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Creado", response = UserDto.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping(value = "api/v1/user",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUser(@Valid @RequestBody User newUser, @ApiIgnore Errors error) {
        if (error.hasErrors()) {
            return new ResponseEntity<>(new ErrorMessage("EX02",Objects.requireNonNull(error.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);
        } else {
            Optional<UserDto> rest = Optional.ofNullable(userService.saveUser(newUser)).map(new UsertoUserDto());
            HttpHeaders h = new HttpHeaders();
            h.add(HttpHeaders.AUTHORIZATION, rest.get().getToken());
            return new ResponseEntity<>(rest, h, HttpStatus.CREATED);
        }
    }

    @ApiOperation(value = "Busca a un usuario por su correo electronico")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping(value = "api/v1/getUserByEmail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@NotEmpty @RequestParam String email) {
        Optional<UserDto> userDto = userService.findByEmail(email).map(new UsertoUserDto());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Modifica la contraseña y genera un nuevo tokem JWT")
    @PutMapping(value = "api/v1/updateUserPassword",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUpdateUserPassWord(@NotEmpty @RequestParam String id
            , @NotEmpty @RequestParam String password
            , @NotEmpty @RequestParam String newPassword) {
        Optional<UserDto> userDto = Optional.ofNullable(userService.updateUser(id, password, newPassword)).map(new UsertoUserDto());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Endpoint de login de usuario")
    @PostMapping(value = "api/v1/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@NotEmpty @RequestParam String username
            , @NotEmpty @RequestParam String password) throws Exception {
        User restUser = userService.login(username, password);
        Optional<UserDto> rest = Optional.ofNullable(restUser).map(new UsertoUserDto());
        HttpHeaders h = new HttpHeaders();
        h.add(HttpHeaders.AUTHORIZATION, rest.get().getToken());
        return new ResponseEntity<>(rest,h, HttpStatus.OK);
    }

    private class UsertoUserDto implements Function<User, UserDto> {

        public UserDto apply(User u) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return new UserDto(
                    u.getId(),
                    u.getCreated() == null ? "" : sdf.format(u.getCreated()),
                    u.getModified() == null ? "" : sdf.format(u.getModified()),
                    u.getLastLogin() == null ? "" : sdf.format(u.getLastLogin()),
                    u.getToken(),
                    u.getIsActive()
            );
        }
    }
}
