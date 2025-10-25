package com.electionsystem;

import java.io.*;
import java.util.*;

public class ElectionSystemMain {
    private static final String IN_FILE = "voting.dat";

    public static void main(String[] args) throws Exception {

        InputStream inputStream = ElectionSystemMain.class.getClassLoader().getResourceAsStream(IN_FILE);
        if (inputStream == null) {
            System.err.println("Input file not found in resources: " + IN_FILE);
            return;
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        // Parse regions and contestants
        Map<String, Set<Character>> regionCandidates = new LinkedHashMap<>();
        int idx = 0;
        while (idx < lines.size()) {
            String line = lines.get(idx);
            if (line.isEmpty()) { idx++; continue; }
            if (line.startsWith("//") || line.equals("&&")) break;

            int slash = line.indexOf('/');
            if (slash <= 0) {
                System.err.println("Skipping invalid region line: " + line);
                idx++;
                continue;
            }

            String region = line.substring(0, slash).trim();
            String letters = line.substring(slash + 1).trim();

            Set<Character> set = new HashSet<>();
            for (char c : letters.toCharArray()) {
                if (Character.isLetter(c)) set.add(Character.toUpperCase(c));
            }
            regionCandidates.put(region, set);
            idx++;
        }

        Map<String, Map<Character, Integer>> regionPoints = new HashMap<>();
        Map<Character, Integer> totalPoints = new HashMap<>();
        Map<String, Integer> invalidVotes = new HashMap<>();
        for (String r : regionCandidates.keySet()) {
            regionPoints.put(r, new HashMap<>());
            invalidVotes.put(r, 0);
        }

        Map<String, String> voterRegion = new HashMap<>();


        while (idx < lines.size()) {
            String line = lines.get(idx);
            if (line.isEmpty()) { idx++; continue; }
            if (line.equals("&&")) break;
            if (!line.startsWith("//")) { idx++; continue; }

            idx++;
            while (idx < lines.size() && lines.get(idx).isEmpty()) idx++;
            if (idx >= lines.size()) break;
            String regionLine = lines.get(idx).trim();
            idx++;

            if (!regionCandidates.containsKey(regionLine)) {
                while (idx < lines.size() && !lines.get(idx).startsWith("//") && !lines.get(idx).equals("&&")) idx++;
                continue;
            }

            String currentRegion = regionLine;
            Set<Character> contestants = regionCandidates.get(currentRegion);
            Map<Character, Integer> rPoints = regionPoints.get(currentRegion);

            while (idx < lines.size()) {
                String vline = lines.get(idx).trim();
                if (vline.isEmpty()) { idx++; continue; }
                if (vline.startsWith("//") || vline.equals("&&")) break;

                String[] toks = vline.split("\\s+");
                if (toks.length < 2) {
                    invalidVotes.put(currentRegion, invalidVotes.get(currentRegion) + 1);
                    idx++;
                    continue;
                }

                String voterId = toks[0];
                String prefsRaw = toks[1].toUpperCase();
                boolean invalid = false;

                // Rule 1: voter voted in multiple regions
                if (voterRegion.containsKey(voterId) && !voterRegion.get(voterId).equals(currentRegion)) {
                    invalid = true;
                } else {
                    voterRegion.put(voterId, currentRegion);
                }

                // Rule 2: 0 or >3 preferences invalid
                if (prefsRaw.length() == 0 || prefsRaw.length() > 3) invalid = true;

                // Rule 3: any candidate not contesting in region invalidates vote
                for (char c : prefsRaw.toCharArray()) {
                    if (!Character.isLetter(c) || !contestants.contains(c)) {
                        invalid = true;
                        break;
                    }
                }

                if (invalid) {
                    invalidVotes.put(currentRegion, invalidVotes.get(currentRegion) + 1);
                    idx++;
                    continue;
                }


                for (int i = 0; i < prefsRaw.length(); i++) {
                    char cand = prefsRaw.charAt(i);
                    int pts = (i == 0 ? 3 : (i == 1 ? 2 : 1));
                    rPoints.put(cand, rPoints.getOrDefault(cand, 0) + pts);
                    totalPoints.put(cand, totalPoints.getOrDefault(cand, 0) + pts);
                }
                idx++;
            }
        }

        // Determine Chief Officer
        char chief = 0;
        int chiefPoints = -1;
        for (Map.Entry<Character, Integer> e : totalPoints.entrySet()) {
            char cand = e.getKey();
            int pts = e.getValue();
            if (pts > chiefPoints || (pts == chiefPoints && cand < chief)) {
                chief = cand;
                chiefPoints = pts;
            }
        }

        String chiefLine = (chiefPoints < 0)
                ? "No Chief Officer (no valid votes)"
                : String.format("%c  (Total Points: %d)", chief, chiefPoints);

        // Determine Regional Heads
        Map<String, String> regionHeads = new LinkedHashMap<>();
        for (String region : regionCandidates.keySet()) {
            Map<Character, Integer> rPts = regionPoints.get(region);
            char head = 0;
            int headPts = -1;
            for (char c : regionCandidates.get(region)) {
                if (chiefPoints >= 0 && c == chief) continue;
                int pts = rPts.getOrDefault(c, 0);
                if (pts > headPts || (pts == headPts && c < head)) {
                    head = c;
                    headPts = pts;
                }
            }
            if (headPts < 0) {
                regionHeads.put(region, "No regional head (no valid votes / only chief present)");
            } else {
                regionHeads.put(region, String.format("%c  (Points: %d)", head, headPts));
            }
        }

        // Display Results
        System.out.println("========================================");
        System.out.println(" ELECTION SYSTEM - RESULTS");
        System.out.println("========================================\n");

        System.out.println("Input File: " + IN_FILE + "\n");

        System.out.println("CHIEF OFFICER");
        System.out.println("-------------");
        System.out.println(chiefLine + "\n");

        System.out.println("REGIONAL RESULTS");
        System.out.println("----------------");

        for (String region : regionCandidates.keySet()) {
            System.out.println("Region: " + region);
            System.out.print("  Contestants: ");
            List<Character> listc = new ArrayList<>(regionCandidates.get(region));
            Collections.sort(listc);
            System.out.println(String.join(", ", listc.stream().map(String::valueOf).toList()));
            System.out.println("  Invalid Votes: " + invalidVotes.getOrDefault(region, 0));
            System.out.println("  Regional Head: " + regionHeads.get(region));
            System.out.println("  Candidate Points (region):");

            Map<Character, Integer> rPts = regionPoints.get(region);
            List<Character> allCands = new ArrayList<>(regionCandidates.get(region));
            allCands.sort((a, b) -> {
                int pa = rPts.getOrDefault(a, 0);
                int pb = rPts.getOrDefault(b, 0);
                if (pa != pb) return Integer.compare(pb, pa);
                return Character.compare(a, b);
            });

            for (char c : allCands) {
                System.out.printf("    %c : %d%n", c, rPts.getOrDefault(c, 0));
            }
            System.out.println();
        }

        System.out.println("GLOBAL CANDIDATE POINTS (ALL REGIONS)");
        System.out.println("-------------------------------------");
        List<Character> globalList = new ArrayList<>(totalPoints.keySet());
        globalList.sort((a, b) -> {
            int pa = totalPoints.getOrDefault(a, 0);
            int pb = totalPoints.getOrDefault(b, 0);
            if (pa != pb) return Integer.compare(pb, pa);
            return Character.compare(a, b);
        });

        if (globalList.isEmpty()) {
            System.out.println("  No valid votes recorded.");
        } else {
            for (char c : globalList) {
                System.out.printf("  %c : %d%n", c, totalPoints.getOrDefault(c, 0));
            }
        }
    }
}
