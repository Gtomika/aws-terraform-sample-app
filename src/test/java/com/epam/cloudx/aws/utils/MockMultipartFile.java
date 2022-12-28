package com.epam.cloudx.aws.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MockMultipartFile implements MultipartFile {

    private final String contentType;

    public MockMultipartFile(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[]{1,2});
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        dest.createNewFile();
    }
}
