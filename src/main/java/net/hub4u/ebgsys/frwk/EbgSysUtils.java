package net.hub4u.ebgsys.frwk;

import java.util.ArrayList;
import java.util.Arrays;
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

//        List<Integer> refDigits = references.stream()
//                .map(ref -> ref.replaceFirst("^0+(?!$)", ""))
//                .map(ref -> Integer.valueOf(ref.substring(ref.lastIndexOf("-")+1, ref.length())) )
//                .collect(Collectors.toList());

        int nextInt = 0;
        if (refDigits != null && !refDigits.isEmpty()) {
            nextInt = Collections.max(refDigits);
        }

        System.out.println(nextInt++);


        String nextRef = String.format("%0"+nbrSize+"d", nextInt) ;

        System.out.println(nextRef);

        return prefix + nextRef;
    }


    public static void main(String[] args) {

        List<String> references = Arrays.asList("VH-EBG-0000221", "VH-EBG-0000022", "VH-EBG-0000028","VH-EBG-0000032");


        System.out.println("Next Ref: " + retrieveNextReference("VH-EBG-", 6, references));

        System.out.println("Start Ref: " + retrieveNextReference("EMP-EBG-", 6, new ArrayList<>()));



//        int yournumber = 246;
//        int size = 8;
//        System.out.println(String.format("%0"+size+"d", yournumber));
    }
}
