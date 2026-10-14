package navigation;
import com.intuit.karate.junit5.Karate;
public class NavigationRunner {
    @Karate.Test
    Karate navigation() { return Karate.run("navigation.feature").tags("~@planned").relativeTo(getClass()); }
}
