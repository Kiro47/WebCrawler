/**
 * 
 * @author 
 *
 */

public class Program5 extends AbstractWebCrawler{

	public Program5( int resultsLimit, String ... blacklist ) {
  	super(int resultsLimit, String ... blacklist);
  }

	public static void main(String[] args) {

	}

	/**
	* Returns the content of the specified web page.
	*
	* To do this, open a socket for the specified domain and port.
	* Then send the following commands:
	*
	* "GET " + page + " HTTP/1.0\r\n"
	* "HOST: " + domain + "\r\n"
	* "CONNECTION: close\r\n"
	* "\r\n"
	*
	* Don't forget to flush the output stream.
	*
	* Then read and return the incoming data from the socket.
	*
	* @param domain - the domain of the web server.
	* @param port - the web server port (usually 80)
	* @param page - the page to be loaded from the web server.
	* @return - a String containing the content of the web page.
	*/
	@Override
	protected String getWebPage(String domain, int port, String page) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
  * Print the nodes using a preorder tree traversal.
  * Use the following format:
  *
  * "%s \t http://%s%s\n", matchedSearchTerms, domain, page
  *
  * @param root - print the subtree rooted at this node
  */
	@Override
	public void preorderTraversalPrint(WebTreeNode root) {
		// TODO Auto-generated method stub

	}

	/**
	* Crawl the web looking for pages containing the specified search terms.
	* Begin at the specified domain, port, and page. Then follow links.
	* Warning: Do not follow links to pages already visited!
	*
	* As you search the web construct a tree rooted at the initial page.
	* The tree should contain no loops and no pages that do not match at
	* least one search term.
	*
	* Ignore case when searching the content for the search terms.
	*
	* Only search until there are not more links to follow or the number
	* of results has reached the results limit.
	*
	* Don't bother searching pages of the following binary file types:
	* exe, png, jpg, gif, mp3, mpg, mp4, mov
	*
	* After the search is complete, print the following statistics:
	*
	* Initial URL
	* Search Terms
	* Number of Pages Searched
	* Number of Pages Found containing Search Terms
	* Matches and URLs (via the preorderTraversalPrint)
	* Root Node
	*
	* @param domain - initial web server domain
	* @param port - web port (usually 80)
	* @param page - initial page
	* @param searchTerms an array of terms to search for.
	*
	* @returns The tree rooted at the specified start page and containing
	* only pages that contain one or more of the search terms.
	*/
	@Override
	public WebTreeNode crawlWeb(String domain, int port, String page, String... searchTerms) {
		// TODO Auto-generated method stub
		return null;
	}
}
