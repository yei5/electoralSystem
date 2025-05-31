package utils;

import java.util.*;

public class MockDatabase {
    public static Map<String, String> votantTable = new HashMap<>();
    public static Set<String> voted = new HashSet<>();

    static {
        votantTable.put("123", "Mesa 1 - Colegio A");
        votantTable.put("456", "Mesa 2 - Universidad B");
    }

    public static String getVotingTable(String id) {
        return votantTable.getOrDefault(id, "No encontrado");
    }

    public static boolean hasVoted(String id) {
        return voted.contains(id);
    }

    public static boolean registerVote(String id) {
        if (hasVoted(id)) return false;
        voted.add(id);
        return true;
    }
}

