package com.project.rag_chatgpt.controller;



import com.project.rag_chatgpt.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping("/file")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Process file and generate embeddings
        embeddingService.processFile(file);
        return "File uploaded and processed successfully.";
    }

    @PostMapping("/text")
    public String uploadText(@RequestParam("text") String text) {
        // Process text and generate embeddings
        embeddingService.processText(text);
        return "Text uploaded and processed successfully.";
    }
}
