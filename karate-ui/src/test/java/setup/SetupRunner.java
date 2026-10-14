package setup;
import com.intuit.karate.junit5.Karate;
public class SetupRunner {
    @Karate.Test
    Karate setup() { return Karate.run("browser-smoke.feature").relativeTo(getClass()); }
}