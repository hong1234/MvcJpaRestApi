package com.hong.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.lang.String;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.apache.commons.codec.binary.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.hong.demo.domain.Book;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MvcJpaRestApiApplicationTests
{
	@LocalServerPort
	private int port;
	
	RestTemplate restTemplate = new RestTemplate();

        /*
     	* Add HTTP Authorization header, using Basic-Authentication to send user-credentials.
     	*/
    	private static HttpHeaders getHeaders(){
    		String plainCredentials="admin:password";
    		String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
    	
    		HttpHeaders headers = new HttpHeaders();
                //headers.setContentType(MediaType.APPLICATION_JSON);
    		headers.add("Authorization", "Basic " + base64Credentials);
    		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    		return headers;
    	}
	
	private String getRootUrl(){
		return "http://localhost:"+port;
	}
	
	@Test
	public void testGetAllBooks() 
	{	
                HttpEntity<String> request = new HttpEntity<String>(getHeaders());
                ResponseEntity<Book[]> response = restTemplate.exchange(getRootUrl()+"/api/books", HttpMethod.GET, request, Book[].class);
                List<Book> books = Arrays.asList(response.getBody());
        	assertNotNull(books);
	}
        
   
	@Test
	public void testGetBookById() 
	{
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
                ResponseEntity<Book> response = restTemplate.exchange(getRootUrl()+"/api/books/1", HttpMethod.GET, request, Book.class);
        	assertNotNull(response.getBody());
	}

       
	@Test
	public void testCreateBook() 
	{
		Book book = new Book();
		book.setTitle("Exploring SpringBoot REST");
		book.setContent("SpringBoot is awesome!!");
		book.setCreatedOn(new Date());
		
                HttpEntity<Object> request = new HttpEntity<Object>(book, getHeaders());
                ResponseEntity<Book> bookResponse = restTemplate.exchange(getRootUrl()+"/api/books", HttpMethod.POST, request, Book.class);
        	assertNotNull(bookResponse);
        	assertNotNull(bookResponse.getBody());	
	}

	
	@Test
	public void testUpdateBook() 
	{
		int id = 1;
                Book book = new Book();
                book.setId(id);
                book.setTitle("UPDATE");
		book.setContent("UPDATE");
		book.setCreatedOn(new Date());

                HttpEntity<Object> request = new HttpEntity<Object>(book, getHeaders());
		ResponseEntity<Book> response = restTemplate.exchange(getRootUrl()+"/api/books/"+id, HttpMethod.PUT, request, Book.class);
                assertNotNull(response.getBody());
	}
        
	
	@Test
	public void testDeleteBook() 
	{
		int id = 2;
                HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        	ResponseEntity<String> response = restTemplate.exchange(getRootUrl()+"/api/books/"+id, HttpMethod.DELETE, request, String.class);
                assertNull(response.getBody());
	}
        
}
