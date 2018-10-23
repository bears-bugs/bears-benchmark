/*
 * Copyright 2018 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.Serializer;
import com.milaboratory.primitivio.annotations.Serializable;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Calculates file checksum + info based on: its' name content = first N + middle N + last N + bytes + it's length
 * modification date.
 *
 * If descriptor has neither content checksum nor modification date, the files always supposed to be changed.
 */
@Serializable(by = LightFileDescriptor.IO.class)      // serialize using binary format by default
@JsonAutoDetect(                                      // enable json for output purposes
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public class LightFileDescriptor {
    private static int MD5_LENGTH = 16;
    private static int DEFAULT_CHUNK = 1024;
    public final String name;
    public final byte[] checksum;
    public final Long lastModified;

    @JsonCreator
    private LightFileDescriptor(@JsonProperty("name") String name,
                                @JsonProperty("checksum") byte[] checksum,
                                @JsonProperty("lastModified") Long lastModified) {
        this.name = name;
        this.checksum = checksum;
        this.lastModified = lastModified;
    }

    private LightFileDescriptor(String name) {
        this(name, null, null);
    }

    public boolean isAlwaysModified() {
        return checksum == null && lastModified == null;
    }

    public boolean hasChecksum() {
        return checksum != null;
    }

    public boolean hasModificationDate() {
        return lastModified != null;
    }

    /**
     * Returns true if file modified
     */
    public boolean checkModified(LightFileDescriptor other) {
        if (isAlwaysModified() || other.isAlwaysModified())
            return true;

        if (!name.equals(other.name))
            return true;

        if (!(hasChecksum() && other.hasChecksum()) && !(hasModificationDate() && other.hasModificationDate()))
            // No intersecting characteristics, cant compare => file is modified
            return true;

        if (hasChecksum() && other.hasChecksum() && !Arrays.equals(checksum, other.checksum))
            return true;

        if (hasModificationDate() && other.hasModificationDate() && !lastModified.equals(other.lastModified))
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "LightFileDescriptor{" +
                "name='" + name + '\'' +
                ", checksum=" + Arrays.toString(checksum) +
                ", lastModified=" + lastModified +
                '}';
    }

    public static LightFileDescriptor calculate(Path file) {
        return calculate(file, true, true);
    }

    public static LightFileDescriptor calculate(Path file, boolean includeContentChecksum, boolean includeModificationDate) {
        return calculate(file, includeContentChecksum, includeModificationDate, DEFAULT_CHUNK);
    }

    public static LightFileDescriptor calculate(Path file, boolean includeContentChecksum, boolean includeModificationDate, int chunk) {
        String name = file.getFileName().toString();
        try {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

            byte[] checksum = null;
            if (includeContentChecksum) {
                if (attrs.isOther() || attrs.isDirectory())
                    return new LightFileDescriptor(name);
                MessageDigest md = MessageDigest.getInstance("MD5");

                // Adding file size to MD5 sum
                md.update(ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(attrs.size()).array());

                // Adding actual content checksum
                try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
                    if (attrs.size() < chunk * 3) {
                        ByteBuffer buff = ByteBuffer.allocate((int) attrs.size());
                        IOUtils.read(channel, buff);
                        buff.flip();
                        md.update(buff);
                    } else {
                        ByteBuffer buff = ByteBuffer.allocate(chunk);

                        // Reading first chunk
                        channel.position(0);
                        IOUtils.read(channel, buff);
                        buff.flip();
                        md.update(buff);

                        // Reading middle chunk
                        buff.clear();
                        channel.position((attrs.size() + chunk) / 2);
                        IOUtils.read(channel, buff);
                        buff.flip();
                        md.update(buff);

                        // Reading last chunk
                        buff.clear();
                        channel.position(attrs.size() - chunk);
                        IOUtils.read(channel, buff);
                        buff.flip();
                        md.update(buff);
                    }
                }

                checksum = md.digest();
            }

            Long lastModified = includeModificationDate ? attrs.lastModifiedTime().toMillis() : null;

            return new LightFileDescriptor(name, checksum, lastModified);
        } catch (NoSuchAlgorithmException | IOException e) {
            return new LightFileDescriptor(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LightFileDescriptor)) return false;
        LightFileDescriptor that = (LightFileDescriptor) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(checksum, that.checksum) &&
                Objects.equals(lastModified, that.lastModified);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, lastModified);
        result = 31 * result + Arrays.hashCode(checksum);
        return result;
    }

    /**
     * A string representation of this.
     */
    public String toBase64() {
        return name
                + (checksum == null ? "null" : DatatypeConverter.printBase64Binary(checksum))
                + (lastModified == null ? "null" : lastModified);
    }


    public static final class IO implements Serializer<LightFileDescriptor> {
        @Override
        public void write(PrimitivO output, LightFileDescriptor object) {
            output.writeUTF(object.name);
            output.writeObject(object.checksum);
            output.writeObject(object.lastModified);
        }

        @Override
        public LightFileDescriptor read(PrimitivI input) {
            String name = input.readUTF();
            byte[] checksum = input.readObject(byte[].class);
            Long lastModified = input.readObject(Long.class);
            return new LightFileDescriptor(name, checksum, lastModified);
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }
}
