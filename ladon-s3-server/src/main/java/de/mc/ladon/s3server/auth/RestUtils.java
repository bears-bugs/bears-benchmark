package de.mc.ladon.s3server.auth;

import com.google.common.base.Strings;
import de.mc.ladon.s3server.common.S3Constants;
import de.mc.ladon.s3server.entities.api.S3CallContext;

import java.util.*;

/**
 * Utilities useful for REST/HTTP S3Service implementations.
 */
public class RestUtils {
    /**
     * The set of request parameters which must be included in the canonical
     * string to sign.
     */
    private static final List<String> SIGNED_PARAMETERS = Arrays.asList("acl", "torrent", "logging", "location", "policy", "requestPayment", "versioning",
            "versions", "versionId", "notification", "uploadId", "uploads", "partNumber", "website",
            "delete", "lifecycle", "tagging", "cors", "restore",
            ResponseHeaderOverrides.RESPONSE_HEADER_CACHE_CONTROL,
            ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_DISPOSITION,
            ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_ENCODING,
            ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_LANGUAGE,
            ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_TYPE,
            ResponseHeaderOverrides.RESPONSE_HEADER_EXPIRES);

    /**
     * Calculate the canonical string for a REST/HTTP request to S3.
     * <p>
     * When expires is non-null, it will be used instead of the Date header.
     */
    public static String makeS3CanonicalString(S3CallContext context, String expires) {
        StringBuilder buf = new StringBuilder();
        buf.append(context.getMethod()).append("\n");

        // Add all interesting headers to a list, then sort them.  "Interesting"
        // is defined as Content-MD5, Content-Type, Date, and x-amz-
        Map<String, String> headersMap = context.getHeader().getFullHeader();
        SortedMap<String, String> interestingHeaders = new TreeMap<>();
        if (headersMap != null && headersMap.size() > 0) {
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key == null) continue;
                String lk = key.toLowerCase(Locale.getDefault());

                // Ignore any headers that are not particularly interesting.
                if (lk.equals("content-type") || lk.equals("content-md5") || lk.equals("date") ||
                        lk.startsWith(S3Constants.X_AMZ_PREFIX)) {
                    interestingHeaders.put(lk, value);
                }
            }
        }

        // Remove default date timestamp if "x-amz-date" is set.
        if (interestingHeaders.containsKey(S3Constants.X_AMZ_DATE)) {
            interestingHeaders.put("date", "");
        }

        // Use the expires value as the timestamp if it is available. This trumps both the default
        // "date" timestamp, and the "x-amz-date" header.
        if (expires != null) {
            interestingHeaders.put("date", expires);
        }

        // These headers require that we still put a new line in after them,
        // even if they don't exist.
        if (!interestingHeaders.containsKey("content-type")) {
            interestingHeaders.put("content-type", "");
        }
        if (!interestingHeaders.containsKey("content-md5")) {
            interestingHeaders.put("content-md5", "");
        }

        // Any parameters that are prefixed with "x-amz-" need to be included
        // in the headers section of the canonical string to sign
        context.getParams().getAllParams().entrySet().stream().filter(parameter -> parameter.getKey().startsWith("x-amz-"))
                .forEach(parameter -> interestingHeaders.put(parameter.getKey(), parameter.getValue()));

        // Add all the interesting headers (i.e.: all that startwith x-amz- ;-))
        for (Map.Entry<String, String> entry : interestingHeaders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith(S3Constants.X_AMZ_PREFIX)) {
                buf.append(key).append(':');
                if (value != null) {
                    buf.append(value);
                }
            } else if (value != null) {
                buf.append(value);
            }
            buf.append("\n");
        }

        // Add all the interesting parameters
        buf.append(context.getUri());
        String[] parameterNames = context.getParams().getAllParams().keySet().toArray(
                new String[context.getParams().getAllParams().size()]);
        Arrays.sort(parameterNames);
        char separator = '?';
        for (String parameterName : parameterNames) {
            // Skip any parameters that aren't part of the canonical signed string
            if (!SIGNED_PARAMETERS.contains(parameterName)) continue;

            buf.append(separator);
            buf.append(parameterName);
            String parameterValue = context.getParams().getAllParams().get(parameterName);
            if (!Strings.isNullOrEmpty(parameterValue)) {
                buf.append("=").append(parameterValue);
            }

            separator = '&';
        }

        return buf.toString();
    }

}