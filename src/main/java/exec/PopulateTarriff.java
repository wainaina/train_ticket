/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exec;

import com.tenacle.sgr.entities.AbstractRepository;
import com.tenacle.sgr.entities.Location;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author samuel
 */
public class PopulateTarriff {

    public static void main(String[] args) {
        List<Location> loc = new AbstractRepository<Location>(Location.class).findAll();

        System.out.println(loc);
    }

    public static ImmutablePair<Double, Double> a(Double econ, Double first) {
        return new ImmutablePair<Double, Double>(econ, first);
    }
}
