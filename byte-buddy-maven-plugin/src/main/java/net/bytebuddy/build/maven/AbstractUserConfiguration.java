package net.bytebuddy.build.maven;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * An abstract base class for a user configuration implying a Maven coordinate.
 */
@SuppressFBWarnings(value = "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", justification = "Written to by Maven")
public class AbstractUserConfiguration {

    /**
     * The group id of the project containing the plugin type or {@code null} if the current project's group id should be used.
     */
    protected String groupId;

    /**
     * The artifact id of the project containing the plugin type or {@code null} if the current project's artifact id should be used.
     */
    protected String artifactId;

    /**
     * The version of the project containing the plugin type or {@code null} if the current project's version should be used.
     */
    protected String version;

    /**
     * Returns the group id to use.
     *
     * @param groupId The current project's group id.
     * @return The group id to use.
     */
    protected String getGroupId(String groupId) {
        return this.groupId == null || this.groupId.isEmpty()
                ? groupId
                : this.groupId;
    }

    /**
     * Returns the artifact id to use.
     *
     * @param artifactId The current project's artifact id.
     * @return The artifact id to use.
     */
    protected String getArtifactId(String artifactId) {
        return this.artifactId == null || this.artifactId.isEmpty()
                ? artifactId
                : this.artifactId;
    }


    /**
     * Returns the version to use.
     *
     * @param version The current project's version.
     * @return The version to use.
     */
    protected String getVersion(String version) {
        return this.version == null || this.version.isEmpty()
                ? version
                : this.version;
    }

    /**
     * Resolves this transformation to a Maven coordinate.
     *
     * @param groupId    The current project's build id.
     * @param artifactId The current project's artifact id.
     * @param version    The current project's version.
     * @return The resolved Maven coordinate.
     */
    public MavenCoordinate asCoordinate(String groupId, String artifactId, String version) {
        return new MavenCoordinate(getGroupId(groupId), getArtifactId(artifactId), getVersion(version));
    }
}
