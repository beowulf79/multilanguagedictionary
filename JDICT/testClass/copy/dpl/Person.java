package testClass.copy.dpl;

import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_MANY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;
import static com.sleepycat.persist.model.Relationship.ONE_TO_MANY;
import java.util.HashSet;
import java.util.Set;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;



//An entity class.
//
@Entity
class Person {

    @PrimaryKey
    String ssn;

    String name;
    Address address;

    @SecondaryKey(relate=MANY_TO_ONE, relatedEntity=Person.class)
    String parentSsn;

    @SecondaryKey(relate=ONE_TO_MANY)
    Set<String> emailAddresses = new HashSet<String>();

    @SecondaryKey(relate=MANY_TO_MANY, relatedEntity=Employer.class,
                                       onRelatedEntityDelete=NULLIFY)
    Set<Long> employerIds = new HashSet<Long>();

    Person(String name, String ssn, String parentSsn) {
        this.name = name;
        this.ssn = ssn;
        this.parentSsn = parentSsn;
    }

    private Person() {} // For bindings
}

