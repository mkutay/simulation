import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class SalesItemTest.
 *
 * @author  mik
 * @version 0.1
 */
public class SalesItemTest
{
    /**
     * Default constructor for test class SalesItemTest
     */
    public SalesItemTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test that a comment can be added, and that the comment count is correct afterwards.
     */
    @Test
    public void testAddComment()
    {
        SalesItem salesIte1 = new SalesItem("Brain surgery for Dummies", 21998);
        assertEquals(true, salesIte1.addComment("James Duckling", "This book is great. I perform brain surgery every week now.", 4));
        assertEquals(1, salesIte1.getNumberOfComments());
    }
    
    @Test
    public void testAddCommentSameAuthor() {
        SalesItem saleItem1 = new SalesItem("test", 1232);
        assertEquals(true, saleItem1.addComment("a", "a", 1));
        assertEquals(false, saleItem1.addComment("a", "a", 1));
    }

    /**
     * Test that a comment using an illegal rating value is rejected.
     */
    @Test
    public void testIllegalRating()
    {
        SalesItem salesIte1 = new SalesItem("Java For Complete Idiots, Vol 2", 19900);
        assertEquals(false, salesIte1.addComment("Joshua Black", "Not worth the money. The font is too small.", -5));
        assertEquals(false, salesIte1.addComment("ad", "Not worth the money. The font is too small.", 0));
        assertEquals(false, salesIte1.addComment("a123", "Not worth the money. The font is too small.", 6));
    }

    /**
     * Test that a sales item is correctly initialised (name and price).
     */
    @Test
    public void testInit()
    {
        SalesItem salesIte1 = new SalesItem("test name", 1000);
        assertEquals("test name", salesIte1.getName());
        assertEquals(1000, salesIte1.getPrice());
    }

    @Test
    public void addComment()
    {
        SalesItem salesIte1 = new SalesItem("Brain Surgery for Dummies.", 9899);
        assertEquals(true, salesIte1.addComment("Fred", "Great - I perform brain surgery every week now!", 4));
    }
    
    @Test
    public void mostHelpfulComment() {
        SalesItem s = new SalesItem("test", 1232);
        s.addComment("a", "a", 2);
        s.addComment("b", "b", 3);
        s.upvoteComment(0);
        s.upvoteComment(0);
        s.downvoteComment(1);
        Comment c = s.findMostHelpfulComment();
        assertEquals(true, c.getAuthor().equals("a"));
        assertEquals(true, c.getRating() == 2);
    }
}



