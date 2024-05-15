package com.project.rag_chatgpt.model;



import java.util.Arrays;
import java.util.List;

public class Document {

    private String text;
    private float[] embedding;

    public Document() {
        // Default constructor
    }

    public Document(String text, float[] embedding) {
        this.text = text;
        this.embedding = embedding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "Document{" +
                "text='" + text + '\'' +
                ", embedding=" + Arrays.toString(embedding) +
                '}';
    }
}
