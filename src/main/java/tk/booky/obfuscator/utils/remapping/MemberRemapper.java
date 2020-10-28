package tk.booky.obfuscator.utils.remapping;
// Created by booky10 in Obfuscator (21:12 28.10.20)

import org.objectweb.asm.commons.SimpleRemapper;

import java.util.Map;

public class MemberRemapper extends SimpleRemapper {

    public MemberRemapper(Map<String, String> mappings) {
        super(mappings);
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        String remappedName = map(owner + '.' + name + '.' + desc);
        return (remappedName != null) ? remappedName : name;
    }
}