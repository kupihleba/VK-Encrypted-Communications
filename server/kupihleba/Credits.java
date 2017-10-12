package kupihleba;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;

public class Credits implements Serializable {
    private static HashMap<Long, Person> people = new HashMap<>();
    private static String PATH = "Data/";
    static {
        //people.addAll( ... ); TODO
        try {
            loadData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(Person... p) {
        Arrays.asList(p).forEach(person -> {
            people.put(person.getId(), person);
        });
    }

    public static PublicKey getKeyOf(long id) {
        return people.get(id).getPublicKey();
    }

    public static boolean humanIdExists(long id) {
        return people.containsKey(id);
    }
    /**
     * @deprecated
     */
    public static void loadData() throws IOException, ClassNotFoundException {
        if (!new File(PATH).exists()) {
            throw new IOException(String.format("\"%s\" do not exist!", PATH));
        }
        File[] files = new File(".").listFiles();
        assert files != null;
        people.clear();
        for (File f : files) {
            if (f.isFile() && FilenameUtils.getExtension(f.getName()).equals("dat")) {
                System.out.println(f.getName());
                FileInputStream fis = new FileInputStream(f.getAbsolutePath());
                ObjectInputStream in = new ObjectInputStream(fis);
                Person person = (Person) in.readObject();
                //XMLDecoder decoder = new XMLDecoder(fis);
                //Person person = (Person) decoder.readObject();
                //decoder.close();
                fis.close();
                people.put(person.getId(), person);
            }
        }
    }

    /**
     * @deprecated
     */
    public static void createData() throws IOException {
        // CREATE PATH HERE
        File f;
        if (!(f = new File(PATH)).exists()) {
            f.mkdirs();
        }
        for (Person person : people.values()) {
            FileOutputStream fos = new FileOutputStream(PATH + String.format("%s_%s.dat", person.getName(), person.getId()));
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(person);
            out.close();
            //XMLEncoder encoder = new XMLEncoder(fos);
            // encoder.setExceptionListener(Throwable::printStackTrace);
            //encoder.writeObject(person); // TODO check security issues
            //encoder.close();
            fos.close();
        }
    }
}
