package kupihleba;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Class holds info about public key of the person
 */
public class Person implements Serializable {
    public Person(String name, String id, PublicKey publicKey) {
        this.name = name.replace(" ", "_");
        this.id = id;
        this.publicKey = publicKey;
    }

    private String name;
    private String id;
    private PublicKey publicKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return name.equals(person.name);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
