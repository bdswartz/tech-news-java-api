package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository repository;

    @Autowired
    VoteRepository voteRepository;
// combine the route ("/api/users") and the type of HTTP method used (GET) to provide the method with a unique endpoint
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
//        create the return variable (ie list of users)
        List<User> userList = repository.findAll();
        for (User u : userList) {
//            for every user get a list of Posts by calling the getPosts method in the Post model object
            List<Post> postList = u.getPosts();
            for (Post p : postList) {
//                for every post get the number of votes for each post ID
                p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            }
        }
        return userList;
    }

    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        User returnUser = repository.getById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }

        return returnUser;
    }

    @PostMapping("/api/users")
//    map the body of this request to a transfer object, then deserialize the body onto a Java object for easier use
    public User addUser(@RequestBody User user) {
        // Encrypt password
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
//        save the
        repository.save(user);
        return user;
    }

    @PutMapping("/api/users/{id}")
//    path variable grabs the user ID from the path (above) and assigns it to int id variable
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User tempUser = repository.getById(id);

        if (!tempUser.equals(null)) {
            user.setId(tempUser.getId());
            repository.save(user);
        }
        return user;
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }
}
