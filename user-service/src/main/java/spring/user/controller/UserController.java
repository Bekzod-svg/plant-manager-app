package spring.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.user.entity.User;
import spring.user.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user-service")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers(HttpServletRequest request) {
        return userService.getAllUsers();
    }

    @GetMapping("/actual-user")
    public User getActualUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }
        return (User) session.getAttribute("user");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, HttpServletRequest request) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails, HttpServletRequest request) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request) {
        User foundUser = userService.findFirstByUsername(username);
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            log.error(foundUser.getUsername());
            HttpSession session = request.getSession();
            session.setAttribute("user", foundUser);
            return ResponseEntity.ok(foundUser);
        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logout successful");
    }
    @PostMapping("/is-logged-in")
    public ResponseEntity<Boolean> isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return ResponseEntity.ok(session != null && session.getAttribute("user") != null);
    }
}