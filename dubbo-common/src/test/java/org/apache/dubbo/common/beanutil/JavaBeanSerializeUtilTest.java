/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.beanutil;

import org.apache.dubbo.common.model.person.BigPerson;
import org.apache.dubbo.common.model.person.FullAddress;
import org.apache.dubbo.common.model.person.PersonInfo;
import org.apache.dubbo.common.model.person.PersonStatus;
import org.apache.dubbo.common.model.person.Phone;
import org.apache.dubbo.common.utils.PojoUtilsTest;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JavaBeanSerializeUtilTest {

    @Test
    public void testSerialize_Primitive() throws Exception {
        JavaBeanDescriptor descriptor;
        descriptor = JavaBeanSerializeUtil.serialize(Integer.MAX_VALUE);
        Assert.assertTrue(descriptor.isPrimitiveType());
        Assert.assertEquals(Integer.MAX_VALUE, descriptor.getPrimitiveProperty());

        Date now = new Date();
        descriptor = JavaBeanSerializeUtil.serialize(now);
        Assert.assertTrue(descriptor.isPrimitiveType());
        Assert.assertEquals(now, descriptor.getPrimitiveProperty());
    }

    @Test
    public void testSerialize_Primitive_NUll() throws Exception {
        JavaBeanDescriptor descriptor;
        descriptor = JavaBeanSerializeUtil.serialize(null);
        Assert.assertTrue(descriptor == null);
    }

    @Test
    public void testDeserialize_Primitive() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.setPrimitiveProperty(Long.MAX_VALUE);
        Assert.assertEquals(Long.MAX_VALUE, JavaBeanSerializeUtil.deserialize(descriptor));

        BigDecimal decimal = BigDecimal.TEN;
        Assert.assertEquals(Long.MAX_VALUE, descriptor.setPrimitiveProperty(decimal));
        Assert.assertEquals(decimal, JavaBeanSerializeUtil.deserialize(descriptor));

        String string = UUID.randomUUID().toString();
        Assert.assertEquals(decimal, descriptor.setPrimitiveProperty(string));
        Assert.assertEquals(string, JavaBeanSerializeUtil.deserialize(descriptor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Primitive0() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_BEAN + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Null() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(null, JavaBeanDescriptor.TYPE_BEAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_containsProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.containsProperty(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEnumNameProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.setEnumNameProperty(JavaBeanDescriptor.class.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEnumNameProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.getEnumPropertyName();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetClassNameProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.setClassNameProperty(JavaBeanDescriptor.class.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetClassNameProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.getClassNameProperty();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPrimitiveProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(JavaBeanDescriptor.class.getName(), JavaBeanDescriptor.TYPE_BEAN);
        descriptor.setPrimitiveProperty(JavaBeanDescriptor.class.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPrimitiveProperty() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(JavaBeanDescriptor.class.getName(), JavaBeanDescriptor.TYPE_BEAN);
        descriptor.getPrimitiveProperty();
    }

    @Test
    public void testDeserialize_get_and_set() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(long.class.getName(), JavaBeanDescriptor.TYPE_BEAN);
        descriptor.setType(JavaBeanDescriptor.TYPE_PRIMITIVE);
        Assert.assertTrue(descriptor.getType() == JavaBeanDescriptor.TYPE_PRIMITIVE);
        descriptor.setClassName(JavaBeanDescriptor.class.getName());
        Assert.assertEquals(JavaBeanDescriptor.class.getName(), descriptor.getClassName());
    }

    @Test
    public void testSerialize_Array() throws Exception {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        JavaBeanDescriptor descriptor = JavaBeanSerializeUtil.serialize(array, JavaBeanAccessor.METHOD);
        Assert.assertTrue(descriptor.isArrayType());
        Assert.assertEquals(int.class.getName(), descriptor.getClassName());
        for (int i = 0; i < array.length; i++) {
            Assert.assertEquals(array[i],
                    ((JavaBeanDescriptor) descriptor.getProperty(i)).getPrimitiveProperty());
        }

        Integer[] integers = new Integer[]{1, 2, 3, 4, null, null, null};
        descriptor = JavaBeanSerializeUtil.serialize(integers, JavaBeanAccessor.METHOD);
        Assert.assertTrue(descriptor.isArrayType());
        Assert.assertEquals(Integer.class.getName(), descriptor.getClassName());
        Assert.assertEquals(integers.length, descriptor.propertySize());
        for (int i = 0; i < integers.length; i++) {
            if (integers[i] == null) {
                Assert.assertTrue(integers[i] == descriptor.getProperty(i));
            } else {
                Assert.assertEquals(integers[i], ((JavaBeanDescriptor) descriptor.getProperty(i)).getPrimitiveProperty());
            }
        }

        int[][] second = {{1, 2}, {3, 4}};
        descriptor = JavaBeanSerializeUtil.serialize(second, JavaBeanAccessor.METHOD);
        Assert.assertTrue(descriptor.isArrayType());
        Assert.assertEquals(int[].class.getName(), descriptor.getClassName());
        for (int i = 0; i < second.length; i++) {
            for (int j = 0; j < second[i].length; j++) {
                JavaBeanDescriptor item = (((JavaBeanDescriptor) descriptor.getProperty(i)));
                Assert.assertTrue(item.isArrayType());
                Assert.assertEquals(int.class.getName(), item.getClassName());
                Assert.assertEquals(second[i][j], ((JavaBeanDescriptor) item.getProperty(j)).getPrimitiveProperty());
            }
        }

        BigPerson[] persons = new BigPerson[]{createBigPerson(), createBigPerson()};
        descriptor = JavaBeanSerializeUtil.serialize(persons);
        Assert.assertTrue(descriptor.isArrayType());
        Assert.assertEquals(BigPerson.class.getName(), descriptor.getClassName());
        for (int i = 0; i < persons.length; i++) {
            assertEqualsBigPerson(persons[i], descriptor.getProperty(i));
        }
    }

    @Test
    public void testConstructorArg() {
        Assert.assertFalse((boolean) JavaBeanSerializeUtil.getConstructorArg(boolean.class));
        Assert.assertFalse((boolean) JavaBeanSerializeUtil.getConstructorArg(Boolean.class));
        Assert.assertEquals((byte)0, JavaBeanSerializeUtil.getConstructorArg(byte.class));
        Assert.assertEquals((byte)0, JavaBeanSerializeUtil.getConstructorArg(Byte.class));
        Assert.assertEquals((short)0, JavaBeanSerializeUtil.getConstructorArg(short.class));
        Assert.assertEquals((short)0, JavaBeanSerializeUtil.getConstructorArg(Short.class));
        Assert.assertEquals(0, JavaBeanSerializeUtil.getConstructorArg(int.class));
        Assert.assertEquals(0, JavaBeanSerializeUtil.getConstructorArg(Integer.class));
        Assert.assertEquals((long)0, JavaBeanSerializeUtil.getConstructorArg(long.class));
        Assert.assertEquals((long)0, JavaBeanSerializeUtil.getConstructorArg(Long.class));
        Assert.assertEquals((float) 0, JavaBeanSerializeUtil.getConstructorArg(float.class));
        Assert.assertEquals((float) 0, JavaBeanSerializeUtil.getConstructorArg(Float.class));
        Assert.assertEquals((double) 0, JavaBeanSerializeUtil.getConstructorArg(double.class));
        Assert.assertEquals((double) 0, JavaBeanSerializeUtil.getConstructorArg(Double.class));
        Assert.assertEquals((char)0, JavaBeanSerializeUtil.getConstructorArg(char.class));
        Assert.assertEquals(new Character((char)0), JavaBeanSerializeUtil.getConstructorArg(Character.class));
        Assert.assertEquals(null, JavaBeanSerializeUtil.getConstructorArg(JavaBeanSerializeUtil.class));
    }

    @Test
    public void testDeserialize_Array() throws Exception {
        final int len = 10;
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor(int.class.getName(), JavaBeanDescriptor.TYPE_ARRAY);
        for (int i = 0; i < len; i++) {
            descriptor.setProperty(i, i);
        }

        Object obj = JavaBeanSerializeUtil.deserialize(descriptor);
        Assert.assertTrue(obj.getClass().isArray());
        Assert.assertTrue(int.class == obj.getClass().getComponentType());
        for (int i = 0; i < len; i++) {
            Assert.assertEquals(i, Array.get(obj, i));
        }

        descriptor = new JavaBeanDescriptor(int[].class.getName(), JavaBeanDescriptor.TYPE_ARRAY);
        for (int i = 0; i < len; i++) {
            JavaBeanDescriptor innerItem = new JavaBeanDescriptor(int.class.getName(), JavaBeanDescriptor.TYPE_ARRAY);
            for (int j = 0; j < len; j++) {
                innerItem.setProperty(j, j);
            }
            descriptor.setProperty(i, innerItem);
        }
        obj = JavaBeanSerializeUtil.deserialize(descriptor);
        Assert.assertTrue(obj.getClass().isArray());
        Assert.assertEquals(int[].class, obj.getClass().getComponentType());
        for (int i = 0; i < len; i++) {
            Object innerItem = Array.get(obj, i);
            Assert.assertTrue(innerItem.getClass().isArray());
            Assert.assertEquals(int.class, innerItem.getClass().getComponentType());
            for (int j = 0; j < len; j++) {
                Assert.assertEquals(j, Array.get(innerItem, j));
            }
        }

        descriptor = new JavaBeanDescriptor(BigPerson[].class.getName(), JavaBeanDescriptor.TYPE_ARRAY);
        JavaBeanDescriptor innerDescriptor = new JavaBeanDescriptor(BigPerson.class.getName(), JavaBeanDescriptor.TYPE_ARRAY);
        innerDescriptor.setProperty(0, JavaBeanSerializeUtil.serialize(createBigPerson(), JavaBeanAccessor.METHOD));
        descriptor.setProperty(0, innerDescriptor);

        obj = JavaBeanSerializeUtil.deserialize(descriptor);
        Assert.assertTrue(obj.getClass().isArray());
        Assert.assertEquals(BigPerson[].class, obj.getClass().getComponentType());
        Assert.assertEquals(1, Array.getLength(obj));
        obj = Array.get(obj, 0);
        Assert.assertTrue(obj.getClass().isArray());
        Assert.assertEquals(BigPerson.class, obj.getClass().getComponentType());
        Assert.assertEquals(1, Array.getLength(obj));
        Assert.assertEquals(createBigPerson(), Array.get(obj, 0));
    }

    @Test
    public void test_Circular_Reference() throws Exception {
        PojoUtilsTest.Parent parent = new PojoUtilsTest.Parent();
        parent.setAge(Integer.MAX_VALUE);
        parent.setEmail("a@b");
        parent.setName("zhangsan");

        PojoUtilsTest.Child child = new PojoUtilsTest.Child();
        child.setAge(100);
        child.setName("lisi");
        child.setParent(parent);

        parent.setChild(child);

        JavaBeanDescriptor descriptor = JavaBeanSerializeUtil.serialize(parent, JavaBeanAccessor.METHOD);
        Assert.assertTrue(descriptor.isBeanType());
        assertEqualsPrimitive(parent.getAge(), descriptor.getProperty("age"));
        assertEqualsPrimitive(parent.getName(), descriptor.getProperty("name"));
        assertEqualsPrimitive(parent.getEmail(), descriptor.getProperty("email"));

        JavaBeanDescriptor childDescriptor = (JavaBeanDescriptor) descriptor.getProperty("child");
        Assert.assertTrue(descriptor == childDescriptor.getProperty("parent"));
        assertEqualsPrimitive(child.getName(), childDescriptor.getProperty("name"));
        assertEqualsPrimitive(child.getAge(), childDescriptor.getProperty("age"));
    }

    @Test
    public void testBeanSerialize() throws Exception {
        Bean bean = new Bean();
        bean.setDate(new Date());
        bean.setStatus(PersonStatus.ENABLED);
        bean.setType(Bean.class);
        bean.setArray(new Phone[]{});

        Collection<Phone> collection = new ArrayList<Phone>();
        bean.setCollection(collection);
        Phone phone = new Phone();
        collection.add(phone);

        Map<String, FullAddress> map = new HashMap<String, FullAddress>();
        FullAddress address = new FullAddress();
        map.put("first", address);
        bean.setAddresses(map);

        JavaBeanDescriptor descriptor = JavaBeanSerializeUtil.serialize(bean, JavaBeanAccessor.METHOD);
        Assert.assertTrue(descriptor.isBeanType());
        assertEqualsPrimitive(bean.getDate(), descriptor.getProperty("date"));
        assertEqualsEnum(bean.getStatus(), descriptor.getProperty("status"));
        Assert.assertTrue(((JavaBeanDescriptor) descriptor.getProperty("type")).isClassType());
        Assert.assertEquals(Bean.class.getName(), ((JavaBeanDescriptor) descriptor.getProperty("type")).getClassNameProperty());
        Assert.assertTrue(((JavaBeanDescriptor) descriptor.getProperty("array")).isArrayType());
        Assert.assertEquals(0, ((JavaBeanDescriptor) descriptor.getProperty("array")).propertySize());

        JavaBeanDescriptor property = (JavaBeanDescriptor) descriptor.getProperty("collection");
        Assert.assertTrue(property.isCollectionType());
        Assert.assertEquals(1, property.propertySize());
        property = (JavaBeanDescriptor) property.getProperty(0);
        Assert.assertTrue(property.isBeanType());
        Assert.assertEquals(Phone.class.getName(), property.getClassName());
        Assert.assertEquals(0, property.propertySize());

        property = (JavaBeanDescriptor) descriptor.getProperty("addresses");
        Assert.assertTrue(property.isMapType());
        Assert.assertEquals(bean.getAddresses().getClass().getName(), property.getClassName());
        Assert.assertEquals(1, property.propertySize());


        Map.Entry<Object, Object> entry = property.iterator().next();
        Assert.assertTrue(((JavaBeanDescriptor) entry.getKey()).isPrimitiveType());
        Assert.assertEquals("first", ((JavaBeanDescriptor) entry.getKey()).getPrimitiveProperty());

        Assert.assertTrue(((JavaBeanDescriptor) entry.getValue()).isBeanType());
        Assert.assertEquals(FullAddress.class.getName(), ((JavaBeanDescriptor) entry.getValue()).getClassName());
        Assert.assertEquals(0, ((JavaBeanDescriptor) entry.getValue()).propertySize());
    }

    @Test
    public void testDeserializeBean() throws Exception {
        Bean bean = new Bean();
        bean.setDate(new Date());
        bean.setStatus(PersonStatus.ENABLED);
        bean.setType(Bean.class);
        bean.setArray(new Phone[]{});

        Collection<Phone> collection = new ArrayList<Phone>();
        bean.setCollection(collection);
        Phone phone = new Phone();
        collection.add(phone);

        Map<String, FullAddress> map = new HashMap<String, FullAddress>();
        FullAddress address = new FullAddress();
        map.put("first", address);
        bean.setAddresses(map);

        JavaBeanDescriptor beanDescriptor = JavaBeanSerializeUtil.serialize(bean, JavaBeanAccessor.METHOD);
        Object deser = JavaBeanSerializeUtil.deserialize(beanDescriptor);
        Assert.assertTrue(deser instanceof Bean);
        Bean deserBean = (Bean) deser;
        Assert.assertEquals(bean.getDate(), deserBean.getDate());
        Assert.assertEquals(bean.getStatus(), deserBean.getStatus());
        Assert.assertEquals(bean.getType(), deserBean.getType());
        Assert.assertEquals(bean.getCollection().size(), deserBean.getCollection().size());
        Assert.assertEquals(bean.getCollection().iterator().next().getClass(),
                deserBean.getCollection().iterator().next().getClass());
        Assert.assertEquals(bean.getAddresses().size(), deserBean.getAddresses().size());
        Assert.assertEquals(bean.getAddresses().entrySet().iterator().next().getKey(),
                deserBean.getAddresses().entrySet().iterator().next().getKey());
        Assert.assertEquals(bean.getAddresses().entrySet().iterator().next().getValue().getClass(),
                deserBean.getAddresses().entrySet().iterator().next().getValue().getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSerializeJavaBeanDescriptor() throws Exception {
        JavaBeanDescriptor descriptor = new JavaBeanDescriptor();
        JavaBeanDescriptor result = JavaBeanSerializeUtil.serialize(descriptor);
        Assert.assertTrue(descriptor == result);

        Map map = new HashMap();
        map.put("first", descriptor);
        result = JavaBeanSerializeUtil.serialize(map);
        Assert.assertTrue(result.isMapType());
        Assert.assertEquals(HashMap.class.getName(), result.getClassName());
        Assert.assertEquals(map.size(), result.propertySize());
        Object object = result.iterator().next().getValue();
        Assert.assertTrue(object instanceof JavaBeanDescriptor);
        JavaBeanDescriptor actual = (JavaBeanDescriptor) object;
        Assert.assertEquals(map.get("first"), actual);
    }

    static void assertEqualsEnum(Enum<?> expected, Object obj) {
        JavaBeanDescriptor descriptor = (JavaBeanDescriptor) obj;
        Assert.assertTrue(descriptor.isEnumType());
        Assert.assertEquals(expected.getClass().getName(), descriptor.getClassName());
        Assert.assertEquals(expected.name(), descriptor.getEnumPropertyName());
    }

    static void assertEqualsPrimitive(Object expected, Object obj) {
        if (expected == null) {
            return;
        }
        JavaBeanDescriptor descriptor = (JavaBeanDescriptor) obj;
        Assert.assertTrue(descriptor.isPrimitiveType());
        Assert.assertEquals(expected, descriptor.getPrimitiveProperty());
    }

    static void assertEqualsBigPerson(BigPerson person, Object obj) {
        JavaBeanDescriptor descriptor = (JavaBeanDescriptor) obj;
        Assert.assertTrue(descriptor.isBeanType());
        assertEqualsPrimitive(person.getPersonId(), descriptor.getProperty("personId"));
        assertEqualsPrimitive(person.getLoginName(), descriptor.getProperty("loginName"));
        assertEqualsEnum(person.getStatus(), descriptor.getProperty("status"));
        assertEqualsPrimitive(person.getEmail(), descriptor.getProperty("email"));
        assertEqualsPrimitive(person.getPenName(), descriptor.getProperty("penName"));

        JavaBeanDescriptor infoProfile = (JavaBeanDescriptor) descriptor.getProperty("infoProfile");
        Assert.assertTrue(infoProfile.isBeanType());
        JavaBeanDescriptor phones = (JavaBeanDescriptor) infoProfile.getProperty("phones");
        Assert.assertTrue(phones.isCollectionType());
        assertEqualsPhone(person.getInfoProfile().getPhones().get(0), phones.getProperty(0));
        assertEqualsPhone(person.getInfoProfile().getPhones().get(1), phones.getProperty(1));
        assertEqualsPhone(person.getInfoProfile().getFax(), infoProfile.getProperty("fax"));
        assertEqualsFullAddress(person.getInfoProfile().getFullAddress(), infoProfile.getProperty("fullAddress"));
        assertEqualsPrimitive(person.getInfoProfile().getMobileNo(), infoProfile.getProperty("mobileNo"));
        assertEqualsPrimitive(person.getInfoProfile().getName(), infoProfile.getProperty("name"));
        assertEqualsPrimitive(person.getInfoProfile().getDepartment(), infoProfile.getProperty("department"));
        assertEqualsPrimitive(person.getInfoProfile().getJobTitle(), infoProfile.getProperty("jobTitle"));
        assertEqualsPrimitive(person.getInfoProfile().getHomepageUrl(), infoProfile.getProperty("homepageUrl"));
        assertEqualsPrimitive(person.getInfoProfile().isFemale(), infoProfile.getProperty("female"));
        assertEqualsPrimitive(person.getInfoProfile().isMale(), infoProfile.getProperty("male"));
    }

    static void assertEqualsPhone(Phone excpected, Object obj) {
        JavaBeanDescriptor descriptor = (JavaBeanDescriptor) obj;
        Assert.assertTrue(descriptor.isBeanType());
        if (excpected.getArea() != null) {
            assertEqualsPrimitive(excpected.getArea(), descriptor.getProperty("area"));
        }
        if (excpected.getCountry() != null) {
            assertEqualsPrimitive(excpected.getCountry(), descriptor.getProperty("country"));
        }
        if (excpected.getExtensionNumber() != null) {
            assertEqualsPrimitive(excpected.getExtensionNumber(), descriptor.getProperty("extensionNumber"));
        }
        if (excpected.getNumber() != null) {
            assertEqualsPrimitive(excpected.getNumber(), descriptor.getProperty("number"));
        }
    }

    static void assertEqualsFullAddress(FullAddress expected, Object obj) {
        JavaBeanDescriptor descriptor = (JavaBeanDescriptor) obj;
        Assert.assertTrue(descriptor.isBeanType());
        if (expected.getCityId() != null) {
            assertEqualsPrimitive(expected.getCityId(), descriptor.getProperty("cityId"));
        }
        if (expected.getCityName() != null) {
            assertEqualsPrimitive(expected.getCityName(), descriptor.getProperty("cityName"));
        }
        if (expected.getCountryId() != null) {
            assertEqualsPrimitive(expected.getCountryId(), descriptor.getProperty("countryId"));
        }
        if (expected.getCountryName() != null) {
            assertEqualsPrimitive(expected.getCountryName(), descriptor.getProperty("countryName"));
        }
        if (expected.getProvinceName() != null) {
            assertEqualsPrimitive(expected.getProvinceName(), descriptor.getProperty("provinceName"));
        }
        if (expected.getStreetAddress() != null) {
            assertEqualsPrimitive(expected.getStreetAddress(), descriptor.getProperty("streetAddress"));
        }
        if (expected.getZipCode() != null) {
            assertEqualsPrimitive(expected.getZipCode(), descriptor.getProperty("zipCode"));
        }
    }

    static BigPerson createBigPerson() {
        BigPerson bigPerson;
        bigPerson = new BigPerson();
        bigPerson.setPersonId("superman111");
        bigPerson.setLoginName("superman");
        bigPerson.setStatus(PersonStatus.ENABLED);
        bigPerson.setEmail("sm@1.com");
        bigPerson.setPenName("pname");

        ArrayList<Phone> phones = new ArrayList<Phone>();
        Phone phone1 = new Phone("86", "0571", "87654321", "001");
        Phone phone2 = new Phone("86", "0571", "87654322", "002");
        phones.add(phone1);
        phones.add(phone2);

        PersonInfo pi = new PersonInfo();
        pi.setPhones(phones);
        Phone fax = new Phone("86", "0571", "87654321", null);
        pi.setFax(fax);
        FullAddress addr = new FullAddress("CN", "zj", "3480", "wensanlu", "315000");
        pi.setFullAddress(addr);
        pi.setMobileNo("13584652131");
        pi.setMale(true);
        pi.setDepartment("b2b");
        pi.setHomepageUrl("www.capcom.com");
        pi.setJobTitle("qa");
        pi.setName("superman");

        bigPerson.setInfoProfile(pi);
        return bigPerson;
    }
}
