package lt.inventi.wicket.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * @author ogrigas, vplatonov
 */
public abstract class FuzzyComponentResolverUtils {

    private FuzzyComponentResolverUtils() {
        // static utils
    }

    /**
     * Inside the given {@code container}, looks for component whose id equals
     * {@code path} or whose path ends with {@code path} with optional
     * intermediaries in the component tree. Search is restricted to the
     * specified {@code componentType}.
     *
     * @return the matching component's path, relative to {@code container}
     * @throws IllegalArgumentException
     *             if it finds zero or more than one macthing component.
     */
    public static <T> String findComponentPath(MarkupContainer container, String path, Class<T> componentType) {
        /*
         * T isn't constrained to the Component hierarchy as sometimes we might be searching using
         * an interface such as an IFormSubmitter.
         */
        Component component = container.get(path);
        if (component != null && componentType.isInstance(component)) {
            return path;
        }
        T fuzzyMatch = searchComponentTree(container, path, componentType);
        return getComponentRelativePath(container, assureFuzzyMatchIsAComponent(container, path, fuzzyMatch));
    }

    /**
     * Inside the given {@code container}, looks for component whose id equals
     * {@code path} or whose path ends with {@code path} with optional
     * intermediaries in the component tree. Search is restricted to the
     * specified {@code componentType}.
     *
     * @return the matching component
     * @throws IllegalArgumentException
     *             if it finds zero or more than one macthing component.
     */
    public static <T> T findComponent(MarkupContainer container, String path, Class<T> componentType) {
        Component component = container.get(path);
        if (component != null && componentType.isInstance(component)) {
            return componentType.cast(component);
        }
        T fuzzyMatch = searchComponentTree(container, path, componentType);
        assureFuzzyMatchIsAComponent(container, path, fuzzyMatch);
        return fuzzyMatch;
    }

    private static <T> Component assureFuzzyMatchIsAComponent(MarkupContainer container, String path, T fuzzyMatch) {
        if (!(fuzzyMatch instanceof Component)) {
            throw new IllegalStateException("Found a non-component " + fuzzyMatch + " in the " + container + " using '" + path + "' path!");
        }
        return (Component) fuzzyMatch;
    }

    /**
     * Returns the {@code component}'s path relative to {@code container}.
     */
    private static String getComponentRelativePath(MarkupContainer container, Component component) {
        return component.getPath().replaceFirst(container.getPath() + ":", "");
    }

    private static <T> T searchComponentTree(MarkupContainer container, String path, Class<T> componentType) {
        PathMatchingVisitor<T> visitor = new PathMatchingVisitor<T>(path, componentType);
        container.visitChildren(componentType, visitor);

        if (visitor.primaryCandidates.size() == 1) {
            return visitor.primaryCandidates.iterator().next();
        }
        if (visitor.primaryCandidates.size() > 1) {
            throw multipleCandidatesException("primary", visitor.primaryCandidates, container, path, componentType);
        }

        return selectSecondaryCandidate(container, path, componentType, visitor.secondaryCandidates);
    }

    private static <T> T selectSecondaryCandidate(MarkupContainer container, String path,
        Class<T> componentType, NavigableMap<Match, Set<T>> candidates) {

        if (candidates.isEmpty()) {
            throw noCandidatesException(container, path, componentType);
        }

        Match last = candidates.lastKey();
        SortedMap<Match, Set<T>> bestMatches = candidates.tailMap(Match.lowestFor(last.distance));
        if (bestMatches.size() > 1) {
            throw multipleCandidatesException("secondary", concat(bestMatches.values()), container, path, componentType);
        }
        Set<T> equalMatches = bestMatches.values().iterator().next();
        if (equalMatches.size() == 1) {
            return equalMatches.iterator().next();
        }
        throw multipleCandidatesException("secondary", equalMatches, container, path, componentType);
    }

    private static <C extends Iterable<T>, T> Iterable<T> concat(Collection<C> xxs) {
        List<T> result = new LinkedList<T>();
        for (Iterable<T> xs: xxs) {
            for (T x: xs) {
                result.add(x);
            }
        }
        return result;
    }

    private static Match calculateMatch(String[] pathToMatch, String[] searchPath) {
        // Find the first element in the current component's path which is
        // equal to the first element in the search path. If no such element exists we
        // won't consider the path at all.
        int firstMatchPosition = 0;
        while (firstMatchPosition < pathToMatch.length &&
            !pathToMatch[firstMatchPosition].equals(searchPath[0])) {

            firstMatchPosition++;
        }
        if (pathToMatch.length == firstMatchPosition) {
            return null;
        }

        String[] toMatch = Arrays.copyOfRange(pathToMatch, firstMatchPosition, pathToMatch.length);
        int numPartsMatched = 0;
        for (int i = 0, j = 0; i < toMatch.length && j < searchPath.length; i++) {
            if (toMatch[i].equals(searchPath[j])) {
                numPartsMatched++;
                j++;
            }
        }
        if (numPartsMatched < searchPath.length) {
            return null;
        }
        return new Match(levenshteinDistance(toMatch, searchPath), toMatch.length);
    }

    /**
     * Straight from Wikipedia
     */
    private static int levenshteinDistance(String[] a, String[] b) {
        int[][] distance = new int[a.length + 1][b.length + 1];

        for (int i = 0; i <= a.length; i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= b.length; j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= a.length; i++) {
            for (int j = 1; j <= b.length; j++) {
                distance[i][j] = minimum(
                    distance[i - 1][j] + 1,
                    distance[i][j - 1] + 1,
                    distance[i - 1][j - 1] + ((a[i - 1].equals(b[j - 1])) ? 0 : 1));
            }
        }

        return distance[a.length][b.length];
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private static class Match implements Comparable<Match> {

        static Match lowestFor(int distance) {
            return new Match(distance, Integer.MIN_VALUE);
        }

        final int distance;
        /**
         * Probably should be removed.
         */
        final int matchLength;

        public Match(int distance, int length) {
            this.distance = distance;
            this.matchLength = length;
        }

        @Override
        public int compareTo(Match o) {
            if (this == o) {
                return 0;
            }

            int distanceResult = compare(o.distance, distance); // the lower, the better
            int matchLengthResult = compare(matchLength, o.matchLength); // the higher, the better

            return distanceResult == 0 ? matchLengthResult : distanceResult;
        }

        private static int compare(int a, int b) {
            return (a < b) ? -1 : ((a > b) ? 1 : 0);
        }

        @Override
        public String toString() {
            return "Match(dst: " + distance + ", mlen:" + matchLength + ")";
        }
    }

    private static IllegalArgumentException noCandidatesException(MarkupContainer container, String path, Class<?> componentType) {
        return new IllegalArgumentException(
            String.format("No %s found with path matching '%s' inside %s (container path: '%s')",
                Classes.simpleName(componentType), path, Classes.simpleName(container.getClass()), container.getPath()));
    }

    private static <T> IllegalArgumentException multipleCandidatesException(String name,
        Iterable<T> candidates, MarkupContainer container, String path, Class<T> componentType) {

        StringBuilder message = new StringBuilder()
            .append(String.format("Multiple %s %ss found with path matching '%s' inside %s (container path: '%s'). ",
                name, Classes.simpleName(componentType), path, Classes.simpleName(container.getClass()), container.getPath()))
            .append("Possible candidates:");
        for (T c : candidates) {
            message.append("\n    ");
            if (c instanceof Component) {
                message.append(((Component) c).getPath());
            } else {
                message.append(c.toString());
            }
        }
        throw new IllegalArgumentException(message.toString());
    }

    private static class PathMatchingVisitor<T> implements IVisitor<Component, Void> {
        private static Pattern SPLIT_PATTERN = Pattern.compile(":");

        private final String searchPath;
        private final String[] searchPathParts;
        private final Class<T> componentType;

        public final Set<T> primaryCandidates = new HashSet<T>();
        public final NavigableMap<Match, Set<T>> secondaryCandidates = new TreeMap<Match, Set<T>>();

        public PathMatchingVisitor(String searchPath, Class<T> componentType) {
            this.searchPath = searchPath;
            this.searchPathParts = SPLIT_PATTERN.split(searchPath);
            this.componentType = componentType;
        }

        @Override
        public void component(Component c, IVisit<Void> visit) {
            String cPath = c.getPath();

            boolean idMatches = c.getId().equals(searchPath) || cPath.endsWith(":" + searchPath);
            if (componentType.isAssignableFrom(c.getClass()) && idMatches) {
                primaryCandidates.add(componentType.cast(c));
            } else {
                String[] pathToMatch = SPLIT_PATTERN.split(cPath);
                if (pathToMatch.length > searchPathParts.length) {
                    Match match = calculateMatch(pathToMatch, searchPathParts);
                    if (match != null) {
                        Set<T> matches = secondaryCandidates.get(match);
                        if (matches == null) {
                            matches = new HashSet<T>();
                        }
                        matches.add(componentType.cast(c));
                        secondaryCandidates.put(match, matches);
                    }
                }
            }
        }
    }
}
