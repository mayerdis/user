package org.mick.user.service;

import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.mick.user.Entity.User;
import org.mick.user.configuration.JsonWebTokenUtils;
import org.mick.user.configuration.exception.ApplicationException;
import org.mick.user.configuration.exception.EntityNotFoundException;
import org.mick.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class UserService {

    @Value("${validation.emailRegExp}")
    public String regExpEmail;

    final UserRepository userRepository;

    final JsonWebTokenUtils jsonWebTokenUtils;


    @Autowired
    public UserService(UserRepository userRepository, JsonWebTokenUtils jsonWebTokenUtils) {
        this.userRepository = userRepository;
        this.jsonWebTokenUtils = jsonWebTokenUtils;
    }

    @SneakyThrows
    public User saveUser(User userSignUp) {
        Pattern pattern = Pattern.compile(regExpEmail);
        Matcher matcher = pattern.matcher(userSignUp.getEmail());
        if (!matcher.find()) {
            throw new ApplicationException("Correo Invalido");
        }
        if (userRepository.existsUserByEmail(userSignUp.getEmail())){
            throw new ApplicationException("Correo ya Registrado");
        }
        userSignUp.setIsActive(true);
        userSignUp.setToken(jsonWebTokenUtils.generateToken(userSignUp));
        userSignUp.setLoginFailed(0);
        return userRepository.save(userSignUp);
    }
    @SneakyThrows
    public Optional<User> findByEmail(String email) {
        User result = userRepository.findUserByEmail(email);
        if (result!=null){
            return Optional.of(result);
        } else {
            throw new EntityNotFoundException("Usuario no Encontrado");
        }
    }

    @SneakyThrows
    public User updateUser(String id, String password, String newPassword) {
        User user = null;
        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isEmpty()){
            throw new EntityNotFoundException("Usuario No encontradado");
        } else {
            user = userOp.get();
        }
        if (user.getPassword().equals(password)) {
            if (!user.getPassword().equals(newPassword)){
                user.setPassword(newPassword);
                user.setToken(jsonWebTokenUtils.generateToken(user));
            } else {
                throw new ApplicationException("La nueva contraseña no puede ser igual al anterior");
            }
        } else {
            throw new ApplicationException("La contraseña no incorrecta");
        }

        return userRepository.save(user);
    }

    public User login(String email, String password) throws ApplicationException, EntityNotFoundException {
        User userLogin = userRepository.findUserByEmail(email);
        if (userLogin!=null){
            if (userLogin.getIsActive()){
                if (password.equals(userLogin.getPassword())) {
                    userLogin.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
                    userLogin.setLoginFailed(0);
                    return userRepository.save(userLogin);
                } else {
                    Integer item = userLogin.getLoginFailed();
                    userLogin.setLoginFailed(++item);
                    userRepository.save(userLogin);
                    throw new ApplicationException("Contraseña incorrecta");
                }
            } else {
                throw new ApplicationException("{usuario.inactivo}");
            }
        } else {
            throw new EntityNotFoundException("Usuario no Encontrado");
        }
    }

}
