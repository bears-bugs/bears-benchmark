/*
 * Copyright 2014-2018 Web Firm Framework
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
 * @author WFF
 */
package com.webfirmframework.wffweb.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class for clone operations.
 *
 * @author WFF
 * @since 1.0.0
 */
public final class CloneUtil {

    private static final Logger LOGGER = Logger
            .getLogger(CloneUtil.class.getName());

    private CloneUtil() {
        throw new AssertionError();
    }

    /**
     * clones only if {@code objects} contains the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it contains {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} contains the given
     *         {@code objectToBeClonned}, otherwise returns the same object. If
     *         {@code objectToBeClonned} is null then returns {@code null}. If
     *         {@code objects} is null then {@code objectToBeClonned} will be
     *         returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfContains(final T objectToBeClonned,
            final T[] objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null || objects == null) {
            return objectToBeClonned;
        }
        final Set<T> objectsSet = new HashSet<>(objects.length);
        Collections.addAll(objectsSet, objects);
        return objectsSet.contains(objectToBeClonned)
                ? deepClone(objectToBeClonned) : objectToBeClonned;
    }

    /**
     * clones only if {@code objects} contains the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it contains {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} contains the given
     *         {@code objectToBeClonned}, otherwise returns the same object. If
     *         {@code objectToBeClonned} is null then returns {@code null}. If
     *         {@code objects} is null then {@code objectToBeClonned} will be
     *         returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfContains(final T objectToBeClonned,
            final List<T> objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null || objects == null) {
            return objectToBeClonned;
        }
        final Set<T> objectsSet = new HashSet<>(objects);
        return objectsSet.contains(objectToBeClonned)
                ? deepClone(objectToBeClonned) : objectToBeClonned;
    }

    /**
     * clones only if {@code objects} contains the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it contains {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} contains the given
     *         {@code objectToBeClonned}, otherwise returns the same object. If
     *         {@code objectToBeClonned} is null then returns {@code null}. If
     *         {@code objects} is null then {@code objectToBeClonned} will be
     *         returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfContains(final T objectToBeClonned,
            final Set<T> objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null || objects == null) {
            return objectToBeClonned;
        }
        return objects.contains(objectToBeClonned)
                ? deepClone(objectToBeClonned) : objectToBeClonned;
    }

    /**
     * clones only if {@code objects} doesn't contain the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it doesn't contain {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} doesn't contain the
     *         given {@code objectToBeClonned}, otherwise returns the same
     *         object. If {@code objectToBeClonned} is null then returns
     *         {@code null}. If {@code objects} is null then the cloned object
     *         will be returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfDoesNotContain(final T objectToBeClonned,
            final T[] objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null) {
            return objectToBeClonned;
        }
        if (objects == null) {
            return deepClone(objectToBeClonned);
        }
        final Set<T> objectsSet = new HashSet<>(objects.length);
        Collections.addAll(objectsSet, objects);
        return objectsSet.contains(objectToBeClonned) ? objectToBeClonned
                : deepClone(objectToBeClonned);
    }

    /**
     * clones only if {@code objects} doesn't contain the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it doesn't contain {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} doesn't contain the
     *         given {@code objectToBeClonned}, otherwise returns the same
     *         object. If {@code objectToBeClonned} is null then returns
     *         {@code null}. If {@code objects} is null then the cloned object
     *         will be returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfDoesNotContain(final T objectToBeClonned,
            final List<T> objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null) {
            return objectToBeClonned;
        }
        if (objects == null) {
            return deepClone(objectToBeClonned);
        }
        final Set<T> objectsSet = new HashSet<>(objects);
        return objectsSet.contains(objectToBeClonned) ? objectToBeClonned
                : deepClone(objectToBeClonned);
    }

    /**
     * clones only if {@code objects} doesn't contain the given
     * {@code objectToBeClonned}, otherwise returns the same object.
     *
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @param objects
     *            to check if it doesn't contain {@code objectToBeClonned}.
     * @return the new cloned object only if {@code objects} doesn't contain the
     *         given {@code objectToBeClonned}, otherwise returns the same
     *         object. If {@code objectToBeClonned} is null then returns
     *         {@code null}. If {@code objects} is null then the cloned object
     *         will be returned.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    public static <T> T deepCloneOnlyIfDoesNotContain(final T objectToBeClonned,
            final Set<T> objects) throws CloneNotSupportedException {
        if (objectToBeClonned == null) {
            return objectToBeClonned;
        }
        if (objects == null) {
            return deepClone(objectToBeClonned);
        }
        return objects.contains(objectToBeClonned) ? objectToBeClonned
                : deepClone(objectToBeClonned);
    }

    /**
     * @param objectToBeClonned
     *            from which a new object will be cloned. {@code null} will be
     *            returned for null value.
     * @return the newly cloned object or {@code null} if
     *         {@code objectToBeClonned} is null.
     * @throws CloneNotSupportedException
     * @since 1.0.0
     * @author WFF
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepClone(final T objectToBeClonned)
            throws CloneNotSupportedException {
        if (objectToBeClonned == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            oos.writeObject(objectToBeClonned);
            oos.flush();

            final ByteArrayInputStream bin = new ByteArrayInputStream(
                    bos.toByteArray());
            ois = new ObjectInputStream(bin);

            return (T) ois.readObject();
        } catch (final NotSerializableException e) {
            throw new CloneNotSupportedException(e.getMessage()
                    + " is not serializable. Implement java.io.Serializable in "
                    + e.getMessage());
        } catch (final Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }
}
