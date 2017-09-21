package kupihleba;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

/**
 * OLOLO LOLbase codding
 */
public class Zalgo {
    private Zalgo() {
    }

    public static String encode(byte[] bytes) {
        BigInteger data = new BigInteger(1, bytes);
        StringBuilder lolBuilder = new StringBuilder();
        BigInteger remainder;
        BigInteger cardinal = BigInteger.valueOf(Alphabet.POWER);
        while (data.signum() == 1) {
            remainder = data.mod(cardinal);
            lolBuilder.append(Alphabet.getChar(remainder.intValue()));
            data = data.divide(cardinal);
        }

        return lolBuilder.toString();
    }

    public static byte[] decode(String lol) {
        BigInteger data = BigInteger.ZERO;
        char[] lolChars = lol.toCharArray();
        ArrayUtils.reverse(lolChars);

        BigInteger cardinal = BigInteger.valueOf(Alphabet.POWER);
        char c;
        int i, cnt = 0;
        for (i = 0; i < lolChars.length; i++) {
            //System.out.println(String.format("i: %d;\tchar: %c", i, lolChars[i]));
            c = lolChars[i];
            data = data.add(BigInteger.valueOf(Alphabet.idOf(c)));
            if (i < lolChars.length - 1) {
                data = data.multiply(cardinal);
                cnt++;
            }
        }
        byte[] res = data.toByteArray();
        if (res[0] == 0) {
            return ArrayUtils.subarray(res, 1, res.length);
        } else return res;
    }

}


final class Alphabet {
    public static long POWER;
    private static char[] total = new char[1000];
    public final static String normal = "0123456789qazxswedcvfrtyhgbnmjuiklopQWEDSAZXCVFRTGBNHYUJMKIOLP";//"qwertyuiop[]';lkjhgfdsazxcvbnm,./`1234567890-=~!@$%^&*()_+|}{POIUYTREWQASDFGHJKL:?><MNBVCXZ ";


    public final static char[] zalgoUp = new char[]{
            '\u030d', /*     ̍     */   '\u030e', /*     ̎     */   '\u0304', /*     ̄     */   '\u0305', /*     ̅     */
            '\u033f', /*     ̿     */   '\u0311', /*     ̑     */   '\u0306', /*     ̆     */   '\u0310', /*     ̐     */
            '\u0352', /*     ͒     */   '\u0357', /*     ͗     */   '\u0351', /*     ͑     */   '\u0307', /*     ̇     */
            '\u0308', /*     ̈     */   '\u030a', /*     ̊     */   '\u0342', /*     ͂     */   '\u0343', /*     ̓     */
            '\u0344', /*     ̈́     */    '\u034a', /*     ͊     */   '\u034b', /*     ͋     */   '\u034c', /*     ͌     */
            '\u0303', /*     ̃     */   '\u0302', /*     ̂     */   '\u030c', /*     ̌     */   '\u0350', /*     ͐     */
            '\u0300', /*     ̀     */   '\u0301', /*     ́     */   '\u030b', /*     ̋     */   '\u030f', /*     ̏     */
            '\u0312', /*     ̒     */   '\u0313', /*     ̓     */   '\u0314', /*     ̔     */   '\u033d', /*     ̽     */
            '\u0309', /*     ̉     */   '\u0363', /*     ͣ     */   '\u0364', /*     ͤ     */   '\u0365', /*     ͥ     */
            '\u0366', /*     ͦ     */   '\u0367', /*     ͧ     */   '\u0368', /*     ͨ     */   '\u0369', /*     ͩ     */
            '\u036a', /*     ͪ     */   '\u036b', /*     ͫ     */   '\u036c', /*     ͬ     */   '\u036d', /*     ͭ     */
            '\u036e', /*     ͮ     */   '\u036f', /*     ͯ     */   '\u033e', /*     ̾     */   '\u035b', /*     ͛     */
            '\u0346', /*     ͆     */   '\u031a' /*     ̚     */
    };


    public final static char[] zalgoDown = new char[]{
            '\u0316', /*     ̖     */   '\u0317', /*     ̗     */   '\u0318', /*     ̘     */   '\u0319', /*     ̙     */
            '\u031c', /*     ̜     */   '\u031d', /*     ̝     */   '\u031e', /*     ̞     */   '\u031f', /*     ̟     */
            '\u0320', /*     ̠     */   '\u0324', /*     ̤     */   '\u0325', /*     ̥     */   '\u0326', /*     ̦     */
            '\u0329', /*     ̩     */   '\u032a', /*     ̪     */   '\u032b', /*     ̫     */   '\u032c', /*     ̬     */
            '\u032d', /*     ̭     */   '\u032e', /*     ̮     */   '\u032f', /*     ̯     */   '\u0330', /*     ̰     */
            '\u0331', /*     ̱     */   '\u0332', /*     ̲     */   '\u0333', /*     ̳     */   '\u0339', /*     ̹     */
            '\u033a', /*     ̺     */   '\u033b', /*     ̻     */   '\u033c', /*     ̼     */   '\u0345', /*     ͅ     */
            '\u0347', /*     ͇     */   '\u0348', /*     ͈     */   '\u0349', /*     ͉     */   '\u034d', /*     ͍     */
            '\u034e', /*     ͎     */   '\u0353', /*     ͓     */   '\u0354', /*     ͔     */   '\u0355', /*     ͕     */
            '\u0356', /*     ͖     */   '\u0359', /*     ͙     */   '\u035a', /*     ͚     */   '\u0323' /*     ̣     */
    };
    public final static char[] zalgoMid = new char[]{
            '\u0315', /*     ̕     */   '\u031b', /*     ̛     */   '\u0340', /*     ̀     */   '\u0341', /*     ́     */
            '\u0358', /*     ͘     */   '\u0321', /*     ̡     */   '\u0322', /*     ̢     */   '\u0327', /*     ̧     */
            '\u0328', /*     ̨     */   '\u0334', /*     ̴     */   '\u0335', /*     ̵     */   //'\u0336', /*     ̶     */
            '\u034f', /*     ͏     */   '\u035c', /*     ͜     */   '\u035d', /*     ͝     */   '\u035e', /*     ͞     */
            '\u035f', /*     ͟     */   '\u0360', /*     ͟     */   '\u0362', /*     ͢     */   '\u0338', /*     ̸     */
            /*'\u0337', /*     ̷     */   '\u0361', /*     ͡     */   '\u0489' /*     ҉_     */
    };

    static {
        //POWER = Alphabet.normal.length() + Alphabet.zalgoDown.length + Alphabet.zalgoMid.length + Alphabet.zalgoUp.length;
        //ArrayUtils.addAll(total, normal.toCharArray());
        //total = ArrayUtils.addAll(zalgoDown, zalgoUp);
        total = ArrayUtils.addAll(normal.toCharArray());
        total = ArrayUtils.addAll(total, zalgoUp);
        total = ArrayUtils.addAll(total, zalgoDown);
        total = ArrayUtils.addAll(total, zalgoMid);
        POWER = total.length;
        System.out.println("Initialized Zalgo Alphabet");
        //ArrayUtils.addAll(total, zalgoDown);
        //ArrayUtils.addAll(total, zalgoMid);
        //ArrayUtils.addAll(total, zalgoUp);
    }

    public static char getChar(int id) {
        return total[id];
    }

    public static int idOf(char c) {
        for (int i = 0; i < total.length; i++) {
            if (total[i] == c)
                return i;
        }
        throw new RuntimeException("Unknown data");
    }
}