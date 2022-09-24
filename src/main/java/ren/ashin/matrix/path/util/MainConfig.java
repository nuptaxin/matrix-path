package ren.ashin.matrix.path.util;

import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;

@Sources({"file:conf/config.properties"})
public interface MainConfig extends Mutable {

    @Key("size")
    int size();

    @Key("nodes")
    String nodes();
    
    @Key("AlgorithmSchema")
    int algorithmSchema();
}
