package testClass.copy.dpl;


import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;


class PersonAccessor {

    // Person accessors
    //
    PrimaryIndex<String,Person> personBySsn;
    SecondaryIndex<String,String,Person> personByParentSsn;
    SecondaryIndex<String,String,Person> personByEmailAddresses;
    SecondaryIndex<Long,String,Person> personByEmployerIds;

    // Employer accessors
    //
    PrimaryIndex<Long,Employer> employerById;
    SecondaryIndex<String,Long,Employer> employerByName;

    // Opens all primary and secondary indices.
    //
    public PersonAccessor(EntityStore store)
        throws DatabaseException {

        personBySsn = store.getPrimaryIndex(
            String.class, Person.class);

        personByParentSsn = store.getSecondaryIndex(
            personBySsn, String.class, "parentSsn");

        personByEmailAddresses = store.getSecondaryIndex(
            personBySsn, String.class, "emailAddresses");

        personByEmployerIds = store.getSecondaryIndex(
            personBySsn, Long.class, "employerIds");

        employerById = store.getPrimaryIndex(
            Long.class, Employer.class);

        employerByName = store.getSecondaryIndex(
            employerById, String.class, "name"); 
    }
}