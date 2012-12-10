package unit;

import org.junit.*;
import play.test.*;

import models.*;

public class TopicTest extends UnitTest {

    @Before
    public void setUpData() {
        Fixtures.deleteAll();
        Fixtures.load("test-data.yml");
    }

    @Test
    public void countObjects() {
        assertEquals(4, Problem.count());
    }

    @Test
    public void tryHelpTopic() {
        Problem help = Problem.find("bySubject", "I need help !").first();
        assertNotNull(help);
        assertEquals(3, help.posts.size());
        assertEquals(3L, (long) help.getPostsCount());
        assertEquals(2L, (long) help.getVoicesCount());
        assertEquals("It's ok for me ...", help.getLastPost().content);
        assertEquals("Play help", help.forum.name);
    }

    @Test
    public void newTopic() {
        Textbook test = new Textbook("Test", "Yop");
        User guillaume = User.find("byName", "Guillaume").first();
        test.newTopic(guillaume, "Hello", "Yop ...");
        assertEquals(2L, (long) guillaume.getTopicsCount());
        assertEquals(1, test.topics.size());
        assertEquals(1, test.getTopicsCount());
        assertEquals(5, Problem.count());
    }

    @Test
    public void testCascadeDelete() {
        Textbook help = Textbook.find("byName", "Play help").first();
        assertEquals(4, Problem.count());
        assertEquals(7, Post.count());
        help.delete();
        assertEquals(1, Problem.count());
        assertEquals(1, Post.count());
        User guillaume = User.find("byName", "Guillaume").first();
        assertEquals(0L, (long) guillaume.getTopicsCount());
    }
}