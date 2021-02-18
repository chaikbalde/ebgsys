package net.hub4u.ebgsys.frwk;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EbgSysUtils {

    /**
     * Retrieves the next reference of an Entity based on its prefix and the previous references
     * */
    public static String retrieveNextReference(String prefix, int nbrSize, List<String> references) {

        List<Integer> refDigits = references.stream()
                .map(ref -> ref.substring(ref.lastIndexOf("-")+1, ref.length()) )
                .map(ref -> ref.replaceFirst("^0+(?!$)", ""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        int nextInt = 0;
        if (refDigits != null && !refDigits.isEmpty()) {
            nextInt = Collections.max(refDigits);
        }

        nextInt++;
        String nextRef = String.format("%0"+ nbrSize +"d", nextInt) ;

        return prefix + nextRef;
    }
}
