package com.samsonan.aggregator.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.samsonan.aggregator.domain.NewsItem;
import com.samsonan.aggregator.domain.ParsingRule;
import com.samsonan.aggregator.service.IntegrationService;
import com.samsonan.aggregator.service.ParsingService;
import com.samsonan.aggregator.service.exception.ParsingException;
import com.samsonan.aggregator.util.ParsingUtils;

@RunWith(MockitoJUnitRunner.class)
public class ParsingServiceTest {

	private ParsingService parser;
	
	@Mock
	private IntegrationService IntegrationService;
	
	@Before
	public void setUp() {
		parser = new ParsingService(IntegrationService);
	}
	
	@Test
	public void parseXmlOkTest() throws XMLStreamException {
	    ParsingRule rule = new ParsingRule();
	    rule.setDescriptionXPath("desc");
		String xml = "<?xml version=\"1.0\"?><root><item><title>title 1</title><desc>desc 1</desc></item><item><title>title 2</title><desc>desc 2</desc></item></root>";
		List<NewsItem> newsItems = parser.parse(xml, rule);
		
		assertEquals(newsItems.size(), 2);

		assertEquals(newsItems.get(0).getTitle(), "title 1");
        assertEquals(newsItems.get(0).getDescription(), "desc 1");
        assertEquals(newsItems.get(0).getLink(), "");

        assertEquals(newsItems.get(1).getTitle(), "title 2");
        assertEquals(newsItems.get(1).getDescription(), "desc 2");
        assertEquals(newsItems.get(1).getLink(), "");
	}
	
	@Test
	public void parseRuleOk() throws ParsingException {
	    
	    String correctRule = "{ \"itemXPath\": \"itemX\", \"descriptionXPath\": \"descriptionYYYYYY\", \"titleXPath\": \"titleZ\", \"linkXPath\": \"linkF\" }"; 
	    
	    ParsingRule rule = ParsingUtils.parseRule(correctRule);
	    
	    assertEquals(rule.getTitleXPath(), "titleZ");
        assertEquals(rule.getDescriptionXPath(), "descriptionYYYYYY");
        assertEquals(rule.getItemXPath(), "itemX");
        assertEquals(rule.getLinkXPath(), "linkF");
	    
	}
	
    @Test(expected = ParsingException.class)
    public void parseRuleError() throws ParsingException {
        
        String correctRule = "{ \"itemXPath\": \"itemX\", \"description\": \"descriptionYYYYYY\", \"titleXPath\": \"titleZ\", \"linkXPath\": \"linkF\" }"; 
        
        ParsingRule rule = ParsingUtils.parseRule(correctRule);
        
        assertEquals(rule.getTitleXPath(), "titleZ");
        assertEquals(rule.getDescriptionXPath(), "descriptionYYYYYY");
        assertEquals(rule.getItemXPath(), "itemX");
        assertEquals(rule.getLinkXPath(), "linkF");
        
    }	
    
    @Test
    public void parseRuleDefaultOk() throws ParsingException {
        
        String correctRule = "{ \"itemXPath\": \"itemX\", \"titleXPath\": \"titleZ\", \"linkXPath\": \"linkF\" }"; 
        
        ParsingRule rule = ParsingUtils.parseRule(correctRule);
        
        assertEquals(rule.getTitleXPath(), "titleZ");
        assertEquals(rule.getDescriptionXPath(), "description");
        assertEquals(rule.getItemXPath(), "itemX");
        assertEquals(rule.getLinkXPath(), "linkF");
        
    }    
	
}
