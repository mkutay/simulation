

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class CommentTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CommentTest
{
    /**
     * Default constructor for test class CommentTest
     */
    public CommentTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
    
    @Test
    public void commentDetailsInitialised() {
        String author = "someone";
        String text = "text";
        int rating = 1;
        Comment c = new Comment(author, text, rating);
        assertEquals(true, c.getAuthor().equals(author));
        assertEquals(true, c.getRating() == rating);
    }
    
    @Test
    public void commentVoteWorks() {
        Comment c = new Comment("someone", "text", 1);
        assertEquals(true, c.getVoteCount() == 0);
        c.upvote();
        assertEquals(true, c.getVoteCount() == 1);
        c.upvote();
        assertEquals(true, c.getVoteCount() == 2);
        c.downvote();
        c.downvote();
        assertEquals(true, c.getVoteCount() == 0);
        c.downvote();
        assertEquals(true, c.getVoteCount() == -1);
    }
}
