package com.samsonan.aggregator.web;

import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samsonan.aggregator.domain.Feed;
import com.samsonan.aggregator.domain.NewsItem;
import com.samsonan.aggregator.domain.User;
import com.samsonan.aggregator.dto.FeedForm;
import com.samsonan.aggregator.service.FeedService;
import com.samsonan.aggregator.service.SyncService;
import com.samsonan.aggregator.service.exception.ParsingException;
import com.samsonan.aggregator.service.exception.RemoteNotFoundException;
import com.samsonan.aggregator.util.ParsingUtils;

@Controller
public class FeedController {

    private static Logger log = LoggerFactory.getLogger(FeedController.class);

    private final FeedService feedService;
    private final SyncService syncService;

    @Autowired
    public FeedController(FeedService feedService, SyncService syncService) {
        this.feedService = feedService;
        this.syncService = syncService;
    }

    @GetMapping({ "/feeds" })
    public String listFeeds(Model model) {

        populateFeeds(model);
        
        return "feed/list";
    }
    
    @GetMapping({ "", "/", "/news" })
    public String listNews(@RequestParam(name = "filter", required = false) String filter, Model model) {

        populateNews(filter, model);
        
        return "news/list";
    }

    /**
     * This method is used when we apply filters - we GET the PART (fragment) of page content as HTML using ajax and simply replacing the data on the page.
     * The page header/footer and filters are not affected. 
     */
    @GetMapping({ "/news/fragment" })
    public String requestFragment(@RequestParam(name = "filter", required = false) String filter, Model model) {

        populateNews(filter, model);
        
        return "news/list :: news_table_full";
    }    
    
    /* Add Feed */

    @GetMapping("/feeds/add")
    public String addFeed(Model model) {

        model.addAttribute("feedForm", new FeedForm());

        return "feed/add";
    }

    @PostMapping("/feeds/add")
    public String submitAddFeed(@Valid FeedForm feed, BindingResult bindingResult, Model model,
            final RedirectAttributes redirectAttributes) {

        log.debug("new feed submitted: {}", feed);

        if (bindingResult.hasErrors()) {
            return "feed/add";
        }

        try {
            if (!feed.getRule().isEmpty()) {
                // TODO: there should be more checks than just valid JSON check
                ParsingUtils.parseRule(feed.getRule());
            }
        } catch (ParsingException e) {
            model.addAttribute("error", "message.rule.failed");
            return "feed/add";
        }
        
        Feed feedAdded = feedService.addFeed(feed);
        syncService.syncSingleFeedAsync(feedAdded);
        
        redirectAttributes.addFlashAttribute("success", "message.success.feed.added");

        return "redirect:/feeds";
    }

    @DeleteMapping("/feeds/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCounter(@PathVariable("id") Long feedId) {
        try {
            Feed feed = feedService.getFeedById(feedId);
            
            if (feed == null || !isHasPermission(feed)) {
                throw new RemoteNotFoundException();
            }

            feedService.deleteFeed(feed);
            return ResponseEntity.ok().build();
        } catch (RemoteNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    
    private void populateNews(String filter, Model model) {

        filter = (filter == null || filter.isEmpty())? null : filter;
        
        List<NewsItem> news;

        if (isAdmin()) {
            news = feedService.getAllNews(filter);
        } else {
            news = feedService.getAllNewsForUser(getCurrentUserLogin(), filter);
        }

        model.addAttribute("news", news);
    }

    private void populateFeeds(Model model) {

        List<Feed> feeds;

        if (isAdmin()) {
            feeds = feedService.getAllFeeds();
        } else {
            feeds = feedService.getAllFeedsForUser(getCurrentUserLogin());
        }

        model.addAttribute("feeds", feeds);
    }    
    
    protected String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(a -> a.getAuthority()).anyMatch(s -> s.equals(User.ROLE_ADMIN));
    }    
    
    /**
     * If the current user has permissions on given feed
     */
    protected boolean isHasPermission(Feed feed) {
        return isAdmin() || getCurrentUserLogin().equals(feed.getOwner());
    }    
    

}
