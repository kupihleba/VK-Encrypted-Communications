package kupihleba;

import java.security.PublicKey;
import java.util.ArrayList;

public class Credits {
    static {
        //people.addAll( ... ); TODO
    }

    private static ArrayList<Person> people = new ArrayList<>();

    public static PublicKey getKeyOf(String name) {
        return people.get(people.indexOf(name)).getPublicKey();
    }
}
