import java.util.ArrayList;
import java.util.Iterator;

/**
 * Node used to construct web results tree
 */
public class WebTreeNode {
   /**
    * Domain and port of web server
    */
   String domain = null;
   int port = 80;
   /**
    * Web Page
    */
   String page = null;
   /**
    * Search terms searched for on page
    */
   String [ ] searchTerms = null;
   /**
    * Search terms that matched
    */
   ArrayList< String > matchedSearchTerms = new ArrayList< String >( );
   /**
    * Child nodes
    */
   ArrayList< WebTreeNode > children = new ArrayList< WebTreeNode >( );

   /**
    * Add a child node
    * @param child
    */
   public void add( WebTreeNode child ) {
      children.add( child );
   }

   /**
    * @return true if this node has children
    */
   public boolean hasChildren( ) {
      return !children.isEmpty( );
   }

   /**
    * Represent the node as a string.
    */
   @Override
   public String toString( ) {
      return String.format( "<%s:%s%s %s %s>", domain, port, page,
            matchedSearchTerms, children.toString( ) );
   }

   /**
    * Constructor for WebTreeNode
    *
    * @param domain
    * @param port
    * @param page
    * @param searchTerms
    */
   public WebTreeNode( String domain, int port, String page, String [ ] searchTerms ) {
      this.domain = domain;
      this.port = port;
      this.page = page;
      this.searchTerms = searchTerms;
   }
}
