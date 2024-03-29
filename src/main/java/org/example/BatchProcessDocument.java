package org.example;


import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.documentai.v1.*;
import com.google.cloud.documentai.v1.DocumentOutputConfig.GcsOutputConfig;
import com.google.cloud.storage.*;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class BatchProcessDocument {
    public static void batchProcessDocument()
            throws IOException, InterruptedException, TimeoutException, ExecutionException {
        // TODO(developer): Replace these variables before running the sample.
        String projectId = "your-project-id";
        String location = "your-project-location"; // Format is "us" or "eu".
        String processerId = "your-processor-id";
        String outputGcsBucketName = "your-gcs-bucket-name";
        String outputGcsPrefix = "PREFIX";
        String inputGcsUri = "gs://your-gcs-bucket/path/to/input/file.pdf";
        batchProcessDocument(
                projectId, location, processerId, inputGcsUri, outputGcsBucketName, outputGcsPrefix);
    }

    public static void batchProcessDocument(
            String projectId,
            String location,
            String processorId,
            String gcsInputUri,
            String gcsOutputBucketName,
            String gcsOutputUriPrefix)
            throws IOException, InterruptedException, TimeoutException, ExecutionException {
        // Initialize client that will be used to send requests. This client only needs
        // to be created
        // once, and can be reused for multiple requests. After completing all of your
        // requests, call
        // the "close" method on the client to safely clean up any remaining background
        // resources.
        String endpoint = String.format("%s-documentai.googleapis.com:443", location);
        DocumentProcessorServiceSettings settings =
                DocumentProcessorServiceSettings.newBuilder().setEndpoint(endpoint).build();
        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
            // The full resource name of the processor, e.g.:
            // projects/project-id/locations/location/processor/processor-id
            // You must create new processors in the Cloud Console first
            String name =
                    String.format("projects/%s/locations/%s/processors/%s", projectId, location, processorId);

            GcsDocument gcsDocument =
                    GcsDocument.newBuilder().setGcsUri(gcsInputUri).setMimeType("application/pdf").build();

            GcsDocuments gcsDocuments = GcsDocuments.newBuilder().addDocuments(gcsDocument).build();

            BatchDocumentsInputConfig inputConfig =
                    BatchDocumentsInputConfig.newBuilder().setGcsDocuments(gcsDocuments).build();

            String fullGcsPath = String.format("gs://%s/%s/", gcsOutputBucketName, gcsOutputUriPrefix);
            GcsOutputConfig gcsOutputConfig = GcsOutputConfig.newBuilder().setGcsUri(fullGcsPath).build();

            DocumentOutputConfig documentOutputConfig =
                    DocumentOutputConfig.newBuilder().setGcsOutputConfig(gcsOutputConfig).build();

            // Configure the batch process request.
            BatchProcessRequest request =
                    BatchProcessRequest.newBuilder()
                            .setName(name)
                            .setInputDocuments(inputConfig)
                            .setDocumentOutputConfig(documentOutputConfig)
                            .build();

            OperationFuture<BatchProcessResponse, BatchProcessMetadata> future =
                    client.batchProcessDocumentsAsync(request);

            // Batch process document using a long-running operation.
            // You can wait for now, or get results later.
            // Note: first request to the service takes longer than subsequent
            // requests.
            System.out.println("Waiting for operation to complete...");
            future.get();

            System.out.println("Document processing complete.");

            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            Bucket bucket = storage.get(gcsOutputBucketName);

            // List all of the files in the Storage bucket.
            Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(gcsOutputUriPrefix + "/"));
            int idx = 0;
            for (Blob blob : blobs.iterateAll()) {
                if (!blob.isDirectory()) {
                    System.out.printf("Fetched file #%d\n", ++idx);
                    // Read the results

                    // Download and store json data in a temp file.
                    File tempFile = File.createTempFile("file", ".json");
                    Blob fileInfo = storage.get(BlobId.of(gcsOutputBucketName, blob.getName()));
                    fileInfo.downloadTo(tempFile.toPath());

                    // Parse json file into Document.
                    FileReader reader = new FileReader(tempFile);
                    Document.Builder builder = Document.newBuilder();
                    JsonFormat.parser().merge(reader, builder);

                    Document document = builder.build();

                    // Get all of the document text as one big string.
                    String text = document.getText();

                    // Read the text recognition output from the processor
                    System.out.println("The document contains the following paragraphs:");
                    Document.Page page1 = document.getPages(0);
                    List<Document.Page.Paragraph> paragraphList = page1.getParagraphsList();
                    for (Document.Page.Paragraph paragraph : paragraphList) {
                        String paragraphText = getText(paragraph.getLayout().getTextAnchor(), text);
                        System.out.printf("Paragraph text:%s\n", paragraphText);
                    }

                    // Form parsing provides additional output about
                    // form-formatted PDFs. You must create a form
                    // processor in the Cloud Console to see full field details.
                    System.out.println("The following form key/value pairs were detected:");

                    for (Document.Page.FormField field : page1.getFormFieldsList()) {
                        String fieldName = getText(field.getFieldName().getTextAnchor(), text);
                        String fieldValue = getText(field.getFieldValue().getTextAnchor(), text);

                        System.out.println("Extracted form fields pair:");
                        System.out.printf("\t(%s, %s))", fieldName, fieldValue);
                    }

                    // Clean up temp file.
                    tempFile.deleteOnExit();
                }
            }
        }
    }

    // Extract shards from the text field
    private static String getText(Document.TextAnchor textAnchor, String text) {
        if (textAnchor.getTextSegmentsList().size() > 0) {
            int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
            int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
            return text.substring(startIdx, endIdx);
        }
        return "[NO TEXT]";
    }
}
