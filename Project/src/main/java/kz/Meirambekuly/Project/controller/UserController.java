package kz.Meirambekuly.Project.controller;

import io.jsonwebtoken.Claims;
import kz.Meirambekuly.Project.PasswordEncoder.PasswordEncoder;
import kz.Meirambekuly.Project.domain.Role;
import kz.Meirambekuly.Project.domain.User;
import kz.Meirambekuly.Project.filter.JwtTokenHelper;
import kz.Meirambekuly.Project.repository.RoleRepository;
import kz.Meirambekuly.Project.repository.UserRepository;
import kz.Meirambekuly.Project.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user){
        User userObj = userRepository.findByUsername(user.getUsername());
        if (userObj == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.getById(1L));
            user.setPassword(PasswordEncoder.hashcode(user.getPassword()));
            user.setRoles(roles);
            userRepository.save(user);
            return new ResponseEntity<>("Successfully_registered", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Username_already_exist", HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody User user) throws IOException, SQLException {
        User userObj = userRepository.getByUsernameAndPassword(user.getUsername(), PasswordEncoder.hashcode(user.getPassword()));
        if (nonNull(userObj)) {
            for (Role role : userObj.getRoles()) {
                Long id = role.getId();
                String roleName = role.getName();
                String access_token = ("Bearer " + JwtTokenHelper.generateJwt(userObj.getUsername(), id, roleName));
                return new ResponseEntity<>(access_token, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Bad_Credentials", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/user/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestParam("newPassword") String newPassword,
                                                 HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        Claims body = JwtTokenHelper.decodeJwt(token);
        String tempUsername = body.getSubject();
        User userObj = userRepository.findByUsername(tempUsername);
        if (!userObj.getPassword().equals(PasswordEncoder.hashcode(newPassword))) {
            userObj.setPassword(PasswordEncoder.hashcode(newPassword));
            userRepository.save(userObj);
            return new ResponseEntity<>("The user's password changed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("The user's password is the same as the previous one",
                HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(roleRepository.save(role));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<User>addRoleToUser(@RequestBody RoleToUserForm form){
        User user = userRepository.findByUsername(form.getUsername());
        Role role = roleRepository.findByName(form.getRoleName());
        user.getRoles().add(role);
        return ResponseEntity.ok().body(userRepository.findByUsername(form.getUsername()));
    }

    @DeleteMapping("/role/deleteFromUser")
    public ResponseEntity<User>deleteRoleFromUser(@RequestBody RoleToUserForm form){
        User user = userRepository.findByUsername(form.getUsername());
        Role role = roleRepository.findByName(form.getRoleName());
        user.getRoles().remove(role);
        return ResponseEntity.ok().body(userRepository.findByUsername(form.getUsername()));
    }

}
@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
