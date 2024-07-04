package spring.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.user.entity.Event;
import spring.user.entity.EventType;
import spring.user.entity.User;
import spring.user.service.UserService;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getAllUsers());
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
        userService.assignEventToUser(createdUser, new Event(EventType.CREATED, new Date()));
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) {
        User updatedUser = userService.updateUser(id, user);
        userService.assignEventToUser(updatedUser, new Event(EventType.UPDATED, new Date()));
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
            userService.assignEventToUser(foundUser, new Event(EventType.LOG_IN, new Date()));
            return ResponseEntity.ok(foundUser);
        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            userService.assignEventToUser((User) session.getAttribute("user"), new Event(EventType.LOG_OUT, new Date()));
            session.invalidate();
        }
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/event")
    public ResponseEntity<Boolean> assignEventToUser(@RequestBody Event event, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }
        return ResponseEntity.ok(userService.assignEventToUser((User) session.getAttribute("user"), event));
    }

    @PostMapping("/is-logged-in")
    public ResponseEntity<Boolean> isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return ResponseEntity.ok(session != null && session.getAttribute("user") != null);
    }
}