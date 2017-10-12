package kupihleba;

import osint_pack.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Preferences implements Serializable {
    String RSAKeysPath;
    Credits credits;
    boolean firstRun;
    ArrayList<Module> modules;
    HashSet<String> activatedModules;
}
