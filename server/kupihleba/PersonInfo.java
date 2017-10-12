package kupihleba;

import org.apache.commons.codec.digest.Sha2Crypt;

import java.util.Date;

/**
 * Format for data
 */
public class PersonInfo {
    PersonInfo(String content) {
        this.content = content;
        hash = Sha2Crypt.sha256Crypt(content.getBytes());
    }

    String label;
    String content;
    String hash;
    Date dateMark = new Date();

    @Override
    public String toString() {
        return content;
    }
}
