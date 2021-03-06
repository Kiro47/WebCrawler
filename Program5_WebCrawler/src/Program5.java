import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Program 5: Web Crawler
 *
 * @author Brandon Paupore, Eli Hovis, James Helm, Stephen Reynolds Last
 *         modified: 2016-12-09
 *
 */

public class Program5 extends AbstractWebCrawler {

	/**
	 * Used for blacklisted file extensions that should be ignored.
	 */
	protected List<String> blacklistedExtensions = Arrays.asList(".dtd", ".exe", ".png", ".jpg", ".gif", ".mp3", ".mpg",
			".mp4", ".mov", ".mkv", ".com&nbsp;&#8250;&#32;");

	/**
	 * Variable used to define total tree size
	 */
	protected int treeSize = 1;

	/**
	 * Set of strings used to keep track and eliminate duplicates.
	 */
	protected Set<String> visited = new HashSet<String>();

	/**
	 * Constructor
	 *
	 * @param resultsLimit
	 * @param blacklist
	 * @author James Helm,
	 */
	public Program5(int resultsLimit, String... blacklist) {
		// Variables here don't need to be redeclared, just passed on.
		super(resultsLimit, blacklist);
	}

	/**
	 * main
	 *
	 * @param args
	 *            Standard VM arguments being passed down.
	 *
	 *            Standard main method to begin any normal program.
	 *
	 * @author James Helm, Stephen Reynolds, Brandon Paupore
	 */
	public static void main(String[] args) {
		String[] blacklist = new String[] { "docs.aws.amazon.com", "dougengelbart.org", "theme.tumblr.com",
				"redbull.com", "maa.org", "teardropshop.com", "golittleguy.com", "abcl.org", "dieselsweeties.com",
				"icptrack.com", "adtechus.com", "vrdconf.com", "gdceurope.com", "gdconf.com", "webscribble.com",
				"jobs.gamasutra.com", "darkreading.com", "www.gdcvault.com", "gamecareerguide.com", "aoir.org",
				"capterra.com", "www.google.com", "academyart.edu", "bing.com" };

		Program5 prog = new Program5(50, blacklist);

		// prog.crawlWeb( "www.cs.rhodes.edu", 80, "/~kirlinp/courses/cs1/f15/",
		// "game", "animation", "java", "loop" );
		prog.crawlWeb("planet.lisp.org", 80, "/", "car", "cdr");
		// prog.crawlWeb( "www.gamasutra.com", 80, "/", "multiplayer", "game
		// design", "patterns" );
		// prog.crawlWeb("citeseerx.ist.psu.edu", 80, "/search?q=Game+Design",
		// "multiplayer", "game design", "patterns"); //FYI This link has a
		// daily limit to access
	}

	/**
	 * Returns the content of the specified web page.
	 *
	 * @param domain
	 *            - the domain of the web server. Ex: www.mtu.edu
	 * @param port
	 *            - the web server port (usually 80, Port 443 for SSL)
	 * @param page
	 *            - the page to be loaded from the web server. ex:
	 *            research/about/areas/
	 * @return - a String containing the content of the web page.
	 *
	 * @author James Helm
	 */
	@Override
	protected String getWebPage(String domain, int port, String page) {
		// Null Check
		if (domain == null || domain.equals(""))
			return null;
		// Combine to form true URL.
		domain += page;
		if (!domain.startsWith("http"))
			domain = "http://" + domain;
		// Holders
		String web = "";
		String input = "";

		// Initialize local variables.
		URL url = null;
		BufferedReader reader = null;
		// Try, because invalid URLs happen
		try {
			url = new URL(domain);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Make sure to stop if the URL is invalid.
		if (url != null) {
			// Begin reading the web page.
			//
			// Weird error where host name ends in http so can't open stream
			try {
				reader = new BufferedReader(new InputStreamReader(url.openStream()));

				// input is equal to the next line, and continues until it's
				// null.
				while ((input = reader.readLine()) != null) {
					web += input;
				}
				// Now null, so close the stream to stop memory leak.
				reader.close();
			} catch (Exception exception) {
				System.out.println(exception.toString());
			}

		}

		// Return the entire web page into a single string.
		return web;
	}

	/**
	 * Print the nodes using a preorder tree traversal. Use the following
	 * format:
	 *
	 * "%s \t http://%s%s\n", matchedSearchTerms, domain, page
	 *
	 * @param root
	 *            - print the subtree rooted at this node
	 * @author Stephen Reynolds, James Helm, Brandon Paupore
	 */
	@Override
	public void preorderTraversalPrint(WebTreeNode root) {
		System.out.println("Initial URL : " + root.domain + root.page);
		System.out.print("Search terms : [");
		for (int i = 0; i < root.searchTerms.length - 1; i++)
			System.out.print(root.searchTerms[i] + ", ");
		System.out.print(root.searchTerms[root.searchTerms.length - 1] + "]\n");
		System.out.println("Number of pages searched : " + visited.size());
		System.out.println("Total matched pages found: " + treeSize);
		System.out.println("URL's");
		printNodes(root);
		System.out.println("\nTREE\n" + root.toString());
	}

	/**
	 * printNodes
	 * 
	 * @param root
	 *            WebTreeNode to be printed.
	 * 
	 * @author Brandon Paupore
	 */
	private void printNodes(WebTreeNode root) {
		System.out.println(root.matchedSearchTerms.toString() + root.domain + root.page);
		for (WebTreeNode child : root.children)
			printNodes(child);
	}

	/**
	 * Crawl the web looking for pages containing the specified search terms.
	 * Begin at the specified domain, port, and page. Then follow links.
	 * Warning: Do not follow links to pages already visited!
	 *
	 * As you search the web construct a tree rooted at the initial page. The
	 * tree should contain no loops and no pages that do not match at least one
	 * search term.
	 *
	 * Ignore case when searching the content for the search terms.
	 *
	 * Only search until there are not more links to follow or the number of
	 * results has reached the results limit.
	 *
	 * Don't bother searching pages of the following binary file types: exe,
	 * png, jpg, gif, mp3over, mpg, mp4, mov
	 *
	 * After the search is complete, print the following statistics:
	 *
	 * Initial URL Search Terms Number of Pages Searched Number of Pages Found
	 * containing Search Terms Matches and URLs (via the preorderTraversalPrint)
	 * Root Node
	 *
	 * @param domain
	 *            - initial web server domain
	 * @param port
	 *            - web port (usually 80)
	 * @param page
	 *            - initial page
	 * @param searchTerms
	 *            an array of terms to search for.
	 *
	 * @returns The tree rooted at the specified start page and containing only
	 *          pages that contain one or more of the search terms.
	 * @author Stephen Reynolds, James Helm, Brandon Paupore
	 */
	@Override
	public WebTreeNode crawlWeb(String domain, int port, String page, String... searchTerms) {
		WebTreeNode root = new WebTreeNode(domain, port, page, searchTerms);
		// Crawl until results limit is reached.
		while (treeSize < resultsLimit) {
			// Ensure domain has http protocol.
			if (root.domain != null && !root.domain.isEmpty()) {

				if (!root.domain.startsWith("http://") && !root.domain.startsWith("https://")) {

					root.domain = "http://" + root.domain;

				}
				visited.add(root.domain + root.page);
				String webContent = getWebPage(domain, port, page);
				root.matchedSearchTerms = hasTerms(webContent, searchTerms);
				List<String> links = getLinks(webContent);
				// Add links to parent node.
				for (String s : links) {
					WebTreeNode current = new WebTreeNode(getDomain(s), port, getPage(s), searchTerms);
					if (treeSize >= resultsLimit)
						break;
					if (current.domain != null && !current.domain.isEmpty())
						if (!current.domain.startsWith("http://") && !current.domain.startsWith("https://"))
							current.domain = "http://" + current.domain;
					if (!blacklist.contains(current.domain)) {
						if (visited.add(current.domain + current.page)) {
							webContent = getWebPage(current.domain, current.port, current.page);
							current.matchedSearchTerms = hasTerms(webContent, searchTerms);
							if (current.matchedSearchTerms.size() > 0) {
								printStatus();
								root.add(current);
								treeSize++;
							}
						}
					}
					// Bad because builds tree weird hugging left side i.e. A
					// FGHI = children of B : JKLM = children of F B/ \C\D\E
					// F/ \G\H\I
					// J/ \K\L\M
				}
				for (WebTreeNode wtn : root.children) {
					if (treeSize < resultsLimit)
						crawlWeb(wtn, visited, searchTerms);
				}
				System.out.println("Done!");
				break;
			}
		}
		preorderTraversalPrint(root);
		visited.clear();
		treeSize = 1;
		return root;
	}

	/**
	 * printStatus()
	 * 
	 * Prints percentage of completion.
	 * 
	 * @author James Helm
	 */
	private void printStatus() {
		System.out.println((int) (((double) treeSize / (double) resultsLimit) * 100) + "%");
	}

	/**
	 * Crawl the web looking for pages containing the specified search terms.
	 * 
	 * @param WebTreeNode
	 *            node to search for links in
	 * @param Set<String>
	 *            pages visited
	 * @param searchTerms
	 * @author Brandon Paupore
	 */
	public void crawlWeb(WebTreeNode root, Set<String> visited, String... searchTerms) {
		// Crawl until results limit is reached.
		// Ensure domain has http protocol.
		if (root.domain != null && !root.domain.isEmpty())
			if (!root.domain.startsWith("http://") && !root.domain.startsWith("https://"))
				root.domain = "http://" + root.domain;

		String webContent = getWebPage(root.domain, root.port, root.page);
		List<String> links = getLinks(webContent);
		// Add links to parent node.
		for (String s : links) {
			if (treeSize >= resultsLimit)
				break;
			WebTreeNode current = new WebTreeNode(getDomain(s), root.port, getPage(s), searchTerms);
			if (current.domain != null && !current.domain.isEmpty())
				if (!current.domain.startsWith("http://") && !current.domain.startsWith("https://"))
					current.domain = "http://" + current.domain;
			if (!blacklist.contains(current.domain)) {
				if (visited.add(current.domain + current.page)) {
					webContent = getWebPage(current.domain, current.port, current.page);
					if (webContent != null) {
						current.matchedSearchTerms = hasTerms(webContent, searchTerms);
						if (current.matchedSearchTerms.size() > 0) {
							printStatus();
							root.add(current);
							treeSize++;
						}
					}
				}
			} // Crawls web over root's children
		}
	}

	/**
	 * Returns the domain of a url
	 *
	 * @param url
	 * @return String
	 * @author Stephen Reynolds, Brandon Paupore
	 */
	private String getDomain(String url) {
		if (url == null || url.equals(""))
			return "";
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			try {
				uri = new URI("");
			} catch (URISyntaxException f) {
				f.printStackTrace();
			}
			e.printStackTrace();
		}
		return uri.getHost();
	}

	/**
	 * Returns the page of a url
	 *
	 * @param url
	 * @return String
	 * @author Stephen Reynolds, Brandon Paupore
	 */
	private String getPage(String url) {
		String domain = getDomain(url);
		// Null Check
		if (domain == null || domain.isEmpty()) {
			url = "";
		}
		// If it's a home page it would return
		else {
			url = url.replace("http://" + domain, "");
		}
		return url;
	}

	/**
	 * Return a list of urls for each link in a given web page.
	 *
	 * @param html
	 *            search for links here
	 * @return list of urls
	 * @author Stephen Reynolds, James Helm, Brandon Paupore
	 */
	private List<String> getLinks(String html) {

		List<String> links = new LinkedList<String>();
		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);

		while (matcher.find()) {
			String linkUrl = matcher.group();
			if (linkUrl.startsWith("(") && linkUrl.endsWith(")")) {
				linkUrl = linkUrl.substring(1, linkUrl.length() - 1);
			}
			// If the linkURL extension isn't blacklisted add it.
			// Accounts for one and two letter extensions as well.
			if (linkUrl != null && !linkUrl.isEmpty())
				if (linkUrl.contains("."))
					if (!blacklistedExtensions.contains(linkUrl.substring(linkUrl.lastIndexOf('.'))))
						links.add(linkUrl);

		}
		return links;
	}

	/**
	 * gets HashSet of searchTerms on the page
	 * 
	 * @param file
	 *            to be searched
	 * @param searchTerms
	 *            terms to search for
	 * @return HashSet terms found, empty if none found
	 * @author Brandon Paupore
	 */
	private ArrayList<String> hasTerms(String html, String... searchTerms) {
		ArrayList<String> matchedTerms = new ArrayList<String>();
		if (html == null)
			return matchedTerms;
		// Tests if page has search terms within
		for (String s : searchTerms)
			if (html.contains(s))
				matchedTerms.add(s);
		return matchedTerms;
	}
}
