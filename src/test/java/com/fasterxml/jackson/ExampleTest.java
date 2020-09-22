package com.fasterxml.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ExampleTest extends BaseTest {

    final static ObjectMapper WITH_OBJECT_AND_NON_CONCRETE = new ObjectMapper().enableDefaultTypingAsProperty(
            ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
            "$type"
    );

    final static ObjectMapper PLAIN_OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testCollectionTyping() throws JsonProcessingException
    {

        final MyClass toSerialize = new MyClass();

        final String fromPlainObjectMapper = PLAIN_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(toSerialize);
        System.out.println("\nfrom plain ObjectMapper:\n" + fromPlainObjectMapper);

        final String fromObjectMapperWithDefaultTyping = WITH_OBJECT_AND_NON_CONCRETE.writerWithDefaultPrettyPrinter().writeValueAsString(toSerialize);
        System.out.println("\nfrom ObjectMapper with default typing:\n" + fromObjectMapperWithDefaultTyping);

//        assertEquals(fromPlainObjectMapper, fromObjectMapperWithDefaultTyping);
    }

    public static interface RandomPojoInterface {
        String getFirstName();

        String getLastName();
    }

    public static class RandomPojo implements RandomPojoInterface {
        private final String firstName;

        private final String lastName;

        public RandomPojo(final String firstName, final String lastName)
        {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @JsonProperty("firstName")
        public String getFirstName()
        {
            return firstName;
        }

        @JsonProperty("lastName")
        public String getLastName()
        {
            return lastName;
        }
    }

    public static class Holder {
        private final RandomPojoInterface pojo;

        public Holder(final RandomPojoInterface pojo)
        {
            this.pojo = pojo;
        }

        @JsonProperty("pojo")
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
        @JsonSubTypes({@JsonSubTypes.Type(value = RandomPojo.class, name = "randomPojo")})
        public RandomPojoInterface getPojo()
        {
            return pojo;
        }
    }

    public static class MyClass {

        private final Set<RandomPojoInterface> treeSetStrings = new HashSet<>();

        private final Set<RandomPojoInterface> immutableSortedSetStrings = new HashSet<>();

        public MyClass()
        {
            this.treeSetStrings.add(new RandomPojo("Hello", "World"));
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "cebolas", applyTo = JsonTypeInfo.ApplyTo.BOTH)
        @JsonSubTypes({@JsonSubTypes.Type(value = RandomPojo.class)})
        public Set<RandomPojoInterface> getTreeSetStrings()
        {
            return this.treeSetStrings;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "cebolas", applyTo = JsonTypeInfo.ApplyTo.BOTH)
        @JsonSubTypes({@JsonSubTypes.Type(value = RandomPojo.class)})
        public Set<RandomPojoInterface> getImmutableSortedSetStrings()
        {
            return this.immutableSortedSetStrings;
        }
    }
}
