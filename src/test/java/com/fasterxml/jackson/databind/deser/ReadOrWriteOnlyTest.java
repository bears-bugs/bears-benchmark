package com.fasterxml.jackson.databind.deser;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;

public class ReadOrWriteOnlyTest extends BaseMapTest
{
    // for [databind#935], verify read/write-only cases
    static class ReadXWriteY {
        @JsonProperty(access=JsonProperty.Access.READ_ONLY)
        public int x = 1;

        @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
        public int y = 2;

        public void setX(int x) {
            throw new Error("Should NOT set x");
        }

        public int getY() {
            throw new Error("Should NOT get y");
        }
    }

    public static class Pojo935
    {
        private String firstName = "Foo";
        private String lastName = "Bar";

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        public String getFullName() {
            return firstName + " " + lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String n) {
            firstName = n;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String n) {
            lastName = n;
        }
    }

    // for [databind#1345], emulate way Lombok embellishes classes
    static class Foo1345 {
        @JsonProperty(access=JsonProperty.Access.READ_ONLY)
        public String id;
        public String name;

        @ConstructorProperties({ "id", "name" })
        public Foo1345(String id, String name) {
            this.id = id;
            this.name = name;
        }

        protected Foo1345() { }
    }

    /*
    /**********************************************************
    /* Test methods
    /**********************************************************
     */

    private final ObjectMapper MAPPER = new ObjectMapper();

    // [databind#935]
    public void testReadOnlyAndWriteOnly() throws Exception
    {
        String json = MAPPER.writeValueAsString(new ReadXWriteY());
        assertEquals("{\"x\":1}", json);

        ReadXWriteY result = MAPPER.readValue("{\"x\":5, \"y\":6}", ReadXWriteY.class);
        assertNotNull(result);
        assertEquals(1, result.x);
        assertEquals(6, result.y);
    }

    public void testReadOnly935() throws Exception
    {
        String json = MAPPER.writeValueAsString(new Pojo935());
        Pojo935 result = MAPPER.readValue(json, Pojo935.class);
        assertNotNull(result);
    }

    public void testReadOnly1345() throws Exception
    {
        Foo1345 result = MAPPER.readValue("{\"name\":\"test\"}", Foo1345.class);
        assertNotNull(result);
        assertEquals("test", result.name);
        assertNull(result.id);
    }
}
