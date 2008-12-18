package testClass.copy.dpl;

import static com.sleepycat.persist.model.Relationship.ONE_TO_ONE;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;





// Another entity class.
//
@Entity
class Employer {

    @PrimaryKey(sequence="ID")
    long id;

    @SecondaryKey(relate=ONE_TO_ONE)
    String name;

    Address address;

    Employer(String name) {
        this.name = name;
    }

    private Employer() {} // For bindings
}