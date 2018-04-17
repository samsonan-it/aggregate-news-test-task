package com.samsonan.aggregator.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.samsonan.aggregator.domain.Feed;
import com.samsonan.aggregator.domain.NewsItem;
import com.samsonan.aggregator.domain.ParsingRule;
import com.samsonan.aggregator.util.ParsingUtils;

/**
 * All operations related to parsing news feeds - xml or xhtml
 */
@Service
public class ParsingService {

    private static Logger log = LoggerFactory.getLogger(ParsingService.class);

    private final IntegrationService integrationService;

    @Autowired
    public ParsingService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    public List<NewsItem> parse(Feed feed) {
        try {
            DocumentBuilder builder = getDocumentBuilder();
            Document xmlDocument = builder.parse(integrationService.read(feed.getUrl()));

            return parse(xmlDocument, ParsingUtils.parseRule(feed.getParseRule()), feed);

        } catch (Exception e) {
            log.error("Error Parsing Feed {}", feed, e);
            // TODO: audit log
            return Collections.emptyList();
        }
    }

    /**
     * For Unit Tests
     */
    public List<NewsItem> parse(String xmlString, ParsingRule rule) {
        try {
            DocumentBuilder builder = getDocumentBuilder();

            InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());
            Document xmlDocument = builder.parse(xmlStream);

            return parse(xmlDocument, rule, null);
            
        } catch (Exception e) {
            log.error("Error Parsing String {}", xmlString, e);
            return Collections.emptyList();
        }
    }

    private List<NewsItem> parse(Document xmlDocument, ParsingRule rule, Feed feed) throws XPathExpressionException {

        List<NewsItem> feedNews = new LinkedList<>();
        
        XPath xPath = XPathFactory.newInstance().newXPath();
        
        XPathExpression newsItemRootXp = xPath.compile(rule.getItemXPath());
        
        XPathExpression titleXp = xPath.compile(rule.getTitleXPath());
        XPathExpression descXp = xPath.compile(rule.getDescriptionXPath());
        XPathExpression linkXp = xPath.compile(rule.getLinkXPath());
        
        NodeList newsItemNodes = (NodeList) newsItemRootXp.evaluate(xmlDocument, XPathConstants.NODESET);

        log.debug("news item nodes = {}", newsItemNodes.getLength());

        for (int i = 0; i < newsItemNodes.getLength(); i++) {
            
            Element newsItemElement = (Element) newsItemNodes.item(i);
            String title = (String) titleXp.evaluate(newsItemElement, XPathConstants.STRING);
            String description = (String) descXp.evaluate(newsItemElement, XPathConstants.STRING);
            String link = (String) linkXp.evaluate(newsItemElement, XPathConstants.STRING);

            log.debug("i={}; title={}; desc={}; link={}", i, title, description, link);

            if (StringUtils.isEmpty(title)) { // it is checked on DB level, but I think it makes sense to also check it here
                continue;
            }
            
            feedNews.add(new NewsItem(title, description, link, feed));
        }
        
        return feedNews;

    }

    // make it a bean???
    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        return builderFactory.newDocumentBuilder();
    }

}
