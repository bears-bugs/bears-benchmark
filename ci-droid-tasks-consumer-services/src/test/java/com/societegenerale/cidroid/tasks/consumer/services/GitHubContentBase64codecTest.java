package com.societegenerale.cidroid.tasks.consumer.services;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GitHubContentBase64codecTest {

    @Test
    public void shouldDecodeCorrectly() {

        String encodedString = "MXN0IGNvbW1pdCBpbiBtYXN0ZXIKCjJuZCBjb21taXQgaW4gbWFzdGVyCgoz\n" +
                "cmQgY29tbWl0IGluIG1hc3RlcgoKNHRoIGNvbW1pdCBpbiBtYXN0ZXIKCjV0\n" +
                "aCBjb21taXQgaW4gbWFzdGVyCg==";

        String decodedString = GitHubContentBase64codec.decode(encodedString);

        assertThat(decodedString).isNotNull();

    }

    @Test
    public void encodeDecodeShouldBeIdemPotent() {

        String originalString = "HelloWorld \n How are you doing ?? --> I am good !!";

        String encodedString = GitHubContentBase64codec.encode(originalString);

        assertThat(GitHubContentBase64codec.decode(encodedString)).isEqualTo(originalString);

    }

}