package testClass.copy.dpl;


import com.sleepycat.persist.model.Persistent;


//A persistent class used in other classes.
//
@Persistent
class Address {
    String street;
    String city;
    String state;
    int zipCode;
    private Address() {} // For bindings
}




