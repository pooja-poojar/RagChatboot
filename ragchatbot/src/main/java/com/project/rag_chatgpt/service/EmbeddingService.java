package com.project.rag_chatgpt.service;


import com.project.rag_chatgpt.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.param.ConnectParam;
import io.milvus.param.dml.InsertParam;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EmbeddingService {

    private final MilvusClient milvusClient;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EmbeddingService() {
        milvusClient = new MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withHost("localhost")
                        .withPort(19530)
                        .build()
        );
        httpClient = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    public void processFile(MultipartFile file) throws IOException {
        // Read the file and split it into chunks
        String content = new String(file.getBytes());
        processText(content);
    }

    public void processText(String text) {
        // Split text into chunks
        String[] chunks = text.split("\\n\\n");
        for (String chunk : chunks) {
            // Get embedding for each chunk
            float[] embedding = getEmbedding(chunk);
            // Create a Document object
            Document document = new Document(chunk, embedding);
            // Save embedding to Milvus
            saveDocumentToMilvus(document);
        }
    }

    private float[] getEmbedding(String text) {
        // Call your embedding model API to get embeddings
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{\"text\":\"" + text + "\"}");
            Request request = new Request.Builder()
                    .url("http://embedding-model-api/embedding")  // replace with actual embedding model API
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseBody = response.body().string();
                // Parse the response to get the embedding
                return parseEmbedding(responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new float[768]; // Return a default or empty embedding in case of error
        }
    }

    private float[] parseEmbedding(String responseBody) {
        // Implement JSON parsing logic here to extract the embedding from the response
        // Assuming the response is a JSON array of floats
        try {
            return objectMapper.readValue(responseBody, float[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new float[768]; // Return a default or empty embedding in case of error
        }
    }

    private void saveDocumentToMilvus(Document document) {
        // Create a list of field names and values for insertion
        List<InsertParam.Field> fields = new ArrayList<>();
        
        fields.add(new InsertParam.Field("embedding", Collections.singletonList(document.getEmbedding())));
        fields.add(new InsertParam.Field("text", Collections.singletonList(document.getText())));

        // Create an InsertParam object
        InsertParam insertParam = InsertParam.newBuilder()
                .withCollectionName("your_collection_name")
                .withFields(fields)
                .build();

        // Insert the embedding into Milvus
        milvusClient.insert(insertParam);
    }
}
