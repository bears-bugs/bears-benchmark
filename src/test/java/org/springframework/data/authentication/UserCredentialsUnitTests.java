/*
 * Copyright 2011-2017 by the original author(s).
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
package org.springframework.data.authentication;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

/**
 * Unit tests for {@link UserCredentials}.
 * 
 * @author Oliver Gierke
 */
public class UserCredentialsUnitTests {

	@Test
	public void treatsEmptyStringAsNull() {

		UserCredentials credentials = new UserCredentials("", "");
		assertThat(credentials.getUsername()).isNull();
		assertThat(credentials.hasUsername()).isFalse();
		assertThat(credentials.getPassword()).isNull();
		assertThat(credentials.hasPassword()).isFalse();
	}

	@Test // DATACMNS-142
	public void noCredentialsNullsUsernameAndPassword() {

		assertThat(UserCredentials.NO_CREDENTIALS.getUsername()).isNull();
		assertThat(UserCredentials.NO_CREDENTIALS.getPassword()).isNull();
	}

	@Test // DATACMNS-142
	public void configuresUsernameCorrectly() {

		UserCredentials credentials = new UserCredentials("username", null);

		assertThat(credentials.hasUsername()).isTrue();
		assertThat(credentials.getUsername()).isEqualTo("username");
		assertThat(credentials.hasPassword()).isFalse();
		assertThat(credentials.getPassword()).isNull();
	}

	@Test // DATACMNS-142
	public void configuresPasswordCorrectly() {

		UserCredentials credentials = new UserCredentials(null, "password");

		assertThat(credentials.hasUsername()).isFalse();
		assertThat(credentials.getUsername()).isNull();
		assertThat(credentials.hasPassword()).isTrue();
		assertThat(credentials.getPassword()).isEqualTo("password");
	}

	@Test // DATACMNS-275
	public void returnsNullForNotSetObfuscatedPassword() {
		assertThat(new UserCredentials(null, null).getObfuscatedPassword()).isNull();
	}

	@Test // DATACMNS-275
	public void obfuscatesShortPasswordsEntirely() {

		assertThat(new UserCredentials(null, "sa").getObfuscatedPassword()).isEqualTo("**");
		assertThat(new UserCredentials(null, "s").getObfuscatedPassword()).isEqualTo("*");
	}

	@Test // DATACMNS-275
	public void returnsObfuscatedPasswordCorrectly() {
		assertThat(new UserCredentials(null, "password").getObfuscatedPassword()).isEqualTo("p******d");
	}

	@Test // DATACMNS-275
	public void toStringDoesNotExposePlainPassword() {

		UserCredentials credentials = new UserCredentials(null, "mypassword");
		assertThat(credentials.toString()).doesNotContain(credentials.getPassword());
		assertThat(credentials.toString()).contains(credentials.getObfuscatedPassword());
	}
}
