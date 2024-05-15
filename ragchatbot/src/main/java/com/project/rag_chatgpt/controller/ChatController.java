package com.project.rag_chatgpt.controller;



import com.project.rag_chatgpt.model.Query;
import com.project.rag_chatgpt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/query")
    public String chat(@RequestBody Query query) {
        return chatService.getResponse(query);
    }
}
