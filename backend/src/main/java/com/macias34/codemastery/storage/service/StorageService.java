package com.macias34.codemastery.storage.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.macias34.codemastery.exception.StorageException;
import com.macias34.codemastery.storage.entity.StorageFile;

@Service
public class StorageService {

	@Autowired
	@Qualifier("code-mastery")
	private AmazonS3 amazonS3Client;

	@Value("${cloud.do.bucket.name}")
	private String bucketName;

	@Value("${cloud.do.cdn.endpoint}")
	private String cdn_endpoint;


	public StorageFile uploadFile(final String fileName, final String objectName, final MultipartFile file) {

		return uploadFileToS3(fileName, objectName, file, false);
	}

	public StorageFile uploadPublicFile(final String fileName, final String objectName, final MultipartFile file) {
		return uploadFileToS3(fileName, objectName, file, true);
	}

	private StorageFile uploadFileToS3(final String fileName, final String objectName, final MultipartFile file,
			final boolean shouldBePublic) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());
			metadata.setContentType(contentType(file));
			if (shouldBePublic) {
				metadata.setHeader("x-amz-acl", "public-read");
			}

			amazonS3Client.putObject(bucketName, objectName, file.getInputStream(), metadata);

			String src = cdn_endpoint + "/" + objectName;
			return new StorageFile(src, fileName, objectName);

		} catch (IOException ioe) {
			System.out.println(ioe);
			throw new StorageException(ioe.getMessage());
		} catch (AmazonServiceException serviceException) {
			System.out.println(serviceException);
			throw new StorageException(serviceException.getMessage());
		} catch (AmazonClientException clientException) {
			System.out.println(clientException);
			throw new StorageException(clientException.getMessage());
		}
	}

	/**
	 * Deletes file from DigitalOcean Spaces
	 *
	 * @param objectName
	 * @return
	 */
	public String deleteFile(final String objectName) {
		amazonS3Client.deleteObject(bucketName, objectName);
		return "Deleted File: " + objectName;
	}

	/**
	 * Downloads file from DigitalOcean Spaces
	 *
	 * @param keyName
	 * @return ByteArrayOutputStream
	 */
	public ByteArrayOutputStream downloadFile(String keyName) throws IOException {
		S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(bucketName, keyName));

		InputStream is = s3object.getObjectContent();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[4096];
		while ((len = is.read(buffer, 0, buffer.length)) != -1) {
			outputStream.write(buffer, 0, len);
		}

		return outputStream;

	}

	/**
	 * Get all files from DO Spaces
	 *
	 * @return
	 */
	public List<String> listFiles() {

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
				.withEncodingType(null);

		List<String> files = new ArrayList<>();
		ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);

		while (true) {
			List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
			if (objectSummaries.size() < 1) {
				break;
			}

			for (S3ObjectSummary item : objectSummaries) {
				if (!item.getKey().endsWith("/"))
					files.add(item.getKey());
			}

			objects = amazonS3Client.listNextBatchOfObjects(objects);
		}

		return files;
	}

	private String contentType(final MultipartFile file) {

		final String fileName = file.getOriginalFilename();
		return file.getContentType() == null ? fileName.substring(fileName.lastIndexOf(".") + 1)
				: file.getContentType();
	}

	/**
	 * Generate a presigned URL for a file stored in DigitalOcean Spaces.
	 *
	 * @param objectName          The name of the object (file) in the bucket.
	 * @param expirationInMinutes The expiration time for the presigned URL in
	 *                            minutes.
	 * @return The presigned URL.
	 */
	public URL generatePresignedUrl(String objectName, int expirationInMinutes) {
		try {
			// Calculate the expiration date for the presigned URL
			Date expiration = new Date(System.currentTimeMillis() + (expirationInMinutes * 60 * 1000));

			// Create a request to generate the presigned URL
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
					objectName)
					.withMethod(HttpMethod.GET)
					.withExpiration(expiration);

			// Optional: Customize the response headers for the presigned URL (e.g., to set
			// Content-Disposition)
			ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides()
					.withContentDisposition("inline");
			generatePresignedUrlRequest.setResponseHeaders(responseHeaders);

			// Generate the presigned URL
			URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

			return presignedUrl;
		} catch (Exception e) {
			throw new StorageException("Error generating presigned URL: " + e.getMessage());
		}
	}

}