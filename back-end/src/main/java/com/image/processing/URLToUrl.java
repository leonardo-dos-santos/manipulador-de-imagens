package com.image.processing;

import java.net.MalformedURLException;
import java.net.URL;

public final class URLToUrl {
    private URLToUrl() {}

    public static URL of(String spec) throws MalformedURLException {
        return new URL(spec);
    }
}
