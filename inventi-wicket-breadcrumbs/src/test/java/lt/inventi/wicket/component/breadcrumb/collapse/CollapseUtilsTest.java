package lt.inventi.wicket.component.breadcrumb.collapse;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.BEquality;

public class CollapseUtilsTest {
    BEquality<String> sameness = new BEquality<String>() {
        @Override
        public boolean equal(String a, String b) {
            return a.equals(b);
        }
    };
    BEquality<String> collapsibility = new BEquality<String>() {
        @Override
        public boolean equal(String a, String b) {
            return a.split(":")[0].equals(b.split(":")[0]);
        }
    };
    BFun<String, String> identity = new BFun<String, String>() {
        @Override
        public String apply(String f) {
            return f;
        }
    };
    BFun<Iterable<String>, String> grouping = new BFun<Iterable<String>, String>() {
        @Override
        public String apply(Iterable<String> f) {
            StringBuilder sb = new StringBuilder();
            for (String s : f) {
                sb.append(s).append("-");
            }
            return sb.toString();
        }
    };

    @Test
    public void collapsesFirstRepetition() {
        List<String> result = doCollapse(Arrays.asList("a:1"), 1);
        assertThat(result, contains("a:1"));

        result = doCollapse(Arrays.asList("a:1", "b:1"), 1);
        assertThat(result, contains("a:1", "b:1"));

        result = doCollapse(Arrays.asList("a:1", "b:1", "c:1", "a:2"), 1);
        assertThat(result, contains("a:1-b:1-c:1-", "a:2"));

        result = doCollapse(Arrays.asList("z:1", "a:1", "b:1", "c:1", "b:2", "a:2"), 1);
        assertThat(result, contains("z:1", "a:1-b:1-c:1-b:2-", "a:2"));

        result = doCollapse(Arrays.asList("z:1", "a:1", "b:1", "a:2", "b:2", "a:3"), 1);
        assertThat(result, contains("z:1", "a:1-b:1-a:2-b:2-", "a:3"));
    }

    @Test
    public void collapsesSecondRepetition() {
        List<String> result = doCollapse(Arrays.asList("a:1"), 2);
        assertThat(result, contains("a:1"));

        result = doCollapse(Arrays.asList("a:1", "b:1"), 2);
        assertThat(result, contains("a:1", "b:1"));

        result = doCollapse(Arrays.asList("a:1", "b:1", "c:1", "a:2"), 2);
        assertThat(result, contains("a:1", "b:1", "c:1", "a:2"));

        result = doCollapse(Arrays.asList("a:1", "b:1", "c:1", "a:2", "a:3"), 2);
        assertThat(result, contains("a:1-b:1-c:1-a:2-", "a:3"));

        result = doCollapse(Arrays.asList("x:1", "z:1", "a:1", "b:1", "c:1", "b:2", "b:3", "c:2", "z:2", "a:2", "b:4", "a:3"), 2);
        assertThat(result, contains("x:1", "z:1", "a:1-b:1-c:1-b:2-b:3-c:2-z:2-a:2-b:4-", "a:3"));
    }

    /**
     * Input cannot contain items equal according to the {@code sameness}
     * predicate.
     */
    @Test
    public void doesntCollapseImpossibleScenarios() {
        List<String> result = doCollapse(Arrays.asList("a:1", "b:1", "a:1"), 1);
        assertThat(result, contains("a:1", "b:1", "a:1"));

        result = doCollapse(Arrays.asList("a:1", "b:1", "a:1"), 5);
        assertThat(result, contains("a:1", "b:1", "a:1"));
    }

    private List<String> doCollapse(List<String> data, int times) {
        return CollapseUtils.collapse(data, sameness, collapsibility, identity, grouping, times);
    }
}
