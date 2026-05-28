package com.demilingua.backend.controllers;

import com.demilingua.backend.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Map<String, String>>> getFriends(@PathVariable int usuarioId) {
        return ResponseEntity.ok(friendshipService.getFriends(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        return ResponseEntity.ok(friendshipService.addFriend(id1, id2));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> acceptFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        return ResponseEntity.ok(friendshipService.acceptFriend(id1, id2));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteFriend(@RequestParam("usuarioId1") int id1, @RequestParam("usuarioId2") int id2) {
        return ResponseEntity.ok(friendshipService.deleteFriend(id1, id2));
    }
}