package kupihleba;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;

/**
 * Class holds info about public key of the person
 */
public class Person implements Serializable {
    public Person(String name, String surname, long id, PublicKey publicKey) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.publicKey = publicKey;
    }

    private String name;
    private String surname;
    private long id;
    private PublicKey publicKey;

    private HashMap<String, PersonInfo> data = new HashMap<>();

    public void addData(PersonInfo data) {
        this.data.put(data.label, data);
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("OK!");

        if (this == o) return true;
        if (o != null && o.getClass() == long.class) {
            return this.id == (long) o;
        }
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id == person.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                ", publicKey=" + publicKey +
                ", data=" + data.toString() +
                '}';
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
